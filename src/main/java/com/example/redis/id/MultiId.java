package com.example.redis.id;

import lombok.Builder;
import lombok.Data;

/**
 * @author ZhuYX
 * @date 2021/04/16
 */
@Data
@Builder
public class MultiId {
    private Long id_1;
    private Long id_2;
}
