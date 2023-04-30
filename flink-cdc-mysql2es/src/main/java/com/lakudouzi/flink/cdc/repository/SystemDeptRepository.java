package com.lakudouzi.flink.cdc.repository;

import com.lakudouzi.flink.cdc.domain.SystemDept;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface SystemDeptRepository extends ElasticsearchRepository<SystemDept,Long> {
}
