package cn.xhb.volunteerplatform.controller;


import cn.xhb.volunteerplatform.dto.*;
import cn.xhb.volunteerplatform.dto.vo.FiveYearNumberVo;
import cn.xhb.volunteerplatform.entity.*;
import cn.xhb.volunteerplatform.service.CommunityService;
import cn.xhb.volunteerplatform.service.MessageService;
import cn.xhb.volunteerplatform.service.StatisticsService;
import cn.xhb.volunteerplatform.service.UserService;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * @author HaibiaoXu
 * @date Create in 10:41 2021/1/11
 * @modified By
 */
@RestController
public class CommonController {

    @Resource
    UserService userService;
    @Resource
    StatisticsService statisticsService;
    @Resource
    CommunityService communityService;
    @Resource
    MessageService messageService;

    @PostMapping("/login")
    public Result<BaseUser> login(@RequestBody LoginRequest loginRequest) {
        BaseUser user = null;
        int type = loginRequest.getType();
        if (type == 0) {
            user = userService.getVolunteerByIdCard(loginRequest.getIdCard());
        } else if (type == 1) {
            user = userService.getWorkerByIdCard(loginRequest.getIdCard());
        } else if (type == 2) {
            user = userService.getAdministratorByIdCard(loginRequest.getIdCard());
        }

        if (user != null && user.getPassword().equals(loginRequest.getPassword()) ) {
            // 密码不返前端
//            user.setPassword(null);
            return Result.success(user, String.valueOf(type));
        } else{
            return Result.error();
        }
    }

    @PostMapping("/signUp")
    public Result<String> signUp(SignUpRequest signUpRequest, MultipartFile file, HttpServletRequest req) {

        try {
            if (signUpRequest.getType() == 0) {
                // 注册志愿者
                Volunteer volunteer = new Volunteer();
                volunteer.setIdCard(signUpRequest.getIdCard());
                volunteer.setName(signUpRequest.getUsername());
                volunteer.setPassword(signUpRequest.getPassword());
                volunteer.setPhone(signUpRequest.getPhone());
                volunteer.setAddress(signUpRequest.getProvince() + signUpRequest.getCity() + signUpRequest.getArea() + signUpRequest.getDetailAddress());
                volunteer.setGender(signUpRequest.getGender());
                volunteer.setCreateTime(new Date());
                volunteer.setVolunteerHours((float) 0);
                volunteer.setVolunteerScore((float) 0);
                volunteer.setVolunteerNumber(0);
                volunteer.setBanStatus(0);
                volunteer.setCommunityId(0);
                int i = userService.addVolunteer(volunteer);
                if (i > 0) {
                    return Result.success(null);
                } else {
                    return Result.error("注册失败！");
                }
            } else if (signUpRequest.getType() == 1) {
                // 注册社区工作者（加入社区）
                Worker worker = new Worker();
                worker.setIdCard(signUpRequest.getIdCard());
                worker.setName(signUpRequest.getUsername());
                worker.setPassword(signUpRequest.getPassword());
                worker.setPhone(signUpRequest.getPhone());
                worker.setAddress(signUpRequest.getProvince() + signUpRequest.getCity() + signUpRequest.getArea() + signUpRequest.getDetailAddress());
                worker.setGender(signUpRequest.getGender());
                worker.setCreateTime(new Date());
                worker.setBanStatus(0);
                // 需要等待社区发起人通过才能设置
                worker.setCommunityId(-1);
                // 返回主键id
                int i = userService.addWorker(worker);
                if (i > 0) {
                    // 解析加入的社区id
                    String joinCommunityInfo = signUpRequest.getJoinCommunityInfo();
                    String s = joinCommunityInfo.split(":")[1];
                    String cid = s.substring(0, s.length() - 1);
                    Integer communityId = Integer.valueOf(cid);
                    // 发送申请加入消息
                    int k = messageService.addWorkerJoinCommunityMsg(worker, communityId);
                    if (k > 0) {
                        return Result.success(null);
                    } else {
                        return Result.error("注册失败！");
                    }
                } else {
                    return Result.error("注册失败！");
                }

            } else {
                // 注册社区工作者（创建社区）
                // 先创建工作者实体
                Worker worker = new Worker();
                worker.setIdCard(signUpRequest.getIdCard());
                worker.setName(signUpRequest.getUsername());
                worker.setPassword(signUpRequest.getPassword());
                worker.setPhone(signUpRequest.getPhone());
                worker.setAddress(signUpRequest.getProvince() + signUpRequest.getCity() + signUpRequest.getArea() + signUpRequest.getDetailAddress());
                worker.setGender(signUpRequest.getGender());
                worker.setCreateTime(new Date());
                worker.setBanStatus(0);
                // 需要等待超级管理员审核通过才能设置
                worker.setCommunityId(-1);
                int i = userService.addWorker(worker);
                if (i > 0) {
                    Result<String> upload = this.upload(file, req);
                    // 如果上传材料成功
                    if (upload.getCode() == 1) {
                        //获取社区信息
                        CommunityOrganization community = new CommunityOrganization();
                        community.setProvince(signUpRequest.getCommunityProvince());
                        community.setCity(signUpRequest.getCommunityCity());
                        community.setArea(signUpRequest.getCommunityArea());
                        community.setDetailAddress(signUpRequest.getCommunityDetailAddress());
                        community.setName(signUpRequest.getCommunityName());
                        community.setUndertaker(signUpRequest.getUsername());
                        community.setCreateTime(new Date());
                        community.setHasDeleted(0);
                        community.setWorkerId(worker.getId());
                        community.setFileinfo(upload.getData());
                        // 需要等待管理员审核
                        community.setHasApproved(0);
                        int k = communityService.add(community);
                        if (k > 0) {
                            return Result.success(null);
                        } else {
                            return Result.error("注册失败！");
                        }
                    } else {
                        return upload;
                    }

                } else {
                    return Result.error("注册失败！");
                }
            }
        } catch (Exception e) {
            return Result.error("注册失败！"+e.getMessage());
        }
    }

