create table if not exists db01.system_dept
(
    id             BIGINT(19) auto_increment comment '部门id'
        primary key,
    name           VARCHAR(30)  default ''                not null comment '部门名称',
    parent_id      BIGINT(19)   default 0                 not null comment '父部门id',
    sort           INT(10)      default 0                 not null comment '显示顺序',
    leader_user_id BIGINT(19)                             null comment '负责人',
    phone          VARCHAR(11)                            null comment '联系电话',
    email          VARCHAR(50)                            null comment '邮箱',
    status         TINYINT(3)                             not null comment '部门状态（0正常 1停用）',
    creator        VARCHAR(64)  default ''                null comment '创建者',
    create_time    DATETIME(19) default CURRENT_TIMESTAMP not null comment '创建时间',
    updater        VARCHAR(64)  default ''                null comment '更新者',
    update_time    DATETIME(19) default CURRENT_TIMESTAMP not null comment '更新时间',
    deleted        BIT          default b'0'              not null comment '是否删除',
    tenant_id      BIGINT(19)   default 0                 not null comment '租户编号'
)
    comment '部门表';

insert into db01.system_dept (id, name, parent_id, sort, leader_user_id, phone, email, status, creator, create_time,
                              updater, update_time, deleted, tenant_id)
values (100, '修改一下', 0, 0, 1, '15888888888', 'ry@qq.com', 0, 'admin', '2021-01-05 17:03:47', '1',
        '2023-05-01 21:55:37', false, 1),
       (101, '深圳总公司', 100, 1, 104, '15888888888', 'ry@qq.com', 0, 'admin', '2021-01-05 17:03:47', '1',
        '2022-05-16 20:25:23', false, 1),
       (102, '长沙分公司', 100, 2, null, '15888888888', 'ry@qq.com', 0, 'admin', '2021-01-05 17:03:47', '',
        '2021-12-15 05:01:40', false, 1),
       (103, '研发部门', 101, 1, 104, '15888888888', 'ry@qq.com', 0, 'admin', '2021-01-05 17:03:47', '103',
        '2022-01-14 01:04:14', false, 1),
       (104, '市场部门', 101, 2, null, '15888888888', 'ry@qq.com', 0, 'admin', '2021-01-05 17:03:47', '',
        '2021-12-15 05:01:38', false, 1),
       (105, '测试部门', 101, 3, null, '15888888888', 'ry@qq.com', 0, 'admin', '2021-01-05 17:03:47', '1',
        '2022-05-16 20:25:15', false, 1),
       (106, '财务部门', 101, 4, 103, '15888888888', 'ry@qq.com', 0, 'admin', '2021-01-05 17:03:47', '103',
        '2022-01-15 21:32:22', false, 1),
       (107, '运维部门', 101, 5, null, '15888888888', 'ry@qq.com', 0, 'admin', '2021-01-05 17:03:47', '',
        '2021-12-15 05:01:33', false, 1),
       (108, '市场部门', 102, 1, null, '15888888888', 'ry@qq.com', 0, 'admin', '2021-01-05 17:03:47', '1',
        '2022-02-16 08:35:45', false, 1),
       (109, '财务部门', 102, 2, null, '15888888888', 'ry@qq.com', 0, 'admin', '2021-01-05 17:03:47', '',
        '2021-12-15 05:01:29', false, 1),
       (110, '新部门', 0, 1, null, null, null, 0, '110', '2022-02-23 20:46:30', '110', '2022-02-23 20:46:30', false,
        121),
       (111, '顶级部门', 0, 1, null, null, null, 0, '113', '2022-03-07 21:44:50', '113', '2022-03-07 21:44:50', false,
        122);