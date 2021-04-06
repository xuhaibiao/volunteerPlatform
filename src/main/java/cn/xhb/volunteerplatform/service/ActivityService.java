package cn.xhb.volunteerplatform.service;

import cn.xhb.volunteerplatform.constant.ActivityConstant;
import cn.xhb.volunteerplatform.dto.ActivityResponse;
import cn.xhb.volunteerplatform.entity.Activity;
import cn.xhb.volunteerplatform.entity.CommunityOrganization;
import cn.xhb.volunteerplatform.entity.Worker;
import cn.xhb.volunteerplatform.mapper.ActivityMapper;
import cn.xhb.volunteerplatform.mapper.CommunityOrganizationMapper;
import cn.xhb.volunteerplatform.mapper.VolunteerRecordMapper;
import cn.xhb.volunteerplatform.mapper.WorkerMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class ActivityService {
    @Resource
    ActivityMapper activityMapper;
    @Resource
    WorkerMapper workerMapper;
    @Resource
    VolunteerRecordMapper volunteerRecordMapper;
    @Resource
    CommunityOrganizationMapper communityOrganizationMapper;

    public List<ActivityResponse> getActivities() {
         List<Activity> activities = activityMapper.selectAll();
         List<ActivityResponse> rsList = new ArrayList<>(activities.size());
        for (Activity activity : activities) {
            ActivityResponse activityResponse = new ActivityResponse();
            activityResponse.setActivity(activity);
//            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String begin = sdf.format(activity.getActivityBeginTime());
            String end = sdf.format(activity.getActivityEndTime());
            activityResponse.setActivityTimeRange(begin + "至" + end);
            Worker worker = workerMapper.selectByPrimaryKey(activity.getWorkerId());
            CommunityOrganization communityOrganization = communityOrganizationMapper.selectByPrimaryKey(worker.getCommunityId());
            activityResponse.setCommunityName(communityOrganization.getName());
            activityResponse.setSponsor(worker.name);
            Date now = new Date();
            if(now.before(activity.getRecruitBeginTime())){
                activityResponse.setActivityStatus(ActivityConstant.RECRUIT_NOT_STARTED);
            } else if (now.before(activity.getRecruitEndTime())) {
                activityResponse.setActivityStatus(ActivityConstant.RECRUITING);
            } else {
                activityResponse.setActivityStatus(ActivityConstant.RECRUIT_OVER);
            }
            int signedUp = volunteerRecordMapper.countByActivity(activity.getId());
            activityResponse.setHasRecruitedNumber(signedUp);
            rsList.add(activityResponse);
        }
        return rsList;
    }

    public List<ActivityResponse> getActivitiesByAddress(String address) {
        List<Activity> activities = activityMapper.selectByAddress(address);
        List<ActivityResponse> rsList = new ArrayList<>(activities.size());
        for (Activity activity : activities) {
            ActivityResponse activityResponse = new ActivityResponse();
            activityResponse.setActivity(activity);
//            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String begin = sdf.format(activity.getActivityBeginTime());
            String end = sdf.format(activity.getActivityEndTime());
            activityResponse.setActivityTimeRange(begin + "至" + end);
            Worker worker = workerMapper.selectByPrimaryKey(activity.getWorkerId());
            CommunityOrganization communityOrganization = communityOrganizationMapper.selectByPrimaryKey(worker.getCommunityId());
            activityResponse.setCommunityName(communityOrganization.getName());
            activityResponse.setSponsor(worker.name);
            Date now = new Date();
            if(now.before(activity.getRecruitBeginTime())){
                activityResponse.setActivityStatus(ActivityConstant.RECRUIT_NOT_STARTED);
            } else if (now.before(activity.getRecruitEndTime())) {
                activityResponse.setActivityStatus(ActivityConstant.RECRUITING);
            } else {
                activityResponse.setActivityStatus(ActivityConstant.RECRUIT_OVER);
            }
            int signedUp = volunteerRecordMapper.countByActivity(activity.getId());
            activityResponse.setHasRecruitedNumber(signedUp);
            rsList.add(activityResponse);
        }
        return rsList;
    }
}
