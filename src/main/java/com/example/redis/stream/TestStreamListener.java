package com.example.redis.stream;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.stream.Record;
import org.springframework.stereotype.Component;

/**
 * @author ZhuYX
 * @date 2021/04/21
 */
@Slf4j
@Component
public class TestStreamListener extends AbstractStreamMessageSubmissionPublisher<Person>{

    public static final String queueName = "test_stream";


    @Override
    public String getConsumerName() {
        return getQueueName() + "_Group";
    }

    @Override
    public String getQueueName() {
        return queueName;
    }

    @Override
    public boolean doMessage(MessageObject<Person> messageObject) {
        return super.doMessage(messageObject);
    }
}
