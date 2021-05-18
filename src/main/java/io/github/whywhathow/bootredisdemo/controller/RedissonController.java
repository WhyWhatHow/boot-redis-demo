package io.github.whywhathow.bootredisdemo.controller;

import io.github.whywhathow.bootredisdemo.service.GoodsService;
import lombok.extern.slf4j.Slf4j;
import org.redisson.Redisson;
import org.redisson.api.RLock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;


import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * @program: boot-redis-demo
 * @description: 实现 集群情况下, redis 分布式锁 续期 行为
 * @author: WhyWhatHow
 * @create: 2021-01-05 10:56
 **/
@RestController
@Slf4j
@RequestMapping("/redisson")
public class RedissonController {
    @Autowired
    Redisson redisson;

    @Autowired
    GoodsService service;
    // redisson 锁
    String REDLOCK = "REDLOCK";

    @Value("${server.port}")
    String serverPort;

    /**
     * 单个redis 情况下 使用redisson 完成 分布式锁
     * 使用redisson 原因:  使用lua 脚本(在redis中属于 原子操作), 避免了加锁, 解锁时出现异常现象
     *
     * @return
     */
    @GetMapping("/buy_goods/{id}")
    public String buyGoods(@PathVariable("id") String id) {
        String result = "";
        //1 . 获取锁
        RLock lock = redisson.getLock(REDLOCK);
        String val = UUID.randomUUID().toString() + Thread.currentThread().getName();
        try {
            //2 . 加锁
            lock.lock(30, TimeUnit.SECONDS);
            // 3. 购买商品
            service.buy(serverPort, id);

        } catch (Exception e) {
        } finally {
            //  4. 解锁
            if (lock.isLocked() && lock.isHeldByCurrentThread()) {
                lock.unlock();
            }
        }
        return result;
    }
}
