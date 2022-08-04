package com.example.c_7_spring_2_assignment.entity;

import com.example.c_7_spring_2_assignment.dto.UserLoginDto;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Getter
@NoArgsConstructor
@Table(name = "USERS")
@Entity
public class User extends Timestamped{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String username;

    @Column(nullable = false)
    @JsonIgnore
    private String password;

    @JsonIgnore
    @Transient // Entity table에 보이지 않도록하는 annotation
    private String passwordConfirm;

    @JsonIgnore
    @Column
    private String refreshToken;


    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }


    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Board> boardList;

    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Comment> commentList;

    public User(UserLoginDto userLoginDto) {
        this.username = userLoginDto.getUsername();
        this.password = userLoginDto.getPassword();
    }
}
