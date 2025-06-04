package com.oopsw.seongsubean.cafe.controller;

import com.oopsw.seongsubean.cafe.domain.OperationTime;
import com.oopsw.seongsubean.cafe.dto.CafeDTO;
import com.oopsw.seongsubean.cafe.dto.CafeHeaderDTO;
import com.oopsw.seongsubean.cafe.dto.MenuDTO;
import com.oopsw.seongsubean.cafe.dto.TotalReviewDTO;
import com.oopsw.seongsubean.cafe.service.CafeService;
import com.oopsw.seongsubean.cafe.service.MenuService;
import com.oopsw.seongsubean.cafe.service.ReviewService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/cafe")
public class CafeController {

  private final CafeService cafeService;
  private final ReviewService reviewService;
  private final MenuService menuService;


  @GetMapping("/{id}")
  public String freeDetail(@PathVariable("id") Integer id, Model model) {
    //카페 헤더
    CafeHeaderDTO cafeHeaderDTO = cafeService.getCafeHeader(id);
    model.addAttribute("cafeHeaderDTO", cafeHeaderDTO);
    //카페 개요
    CafeDTO cafeDTO = cafeService.getCafeDTO(id);
    model.addAttribute("cafeDTO", cafeDTO);

    //카페 영업 시간
    List<OperationTime> operationTimes = cafeService.getOperationTimes(id);
    model.addAttribute("operationTimes", operationTimes);

    //카페 메뉴
    Pageable pageable = PageRequest.of(0, 3);
    List<MenuDTO> menuInfoList = menuService.getMenuList(id, pageable);
    model.addAttribute("menuInfoList", menuInfoList);

    //카페 리뷰
    pageable = PageRequest.of(0, 2);
    List<TotalReviewDTO> totalReviewDTOList = reviewService.getReviews(id, pageable);
    System.out.println(totalReviewDTOList);
    model.addAttribute("totalReviewDTOList", totalReviewDTOList);

    return "cafe/cafe-detail";
  }


}
