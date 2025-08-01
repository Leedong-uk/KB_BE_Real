package org.scoula.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GaScoreDTO {
    //무주택
    @JsonProperty("no_house_period")
    private int noHousePeriod;

    //부양가족수
    @JsonProperty("dependents_nm")
    private int dependentsNm;

    //무주택기간 점수로 변환
    @JsonProperty("no_house_score")
    private int noHouseScore;

    //부양 가족수 점수
    @JsonProperty("dependents_score")
    private int dependentsScore;

    //주택 청약 통장 가입 기간(개월수)
    @JsonProperty("payment_period")
    private int paymentPeriod;

    //통장가입기간 점수화
    @JsonProperty("payment_period_score")
    private int paymentPeriodScore;

    //세대주 여부 (1 or 0)
    @JsonProperty("head_of_household")
    private int headOfHousehold;

    //주택 소유 여부 (1 or 0 )
    @JsonProperty("house_owner")
    private int houseOwner;

    //주택 처분 여부 (1 or 0 )
    @JsonProperty("house_disposal")
    private int houseDisposal;

    //주택 처분 날짜 (처분여부가 1이면 이게 필수임 or null)
    @JsonProperty("disposal_date")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate disposalDate;

    //결혼 여부 (1 or 0)
    @JsonProperty("marital_status")
    private int maritalStatus;
    // 결혼 1이면 결혼 날짜 0 이면 null
    @JsonProperty("wedding_date")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate weddingDate;

    // 총 가점 합계
    @JsonProperty("total_ga_score")
    private int totalGaScore;

}
