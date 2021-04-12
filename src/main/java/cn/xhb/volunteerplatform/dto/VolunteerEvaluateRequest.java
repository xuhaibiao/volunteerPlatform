package cn.xhb.volunteerplatform.dto;

import cn.xhb.volunteerplatform.entity.VolunteerRecord;
import lombok.Data;

@Data
public class VolunteerEvaluateRequest {
    private Float score;
    private String content;
    private Integer recordId;

    /**
     * 作为评价后状态转变的依据（重要）
     */
    private Integer recordStatus;
}
