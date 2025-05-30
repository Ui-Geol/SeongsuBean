package com.oopsw.seongsubean.board.controller;


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
    return "board/list";
  }

  @GetMapping("/detail/{id}")
  public String freeDetail(@PathVariable("id") Integer id, Model model){
    model.addAttribute("free", freeBoardService.getFreeBoardDetail(id));
    return "board/detail";
  }

  @GetMapping("/post")
  public String freePost(){
    return "board/free-post";
  }
}
