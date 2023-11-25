package com.ssafy.zip.user.model.service;

import java.util.List;

import com.ssafy.zip.user.model.UserDto;

public interface UserService {

	int registUser(UserDto userDto);

	UserDto loginUser(UserDto userDto);

	void modifyUser(UserDto userDto);

	void deleteUser(int userNo);

	int idCheck(String userId);

	List<UserDto> listUser();

	UserDto userInfo(int userNo);

	String findPass(String userId);

	int nickNameCheck(String nickName);

}
