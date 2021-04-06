package cn.xhb.volunteerplatform.entity;

import lombok.Data;

import java.util.Date;

@Data
public class Volunteer extends BaseUser{
    private Integer gender;

    private Float volunteerHours;

    private Float volunteerScore;

    private Date createTime;

    private Integer banStatus;

    private String address;

    private Integer communityId;

    private Date deleteTime;


}