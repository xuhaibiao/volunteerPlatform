package cn.xhb.volunteerplatform.dto;

import cn.xhb.volunteerplatform.entity.Volunteer;
import lombok.Data;

/**
 * worker审核报名请求
 */
@Data
public class JoinRequest {
    private Volunteer volunteer;

    private Integer communityId;
}
