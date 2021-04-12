package cn.xhb.volunteerplatform.mapper;

import cn.xhb.volunteerplatform.entity.VolunteerRecord;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VolunteerRecordMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(VolunteerRecord record);

    int insertSelective(VolunteerRecord record);

    VolunteerRecord selectByPrimaryKey(Integer id);

    int countByVolunteerIdAndActivityId(Integer activityId, Integer volunteerId);

    int countByActivityIdAndStatus(Integer activityId, Integer status);


    List<VolunteerRecord> selectByVolunteerId(Integer volunteerId);

    List<VolunteerRecord> selectByActivityIdAndStatus(Integer activityId, Integer status);

    List<VolunteerRecord> selectByActivityIdAndTwoStatus(Integer activityId, Integer status1, Integer status2);

    int countByActivity(Integer activityId);

    int updateByPrimaryKeySelective(VolunteerRecord record);

    int updateByPrimaryKey(VolunteerRecord record);
}