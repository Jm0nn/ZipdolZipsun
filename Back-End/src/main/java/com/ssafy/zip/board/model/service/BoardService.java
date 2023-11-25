package com.ssafy.zip.board.model.service;

import java.util.List;
import java.util.Map;

import com.ssafy.zip.board.model.BoardDto;
import com.ssafy.zip.util.PageNavigation;

public interface BoardService {

	void writeArticle(BoardDto boardDto);

	List<BoardDto> listArticle(String boardNo);

//	PageNavigation makePageNavigation(Map<String, String> map);

	BoardDto viewArticle(int articleNo);
	
	void updateHit(int articleNo);

	void modifyArticle(BoardDto boardDto);

	void deleteArticle(int articleNo);

	List<BoardDto> searchList(Map<String, String> map);

}
