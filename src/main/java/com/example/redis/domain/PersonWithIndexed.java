package com.example.redis.domain;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

import java.time.Instant;

/**
 * @author ZhuYX
 * @date 2021/04/16
 */
@Data
@Builder
@RedisHash(value = "system:person-with-index")
public class PersonWithIndexed {

    @Indexed
    @Id
    private Long id;

    @Indexed
    private String msg;

    private Instant createInstant;

}
