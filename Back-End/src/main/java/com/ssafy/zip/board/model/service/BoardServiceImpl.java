package com.ssafy.zip.board.model.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.ssafy.zip.board.model.BoardDto;
import com.ssafy.zip.board.model.mapper.BoardMapper;
import com.ssafy.zip.util.PageNavigation;
import com.ssafy.zip.util.SizeConstant;

@Service
public class BoardServiceImpl implements BoardService {

	private BoardMapper boardMapper;

	private BoardServiceImpl(BoardMapper boardMapper) {
		this.boardMapper = boardMapper;
	}

	@Override
	public void writeArticle(BoardDto boardDto) {
		boardMapper.writeArticle(boardDto);
	}
	
	@Override
	public List<BoardDto> listArticle(String boardNo) {
//		System.out.println("hi");
		return boardMapper.listArticle(boardNo);
	}
	
//	@Override
//	public PageNavigation makePageNavigation(Map<String, String> map) {
//		PageNavigation pageNavigation = new PageNavigation();
//
//		int naviSize = SizeConstant.NAVIGATION_SIZE;
//		int sizePerPage = SizeConstant.LIST_SIZE;
//		int currentPage = Integer.parseInt(map.get("pgno"));
//
//		pageNavigation.setCurrentPage(currentPage);
//		pageNavigation.setNaviSize(naviSize);
//		Map<String, Object> param = new HashMap<String, Object>();
//		String key = map.get("key");
//		String word = map.get("word");
//		param.put("key", key.isEmpty() ? "" : key);
//		param.put("word", word.isEmpty() ? "" : word);
//		int totalCount = boardMapper.getTotalArticleCount(param);
//		pageNavigation.setTotalCount(totalCount);
//		int totalPageCount = (totalCount - 1) / sizePerPage + 1;
//		pageNavigation.setTotalPageCount(totalPageCount);
//		boolean startRange = currentPage <= naviSize;
//		pageNavigation.setStartRange(startRange);
//		boolean endRange = (totalPageCount - 1) / naviSize * naviSize < currentPage;
//		pageNavigation.setEndRange(endRange);
//		pageNavigation.makeNavigator();
//
//		return pageNavigation;
//	}

	@Override
	public BoardDto viewArticle(int articleNo) {
		return boardMapper.viewArticle(articleNo);
	}
	
	@Override
	public void updateHit(int articleNo) {
		boardMapper.updateHit(articleNo);
	}

	@Override
	public void modifyArticle(BoardDto boardDto) {
		boardMapper.modifyArticle(boardDto);
	}

	@Override
	public void deleteArticle(int articleNo) {
		boardMapper.deleteArticle(articleNo);
	}

	@Override
	public List<BoardDto> searchList(Map<String, String> map) {
		return boardMapper.searchList(map);
	}

}
