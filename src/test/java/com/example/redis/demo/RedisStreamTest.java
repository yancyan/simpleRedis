// package com.example.redis.demo;
//
// import com.example.redis.RedisDemoApplication;
// import com.example.redis.domain.Person;
// import com.example.redis.message.StreamMessage;
// import com.example.redis.template.DefaultRedisTemplate;
// import com.fasterxml.jackson.core.JsonProcessingException;
// import com.fasterxml.jackson.databind.ObjectMapper;
// import org.junit.jupiter.api.Test;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.boot.test.context.SpringBootTest;
// import org.springframework.data.redis.connection.stream.*;
// import org.springframework.data.redis.core.StreamOperations;
// import org.springframework.data.redis.stream.StreamListener;
// import org.springframework.data.redis.stream.StreamMessageListenerContainer;
// import org.springframework.data.redis.stream.Subscription;
// import org.springframework.util.CollectionUtils;
//
// import java.net.InetAddress;
// import java.time.Duration;
// import java.util.List;
// import java.util.Map;
// import java.util.Objects;
// import java.util.stream.Collectors;
//
//
// @SpringBootTest(classes = RedisDemoApplication.class)
// public class RedisStreamTest {
//     public static final String key = "redis-stream";
//     public static final String group_prefix = "redis-stream-group-";
//     public static final String group = group_prefix + "One";
//
//
//     @Autowired
//     private DefaultRedisTemplate defaultRedisTemplate;
//
//
//     @Test
//     void redis_send() {
//         var opsForStream = defaultRedisTemplate.opsForStream();
//
//         for (int i = 0; i < 10; i++) {
//
//             var ofMap =
//                     StreamRecords.newRecord().in(key).ofObject(StreamMessage.builder().id(12L + i).name("name " + i).build());
//             opsForStream.add(ofMap);
//         }
//     }
//
//     @SuppressWarnings("unchecked")
//     @Test
//     void redis_receive_sync() {
//         var opsForStream = defaultRedisTemplate.opsForStream();
//
//         var groups = opsForStream.groups(key);
//         groups.forEach(g -> System.out.println(g.toString()));
//         System.out.println("############ ");
//         if (groups.stream().noneMatch(s -> group.equals(s.groupName()))) {
//             opsForStream.createGroup(key, group);
//         }
//
//         var consumers = opsForStream.consumers(key, group);
//         System.out.println(consumers);
//
//         var mapRecords = opsForStream.read(StreamMessage.class, Consumer.from(group, key),
//                 StreamReadOptions.empty().autoAcknowledge()
//                         // .block(Duration.ofSeconds(5))
//                         .count(1),
//                 StreamOffset.create(key, ReadOffset.lastConsumed()));
//
//
//         System.out.println(mapRecords);
//         if (!CollectionUtils.isEmpty(mapRecords)) {
//             opsForStream.acknowledge(key, group, mapRecords.stream().map(ObjectRecord::getId).toArray(RecordId[]::new));
//         }
//     }
//
//     @Test
//     void redis_consumer() {
//         var opsForStream = defaultRedisTemplate.opsForStream();
//         var infoConsumers = opsForStream.consumers(key, group);
//         System.out.println(infoConsumers);
//     }
//
//     @Test
//     public void consumer() {
//         var opsForStream = defaultRedisTemplate.opsForStream();
//
//         var records = opsForStream.read(Consumer.from(group, "printConsumer"),
//                 StreamReadOptions.empty().count(20),
//                 StreamOffset.create(key, ReadOffset.lastConsumed()));
//
//         System.out.println("########## " + records);
//
//         opsForStream.acknowledge(key, group, records.stream().map(x -> x.getId().toString()).toArray(String[]::new));
//
//     }
//
//     @Test
//     void redis_test_() {
//         var opsForStream = defaultRedisTemplate.opsForStream();
//         // opsForStream.createGroup(key, groupOne);
//         for (int i = 0; i < 10; i++) {
//
//             var ofMap =
//                     StreamRecords.newRecord().in(key).ofMap(Map.of("customerId", i
//                             , "customerName", "lihua " + i));
//             opsForStream.add(ofMap);
//         }
//
//
//         var listenerContainer = StreamMessageListenerContainer
//                 .create(Objects.requireNonNull(defaultRedisTemplate.getConnectionFactory()), StreamMessageListenerContainer
//                         .StreamMessageListenerContainerOptions
//                         .builder()
//                         .pollTimeout(Duration.ofSeconds(1))
//                         .targetType(Map.class)
//                         .build());
//         var autoAck = listenerContainer.receiveAutoAck(
//                 Consumer.from(group, key),
//                 StreamOffset.create(key, ReadOffset.lastConsumed()),
//                 (StreamListener<String, ObjectRecord<String, Map>>) listenerContainer);
//         listenerContainer.start();
//
//     }
//
// }
