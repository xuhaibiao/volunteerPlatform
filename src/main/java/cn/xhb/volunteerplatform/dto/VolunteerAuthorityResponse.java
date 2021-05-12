package cn.xhb.volunteerplatform.dto;

import cn.xhb.volunteerplatform.entity.Volunteer;
import lombok.Data;

@Data
public class VolunteerAuthorityResponse {
    private Volunteer volunteer;
    private String createTime;
}
