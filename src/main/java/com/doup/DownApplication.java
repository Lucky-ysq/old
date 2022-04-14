package com.doup;

import com.doup.domain.FileProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties({FileProperties.class})
public class DownApplication {

    public static void main(String[] args) {
        SpringApplication.run(DownApplication.class, args);
    }

}
