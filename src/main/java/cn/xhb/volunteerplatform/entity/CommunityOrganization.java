package cn.xhb.volunteerplatform.entity;

import lombok.Data;

import java.util.Date;

@Data
public class CommunityOrganization {
    private Integer id;

    private String name;

    private Date createTime;

    private String detailAddress;

    private String undertaker;

    private String province;

    private String city;

    private String area;

    private Date updateTime;

    private Integer hasDeleted;

    private Date deleteTime;


}