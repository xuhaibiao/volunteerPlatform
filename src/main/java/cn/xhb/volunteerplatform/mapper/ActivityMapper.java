package cn.xhb.volunteerplatform.mapper;

import cn.xhb.volunteerplatform.entity.Activity;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ActivityMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Activity record);

    int insertSelective(Activity record);

    Activity selectByPrimaryKey(Integer id);

    List<Activity> selectAll();

    List<Activity> selectByAddress(String address);

    int updateByPrimaryKeySelective(Activity record);

    int updateByPrimaryKey(Activity record);
}