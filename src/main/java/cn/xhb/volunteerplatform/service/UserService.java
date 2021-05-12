package cn.xhb.volunteerplatform.service;

import cn.xhb.volunteerplatform.constant.ActivityConstant;
import cn.xhb.volunteerplatform.constant.RecordConstant;
import cn.xhb.volunteerplatform.dto.*;
import cn.xhb.volunteerplatform.entity.*;
import cn.xhb.volunteerplatform.mapper.*;
import cn.xhb.volunteerplatform.util.DateUtils;
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

    public int addVolunteer(Volunteer volunteer) {
        return volunteerMapper.insertSelective(volunteer);
    }

    public int addWorker(Worker worker) {
        return workerMapper.insertSelective(worker);

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
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        for (VolunteerRecord volunteerRecord : volunteerRecords) {
            VolunteerRecordResponse volunteerRecordResponse = new VolunteerRecordResponse();

            String recordCreateTime = sdf.format(volunteerRecord.getCreateTime());
            volunteerRecordResponse.setRecordCreateTime(recordCreateTime);

            volunteerRecordResponse.setVolunteerRecord(volunteerRecord);
            Integer activityId = volunteerRecord.getActivityId();
            Activity activity = activityMapper.selectByPrimaryKey(activityId);
            Date now = new Date();
            // 记录状态
            if (activity.getHasDeleted() == 1 ) {
                volunteerRecord.setStatus(RecordConstant.ACTIVITY_HAS_DELETED);
            }else if (activity.getActivityBeginTime().before(now) && activity.getActivityEndTime().after(now) && volunteerRecord.getStatus() == ActivityConstant.SIGNED_UP_SUCCESS) {
                volunteerRecord.setStatus(RecordConstant.ACTIVITY_IN_PROGRESS);
            } else if (activity.getActivityEndTime().before(now) && volunteerRecord.getStatus() == ActivityConstant.SIGNED_UP_SUCCESS) {
                volunteerRecord.setStatus(RecordConstant.ACTIVITY_IS_OVER);

            }
            volunteerRecordMapper.updateByPrimaryKeySelective(volunteerRecord);
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
            List<VolunteerRecord> volunteerRecords = volunteerRecordMapper.selectByActivityIdAndStatus(activity.getId(), RecordConstant.VOLUNTEER_HAS_EVALUATE_WORKER_NOT);
            for (VolunteerRecord volunteerRecord : volunteerRecords) {
                NeedEvaluateRecordsResponse tmp = new NeedEvaluateRecordsResponse();
                tmp.setRecordId(volunteerRecord.getId());
                tmp.setRecordStatus(volunteerRecord.getStatus());
                tmp.setActivityId(volunteerRecord.getActivityId());
                tmp.setActivityName(activity.getName());
                tmp.setVolunteerEvaluatContent(volunteerRecord.getVolunteerEvaluateContent());
                tmp.setVolunteerEvaluateScore(volunteerRecord.getVolunteerEvaluateScore());
                String[] picInfos = volunteerRecord.getPicInfo().split(";");
                String picName = picInfos[0];
                String picDate = picInfos[1];
                tmp.setPicUrl("http://localhost:9000/show/" + picDate + "/" + picName);
                Integer volunteerId = volunteerRecord.getVolunteerId();
                Volunteer volunteer = volunteerMapper.selectByPrimaryKey(volunteerId);
                tmp.setVolunteerName(volunteer.getName());
                tmp.setVolunteerIdCard(volunteer.getIdCard());
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
        for (WorkerGetVolunteerEvaluateInfoResponse info : infos) {
            String[] picInfos = info.getPicInfo().split(";");
            String picName = picInfos[0];
            String picDate = picInfos[1];
            info.setPicUrl("http://localhost:9000/show/" + picDate + "/" + picName);
        }
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

    public List<VolunteerAuthorityResponse> getAllVolunteer() {
        List<Volunteer> volunteers = volunteerMapper.selectAll();
        List<VolunteerAuthorityResponse> rs = new ArrayList<>(volunteers.size());
        for (Volunteer volunteer : volunteers) {
            VolunteerAuthorityResponse tmp = new VolunteerAuthorityResponse();
            tmp.setVolunteer(volunteer);
            tmp.setCreateTime(DateUtils.dateToStr(volunteer.getCreateTime()));
            rs.add(tmp);
        }
        return rs;
    }

    public List<WorkerAuthorityResponse> getAllWorker() {
        List<Worker> workers = workerMapper.selectAll();
        List<WorkerAuthorityResponse> rs = new ArrayList<>(workers.size());
        for (Worker worker : workers) {
            WorkerAuthorityResponse tmp = new WorkerAuthorityResponse();
            tmp.setWorker(worker);
            tmp.setCreateTime(DateUtils.dateToStr(worker.getCreateTime()));
            rs.add(tmp);
        }
        return rs;
    }


}
