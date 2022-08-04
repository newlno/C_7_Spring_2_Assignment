package com.example.c_7_spring_2_assignment.repository;

import com.example.c_7_spring_2_assignment.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {
}
