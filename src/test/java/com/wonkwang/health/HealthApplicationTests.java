package com.wonkwang.health;

import com.wonkwang.health.dto.UserDTO;
import com.wonkwang.health.service.PostService;
import com.wonkwang.health.service.S3Service;
import com.wonkwang.health.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;

@SpringBootTest
@Commit
class HealthApplicationTests {

	@Autowired
	private S3Service s3Service;
	@Autowired
	private UserService userService;

	@Test
	void create() {

	}


}
