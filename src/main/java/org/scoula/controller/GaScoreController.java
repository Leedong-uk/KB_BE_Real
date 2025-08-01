package org.scoula.controller;

import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.scoula.dto.GaScoreDTO;
import org.scoula.dto.swagger.GaScore.SwaggerGaScoreRequest;
import org.scoula.service.GaScoreService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@Log4j2
@RestController
@RequestMapping("/v1/ga-score")
@RequiredArgsConstructor
@Api(tags = "가점 API", description = "사용자 설문 기반 가점 계산 및 저장")
public class GaScoreController {

    private final GaScoreService gaScoreService;

    @PostMapping
    @ApiOperation(value = "가점 계산 정보 저장 및 응답", notes = "사용자의 설문 응답을 저장하고 가점 계산 결과를 반환합니다.")
    @ApiResponses({
            @ApiResponse(code = 200, message = "가점 계산 성공", response = GaScoreDTO.class),
            @ApiResponse(code = 400, message = "잘못된 요청"),
            @ApiResponse(code = 500, message = "서버 오류")
    })
    public ResponseEntity<GaScoreDTO> saveGaScore(@RequestBody SwaggerGaScoreRequest requestDto,
                                                 @ApiParam(hidden = true) HttpServletRequest request) {
        log.info("POST /v1/ga-score request: {}", requestDto);

        GaScoreDTO responseDto = gaScoreService.saveGaScore(requestDto, request);

        return ResponseEntity.ok(responseDto);
    }

    @GetMapping
    @ApiOperation(value = "가점 계산 결과 조회", notes = "저장된 가점 계산 결과를 반환합니다.")
    @ApiResponses({
            @ApiResponse(code = 200, message = "가점 조회 성공", response = GaScoreDTO.class),
            @ApiResponse(code = 404, message = "데이터 없음"),
            @ApiResponse(code = 500, message = "서버 오류")
    })
    public ResponseEntity<GaScoreDTO> getGaScore(@ApiParam(hidden = true) HttpServletRequest request) {
        log.info("GET /v1/ga-score");

        GaScoreDTO responseDto = gaScoreService.getGaScore(request);

        if (responseDto == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(responseDto);
    }
}
