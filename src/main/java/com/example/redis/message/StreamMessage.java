package com.example.redis.message;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author ZhuYX
 * @date 2021/04/20
 */
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class StreamMessage {

    private Long id;
    private String name;
    private String address;

}
