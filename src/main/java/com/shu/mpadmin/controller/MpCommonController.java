package com.shu.mpadmin.controller;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
public class MpCommonController {

    private static final String appId="wxf9788c249032b959";
    private static final String appSecret="c69674e1cb73d754ef9cab64f3553867";

    //日志记录器
    private static final Logger logger = LoggerFactory.getLogger(MpUserController.class);

    /**
     * 获取openID方法
     * @param code
     * @return
     * @throws Exception
     */
    public JSONObject getOpenIdByWX(String code) throws Exception{
        // 获得Http客户端
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        // 创建GET请求
        HttpGet hget = new HttpGet("https://api.weixin.qq.com/sns/jscode2session?appid="+appId+"&secret="+appSecret+"&js_code="+code+"&grant_type=authorization_code");
        // 由客户端执行发送GET请求
        CloseableHttpResponse res= httpClient.execute(hget);
        /* 从响应模型中获取响应实体 */
        HttpEntity resEntity = res.getEntity();
        //将响应实体转换为字符串
        String resStr = EntityUtils.toString(resEntity);
        //将字符串转换为json对象
        JSONObject jsonObject=new JSONObject(resStr);
        return jsonObject;
    }

    /**
     * 获取小程序的accessToken
     * @return
     * @throws IOException
     * @throws JSONException
     */
    @GetMapping("/shu/common/getAccessToken")
    public String getAccessToken() throws IOException, JSONException {
        logger.info("/shu/common/getAccessToken,获取小程序的accessToken接口");
        // 获得Http客户端
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        // 创建GET请求
        HttpGet hget = new HttpGet("https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid="+appId+"&secret="+appSecret);
        // 由客户端执行发送GET请求
        CloseableHttpResponse res= httpClient.execute(hget);
        /* 从响应模型中获取响应实体 */
        HttpEntity resEntity = res.getEntity();
        //将响应实体转换为字符串
        String resStr = EntityUtils.toString(resEntity);
        //将字符串转换为json对象
        JSONObject jsonObject=new JSONObject(resStr);
        return jsonObject.toString();
    }




}
