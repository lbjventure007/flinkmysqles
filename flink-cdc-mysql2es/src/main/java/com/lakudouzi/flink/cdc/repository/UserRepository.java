package com.lakudouzi.flink.cdc.repository;

import com.lakudouzi.flink.cdc.domain.User;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface UserRepository extends ElasticsearchRepository<User,Long> {
}
