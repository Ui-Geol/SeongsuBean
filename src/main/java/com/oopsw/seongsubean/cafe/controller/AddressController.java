package com.oopsw.seongsubean.cafe.controller;


import com.oopsw.seongsubean.cafe.dto.ResponseAdressDTO;
import com.oopsw.seongsubean.cafe.service.AddAdressService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/address")
@RequiredArgsConstructor
public class AddressController {
  @Autowired
  private final AddAdressService addAdressService;

  @PostMapping("/save")
  public ResponseEntity<String> saveAddress(@RequestBody ResponseAdressDTO request) {
    addAdressService.saveAddress(request);
    return ResponseEntity.ok("주소 저장 완료");
  }
}