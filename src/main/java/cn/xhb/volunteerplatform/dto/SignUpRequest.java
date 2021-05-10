package cn.xhb.volunteerplatform.dto;

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

    private String communityName;
    private String communityProvince;
    private String communityCity;
    private String communityArea;
    private String communityDetailAddress;



}
