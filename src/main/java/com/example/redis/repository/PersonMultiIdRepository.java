package com.example.redis.repository;

import com.example.redis.domain.PersonMultiId;
import com.example.redis.id.MultiId;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.QueryByExampleExecutor;

/**
 * @author ZhuYX
 * @date 2021/04/16
 */
public interface PersonMultiIdRepository extends CrudRepository<PersonMultiId, MultiId>, QueryByExampleExecutor<PersonMultiId> {
}
