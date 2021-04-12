package cn.xhb.volunteerplatform.dto;

import cn.xhb.volunteerplatform.entity.Worker;
import lombok.Data;

import java.util.Date;

@Data
public class AddActivityRquest {
    private String  name;
    private String province;
    private String city;
    private String area;
    private String detailAddress;
    private Float workingHours;
    private Integer recruitNumber;
    private Date[] recruitDateRange;
    private Date[] activityDateRange;
    private String recruitRange;
    private String content;
    private Worker worker;

}
