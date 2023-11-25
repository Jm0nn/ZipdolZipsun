package com.ssafy.zip.comment.model.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.ssafy.zip.comment.model.CommentDto;

@Mapper
public interface CommentMapper {

	List<CommentDto> list(int articleNo);

	int create(CommentDto commentDto);

	int modify(CommentDto commentDto);

	int delete(int commentNo);

}
