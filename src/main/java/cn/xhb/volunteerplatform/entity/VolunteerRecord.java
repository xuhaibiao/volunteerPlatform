package cn.xhb.volunteerplatform.entity;

import lombok.Data;

@Data
public class VolunteerRecord {
    private Integer id;

    private Integer volunteerId;

    private Integer activityId;

    private Integer status;

    private String volunteerEvaluate;

    private Float workerEvaluate;


}