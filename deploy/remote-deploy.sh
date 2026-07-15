#!/usr/bin/env bash
# EC2 서버에서 실행되는 배포 스크립트. GitHub Actions 가 jar 를 올린 뒤 호출한다.
set -e
cd /home/ubuntu

# 현재 jar 백업 후, 새로 올라온 jar 를 교체
[ -f health.jar ] && cp health.jar health.jar.bak
mv deploy/health.jar.new health.jar

sudo systemctl restart health

# 앱이 실제로 HTTP 200 을 줄 때까지 대기 (최대 60초)
OK=0
for i in $(seq 1 30); do
  code=$(curl -s -o /dev/null -w '%{http_code}' http://localhost:8080/staff || true)
  if [ "$code" = "200" ]; then OK=1; break; fi
  sleep 2
done

if [ "$OK" = "1" ]; then
  echo "✅ DEPLOY OK (health 200)"
  exit 0
fi

# 기동 실패 시 이전 jar 로 자동 롤백
echo "❌ HEALTHCHECK FAILED - rolling back to previous jar"
if [ -f health.jar.bak ]; then
  cp health.jar.bak health.jar
  sudo systemctl restart health
fi
exit 1
