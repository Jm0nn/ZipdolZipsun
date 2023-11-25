package com.ssafy.zip.board.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class BoardDto {

	private int articleNo;
	private int userNo;
	private String nickName;
	private String boardNo;
	private String subject;
	private String content;
	private String registTime;
	private int hit;
	private int likes;
	private int articleTypeNo;
	private int deleted;
	
}
