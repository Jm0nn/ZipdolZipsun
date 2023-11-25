package com.ssafy.zip.user.model.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.ssafy.zip.user.model.UserDto;

@Mapper
public interface UserMapper {

	int registUser(UserDto memberDto);

	UserDto loginUser(UserDto memberDto);

	void modifyUser(UserDto memberDto);

	void deleteUser(int userNo);

	int idCheck(String userId);

	List<UserDto> listUser();

	UserDto userInfo(int userNo);

	String getPass(String userId);

	int nickNameCheck(String nickName);

}
