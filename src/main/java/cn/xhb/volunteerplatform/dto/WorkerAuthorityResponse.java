package cn.xhb.volunteerplatform.dto;

import cn.xhb.volunteerplatform.entity.Worker;
import lombok.Data;

@Data
public class WorkerAuthorityResponse {
    private Worker worker;
    private String createTime;
}
