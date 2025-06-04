package com.oopsw.seongsubean.board.controller;

import com.oopsw.seongsubean.board.dto.FreeBoardDTO;
import com.oopsw.seongsubean.board.service.FreeBoardService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/free")
public class FreeBoardController {
  private final FreeBoardService freeBoardService;
  public FreeBoardController(FreeBoardService freeBoardService) {
    this.freeBoardService = freeBoardService;
  }
  @GetMapping("/list")
  public String freeList(@RequestParam(value = "page", defaultValue = "1") int page,
                         @RequestParam(value = "size", defaultValue = "12") int size,
                         Model model) {
    int offset = (page - 1) * size;
    List<FreeBoardDTO> list = freeBoardService.getFreeBoardList(offset, size);
    int totalCount = freeBoardService.getTotalFreeBoardCount();
    int totalPages = (int) Math.ceil((double) totalCount / size);
    model.addAttribute("freeList", list);
    model.addAttribute("currentPage", page);
    model.addAttribute("totalPages", totalPages);
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