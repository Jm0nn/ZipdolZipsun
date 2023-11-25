package com.ssafy.zip.comment.model.service;

import java.util.List;

import com.ssafy.zip.comment.model.CommentDto;

public interface CommentService {
	
	List<CommentDto> list(int articleNo);

	boolean create(CommentDto commentDto);

	boolean modify(CommentDto commentDto);

	boolean delete(int commentNo);

}
