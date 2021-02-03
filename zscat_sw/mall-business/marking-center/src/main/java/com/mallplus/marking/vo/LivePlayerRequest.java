package com.mallplus.marking.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 小程序直播房间列表请求实体类
 *
 * @author Caizize mojin on 2020/4/27
 */
@Data
@ApiModel(description = "小程序直播请求实体")
public class LivePlayerRequest {

    /**
     * 起始拉取房间，start = 0 表示从第 1 个房间开始拉取
     */
    @ApiModelProperty(value = "起始拉取房间，start = 0 表示从第 1 个房间开始拉取")
    private int start = 0;

    /**
     * 每次拉取的个数上限，不要设置过大，建议 100 以内
     */
    @ApiModelProperty(value = "每次拉取的个数上限，不要设置过大，建议 100 以内")
    private int limit = 3;
    /**
     * 101 直播中
     * 102 预告
     * 103 已结束
     * // 101: 直播中, 102: 未开始, 103: 已结束, 104: 禁播, 105: 暂停中, 106: 异常，107：已过期
     */
    private int live_status;

}
