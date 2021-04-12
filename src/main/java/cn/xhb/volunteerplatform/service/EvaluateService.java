package cn.xhb.volunteerplatform.service;

import cn.xhb.volunteerplatform.constant.RecordConstant;
import cn.xhb.volunteerplatform.entity.VolunteerRecord;
import cn.xhb.volunteerplatform.mapper.VolunteerRecordMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class EvaluateService {
    @Resource
    VolunteerRecordMapper volunteerRecordMapper;

    public int volunteerEvaluate(Float score, String content, Integer volunteerRecordId) {
        VolunteerRecord volunteerRecord = new VolunteerRecord();
        volunteerRecord.setId(volunteerRecordId);
        volunteerRecord.setVolunteerEvaluateScore(score);
        volunteerRecord.setVolunteerEvaluateContent(content);
        volunteerRecord.setStatus(RecordConstant.HAS_EVALUATE);
        return volunteerRecordMapper.updateByPrimaryKeySelective(volunteerRecord);
    }
}
