package com.example.c_7_spring_2_assignment.service;

import com.example.c_7_spring_2_assignment.dto.CommentDto;
import com.example.c_7_spring_2_assignment.dto.ResponseDto;
import com.example.c_7_spring_2_assignment.entity.Board;
import com.example.c_7_spring_2_assignment.entity.Comment;
import com.example.c_7_spring_2_assignment.entity.User;
import com.example.c_7_spring_2_assignment.repository.BoardRepository;
import com.example.c_7_spring_2_assignment.repository.CommentRepository;
import com.example.c_7_spring_2_assignment.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@AllArgsConstructor
public class CommentService {

    private final BoardRepository boardRepository;
    private final CommentRepository commentRepository;

    private final UserRepository userRepository;

    // 댓글 전체 조회(권한 필요X)
    @Transactional
    public ResponseEntity<?> getAllComments(Long boardId) {
        Optional<Board> foundBoard = boardRepository.findById(boardId);
        if(foundBoard.isPresent()) {
            List<Comment> commentList = foundBoard.get().getCommentList();
            return new ResponseEntity<>(ResponseDto.success(commentList), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(ResponseDto.fail("NULL_POST_ID", "해당 게시글은 존재하지 않는 게시글입니다."),
                    HttpStatus.NOT_FOUND);
        }
    }

    // 댓글 작성
    @Transactional
    public ResponseEntity<?> addComments(Long boardId, CommentDto commentDto, String username) {

        //boardId 값으로 게시글을 찾는다
        Optional<Board> checkBoard = boardRepository.findById(boardId);
        User foundUser = userRepository.findByUsername(username);

        if(checkBoard.isPresent()) {
            Board foundBoard = checkBoard.get();
            commentDto.setUser(foundUser);
            commentDto.setBoard(foundBoard);
            Comment saveComment = commentRepository.save(new Comment(commentDto,foundBoard,foundUser));
            foundBoard.addComment(saveComment);
            return new ResponseEntity<>(ResponseDto.success(saveComment), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(ResponseDto.fail("NULL_POST_ID", "해당 게시글은 존재하지 않는 게시글입니다."),
                    HttpStatus.NOT_FOUND);
        }
    }

    // 댓글 수정
    @Transactional
    public ResponseEntity<?> updateComment(Long boardId, Long commentId, CommentDto commentDto, String username) {

        User foundUser = userRepository.findByUsername(username);
        //게시글 있는지 확인을 위해 객체생성
        Optional<Board> checkBoard = boardRepository.findById(boardId);
        Comment foundComment;

        if(checkBoard.isPresent()) {
            List<Comment> commentList = checkBoard.get().getCommentList();
            // 댓글리스트 중에 수정을 원하는 특정 댓글, 댓글리스트에서 찾을 때는 인덱스값을 사용했기 때문에 commentId - 1
            try {
                foundComment = commentList.get(commentId.intValue()-1);
            } catch (IndexOutOfBoundsException e) {
                return new ResponseEntity<>(ResponseDto.fail("NULL_POST_ID", "존재하지 않는 댓글입니다."),
                        HttpStatus.NOT_FOUND);
            }

            //수정할 댓글의 작성자가 맞는지 확인
            if(Objects.equals(foundUser.getId(), foundComment.getUser().getId())) {
                foundComment.update(commentDto,checkBoard.get(),foundUser);
                return new ResponseEntity<>(ResponseDto.success(foundComment), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(ResponseDto.fail("UNAUTHORIZED", "작성자만 수정할 수 있습니다."),
                        HttpStatus.UNAUTHORIZED);
            }
        }
        return new ResponseEntity<>(ResponseDto.fail("NULL_POST_ID", "해당 게시글은 존재하지 않습니다."),
                HttpStatus.NOT_FOUND);
    }


    // 댓글 삭제
    @Transactional
    public ResponseEntity<?> removeComment(Long boardId, Long commentId, String username) {
        Optional<Board> foundBoard = boardRepository.findById(boardId);
        Comment foundComment;

        if(foundBoard.isPresent()) {
            try {
                // 댓글리스트 중에 삭제를 원하는 특정 댓글, 댓글리스트에서 찾을 때는 인덱스값을 사용했기 때문에 commentId - 1
                foundComment = foundBoard.get().getCommentList().get(commentId.intValue()-1);
            } catch (IndexOutOfBoundsException e) {
                return new ResponseEntity<>(ResponseDto.fail("NULL_POST_ID", "존재하지 않는 댓글입니다."),
                        HttpStatus.NOT_FOUND);
            }
            User foundUser = userRepository.findByUsername(username);

            //삭제할 댓글의 작성자가 맞는지 확인
            if(Objects.equals(foundUser.getId(), foundComment.getUser().getId())) {
                commentRepository.deleteById(commentId);
                return new ResponseEntity<>(ResponseDto.success("delete success"),HttpStatus.OK);
            } else {
                return new ResponseEntity<>(ResponseDto.fail("UNAUTHORIZED", "작성자만 삭제할 수 있습니다."),
                        HttpStatus.UNAUTHORIZED);
            }
        }
        return new ResponseEntity<>(ResponseDto.fail("NULL_POST_ID", "해당 게시글은 존재하지 않는 게시글입니다."),
                HttpStatus.NOT_FOUND);
    }
}
