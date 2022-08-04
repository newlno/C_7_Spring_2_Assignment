package com.example.c_7_spring_2_assignment.dto;
import com.example.c_7_spring_2_assignment.entity.Board;
import com.example.c_7_spring_2_assignment.entity.User;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class CommentDto {
    private String comment;
    private Board board;
    private User user;
}
