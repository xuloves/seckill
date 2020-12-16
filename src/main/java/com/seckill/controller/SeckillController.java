package com.seckill.controller;

import com.seckill.dao.OrderMapper;
import com.seckill.dao.SeckillGoodsMapper;
import com.seckill.entity.Goods;
import com.seckill.entity.Order;
import com.seckill.entity.SeckillGoods;
import com.seckill.service.SeckillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author 今何许
 * @date 2020-12-16 16:43
 */
@RestController
public class SeckillController {
    @Resource
    private SeckillService seckillService;
    @Autowired
    private SeckillGoodsMapper seckillGoodsMapper;

    @Autowired
    private OrderMapper orderMapper;

    @GetMapping("/kill")
    public String kill(int goodId, int num) throws InterruptedException {
        System.out.println("id: " + goodId + "  数量：" + num);
        seckillService.kill(goodId, num);
        return "success";
    }

    @GetMapping("/upload")
    public String upload() {
        SeckillGoods seckillGoods = seckillGoodsMapper.selectById(1);
        List<SeckillGoods> goodsList = new ArrayList<>();
        goodsList.add(seckillGoods);
        seckillService.uploadSeckillGoods(goodsList);
        return "上架。。。。";
    }

}
