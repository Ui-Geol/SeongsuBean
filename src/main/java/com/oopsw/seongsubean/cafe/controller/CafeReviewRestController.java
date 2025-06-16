package com.oopsw.seongsubean.cafe.controller;

import com.oopsw.seongsubean.account.dto.UserDTO;
import com.oopsw.seongsubean.auth.AccountDetails;
import com.oopsw.seongsubean.cafe.dto.TotalReviewDTO;
import com.oopsw.seongsubean.cafe.service.ReviewService;
import jakarta.validation.Valid;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/cafe/{cafeId}/reviews")
public class CafeReviewRestController {

  private final ReviewService reviewService;

  @PostMapping
  public ResponseEntity<String> addMenu(@PathVariable Integer cafeId,
      @RequestBody TotalReviewDTO totalReviewDto,
      @AuthenticationPrincipal AccountDetails accountDetails) {

    UserDTO userDto = accountDetails.getUser();
    if (userDto == null) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인이 필요합니다");
    }

    totalReviewDto.getReviewDTO().setEmail(userDto.getEmail());
    totalReviewDto.getReviewDTO().setCafeId(cafeId);

    try {
      reviewService.addReview(totalReviewDto);
      return ResponseEntity.status(HttpStatus.CREATED).body("리뷰 생성이 성공하였습니다");
    } catch (RuntimeException e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("리뷰 생성이 실패하였습니다");
    }
  }

  @GetMapping("/{pageNumber}")
  public ResponseEntity<Map<String, Object>> getReviews(@PathVariable("cafeId") Integer cafeId,
      @PathVariable("pageNumber") Integer pageNumber) {
    Pageable pageable = PageRequest.of(pageNumber, 2);
    List<TotalReviewDTO> totalReviewDTOList = reviewService.getReviews(cafeId, pageable);

    return ResponseEntity.ok(Map.of("totalReviewDTOList", totalReviewDTOList));
  }

  @DeleteMapping
  public ResponseEntity<String> removeReview(@Valid @RequestBody TotalReviewDTO totalReviewDto) {
    try {
      reviewService.removeReview(totalReviewDto.getReviewDTO().getReviewId());
      return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    } catch (RuntimeException e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("메뉴 삭제를 실패하였습니다");
    }
  }


}
