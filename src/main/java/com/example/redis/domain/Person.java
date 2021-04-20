package com.example.redis.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
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
@NoArgsConstructor
@AllArgsConstructor
// @RedisHash(value = "system:person")
@RedisHash(value = "system:no-id:person")
public class Person {
    @Id
    private Long id;

    private String msg;

    private Instant createInstant;

}
