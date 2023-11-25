package com.ssafy.zip.user.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
@ApiModel(value = "UserDto (회원정보)", description = "아이디, 비밀번호, 이름을 가진 Domain Class")
public class UserDto {

	@ApiModelProperty("회원 번호")
	private int userNo;
	@ApiModelProperty("회원 아이디")
	private String userId;
	@ApiModelProperty("회원 이름")
	private String userName;
	@ApiModelProperty("회원 비밀번호")
	private String userPass;
	@ApiModelProperty("닉네임")
	private String nickName;
	@ApiModelProperty("회원 타입 번호")
	private int userTypeNo;
	@ApiModelProperty("회원 타입")
	private String userTypeName;
	@ApiModelProperty("성별")
	private String sex;
	@ApiModelProperty("회원 생년월일")
	private String birthDate;
	@ApiModelProperty("가입일")
	private String joinDate;
	@ApiModelProperty("이메일 아이디")
	private String emailId;
	@ApiModelProperty("이메일 도메인")
	private String emailDomain;
	@ApiModelProperty("회원 전화번호1")
	private String phoneNumber1;
	@ApiModelProperty("회원 전화번호2")
	private String phoneNumber2;
	@ApiModelProperty("회원 전화번호3")
	private String phoneNumber3;

}
