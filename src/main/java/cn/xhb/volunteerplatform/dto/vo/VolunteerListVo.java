package cn.xhb.volunteerplatform.dto.vo;

import cn.xhb.volunteerplatform.entity.Message;
import cn.xhb.volunteerplatform.entity.Volunteer;
import lombok.Data;

@Data
public class VolunteerListVo {
    public Volunteer volunteer;

    public Message message;

    /**
     * 申请时间
     */
    public String createTime;
}
