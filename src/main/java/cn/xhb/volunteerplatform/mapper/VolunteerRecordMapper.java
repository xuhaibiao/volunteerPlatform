package cn.xhb.volunteerplatform.mapper;

import cn.xhb.volunteerplatform.entity.VolunteerRecord;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Repository
public interface VolunteerRecordMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(VolunteerRecord record);

    int insertSelective(VolunteerRecord record);

    VolunteerRecord selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(VolunteerRecord record);

    int updateByPrimaryKey(VolunteerRecord record);

    int countByActivity(Integer activityId);
}