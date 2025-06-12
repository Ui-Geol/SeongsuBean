package com.oopsw.seongsubean.cafe.controller;

import com.oopsw.seongsubean.cafe.dto.TotalReviewDTO;
import com.oopsw.seongsubean.cafe.service.ReviewService;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/cafe/{cafeId}/reviews")
public class CafeReviewRestController {

  private final ReviewService reviewService;

  @GetMapping("/{pageNumber}")
  public ResponseEntity<Map<String, Object>> getReviews(@PathVariable("cafeId") Integer cafeId,
      @PathVariable("pageNumber") Integer pageNumber) {
    Pageable pageable = PageRequest.of(pageNumber, 2);
    List<TotalReviewDTO> totalReviewDTOList = reviewService.getReviews(cafeId, pageable);

    return ResponseEntity.ok(Map.of("totalReviewDTOList", totalReviewDTOList));
  }


}
