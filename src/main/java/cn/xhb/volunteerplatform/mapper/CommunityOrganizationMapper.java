package cn.xhb.volunteerplatform.mapper;

import cn.xhb.volunteerplatform.entity.CommunityOrganization;
import org.springframework.stereotype.Repository;

@Repository
public interface CommunityOrganizationMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(CommunityOrganization record);

    int insertSelective(CommunityOrganization record);

    CommunityOrganization selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(CommunityOrganization record);

    int updateByPrimaryKey(CommunityOrganization record);
}