package com.example.redis.demo;

import com.google.common.hash.BloomFilter;
import com.google.common.hash.Funnels;
import org.junit.jupiter.api.Test;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * @author ZhuYX
 * @date 2021/04/16
 */
public class BloomFilterTest {

    @Test
    public void test() {
        BloomFilter<String> bloomFilter = BloomFilter.create(Funnels.stringFunnel(StandardCharsets.UTF_8),
                100_0000, 0.01);

        for (int i = 0; i < 10_000_000; i++) {
            bloomFilter.put("abcd" + i);
        }
        var abc = bloomFilter.mightContain("abc");
        System.out.println("######## " + abc);
        System.out.println("######## " +  bloomFilter.mightContain("abcd12"));
    }
}
