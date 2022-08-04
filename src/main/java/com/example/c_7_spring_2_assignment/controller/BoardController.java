package com.example.c_7_spring_2_assignment.controller;

import com.example.c_7_spring_2_assignment.dto.BoardRequestDto;
import com.example.c_7_spring_2_assignment.sercurity.UserDetailsImpl;
import com.example.c_7_spring_2_assignment.service.BoardService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;


@RestController
@AllArgsConstructor
@RequestMapping("/blog")
public class BoardController {
    private final BoardService boardService;


    //전체 블로그글 목록 조회
    @GetMapping("/list")
    public ResponseEntity<?> getBlogList() {
        return boardService.getAllBlogList();
    }

    //글작성
    @PostMapping("/auth/list")
    public ResponseEntity<?> createBlog(@RequestBody BoardRequestDto requestDto,
                                     @AuthenticationPrincipal UserDetailsImpl userDetails) {

        String username = userDetails.getUsername();
        return boardService.createBlog(requestDto, username);
    }

    //글 상세조회
    @GetMapping("/list/{id}")
    public ResponseEntity<?> readBlog(@PathVariable Long id) {
        return boardService.getOnePost(id);
    }


    // 글수정
    @PutMapping("/auth/list/{id}")
    public ResponseEntity<?> modifyBlog(@PathVariable Long id,
                                        @RequestBody BoardRequestDto requestDto,
                                        @AuthenticationPrincipal UserDetailsImpl userDetails) {
        String username = userDetails.getUsername();
        return boardService.modifyPost(id, requestDto, username);
    }


    //글삭제
    @DeleteMapping("/auth/list/{id}")
    public ResponseEntity<?> deleteBlog(@PathVariable Long id,
                                     @AuthenticationPrincipal UserDetailsImpl userDetails) {
        String username = userDetails.getUsername();
        System.out.println("게시글 삭제");
        return boardService.deletePost(id,username);
    }

}

