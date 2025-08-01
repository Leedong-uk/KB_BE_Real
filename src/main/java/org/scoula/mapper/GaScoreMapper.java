package org.scoula.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.scoula.dto.GaScoreDTO;

@Mapper
public interface GaScoreMapper {

    // 청약 가점 정보 저장
    void insertGaScore(@Param("dto") GaScoreDTO dto, @Param("userIdx") int userIdx);

    GaScoreDTO findGaScoreByUserIdx(@Param("userIdx") int userIdx);
}
