package cn.xhb.volunteerplatform.dto;

import lombok.Data;

@Data
public class CommunitySearchQuery {
    private String province;
    private String city;
    private String area;
    private String communityName;
}
