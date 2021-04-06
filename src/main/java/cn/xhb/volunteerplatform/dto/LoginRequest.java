package cn.xhb.volunteerplatform.dto;

import lombok.Data;

@Data
public class LoginRequest {
    private String username;
    private String password;
    private int type;
}
