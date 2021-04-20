package com.example.redis.cache.domain.system;

import com.example.redis.cache.domain.enums.Status;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

import java.io.Serializable;
import java.time.Instant;

/**
 * @author ZhuYX
 * @date 2021/04/16
 */
@RedisHash(value = "system:company")
public class Company implements Serializable {
    private static final long serialVersionUID = -1341405106067396303L;

    @Id private Long id;
    @Indexed private Long parentId;
    @Indexed private Long countryId;
    private String name;
    private String code;
    private Status status;
    private Long dssCompanyKey;
    private Double rmbRate;
    private String mem;
    private Long tempEmployeesNumber;
    private Double exchangeRate;
    private Double taxRate;

    private int version;
    private Long createId;
    private Instant createInstant;
    private Long modifyId;
    private Instant modifyInstant;
    private String transactionId;
    private String serverName;
}
