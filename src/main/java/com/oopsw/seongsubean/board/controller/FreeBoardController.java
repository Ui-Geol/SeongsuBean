package com.oopsw.seongsubean.board.controller;

import com.oopsw.seongsubean.board.dto.FreeBoardDTO;
import com.oopsw.seongsubean.board.service.FreeBoardService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/free")
public class FreeBoardController {
  private final FreeBoardService freeBoardService;
  public FreeBoardController(FreeBoardService freeBoardService) {
    this.freeBoardService = freeBoardService;
  }
  @GetMapping("/list")
  public String freeList(Model model) {
    model.addAttribute("freeList", freeBoardService.getFreeBoardList());
    return "board/free-list";
  }
  @GetMapping("/detail/{id}")
  public String freeDetail(@PathVariable("id") String id, Model model) {
    try {
      Integer parsedId = Integer.parseInt(id);
      model.addAttribute("free", freeBoardService.getFreeBoardDetail(parsedId));
      return "board/free-detail";
    } catch (NumberFormatException e) {
      return "redirect:/board/free-list";
    }
  }
  @GetMapping("/post")
  public String freePost() {
    return "board/free-post";
  }
  @GetMapping("/set/{id}")
  public String freeSet(@PathVariable("id") Integer id, Model model) {
    FreeBoardDTO dto = freeBoardService.getFreeBoardDetail(id);
    model.addAttribute("mode", "update");
    return "board/free-post";
  }
}