package io.github.whywhathow.bootredisdemo.controller;

import io.github.whywhathow.bootredisdemo.service.GoodsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

/**
 * @program: boot-redis-demo
 * @description: 处理分布式系统下的情况.... ,未实现 分布式锁续期(刷新锁的expire时间)
 * @author: WhyWhatHow
 * @create: 2020-12-25 14:15
 **/
@RestController
@Slf4j
@RequestMapping("/redis/setnx/")
public class RedisSetNxGoodsController {
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Value("${server.port}")
    private String serverPort;


    /**
     * @Description: 微服务集群 , redis 单机情况满足, 若是redis集群存在问题... redis-setnx
     * @return: java.lang.String
     * @Author: WhyWhatHow
     * @Date: 2020/12/25 15:07
     **/
    @GetMapping("/buy_goods/{id}")
    public String buy_GoodsPart1(@PathVariable("id") String id) {
        String val = UUID.randomUUID().toString() + Thread.currentThread().getName();
        String result = "";
        try {
            // 1. 尝试获取锁,redis_setnx( key   , value , ttl )
            // 设置超时时间, 避免因为 当前服务进程出错,导致无法删除锁对象
            Boolean aBoolean = stringRedisTemplate.opsForValue().setIfAbsent(REDIS_SETNX_LOCK, val, Duration.ofSeconds(10));
            if (aBoolean) {
                // 2. 获取锁成功, shopping ,失败则返回提示信息
                result = setnxBy(id);
            } else {
                result = " 获取锁失败...页面繁忙, 请稍后重试. serverPort: " + serverPort;
            }
        } catch (Exception e) {
            log.error("获取锁过程出现异常: exception: "+e.getMessage());

            // // Question: 2021/1/3 异常出错, 需要释放锁 ,
            //  解决方案: finally 释放锁

        } finally {
            // 3.  主动释放锁, 不主动释放锁的话 ,会在超时时间结束后释放锁
            // 3.1 判断锁 是否被当前线程所占有, 避免 删除其他线程的锁情况
            String s = stringRedisTemplate.opsForValue().get(REDIS_SETNX_LOCK);
            //
            if (val.equals(s)) {
                //3.2  删除当前线程所占有的锁
                // 3.2.1  非原子性 删除
//                stringRedisTemplate.delete(REDIS_SETNX_LOCK);
                // 3.2.2 原子性删除 ... lua
                unLockByLua(val, REDIS_SETNX_LOCK);
                // 3.2.3 redis 事务删除
//                redisTransactionDel(val);

            }

        }

        return result;
    }

    private void redisTransactionDel(String val) {
        while (true){
            if (val.equals(stringRedisTemplate.opsForValue().get(REDIS_SETNX_LOCK))) {
                stringRedisTemplate.watch(REDIS_SETNX_LOCK);
                stringRedisTemplate.setEnableTransactionSupport(true);
                stringRedisTemplate.multi();
                stringRedisTemplate.delete(REDIS_SETNX_LOCK);
                List<Object> exec = stringRedisTemplate.exec();
                if (exec.isEmpty()) {
                    continue;
                }
                stringRedisTemplate.unwatch();
                break;
            }
        }
    }

    /**
     * 原子操作,  redis eval luascript
     * 解锁
     *  // 官网: https://redis.io/topics/distlock
     * @param val
     * @param redisLock
     */
    private void unLockByLua(String val, String redisLock) {
        String luaScript = "if redis.call(\"get\",KEYS[1]) == ARGV[1]\n" +
                "then\n" +
                "    return redis.call(\"del\",KEYS[1])\n" +
                "else\n" +
                "    return 0\n" +
                "end";
        DefaultRedisScript<Long> objectDefaultRedisScript = new DefaultRedisScript<>(luaScript, Long.class);
        stringRedisTemplate.execute(objectDefaultRedisScript, Collections.singletonList(redisLock), val);
    }


    /***
     * 利用 redis 自带命令setnx(if key not exist ,then set val )
     * 模拟 购买某一固定商品(goods:001)的过程
     * @return
     */
    String REDIS_SETNX_LOCK = "redisSetnxLock";

    @Autowired
    GoodsService service;
    private String setnxBy(String id ) {
        return  service.buy(serverPort,id);
    }


}
