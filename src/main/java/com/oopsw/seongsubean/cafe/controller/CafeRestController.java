package com.oopsw.seongsubean.cafe.controller;

import com.oopsw.seongsubean.cafe.dto.CafeDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class CafeRestController {

    /**
     * 클라이언트에서 JSON으로 보낸 CafeDTO 전체를 받아서,
     * 그대로 ResponseBody로 다시 내려주는 테스트용 엔드포인트
     */
    @PostMapping("/cafe-register")
    public ResponseEntity<CafeDTO> registerCafeJson(@RequestBody CafeDTO cafeDTO) {
        // (로깅용) 서버 콘솔에 찍어보기
        System.out.println("서버에서 받은 CafeDTO = " + cafeDTO);

        // 확인용으로 같은 데이터를 그대로 반환
        return ResponseEntity.ok(cafeDTO);
    }
}