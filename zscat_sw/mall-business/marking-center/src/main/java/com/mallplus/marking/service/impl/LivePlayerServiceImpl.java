package com.mallplus.marking.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import com.mallplus.common.utils.CommonResult;
import com.mallplus.marking.service.LivePlayerService;
import com.mallplus.marking.util.CustomHttpUtils;
import com.mallplus.marking.util.WeChatAppletUtils;
import com.mallplus.marking.vo.LivePlayerRequest;
import com.mallplus.marking.vo.LivePlayerResponse;
import com.mallplus.marking.vo.RoomInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 小程序直播列表服务接口实现类
 *
 * @author Caizize mojin on 2020/4/27
 */
@Service
@Slf4j
public class LivePlayerServiceImpl implements LivePlayerService {



    /**
     * 获取小程序直播房间列表
     *
     * @param livePlayerRequest 小程序直播列表请求实体
     * @return 小程序直播房间列表
     */
    @Override
    public Object queryLivePlayerList(LivePlayerRequest livePlayerRequest) {
        log.debug("queryLivePlayerList and livePlayerRequest:{} ", livePlayerRequest);
        if (Objects.isNull(livePlayerRequest)) {
        //    return Collections.emptyList();
        }
        LivePlayerResponse livePlayerResponse = new LivePlayerResponse();
        try {
            // 获取微信小程序支付设置

            // 设置获取微信小程序码接口调用凭证
            String accessToken = WeChatAppletUtils.getWxAppletAccessToken(null);
            if (StringUtils.isEmpty(accessToken)) {
                log.error("queryLivePlayerList fail due to accessToken is null");
                return new CommonResult().failed("queryLivePlayerList fail due to accessToken is null");
            }
            // 获取微信小程序直播列表地址
            String getWeChatAppletLivePlayerListUrl = WeChatAppletUtils.getWeChatAppletLivePlayerListUrl(accessToken);
            if (StringUtils.isEmpty(getWeChatAppletLivePlayerListUrl)) {
                log.error("queryLivePlayerList fail due to getWeChatAppletLivePlayerListUrl is null");
                return new CommonResult().failed("queryLivePlayerList fail due to getWeChatAppletLivePlayerListUrl is null");
            }
            // 获取请求返回信息
            String liveResponseString = CustomHttpUtils.postJson(getWeChatAppletLivePlayerListUrl, JSON.toJSONString(livePlayerRequest));
            if (Objects.isNull(liveResponseString)) {
                log.error("queryLivePlayerList fail due to http post response null");
                return new CommonResult().failed("queryLivePlayerList fail due to http post response null");
            }
            livePlayerResponse = JSONObject.parseObject(liveResponseString, LivePlayerResponse.class);
            // 判断是否正确返回信息
            if (livePlayerResponse.getErrcode() != 0) {
                log.error("queryLivePlayerList fail due to errmsg :{}", livePlayerResponse.getErrmsg());
                return new CommonResult().failed("queryLivePlayerList fail due to errmsg:"+livePlayerResponse.getErrmsg());
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
        }
        if (CollectionUtils.isEmpty(livePlayerResponse.getRoom_info())) {
            return new CommonResult().failed("queryLivePlayerList fail due to accessToken is null");
        }
        return new CommonResult().success(livePlayerResponse.getRoom_info().stream().peek(RoomInfo::formatTime).collect(Collectors.toList()));
    }

}
