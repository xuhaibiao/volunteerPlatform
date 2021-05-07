package cn.xhb.volunteerplatform.service;

import cn.xhb.volunteerplatform.constant.MessageConstant;
import cn.xhb.volunteerplatform.dto.MessageResponse;
import cn.xhb.volunteerplatform.dto.WorkerAddMsgRequest;
import cn.xhb.volunteerplatform.entity.Activity;
import cn.xhb.volunteerplatform.entity.Message;
import cn.xhb.volunteerplatform.entity.Volunteer;
import cn.xhb.volunteerplatform.entity.Worker;
import cn.xhb.volunteerplatform.mapper.MessageMapper;
import cn.xhb.volunteerplatform.mapper.VolunteerMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class MessageService {

    @Resource
    MessageMapper messageMapper;
    @Resource
    VolunteerMapper volunteerMapper;

    /**
     * 工作者消息获取
     * @param workerId
     * @param communityId
     * @return
     */
    public MessageResponse getWorkerMessages(Integer workerId, Integer communityId) {
        MessageResponse w = new MessageResponse();
        List<Message> communityMessage = new ArrayList<>();
        // 还没通过审核的工作者
        if (communityId != -1) {
            communityMessage = messageMapper.selectWorkerCommunityMessageByRecipient(communityId);
        }
        List<Message> systemMessage = messageMapper.selectSystemMessageByWorker(communityId);
        w.setCommunityMessage(communityMessage);
        w.setSystemMessage(systemMessage);
        return w;
    }

    /**
     * 发布社区消息
     * @param workerAddMsgRequest
     * @return
     */
    public int addCommunityMsg(WorkerAddMsgRequest workerAddMsgRequest) {
        Message message = new Message();
        message.setStatus(0);
        message.setSender(workerAddMsgRequest.getCommunityId());
        message.setType(MessageConstant.COMMUNITY_TYPE);
        String title = workerAddMsgRequest.getTitle();
        message.setTitle("【社区公告】" + title);
        message.setContent(workerAddMsgRequest.getContent());
        // 0无意思
        message.setRecipient(0);
        message.setCreateTime(new Date());

        return messageMapper.insertSelective(message);
    }

    /**
     * 志愿者消息获取
     *
     * @param volunteer
     * @return
     */
    public MessageResponse getVolunteerMessages(Volunteer volunteer) {

        MessageResponse m = new MessageResponse();
        List<Message> systemMessage = messageMapper.selectSystemMessageByVolunteer(volunteer.getId());
        m.setSystemMessage(systemMessage);
        Integer communityId = volunteer.getCommunityId();
        if (communityId != 0) {
            List<Message> communityMsg = messageMapper.selectNotDealBySenderAndRecipientAndType(communityId, null, MessageConstant.COMMUNITY_TYPE);
            m.setCommunityMessage(communityMsg);
        } else {
            m.setCommunityMessage(new ArrayList<>());
        }
        return m;

    }

    /**
     * 添加志愿者取消报名消息
     *
     * @param activityId
     * @param activityName
     * @param communityId
     * @param volunteerName
     * @param volunteerIdCard
     * @return
     */
    public int addCancelSignUpMsg(Integer activityId, String activityName, Integer communityId, String volunteerName, String volunteerIdCard, Integer volunteerId) {
        Message message = new Message();
        message.setStatus(0);
        message.setSender(volunteerId);
        message.setRecipient(communityId);
        message.setType(MessageConstant.CANCEL_SIGNUP_TYPE);
        message.setTitle("【取消报名通知】志愿者[" + volunteerName + "]取消[" + activityName + "]活动报名");
        message.setContent("志愿者：" + volunteerName + "取消[" + activityName + "]活动报名，活动编号为：" + activityId + "，其身份证号为：" + volunteerIdCard);
        message.setCreateTime(new Date());
        return messageMapper.insertSelective(message);
    }


    /**
     * 管理员删除活动提醒社区 系统消息
     *
     * @param communityId
     * @param activity
     * @param reason
     * @return
     */
    public int addChangeActivityBanStatusSystemMsg(Integer communityId, Activity activity, String reason) {
        Message message = new Message();
        message.setStatus(0);
        message.setSender(2);
        message.setRecipient(communityId);
        message.setType(MessageConstant.SYSTEM_TYPE);
        message.setTitle("【删除活动通知】本社区活动编号[" + activity.getId() + "]，活动名[" + activity.getName() + "]的活动已被管理员删除");
        message.setContent("本社区活动编号[" + activity.getId() + "]，活动名[" + activity.getName() + "]的活动已被管理员删除,原因为[" +reason+"]");
        message.setCreateTime(new Date());
        return messageMapper.insertSelective(message);

    }

    public List<Message> getAllMessage() {
        return messageMapper.selectAll();
    }

    /**
     * 管理员发布系统消息
     * @param title
     * @param content
     * @param sender 面向人群
     * @return
     */
    public int addSystemMsg(String title, String content, Integer sender) {
        Message message = new Message();
        message.setStatus(0);
        message.setSender(sender);
        message.setRecipient(0);
        message.setType(MessageConstant.SYSTEM_TYPE);
        message.setTitle("【系统消息】" + title);
        message.setContent(content);
        message.setCreateTime(new Date());
        return messageMapper.insertSelective(message);
    }

    public int addWorkerJoinCommunityMsg(Worker worker, Integer communityId) {
        Message message = new Message();
        message.setStatus(0);
        message.setSender(worker.getId());
        message.setRecipient(communityId);
        message.setType(MessageConstant.WORKER_JOIN_COMMUNITY_TYPE);
        message.setTitle("【工作者注册并申请加入组织】" + "工作者" + worker.getName() + "申请加入");
        message.setContent("工作者注册信息如下：编号["+worker.getId()+"]，身份证号["+worker.getIdCard()+"]，姓名["+worker.getName()+"]");
        message.setCreateTime(new Date());
        return messageMapper.insertSelective(message);
    }

    public List<Message> getNotDealWorkerSignUpMsg(Integer workerId){
        return messageMapper.selectNotDealBySenderAndRecipientAndType(workerId, null, MessageConstant.WORKER_JOIN_COMMUNITY_TYPE);
    }
}
