package com.example.redis.stream;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author ZhuYX
 * @date 2021/04/21
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MessageObject<T> {

    private String messageId;
    private Long messageTime;
    private String queueName;
    private T messageContent;

}
