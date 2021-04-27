package cn.xhb.volunteerplatform.dto;

import cn.xhb.volunteerplatform.entity.Activity;
import lombok.Data;

@Data
public class ChangeActivityBanStatusRequest {
    Activity activity;
    String reason;
}
