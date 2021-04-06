package cn.xhb.volunteerplatform.service;

import cn.xhb.volunteerplatform.entity.Administrator;
import cn.xhb.volunteerplatform.entity.Volunteer;
import cn.xhb.volunteerplatform.entity.Worker;
import cn.xhb.volunteerplatform.mapper.AdministratorMapper;
import cn.xhb.volunteerplatform.mapper.VolunteerMapper;
import cn.xhb.volunteerplatform.mapper.WorkerMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class UserService {
    @Resource
    AdministratorMapper administratorMapper;
    @Resource
    VolunteerMapper volunteerMapper;
    @Resource
    WorkerMapper workerMapper;

    public Volunteer getVolunteerByIdCard(String idCard) {
        return volunteerMapper.selectByIdCard(idCard);
    }

    public Worker getWorkerByIdCard(String idCard) {
        return workerMapper.selectByIdCard(idCard);
    }

    public Administrator getAdministratorByIdCard(String idCard) {
        return administratorMapper.selectByIdCard(idCard);
    }

}
