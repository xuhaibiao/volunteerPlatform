package cn.xhb.volunteerplatform.service;

import cn.xhb.volunteerplatform.constant.ActivityConstant;
import cn.xhb.volunteerplatform.constant.RecordConstant;
import cn.xhb.volunteerplatform.dto.*;
import cn.xhb.volunteerplatform.entity.*;
import cn.xhb.volunteerplatform.mapper.*;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class UserService {
    @Resource
    AdministratorMapper administratorMapper;
    @Resource
    VolunteerMapper volunteerMapper;
    @Resource
    WorkerMapper workerMapper;
    @Resource
    VolunteerRecordMapper volunteerRecordMapper;
    @Resource
    ActivityMapper activityMapper;
    @Resource
    CommunityOrganizationMapper communityOrganizationMapper;

    public Volunteer getVolunteerById(Integer id){
        return volunteerMapper.selectByPrimaryKey(id);
    }

    public Worker getWorkerById(Integer id){
        return workerMapper.selectByPrimaryKey(id);
    }

    public Administrator getAdministratorById(Integer id){
        return administratorMapper.selectByPrimaryKey(id);
    }

    public Volunteer getVolunteerByIdCard(String idCard) {
        return volunteerMapper.selectByIdCard(idCard);
    }

    public int updateVolunteer(Volunteer volunteer) {
        return volunteerMapper.updateByPrimaryKeySelective(volunteer);
    }

    public int updateWorker(Worker worker) {
        return workerMapper.updateByPrimaryKeySelective(worker);
    }
    public int updateAdministrator(Administrator administrator) {
        return administratorMapper.updateByPrimaryKeySelective(administrator);
    }


    public Worker getWorkerByIdCard(String idCard) {
        return workerMapper.selectByIdCard(idCard);
    }

    public Administrator getAdministratorByIdCard(String idCard) {
        return administratorMapper.selectByIdCard(idCard);
    }

    public List<VolunteerRecordResponse> getVolunteerRecordsByVolunteerId(Integer volunteerId){
        List<VolunteerRecord> volunteerRecords = volunteerRecordMapper.selectByVolunteerId(volunteerId);
        List<VolunteerRecordResponse> rs = new ArrayList<>();
        for (VolunteerRecord volunteerRecord : volunteerRecords) {
            VolunteerRecordResponse volunteerRecordResponse = new VolunteerRecordResponse();
            volunteerRecordResponse.setVolunteerRecord(volunteerRecord);
            Integer activityId = volunteerRecord.getActivityId();
            Activity activity = activityMapper.selectByPrimaryKey(activityId);
            Date now = new Date();
            // 记录状态
            if (activity.getHasDeleted() == 1) {
                volunteerRecord.setStatus(RecordConstant.ACTIVITY_HAS_DELETED);
            }else if (activity.getActivityBeginTime().before(now) && activity.getActivityEndTime().after(now) && volunteerRecord.getStatus() == ActivityConstant.SIGNED_UP_SUCCESS) {
                volunteerRecord.setStatus(RecordConstant.ACTIVITY_IN_PROGRESS);
            } else if (activity.getActivityEndTime().before(now) && volunteerRecord.getStatus() == ActivityConstant.SIGNED_UP_SUCCESS) {
                volunteerRecord.setStatus(RecordConstant.ACTIVITY_IS_OVER);

            }
            volunteerRecordMapper.updateByPrimaryKeySelective(volunteerRecord);
            ActivityResponse activityResponse = new ActivityResponse();
            activityResponse.setActivity(activity);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String begin = sdf.format(activity.getActivityBeginTime());
            String end = sdf.format(activity.getActivityEndTime());
            activityResponse.setActivityTimeRange(begin + "至" + end);
            Worker worker = workerMapper.selectByPrimaryKey(activity.getWorkerId());
            CommunityOrganization communityOrganization = communityOrganizationMapper.selectByPrimaryKey(worker.getCommunityId());
            activityResponse.setCommunityName(communityOrganization.getName());
            activityResponse.setSponsor(worker.name);
            Date now2 = new Date();
            // 活动状态
            if(now2.before(activity.getRecruitBeginTime())){
                activityResponse.setActivityStatus(ActivityConstant.RECRUIT_NOT_STARTED);
            } else if (now2.before(activity.getRecruitEndTime())) {
                activityResponse.setActivityStatus(ActivityConstant.RECRUITING);
            } else if(now2.before(activity.getActivityBeginTime())){
                activityResponse.setActivityStatus(ActivityConstant.ACITVITY_NOT_STARTED);
            } else if (now2.before(activity.getActivityEndTime())) {
                activityResponse.setActivityStatus(ActivityConstant.ACITVITY_DOING);
            } else {
                activityResponse.setActivityStatus(ActivityConstant.ACITVITY_OVER);
            }
            int signedUp = volunteerRecordMapper.countByActivity(activity.getId());
            activityResponse.setHasRecruitedNumber(signedUp);
            volunteerRecordResponse.setActivityResponse(activityResponse);

            rs.add(volunteerRecordResponse);
        }
        return rs;
    }

    /**
     * 获取社区工作者需要评价的记录
     * @param workerId 社区工作者id
     * @return 社区工作者需要评价的记录
     */
    public List<NeedEvaluateRecordsResponse> needEvaluateRecordsResponseByWorkerId(Integer workerId) {
        List<Activity> activities = activityMapper.selectNotDeletedByWorkerId(workerId);
        List<NeedEvaluateRecordsResponse> rs = new ArrayList<>();
        for (Activity activity : activities) {
            List<VolunteerRecord> volunteerRecords = volunteerRecordMapper.selectByActivityIdAndTwoStatus(activity.getId(), RecordConstant.ACTIVITY_IS_OVER, RecordConstant.VOLUNTEER_HAS_EVALUATE_WORKER_NOT);
            for (VolunteerRecord volunteerRecord : volunteerRecords) {
                NeedEvaluateRecordsResponse tmp = new NeedEvaluateRecordsResponse();
                tmp.setRecordId(volunteerRecord.getId());
                tmp.setRecordStatus(volunteerRecord.getStatus());
                tmp.setActivityId(volunteerRecord.getActivityId());
                tmp.setActivityName(activity.getName());
                Integer volunteerId = volunteerRecord.getVolunteerId();
                Volunteer volunteer = volunteerMapper.selectByPrimaryKey(volunteerId);
                tmp.setVolunteerName(volunteer.getName());
                rs.add(tmp);
            }
        }

        return rs;



    }

    public List<ReviewTableResponse> getRegistrationData(Integer activityId) {
        // 报名审核中的记录
        List<VolunteerRecord> volunteerRecords = volunteerRecordMapper.selectByActivityIdAndStatus(activityId, RecordConstant.REGISTRATION_REVIEWING);
        List<ReviewTableResponse> rs = new ArrayList<>();
        for (VolunteerRecord volunteerRecord : volunteerRecords) {
            ReviewTableResponse tmp = new ReviewTableResponse();
            Integer volunteerId = volunteerRecord.getVolunteerId();
            Volunteer volunteer = volunteerMapper.selectByPrimaryKey(volunteerId);
            volunteer.setPassword("");
            tmp.setVolunteer(volunteer);
            tmp.setRegistrationTime(volunteerRecord.getCreateTime());
            tmp.setRecordId(volunteerRecord.getId());
            rs.add(tmp);
        }
        return rs;
    }


    /**
     * 同意活动报名申请
     * @param recordId
     * @return
     */
    public int agreeJoin(Integer recordId) {
        VolunteerRecord volunteerRecord = new VolunteerRecord();
        volunteerRecord.setStatus(RecordConstant.REGISTRATION_PASSED);
        volunteerRecord.setUpdateTime(new Date());
        volunteerRecord.setId(recordId);
        return volunteerRecordMapper.updateByPrimaryKeySelective(volunteerRecord);
    }

    /**
     * 拒绝活动报名申请
     * @param recordId
     * @return
     */
    public int refuseJoin(Integer recordId) {
        VolunteerRecord volunteerRecord = new VolunteerRecord();
        volunteerRecord.setStatus(RecordConstant.REGISTRATION_FAILED);
        volunteerRecord.setUpdateTime(new Date());
        volunteerRecord.setId(recordId);
        return volunteerRecordMapper.updateByPrimaryKeySelective(volunteerRecord);
    }


    public List<Volunteer> getVolunteerByCommunityId(Integer communityId) {
        return volunteerMapper.selectByCommunityId(communityId);
    }

    public List<Worker> getWorkerByCommunityId(Integer communityId) {
        return workerMapper.selectByCommunityId(communityId);
    }


    /**
     * 获取参加该活动的志愿者列表
     * @param activityId
     * @return
     */
    public List<Volunteer> getVolunteerInfoByActivityId(Integer activityId) {
        List<Volunteer> volunteers = volunteerRecordMapper.selectVolunteerInfoByActivityId(activityId);
        return volunteers;
    }

    /**
     * 获取志愿者对该活动的评价列表
     * @param activityId
     * @return
     */
    public List<WorkerGetVolunteerEvaluateInfoResponse> getVolunteerEvaluateInfoByActivityId(Integer activityId) {
        List<WorkerGetVolunteerEvaluateInfoResponse> infos = volunteerRecordMapper.selectVolunteerEvaluateInfoByActivityId(activityId);
        return infos;
    }

    /**
     * 取消报名
     * @param volunteerRecord
     * @return
     */
    public int cancelSignUp(VolunteerRecord volunteerRecord) {
        volunteerRecord.setStatus(RecordConstant.REGISTRATION_FAILED);
        return volunteerRecordMapper.updateByPrimaryKeySelective(volunteerRecord);

    }
}
