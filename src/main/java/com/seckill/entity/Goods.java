package com.seckill.entity;

import java.math.BigDecimal;

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
    * 秒杀-商品
    */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("goods")
public class Goods {
    private Integer id;

    /**
    * 商品名称
    */
    private String name;

    /**
    * 商品标题
    */
    private String title;

    /**
    * 商品的详情介绍
    */
    private String detail;

    /**
    * 商品单价
    */
    private BigDecimal price;

    /**
    * 商品库存，-1表示没有限制
    */
    private Integer stock;
}