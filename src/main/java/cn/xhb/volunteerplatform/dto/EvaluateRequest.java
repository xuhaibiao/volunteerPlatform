package cn.xhb.volunteerplatform.dto;

import cn.xhb.volunteerplatform.entity.VolunteerRecord;
import lombok.Data;

@Data
public class EvaluateRequest {
    private Float score;
    private String content;
    private Integer recordId;
}
