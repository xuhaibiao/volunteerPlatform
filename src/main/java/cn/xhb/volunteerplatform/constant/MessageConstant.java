package cn.xhb.volunteerplatform.constant;

public interface MessageConstant {

    /**
     * 消息类型               对应   sender                               recipient
     * 1. 申请加入社区消息           志愿者id                              社区id
     * 2. 申请退出社区消息           志愿者id                              社区id
     * 3. 取消活动报名消息           志愿者id                              --未定
     * 4. 工作者发布社区消息          社区id                                   0
     * 5. 系统消息             0:所有/ 1:发给志愿者 /2:发给社区           0/志愿者id/社区id
     */
    int JOIN_COMMUNITY_TYPE = 1;
    int QUIT_COMMUNITY_TYPE = 2;
    int CANCEL_SIGNUP_TYPE = 3;
    int COMMUNITY_TYPE = 4;
    int SYSTEM_TYPE = 5;



    /**
     * 消息状态 未处理、已处理
     */
    int UNPROCESSED = 0;
    int PROCESSED = 1;




}
