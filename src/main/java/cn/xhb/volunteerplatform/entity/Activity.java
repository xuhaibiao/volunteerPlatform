package cn.xhb.volunteerplatform.entity;

import lombok.Data;

import java.util.Date;

@Data
public class Activity {
    private Integer id;

    private String address;

    private String name;

    private String content;

    private Date recruitBeginTime;

    private Date recruitEndTime;

    private Date activityBeginTime;

    private Date activityEndTime;

    private Integer recruitNumber;

    private Float workingHours;

    private String detailAddress;

    private Integer recruitRange;

    private Integer workerId;


}