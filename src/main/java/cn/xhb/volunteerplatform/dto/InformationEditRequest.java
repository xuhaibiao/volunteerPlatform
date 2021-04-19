package cn.xhb.volunteerplatform.dto;

import lombok.Data;


@Data
public class InformationEditRequest {

    public Integer id;

    public String name;

    public String phone;

    public String idCard;

    public String password;

    public Integer gender;

    public Integer type;

    public String email;



}
