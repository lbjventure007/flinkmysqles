package com.lakudouzi.flink.cdc;

import lombok.extern.slf4j.Slf4j;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.table.api.TableResult;
import org.apache.flink.table.api.bridge.java.StreamTableEnvironment;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class MysqlEventListener implements ApplicationRunner {

    @Override
    public void run(ApplicationArguments args) {
        mysql2es();
    }

    /**
     * mysql to es
     */
    private void mysql2es() {
        try {
            StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
            // 设置1个并行源任务

            env.setParallelism(1);


            StreamTableEnvironment tableEnv = StreamTableEnvironment.create(env);



            // 数据源表
            String sourceDDL =
                    """
                            create table product_view_source (
                             `id` int,
                             `user_id` int,
                             `product_id` int,
                             `server_id` int,
                             `duration` int,
                             `times` string,
                             `time` timestamp,
                             PRIMARY KEY (`id`) NOT ENFORCED) WITH (
                             'connector' = 'mysql-cdc',
                             'hostname' = 'localhost',
                             'port' = '3306',
                             'username' = 'root',
                             'password' = '1234qwer',
                             'database-name' = 'test',
                             'table-name' = 'product_view'
                             );
                            """;
            // 输出目标表
            String sinkDDL =
                    """
                           
                             
                             CREATE TABLE product_view_index(
                             `id` int,
                             `user_id` int,
                             `product_id` int,
                             `server_id` int,
                             `duration` int,
                             `times` string,
                             `time` timestamp,
                               PRIMARY KEY (id) NOT ENFORCED
                             ) WITH (
                                 'connector' = 'elasticsearch-7',
                                 'hosts' = 'http://localhost:9200',
                                 'index' = 'product_view_index',
                                 'username' = 'elastic',
                                 'password' = 'elastic'
                             );
                            """;



            // 简单的聚合处理
            String transformSQL = "insert into product_view_index select * from product_view_source;";
            log.info("============================");
            tableEnv.executeSql(sourceDDL);
            tableEnv.executeSql(sinkDDL);



            TableResult result = tableEnv.executeSql(transformSQL);


            result.print();
            env.executeAsync("mysql-cdc-es");
        } catch (Exception e) {
            log.error("mysql --> es, Exception=", e);
        }
    }
}