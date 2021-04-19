package cn.xhb.volunteerplatform.dto;

import cn.xhb.volunteerplatform.entity.CommunityOrganization;
import cn.xhb.volunteerplatform.entity.Volunteer;
import cn.xhb.volunteerplatform.entity.Worker;
import lombok.Data;

import java.util.List;

@Data
public class WorkerGetCommunityResponse {
    private List<Volunteer> volunteers;

    private List<Worker> workers;

    private CommunityOrganization userCommuntity;


}
