package com.mallplus.marking.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 小程序直播商品实体类
 *
 * @author Caizize mojin on 2020/4/27
 */
@Data
@ApiModel(description = "小程序直播商品实体")
public class Goods {

    /**
     * 商品名称
     */
    @ApiModelProperty(value = "商品名称")
    private String name;

    /**
     * 商品图片
     */
    @ApiModelProperty(value = "商品图片")
    private String cover_img;

    /**
     * 商品详情地址
     */
    @ApiModelProperty(value = "商品详情地址")
    private String url;

    /**
     * 商品价格
     */
    @ApiModelProperty(value = "商品价格")
    private BigDecimal price;

    /**
     * 商品价格
     */
    @ApiModelProperty(value = "商品价格")
    private BigDecimal price2;

    /**
     * 商品价格类型
     */
    @ApiModelProperty(value = "商品价格类型")
    private int price_type;

}
