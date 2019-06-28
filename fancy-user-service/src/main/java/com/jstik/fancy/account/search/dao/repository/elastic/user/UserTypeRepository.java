package com.jstik.fancy.account.search.dao.repository.elastic.user;

import com.jstik.fancy.account.search.entity.elastic.UserType;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface UserTypeRepository extends ElasticsearchRepository<UserType, String> {
}
