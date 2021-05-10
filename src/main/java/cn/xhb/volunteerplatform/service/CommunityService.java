package cn.xhb.volunteerplatform.service;

import cn.xhb.volunteerplatform.constant.MessageConstant;
import cn.xhb.volunteerplatform.dto.*;
import cn.xhb.volunteerplatform.dto.vo.VolunteerListVo;
import cn.xhb.volunteerplatform.dto.vo.WorkerListVo;
import cn.xhb.volunteerplatform.entity.CommunityOrganization;
import cn.xhb.volunteerplatform.entity.Message;
import cn.xhb.volunteerplatform.entity.Volunteer;
import cn.xhb.volunteerplatform.entity.Worker;
import cn.xhb.volunteerplatform.mapper.CommunityOrganizationMapper;
import cn.xhb.volunteerplatform.mapper.MessageMapper;
import cn.xhb.volunteerplatform.mapper.VolunteerMapper;
import cn.xhb.volunteerplatform.mapper.WorkerMapper;
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
    @Resource
    WorkerMapper workerMapper;

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
     * @return
     */
    public JoinListResponse getNotDealJoinList(Integer communityId) {

        List<Message> volunteerJoinMessages = messageMapper.selectNotDealBySenderAndRecipientAndType(null, communityId, MessageConstant.JOIN_COMMUNITY_TYPE);
        List<Message> workerJoinMessages = messageMapper.selectNotDealBySenderAndRecipientAndType(null, communityId, MessageConstant.WORKER_JOIN_COMMUNITY_TYPE);
        JoinListResponse rs = new JoinListResponse();
        List<VolunteerListVo> volunteerJoinListVo = new ArrayList<>();
        List<WorkerListVo> workerJoinListVo = new ArrayList<>();
        for (Message message : volunteerJoinMessages) {
            // 如果是志愿者申请加入
            VolunteerListVo tmp = new VolunteerListVo();
            Integer volunteerId = message.getSender();
            Volunteer volunteer = volunteerMapper.selectByPrimaryKey(volunteerId);
            tmp.setVolunteer(volunteer);
            tmp.setMessage(message);
            volunteerJoinListVo.add(tmp);

        }
        for (Message message : workerJoinMessages) {
            // 如果是工作者申请加入
            WorkerListVo tmp = new WorkerListVo();
            Integer workerId = message.getSender();
            Worker worker = workerMapper.selectByPrimaryKey(workerId);
            tmp.setWorker(worker);
            tmp.setMessage(message);
            workerJoinListVo.add(tmp);
        }
        rs.setVolunteerJoinList(volunteerJoinListVo);
        rs.setWorkerJoinList(workerJoinListVo);
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
            message.setTitle("【志愿者加入组织申请】志愿者"+volunteer.getName()+"申请加入组织");
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
        message.setTitle("【志愿者退出组织】志愿者" + name + "退出组织");
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
     * @param sender
     * @param messageId
     * @param communityId
     * @param type
     * @return
     */
    public int agreeJoin(Integer sender, Integer messageId, Integer communityId, Integer type) {

        CommunityOrganization community = this.getCommunity(communityId);
        String communityName = community.getName();
        Message message = new Message();
        message.setId(messageId);
        message.setStatus(MessageConstant.PROCESSED);
        message.setUpdateTime(new Date());
        if (type == MessageConstant.WORKER_JOIN_COMMUNITY_TYPE) {
            Worker worker = new Worker();
            worker.setId(sender);
            worker.setCommunityId(communityId);
            int i = messageMapper.updateByPrimaryKeySelective(message);
            if (i > 0) {
                return workerMapper.updateByPrimaryKeySelective(worker);
            } else {
                return 0;
            }
        } else {
            Volunteer v = volunteerMapper.selectByPrimaryKey(sender);
            // 志愿者多个申请情况，此工作者同意之前已有其他社区组织同意
            if (v.getCommunityId() != 0) {
                return -1;
            } else {
                Volunteer volunteer = new Volunteer();
                volunteer.setId(sender);
                volunteer.setCommunityId(communityId);
                int i = messageMapper.updateByPrimaryKeySelective(message);
                if (i > 0) {
                    // 同意后给志愿者发送消息
                    Message msg = new Message();
                    msg.setType(MessageConstant.SYSTEM_TYPE);
                    msg.setSender(1);
                    msg.setRecipient(sender);
                    msg.setTitle("【同意加入组织】" + communityName + " 社区已同意您加入组织");
                    msg.setContent("【同意加入组织】" + communityName + " 社区已同意您加入组织");
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
    }


    /**
     * 工作者拒绝加入组织
     * @param sender
     * @param messageId
     * @param communityId
     * @param type
     * @return
     */
    public int refuseJoin(Integer sender, Integer messageId,Integer communityId, Integer type) {
        CommunityOrganization community = this.getCommunity(communityId);
        String communityName = community.getName();
        Message message = new Message();
        message.setId(messageId);
        message.setStatus(MessageConstant.PROCESSED);
        message.setUpdateTime(new Date());
        int i = messageMapper.updateByPrimaryKeySelective(message);
        if (i > 0) {
            if (type == MessageConstant.WORKER_JOIN_COMMUNITY_TYPE) {
                // 如果拒绝工作者加入，那么工作者注册失败，需要重新注册
                return workerMapper.deleteByPrimaryKey(sender);
            } else {
                // 拒绝后给志愿者发送消息
                Message msg = new Message();
                msg.setType(MessageConstant.SYSTEM_TYPE);
                msg.setSender(1);
                msg.setRecipient(sender);
                msg.setTitle("【拒绝加入组织】"+communityName+" 社区拒绝您加入组织");
                msg.setContent("【拒绝加入组织】"+communityName+" 社区拒绝您加入组织");
                msg.setCreateTime(new Date());
                msg.setStatus(0);
                return messageMapper.insertSelective(msg);
            }

        } else {
            return 0;
        }
    }


    public List<LoadAllCommunityResponse> getAllNotDelCommunity() {
        List<CommunityOrganization> communityOrganizations = communityOrganizationMapper.selectNotDeleted();
        List<LoadAllCommunityResponse> rs = new ArrayList<>(communityOrganizations.size());
        for (CommunityOrganization communityOrganization : communityOrganizations) {
            LoadAllCommunityResponse tmp = new LoadAllCommunityResponse();
            tmp.setValue(communityOrganization.getName() + "(编号:" + communityOrganization.getId() + ")");
            rs.add(tmp);
        }
        return rs;
    }

    public int add(CommunityOrganization community) {
        return communityOrganizationMapper.insertSelective(community);
    }

    public List<ReviewCommunitiesResponse> getAllNeedReviewCommunities() {
        List<CommunityOrganization> communities = communityOrganizationMapper.selectNotApproved();
        List<ReviewCommunitiesResponse> rs = new ArrayList<>(communities.size());
        for (CommunityOrganization community : communities) {
            ReviewCommunitiesResponse tmp = new ReviewCommunitiesResponse();
            tmp.setCommunity(community);
            String fileinfo = community.getFileinfo();
            String[] fileInfos = fileinfo.split(";");
            tmp.setFileName(fileInfos[0]);
            tmp.setFileDate(fileInfos[1]);
            rs.add(tmp);
        }
        return rs;
    }

    public int approved(CommunityOrganization community) {
        community.setUpdateTime(new Date());
        community.setHasApproved(1);
        int i = communityOrganizationMapper.updateByPrimaryKeySelective(community);
        if (i > 0) {
            Worker worker = new Worker();
            worker.setId(community.getWorkerId());
            worker.setCommunityId(community.getId());
            return workerMapper.updateByPrimaryKeySelective(worker);
        } else {
            return 0;
        }

    }

    public int refuseApproved(CommunityOrganization community) {
        int i = communityOrganizationMapper.deleteByPrimaryKey(community.getId());
        if (i > 0) {
            return workerMapper.deleteByPrimaryKey(community.getWorkerId());
        } else {
            return 0;
        }
    }
}
