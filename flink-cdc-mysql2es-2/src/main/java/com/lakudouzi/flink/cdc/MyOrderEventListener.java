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
public class MyOrderEventListener implements ApplicationRunner {

    @Override
    public void run(ApplicationArguments args) {
        mysql2es1();
    }

    /**
     * mysql to es
     */
    private void mysql2es1() {
        try {
            StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
            // 设置1个并行源任务

            env.setParallelism(1);


            StreamTableEnvironment tableEnv = StreamTableEnvironment.create(env);


            String sourcemy_orderDDl = """
                    CREATE TABLE my_order (
                      `order_id` INT,
                      `order_money` DECIMAL(8, 2),
                      PRIMARY KEY (`order_id`)  NOT ENFORCED ) WITH (
                      'connector' = 'mysql-cdc',
                     'hostname' = 'localhost',
                     'port' = '3306',
                     'username' = 'root',
                     'password' = '1234qwer',
                     'database-name' = 'test',
                     'table-name' = 'my_order'
                    );
                    """;
            String sourcemy_goodsDDl = """
                    CREATE TABLE my_goods (
                         `goods_id` INT,
                         `goods_name` STRING,
                         `goods_price` DECIMAL(8, 2),
                           PRIMARY KEY (`goods_id`)  NOT ENFORCED
                       ) WITH (
                         'connector' = 'mysql-cdc',
                         'hostname' = 'localhost',
                         'port' = '3306',
                         'username' = 'root',
                         'password' = '1234qwer',
                         'database-name' = 'test',
                         'table-name' = 'my_goods'
                       );
                    """;

            String sourcemy_order_goodsddl = """
                    CREATE TABLE my_order_goods (
                         `order_id` INT,
                         `goods_id` INT,
                         `goods_count` INT
                       ) WITH (
                         'connector' = 'mysql-cdc',
                         'hostname' = 'localhost',
                         'port' = '3306',
                         'username' = 'root',
                         'password' = '1234qwer',
                         'database-name' = 'test',
                         'table-name' = 'my_order_goods',
                          'scan.incremental.snapshot.enabled' = 'false'
                       );
                    """;

            String orderIndexsinkDDl = """
                    CREATE TABLE order_index(
                            `order_id` INT,
                           `goods_name` STRING,
                           `goods_count` INT,
                           `goods_price` DECIMAL(8, 2),
                           `order_money` DECIMAL(8, 2),
                           PRIMARY KEY (order_id) NOT ENFORCED
                         ) WITH (
                             'connector' = 'elasticsearch-7',
                             'hosts' = 'http://localhost:9200',
                             'index' = 'order_index',
                             'username' = 'elastic',
                             'password' = 'elastic'
                         );
                    """;


            //    order start

            tableEnv.executeSql(sourcemy_orderDDl);
            tableEnv.executeSql(sourcemy_goodsDDl);
            tableEnv.executeSql(sourcemy_order_goodsddl);

            tableEnv.executeSql(orderIndexsinkDDl);
            // 简单的聚合处理
            String transformSQL= "insert into order_index select mo.order_id,mg.goods_name, mog.goods_count,  mo.order_money,mg.goods_price from my_order mo left join my_order_goods mog on mo.order_id = mog.order_id left join my_goods mg on mog.goods_id = mg.goods_id;";

            //order end


            TableResult result = tableEnv.executeSql(transformSQL);
            result.print();

            env.executeAsync("mysql-cdc-es1");
        } catch (Exception e) {
            System.out.println("mysql --> es, Exception="+ e);
        }
    }
}