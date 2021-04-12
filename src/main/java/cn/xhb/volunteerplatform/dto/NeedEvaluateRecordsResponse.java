package cn.xhb.volunteerplatform.dto;

import lombok.Data;

@Data
public class NeedEvaluateRecordsResponse {
    private Integer recordId;

    private Integer activityId;

    private String activityName;

    private String volunteerName;

    private Float evaluateScore;

    /**
     * 作为评价后状态转变的依据（重要）
     */
    private Integer recordStatus;



}
