package cn.xhb.volunteerplatform.service;

import cn.xhb.volunteerplatform.dto.vo.FiveYearNumberVo;
import cn.xhb.volunteerplatform.mapper.ActivityMapper;
import cn.xhb.volunteerplatform.mapper.VolunteerMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Calendar;

@Service
public class StatisticsService {
    @Resource
    ActivityMapper activityMapper;
    @Resource
    VolunteerMapper volunteerMapper;


    public FiveYearNumberVo getNumInFiveYear() {
        FiveYearNumberVo fiveYearNumberVo = new FiveYearNumberVo();
        int[] years = new int[5];
        int[] activityNums = new int[5];
        int[] volunteerNums = new int[5];
        int beginYear = Calendar.getInstance().get(Calendar.YEAR) - 4;
        for (int i = 0; i < 5; i++) {
            years[i] = beginYear;
            int activityNum = activityMapper.countByYear(beginYear);
            int volunteerNum = volunteerMapper.countByYear(beginYear);
            activityNums[i] = activityNum;
            volunteerNums[i] = volunteerNum;
            beginYear++;
        }
        fiveYearNumberVo.setActivityNums(activityNums);
        fiveYearNumberVo.setYears(years);
        fiveYearNumberVo.setVolunteerNums(volunteerNums);
        return fiveYearNumberVo;
    }

    public int[] getSexRatio() {
        int[] rs = new int[2];
        int female = volunteerMapper.countByGender(0);
        int male = volunteerMapper.countByGender(1);
        rs[0] = female;
        rs[1] = male;
        return rs;
    }
}
