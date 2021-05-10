package cn.xhb.volunteerplatform.dto;


import cn.xhb.volunteerplatform.entity.CommunityOrganization;
import lombok.Data;

@Data
public class ReviewCommunitiesResponse {
    private CommunityOrganization community;
    private String fileName;
    private String fileDate;
}
