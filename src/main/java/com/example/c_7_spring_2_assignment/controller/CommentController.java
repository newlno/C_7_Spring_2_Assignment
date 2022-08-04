package com.example.c_7_spring_2_assignment.controller;


import com.example.c_7_spring_2_assignment.dto.CommentDto;
import com.example.c_7_spring_2_assignment.sercurity.UserDetailsImpl;
import com.example.c_7_spring_2_assignment.service.CommentService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/blog")
@AllArgsConstructor
public class CommentController {

    private final CommentService commentService;

    //댓글 목록조회(권한 필요X)
    @GetMapping("/list/{id}/comments")
    public ResponseEntity<?> getAllCommentList(@PathVariable(name = "id") Long boardId) {
        return commentService.getAllComments(boardId);
    }

    // 댓글 작성(권한 필요)
    @PostMapping("/auth/list/{id}/comments")
    public ResponseEntity<?> addComments(@PathVariable(name = "id") Long boardId,
                                         @RequestBody CommentDto commentDto,
                                         @AuthenticationPrincipal UserDetailsImpl userDetails) {
        String username = userDetails.getUsername();
        return commentService.addComments(boardId, commentDto, username);
    }


    // 댓글 수정(권한 필요)
    @PutMapping("/auth/list/{id}/comments/{commentId}")
    public ResponseEntity<?> modifyComment(@PathVariable(name = "id") Long boardId,
                                        @PathVariable(name = "commentId") Long commentId,
                                        @RequestBody CommentDto commentDto,
                                        @AuthenticationPrincipal UserDetailsImpl userDetails) {
        String username = userDetails.getUsername();
        return commentService.updateComment(boardId, commentId, commentDto, username);
    }


    // 댓글 삭제(권한 필요)
    @DeleteMapping("/auth/list/{id}/comments/{commentId}")
    public ResponseEntity<?> deleteComment(@PathVariable(name = "id") Long boardId,
                                        @PathVariable(name = "commentId") Long commentId,
                                        @AuthenticationPrincipal UserDetailsImpl userDetails) {
        String username = userDetails.getUsername();
        return commentService.removeComment(boardId, commentId, username);
    }


}
