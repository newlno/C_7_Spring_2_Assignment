package com.example.c_7_spring_2_assignment.service;

import com.example.c_7_spring_2_assignment.dto.BoardRequestDto;
import com.example.c_7_spring_2_assignment.dto.ResponseDto;
import com.example.c_7_spring_2_assignment.entity.Board;
import com.example.c_7_spring_2_assignment.entity.ShowAllBoardList;
import com.example.c_7_spring_2_assignment.entity.User;
import com.example.c_7_spring_2_assignment.repository.BoardRepository;
import com.example.c_7_spring_2_assignment.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BoardService {
    private final BoardRepository boardRepository;
    private final UserRepository userRepository;


    //블로그 전체 리스트 조회
    @Transactional
    public ResponseEntity<?> getAllBlogList() {
        List<ShowAllBoardList> boardList = boardRepository.findAllByOrderByCreatedAtDesc();
        return new ResponseEntity<>(ResponseDto.success(boardList),HttpStatus.OK);
    }

    //블로그 글작성
    @Transactional
    public ResponseEntity<?> createBlog(BoardRequestDto requestDto, String username) {
        User foundUser = userRepository.findByUsername(username);
        requestDto.setUser(foundUser);
        Board addBoard = boardRepository.save(new Board(requestDto, foundUser));
        return new ResponseEntity<>(ResponseDto.success(addBoard),HttpStatus.OK);
    }


    @Transactional
    public ResponseEntity<?> modifyPost(Long id, BoardRequestDto requestDto, String username) {
        User foundUser = userRepository.findByUsername(username);
        Optional<Board> checkBoard = boardRepository.findById(id);


        if(checkBoard.isPresent()) {
            if (Objects.equals(foundUser.getId(), checkBoard.get().getUser().getId())) {
                Board board = checkBoard.get();

                board.update(requestDto, foundUser);
                return new ResponseEntity<>(ResponseDto.success(board), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(ResponseDto.fail("UNAUTHORIZED", "작성자만 수정할 수 있습니다."),HttpStatus.UNAUTHORIZED);
            }
        }
        return new ResponseEntity<>(ResponseDto.fail("NULL_POST_ID", "해당 게시글은 존재하지 않는 게시글입니다."),HttpStatus.NOT_FOUND);
    }


    @Transactional
    public ResponseEntity<?> getOnePost(Long id) {
        Optional<Board> foundBoard = boardRepository.findById(id);

        if(foundBoard.isPresent()) {
            return new ResponseEntity<>(ResponseDto.success(foundBoard),HttpStatus.OK);
        } else {
            return new ResponseEntity<>(ResponseDto.fail("NULL_POST_ID", "해당 게시글은 존재하지 않는 게시글입니다."), HttpStatus.NOT_FOUND);
        }
    }

    @Transactional
    public ResponseEntity<?> deletePost(Long id, String username) {
        User foundUser = userRepository.findByUsername(username);
        Optional<Board> foundBoard = boardRepository.findById(id);

        if (foundBoard.isPresent()) {
            if (Objects.equals(foundUser.getId(), foundBoard.get().getUser().getId())) {
                boardRepository.deleteById(id);
                return new ResponseEntity<>(ResponseDto.success("delete success"),HttpStatus.OK);
            } else {
                return new ResponseEntity<>(ResponseDto.fail("UNAUTHORIZED", "작성자만 삭제할 수 있습니다."),HttpStatus.UNAUTHORIZED);
            }
        }
        return new ResponseEntity<>(ResponseDto.fail("NULL_POST_ID", "해당 게시글은 존재하지 않는 게시글입니다."), HttpStatus.NOT_FOUND);
    }
}
