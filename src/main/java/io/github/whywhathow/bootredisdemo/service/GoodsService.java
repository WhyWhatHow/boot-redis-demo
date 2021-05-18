package io.github.whywhathow.bootredisdemo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Arrays;

/**
 * @program: boot-redis-demo
 * @description: 实际购买的 业务
 * @author: WhyWhatHow
 * @create: 2021-01-05 12:52
 **/
@Service
public class GoodsService {
    @Autowired
    StringRedisTemplate redisTemplate;

    /***
     * 模拟 购买某一固定商品(goods:001)的过程
     * @return 返回购买结果
     */

    public String buy(String serverPort, String goodsID) {
        String goods = goodsID;
        String result = redisTemplate.opsForValue().get(goods);
        int goodsNumber = result == null ? 0 : Integer.parseInt(result);

        if (goodsNumber > 0) {
            int realNumber = goodsNumber - 1;
            redisTemplate.opsForValue().set(goods, realNumber + "");
            System.out.println("你: " + Thread.currentThread().getName() + "已经成功秒杀商品，此时还剩余：" + realNumber + "件" + "\t 服务器端口: " + serverPort);
            return "你: " + Thread.currentThread().getName() + "已经成功秒杀商品，此时还剩余：" + realNumber + "件" + "\t 服务器端口: " + serverPort;
        }
        return Thread.currentThread().getName() + " :商品已经售罄/活动结束/调用超时，欢迎下次光临" + "\t 服务器端口: " + serverPort;
    }
}
