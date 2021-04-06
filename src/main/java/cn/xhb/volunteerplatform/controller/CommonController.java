package cn.xhb.volunteerplatform.controller;


import cn.xhb.volunteerplatform.dto.LoginRequest;
import cn.xhb.volunteerplatform.dto.Result;
import cn.xhb.volunteerplatform.entity.BaseUser;
import cn.xhb.volunteerplatform.service.UserService;
import com.google.gson.Gson;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

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
            return Result.success(user, String.valueOf(type));
        } else{
            return Result.error();
        }
    }


}
