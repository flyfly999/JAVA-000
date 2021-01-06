package com.test.demo.lock;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.params.SetParams;

import java.util.Collections;

@Service
@Slf4j
public class RedisLock {
    private String LOCK_KEY = "redis_lock_key";

    protected static long internalLockLeaseTime = 30000;//锁过期时间

    private static long timeout = 1000000; //获取锁的超时时间

    @Autowired
    private JedisPool jedisPool;

    private static SetParams params = SetParams.setParams().nx().px(internalLockLeaseTime);


    public boolean lock(String id) {
        Jedis jedis = jedisPool.getResource();
        Long start = System.currentTimeMillis();

        try {
            for (; ; ) {
                String lock = jedis.set(LOCK_KEY, id, params);
                if ("OK".equals(lock)) {
                    return true;
                }

                long l = System.currentTimeMillis() - start;
                if (l >= timeout) {
                    return false;
                }
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    log.error("分布式锁异常", e);
                }
            }
        } finally {
            jedis.close();
        }
    }

    public boolean unlock(String id) {
        Jedis jedis = jedisPool.getResource();
        String script =
                "if redis.call('get',KEYS[1]) == ARGV[1] then" +
                        "   return redis.call('del',KEYS[1]) " +
                        "else" +
                        "   return 0 " +
                        "end";
        try {
            Object result = jedis.eval(script, Collections.singletonList(LOCK_KEY),
                    Collections.singletonList(id));
            if ("1".equals(result.toString())) {
                return true;
            }
            return false;
        } finally {
            jedis.close();
        }
    }

}
