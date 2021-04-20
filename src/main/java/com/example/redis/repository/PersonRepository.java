package com.example.redis.repository;

import com.example.redis.domain.Person;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.QueryByExampleExecutor;

/**
 * @author ZhuYX
 * @date 2021/04/16
 */
public interface PersonRepository extends CrudRepository<Person, String>, QueryByExampleExecutor<Person> {
}
