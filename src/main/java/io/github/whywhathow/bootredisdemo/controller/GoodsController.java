package io.github.whywhathow.bootredisdemo.controller;

import io.github.whywhathow.bootredisdemo.service.GoodsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.locks.ReentrantLock;

/**
 * @program: boot-redis-demo
 * @description: 单机情况下, 通过添加java 自带的锁可以实现,但是 在分布式系统环境下, 会出现重买,超卖的现象
 * @author: WhyWhatHow
 * @create: 2020-12-25 14:15
 **/
@RestController
@Slf4j
public class GoodsController {

    @Value("${server.port}")
    private String serverPort;

    // 模拟 购买 goods:001 的行为
    @Autowired
    GoodsService goodsService;
    /**
     * @Description: 单机, 无锁情况,在高并发情况下 出现超卖
     * @return: java.lang.String
     * @Author: WhyWhatHow
     * @Date: 2020/12/25 15:07
     **/
    @GetMapping("/buy_goods")
    public String buy_Goods() {
        return goodsService.buy(serverPort);
    }

    Object syncLock = new Object();

    /**
     * @Description:
     *  单机, synchronized 锁情况下, 未出现超卖情况
     *  集群情况下: 失败 出现超卖现象(原因  nginx->tomcat1111(自己的锁),tomcat2222(自己的锁))
     *  solve: 锁定一个公共变量充当锁, 然后让两个server 去竞争这把锁,已避免超卖情况.
     *
     * @Param:
     * @return:
     * @Author: WhyWhatHow
     * @Date: 2020/12/25 15:08
     **/
    @GetMapping("/sync/buy_goods")
    public String buy_GoodsBySynchronized() {
        synchronized (syncLock) {
            return goodsService.buy(serverPort);
        }
    }

    ReentrantLock lock = new ReentrantLock();

    @GetMapping("/re/buy_goods")
    public String buy_GoodsByReentrantLock() {
        if (lock.tryLock()) {
            lock.lock();
            try {
                String buy = goodsService.buy(serverPort);
                return buy;
            } finally {
                lock.unlock();
            }


        } else {
            return "当前人数过多,请稍后再试!!!!!! ";
        }

    }

}
