package com.ssafy.zip.board.model.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.ssafy.zip.board.model.BoardDto;

@Mapper
public interface BoardMapper {

	void writeArticle(BoardDto boardDto);

	List<BoardDto> listArticle(String boardNo);
	
	int getTotalArticleCount(Map<String, Object> param);

	BoardDto viewArticle(int articleNo);
	
	void updateHit(int articleNo);

	void modifyArticle(BoardDto boardDto);

	void deleteArticle(int articleNo);

	List<BoardDto> searchList(Map<String, String> map);

}
