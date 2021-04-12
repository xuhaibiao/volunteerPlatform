package cn.xhb.volunteerplatform.mapper;

import cn.xhb.volunteerplatform.dto.ActivitySearchQuery;
import cn.xhb.volunteerplatform.entity.Activity;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ActivityMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Activity record);

    int insertSelective(Activity record);

    Activity selectByPrimaryKey(Integer id);

    List<Activity> selectAll();

    List<Activity> selectNotDeleted();

    List<Activity> selectNotDeletedByWorkerId(Integer workerId);

    List<Activity> selectNotDeletedBySearch(@Param("activitySearchQuery") ActivitySearchQuery activitySearchQuery);

    int updateByPrimaryKeySelective(Activity record);

    int updateByPrimaryKey(Activity record);
}