package cn.xhb.volunteerplatform.entity;

import lombok.Data;

import java.util.Date;

@Data
public class VolunteerRecord {
    private Integer id;

    private Integer volunteerId;

    private Integer activityId;

    private Integer status;

    private String volunteerEvaluateContent;

    private Float volunteerEvaluateScore;

    private Float workerEvaluate;

    private Date createTime;

    private Date deleteTime;

    private Date updateTime;

    private String picInfo;


}