    @GetMapping("/loadAllCommunity")
    public Result<List<LoadAllCommunityResponse>> getAllCommunity(){
        List<LoadAllCommunityResponse> list = communityService.getAllNotDelCommunity();
        return Result.success(list);
    }


    @GetMapping("/information")
    public Result<BaseUser> getInformation(@RequestParam("userId") Integer userId,@RequestParam("type") Integer type) {
        BaseUser user = null;
        if (type == 0) {
            user = userService.getVolunteerById(userId);
        } else if (type == 1) {
            user = userService.getWorkerById(userId);
        } else if (type == 2) {
            user = userService.getAdministratorById(userId);
        }
        if (user != null) {
//            user.setPassword(null);
            return Result.success(user);
        }else{
            return Result.error();
        }
    }

    @PostMapping("/information/edit")
    public Result<BaseUser> updateVolunteer(@RequestBody InformationEditRequest informationEditRequest){
        if (informationEditRequest.getType() == 0) {
            Volunteer volunteer = new Volunteer();
            BeanUtils.copyProperties(informationEditRequest, volunteer);
            int i = userService.updateVolunteer(volunteer);
            if (i > 0) {
                Volunteer v = userService.getVolunteerById(volunteer.id);
                return Result.success(v);
            } else {
                return Result.error();
            }
        } else if (informationEditRequest.getType() == 1) {
            Worker worker = new Worker();
            BeanUtils.copyProperties(informationEditRequest, worker);
            int i = userService.updateWorker(worker);
            if (i > 0) {
                Worker w = userService.getWorkerById(worker.id);
                return Result.success(w);
            } else {
                return Result.error();
            }
        } else {
            Administrator administrator = new Administrator();
            BeanUtils.copyProperties(informationEditRequest, administrator);
            int i = userService.updateAdministrator(administrator);
            if (i > 0) {
                Administrator a = userService.getAdministratorById(administrator.id);
                return Result.success(a);
            } else {
                return Result.error();
            }
        }

    }

    @GetMapping("/statistics")
    public Result<StatisticsDataResponse> getStatisticsData() {
        StatisticsDataResponse statisticsDataResponse = new StatisticsDataResponse();
        FiveYearNumberVo f = statisticsService.getNumInFiveYear();
        int[] sexRatio = statisticsService.getSexRatio();

        statisticsDataResponse.setFiveYearNumberVo(f);
        statisticsDataResponse.setSexRatio(sexRatio);


        return Result.success(statisticsDataResponse);
    }


    public Result<String> upload(MultipartFile file, HttpServletRequest req) {
        if (file == null) {
            return Result.error("上传文件为空");
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        //再用pdf格式开始书写,先找原始的名字
        String originName = file.getOriginalFilename();
//        判断文件类型是不是pdf
        if(!originName.endsWith(".pdf")){
            //如果不是的话，就返回类型
            return Result.error("文件类型不对");

        }

        String format=sdf.format(new Date());
        String realPath = "C:\\Users\\Lucas\\IdeaProjects\\volunteerPlatform\\fileData\\" + format;

        //再是保存文件的文件夹
        File folder = new File(realPath);
        //如果不存在，就自己创建
        if(!folder.exists()){
            folder.mkdirs();
        }
        String newName = UUID.randomUUID().toString() + ".pdf";
        Result<String> rs;
        //然后就可以保存了
        try {
            file.transferTo(new File(folder,newName));
            //这个还有一个url
//            String url = req.getScheme() + "://" + req.getServerName() + ":" + req.getServerPort() + "/" + realPath + newName;
            //如果指向成功了
//            rs = Result.success(url);
            rs = Result.success(newName + ";" + format);
        } catch (IOException e) {
            //返回异常
            rs = Result.error(e.getMessage());

        }
        return  rs;

    }

}
