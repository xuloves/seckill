package com.seckill.service;

import com.seckill.entity.SeckillGoods;

import java.util.List;

/**
 * @author 今何许
 * @date 2020-12-16 16:44
 */
public interface SeckillService {
    void kill(Integer goodId, int num) throws InterruptedException;

    void uploadSeckillGoods(List<SeckillGoods> goodsList);
}
