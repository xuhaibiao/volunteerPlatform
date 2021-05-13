package cn.xhb.volunteerplatform.service;

import cn.xhb.volunteerplatform.constant.ActivityConstant;
import cn.xhb.volunteerplatform.constant.RecordConstant;
import cn.xhb.volunteerplatform.dto.*;
import cn.xhb.volunteerplatform.entity.Activity;
import cn.xhb.volunteerplatform.entity.CommunityOrganization;
import cn.xhb.volunteerplatform.entity.VolunteerRecord;
import cn.xhb.volunteerplatform.entity.Worker;
import cn.xhb.volunteerplatform.mapper.ActivityMapper;
import cn.xhb.volunteerplatform.mapper.CommunityOrganizationMapper;
import cn.xhb.volunteerplatform.mapper.VolunteerRecordMapper;
import cn.xhb.volunteerplatform.mapper.WorkerMapper;
import cn.xhb.volunteerplatform.util.DateUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
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

    public List<ActivityResponse> activityToActivityResponse(List<Activity> activities) {
        if (activities == null || activities.size() == 0) {
            return new ArrayList<>();
        }
        List<ActivityResponse> rsList = new ArrayList<>(activities.size());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        for (Activity activity : activities) {
            ActivityResponse activityResponse = new ActivityResponse();
            activityResponse.setActivity(activity);

            String abegin = sdf.format(activity.getActivityBeginTime());
            String aend = sdf.format(activity.getActivityEndTime());
            String rbegin = sdf.format(activity.getRecruitBeginTime());
            String rend = sdf.format(activity.getRecruitEndTime());
            activityResponse.setActivityTimeRange(abegin + " 至 " + aend);
            activityResponse.setRecruitTimeRange(rbegin + " 至 " + rend);

            Worker worker = workerMapper.selectByPrimaryKey(activity.getWorkerId());
            CommunityOrganization communityOrganization = communityOrganizationMapper.selectByPrimaryKey(worker.getCommunityId());
            activityResponse.setCommunityName(communityOrganization.getName());
            activityResponse.setCommunityId(communityOrganization.getId());
            activityResponse.setSponsor(worker.name);
            activityResponse.setSponsorPhoneNumber(worker.getPhone());
            // 活动状态
            Date now = new Date();
            if(now.before(activity.getRecruitBeginTime())){
                activityResponse.setActivityStatus(ActivityConstant.RECRUIT_NOT_STARTED);
            } else if (now.before(activity.getRecruitEndTime())) {
                activityResponse.setActivityStatus(ActivityConstant.RECRUITING);
            } else if(now.before(activity.getActivityBeginTime())){
                activityResponse.setActivityStatus(ActivityConstant.ACITVITY_NOT_STARTED);
            } else if (now.before(activity.getActivityEndTime())) {
                activityResponse.setActivityStatus(ActivityConstant.ACITVITY_DOING);
            } else {
                activityResponse.setActivityStatus(ActivityConstant.ACITVITY_OVER);
            }
            // 已报名的人数（不包括取消报名或者被拒绝的人数，即不包括记录状态为1的记录数)
            int signedUpCount = volunteerRecordMapper.countByActivity(activity.getId());
            activityResponse.setHasRecruitedNumber(signedUpCount);
            // 获取该活动已经报名审核通过的人数
            int agreeNum;
            if (activityResponse.getActivityStatus() == ActivityConstant.RECRUIT_NOT_STARTED) {
                agreeNum = 0;
            }else{
                agreeNum = volunteerRecordMapper.countByActivityIdAndStatus(activity.getId(), RecordConstant.REGISTRATION_PASSED);
            }
            activityResponse.setHasAgreeNumber(agreeNum);
            String[] picInfos = activity.getPicInfo().split(";");
            String picName = picInfos[0];
            String picDate = picInfos[1];
            activityResponse.setPicUrl("http://localhost:9000/show/" + picDate + "/" + picName);
            rsList.add(activityResponse);
        }
        return rsList;
    }

