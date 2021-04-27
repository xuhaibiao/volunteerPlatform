package cn.xhb.volunteerplatform.mapper;

import cn.xhb.volunteerplatform.entity.Volunteer;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VolunteerMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Volunteer record);

    int insertSelective(Volunteer record);

    List<Volunteer> selectAll();

    Volunteer selectByPrimaryKey(Integer id);

    Volunteer selectByIdCard(String idCard);

    int updateByPrimaryKeySelective(Volunteer record);

    int updateByPrimaryKey(Volunteer record);

    List<Volunteer> selectByCommunityId(Integer communityId);

    int countByYear(int year);

    int countByGender(int gender);
}