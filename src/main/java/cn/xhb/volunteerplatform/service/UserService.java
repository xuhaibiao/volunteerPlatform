package cn.xhb.volunteerplatform.service;

import cn.xhb.volunteerplatform.entity.User;
import cn.xhb.volunteerplatform.mapper.UserMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author HaibiaoXu
 * @date Create in 10:56 2021/3/4
 * @modified By
 */
@Service
public class UserService {
    @Resource
    UserMapper userMapper;

    public User getByUsername(String username) {
        return userMapper.getByUsername(username);
    }
}
