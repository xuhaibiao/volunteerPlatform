package cn.xhb.volunteerplatform.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class Activity implements Serializable {
    private Integer id;

    private String province;
    private String city;
    private String area;

    private String name;

    private Date recruitBeginTime;

    private Date recruitEndTime;

    private Date activityBeginTime;

    private Date activityEndTime;

    private Integer recruitNumber;

    private Float workingHours;

    private String detailAddress;

    private Integer recruitRange;

    private Integer workerId;

    private Date createTime;

    private Date deleteTime;

    private Integer hasDeleted;

    private Date updateTime;

    private String content;


    private Integer banStatus;

    private String picInfo;


}