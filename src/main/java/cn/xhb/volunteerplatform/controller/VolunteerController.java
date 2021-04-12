package cn.xhb.volunteerplatform.controller;

import cn.xhb.volunteerplatform.constant.ActivityConstant;
import cn.xhb.volunteerplatform.dto.*;
import cn.xhb.volunteerplatform.entity.BaseUser;
import cn.xhb.volunteerplatform.entity.Volunteer;
import cn.xhb.volunteerplatform.service.ActivityService;
import cn.xhb.volunteerplatform.service.EvaluateService;
import cn.xhb.volunteerplatform.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/volunteer")
public class VolunteerController {

    @Resource
    ActivityService activityService;
    @Resource
    UserService userService;
    @Resource
    EvaluateService evaluateService;


    @GetMapping("/activity")
    public Result<List<ActivityResponse>> getActivities(){
        List<ActivityResponse> rs = activityService.getNotDeletedActivities();
        return Result.success(rs);

    }

    @GetMapping("/activity/search")
    public Result<List<ActivityResponse>> getActivitiesBySearch(@RequestParam("province") String province,@RequestParam("city") String city,@RequestParam("area") String area,@RequestParam("activityName") String activityName){
        ActivitySearchQuery activitySearchQuery = new ActivitySearchQuery();

        if("省".equals(province)){
            activitySearchQuery.setProvince(null);
            activitySearchQuery.setCity(null);
            activitySearchQuery.setArea(null);
        } else if ("市".equals(city)) {
            activitySearchQuery.setProvince(province);
            activitySearchQuery.setCity(null);
            activitySearchQuery.setArea(null);
        } else if ("区".equals(area)) {
            activitySearchQuery.setProvince(province);
            activitySearchQuery.setCity(city);
            activitySearchQuery.setArea(null);
        } else {
            activitySearchQuery.setProvince(province);
            activitySearchQuery.setCity(city);
            activitySearchQuery.setArea(area);
        }
        if (StringUtils.isNotBlank(activityName)) {
            activitySearchQuery.setActivityName(activityName);
        } else {
            activitySearchQuery.setActivityName(null);
        }
        List<ActivityResponse> rs = activityService.getNotDeletedActivitiesBySearch(activitySearchQuery);
        return Result.success(rs);
    }

    @PostMapping("/activity/signUp")
    public Result<Object> activitySignUp(@RequestBody ActivitySignUpRequest activitySignUpRequest) {
        int type = activityService.activitySignUp(activitySignUpRequest.getActivityId(), activitySignUpRequest.getUserId());
        if (type == ActivityConstant.HAS_SIGNED_UP) {
            return Result.error("您已报名过该活动！","warning");
        } else if (type == ActivityConstant.SIGNED_UP_ERROR) {
            return Result.error("报名失败！","error");
        } else if (type == ActivityConstant.HAS_DELETE) {
            return Result.error("活动已删除！", "error");
            
        } else {
            return Result.success(null);
        }

    }

    @PostMapping("/information/edit")
    public Result<BaseUser> updateVolunteer(@RequestBody Volunteer volunteer){
        int i = userService.updateVolunteer(volunteer);
        if (i > 0) {
            Volunteer v = userService.getVolunteerById(volunteer.id);
            return Result.success(v);
        } else {
            return Result.error();
        }
    }

    @GetMapping("/record")
    public Result<List<VolunteerRecordResponse>> getVolunteerRecords(@RequestParam Integer userId) {
        List<VolunteerRecordResponse> rs = userService.getVolunteerRecordsByVolunteerId(userId);
        return Result.success(rs);
    }

    @PostMapping("/record/evaluate")
    public Result<Object> evaluate(@RequestBody VolunteerEvaluateRequest evaluateRequest){
        int i = evaluateService.volunteerEvaluate(evaluateRequest.getScore(), evaluateRequest.getContent(), evaluateRequest.getRecordId(), evaluateRequest.getRecordStatus());
        if (i > 0) {
            return Result.success(null);
        } else {
            return Result.error();
        }
    }

}
