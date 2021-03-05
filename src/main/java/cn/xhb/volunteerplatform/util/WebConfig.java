package cn.xhb.volunteerplatform.util;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * @author HaibiaoXu
 * @date Create in 10:48 2021/1/11
 * @modified By
 */
@Configuration
public class WebConfig extends WebMvcConfigurerAdapter {

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
}
