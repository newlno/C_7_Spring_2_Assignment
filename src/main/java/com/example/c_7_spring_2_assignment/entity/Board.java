package com.example.c_7_spring_2_assignment.entity;

import com.example.c_7_spring_2_assignment.dto.BoardRequestDto;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Entity
public class Board extends Timestamped{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "BOARD_ID")
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    @Lob
    private String content;

    @Column(nullable = false)
    private String username;

    // Many = Board, User = One
    @ManyToOne(fetch = FetchType.EAGER) // Board를 select하면 무조건 들고와야하는 정보-> EAGER전략!
    @JoinColumn(name = "USER_ID")
    @JsonIgnore
    private User user; // DB는 object를 저장할 수 없다. FK, Java는 object를 저장할 수 있다.


    //mappedBy를 넣어주는 것은 연관관계의 주인이 아니라는것 -> FK가 아니니 DB에 Column 만들지마랏!
    // Board select시에 Join문을 통해 값을 얻기위함
    @OneToMany(mappedBy = "board", cascade = CascadeType.REMOVE, fetch = FetchType.EAGER)
    private List<Comment> commentList = new ArrayList<>();


    public Board(BoardRequestDto requestDto, User user){
        this.title = requestDto.getTitle();
        this.content = requestDto.getContent();
        this.username = requestDto.getUser().getUsername();
        this.commentList = requestDto.getCommentList();
        this.user = user;
    }

    //게시글 수정메소드
    public void update(BoardRequestDto requestDto, User user) {
        this.title = requestDto.getTitle();
        this.content = requestDto.getContent();
        this.user = user;
    }

    public void addComment(Comment comment) {
        this.commentList.add(comment);
    }
}
