package cn.xhb.volunteerplatform.dto;

import cn.xhb.volunteerplatform.entity.VolunteerRecord;
import lombok.Data;

/**
 * 志愿者取消活动报名请求
 */
@Data
public class VolunteerCancelSignUpRequest {
    private VolunteerRecord volunteerRecord;
    private Integer communityId;
    private Integer activityId;
    private String activityName;

}
