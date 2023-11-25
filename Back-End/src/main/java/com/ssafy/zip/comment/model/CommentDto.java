package com.ssafy.zip.comment.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class CommentDto {
	
	private int commentNo;
	private int articleNo;
	private int userNo;
	private String nickName;
	private String content;
	private String registTime;
	private int likes;
	private int deleted;
	
}
