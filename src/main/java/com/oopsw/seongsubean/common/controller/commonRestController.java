package com.oopsw.seongsubean.common.controller;

import static com.oopsw.seongsubean.common.utils.MediaTypeUtils.getContentType;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/common")
public class commonRestController {

  @GetMapping("/{resourceType}/{category}/{fileName}")
  public ResponseEntity<Resource> getStaticImage(
      @PathVariable String resourceType,
      @PathVariable String category,
      @PathVariable String fileName) {

    try {
      // ResourceLoader 없이 직접 ClassPathResource 생성
      String filePath = String.format("static/%s/%s/%s", resourceType, category, fileName);
      Resource resource = new ClassPathResource(filePath);

      if (!resource.exists() || !resource.isReadable()) {
        return ResponseEntity.notFound().build();
      }

      String contentType = getContentType(fileName);

      return ResponseEntity.ok()
          .contentType(MediaType.parseMediaType(contentType))
          .body(resource);

    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
  }
}
