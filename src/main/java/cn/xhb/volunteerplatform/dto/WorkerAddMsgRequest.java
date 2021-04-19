package cn.xhb.volunteerplatform.dto;

import lombok.Data;

@Data
public class WorkerAddMsgRequest {
    private String title;
    private String content;
    private Integer workerId;
    private Integer communityId;

}
