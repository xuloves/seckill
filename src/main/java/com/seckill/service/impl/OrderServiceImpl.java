package com.seckill.service.impl;

import com.seckill.dao.OrderMapper;
import com.seckill.entity.Order;
import com.seckill.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * @author 今何许
 * @date 2020-12-16 17:56
 */
@Service
public class OrderServiceImpl implements OrderService {
    @Autowired
    private OrderMapper orderMapper;

    @Override
    public void createSeckillOrder(Order order) {
        order.setPayDate(new Date());
        order.setId(1);
        System.out.println(order);
        orderMapper.insert(order);
    }
}
