package cn.xhb.volunteerplatform.dto;

import cn.xhb.volunteerplatform.entity.Activity;
import cn.xhb.volunteerplatform.entity.CommunityOrganization;
import cn.xhb.volunteerplatform.entity.Volunteer;
import lombok.Data;

import java.util.List;

@Data
public class VolunteerGetCommunityResponse {
    private List<CommunityOrganization> communityOrganizations;

    private CommunityOrganization userCommuntity;

    private Volunteer volunteer;


}
