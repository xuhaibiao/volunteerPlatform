package cn.xhb.volunteerplatform.controller;


import cn.xhb.volunteerplatform.constant.MessageConstant;
import cn.xhb.volunteerplatform.dto.*;
import cn.xhb.volunteerplatform.entity.CommunityOrganization;
import cn.xhb.volunteerplatform.entity.Message;
import cn.xhb.volunteerplatform.entity.Volunteer;
import cn.xhb.volunteerplatform.entity.Worker;
import cn.xhb.volunteerplatform.service.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/worker")
public class WorkerController {

    @Resource
    ActivityService activityService;
    @Resource
    UserService userService;
    @Resource
    EvaluateService evaluateService;
    @Resource
    CommunityService communityService;
    @Resource
    MessageService messageService;


    @GetMapping("/activity")
    public Result<List<ActivityResponse>> getActivities(@RequestParam("userId") Integer workerId){
        List<ActivityResponse> rs = activityService.getNotDeletedActivitiesByWorkerId(workerId);
        return Result.success(rs);
    }

    @GetMapping("/activity/reviewTable")
    public Result<List<ReviewTableResponse>> getReviewTableData(@RequestParam("activityId") Integer activityId){
        List<ReviewTableResponse> rs = userService.getRegistrationData(activityId);
        return Result.success(rs);
    }



    @GetMapping("/activity/search")
    public Result<List<ActivityResponse>> getActivitiesBySearch(@RequestParam("activityName") String activityName){
        ActivitySearchQuery activitySearchQuery = new ActivitySearchQuery();
        activitySearchQuery.setProvince(null);
        activitySearchQuery.setCity(null);
        activitySearchQuery.setArea(null);
        if (StringUtils.isNotBlank(activityName)) {
            activitySearchQuery.setActivityName(activityName);
        } else {
            activitySearchQuery.setActivityName(null);
        }
        List<ActivityResponse> rs = activityService.getNotDeletedActivitiesBySearch(activitySearchQuery);
        return Result.success(rs);
    }

    @DeleteMapping("/activity/delete")
    public Result<Object> deleteActivity(@RequestParam("activityId") Integer activityId){
        int i = activityService.workerDeleteActivityByActivityId(activityId);
        if (i > 0) {
            return Result.success(null);
        } else {
            return Result.error();
        }

    }

    @PostMapping("/activity/add")
    public Result<Object> addActivity(@RequestBody AddActivityRquest addActivityRquest){
        int i = activityService.addActivity(addActivityRquest);
        if (i > 0) {
            return Result.success(null);
        } else {
            return Result.error();
        }
    }

    @PutMapping("/activity/edit")
    public Result<Object> editActivity(@RequestBody EditActivityRquest editActivityRquest){
        int i = activityService.editActivity(editActivityRquest);
        if (i > 0) {
            return Result.success(null);
        } else {
            return Result.error();
        }
    }

    @PostMapping("/activity/agreeJoin")
    public Result<Object> agreeJoinActivity(@RequestBody ExamineActivityRequest examineActivityRequest){
        int i = userService.agreeJoin(examineActivityRequest.getRecordId());
        if (i > 0) {
            return Result.success(null);
        } else {
            return Result.error();
        }
    }

    @PostMapping("/activity/refuseJoin")
    public Result<Object> refuseJoinActivity(@RequestBody ExamineActivityRequest examineActivityRequest){
        int i = userService.refuseJoin(examineActivityRequest.getRecordId());
        if (i > 0) {
            return Result.success(null);
        } else {
            return Result.error();
        }
    }

    @GetMapping("/needEvaluateRecords")
    public Result<List<NeedEvaluateRecordsResponse>> getNeedEvaluateRecords(@RequestParam("userId") Integer workerId){
        List<NeedEvaluateRecordsResponse> rs = userService.needEvaluateRecordsResponseByWorkerId(workerId);
        return Result.success(rs);
    }

    @PostMapping("/needEvaluateRecords/evaluate")
    public Result<Object> evaluate(@RequestBody WorkerEvaluateRecordsRequest workerEvaluateRecordsRequest){
        int i = evaluateService.workerEvaluate(workerEvaluateRecordsRequest.getEvaluateScore(), workerEvaluateRecordsRequest.getRecordId(), workerEvaluateRecordsRequest.getRecordStatus());
        if (i > 0) {
            return Result.success(null);
        } else {
            return Result.error();
        }
    }

    @GetMapping("/community")
    public Result<WorkerGetCommunityResponse> getCommunityInfo(@RequestParam("communityId") Integer communityId){
        WorkerGetCommunityResponse workerGetCommunityResponse = new WorkerGetCommunityResponse();
        CommunityOrganization community = communityService.getCommunity(communityId);
        workerGetCommunityResponse.setUserCommuntity(community);

        List<Volunteer> volunteers = userService.getVolunteerByCommunityId(communityId);
        List<Worker> workers = userService.getWorkerByCommunityId(communityId);

        workerGetCommunityResponse.setVolunteers(volunteers);
        workerGetCommunityResponse.setWorkers(workers);

        return Result.success(workerGetCommunityResponse);
    }

    @GetMapping("/community/joinList")
    public Result<List<JoinListResponse>> getJoinList(@RequestParam("communityId") Integer communityId) {
        List<JoinListResponse> rs = communityService.getNotDealJoinList(communityId, MessageConstant.JOIN_COMMUNITY_TYPE);
        return Result.success(rs);
    }

    @PostMapping("/community/agreeJoin")
    public Result<Object> agreeJoinCommunity(@RequestBody  Message message){
        int i = communityService.agreeJoin(message.getSender(), message.getId(),message.getRecipient());
        if (i > 0) {
            return Result.success(null);
        } else if (i == -1) {
            return Result.error("该志愿者已加入其他社区组织！","-1");
        } else {
            return Result.error();
        }
    }

    @PostMapping("/community/refuseJoin")
    public Result<Object> refuseJoinCommunity(@RequestBody Message message){
        int i = communityService.refuseJoin(message.getId());
        if (i > 0) {
            return Result.success(null);
        } else {
            return Result.error();
        }
    }

    @GetMapping("/message")
    public Result<MessageResponse> getMessages(@RequestParam("userId") Integer workerId, @RequestParam("communityId") Integer communityId) {
        MessageResponse rs = messageService.getWorkerMessages(workerId, communityId);
        return Result.success(rs);
    }

    @PostMapping("/message/add")
    public Result<Object> addMessage(@RequestBody WorkerAddMsgRequest workerAddMsgRequest){
        int i = messageService.addCommunityMsg(workerAddMsgRequest);
        if (i > 0) {
            return Result.success(null);
        } else {
            return Result.error();
        }
    }


}
