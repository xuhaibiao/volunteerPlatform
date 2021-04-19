package cn.xhb.volunteerplatform.entity;

import lombok.Data;

import java.util.Date;

@Data
public class Message {
    private Integer id;

    private String title;

    private String content;

    private Integer sender;

    private Integer recipient;

    private Date createTime;

    private Integer type;

    private Integer status;

    private Date updateTime;


}