package cn.xhb.volunteerplatform.dto;

import lombok.Data;

@Data
public class ActivitySearchQuery {
    private String province;
    private String city;
    private String area;
    private String activityName;
}
