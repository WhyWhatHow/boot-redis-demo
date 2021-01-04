package io.github.whywhathow.bootredisdemo.config.controller;

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
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Value("${server.port}")
    private String serverPort;


    /**
     * @Description: 单机, 无锁情况,在高并发情况下 出现超卖
     * @return: java.lang.String
     * @Author: WhyWhatHow
     * @Date: 2020/12/25 15:07
     **/
    @GetMapping("/buy_goods")
    public String buy_Goods() {
        return buy();
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
            return buy();
        }
    }

    ReentrantLock lock = new ReentrantLock();

    @GetMapping("/re/buy_goods")
    public String buy_GoodsByReentrantLock() {
        if (lock.tryLock()) {
            lock.lock();
            try {
                String buy = buy();
                return buy;
            } finally {
                lock.unlock();
            }


        } else {
            return "当前人数过多,请稍后再试!!!!!! ";
        }

    }

    /***
     * 模拟 购买某一固定商品(goods:001)的过程
     * @return
     */
    private String buy() {

        String result = stringRedisTemplate.opsForValue().get("goods:001");
        int goodsNumber = result == null ? 0 : Integer.parseInt(result);

        if (goodsNumber > 0) {
            int realNumber = goodsNumber - 1;
            stringRedisTemplate.opsForValue().set("goods:001", realNumber + "");
            System.out.println("你已经成功秒杀商品，此时还剩余：" + realNumber + "件" + "\t 服务器端口: " + serverPort);
            return "你已经成功秒杀商品，此时还剩余：" + realNumber + "件" + "\t 服务器端口: " + serverPort;
        } else {
            System.out.println("商品已经售罄/活动结束/调用超时，欢迎下次光临" + "\t 服务器端口: " + serverPort);
        }
        return "商品已经售罄/活动结束/调用超时，欢迎下次光临" + "\t 服务器端口: " + serverPort;
    }


}
