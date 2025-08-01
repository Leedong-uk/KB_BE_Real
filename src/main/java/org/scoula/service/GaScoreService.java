package org.scoula.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.scoula.dto.GaScoreDTO;
import org.scoula.dto.swagger.GaScore.SwaggerGaScoreRequest;
import org.scoula.mapper.AccountMapper;
import org.scoula.mapper.GaScoreMapper;
import org.scoula.mapper.UserMapper;
import org.scoula.security.util.JwtProcessor;
import org.scoula.util.TokenUtils;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

@Service
@RequiredArgsConstructor
@Log4j2
public class GaScoreService {

    private final GaScoreMapper gaScoreMapper;
    private final UserMapper userMapper;
    private final AccountMapper accountMapper; // ✅ 추가
    private final TokenUtils tokenUtils;
    private final JwtProcessor jwtProcessor; // JWT 파싱용

    public GaScoreDTO saveGaScore(SwaggerGaScoreRequest reqeustDTO, HttpServletRequest request) {
        // 1. 헤더에서 토큰 추출
        String bearerToken = request.getHeader("Authorization");
        String accessToken = tokenUtils.extractAccessToken(bearerToken);

        // 2. JWT에서 userIdx 추출
        String userId = jwtProcessor.getUsername(accessToken);
        int userIdx = userMapper.findUserIdxByUserId(userId);

        // 3. 점수 계산
        int noHouseScore = Math.min(reqeustDTO.getNoHousePeriod() * 2, 32);
        int dependentsScore = Math.min((reqeustDTO.getDependentsNm() + 1) * 5, 35);
        int paymentPeriod = calculatePaymentPeriod(userIdx);
        int paymentPeriodScore = calculatePaymentPeriodScore(paymentPeriod);
        int totalScore = noHouseScore + dependentsScore + paymentPeriodScore;

        // 4. DTO 생성 (Builder 사용)
        GaScoreDTO responseDTO = GaScoreDTO.builder()
                .noHousePeriod(reqeustDTO.getNoHousePeriod())
                .noHouseScore(noHouseScore)
                .dependentsNm(reqeustDTO.getDependentsNm())
                .dependentsScore(dependentsScore)
                .paymentPeriod(paymentPeriod)
                .paymentPeriodScore(paymentPeriodScore)
                .maritalStatus(reqeustDTO.getMaritalStatus())
                .weddingDate(reqeustDTO.getMaritalStatus() == 1 ? LocalDate.parse(reqeustDTO.getWeddingDate()) : null)
                .houseDisposal(reqeustDTO.getHouseDisposal())
                .disposalDate(reqeustDTO.getHouseDisposal() == 1 ? LocalDate.parse(reqeustDTO.getDisposalDate()) : null)
                .houseOwner(reqeustDTO.getHouseOwner())
                .headOfHousehold(reqeustDTO.getHeadOfHousehold())
                .totalGaScore(totalScore)
                .build();

        // 5. DB 저장
        gaScoreMapper.insertGaScore(responseDTO, userIdx);
        log.info("사용자 {} 청약 가점 저장 완료: totalScore={}", userIdx, totalScore);

        return responseDTO;
    }

    public GaScoreDTO getGaScore(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        String accessToken = tokenUtils.extractAccessToken(bearerToken);

        String userId = jwtProcessor.getUsername(accessToken);
        int userIdx = userMapper.findUserIdxByUserId(userId);

        GaScoreDTO dto = gaScoreMapper.findGaScoreByUserIdx(userIdx);

        if (dto != null) {
            log.info("사용자 {} 청약 가점 조회 완료: totalScore={}", userIdx, dto.getTotalGaScore());
        } else {
            log.warn("사용자 {} 청약 가점 조회 실패 - 데이터 없음", userIdx);
        }

        return dto;
    }


    // AccountMapper 활용해서 계좌 개설일 조회
    private int calculatePaymentPeriod(int userIdx) {
        LocalDate startDate = accountMapper.findAccountStartDate(userIdx);
        if (startDate == null) return 0;

        return (int) ChronoUnit.MONTHS.between(startDate, LocalDate.now());
    }

    private int calculatePaymentPeriodScore(int months) {
        int years = months / 12;
        if (months < 6) return 1;
        if (months < 12) return 2;
        if (years < 2) return 3;
        if (years < 3) return 4;
        if (years < 4) return 5;
        if (years < 5) return 6;
        if (years < 6) return 7;
        if (years < 7) return 8;
        if (years < 8) return 9;
        if (years < 9) return 10;
        if (years < 10) return 11;
        if (years < 11) return 12;
        if (years < 12) return 13;
        if (years < 13) return 14;
        if (years < 14) return 15;
        if (years < 15) return 16;
        return 17;
    }
}
