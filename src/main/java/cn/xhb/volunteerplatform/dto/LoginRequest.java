package cn.xhb.volunteerplatform.dto;

import lombok.Data;

@Data
public class LoginRequest {
    private String idCard;
    private String password;
    private int type;
}
