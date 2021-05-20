package cn.xhb.volunteerplatform.dto;

import lombok.Data;

@Data
public class WorkerGetVolunteerEvaluateInfoResponse {
    private Integer id;
    private String name;
    private Integer gender;
    private Float volunteerEvaluateScore;
    private Float workerEvaluate;
    private String volunteerEvaluateContent;
    private String picInfo;
    private String picUrl;
    /**
     * 志愿记录状态
     */
    private Integer status;
    /**
     * 志愿审核结果
     */
    private String approvedResult;
}
