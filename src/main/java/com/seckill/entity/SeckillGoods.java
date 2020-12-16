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
    * 秒杀-秒杀商品
    */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("seckill_goods")
public class SeckillGoods {
    private Integer id;

    /**
    * 商品Id
    */
    private Integer goodsId;

    /**
    * 秒杀价
    */
    private BigDecimal price;

    /**
    * 库存数量
    */
    private Integer stockCount;

    /**
    * 秒杀开始时间
    */
    private Date startDate;

    /**
    * 秒杀结束时间
    */
    private Date endDate;
}