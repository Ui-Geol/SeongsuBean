package com.oopsw.seongsubean.board.Service;

import com.oopsw.seongsubean.board.dto.ReportBoardDTO;
import com.oopsw.seongsubean.board.service.ReportBoardService;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class ReportBoardServiceTest {
  @Autowired
  ReportBoardService reportBoardService;
  @Test
  public void successAddReportBoardTest(){
    ReportBoardDTO dto = ReportBoardDTO.builder()
        .title("제보 제목")
        .content("제보 내용")
        .email("taylor1213@gmail.com")
        .build();

    List<String> images = List.of("test1.png", "test2.png");

    boolean result = reportBoardService.addReportBoard(dto, images);

    assertTrue(result);
    assertNotNull(dto.getReportBoardId());

    ReportBoardDTO inserted = reportBoardService.getReportBoardDetail(dto.getReportBoardId());
    assertEquals("제보 제목", inserted.getTitle());
    assertEquals(2, inserted.getImages().size());
    assertTrue(inserted.getImages().contains("test1.png"));
  }

  @Test
  public void failAddReportBoardTest(){
    ReportBoardDTO dto = ReportBoardDTO.builder()
        .reportBoardId(1)
        .title("실패 테스트")
        .content("제보 내용")
        .email("")
        .build();

    List<String> images = List.of("test1.png", "test2.png");

    assertThrows(DataIntegrityViolationException.class, () -> {
      reportBoardService.addReportBoard(dto, images);
    });
  }

  @Test
  public void successSetReportBoardTest(){
    ReportBoardDTO dto = ReportBoardDTO.builder()
        .title("초기 제목")
        .content("초기 내용")
        .email("taylor1213@gmail.com")
        .build();

    List<String> oldImages = List.of("old1.png", "old2.png");
    reportBoardService.addReportBoard(dto, oldImages);
    dto.setTitle("수정된 제목");
    dto.setContent("수정된 내용");
    List<String> newImages = List.of("new1.png");
    boolean result = reportBoardService.setReportBoard(dto, newImages);

    assertTrue(result);
    ReportBoardDTO updated = reportBoardService.getReportBoardDetail(dto.getReportBoardId());
    assertEquals("수정된 제목", updated.getTitle());
    assertEquals("수정된 내용", updated.getContent());
    assertEquals(1, updated.getImages().size());
    assertTrue(updated.getImages().contains("new1.png"));
  }

  @Test
  public void failSetReportBoardTest() {
    ReportBoardDTO dto = ReportBoardDTO.builder()
        .reportBoardId(999999)
        .title("수정할 제목")
        .content("수정할 내용")
        .email("taylor1213@gmail.com")
        .build();

    List<String> images = List.of("notused1.png");
    assertThrows(DataIntegrityViolationException.class, () -> {
      reportBoardService.setReportBoard(dto, images);
    });
  }
  @Test
  public void successDeleteReportBoardTest(){
    ReportBoardDTO dto = ReportBoardDTO.builder()
        .title("삭제용 제목")
        .content("삭제용 내용")
        .email("taylor1213@gmail.com")
        .build();
    List<String> images = List.of("del1.png");
    reportBoardService.addReportBoard(dto, images);

    boolean deleted = reportBoardService.removeReportBoard(dto.getReportBoardId());
    assertTrue(deleted);

    ReportBoardDTO check = reportBoardService.getReportBoardDetail(dto.getReportBoardId());
    assertNull(check);
  }
  @Test
  public void failDeleteReportBoardTest() {
    int nonExistentId = 999999;
    boolean result = reportBoardService.removeReportBoard(nonExistentId);
    assertFalse(result);
  }
  @Test
  public void successGetReportBoardListTest(){
    List<ReportBoardDTO> list = reportBoardService.getReportBoardList();

    assertNotNull(list);
    assertTrue(list.size() >= 0);
  }
  @Test
  public void failGetReportBoardDetailTest() {
    int nonExistentId = 999999;
    assertThrows(NullPointerException.class, () -> {
      ReportBoardDTO dto = reportBoardService.getReportBoardDetail(nonExistentId);
      dto.getTitle();
    });
  }
  @Test
  public void successGetReportBoardDetailTest() {
    ReportBoardDTO dto = ReportBoardDTO.builder()
        .title("상세 제목")
        .content("상세 내용")
        .email("taylor1213@gmail.com")
        .build();
    List<String> images = List.of("detail1.png", "detail2.png");
    reportBoardService.addReportBoard(dto, images);

    ReportBoardDTO result = reportBoardService.getReportBoardDetail(dto.getReportBoardId());

    assertNotNull(result);
    assertEquals("상세 제목", result.getTitle());
    assertEquals(2, result.getImages().size());
    assertTrue(result.getImages().contains("detail2.png"));
  }
  @Test
  public void failGetReportBoardListTest() {
    List<ReportBoardDTO> list = reportBoardService.getReportBoardList();
    assertDoesNotThrow(() -> {
      for (ReportBoardDTO dto : list) {
        assertNotNull(dto.getTitle());
      }
    });
  }
}
