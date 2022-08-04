package com.example.c_7_spring_2_assignment.entity;

import java.time.LocalDateTime;

public interface ShowAllBoardList {
    LocalDateTime getCreatedAt();
    LocalDateTime getModifiedAt();
    Long getId();
    String getTitle();
    String getContent();
    String getUsername();
}


