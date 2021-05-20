package cn.xhb.volunteerplatform.util;

import cn.xhb.volunteerplatform.dto.Result;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

public class FileUtils {

    public static String fileDataPath = "C:\\WHUTData\\fileData\\";
    public static String picDataPath = "C:\\WHUTData\\picData\\";

    public static Result<String> uploadPic(MultipartFile file, HttpServletRequest req) {
        if (file == null) {
            return Result.error("上传图片为空");
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        // 找到原始的名字
        String originName = file.getOriginalFilename();
        // 判断文件类型
        if(!(originName.endsWith(".jpg")||originName.endsWith(".JPG")
                ||originName.endsWith(".jpeg") ||originName.endsWith(".JPEG")
                ||originName.endsWith(".png")||originName.endsWith(".PNG"))){
            //如果不是的话，就返回类型
            return Result.error("文件类型不对");

        }
        String format=sdf.format(new Date());
        String realPath = picDataPath + format;
        // 再是保存文件的文件夹
        File folder = new File(realPath);
        // 如果不存在，就自己创建
        if(!folder.exists()){
            folder.mkdirs();
        }
        String newName = UUID.randomUUID().toString() + ".jpg";
        Result<String> rs;
        //然后就可以保存了
        try {
            file.transferTo(new File(folder,newName));
            rs = Result.success(newName + ";" + format);
        } catch (IOException e) {
            //返回异常
            rs = Result.error(e.getMessage());
        }
        return  rs;
    }

    public static void download(String fileName,String fileDate, HttpServletRequest req, HttpServletResponse resp) {
        BufferedInputStream bis = null;
        BufferedOutputStream bos = null;
        OutputStream fos = null;
        try {
            String filePath = fileDataPath;
            bis = new BufferedInputStream(new FileInputStream(filePath + "/" + fileDate + "/" + fileName));
            fos = resp.getOutputStream();
            bos = new BufferedOutputStream(fos);
            setFileDownloadHeader(req, resp, fileName);
            resp.setContentType("application/octet-stream");
            resp.setHeader("Content-Disposition", "attachment;filename=" + new String(fileName.getBytes(StandardCharsets.UTF_8), "iso-8859-1"));
            int byteRead = 0;
            byte[] buffer = new byte[2048];
            while ((byteRead = bis.read(buffer)) != -1) {
                bos.write(buffer, 0, byteRead);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                assert bos != null;
                bos.flush();
                bis.close();
                fos.close();
                bos.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    public static void setFileDownloadHeader(HttpServletRequest request,
                                             HttpServletResponse response, String fileName) {
        try {
            String encodedFileName = null;
            String agent = request.getHeader("USER-AGENT");
            if (null != agent && agent.contains("MSIE")) {
                encodedFileName = URLEncoder.encode(fileName, "UTF-8");
            } else if (null != agent && agent.contains("Mozilla")) {
                encodedFileName = new String(fileName.getBytes(StandardCharsets.UTF_8),
                        StandardCharsets.ISO_8859_1);
            } else {
                encodedFileName = URLEncoder.encode(fileName, "UTF-8");
            }

            response.setContentType("application/x-download;charset=UTF-8");
            response.setHeader("Content-Disposition", "attachment;filename=\""
                    + encodedFileName + "\"");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Result<String> uploadCommunityFile(MultipartFile file) {
        if (file == null) {
            return Result.error("上传文件为空");
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        //再用pdf格式开始书写,先找原始的名字
        String originName = file.getOriginalFilename();
//        判断文件类型是不是pdf
        if(!(originName.endsWith(".pdf")||originName.endsWith(".PDF"))){
            //如果不是的话，就返回类型
            return Result.error("文件类型不对");

        }
        String format=sdf.format(new Date());
        String realPath = fileDataPath + format;
        //再是保存文件的文件夹
        File folder = new File(realPath);
        //如果不存在，就自己创建
        if(!folder.exists()){
            folder.mkdirs();
        }
        String newName = UUID.randomUUID().toString() + ".pdf";
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
