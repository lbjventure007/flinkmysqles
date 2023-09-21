package com.lakudouzi.flink;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

@SpringBootApplication
@EnableElasticsearchRepositories
public class FlinkCdcMysql2esApplication {

    public static void main(String[] args) {
        SpringApplication.run(FlinkCdcMysql2esApplication.class, args);
    }

}
