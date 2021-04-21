package com.example.redis;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

@Slf4j
@EnableScheduling
@EnableRedisRepositories
@SpringBootApplication
public class RedisDemoApplication {

    public static final String key = "redis-stream-2";
    public static final String keyGroup = key + "group";

    public static void main(String[] args) {
        SpringApplication.run(RedisDemoApplication.class, args);
    }

    // @Bean
    // public StreamMessageListenerContainer<String, ObjectRecord<String, StreamMessage>> streamMessageListenerContainer(DefaultRedisTemplate defaultRedisTemplate){
    //
    //
    //     return StreamMessageListenerContainer.create(Objects.requireNonNull(defaultRedisTemplate.getConnectionFactory()),
    //             StreamMessageListenerContainer.StreamMessageListenerContainerOptions
    //                     .builder()
    //                     .pollTimeout(Duration.ofSeconds(1))
    //                     .objectMapper(new Jackson2HashMapper(false))
    //                     .targetType(StreamMessage.class)
    //                     .build());
    //
    // }
    //
    // @Bean
    // public Subscription subscription(StreamMessageListenerContainer<String, ObjectRecord<String,StreamMessage>> container,
    //                                  DefaultRedisTemplate defaultRedisTemplate,
    //                                  PrintConsumer pc){
    //     checkGroup(defaultRedisTemplate, key, keyGroup);
    //
    //     Subscription subscription = container.receiveAutoAck(Consumer.from(keyGroup, key),
    //             StreamOffset.create(key, ReadOffset.lastConsumed()), pc);
    //     container.start();
    //     return subscription;
    // }
    //
    // /**
    //  * 由于订阅需要先有stream，先做下检查
    //  */
    // private void checkGroup(DefaultRedisTemplate defaultRedisTemplate, String streamKey, String groupName){
    //     var opsForStream = defaultRedisTemplate.opsForStream(new Jackson2HashMapper(false));
    //
    //     try {
    //         if (opsForStream.groups(streamKey).stream().noneMatch(g -> groupName.equals(g.groupName()))) {
    //             opsForStream.createGroup(streamKey, groupName);
    //         }
    //         opsForStream.add(StreamRecords.objectBacked(StreamMessage.builder()
    //                 .id(12L )
    //                 .name("name")
    //                 .build()).withStreamKey(streamKey));
    //     } catch (Exception ex) {
    //         log.error("group key not exist", ex);
    //     }
    // }

}
