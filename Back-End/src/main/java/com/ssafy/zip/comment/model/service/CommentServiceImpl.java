package com.ssafy.zip.comment.model.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ssafy.zip.comment.model.CommentDto;
import com.ssafy.zip.comment.model.mapper.CommentMapper;

@Service
public class CommentServiceImpl implements CommentService {

	@Autowired
	CommentMapper commentMapper;

	@Override
	public List<CommentDto> list(int articleNo) {
		return commentMapper.list(articleNo);
	}

	@Override
	public boolean create(CommentDto commentDto) {
		return commentMapper.create(commentDto) == 1;
	}

	@Override
	public boolean modify(CommentDto commentDto) {

		return commentMapper.modify(commentDto) == 1;
	}

	@Override
	public boolean delete(int commentNo) {
		
		return commentMapper.delete(commentNo) == 1;
	}

}
