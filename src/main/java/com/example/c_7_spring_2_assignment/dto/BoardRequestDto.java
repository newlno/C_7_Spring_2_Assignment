package com.example.c_7_spring_2_assignment.dto;


import com.example.c_7_spring_2_assignment.entity.Comment;
import com.example.c_7_spring_2_assignment.entity.User;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@RequiredArgsConstructor
public class BoardRequestDto {
    private String title;
    private String content;
    private User user;
    private List<Comment> commentList;
}
