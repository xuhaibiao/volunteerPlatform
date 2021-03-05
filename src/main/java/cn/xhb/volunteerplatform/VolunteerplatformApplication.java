package cn.xhb.volunteerplatform;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("cn.xhb.volunteerplatform.mapper")
public class VolunteerplatformApplication {

    public static void main(String[] args) {
        SpringApplication.run(VolunteerplatformApplication.class, args);
    }

}
