package com.example.redis.stream;

/**
 * @author ZhuYX
 * @date 2021/04/21
 */
public interface MessageListener<T> {

    int defaultBatchSize = 10;

    boolean doMessage(MessageObject<T> messageObject);

    MessageObject<T> sendMessage(T message) throws Exception;
}