//    public List<ActivityResponse> getActivities() {
//        List<Activity> activities = activityMapper.selectAll();
//        return activityToActivityResponse(activities);
//    }

    public List<ActivityResponse> getNotDeletedActivities() {
        List<Activity> activities = activityMapper.selectNotDeleted();
        return activityToActivityResponse(activities);
    }
    public List<ActivityResponse> getNotDeletedActivitiesByWorkerId(Integer workerId) {
        List<Activity> activities = activityMapper.selectNotDeletedByWorkerId(workerId);
        return activityToActivityResponse(activities);
    }


    public List<ActivityResponse> getNotDeletedActivitiesBySearch(ActivitySearchQuery activitySearchQuery) {
        List<Activity> activities = activityMapper.selectNotDeletedBySearch(activitySearchQuery);
        return activityToActivityResponse(activities);

    }

    public int activitySignUp(Integer activityId, Integer volunteerId) {
        int count = volunteerRecordMapper.countByVolunteerIdAndActivityId(activityId, volunteerId);
        Activity activity = activityMapper.selectByPrimaryKey(activityId);
        if (activity.getHasDeleted() == 1) {
            return ActivityConstant.HAS_DELETE;
        }else if (count > 0) {
            return ActivityConstant.HAS_SIGNED_UP;
        } else {
            VolunteerRecord volunteerRecord = new VolunteerRecord();
            volunteerRecord.setActivityId(activityId);
            volunteerRecord.setStatus(0);
            volunteerRecord.setVolunteerId(volunteerId);
            volunteerRecord.setCreateTime(new Date());
            int i = volunteerRecordMapper.insertSelective(volunteerRecord);
            if (i > 0) {
                return ActivityConstant.SIGNED_UP_SUCCESS;
            } else {
                return ActivityConstant.SIGNED_UP_ERROR;
            }
        }
    }


    public int workerDeleteActivityByActivityId(Integer activityId) {
        Activity activity = new Activity();
        activity.setDeleteTime(new Date());
        activity.setHasDeleted(1);
        activity.setId(activityId);
        return activityMapper.updateByPrimaryKeySelective(activity);
    }

    public int addActivity(AddActivityRquest addActivityRquest, String picInfo) {
        try {
            Activity activity = new Activity();
            BeanUtils.copyProperties(addActivityRquest, activity);
            activity.setHasDeleted(0);

            String[] activityDateRange = addActivityRquest.getActivityDateRange();
            String[] recruitDateRange = addActivityRquest.getRecruitDateRange();
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date ab = DateUtils.dayAddAndSub(Calendar.HOUR, 0, df.parse(activityDateRange[0]));
            Date ae = DateUtils.dayAddAndSub(Calendar.HOUR, 0, df.parse(activityDateRange[1]));
            Date rb = DateUtils.dayAddAndSub(Calendar.HOUR, 0, df.parse(recruitDateRange[0]));
            Date re = DateUtils.dayAddAndSub(Calendar.HOUR, 0, df.parse(recruitDateRange[1]));
            activity.setActivityBeginTime(ab);
            activity.setActivityEndTime(ae);
            activity.setRecruitBeginTime(rb);
            activity.setRecruitEndTime(re);

            activity.setCreateTime(new Date());
            activity.setWorkerId(addActivityRquest.getWorkerId());
            activity.setBanStatus(0);
            activity.setPicInfo(picInfo);
            if ("本社区".equals(addActivityRquest.getRecruitRange())) {
                activity.setRecruitRange(0);
            } else {
                activity.setRecruitRange(1);
            }
            return activityMapper.insertSelective(activity);
        } catch (ParseException e) {
            return 0;
        }
    }

    public int editActivity(EditActivityRquest editActivityRquest, String picInfo) {
        try {
            Activity activity = new Activity();
            BeanUtils.copyProperties(editActivityRquest, activity);
            activity.setHasDeleted(0);

            String[] activityDateRange = editActivityRquest.getActivityDateRange();
            String[] recruitDateRange = editActivityRquest.getRecruitDateRange();
//            System.out.println(activityDateRange[1]);
            // 前后端同步时间，后端需要增加8小时
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
            Date ab = DateUtils.dayAddAndSub(Calendar.HOUR, 8, df.parse(activityDateRange[0]));
            Date ae = DateUtils.dayAddAndSub(Calendar.HOUR, 8, df.parse(activityDateRange[1]));
            Date rb = DateUtils.dayAddAndSub(Calendar.HOUR, 8, df.parse(recruitDateRange[0]));
            Date re = DateUtils.dayAddAndSub(Calendar.HOUR, 8, df.parse(recruitDateRange[1]));
            activity.setActivityBeginTime(ab);
            activity.setActivityEndTime(ae);
            activity.setRecruitBeginTime(rb);
            activity.setRecruitEndTime(re);
            activity.setWorkerId(editActivityRquest.getWorkerId());
            if (picInfo != null) {
                activity.setPicInfo(picInfo);
            }
            if ("本社区".equals(editActivityRquest.getRecruitRange())) {
                activity.setRecruitRange(0);
            } else {
                activity.setRecruitRange(1);
            }
            activity.setUpdateTime(new Date());
            return activityMapper.updateByPrimaryKeySelective(activity);
        } catch (ParseException e) {
            return 0;
        }
    }


    public List<ActivityAuthorityResponse> getAllActivity() {
        List<Activity> activities = activityMapper.selectAll();
        List<ActivityAuthorityResponse> rs = new ArrayList<>(activities.size());
        for (Activity activity : activities) {
            ActivityAuthorityResponse tmp = new ActivityAuthorityResponse();
            tmp.setActivity(activity);
            tmp.setCreateTime(DateUtils.dateToStr(activity.getCreateTime()));
            rs.add(tmp);
        }
        return rs;
    }

    public int updateActivity(Activity activity) {
        return activityMapper.updateByPrimaryKeySelective(activity);
    }

    public ActivityResponse getActivityInfoById(Integer activityId) {
        Activity activity = activityMapper.selectByPrimaryKey(activityId);
        ActivityResponse activityResponse = new ActivityResponse();
        activityResponse.setActivity(activity);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        String abegin = sdf.format(activity.getActivityBeginTime());
        String aend = sdf.format(activity.getActivityEndTime());
        String rbegin = sdf.format(activity.getRecruitBeginTime());
        String rend = sdf.format(activity.getRecruitEndTime());
        activityResponse.setActivityTimeRange(abegin + " 至 " + aend);
        activityResponse.setRecruitTimeRange(rbegin + " 至 " + rend);

        Worker worker = workerMapper.selectByPrimaryKey(activity.getWorkerId());
        CommunityOrganization communityOrganization = communityOrganizationMapper.selectByPrimaryKey(worker.getCommunityId());
        activityResponse.setCommunityName(communityOrganization.getName());
        activityResponse.setCommunityId(communityOrganization.getId());
        activityResponse.setSponsor(worker.name);
        activityResponse.setSponsorPhoneNumber(worker.getPhone());
        // 活动状态
        Date now = new Date();
        if(now.before(activity.getRecruitBeginTime())){
            activityResponse.setActivityStatus(ActivityConstant.RECRUIT_NOT_STARTED);
        } else if (now.before(activity.getRecruitEndTime())) {
            activityResponse.setActivityStatus(ActivityConstant.RECRUITING);
        } else if(now.before(activity.getActivityBeginTime())){
            activityResponse.setActivityStatus(ActivityConstant.ACITVITY_NOT_STARTED);
        } else if (now.before(activity.getActivityEndTime())) {
            activityResponse.setActivityStatus(ActivityConstant.ACITVITY_DOING);
        } else {
            activityResponse.setActivityStatus(ActivityConstant.ACITVITY_OVER);
        }
        // 已报名的人数（不包括取消报名或者被拒绝的人数，即不包括记录状态为1的记录数)
        int signedUpCount = volunteerRecordMapper.countByActivity(activity.getId());
        activityResponse.setHasRecruitedNumber(signedUpCount);
        // 获取该活动已经报名审核通过的人数
        int agreeNum;
        if (activityResponse.getActivityStatus() == ActivityConstant.RECRUIT_NOT_STARTED) {
            agreeNum = 0;
        }else{
            agreeNum = volunteerRecordMapper.countByActivityIdAndStatus(activity.getId(), RecordConstant.REGISTRATION_PASSED);
        }
        activityResponse.setHasAgreeNumber(agreeNum);
        String[] picInfos = activity.getPicInfo().split(";");
        String picName = picInfos[0];
        String picDate = picInfos[1];
        activityResponse.setPicUrl("http://localhost:9000/show/" + picDate + "/" + picName);
        return activityResponse;
    }
}
