package cn.xhb.volunteerplatform.dto;

import cn.xhb.volunteerplatform.dto.vo.ChinaMapVo;
import cn.xhb.volunteerplatform.dto.vo.FiveYearNumberVo;
import lombok.Data;

import java.util.List;

@Data
public class StatisticsDataResponse {
    private FiveYearNumberVo fiveYearNumberVo;

    private int[] sexRatio;

    private List<ChinaMapVo> provinceActivityNum;

}
