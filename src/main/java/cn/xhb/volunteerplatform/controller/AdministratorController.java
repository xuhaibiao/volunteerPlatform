package cn.xhb.volunteerplatform.controller;


import cn.xhb.volunteerplatform.dto.*;
import cn.xhb.volunteerplatform.entity.*;
import cn.xhb.volunteerplatform.service.ActivityService;
import cn.xhb.volunteerplatform.service.CommunityService;
import cn.xhb.volunteerplatform.service.MessageService;
import cn.xhb.volunteerplatform.service.UserService;
import cn.xhb.volunteerplatform.util.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RestController
@RequestMapping("/administrator")
public class AdministratorController {

    @Resource
    UserService userService;
    @Resource
    ActivityService activityService;
    @Resource
    MessageService messageService;
    @Resource
    CommunityService communityService;
//    @Resource
//    RedisTemplate<String,List<Activity>> redisTemplate;

    @GetMapping("/volunteerAuthority")
    public Result<List<VolunteerAuthorityResponse>> getAllVolunteer() {
        List<VolunteerAuthorityResponse> rs = userService.getAllVolunteer();
        return Result.success(rs);
    }

    @GetMapping("/workerAuthority")  
    public Result<List<WorkerAuthorityResponse>> getAllWorker() {
        List<WorkerAuthorityResponse> rs = userService.getAllWorker();
        return Result.success(rs);
    }

    @GetMapping("/activityAuthority")
    public Result<List<ActivityAuthorityResponse>> getAllActivity() {
        List<ActivityAuthorityResponse> rs = activityService.getAllActivity();
        return Result.success(rs);
    }

    @GetMapping("/communityReview")
    public Result<List<ReviewCommunitiesResponse>> getNeedReviewCommunities() {
        List<ReviewCommunitiesResponse> rs = communityService.getAllNeedReviewCommunities();
        return Result.success(rs);
    }

    @PostMapping("/communityReview/agree")
    public Result<Object> agree(@RequestBody CommunityOrganization community) {
        int i = communityService.approved(community);
        if (i > 0) {
            return Result.success(null);
        } else {
            return Result.error("通过失败！");
        }
    }

    @PostMapping("/communityReview/refuse")
    public Result<Object> refuse(@RequestBody CommunityOrganization community) {
        int i = communityService.refuseApproved(community);
        if (i > 0) {
            return Result.success(null);
        } else {
            return Result.error("驳回失败！");
        }
    }





    @GetMapping("/message")
    public Result<List<Message>> getAllMessage() {
        List<Message> rs = messageService.getAllMessage();
        return Result.success(rs);
    }

    @PostMapping("/volunteerAuthority/changeBanStatus")
    public Result<Object> changeVolunteerBanStatus(@RequestBody Volunteer volunteer) {
        int i = userService.updateVolunteer(volunteer);
        if (i > 0) {
            return Result.success(null);
        } else {
            return Result.error();
        }
    }

    @PostMapping("/workerAuthority/changeBanStatus")
    public Result<Object> changeWorkerBanStatus(@RequestBody Worker worker) {
        int i = userService.updateWorker(worker);
        if (i > 0) {
            return Result.success(null);
        } else {
            return Result.error();
        }
    }

    @PostMapping("/activityAuthority/changeBanStatus")
    public Result<Object> changeActivityBanStatus(@RequestBody ChangeActivityBanStatusRequest changeActivityBanStatusRequest) {
        Activity activity = changeActivityBanStatusRequest.getActivity();
        int i = activityService.updateActivity(activity);
        if (i > 0) {
            Worker workerById = userService.getWorkerById(activity.getWorkerId());
            Integer communityId = workerById.getCommunityId();
            if (StringUtils.isNotBlank(changeActivityBanStatusRequest.getReason())) {
                int k = messageService.addChangeActivityBanStatusSystemMsg(communityId, activity, changeActivityBanStatusRequest.getReason());
                if (k > 0) {
//                    redisTemplate.delete("notDeletedActivities");
                    return Result.success(null);
                }
            } else {
                return Result.success(null);
            }
        }
        return Result.error();

    }

    @PostMapping("/message/add")
    public Result<Object> changeActivityBanStatus(@RequestBody AdministratorAddMsgRequest addMsgRequest) {
       int i =  messageService.addSystemMsg(addMsgRequest.getTitle(), addMsgRequest.getContent(), addMsgRequest.getUserType());
        if (i > 0) {
            return Result.success(null);
        } else {
            return Result.error();
        }
    }

    @GetMapping("/download")
    public void download(@RequestParam("fileName") String fileName, @RequestParam("fileDate") String fileDate, HttpServletRequest req, HttpServletResponse resp) {
        FileUtils.download(fileName, fileDate, req, resp);
    }

}
