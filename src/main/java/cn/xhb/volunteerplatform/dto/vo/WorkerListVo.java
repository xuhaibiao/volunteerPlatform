package cn.xhb.volunteerplatform.dto.vo;

import cn.xhb.volunteerplatform.entity.Message;
import cn.xhb.volunteerplatform.entity.Worker;
import lombok.Data;

@Data
public class WorkerListVo {
    public Worker worker;

    public Message message;

    /**
     * 申请时间
     */
    public String createTime;
}
