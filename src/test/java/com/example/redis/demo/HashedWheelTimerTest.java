package com.example.redis.demo;

import io.netty.util.HashedWheelTimer;
import org.junit.jupiter.api.Test;

import java.util.concurrent.TimeUnit;

/**
 * @author ZhuYX
 * @date 2021/04/19
 */
public class HashedWheelTimerTest {

    public static final HashedWheelTimer timer = new HashedWheelTimer();

    @Test
    public void test() {
        timer.newTimeout(timeout -> {
            System.out.println(timeout.toString());
        }, 10, TimeUnit.SECONDS);
        timer.start();
    }
}
