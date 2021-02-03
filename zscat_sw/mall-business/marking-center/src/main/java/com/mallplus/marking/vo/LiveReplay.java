package com.mallplus.marking.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 小程序直播回放实体类
 *
 * @author Caizize mojin on 2020/4/27
 */
@Data
@ApiModel(description = "小程序直播回放实体")
public class LiveReplay {

    /**
     * 回放视频 url 过期时间
     */
    @ApiModelProperty(value = "回放视频 url 过期时间")
    private String expire_time;

    /**
     * 回放视频创建时间
     */
    @ApiModelProperty(value = "回放视频创建时间")
    private String create_time;

    /**
     * 回放视频
     */
    @ApiModelProperty(value = "回放视频")
    private String media_url;

}
