package cn.xhb.volunteerplatform.mapper;

import cn.xhb.volunteerplatform.entity.User;
import org.springframework.stereotype.Repository;

/**
 * @author HaibiaoXu
 * @date Create in 10:11 2021/3/4
 * @modified By
 */
@Repository
public interface UserMapper {
    User getByUsername(String username);
}
