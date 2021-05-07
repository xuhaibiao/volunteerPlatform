package cn.xhb.volunteerplatform.dto;

import cn.xhb.volunteerplatform.dto.vo.VolunteerListVo;
import cn.xhb.volunteerplatform.dto.vo.WorkerListVo;
import lombok.Data;

import java.util.List;


@Data
public class JoinListResponse {

    private List<VolunteerListVo> volunteerJoinList;

    private List<WorkerListVo> workerJoinList;


}




