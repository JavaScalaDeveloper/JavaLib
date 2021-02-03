package com.mallplus.marking.service;


import com.mallplus.marking.vo.LivePlayerRequest;

/**
 * 小程序直播列表服务接口
 *
 * @author Caizize mojin on 2020/4/27
 */
public interface LivePlayerService {

    /**
     * 获取小程序直播房间列表
     *
     * @param livePlayerRequest 小程序直播列表请求实体
     * @return 小程序直播房间列表
     */
    Object queryLivePlayerList(LivePlayerRequest livePlayerRequest);

}
