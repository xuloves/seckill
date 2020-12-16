package com.seckill.entity;

import java.math.BigDecimal;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 * @author 今何许
 * @date 2020-12-16 16:46
 *
 */

/**
    * 秒杀-秒杀订单信息
    */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("orders")
public class Order {
    private Integer id;

    /**
    * 商品ID
    */
    private Integer goodsId;

    /**
    * 商品名称
    */
    private String goodsName;

    /**
    * 商品数量
    */
    private Integer goodsCount;

    /**
    * 商品单价
    */
    private BigDecimal goodsPrice;

    /**
    * 订单状态，0新建未支付，1已支付，2已发货，3已收货，4已退款，5已完成
    */
    private int status;

    /**
    * 订单的创建时间
    */
    private Date createDate;

    /**
    * 支付时间
    */
    private Date payDate;
}