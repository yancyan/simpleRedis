package com.example.redis.demo;

import com.example.redis.RedisDemoApplication;
import com.example.redis.domain.Person;
import com.example.redis.domain.PersonWithIndexed;
import com.example.redis.repository.PersonMultiIdRepository;
import com.example.redis.repository.PersonRepository;
import com.example.redis.repository.PersonWithIndexedRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


@SpringBootTest(classes = RedisDemoApplication.class)
class RedisDemoApplicationTests {

    @Autowired
    private PersonRepository personRepository;
    @Autowired
    private PersonWithIndexedRepository personWithIndexedRepository;
    @Autowired
    private PersonMultiIdRepository personMultiIdRepository;

    @Test
    void redis_test_person() {
        personRepository.save(Person.builder().id(15L).msg("test_key_1").build());
    }

    @Test
    void redis_test_person_with_index() {
        personWithIndexedRepository.save(PersonWithIndexed.builder().id(15L).msg("test_id_1").build());
    }


}
