package org.scoula.controller;

import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import org.scoula.dto.UserSelectedDTO;
import org.scoula.security.util.JwtProcessor;
import org.scoula.service.UserSelectedService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/v1/user/preferences")
@RequiredArgsConstructor
@Api(tags = "사용자 선호 API", description = "사용자의 선호 정보를 저장·조회합니다")
public class UserSelectedController {

    private final UserSelectedService userSelectedService;
    private final JwtProcessor jwtProcessor;

    private String extractUserIdFromToken(String token) {
        return jwtProcessor.getUsername(token.replace("Bearer ", ""));
    }

    @PostMapping
    @ApiOperation(value = "선호 정보 저장", notes = "로그인된 사용자의 선호 정보를 저장합니다.")
    @ApiResponses({
            @ApiResponse(code = 200, message = "저장 성공"),
            @ApiResponse(code = 400, message = "잘못된 요청"),
            @ApiResponse(code = 401, message = "인증 실패")
    })

    public ResponseEntity<Map<String, String>> saveUserSelected(
            @ApiParam(value = "저장할 사용자 선호 정보", required = true)
            @RequestBody UserSelectedDTO userSelectedDTO,
            @ApiParam(hidden = true)@RequestHeader("Authorization") String token
    ) {
        String userId = extractUserIdFromToken(token);
        userSelectedService.saveAllPreferences(userId, userSelectedDTO);
        Map<String, String> response = new HashMap<>();
        response.put("message", "사용자 선호 정보가 저장되었습니다.");
        return ResponseEntity.ok(response);
    }

    @GetMapping
    @ApiOperation(value = "❌❌❌❌❌❌❌❌❌❌❌❌❌❌❌❌❌❌❌❌❌❌", notes = "로그인된 사용자의 선호 정보를 조회합니다.")
    @ApiResponses({
            @ApiResponse(code = 200, message = "조회 성공", response = UserSelectedDTO.class),
            @ApiResponse(code = 401, message = "인증 실패"),
            @ApiResponse(code = 404, message = "선호 정보 없음")
    })

    public ResponseEntity<UserSelectedDTO> getUserSelected(
            @ApiParam(hidden = true) @RequestHeader("Authorization") String token
    ) {
        String userId = extractUserIdFromToken(token);
        UserSelectedDTO dto = userSelectedService.getUserSelected(userId);
        return ResponseEntity.ok(dto);

    }

}
