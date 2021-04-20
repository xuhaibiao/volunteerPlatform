package cn.xhb.volunteerplatform.dto;

import lombok.Data;

@Data
public class WorkerGetVolunteerEvaluateInfoResponse {
    private Integer id;
    private String name;
    private Integer gender;
    private Float volunteerEvaluateScore;
    private String volunteerEvaluateContent;
}
