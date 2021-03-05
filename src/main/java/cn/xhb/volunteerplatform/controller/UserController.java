package cn.xhb.volunteerplatform.controller;


import cn.xhb.volunteerplatform.dto.Result;
import cn.xhb.volunteerplatform.entity.User;
import cn.xhb.volunteerplatform.service.UserService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author HaibiaoXu
 * @date Create in 10:41 2021/1/11
 * @modified By
 */
@RestController
public class UserController {

    @Resource
    UserService userService;

    @RequestMapping("/login")
    public String login(@RequestBody User user) {
        User rs = userService.getByUsername(user.getUsername());
        if (rs != null && rs.getPassword().equals(user.getPassword()) ) {
            Gson gson = new Gson();
            return gson.toJson(Result.success(rs));

        } else{
            Gson gson = new Gson();
            return gson.toJson(Result.error());
        }

    }
}
