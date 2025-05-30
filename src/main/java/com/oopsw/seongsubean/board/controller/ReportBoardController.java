package com.oopsw.seongsubean.board.controller;

import com.oopsw.seongsubean.board.dto.ReportBoardDTO;
import com.oopsw.seongsubean.board.service.ReportBoardService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/report")
public class ReportBoardController {
  private final ReportBoardService reportBoardService;
  public ReportBoardController(ReportBoardService reportBoardService) {
    this.reportBoardService = reportBoardService;
  }

  @GetMapping("/list")
  public String reportList(Model model) {
    model.addAttribute("reportList", reportBoardService.getReportBoardList());
    return "board/report-list";
  }

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

  @GetMapping("/post")
  public String reportPost() {
    return "board/report-post";
  }

  //update
  @GetMapping("/set/{id}")
  public String showUpdateForm(@PathVariable("id") Integer id, Model model) {
    ReportBoardDTO dto = reportBoardService.getReportBoardDetail(id);
    model.addAttribute("mode", "update");
    return "board/report-post";
  }

}