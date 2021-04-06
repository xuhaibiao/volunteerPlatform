package cn.xhb.volunteerplatform.entity;

import lombok.Data;

import java.util.Date;

@Data
public class CommunityOrganization {
    private Integer id;

    private String name;

    private Date createTime;

    private String address;

    private String undertaker;


}