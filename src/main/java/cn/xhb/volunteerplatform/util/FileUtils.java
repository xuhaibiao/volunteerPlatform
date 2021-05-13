package cn.xhb.volunteerplatform.util;

import cn.xhb.volunteerplatform.dto.Result;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

public class FileUtils {

    public static Result<String> uploadPic(MultipartFile file, HttpServletRequest req) {
        if (file == null) {
            return Result.error("上传图片为空");
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        //再用pdf格式开始书写,先找原始的名字
        String originName = file.getOriginalFilename();
//        判断文件类型是不是pdf
        if(!(originName.endsWith(".jpg")||originName.endsWith(".JPG")
                ||originName.endsWith(".jpeg") ||originName.endsWith(".JPEG")
                ||originName.endsWith(".png")||originName.endsWith(".PNG"))){
            //如果不是的话，就返回类型
            return Result.error("文件类型不对");

        }

        String format=sdf.format(new Date());
//        String realPath = "C:\\Users\\Lucas\\IdeaProjects\\volunteerPlatform\\picData\\" + format;
        String realPath = "E:\\Java_in_idea\\volunteerPlatform\\picData\\" + format;

        //再是保存文件的文件夹
        File folder = new File(realPath);
        //如果不存在，就自己创建
        if(!folder.exists()){
            folder.mkdirs();
        }
        String newName = UUID.randomUUID().toString() + ".jpg";
        Result<String> rs;
        //然后就可以保存了
        try {
            file.transferTo(new File(folder,newName));
            //这个还有一个url
//            String url = req.getScheme() + "://" + req.getServerName() + ":" + req.getServerPort() + "/" + realPath + newName;
            //如果指向成功了
//            rs = Result.success(url);
            rs = Result.success(newName + ";" + format);
        } catch (IOException e) {
            //返回异常
            rs = Result.error(e.getMessage());

        }
        return  rs;

    }
}
