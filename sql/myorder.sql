
-- 订单表
CREATE TABLE `my_order` (
                            `order_id` int(8) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '订单id',
                            `order_money` decimal(8,2) NOT NULL COMMENT '订单金额',
                            `user_id` int(8) NOT NULL COMMENT '用户id',
                            `sub_province` varchar(20) NOT NULL COMMENT '下单时 省',
                            `sub_city` varchar(20)  NOT NULL COMMENT '下单时 市',
                            `sub_district` varchar(20) NOT NULL COMMENT '下单时 区',
                            `payment_status` int(1) NOT NULL DEFAULT '0' COMMENT '付款状态 0正常 1作废',
                            `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                            PRIMARY KEY (`order_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='拟订单表';

-- 订单商品表
CREATE TABLE `my_order_goods` (
                                  `order_goods_id` int(8) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '订单商品id',
                                  `order_id` int(8) NOT NULL COMMENT '订单id',
                                  `goods_id` int(8) NOT NULL COMMENT '商品id',
                                  `sub_goods_name` varchar(50)  NOT NULL COMMENT '下单时商品名称',
                                  `sub_goods_price` decimal(8,2) NOT NULL COMMENT '下单时商品价格',
                                  `goods_count` int(11) NOT NULL COMMENT '下单了多少件',
                                  PRIMARY KEY (`order_goods_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='拟订单下单商品表';

-- 商品表
CREATE TABLE `my_goods` (
                            `goods_id` int(8) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '商品id',
                            `goods_price` decimal(8,2) NOT NULL COMMENT '商品价格',
                            `goods_name` varchar(50) NOT NULL COMMENT '商品名称',
                            `goods_details` varchar(255) DEFAULT NULL COMMENT '商品详情',
                            PRIMARY KEY (`goods_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='拟商品表';



-- 初始化订单数据
INSERT INTO `my_order`(`order_id`, `order_money`, `user_id`, `sub_province`, `sub_city`, `sub_district`, `payment_status`, `create_time`) VALUES (1, 19.80, 1, '北京', '北京市', '西城区', 0, '2021-06-10 11:02:29');
INSERT INTO `my_order`(`order_id`, `order_money`, `user_id`, `sub_province`, `sub_city`, `sub_district`, `payment_status`, `create_time`) VALUES (2, 9.90, 1, '北京', '北京市', '丰台区', 0, '2021-06-10 11:02:59');
INSERT INTO `my_order`(`order_id`, `order_money`, `user_id`, `sub_province`, `sub_city`, `sub_district`, `payment_status`, `create_time`) VALUES (3, 300.00, 1, '北京', '北京市', '朝阳区', 0, '2021-06-10 11:03:16');
INSERT INTO `my_order`(`order_id`, `order_money`, `user_id`, `sub_province`, `sub_city`, `sub_district`, `payment_status`, `create_time`) VALUES (4, 66.60, 1, '北京', '北京市', '顺义区', 0, '2021-06-10 11:03:32');

-- 初始化商品数据
INSERT INTO `my_goods`(`goods_id`, `goods_price`, `goods_name`, `goods_details`) VALUES (1, 9.90, '两次性保温杯-改名称了~', '我是一只保温杯~');
INSERT INTO `my_goods`(`goods_id`, `goods_price`, `goods_name`, `goods_details`) VALUES (2, 100.00, '欧莱雅男士洗面奶', '只买贵的，不买对的~');
INSERT INTO `my_goods`(`goods_id`, `goods_price`, `goods_name`, `goods_details`) VALUES (3, 66.60, 'ipone13双面曲折屏', '是苹果，不是吃的那种...');

-- 初始化订单商品数据(暂时不考虑一对多)
INSERT INTO `my_order_goods`(`order_goods_id`, `order_id`, `goods_id`, `sub_goods_name`, `sub_goods_price`, `goods_count`) VALUES (1, 1, 1, '一次性保温杯', 9.90, 2);
INSERT INTO `my_order_goods`(`order_goods_id`, `order_id`, `goods_id`, `sub_goods_name`, `sub_goods_price`, `goods_count`) VALUES (2, 2, 1, '一次性保温杯', 9.90, 1);
INSERT INTO `my_order_goods`(`order_goods_id`, `order_id`, `goods_id`, `sub_goods_name`, `sub_goods_price`, `goods_count`) VALUES (3, 3, 2, '欧莱雅洗面奶', 100.00, 3);
INSERT INTO `my_order_goods`(`order_goods_id`, `order_id`, `goods_id`, `sub_goods_name`, `sub_goods_price`, `goods_count`) VALUES (4, 4, 3, '吃的苹果', 66.60, 1);




select mo.order_id, mg.goods_name, mog.goods_count,
       mg.goods_price, mo.order_money
from my_order mo
         left join my_order_goods mog on mo.order_id = mog.order_id
         left join my_goods mg on mog.goods_id = mg.goods_id



--    根据官网内容建立Flink 与 MySql的映射关系如下：

-- 在Flink SQL CDC中执行
-- 订单表
CREATE TABLE my_order (
                          order_id INT,
                          order_money DECIMAL(8, 2)
) WITH (
      'connector' = 'mysql-cdc',
      'hostname' = 'localhost',
      'port' = '3306',
      'username' = 'root',
      'password' = '123456',
      'database-name' = 'mytest',
      'table-name' = 'my_order'
      );

-- 商品表
CREATE TABLE my_goods (
                          goods_id INT,
                          goods_name STRING,
                          goods_price DECIMAL(8, 2)
) WITH (
      'connector' = 'mysql-cdc',
      'hostname' = 'localhost',
      'port' = '3306',
      'username' = 'root',
      'password' = '123456',
      'database-name' = 'mytest',
      'table-name' = 'my_goods'
      );

-- 订单商品表
CREATE TABLE my_order_goods (
                                order_id INT,
                                goods_id INT,
                                goods_count INT
) WITH (
      'connector' = 'mysql-cdc',
      'hostname' = 'localhost',
      'port' = '3306',
      'username' = 'root',
      'password' = '123456',
      'database-name' = 'mytest',
      'table-name' = 'my_order_goods'
      );


--如上所示，创建好Flink SQL CDC与MySql的映射关系后，我们再创建Flink SQL CDC与Elasticsearch的映射关系如下

-- 在Flink SQL CDC中执行
-- 下面的语法是执行查询将查询结果同步到ES中，如果ES没有安装x-pack插件就可以不用输入账号和密码项
CREATE TABLE order_index(
                            order_id INT,
                            goods_name STRING,
                            goods_count INT,
                            goods_price DECIMAL(8, 2),
                            order_money DECIMAL(8, 2),
                            PRIMARY KEY (order_id) NOT ENFORCED
) WITH (
      'connector' = 'elasticsearch-7',
      'hosts' = 'http://localhost:9200',
      'index' = 'order_index',
      'username' = 'username',
      'password' = 'password'
      );


--在完成上述步骤后，我们就可以开始往ES中写数据了，在CDC 中执行

insert into order_index
select mo.order_id, mg.goods_name, mog.goods_count,
       mg.goods_price, mo.order_money
from my_order mo
         left join my_order_goods mog on mo.order_id = mog.order_id
         left join my_goods mg on mog.goods_id = mg.goods_id;



--- 相关mysql sql插入mysql  flink sql  可以放 flink sql client操作  ，不过我这里是直接用代码处理的