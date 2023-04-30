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
@Document(indexName = "system_dept_search")
public class SystemDept implements Serializable {

    /**
     * 部门id
     */
    @Id
    private Long id;
    /**
     * 部门名称
     */
    @Field(type = FieldType.Text,store = true)
    private String name;
    /**
     * 显示顺序
     */
    @Field(type = FieldType.Integer)
    private Integer sort;
    /**
     * 负责人
     */
    @Field(type = FieldType.Long)
    private Long leaderUserId;
    /**
     * 联系电话
     */
    @Field(type = FieldType.Text)
    private String phone;
    /**
     * 邮箱
     */
    @Field(type = FieldType.Text)
    private String email;
    /**
     * 部门状态（0正常 1停用）
     */
    @Field(type = FieldType.Integer)
    private Integer status;
    /**
     * 创建者
     */
    @Field(type = FieldType.Text)
    private String creator;
    /**
     * 创建时间
     */
    @Field(type = FieldType.Date)
    private Date createTime;

}
