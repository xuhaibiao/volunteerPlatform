package cn.xhb.volunteerplatform.util;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * @author HaibiaoXu
 * @date Create in 10:48 2021/1/11
 * @modified By
 */
@Configuration
public class WebConfig extends WebMvcConfigurerAdapter {
    @Value("${path.picPath}")
    private String picPath;
    @Value("${path.show}")
    private String showPath;

    /**
     * 跨域配置
     * @param registry
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
            .allowedOrigins("Http://localhost:8080", "null")
            .allowedMethods("GET","PUT","POST","DELETE","OPTIONS")
            .allowCredentials(true)
            .maxAge(3600);
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        //  /home/file/**为前端URL访问路径  后面 file:xxxx为本地磁盘映射
        registry.addResourceHandler(showPath).addResourceLocations("file:" + picPath);
    }
}
