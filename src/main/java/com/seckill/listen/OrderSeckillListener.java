package com.seckill.listen;

import com.rabbitmq.client.Channel;
import com.seckill.entity.Order;
import com.seckill.service.OrderService;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.IOException;

/**
 * @author 今何许
 * @date 2020-12-16 17:50
 */
@Component
@RabbitListener(queues = "order.seckill.order.queue")
public class OrderSeckillListener {
    @Resource
    private OrderService orderService;

    @RabbitHandler
    public void listener(Order order, Channel channel, Message message) throws IOException {

        try {
            System.out.println("....消费");
            orderService.createSeckillOrder(order);
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
        } catch (Exception e) {
            channel.basicReject(message.getMessageProperties().getDeliveryTag(), true);
        }

    }
}
