package cn.xhb.volunteerplatform.mapper;

import cn.xhb.volunteerplatform.entity.Administrator;

import org.springframework.stereotype.Repository;

@Repository
public interface AdministratorMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Administrator record);

    int insertSelective(Administrator record);

    Administrator selectByPrimaryKey(Integer id);

    Administrator selectByIdCard(String idCard);

    int updateByPrimaryKeySelective(Administrator record);

    int updateByPrimaryKey(Administrator record);
}