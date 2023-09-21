# flink cdc mysql es
### 一个通过datastream 
    flink-cdc-mysql2es 当前的数据库版本为mysql8+ es8.6.2 kibana 8.6.2
    单表的捕获写入es
### 一个通过 sql
    flink-cdc-mysql2es-2 当前的数据库版本为mysql8+ es7.3.0 kibana 7.3.0
    关联join 和 单表的捕获 都可以写入es  


# 说明
    es版本数据库版本 根据自己的需求 调整相关依赖 或者 相关参数依赖等
# 遇到的问题
    我的版本是java 19 SDK 执行 flink-cdc-mysql2es-2这个项目的时候 需增加VM option
    --add-opens java.base/java.lang=ALL-UNNAMED --add-opens java.base/java.lang.invoke=ALL-UNNAMED 
    --add-opens java.base/java.math=ALL-UNNAMED --add-opens java.base/java.util=ALL-UNNAMED
    --add-opens java.base/java.nio=ALL-UNNAMED --add-opens java.base/sun.nio.ch=ALL-UNNAMED 
    --add-opens java.base/java.io=ALL-UNNAMED --add-opens java.rmi/sun.rmi.transport=ALL-UNNAMED