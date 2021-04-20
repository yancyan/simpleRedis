package com.example.redis.cache.config;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author ZhuYX
 * @date 2021/04/16
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SystemConfig {
    private String   moduleName ;
    private String   dbUserName ;

}
