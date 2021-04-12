package cn.xhb.volunteerplatform.controller;


import cn.xhb.volunteerplatform.dto.*;
import cn.xhb.volunteerplatform.service.ActivityService;
import cn.xhb.volunteerplatform.service.EvaluateService;
import cn.xhb.volunteerplatform.service.UserService;
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
    public Result<Object> agreeJoin(@RequestBody ExamineRequest examineRequest){
        int i = userService.agreeJoin(examineRequest.getRecordId());
        if (i > 0) {
            return Result.success(null);
        } else {
            return Result.error();
        }
    }

    @PostMapping("/activity/refuseJoin")
    public Result<Object> refuseJoin(@RequestBody ExamineRequest examineRequest){
        int i = userService.refuseJoin(examineRequest.getRecordId());
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


}
