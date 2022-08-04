package com.example.c_7_spring_2_assignment.repository;

import com.example.c_7_spring_2_assignment.entity.Board;
import com.example.c_7_spring_2_assignment.entity.ShowAllBoardList;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BoardRepository extends JpaRepository<Board, Long> {
    List<ShowAllBoardList> findAllByOrderByCreatedAtDesc();
}
