package cn.xhb.volunteerplatform.controller;


import cn.xhb.volunteerplatform.dto.AdministratorAddMsgRequest;
import cn.xhb.volunteerplatform.dto.ChangeActivityBanStatusRequest;
import cn.xhb.volunteerplatform.dto.Result;
import cn.xhb.volunteerplatform.entity.*;
import cn.xhb.volunteerplatform.service.ActivityService;
import cn.xhb.volunteerplatform.service.CommunityService;
import cn.xhb.volunteerplatform.service.MessageService;
import cn.xhb.volunteerplatform.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
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

    @GetMapping("/volunteerAuthority")
    public Result<List<Volunteer>> getAllVolunteer() {
        List<Volunteer> rs = userService.getAllVolunteer();
        return Result.success(rs);
    }

    @GetMapping("/workerAuthority")  
    public Result<List<Worker>> getAllWorker() {
        List<Worker> rs = userService.getAllWorker();
        return Result.success(rs);
    }

    @GetMapping("/activityAuthority")
    public Result<List<Activity>> getAllActivity() {
        List<Activity> rs = activityService.getAllActivity();
        return Result.success(rs);
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


}
