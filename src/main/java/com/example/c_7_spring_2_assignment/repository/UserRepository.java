package com.example.c_7_spring_2_assignment.repository;

import com.example.c_7_spring_2_assignment.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);
}
