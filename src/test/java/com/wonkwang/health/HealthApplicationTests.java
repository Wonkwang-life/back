package com.wonkwang.health;

import com.wonkwang.health.service.PostService;
import com.wonkwang.health.service.S3Service;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;

@SpringBootTest
@Commit
class HealthApplicationTests {

	@Autowired
	private S3Service s3Service;

	@Test
	void delete() {
		s3Service.deleteFile("https://homereview1.s3.ap-northeast-2.amazonaws.com/products/037a0de8-0b1b-4d03-8466-4903845f604c-%EC%8A%A4%ED%81%AC%EB%A6%B0%EC%83%B7%202023-12-16%20234714.png"
		);
	}

}
