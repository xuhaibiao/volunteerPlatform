package cn.xhb.volunteerplatform.controller;


import cn.xhb.volunteerplatform.dto.InformationEditRequest;
import cn.xhb.volunteerplatform.dto.LoginRequest;
import cn.xhb.volunteerplatform.dto.Result;
import cn.xhb.volunteerplatform.dto.StatisticsDataResponse;
import cn.xhb.volunteerplatform.dto.vo.FiveYearNumberVo;
import cn.xhb.volunteerplatform.entity.Administrator;
import cn.xhb.volunteerplatform.entity.BaseUser;
import cn.xhb.volunteerplatform.entity.Volunteer;
import cn.xhb.volunteerplatform.entity.Worker;
import cn.xhb.volunteerplatform.service.StatisticsService;
import cn.xhb.volunteerplatform.service.UserService;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

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

    @PostMapping("/login")
    public Result<BaseUser> login(@RequestBody LoginRequest loginRequest) {
        BaseUser user = null;
        int type = loginRequest.getType();
        if (type == 0) {
            user = userService.getVolunteerByIdCard(loginRequest.getUsername());
        } else if (type == 1) {
            user = userService.getWorkerByIdCard(loginRequest.getUsername());
        } else if (type == 2) {
            user = userService.getAdministratorByIdCard(loginRequest.getUsername());
        }

        if (user != null && user.getPassword().equals(loginRequest.getPassword()) ) {
            // 密码不返前端
//            user.setPassword(null);
            return Result.success(user, String.valueOf(type));
        } else{
            return Result.error();
        }
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





}
