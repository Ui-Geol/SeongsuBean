package com.oopsw.seongsubean.board.controller;

import com.oopsw.seongsubean.board.dto.ReportBoardDTO;
import com.oopsw.seongsubean.board.service.ReportBoardService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/report")
public class ReportBoardController {
  private final ReportBoardService reportBoardService;
  public ReportBoardController(ReportBoardService reportBoardService) {
    this.reportBoardService = reportBoardService;
  }
  //select
  @GetMapping("/list")
  public String reportList(@RequestParam(value = "page", defaultValue = "1") int page,
                           @RequestParam(value = "size", defaultValue = "7") int size,
                           Model model) {
    int offset = (page - 1) * size;
    List<ReportBoardDTO> list = reportBoardService.getReportBoardList(offset, size);
    int totalCount = reportBoardService.getTotalReportBoardCount();
    int totalPages = (int) Math.ceil((double) totalCount / size);
    model.addAttribute("reportList", list);
    model.addAttribute("currentPage", page);
    model.addAttribute("totalPages", totalPages);
    return "board/report-list";
  }
  //select
  @GetMapping("/detail/{id}")
  public String reportDetail(@PathVariable("id") String id, Model model) {
    try {
      Integer parsedId = Integer.parseInt(id);
      model.addAttribute("report", reportBoardService.getReportBoardDetail(parsedId));
      return "board/report-detail";
    } catch (NumberFormatException e) {
      return "redirect:/board/report-list";
    }
  }
  //add
  @GetMapping("/post")
  public String reportPost() {
    return "board/report-post";
  }

  //update
  @GetMapping("/set/{id}")
  public String reportSet(@PathVariable("id") Integer id, Model model) {
    ReportBoardDTO dto = reportBoardService.getReportBoardDetail(id);
    model.addAttribute("mode", "update");
    return "board/report-post";
  }

}