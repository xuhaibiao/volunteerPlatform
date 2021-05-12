package cn.xhb.volunteerplatform.dto;

import lombok.Data;

@Data
public class AddActivityRquest {
    private String  name;
    private String province;
    private String city;
    private String area;
    private String detailAddress;
    private Float workingHours;
    private Integer recruitNumber;
//    private Date[] recruitDateRange;
//    private Date[] activityDateRange;
    private String[] recruitDateRange;
    private String[] activityDateRange;
    private String recruitRange;
    private String content;
    private Integer workerId;

}
