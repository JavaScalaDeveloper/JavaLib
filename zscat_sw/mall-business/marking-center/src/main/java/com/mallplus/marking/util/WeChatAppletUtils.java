package com.mallplus.marking.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.BasicHttpClientConnectionManager;
import org.springframework.util.StringUtils;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.spec.AlgorithmParameterSpec;
import java.util.Objects;

/**
 * 微信小程序工具类
 *
 * @author SK
 * @since 2018/6/13
 */
@Slf4j
public class WeChatAppletUtils {

    /**
     * 获取登录信息地址
     */
    private final static String GET_LOGIN_INFO_URL = "https://api.weixin.qq.com/sns/jscode2session?appid=%s&secret=%s&js_code=%s&grant_type=authorization_code";

    /**
     * 获取微信小程序access_token地址（参数为 appid 和 secret）
     */
    private final static String GET_WECHAT_APPLET_ACCESS_TOKEN_ = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=%s&secret=%s";
    /**
     * 获取微信小程序分享码请求地址
     */
    private final static String GET_WECHAT_APPLET_SHARE_CODE_URL = "https://api.weixin.qq.com/wxa/getwxacodeunlimit?access_token=%s";
    /**
     * 获取微信小程序直播房间列表地址
     */
    private final static String GET_WECHAT_APPLET_LIVE_PLAYER_LIST_URL = "https://api.weixin.qq.com/wxa/business/getliveinfo?access_token=%s";

    private WeChatAppletUtils() {
    }

    private static String appid="";
    private static  String appSecret="";


        // 步数转卡路里
        public static float getDistanceByStep(long steps) {
            return steps * 0.6f / 1000;
        }

        /**
         * 微信解密运动步数
         *
         * @param sessionKey
         * @param encryptedData
         * @param iv
         * @return
         */
        public static String decryptWeChatRunInfo(String sessionKey, String encryptedData, String iv) {
            String result = null;
            byte[] encrypData = Base64.decodeBase64(encryptedData);
            byte[] ivData = Base64.decodeBase64(iv);
            byte[] sessionKeyB = Base64.decodeBase64(sessionKey);
            try {
                AlgorithmParameterSpec ivSpec = new IvParameterSpec(ivData);
                Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
                SecretKeySpec keySpec = new SecretKeySpec(sessionKeyB, "AES");
                cipher.init(Cipher.DECRYPT_MODE, keySpec, ivSpec);
                byte[] doFinal = cipher.doFinal(encrypData);
                result = new String(doFinal);
            }catch (Exception e){
                e.printStackTrace();
            }
            return result;
        }



