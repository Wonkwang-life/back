# 배포 (CI/CD) 가이드

`master` 에 push 하면 GitHub Actions 가 자동으로 빌드 → EC2 전송 → `systemd` 재시작 → 헬스체크까지 수행한다.
실패하면 이전 jar 로 자동 롤백한다.

파이프라인 정의: [`.github/workflows/deploy.yml`](../.github/workflows/deploy.yml)

---

## 1. GitHub Secrets 등록 (최초 1회 · repo 관리자가 직접)

repo → **Settings → Secrets and variables → Actions → New repository secret** 에서 아래 3개를 등록한다.

| 이름 | 값 |
|---|---|
| `EC2_HOST` | `ec2-43-203-209-58.ap-northeast-2.compute.amazonaws.com` |
| `EC2_SSH_KEY` | `wkhealth.pem` **파일 내용 전체** (`-----BEGIN ... KEY-----` 부터 끝까지) |
| `APPLICATION_YML` | `src/main/resources/application.yml` **파일 내용 전체** |

> ⚠️ `application.yml` 에는 RDS 비밀번호·AWS 키·Discord 웹훅이 들어있다. Secret 은 안전하지만,
> 이 값들이 과거에 외부로 노출된 적이 있다면 이 기회에 **AWS 키/RDS 비번 로테이션**을 권장한다.

---

## 2. EC2 서버 최초 설정 (systemd · 최초 1회)

`nohup` 대신 `systemd` 로 앱을 관리한다 → 크래시/재부팅 시 자동 재시작, `systemctl restart` 로 배포.

```bash
# (로컬) 유닛 파일 업로드
scp -i wkhealth.pem deploy/health.service \
  ubuntu@ec2-43-203-209-58.ap-northeast-2.compute.amazonaws.com:/tmp/health.service

# (서버) 아래를 순서대로 실행
sudo mv /tmp/health.service /etc/systemd/system/health.service
sudo systemctl daemon-reload
sudo systemctl enable health

# 기존 nohup 프로세스 종료 후 systemd 로 기동
pkill -f 'java -jar health-' || true
cp health-1.0.10-SNAPSHOT.jar health.jar   # 현재 운영 중인 jar 를 안정 경로로
sudo systemctl start health
sudo systemctl status health --no-pager
```

> ⚠️ CI 배포가 `sudo systemctl restart health` 를 무암호로 실행하려면 `ubuntu` 계정에 passwordless sudo 가 필요하다 (현재 설정돼 있음).

---

## 3. 운영 명령어

```bash
sudo systemctl status health          # 상태
sudo systemctl restart health         # 수동 재시작
sudo journalctl -u health -f          # 실시간 로그 (기존 nohup.out 대체)
sudo journalctl -u health -n 200      # 최근 200줄
```

## 4. 롤백

- CI 는 배포 실패(헬스체크 미통과) 시 `health.jar.bak` 으로 **자동 롤백**한다.
- 수동 롤백: `cp health.jar.bak health.jar && sudo systemctl restart health`

## 5. 동작 흐름

```
master push
  └─ GitHub Actions (ubuntu-latest)
       ├─ JDK 17 셋업
       ├─ Secret 에서 application.yml 복원
       ├─ ./gradlew clean bootJar -x test
       └─ EC2 전송 후 deploy/remote-deploy.sh 실행
            ├─ health.jar 백업 → 새 jar 교체
            ├─ sudo systemctl restart health
            ├─ /staff 200 대기 (최대 60초)
            └─ 실패 시 이전 jar 로 롤백
```
