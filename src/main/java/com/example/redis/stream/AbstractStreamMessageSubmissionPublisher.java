package com.example.redis.stream;

import com.example.redis.template.DefaultRedisTemplate;
import lombok.extern.slf4j.Slf4j;
import org.checkerframework.common.reflection.qual.GetClass;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.ResolvableType;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.stream.*;
import org.springframework.data.redis.core.StreamOperations;
import org.springframework.data.redis.hash.Jackson2HashMapper;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.stream.StreamListener;
import org.springframework.data.redis.stream.StreamMessageListenerContainer;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.time.Duration;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author ZhuYX
 * @date 2021/04/21
 */
// @Slf4j(topic = "")
public abstract class AbstractStreamMessageSubmissionPublisher<T>
        implements StreamListener<String, ObjectRecord<String, T>>, MessageListener<T>, InitializingBean, DisposableBean {

    public final Logger log = LoggerFactory.getLogger(getClass());

    //默认的线程池
    public static final Executor defaultMessageListenerExecutor = new ThreadPoolExecutor(1000, 1000,
            10L, TimeUnit.SECONDS, new LinkedBlockingQueue<>());

    //监听的容器
    private StreamMessageListenerContainer<String, ObjectRecord<String, T>> container = null;

    //泛型的类型
    private final Class<T> genericClass = (Class<T>) ResolvableType.forClass(getClass()).getSuperType().resolveGeneric(0);

    @Resource
    private RedisConnectionFactory redisConnectionFactory;
    private DefaultRedisTemplate<T> defaultRedisTemplate;


    public int getBatchSize() {
        return defaultBatchSize;
    }

    protected abstract String getConsumerName();

    protected abstract String getQueueName();

    public String getGroupName() {
        return getQueueName() + "_defaultGroup";
    }


    @Override
    public void onMessage(ObjectRecord<String, T> message) {
        try {
            RecordId recordId = messageSuccessRecordId(message);
            if (recordId != null) {
                //消息确认ack
                getOpsStream().acknowledge(getQueueName(), getGroupName(), recordId);
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    @Override
    public boolean doMessage(MessageObject<T> messageObject) {
        log.info("################ doMessage info : " + messageObject.toString());
        return true;
    }


    private RecordId messageSuccessRecordId(ObjectRecord<String, T> message) {
        RecordId recordId = message.getId();
        MessageObject<T> messageObject = objectRecord2MessageObject(message);
        try {
            boolean ok = doMessage(messageObject);
            if (ok) {
                return recordId;
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return null;
    }

    protected MessageObject<T> objectRecord2MessageObject(ObjectRecord<String, T> message) {
        return MessageObject.<T>builder()
                .messageTime(message.getId().getTimestamp())
                .messageId(message.getId().getValue())
                .messageContent(message.getValue())
                .queueName(message.getStream())
                .build();
    }


    @Override
    public void destroy() {
        if (container != null) {
            container.stop();
        }
    }

    @Override
    public void afterPropertiesSet() {
        try {
            if (defaultRedisTemplate == null) {
                defaultRedisTemplate = new DefaultRedisTemplate<>(redisConnectionFactory);
            }

            preCheck();
            var hashMapper = new Jackson2HashMapper(false);

            container = StreamMessageListenerContainer.create(redisConnectionFactory,
                    StreamMessageListenerContainer.StreamMessageListenerContainerOptions.builder()
                            .batchSize(getBatchSize()) //一批次拉取的最大count数
                            // .executor(defaultMessageListenerExecutor)  //线程池
                            .pollTimeout(Duration.ZERO) //阻塞式轮询
                            //设置默认的序列化器,要和 redisTemplate 保持一致!!!!!!!!!!!!!!!!!!!!!
                            //默认 targetType 会设置序列化器是  RedisSerializer.byteArray,这里手动初始化objectMapper,并设置自定义转换器和序列化器.
                            .objectMapper(hashMapper)
                            .keySerializer(RedisSerializer.string())
                            .hashKeySerializer(RedisSerializer.string())
                            .hashValueSerializer(defaultRedisTemplate.getHashValueSerializer())
                            //.serializer(RedisCacheConfig.fstSerializer)
                            .targetType(genericClass) //目标类型(消息内容的类型),如果objectMapper为空,会设置默认的ObjectHashMapper
                            .build());

            //检查创建group组
            prepareChannelAndGroup(getOpsStream(), getQueueName(), getGroupName());

            // 我们可以通过0、>、$分别表示第一条记录、最后一次未被消费的记录和最新一条记录,
            // 比如创建消费者组时不能使用>表示最后一次未被消费的记录,比如0表示从第一条开始并且包括第一条,
            // $表示从最新一条开始但并不是指当前Stream的最后一条记录,是表示下一个xadd添加的那一条记录,所以说$在非消费者组模式的阻塞读取下才有意义!

            // 需要手动回复应答 ACK
            container.receive(Consumer.from(getGroupName(), getConsumerName()),
                    StreamOffset.create(getQueueName(),
                            ReadOffset.lastConsumed()),
                    this);

            // container.receiveAutoAck(Consumer.from(getGroupName(), getConsumerName()),
            //         StreamOffset.create(getQueueName(),
            //                 ReadOffset.lastConsumed()),
            //         this);


            container.start();


            // //开启线程,重试异常的消息
            // executor.execute(() -> {
            //     //重试失败的消息
            //     try {
            //         retryFailMessage();
            //     } catch (Exception e) {
            //         log.error(e.getMessage(), e);
            //     }
            // });


        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw e;
        }
    }

    // public List<MessageObject<T>> retryFailMessage() throws Exception {
    //
    //     int batchSize = getBatchSize();
    //
    //     //消费者
    //     Consumer consumer = Consumer.from(getGroupName(), getConsumerName());
    //     //设置配置
    //     StreamReadOptions streamReadOptions = StreamReadOptions.empty().count(batchSize).block(Duration.ofSeconds(5));
    //     List<ObjectRecord<String, T>> retryFailMessageList = new ArrayList<>();
    //     //避免死循环,最多1000次.如果单次返回的所有消息都是异常的,退出循环
    //     for (int i = 0; i < 1000; i++) {
    //         List<ObjectRecord<String, T>> readList = redisTemplate.opsForStream().read(genericClass, consumer, streamReadOptions, StreamOffset.fromStart(getQueueName()));
    //         //如果已经没有异常的消息,退出循环
    //         if (CollectionUtils.isEmpty(readList)) {
    //             break;
    //         }
    //         //如果返回的消息全部都是异常的,退出循环
    //         if (retryFailMessageList.containsAll(readList)) {
    //             break;
    //         }
    //
    //         // 遍历异常的消息
    //         for (ObjectRecord<String, T> message : readList) {
    //             RecordId recordId = messageSuccessRecordId(message);
    //             //处理成功
    //             if (recordId != null) {
    //                 //消息确认ack
    //                 redisTemplate.opsForStream().acknowledge(getQueueName(), getGroupName(), recordId);
    //             } else {//处理失败,记录下来
    //                 retryFailMessageList.add(message);
    //             }
    //         }
    //     }
    //     // 没有失败的消息记录
    //     if (CollectionUtils.isEmpty(retryFailMessageList)) {
    //         return null;
    //     }
    //     //返回处理异常的消息
    //     List<MessageObjectDto<T>> retryFailMessageObjectList = new ArrayList<>();
    //     for (ObjectRecord<String, T> message : retryFailMessageList) {
    //         retryFailMessageObjectList.add(objectRecord2MessageObject(message));
    //     }
    //     return retryFailMessageObjectList;
    // }


    private void prepareChannelAndGroup(StreamOperations<String, ?, ?> ops, String queueName, String group) {
        String status = "OK";
        try {
            StreamInfo.XInfoGroups groups = ops.groups(queueName);
            if (groups.stream().noneMatch(xInfoGroup -> group.equals(xInfoGroup.groupName()))) {
                status = ops.createGroup(queueName, ReadOffset.from("0-0"), group);
            }
        } catch (Exception exception) {
            RecordId initialRecord = ops.add(ObjectRecord.create(queueName, "Initial Record"));
            Assert.notNull(initialRecord, "Cannot initialize stream with key '" + queueName + "'");
            status = ops.createGroup(queueName, ReadOffset.from(initialRecord), group);
        } finally {
            Assert.isTrue("OK".equals(status), "Cannot create group with name '" + group + "'");
        }
    }

    private void preCheck() {
        String className = getClass().toString();
        if (!StringUtils.hasText(getQueueName())) {
            log.error(className + "getQueueName() is null, registerConsumerListener() fail.");
        }
        if (!StringUtils.hasText(getGroupName())) {
            log.error(className + "getGroupName() is null, registerConsumerListener() fail.");
        }
        if (!StringUtils.hasText(getConsumerName())) {
            log.error(className + "getConsumerName() is null, registerConsumerListener() fail.");
        }
    }


    @Override
    public MessageObject<T> sendMessage(T message) {
        if (message == null) {
            return null;
        }
        try {


            ObjectRecord<String, T> record = Record.of(message).withStreamKey(getQueueName());
            var recordId = getOpsStream().add(record);
            return MessageObject.<T>builder()
                    .messageContent(message)
                    .queueName(getQueueName())
                    // .messageTime(recordId.getTimestamp())
                    .build();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw e;
        }
    }


    private StreamOperations<String, String, T> getOpsStream() {
        return defaultRedisTemplate.opsForStream(new Jackson2HashMapper(false));
    }
}
