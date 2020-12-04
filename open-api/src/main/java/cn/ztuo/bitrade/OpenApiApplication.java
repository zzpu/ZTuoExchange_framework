package cn.ztuo.bitrade;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * openApi工程
 * @Author MrGao
 */
@EnableScheduling
@SpringBootApplication
@EnableDiscoveryClient
public class OpenApiApplication {
    public static void main(String[] args){
        SpringApplication.run(OpenApiApplication.class,args);
    }
}
