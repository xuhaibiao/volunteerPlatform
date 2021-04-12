package cn.xhb.volunteerplatform.controller;


import cn.xhb.volunteerplatform.dto.LoginRequest;
import cn.xhb.volunteerplatform.dto.Result;
import cn.xhb.volunteerplatform.entity.BaseUser;
import cn.xhb.volunteerplatform.service.UserService;
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




}
