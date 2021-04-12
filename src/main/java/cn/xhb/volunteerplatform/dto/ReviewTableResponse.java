package cn.xhb.volunteerplatform.dto;


import cn.xhb.volunteerplatform.entity.Volunteer;
import lombok.Data;

import java.util.Date;

@Data
public class ReviewTableResponse {
    private Volunteer volunteer;

    /**
     * 报名时间
     */
    private Date registrationTime;

    /**
     * 记录id
     */
    private Integer recordId;
}
