package com.oopsw.seongsubean.board.Service;

import com.oopsw.seongsubean.board.dto.FreeBoardCommentDTO;
import com.oopsw.seongsubean.board.dto.FreeBoardDTO;
import com.oopsw.seongsubean.board.service.FreeBoardService;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class FreeBoardServiceTest {
  @Autowired
  private FreeBoardService freeBoardService;
  @Test
  public void successAddFreeBoardTest(){
    FreeBoardDTO dto = FreeBoardDTO.builder()
        .title("자유게시판 제목")
        .headWord("잡담")
        .content("자유게시판 내용")
        .email("taylor1213@gmail.com")
        .build();
    List<String> images = List.of("img1.png", "img2.png");

    boolean result = freeBoardService.addFreeBoard(dto, images);

    assertTrue(result);
    assertNotNull(dto.getFreeBoardId());

    FreeBoardDTO inserted = freeBoardService.getFreeBoardDetail(dto.getFreeBoardId());
    assertEquals("자유게시판 제목", inserted.getTitle());
    assertEquals(2, inserted.getImages().size());
    assertTrue(inserted.getImages().contains("img1.png"));
  }
  @Test
  public void successGetFreeBoardDetailTest(){
    FreeBoardDTO dto = FreeBoardDTO.builder()
        .title("상세 제목")
        .headWord("정보")
        .content("상세 내용")
        .email("taylor1213@gmail.com")
        .build();
    List<String> images = List.of("detail1.png", "detail2.png");
    freeBoardService.addFreeBoard(dto, images);

    FreeBoardDTO result = freeBoardService.getFreeBoardDetail(dto.getFreeBoardId());

    assertNotNull(result);
    assertEquals("상세 제목", result.getTitle());
    assertEquals(2, result.getImages().size());
    assertTrue(result.getImages().contains("detail2.png"));
  }
  @Test
  public void successSetFreeBoardTest(){  FreeBoardDTO dto = FreeBoardDTO.builder()
      .title("수정 전 제목")
      .headWord("공지")
      .content("수정 전 내용")
      .email("taylor1213@gmail.com")
      .build();
    List<String> oldImages = List.of("old1.png", "old2.png");
    freeBoardService.addFreeBoard(dto, oldImages);

    dto.setTitle("수정된 제목");
    dto.setContent("수정된 내용");
    List<String> newImages = List.of("new1.png");

    boolean result = freeBoardService.setFreeBoard(dto, newImages);

    assertTrue(result);
    FreeBoardDTO updated = freeBoardService.getFreeBoardDetail(dto.getFreeBoardId());
    assertEquals("수정된 제목", updated.getTitle());
    assertEquals("수정된 내용", updated.getContent());
    assertEquals(1, updated.getImages().size());
    assertTrue(updated.getImages().contains("new1.png"));
  }
  @Test
  public void successRemoveFreeBoardTest(){
    FreeBoardDTO dto = FreeBoardDTO.builder()
        .title("삭제용 제목")
        .headWord("기타")
        .content("삭제용 내용")
        .email("taylor1213@gmail.com")
        .build();
    List<String> images = List.of("del1.png");
    freeBoardService.addFreeBoard(dto, images);

    boolean deleted = freeBoardService.removeFreeBoard(dto.getFreeBoardId());
    assertTrue(deleted);

    assertThrows(NullPointerException.class, () -> {
      FreeBoardDTO check = freeBoardService.getFreeBoardDetail(dto.getFreeBoardId());
      check.getTitle();
    });
  }
  @Test
  public void successAddFreeBoardCommentTest(){
    FreeBoardDTO board = FreeBoardDTO.builder()
        .title("댓글 테스트")
        .headWord("잡담")
        .content("댓글용 본문")
        .email("taylor1213@gmail.com")
        .build();
    freeBoardService.addFreeBoard(board, List.of());

    FreeBoardCommentDTO comment = FreeBoardCommentDTO.builder()
        .freeBoardId(board.getFreeBoardId())
        .content("첫 번째 댓글")
        .email("taylor1213@gmail.com")
        .createdDate(LocalDateTime.now())
        .build();

    boolean result = freeBoardService.addFreeBoardComment(comment);
    assertTrue(result);

    FreeBoardDTO resultBoard = freeBoardService.getFreeBoardDetail(board.getFreeBoardId());
    assertTrue(resultBoard.getComments().stream().anyMatch(c -> c.getContent().equals("첫 번째 댓글")));

  }
  @Test
  public void successGetFreeBoardCommentDetailTest(){
    FreeBoardDTO board = FreeBoardDTO.builder()
        .title("댓글 상세 테스트")
        .headWord("잡담")
        .content("본문")
        .email("taylor1213@gmail.com")
        .build();
    freeBoardService.addFreeBoard(board, List.of());

    FreeBoardCommentDTO comment = FreeBoardCommentDTO.builder()
        .freeBoardId(board.getFreeBoardId())
        .content("상세 댓글")
        .email("taylor1213@gmail.com")
        .createdDate(LocalDateTime.now())
        .build();
    freeBoardService.addFreeBoardComment(comment);

    FreeBoardDTO resultBoard = freeBoardService.getFreeBoardDetail(board.getFreeBoardId());
    assertNotNull(resultBoard.getComments());
    assertEquals("상세 댓글", resultBoard.getComments().get(0).getContent());
  }
  @Test
  public void successRemoveFreeBoardCommentTest(){    FreeBoardDTO board = FreeBoardDTO.builder()
      .title("댓글 삭제 테스트")
      .headWord("잡담")
      .content("본문")
      .email("taylor1213@gmail.com")
      .build();
    freeBoardService.addFreeBoard(board, List.of());

    FreeBoardCommentDTO comment = FreeBoardCommentDTO.builder()
        .freeBoardId(board.getFreeBoardId())
        .content("삭제될 댓글")
        .email("taylor1213@gmail.com")
        .createdDate(LocalDateTime.now())
        .build();
    freeBoardService.addFreeBoardComment(comment);

    boolean result = freeBoardService.removeFreeBoardComment(board.getFreeBoardId());
    assertTrue(result);

    FreeBoardDTO resultBoard = freeBoardService.getFreeBoardDetail(board.getFreeBoardId());
    assertEquals(0, resultBoard.getComments().size());
  }
  @Test
  public void successGetFreeBoardListTest(){
    List<FreeBoardDTO> list = freeBoardService.getFreeBoardList();
    assertNotNull(list);
    assertTrue(list.size() >= 0);
  }
}
