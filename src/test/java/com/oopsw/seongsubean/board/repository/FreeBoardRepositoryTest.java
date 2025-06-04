package com.oopsw.seongsubean.board.repository;

import com.oopsw.seongsubean.board.dto.FreeBoardCommentDTO;
import com.oopsw.seongsubean.board.dto.FreeBoardDTO;
import com.oopsw.seongsubean.config.PropertyConfig;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.dao.DataIntegrityViolationException;



import static org.junit.jupiter.api.Assertions.*;


@MybatisTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import(PropertyConfig.class)
public class FreeBoardRepositoryTest {
  @Autowired
  FreeBoardRepository freeBoardRepository;

  @Test
  public void successAddFreeBoardTest() {
    FreeBoardDTO dto = FreeBoardDTO.builder()
        .title("Flash out the Slammer - Taylor Swift")
        .headWord("잡담")
        .content("나만알고있던숨듣명")
        .email("taylor1213@gmail.com")
        .build();
    boolean result = freeBoardRepository.addFreeBoard(dto);
    System.out.println("✅ 게시글 등록 결과: " + result);
    System.out.println("✅ 생성된 게시글 ID: " + dto.getFreeBoardId());
  }

  @Test
  public void failAddFreeBoardTest() {
    FreeBoardDTO dto = FreeBoardDTO.builder()
        .title("guilty as sin - Taylor Swift")
        .content("ttpd 갓뎀")
        .email("")
        .build();
    assertThrows(DataIntegrityViolationException.class, () -> {
      freeBoardRepository.addFreeBoard(dto);
    });;
  }

  @Test
  public void successAddFreeBoardImageTest() {
    Map<String, Object> map = new HashMap<>();
    map.put("freeBoardId", 1); // 테스트 게시글 ID
    map.put("image", "/images/cafe/Cafe1213.png");
    boolean result = freeBoardRepository.addFreeBoardImages(map);
    System.out.println("✅ 이미지 등록 결과: " + result);
  }

  @Test
  public void failAddFreeBoardImageTest() {
    Map<String, Object> map = new HashMap<>();
    map.put("freeBoardId", 1213); // 테스트 게시글 ID
    map.put("image", "/images/cafe/Cafe1989.png");
    assertThrows(DataIntegrityViolationException.class, () -> {
      freeBoardRepository.addFreeBoardImages(map);
    });
  }

  @Test
  public void successGetFreeBoardListTest() {
    List<FreeBoardDTO> list = freeBoardRepository.getFreeBoardList();
    for (FreeBoardDTO dto : list) {
      System.out.println("📄 " + dto);
    }
  }

  @Test
  public void successGetFreeBoardDetailTest() {
    FreeBoardDTO dto = freeBoardRepository.getFreeBoardDetail(12); // 테스트용 ID
    System.out.println("📌 게시글 상세: " + dto);
  }

  @Test
  public void failGetFreeBoardDetailTest() {
    FreeBoardDTO dto = freeBoardRepository.getFreeBoardDetail(31); // 테스트용 ID
    assertThrows(NullPointerException.class, () -> {
      freeBoardRepository.getFreeBoardDetail(dto.getFreeBoardId());
    });
  }

  @Test
  public void successGetFreeBoardDetailImagesTest() {
    List<String> images = freeBoardRepository.getFreeBoardDetailImages(31);
    System.out.println("🖼️ 이미지 리스트:");
    for (String img : images) {
      System.out.println(" - " + img);
    }
  }

  @Test
  public void successSetFreeBoardDetailTest() {
    FreeBoardDTO dto = FreeBoardDTO.builder()
        .freeBoardId(31)
        .title("크썸보다는 역시 Afterglow가 더 좋지")
        .content("러버는 갓앨범임")
        .email("taylor1213@gmail.com")
        .build();
    boolean result = freeBoardRepository.setFreeBoardDetail(dto);
    System.out.println("✏️ 수정 결과: " + result);
  }

  @Test
  public void successRemoveFreeBoardImagesTest() {
    boolean result = freeBoardRepository.removeFreeBoardImages(31);
    System.out.println("🗑️ 이미지 삭제 결과: " + result);
  }

  @Test
  public void failRemoveFreeBoardImagesTest() {
    boolean result = freeBoardRepository.removeFreeBoardImages(1001);
    assertFalse(result, "존재하지 않는 이미지인데 삭제된 것으로 나옴");
  }

  @Test
  public void successRemoveFreeBoardTest() {
    int result = freeBoardRepository.removeFreeBoard(31);
    System.out.println("🗑️ 게시글 삭제 결과: " + result);
  }

  @Test
  public void failRemoveFreeBoardTest() {
    int result = freeBoardRepository.removeFreeBoard(31);
    assertEquals(result, "존재하지 않는 게시글인데 삭제된 것으로 나옴");
  }

  @Test
  public void successAddFreeBoardComment(){
    FreeBoardCommentDTO dto = FreeBoardCommentDTO.builder()
        .freeBoardId(13)
        .content("테일러 내한해 하지마 내한해")
        .freeBoardCommentId(13)
        .email("taylor1213@gmail.com")
        .build();
    boolean result = freeBoardRepository.addFreeBoardComment(dto);
    System.out.println("✅ 댓글 등록 결과: " + result);
    System.out.println("✅ 생성된 댓글 ID: " + dto.getFreeBoardCommentId());
  }

  @Test
  public void failAddFreeBoardComment(){
    FreeBoardCommentDTO dto = FreeBoardCommentDTO.builder()
        .freeBoardId(13)
        .content("테일러 내한해 하지마 내한해")
        .freeBoardCommentId(13)
        .email("")
        .build();
    assertThrows(DataIntegrityViolationException.class, () -> {
      freeBoardRepository.addFreeBoardComment(dto);
    });;
  }

  @Test
  public void successGetFreeBoardComments(){
    List<FreeBoardCommentDTO> list = freeBoardRepository.getFreeBoardDetailComments(1);
    for (FreeBoardCommentDTO dto : list) {
      System.out.println("📄 " + dto);
    }
  }

  @Test
  public void successRemoveFreeBoardComments(){
    int result = freeBoardRepository.removeFreeBoardComment(1);
    System.out.println("🗑️ 댓글 삭제 결과: " + result);
  }
}
