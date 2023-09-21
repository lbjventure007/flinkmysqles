package com.lakudouzi.flink.cdc.domain;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.io.Serializable;
import java.util.Date;

/**
 * 部门表
 * @TableName system_dept
 */
@Data
@Document(indexName = "flink-user-index")
public class User implements Serializable {
    /**
     * 部门id
     */
    @Id
    private Long id;
    /**
     * 部门名称
     */
    @Field(type = FieldType.Text,store = true)
    private String username;
    /**
     * 显示顺序
     */
    @Field(type = FieldType.Text)
    private String password;
    /**
     * 负责人
     */
    @Field(type = FieldType.Long)
    private Long phone;
    /**
     * 联系电话
     */
    @Field(type = FieldType.Text)
    private String queston;
    /**
     * 邮箱
     */
    @Field(type = FieldType.Text)
    private String answer;
    /**
     * 部门状态（0正常 1停用）
     */
    @Field(type = FieldType.Double)
    private Double balance;
    /**
     * 创建者
     */
    @Field(type = FieldType.Double)
    private Double forzen_balance;
    /**
     * 创建时间
     */
    @Field(type = FieldType.Date)
    private Date create_time;
    /**
     * 创建时间
     */
    @Field(type = FieldType.Date)
    private Date update_time;
}
