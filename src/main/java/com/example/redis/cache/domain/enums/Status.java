package com.example.redis.cache.domain.enums;

/**
 * @author ZhuYX
 * @date 2021/04/16
 */
public enum Status {
    INVALID,
    VALID;
    public static Status valueOf(int ordinal) {
        Status[] values = Status.values();
        if (ordinal >= values.length) {
            return null;
        } else {
            return values[ordinal];
        }
    }
}
