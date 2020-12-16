package com.seckill.service.impl;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.seckill.dao.GoodsMapper;
import com.seckill.dao.SeckillGoodsMapper;
import com.seckill.entity.Goods;
import com.seckill.entity.Order;
import com.seckill.entity.SeckillGoods;
import com.seckill.service.SeckillService;
import org.redisson.api.RSemaphore;
import org.redisson.api.RedissonClient;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.lang.model.element.VariableElement;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @author 今何许
 * @date 2020-12-16 16:59
 */
@Service
public class SeckillServiceImpl implements SeckillService {
    @Autowired
    private SeckillGoodsMapper seckillGoodsMapper;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private GoodsMapper goodsMapper;

    @Resource
    private RedissonClient redissonClient;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    private final String SESSION__CACHE_PREFIX = "seckill:sessions:";

    private final String SECKILL_CHARE_PREFIX = "seckill:goods";


    @Override
    public void kill(Integer goodId, int num) throws InterruptedException {
        SeckillGoods seckillGoods = seckillGoodsMapper.selectById(goodId);
        Long startTime = seckillGoods.getStartDate().getTime();
        Long endTime = seckillGoods.getEndDate().getTime();
        Long currentTime = System.currentTimeMillis();
        if (currentTime >= startTime && currentTime <= endTime) {
            System.out.println("......时间合格");
            Goods good = goodsMapper.selectById(goodId);
            Integer stock = good.getStock();
            String seckillCount = redisTemplate.opsForValue().get(SECKILL_CHARE_PREFIX + goodId);
            Integer count = Integer.valueOf(seckillCount);
            if (count > 0 && num <= stock && count > num) {
                System.out.println(".....库存充足");
                RSemaphore semaphore = redissonClient.getSemaphore(SECKILL_CHARE_PREFIX + goodId);
                //TODO 秒杀成功，快速下单
                boolean semaphoreCount = semaphore.tryAcquire(num, 100, TimeUnit.MILLISECONDS);
                //保证Redis中还有商品库存
                if (semaphoreCount) {
                    int s = good.getStock() - num;
                    good.setStock(s);
                    goodsMapper.updateById(good);
                    //创建订单号和订单信息发送给MQ
                    // 秒杀成功 快速下单 发送消息到 MQ 整个操作时间在 10ms 左右
                    Order order = new Order();
                    order.setGoodsId(goodId);
                    order.setGoodsCount(num);
                    order.setStatus(1);
                    order.setGoodsName(good.getName());
                    BigDecimal pay = new BigDecimal(String.valueOf(num)).multiply(good.getPrice());
                    order.setGoodsPrice(pay);
                    order.setCreateDate(new Date());
                    System.out.println("order...." + order);
                    rabbitTemplate.convertAndSend("order-event-exchange", "order.seckill.order", order);
                }
            }

        }
    }

    @Override
    public void uploadSeckillGoods(List<SeckillGoods> goodsList) {
        //缓存到Redis
        //1、缓存活动信息
        saveSessionInfos(goodsList);

        //2、缓存活动的关联商品信息
        saveSessionGoodsInfo(goodsList);
    }

    private void saveSessionGoodsInfo(List<SeckillGoods> goodsList) {
        goodsList.stream().forEach(good -> {
            //获取当前活动的开始和结束时间的时间戳
            long startTime = good.getStartDate().getTime();
            long endTime = good.getEndDate().getTime();

            //存入到Redis中的key
            String key = SESSION__CACHE_PREFIX + startTime + "_" + endTime;

            //判断Redis中是否有该信息，如果没有才进行添加
            Boolean hasKey = redisTemplate.hasKey(key);
            //缓存活动信息
            if (!hasKey) {
                List<String> ids = goodsList.stream().map(g ->
                        g.getGoodsId().toString()
                ).collect(Collectors.toList());
                redisTemplate.opsForList().leftPushAll(key, ids);
            }
        });
    }

    private void saveSessionInfos(List<SeckillGoods> goodsList) {
        goodsList.stream().forEach(good -> {
            RSemaphore semaphore = redissonClient.getSemaphore(SECKILL_CHARE_PREFIX + good.getGoodsId());
            // 商品可以秒杀的数量作为信号量
            semaphore.trySetPermits(good.getStockCount());
        });
    }
}
