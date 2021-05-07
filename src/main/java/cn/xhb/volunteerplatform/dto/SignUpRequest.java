package cn.xhb.volunteerplatform.dto;

import cn.xhb.volunteerplatform.entity.CommunityOrganization;
import lombok.Data;

@Data
public class SignUpRequest {

    private String idCard;
    private String username;
    private String password;
    private Integer type;
    private Integer gender;
    private String phone;
    private String detailAddress;
    private String province;
    private String city;
    private String area;
    private String joinCommunityInfo;
    private CommunityOrganization community;

}
