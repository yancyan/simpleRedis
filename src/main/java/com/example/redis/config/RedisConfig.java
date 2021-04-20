package com.example.redis.config;

import com.example.redis.template.DefaultRedisTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnSingleCandidate;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.support.GenericConversionService;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.convert.MappingRedisConverter;
import org.springframework.data.redis.serializer.RedisSerializer;

import javax.annotation.PostConstruct;

/**
 * @author ZhuYX
 * @date 2021/04/16
 */
@AutoConfigureAfter(RedisAutoConfiguration.class)
@Configuration(proxyBeanMethods = false)
public class RedisConfig {

    @Autowired
    private MappingRedisConverter mappingRedisConverter;

    @PostConstruct
    public void initialConverter() {
        ConversionService conversionService = mappingRedisConverter.getConversionService();
        if (conversionService instanceof GenericConversionService) {
            registryKeyConverter((GenericConversionService) conversionService);
        }
    }
    private void registryKeyConverter(GenericConversionService conversionService) {

    }

    @Bean
    // @ConditionalOnMissingBean
    @ConditionalOnSingleCandidate(RedisConnectionFactory.class)
    public DefaultRedisTemplate defaultRedisTemplate(RedisConnectionFactory redisConnectionFactory) {
        return new DefaultRedisTemplate(redisConnectionFactory);
    }

}
