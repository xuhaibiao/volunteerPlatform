package cn.xhb.volunteerplatform.dto;

import cn.xhb.volunteerplatform.entity.Message;
import cn.xhb.volunteerplatform.entity.Volunteer;
import lombok.Data;




@Data
public class JoinListResponse {

    public Volunteer volunteer;

    public Message message;


}
