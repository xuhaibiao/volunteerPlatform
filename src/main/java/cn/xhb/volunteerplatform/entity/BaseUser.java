package cn.xhb.volunteerplatform.entity;

import lombok.Data;

@Data
public class BaseUser {
    public Integer id;

    public String name;

    public String phone;

    public String idCard;

    public String password;
}
