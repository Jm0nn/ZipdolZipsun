package com.ssafy.zip.board.controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ssafy.zip.board.model.BoardDto;
import com.ssafy.zip.board.model.service.BoardService;

@RestController
@RequestMapping("/board")
@CrossOrigin("*")
public class BoardController {

	private BoardService boardService;

	public BoardController(BoardService boardService) {
		this.boardService = boardService;
	}

	// 전체 리스트 출력
	@GetMapping("/list/{boardNo}")
	public ResponseEntity<?> list(@PathVariable("boardNo") String boardNo) {
		List<BoardDto> list = boardService.listArticle(boardNo);
		if (list != null && !list.isEmpty()) {
			return ResponseEntity.status(HttpStatus.OK).body(list);
		} else {
			return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
		}
	}
	
	// 공지사항 출력
	@GetMapping("/announcelist")
	public ResponseEntity<?> list() {
		List<BoardDto> list = boardService.listArticle("01");
		if (list != null && !list.isEmpty()) {
			return ResponseEntity.status(HttpStatus.OK).body(list);
		} else {
			return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
		}
	}
	
	// 번호 받아와서 해당 글 삭제
	@DeleteMapping("/delete/{boardNo}/{articleNo}")
	public ResponseEntity<?> boardDelete(@PathVariable("boardNo") String boardNo,
			@PathVariable("articleNo") int articleNo) {
		boardService.deleteArticle(articleNo);
		System.out.println(articleNo);

		List<BoardDto> list = boardService.listArticle(boardNo);
		if (list != null && !list.isEmpty()) {
			return ResponseEntity.status(HttpStatus.OK).body(list);
		} else {
			return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
		}
	}

	// 글 작성
	@PostMapping("/write/{boardNo}")
	public ResponseEntity<?> boardWrite(@RequestBody BoardDto boardDto, @PathVariable("boardNo") String boardNo ) throws Exception {

		boardService.writeArticle(boardDto);

		List<BoardDto> list = boardService.listArticle(boardNo);
		if (list != null && !list.isEmpty()) {
			return ResponseEntity.status(HttpStatus.OK).body(list);
		} else {
			return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
		}
	}

	// 글 수정
	@PutMapping("/modify/{boardNo}/{articleNo}")
	public ResponseEntity<?> boardModify(@RequestBody BoardDto boardDto) {

		boardService.modifyArticle(boardDto);

		if (boardDto != null) {
			return ResponseEntity.status(HttpStatus.OK).build();
		} else {
			return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
		}
	}

	
	// 글 상세내용 확인
	@GetMapping("/{boardNo}/{articleNo}")
	public ResponseEntity<?> boardInfo(@PathVariable("articleNo") int articleNo) {
		BoardDto boardDto = boardService.viewArticle(articleNo);

		if (boardDto != null) {
			return ResponseEntity.status(HttpStatus.OK).body(boardDto);
		} else {
			return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
		}
	}
	
	@PutMapping("/hit/{articleNo}")
	public ResponseEntity<?> updateHit(@PathVariable("articleNo") int articleNo) {
		boardService.updateHit(articleNo);
		return ResponseEntity.ok().build();
	}
	
	@GetMapping("/search")
	public ResponseEntity<?> searchList(@RequestParam Map<String, String> map) {
		List<BoardDto> list = boardService.searchList(map);
		return ResponseEntity.ok(list);
	}

}