    /**
     * 获取微信小程序access_token（有效期两小时）
     *
     * @return 微信小程序access_token
     */
    public static String getAccessToken() {

        String accessTokenForShareUrl = String.format(GET_WECHAT_APPLET_ACCESS_TOKEN_, appid, appSecret);
        JSONObject res = JSON.parseObject(CustomHttpUtils.doGet(accessTokenForShareUrl));
        if (!StringUtils.isEmpty(res.getString("errcode"))) {
            if (!StringUtils.isEmpty(res.getString("errmsg"))) {
                log.error("getAccessToken Fail and errmsg:{}", res.getString("errmsg"));
            }
            return null;
        } else {
            return res.getString("access_token");
        }
    }
    public static String getWxAppletAccessToken(String wechatSetting) {

        String wxAppletAccessToken = WeChatAppletUtils.getAccessToken();
        /*log.debug("getWxAppletAccessToken and wxAppletAccessToken :{}", wxAppletAccessToken);
        if (StringUtils.isEmpty(wxAppletAccessToken)) {
            wxAppletAccessToken = WeChatAppletUtils.getAccessToken(wechatSetting);
            redisService.setCacheObject(String.format("%s_%s", "wxAppletAccessToken_", wechatSetting.getAppId()), wxAppletAccessToken, 60, TimeUnit.MINUTES);
            log.info("getWxAppletAccessToken form HttpPost");
        } else {
            log.info("getWxAppletAccessToken form redis");
        }*/
        return wxAppletAccessToken;

    }
    /**
     * 获取json参数post请求返回值（微信小程序码）
     *
     * @param url        请求地址
     * @param jsonString 请求参数
     * @return 请求返回（ByteArrayInputStream格式，访问数组的字节输入流）
     */
    public static ByteArrayInputStream getJsonRequestResult(String url, String jsonString) {
        log.debug("getWeChatAppletCode and url :{} \r\n jsonString :{} ", url, jsonString);
        String result = null;
        InputStream inputStream = null;
        ByteArrayInputStream byteArrayInputStream = null;
        BasicHttpClientConnectionManager connManager = new BasicHttpClientConnectionManager(
                RegistryBuilder.<ConnectionSocketFactory>create()
                        .register("http", PlainConnectionSocketFactory.getSocketFactory())
                        .register("https", SSLConnectionSocketFactory.getSocketFactory())
                        .build(),
                null,
                null,
                null
        );
        HttpClient httpClient = HttpClientBuilder.create()
                .setConnectionManager(connManager)
                .build();
        HttpPost httpRequest = new HttpPost(url);
        httpRequest.setHeader("Content-type", "application/json; charset=utf-8");
        StringEntity requestParam = new StringEntity(jsonString, "UTF-8");
        requestParam.setContentType("application/json");
        try {
            httpRequest.setEntity(requestParam);
            HttpResponse response = httpClient.execute(httpRequest);
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                HttpEntity entity = response.getEntity();
                inputStream = entity.getContent();
                ByteArrayOutputStream outStream = new ByteArrayOutputStream();
                // 创建一个Buffer字符串
                byte[] buffer = new byte[1024];
                // 每次读取的字符串长度，如果为-1，代表全部读取完毕
                int len = 0;
                // 使用一个输入流从buffer里把数据读取出来
                while ((len = inputStream.read(buffer)) != -1) {
                    // 用输出流往buffer里写入数据，中间参数代表从哪个位置开始读，len代表读取的长度
                    outStream.write(buffer, 0, len);
                }
                // 关闭输入流
                inputStream.close();
                // 把outStream里的数据写入内存
                byteArrayInputStream = new ByteArrayInputStream(outStream.toByteArray());
            }
        } catch (IOException e) {
            e.printStackTrace();
            log.error(e.getMessage());
        }
        log.debug("getJsonRequestResult and result:{}", result == null ? "" : result);
        return byteArrayInputStream;
    }

    /**
     * 数组的字节输入流转化为base64字符串
     *
     * @param inputStream 输入流
     * @return base64字符串
     */
    public static String getBase64FromInputStream(InputStream inputStream) {
        // 将图片文件转化为字节数组字符串，并对其进行Base64编码处理
        byte[] data = null;
        // 读取图片字节数组
        try {
            if (Objects.isNull(inputStream) || inputStream.available() <= 200) {
                log.error("getBase64FromInputStream fail due to inputStream is null");
                return null;
            }
            ByteArrayOutputStream swapStream = new ByteArrayOutputStream();
            byte[] buff = new byte[100];
            int rc = 0;
            while ((rc = inputStream.read(buff, 0, 100)) > 0) {
                swapStream.write(buff, 0, rc);
            }
            data = swapStream.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return java.util.Base64.getEncoder().encodeToString(data);
    }

    /**
     * 获取微信小程序分享码请求地址
     *
     * @param accessToken 微信小程序access_token
     * @return 微信小程序分享码请求地址
     */
    public static String getWeChatAppletShareCodeUrl(String accessToken) {
        if (StringUtils.isEmpty(accessToken)) {
            log.error("getWeChatAppletShareCodeUrl fail due to accessToken is null");
            return null;
        }
        return String.format(GET_WECHAT_APPLET_SHARE_CODE_URL, accessToken);
    }

    /**
     * 获取微信小程序直播列表地址
     *
     * @param accessToken 微信小程序access_token
     * @return 微信小程序直播列表地址
     */
    public static String getWeChatAppletLivePlayerListUrl(String accessToken) {
        if (StringUtils.isEmpty(accessToken)) {
            log.error("getWeChatAppletLivePlayerListUrl fail due to accessToken is null");
            return null;
        }
        return String.format(GET_WECHAT_APPLET_LIVE_PLAYER_LIST_URL, accessToken);
    }

}
