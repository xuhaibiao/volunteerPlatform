package cn.xhb.volunteerplatform.dto;

import cn.xhb.volunteerplatform.entity.Activity;
import lombok.Data;

@Data
public class ActivityResponse {
    private Activity activity;

    /**
     * 活动时间范围
     */
    private String activityTimeRange;

    /**
     * 发起人
     */
    private String sponsor;

    /**
     * 发起社区名
     */
    private String communityName;

    /**
     * 计划招募人数
     */
    private Integer recruitNumber;

    /**
     * 已招募人数
     */
    private Integer hasRecruitedNumber;

    private String activityStatus;
}
