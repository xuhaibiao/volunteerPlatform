package cn.xhb.volunteerplatform.dto;

import lombok.Data;

@Data
public class EditActivityRquest {
    private Integer id;
    private String name;
    private String province;
    private String city;
    private String area;
    private String detailAddress;
    private Float workingHours;
    private Integer recruitNumber;
    private String[] recruitDateRange;
    private String[] activityDateRange;
    //    private Date recruitBeginTime;
//    private Date recruitEndTime;
//    private Date activityBeginTime;
//    private Date activityEndTime;
    private String recruitRange;
    private String content;
    private Integer workerId;

}
