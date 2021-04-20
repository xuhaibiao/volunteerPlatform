package cn.xhb.volunteerplatform.constant;

public interface RecordConstant {


    /**
     * 记录状态
     * 0：报名审核中，1：报名未通过(志愿者取消或者工作者拒绝），2：报名通过，3：志愿进行中，4：志愿结束待评价，
     * 5：志愿者已评价但工作者未评价，6：工作者已评价但志愿者未评价，7：全部已评价，8：该记录涉及活动已被删除
     */
    int REGISTRATION_REVIEWING = 0;
    int REGISTRATION_FAILED =1;
    int REGISTRATION_PASSED = 2;
    int ACTIVITY_IN_PROGRESS = 3;
    int ACTIVITY_IS_OVER = 4;
    int VOLUNTEER_HAS_EVALUATE_WORKER_NOT = 5;
    int WORKER_HAS_EVALUATE_VOLUNTEER_NOT= 6;
    int ALL_HAS_EVALUATE = 7;
    int ACTIVITY_HAS_DELETED = 8;




}
