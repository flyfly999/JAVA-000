package com.test.demo.lock;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

@Service
@Slf4j
public class RedisConter {
    @Autowired
    private JedisPool jedisPool;

    public long reduceStock(String sKey) {
        Jedis jedis = jedisPool.getResource();
        long num = jedis.decr(sKey);
        if (num >= 0) {
            return num;
        }else {
            return -1L;
        }
    }

}
