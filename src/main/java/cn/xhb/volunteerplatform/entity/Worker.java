package cn.xhb.volunteerplatform.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Setter;

import java.util.Date;

@EqualsAndHashCode(callSuper = true)
@Data
@Setter
public class Worker extends BaseUser{
    private Integer gender;

    private String address;

    private String email;

    private Integer communityId;

    private Integer banStatus;

    private Date createTime;

    private Date deleteTime;


}