package cn.xhb.volunteerplatform.service;

import cn.xhb.volunteerplatform.constant.MessageConstant;
import cn.xhb.volunteerplatform.dto.CommunitySearchQuery;
import cn.xhb.volunteerplatform.dto.JoinListResponse;
import cn.xhb.volunteerplatform.dto.JoinRequest;
import cn.xhb.volunteerplatform.entity.CommunityOrganization;
import cn.xhb.volunteerplatform.entity.Message;
import cn.xhb.volunteerplatform.entity.Volunteer;
import cn.xhb.volunteerplatform.mapper.*;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class CommunityService {

    @Resource
    CommunityOrganizationMapper communityOrganizationMapper;
    @Resource
    MessageMapper messageMapper;
    @Resource
    VolunteerMapper volunteerMapper;

    public List<CommunityOrganization> getNotDeletedCommunities() {
        return communityOrganizationMapper.selectNotDeleted();
    }

    public List<CommunityOrganization> getNotDeletedCommunitiesBySearch(CommunitySearchQuery communitySearchQuery) {
        return communityOrganizationMapper.selectNotDeletedBySearch(communitySearchQuery);
    }

    public CommunityOrganization getCommunity(Integer id) {
        return communityOrganizationMapper.selectByPrimaryKey(id);
    }

    /**
     * 工作者获取申请加入组织列表
     * @param communityId
     * @param type
     * @return
     */
    public List<JoinListResponse> getNotDealJoinList(Integer communityId, Integer type) {
        List<Message> messages = messageMapper.selectNotDealBySenderAndRecipientAndType(null, communityId, type);
        List<JoinListResponse> rs = new ArrayList<>();
        for (Message message : messages) {
            JoinListResponse tmp = new JoinListResponse();
            Integer volunteerId = message.getSender();
            Volunteer volunteer = volunteerMapper.selectByPrimaryKey(volunteerId);
            tmp.setVolunteer(volunteer);
            tmp.setMessage(message);
            rs.add(tmp);
        }
        return rs;
    }

    /**
     * 志愿者加入组织申请
     * @param joinRequest
     * @return
     */
    public int joinCommunity(JoinRequest joinRequest) {
        Integer communityId = joinRequest.getCommunityId();
        Volunteer volunteer = joinRequest.getVolunteer();
        List<Message> messages = messageMapper.selectNotDealBySenderAndRecipientAndType(volunteer.getId(), communityId, MessageConstant.JOIN_COMMUNITY_TYPE);
        // 如果已经申请过该组织且该申请还没有被处理，那提醒志愿者请等待
        if (messages.size() > 0) {
            return -1;
        } else {
            Message message = new Message();
            message.setCreateTime(new Date());
            message.setSender(volunteer.getId());
            message.setRecipient(communityId);
            message.setType(MessageConstant.JOIN_COMMUNITY_TYPE);
            message.setTitle("【加入组织申请】志愿者"+volunteer.getName()+"申请加入组织");
            message.setContent("志愿者：" + volunteer.getName() + "申请加入组织，其身份证号为：" + volunteer.getIdCard());
            message.setStatus(MessageConstant.UNPROCESSED);
            return messageMapper.insertSelective(message);
        }

    }

    /**
     * 志愿者退出组织
     *
     * @param id
     * @return
     */
    public int quitCommunity(Integer id, String name, String idCard, Integer community) {
        Volunteer v = new Volunteer();
        v.setId(id);
        v.setCommunityId(0);
        Message message = new Message();
        message.setTitle("【退出组织】志愿者" + name + "退出组织");
        message.setContent("志愿者：" + name + "退出组织，其身份证号为：" + idCard);
        message.setCreateTime(new Date());
        message.setType(MessageConstant.QUIT_COMMUNITY_TYPE);
        message.setSender(id);
        message.setRecipient(community);
        message.setStatus(0);
        int i = messageMapper.insertSelective(message);
        if (i > 0) {
            return volunteerMapper.updateByPrimaryKeySelective(v);
        } else {
            return 0;
        }

    }


    /**
     * 工作者同意加入组织
     * @param volunteerId
     * @param messageId
     * @return
     */
    public int agreeJoin(Integer volunteerId, Integer messageId,Integer communityId) {
        CommunityOrganization community = this.getCommunity(communityId);
        String communityName = community.getName();
        Message message = new Message();
        message.setId(messageId);
        message.setStatus(MessageConstant.PROCESSED);
        message.setUpdateTime(new Date());
        Volunteer v = volunteerMapper.selectByPrimaryKey(volunteerId);
        // 志愿者多个申请情况，此工作者同意之前已有其他社区组织同意
        if (v.getCommunityId() != 0) {
            return -1;
        } else {
            Volunteer volunteer = new Volunteer();
            volunteer.setId(volunteerId);
            volunteer.setCommunityId(communityId);
            int i = messageMapper.updateByPrimaryKeySelective(message);
            if (i > 0) {
                // 同意后给志愿者发送消息
                Message msg = new Message();
                msg.setType(MessageConstant.SYSTEM_TYPE);
                msg.setSender(1);
                msg.setRecipient(volunteerId);
                msg.setTitle("【同意加入组织】"+communityName+" 社区已同意您加入组织");
                msg.setContent("【同意加入组织】"+communityName+" 社区已同意您加入组织");
                msg.setCreateTime(new Date());
                msg.setStatus(0);
                int i1 = messageMapper.insertSelective(msg);
                if (i1 > 0) {
                    return volunteerMapper.updateByPrimaryKeySelective(volunteer);
                } else {
                    return 0;
                }

            } else {
                return 0;
            }
        }


    }

    /**
     * 工作者拒绝加入组织
     * @param volunteerId
     * @param messageId
     * @param communityId
     * @return
     */
    public int refuseJoin(Integer volunteerId, Integer messageId,Integer communityId) {
        CommunityOrganization community = this.getCommunity(communityId);
        String communityName = community.getName();
        Message message = new Message();
        message.setId(messageId);
        message.setStatus(MessageConstant.PROCESSED);
        message.setUpdateTime(new Date());
        int i = messageMapper.updateByPrimaryKeySelective(message);
        if (i > 0) {
            // 拒绝后给志愿者发送消息
            Message msg = new Message();
            msg.setType(MessageConstant.SYSTEM_TYPE);
            msg.setSender(1);
            msg.setRecipient(volunteerId);
            msg.setTitle("【拒绝加入组织】"+communityName+" 社区拒绝您加入组织");
            msg.setContent("【拒绝加入组织】"+communityName+" 社区拒绝您加入组织");
            msg.setCreateTime(new Date());
            msg.setStatus(0);
            return messageMapper.insertSelective(msg);

        } else {
            return 0;
        }
    }





}
