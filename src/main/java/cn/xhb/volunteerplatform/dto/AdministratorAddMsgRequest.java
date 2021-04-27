package cn.xhb.volunteerplatform.dto;

import lombok.Data;

@Data
public class AdministratorAddMsgRequest {
    private String title;
    private String content;
    /**
     * 面向用户类型
     */
    private Integer userType;
}
