package cn.xhb.volunteerplatform.dto;

import lombok.Data;

@Data
public class WorkerEvaluateRecordsRequest {
    private Integer recordId;

    private Integer activityId;

    private String activityName;

    private String volunteerName;

    private String volunteerIdCard;

    private Float evaluateScore;

    private String picUrl;

    /**
     * 评价前的记录状态
     */
    private Integer recordStatus;


}
