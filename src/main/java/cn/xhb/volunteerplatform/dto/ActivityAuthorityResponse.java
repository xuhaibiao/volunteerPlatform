package cn.xhb.volunteerplatform.dto;

import cn.xhb.volunteerplatform.entity.Activity;
import lombok.Data;

@Data
public class ActivityAuthorityResponse {

    private Activity activity;

    private String createTime;
}
