package cn.xhb.volunteerplatform.service;

import cn.xhb.volunteerplatform.constant.MessageConstant;
import cn.xhb.volunteerplatform.dto.MessageResponse;
import cn.xhb.volunteerplatform.dto.WorkerAddMsgRequest;
import cn.xhb.volunteerplatform.entity.Message;
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
        List<Message> communityMessage = messageMapper.selectWorkerCommunityMessageByRecipient(communityId);
        List<Message> systemMessage = messageMapper.selectSystemMessage();

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
     * 志愿者消息获取 todo:
     * @param volunteerId
     * @param communityId
     * @return
     */
    public MessageResponse getVolunteerMessages(Integer communityId) {
        MessageResponse m = new MessageResponse();
        List<Message> systemMessage = messageMapper.selectSystemMessage();
        m.setSystemMessage(systemMessage);
        if (communityId != 0) {
            List<Message> communityMsg = messageMapper.selectNotDealBySenderAndRecipientAndType(communityId, null, MessageConstant.COMMUNITY_TYPE);
            m.setCommunityMessage(communityMsg);
        } else {
            m.setCommunityMessage(new ArrayList<>());
        }
        return m;

    }
}