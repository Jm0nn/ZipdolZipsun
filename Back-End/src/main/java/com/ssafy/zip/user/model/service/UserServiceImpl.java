package com.ssafy.zip.user.model.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.ssafy.zip.user.model.UserDto;
import com.ssafy.zip.user.model.mapper.UserMapper;

@Service
public class UserServiceImpl implements UserService {

	private UserMapper userMapper;

	public UserServiceImpl(UserMapper userMapper) {
		this.userMapper = userMapper;
	}

	@Override
	public int registUser(UserDto userDto) {
		return userMapper.registUser(userDto);
	}

	@Override
	public UserDto loginUser(UserDto userDto) {
		return userMapper.loginUser(userDto);
	}

	@Override
	public void modifyUser(UserDto userDto) {
		userMapper.modifyUser(userDto);
	}

	@Override
	public void deleteUser(int userNo) {
		userMapper.deleteUser(userNo);
	}


	@Override
	public int idCheck(String userId) {
		return userMapper.idCheck(userId);
	}

	@Override
	public List<UserDto> listUser() {
		return userMapper.listUser();
	}

	@Override
	public UserDto userInfo(int userNo) {
		return userMapper.userInfo(userNo);
	}

	@Override
	public String findPass(String userId) {
		return userMapper.getPass(userId);
	}

	@Override
	public int nickNameCheck(String nickName) {
		return userMapper.nickNameCheck(nickName);
	}

}
