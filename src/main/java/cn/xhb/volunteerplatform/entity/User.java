package cn.xhb.volunteerplatform.entity;

import lombok.Data;

/**
 * @author HaibiaoXu
 * @date Create in 11:00 2021/3/4
 * @modified By
 */
@Data
public class User {
    private int id;
    private String username;
    private String password;
    private String email;
    private String role;
    private boolean state;
}
