package com.mallplus.marking.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;

/**
 * 小程序直播房间信息实体类
 *
 * @author Caizize mojin on 2020/4/27
 */
@Data
@ApiModel(description = "小程序直播房间信息实体")
public class RoomInfo {

    /**
     * 房间名
     */
    @ApiModelProperty(value = "房间名")
    private String name;

    /**
     * 直播间背景墙
     */
    @ApiModelProperty(value = "直播间背景墙")
    private String cover_img;

    /**
     * 直播计划开始时间，列表按照 start_time 降序排列
     */
    @ApiModelProperty(value = "直播计划开始时间，列表按照 start_time 降序排列")
    private long start_time;

    /**
     * 直播计划结束时间
     */
    @ApiModelProperty(value = "直播计划结束时间")
    private long end_time;

    /**
     * 主播名
     */
    @ApiModelProperty(value = "主播名")
    private String anchor_name;

    /**
     * 房间 id
     */
    @ApiModelProperty(value = "房间 id")
    private long roomid;

    /**
     * 商品列表
     */
    @ApiModelProperty(value = "商品列表")
    private List<Goods> goods;

    /**
     * 直播状态 101: 直播中, 102: 未开始, 103: 已结束, 104: 禁播, 105: 暂停中, 106: 异常, 107: 已过期
     */
    @ApiModelProperty(value = "直播状态 101: 直播中, 102: 未开始, 103: 已结束, 104: 禁播, 105: 暂停中, 106: 异常, 107: 已过期")
    private int live_status;

    /**
     * 分享图片
     */
    @ApiModelProperty(value = "分享图片")
    private String share_img;

    /**
     * 开始时间
     */
    @ApiModelProperty(value = "锁定时间")
    private LocalDateTime startTime;

    /**
     * 结束时间
     */

    @ApiModelProperty(value = "最后登录时间")
    private LocalDateTime endTime;


    /**
     * 格式化时间
     *
     * @return 当前实体
     */
    public RoomInfo formatTime() {
        this.startTime = LocalDateTime.ofEpochSecond(this.start_time, 0, ZoneOffset.ofHours(8));
        this.endTime = LocalDateTime.ofEpochSecond(this.end_time, 0, ZoneOffset.ofHours(8));
        return this;
    }


}
