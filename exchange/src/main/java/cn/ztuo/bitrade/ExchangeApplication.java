package cn.ztuo.bitrade;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;


@EnableDiscoveryClient
@SpringBootApplication
public class ExchangeApplication {
    public static void main(String[] args){
        SpringApplication. run(ExchangeApplication.class,args);
    }
}
