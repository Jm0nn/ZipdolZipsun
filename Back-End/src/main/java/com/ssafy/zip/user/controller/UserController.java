package com.ssafy.zip.user.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ssafy.zip.user.model.UserDto;
import com.ssafy.zip.user.model.service.UserService;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/user")
@CrossOrigin("*")
@Slf4j
public class UserController {

	private UserService userService;

	public UserController(UserService userService) {
		this.userService = userService;
	}

	@PostMapping("/regist")
	public ResponseEntity<?> regist(@RequestBody UserDto userDto) {
		userService.registUser(userDto);

		return ResponseEntity.status(HttpStatus.OK).build();
	}

	@GetMapping("/id/{userId}")
	public ResponseEntity<?> idCheck(@PathVariable("userId") String userId) {
		int cnt = userService.idCheck(userId);

		log.debug("idCheck : {}", cnt);

		return ResponseEntity.ok(cnt);
	}

	@GetMapping("/nickname/{nickName}")
	public ResponseEntity<?> nickNameCheck(@PathVariable("nickName") String nickName) {
		int cnt = userService.nickNameCheck(nickName);

		log.debug("nickNameCheck : {}", cnt);

		return ResponseEntity.status(HttpStatus.OK).body(cnt);
	}

	@GetMapping("/info/{userNo}")
	public ResponseEntity<?> userInfo(@PathVariable("userNo") int userNo) {
		UserDto userDto = userService.userInfo(userNo);

		log.debug("userInfo : {}", userDto);

		return ResponseEntity.ok(userDto);
	}

	@PostMapping("/login")
	public ResponseEntity<?> login(@RequestBody UserDto userDto) {
		UserDto loginUser = userService.loginUser(userDto);

		log.debug("login : {}", loginUser);

		if (loginUser != null) {
			return ResponseEntity.status(HttpStatus.OK).body(loginUser);
		} else {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
		}
	}

	@GetMapping("/find/{userId}")
	public ResponseEntity<?> findPass(@PathVariable("userId") String userId) {
		String pass = userService.findPass(userId);
		
		if (pass == null) {
			pass = "";
		}

		return ResponseEntity.status(HttpStatus.OK).body(pass);
	}

	@PutMapping("/modify")
	public ResponseEntity<?> modify(@RequestBody UserDto userDto) {
		userService.modifyUser(userDto);

		return ResponseEntity.status(HttpStatus.OK).build();
	}

	@DeleteMapping("/{userNo}")
	public ResponseEntity<?> delete(@PathVariable("userNo") int userNo) {
		userService.deleteUser(userNo);

		return ResponseEntity.status(HttpStatus.OK).build();
	}
	
	@GetMapping
	public ResponseEntity<?> userList() {
		List<UserDto> list = userService.listUser();
		if (list != null && !list.isEmpty()) {
			return ResponseEntity.status(HttpStatus.OK).body(list);
		} else {
			return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
		}
	}

}
