package cn.xhb.volunteerplatform.controller;


import cn.xhb.volunteerplatform.dto.*;
import cn.xhb.volunteerplatform.entity.*;
import cn.xhb.volunteerplatform.service.ActivityService;
import cn.xhb.volunteerplatform.service.CommunityService;
import cn.xhb.volunteerplatform.service.MessageService;
import cn.xhb.volunteerplatform.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
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

        BufferedInputStream bis = null;
        BufferedOutputStream bos = null;
        OutputStream fos = null;
        try {
//            String filePath = "C:\\Users\\Lucas\\IdeaProjects\\volunteerPlatform\\fileData";
            String filePath = "E:\\Java_in_idea\\volunteerPlatform\\fileData";
            bis = new BufferedInputStream(new FileInputStream(filePath + "/" + fileDate + "/" + fileName));
            fos = resp.getOutputStream();
            bos = new BufferedOutputStream(fos);
            setFileDownloadHeader(req, resp, fileName);
            resp.setContentType("application/octet-stream");
            resp.setHeader("Content-Disposition", "attachment;filename=" + new String(fileName.getBytes(StandardCharsets.UTF_8), "iso-8859-1"));
            int byteRead = 0;
            byte[] buffer = new byte[2048];
            while ((byteRead = bis.read(buffer)) != -1) {
                bos.write(buffer, 0, byteRead);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                assert bos != null;
                bos.flush();
                bis.close();
                fos.close();
                bos.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    public static void setFileDownloadHeader(HttpServletRequest request,
                                             HttpServletResponse response, String fileName) {
        try {
            String encodedFileName = null;
            String agent = request.getHeader("USER-AGENT");
            if (null != agent && agent.contains("MSIE")) {
                encodedFileName = URLEncoder.encode(fileName, "UTF-8");
            } else if (null != agent && agent.contains("Mozilla")) {
                encodedFileName = new String(fileName.getBytes(StandardCharsets.UTF_8),
                        StandardCharsets.ISO_8859_1);
            } else {
                encodedFileName = URLEncoder.encode(fileName, "UTF-8");
            }

            response.setContentType("application/x-download;charset=UTF-8");
            response.setHeader("Content-Disposition", "attachment;filename=\""
                    + encodedFileName + "\"");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



}
