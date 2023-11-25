package com.ssafy.zip.comment.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
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
import org.springframework.web.bind.annotation.RestController;

import com.ssafy.zip.comment.model.CommentDto;
import com.ssafy.zip.comment.model.service.CommentService;

@RestController
@RequestMapping("/comment")
@CrossOrigin("*")
public class CommentController {

	@Autowired
	CommentService commentService;

	@GetMapping("/{articleNo}")
	public ResponseEntity<?> listComment(@PathVariable("articleNo") int articleNo) {

		try {
			List<CommentDto> listComment = commentService.list(articleNo);
			return new ResponseEntity<List<CommentDto>>(listComment, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<Void>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	@PostMapping
	public ResponseEntity<?> createComment(@RequestBody CommentDto commentDto) {
		try {
			commentService.create(commentDto);

			List<CommentDto> listComment = commentService.list(commentDto.getArticleNo());
			return new ResponseEntity<List<CommentDto>>(listComment, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<Void>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	@PutMapping
	public ResponseEntity<?> modifyComment(@RequestBody CommentDto commentDto) {
		try {
			commentService.modify(commentDto);

			List<CommentDto> listComment = commentService.list(commentDto.getArticleNo());
			return new ResponseEntity<List<CommentDto>>(listComment, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<Void>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	@DeleteMapping("/delete/{articleNo}/{commentNo}")
	public ResponseEntity<?> deleteComment(@PathVariable("articleNo") int articleNo,
			@PathVariable("commentNo") int commentNo) {
		try {
			commentService.delete(commentNo);

			List<CommentDto> listComment = commentService.list(articleNo);
			return new ResponseEntity<List<CommentDto>>(listComment, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<Void>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

}
