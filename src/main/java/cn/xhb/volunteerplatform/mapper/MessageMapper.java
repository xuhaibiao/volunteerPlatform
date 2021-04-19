package cn.xhb.volunteerplatform.mapper;

import cn.xhb.volunteerplatform.entity.Message;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Message record);

    int insertSelective(Message record);

    Message selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Message record);

    int updateByPrimaryKey(Message record);

    List<Message> selectNotDealBySenderAndRecipientAndType(@Param("sender")Integer sender, @Param("recipient")Integer recipient, @Param("type")Integer type);

    List<Message> selectWorkerCommunityMessageByRecipient(Integer communityId);

    List<Message> selectSystemMessage();
}