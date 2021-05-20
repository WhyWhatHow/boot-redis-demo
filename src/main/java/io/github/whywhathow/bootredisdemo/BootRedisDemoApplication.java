package io.github.whywhathow.bootredisdemo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude ={DataSourceAutoConfiguration.class})
public class BootRedisDemoApplication {
    /**
     * 项目详细说明:
     *     文档地址:https://www.whywhathow.fun/projects/projects-01-redis-distributedLock/ 大概还有五个案例:
     * 分别是:
     *      1.单机不加锁: http://localhost:1111/buy_goods/{goodsId}
     *      2. 单机加锁:  http://localhost:1111/sync/buy_goods/{id}
     *      3. 分布式加锁--事务,lua 原子操作模式:  http://localhost:1111/redis/setnx/buy_goods/{id}
     *      4. 分布式加锁-- redisson 分布式锁: http://localhost:1111/redisson/buy_goods/{id}
     *
     * PS: 建议项目用idea 工具打开, 并安装 RestService  插件(直接在插件市场中搜索就好) ,
     *     并参考文档地址
     *
     * @param args
     */
    public static void main(String[] args) {
        SpringApplication.run(BootRedisDemoApplication.class, args);
    }

}
