package cn.xhb.volunteerplatform.mapper;

import cn.xhb.volunteerplatform.entity.Worker;
import org.springframework.stereotype.Repository;

@Repository
public interface WorkerMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Worker record);

    int insertSelective(Worker record);

    Worker selectByPrimaryKey(Integer id);

    Worker selectByIdCard(String idCard);

    int updateByPrimaryKeySelective(Worker record);

    int updateByPrimaryKey(Worker record);
}