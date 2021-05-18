package io.github.whywhathow.bootredisdemo;

import java.util.HashMap;

/**
 * @program: boot-redis-demo
 * @description:
 * @author: WhyWhatHow
 * @create: 2021-04-29 16:25
 **/
public class HashMapTest {
    public static void main(String[] args) {

        HashMap map=new HashMap(5);
        map.put("anb", 1);
        System.out.println(map);
    }
}
