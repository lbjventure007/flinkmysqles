package com.lakudouzi.flink.cdc;

import cn.hutool.extra.spring.SpringUtil;
import com.alibaba.fastjson2.JSONObject;
import com.lakudouzi.flink.cdc.domain.SystemDept;
import com.lakudouzi.flink.cdc.domain.User;
import com.lakudouzi.flink.cdc.repository.SystemDeptRepository;
import com.lakudouzi.flink.cdc.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.flink.streaming.api.functions.sink.SinkFunction;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Component;

import java.util.HashMap;

@Slf4j
@Component
public class DataChangeSink implements SinkFunction<DataChangeInfo> {

    private static final HashMap<String,String> mapTable;
    static {
         mapTable = new HashMap<>();
         mapTable.put("System_dept","SystemDept");  //这里做一个数据库表和对象的映射
         mapTable.put("User","User");



     };
    @Override
    public void invoke(DataChangeInfo dataChangeInfo, Context context) {
        // 变更类型： 0 初始化 1新增 2修改 3删除 4导致源中的现有表被截断的操作
        Integer operatorType = dataChangeInfo.getOperatorType();
        // TODO 数据处理,不能在方法外注入需要的bean，会报错必须实例化才可以
        //  (Caused by: org.apache.flink.api.common.InvalidProgramException:  xxx... is not serializable.
        //  The object probably contains or references non serializable fields.)，
        // 所以使用SpringUtil 获取需要的 bean，比如获取 extends ElasticsearchRepository<T, ID>的接口如下所示，
        // 然后就可以使用封装的方法进行增删改操作了
        String data = dataChangeInfo.getData();
        String table = dataChangeInfo.getTableName().toUpperCase();
        log.info("########data={}", table, data);
        System.out.println("########data={}" + table + "  " + data);


        try {
            String tname = dataChangeInfo.getTableName();
            String tableName = StringUtils.capitalize(tname);

            String objectName = mapTable.get(tableName);
            if (!("".equals(objectName))){
                String name ="com.lakudouzi.flink.cdc.domain."+ objectName;
                Class<?> aClass = Class.forName(name);
                Object tt = JSONObject.parseObject(data, aClass);


                String rname ="com.lakudouzi.flink.cdc.repository."+ objectName+"Repository";
                Class<?> bClass = Class.forName(rname);
                ElasticsearchRepository bean = (ElasticsearchRepository) SpringUtil.getBean(bClass);
                if (operatorType == 1 || operatorType == 2) {
                    bean.save(tt);
                }
                if (operatorType == 3) {
                    // 删除/修改（逻辑删除）
                    bean.delete(tt);
                }
            }


        }catch (Exception exception){
           exception.printStackTrace();
        }



//        if ("SYSTEM_DEPT".equals(table)) {
//            SystemDept systemDept = JSONObject.parseObject(data, SystemDept.class);
//
//            SystemDeptRepository bean = SpringUtil.getBean(SystemDeptRepository.class);
//            // 初始化/新增/修改（非逻辑删除）
//            System.out.println("操作类型-----"+operatorType);
//            System.out.println("操作内容-----"+systemDept);
//            if (operatorType==1 || operatorType ==2) {
//                bean.save(systemDept);
//            }
//            if (operatorType ==3 ){
//                // 删除/修改（逻辑删除）
//                bean.delete(systemDept);
//            }
//        }
//
//        System.out.println("-----+++__--00"+table);
//        if ("USER".equals(table)) {
//            System.out.println("-----+++__--11"+table);
//            User user = JSONObject.parseObject(data, User.class);
//
//            UserRepository bean = SpringUtil.getBean(UserRepository.class);
//            // 初始化/新增/修改（非逻辑删除）
//            System.out.println("操作类型-----"+operatorType);
//            System.out.println("操作内容-----"+user);
//            if (operatorType==1 || operatorType ==2) {
//                bean.save(user);
//            }
//            if (operatorType ==3 ){
//                // 删除/修改（逻辑删除）
//                bean.delete(user);
//            }
//        }



    }

}