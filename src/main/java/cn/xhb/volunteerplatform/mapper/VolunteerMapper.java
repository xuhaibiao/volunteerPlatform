package cn.xhb.volunteerplatform.mapper;

import cn.xhb.volunteerplatform.entity.Volunteer;
import org.springframework.stereotype.Repository;

@Repository
public interface VolunteerMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Volunteer record);

    int insertSelective(Volunteer record);

    Volunteer selectByPrimaryKey(Integer id);

    Volunteer selectByIdCard(String idCard);

    int updateByPrimaryKeySelective(Volunteer record);

    int updateByPrimaryKey(Volunteer record);
}