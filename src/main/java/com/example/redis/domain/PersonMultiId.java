package com.example.redis.domain;

import com.example.redis.id.MultiId;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import java.time.Instant;

/**
 * @author ZhuYX
 * @date 2021/04/16
 */
@Data
@Builder
// @RedisHash(value = "system:person")
@RedisHash(value = "system:multi-id:person")
public class PersonMultiId {
    @Id
    private MultiId multiId;

    private String msg;

    private Instant createInstant;

}
