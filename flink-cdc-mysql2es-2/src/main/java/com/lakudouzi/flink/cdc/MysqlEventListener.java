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
                            CREATE TABLE IF NOT EXISTS system_dept (
                                id BIGINT,
                                name VARCHAR(30),
                                sort INT,
                                leader_user_id BIGINT,
                                phone VARCHAR(11),
                                email VARCHAR(50),
                                status TINYINT,
                                creator VARCHAR(64),
                                create_time TIMESTAMP(19),
                                PRIMARY KEY(id) NOT ENFORCED
                            ) WITH (
                                'connector' = 'mysql-cdc',
                                'hostname' = 'localhost',
                                'port' = '3306',
                                'username' = 'flinkcdc',
                                'password' = 'flinkcdc',
                                'database-name' = 'db01',
                                'table-name' = 'system_dept'
                            )
                            """;
            // 输出目标表
            String sinkDDL =
                    """
                            CREATE TABLE IF NOT EXISTS system_dept_es (
                                id BIGINT,
                                name VARCHAR(30),
                                sort INT,
                                leader_user_id BIGINT,
                                phone VARCHAR(11),
                                email VARCHAR(50),
                                status TINYINT,
                                creator VARCHAR(64),
                                create_time TIMESTAMP(19),
                                PRIMARY KEY(id) NOT ENFORCED
                            ) WITH (
                                'connector' = 'elasticsearch-7',
                                'hosts' = 'http://localhost:9200',
                                'index' = 'system_dept_search',
                                'sink.bulk-flush.max-actions' = '1'
                            )
                            """;
            // 简单的聚合处理
            String transformSQL = "INSERT INTO system_dept_es SELECT * FROM system_dept";
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