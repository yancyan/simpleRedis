package com.example.redis.message;

import io.lettuce.core.RedisClient;
import io.lettuce.core.RedisURI;
import io.lettuce.core.StreamMessage;
import io.lettuce.core.XReadArgs;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.sync.RedisStreamCommands;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.Duration;
import java.util.List;

/**
 * @author ZhuYX
 * @date 2021/04/20
 */
public class SimpleConsumer {
    private static final Logger LOGGER = LogManager.getLogger(SimpleConsumer.class);

    @SuppressWarnings("unchecked")
    public static void main(String[] args) {
        RedisClient client = RedisClient.create(RedisURI.create("10.0.251.217", 6380));
        StatefulRedisConnection<String, String> connection = client.connect();
        RedisStreamCommands<String, String> commands = connection.sync();
        String lastSeenMessage = "0-0";
        while (true) {
            List<StreamMessage<String, String>> messages = commands.xread(XReadArgs.Builder.block(Duration.ofSeconds(1)),
                    XReadArgs.StreamOffset.from("my_stream", lastSeenMessage));
            for (StreamMessage<String, String> message : messages) {
                lastSeenMessage = message.getId();
                LOGGER.info(String.format("Received %s", message));
            }
        }
    }
}
