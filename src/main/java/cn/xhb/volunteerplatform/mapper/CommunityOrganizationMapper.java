package cn.xhb.volunteerplatform.mapper;

import cn.xhb.volunteerplatform.dto.CommunitySearchQuery;
import cn.xhb.volunteerplatform.entity.CommunityOrganization;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommunityOrganizationMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(CommunityOrganization record);

    int insertSelective(CommunityOrganization record);

    CommunityOrganization selectByPrimaryKey(Integer id);

    List<CommunityOrganization> selectNotDeleted();

    int updateByPrimaryKeySelective(CommunityOrganization record);

    int updateByPrimaryKey(CommunityOrganization record);

    List<CommunityOrganization> selectNotDeletedBySearch(@Param("communitySearchQuery") CommunitySearchQuery communitySearchQuery);
}