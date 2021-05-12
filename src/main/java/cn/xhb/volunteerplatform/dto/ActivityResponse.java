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
     * 招募时间范围
     */
    private String recruitTimeRange;

    /**
     * 发起人
     */
    private String sponsor;

    /**
     * 联系方式
     */
    private String sponsorPhoneNumber;
    /**
     * 发起社区名
     */
    private String communityName;

    /**
     * 发起社区id
     */
    private Integer communityId;

    /**
     * 计划招募人数
     */
    private Integer recruitNumber;

    /**
     * 已招募(报名）人数
     */
    private Integer hasRecruitedNumber;

    /**
     * 审核通过人数
     */
    private Integer hasAgreeNumber;

    /**
     * 活动状态
     */
    private String activityStatus;

    private String picUrl;


}
