package cn.xhb.volunteerplatform.controller;

import cn.xhb.volunteerplatform.dto.ActivityResponse;
import cn.xhb.volunteerplatform.dto.Result;
import cn.xhb.volunteerplatform.service.ActivityService;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/volunteer")
public class VolunteerController {

    @Resource
    ActivityService activityService;

    @GetMapping("/activity")
    public Result<List<ActivityResponse>> getActivities(){
        List<ActivityResponse> rs = activityService.getActivities();
        return Result.success(rs);

    }
    @GetMapping("/activity/some")
    public Result<List<ActivityResponse>> getActivitiesByPlace(@RequestParam("province") String province,@RequestParam("city") String city,@RequestParam("area") String area){
        String address = province + city + area;
        List<ActivityResponse> rs = activityService.getActivitiesByAddress(address);
        return Result.success(rs);

    }
    @GetMapping("/activity/search")
    public Result<List<ActivityResponse>> getActivitiesBySearch(@RequestParam("province") String province,@RequestParam("city") String city,@RequestParam("area") String area,@RequestParam("activityName") String activityName){
        String address = "";
//        if(city!=null&&city.equals())
        List<ActivityResponse> rs = activityService.getActivitiesByAddress(address);
        return Result.success(rs);

    }
}
