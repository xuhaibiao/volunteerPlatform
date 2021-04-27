package cn.xhb.volunteerplatform.dto;

import cn.xhb.volunteerplatform.dto.vo.FiveYearNumberVo;
import lombok.Data;

@Data
public class StatisticsDataResponse {
    private FiveYearNumberVo fiveYearNumberVo;

    private int[] sexRatio;

}
