package cn.xhb.volunteerplatform.dto;

import cn.xhb.volunteerplatform.entity.VolunteerRecord;
import lombok.Data;

@Data
public class VolunteerRecordResponse {
    private VolunteerRecord volunteerRecord;

    /**
     * 活动
     */
    private ActivityResponse activityResponse;
    
}
