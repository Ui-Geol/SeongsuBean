package com.oopsw.seongsubean.board.repository;

import com.oopsw.seongsubean.board.dto.ReportBoardDTO;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import static org.junit.jupiter.api.Assertions.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;

@SpringBootTest
public class ReportBoardRepositoryTest {
  @Autowired
  ReportBoardRepository reportBoardRepository;

  @Test
  public void successAddReportBoardTest() {
    ReportBoardDTO dto = ReportBoardDTO.builder()
        .title("Cruel Summer - Taylor Swift")
        .content("나만알고있던숨듣명")
        .email("taylor1213@gmail.com")
        .build();
    boolean result = reportBoardRepository.addReportBoard(dto);
    System.out.println("✅ 게시글 등록 결과: " + result);
    System.out.println("✅ 생성된 게시글 ID: " + dto.getReportBoardId());
  }

  @Test
  public void failAddReportBoardTest() {
    ReportBoardDTO dto = ReportBoardDTO.builder()
        .title("fortnight - Taylor Swift")
        .content("ttpd 갓뎀")
        .email("")
        .build();
    assertThrows(DataIntegrityViolationException.class, () -> {
      reportBoardRepository.addReportBoard(dto);
    });;
  }

  @Test
  public void successAddReportBoardImageTest() {
    Map<String, Object> map = new HashMap<>();
    map.put("reportBoardId", 1); // 테스트 게시글 ID
    map.put("image", "/images/cafe/Cafe13.png");
    boolean result = reportBoardRepository.addReportBoardImages(map);
    System.out.println("✅ 이미지 등록 결과: " + result);
  }

  @Test
  public void failAddReportBoardImageTest() {
    Map<String, Object> map = new HashMap<>();
    map.put("reportBoardId", 31); // 테스트 게시글 ID
    map.put("image", "/images/cafe/Cafe13.png");
    assertThrows(DataIntegrityViolationException.class, () -> {
      reportBoardRepository.addReportBoardImages(map);
    });
  }

  @Test
  public void successGetReportBoardListTest() {
    List<ReportBoardDTO> list = reportBoardRepository.getReportBoardList();
    for (ReportBoardDTO dto : list) {
      System.out.println("📄 " + dto);
    }
  }

  @Test
  public void successGetReportBoardDetailTest() {
    ReportBoardDTO dto = reportBoardRepository.getReportBoardDetail(31); // 테스트용 ID
    System.out.println("📌 게시글 상세: " + dto);
  }

  @Test
  public void failGetReportBoardDetailTest() {
    ReportBoardDTO dto = reportBoardRepository.getReportBoardDetail(31); // 테스트용 ID
    assertThrows(NullPointerException.class, () -> {
      reportBoardRepository.getReportBoardDetail(dto.getReportBoardId());
    });
  }

  @Test
  public void successGetReportBoardDetailImagesTest() {
    List<String> images = reportBoardRepository.getReportBoardDetailImages(31);
    System.out.println("🖼️ 이미지 리스트:");
    for (String img : images) {
      System.out.println(" - " + img);
    }
  }

  @Test
  public void successSetReportBoardDetailTest() {
    ReportBoardDTO dto = ReportBoardDTO.builder()
        .reportBoardId(31)
        .title("크썸보다는 역시 Afterglow가 더 좋지")
        .content("러버는 갓앨범임")
        .email("taylor1213@gmail.com")
        .build();
    boolean result = reportBoardRepository.setReportBoardDetail(dto);
    System.out.println("✏️ 수정 결과: " + result);
  }

  @Test
  public void successRemoveReportBoardImagesTest() {
    boolean result = reportBoardRepository.removeReportBoardImages(31);
    System.out.println("🗑️ 이미지 삭제 결과: " + result);
  }

  @Test
  public void failRemoveReportBoardImagesTest() {
    boolean result = reportBoardRepository.removeReportBoardImages(1001);
    assertFalse(result, "존재하지 않는 이미지인데 삭제된 것으로 나옴");
  }

  @Test
  public void successRemoveReportBoardTest() {
    int  result = reportBoardRepository.removeReportBoard(31);
    System.out.println("🗑️ 게시글 삭제 결과: " + result);
  }

  @Test
  public void failRemoveReportBoardTest() {
    int  result = reportBoardRepository.removeReportBoard(31);
    assertEquals(result, "존재하지 않는 게시글인데 삭제된 것으로 나옴");
  }

}
