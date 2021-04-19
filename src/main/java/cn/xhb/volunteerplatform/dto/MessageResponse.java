package cn.xhb.volunteerplatform.dto;


import cn.xhb.volunteerplatform.entity.Message;
import lombok.Data;

import java.util.List;

@Data
public class MessageResponse {
    private List<Message> communityMessage;

    private List<Message> systemMessage;


}
