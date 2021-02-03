package com.mallplus.marking.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 小程序直播房间列表返回实体类
 *
 * @author Caizize mojin on 2020/4/27
 */
@Data
@ApiModel(description = "小程序直播列表返回实体")
public class LivePlayerResponse {

    /**
     * errcode = 0 代表成功；errcode = 1 代表未创建直播房间
     */
    @ApiModelProperty(value = "errcode = 0 代表成功；errcode = 1 代表未创建直播房间")
    private int errcode;

    /**
     * 失败原因
     */
    @ApiModelProperty(value = "失败原因")
    private String errmsg;

    /**
     * 直播房间列表
     */
    @ApiModelProperty(value = "直播房间列表")
    private List<RoomInfo> room_info;

    /**
     * 总记录数
     */
    @ApiModelProperty(value = "总记录数")
    private int total;

    /**
     * 直播回放列表
     */
    @ApiModelProperty(value = "直播回放列表")
    private List<LiveReplay> live_replay;
}
