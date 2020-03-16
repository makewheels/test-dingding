package com.eg.dinding.util;

import com.dingtalk.api.DefaultDingTalkClient;
import com.dingtalk.api.DingTalkClient;
import com.dingtalk.api.request.OapiRobotSendRequest;
import com.dingtalk.api.response.OapiRobotSendResponse;
import com.taobao.api.ApiException;
import org.apache.commons.codec.binary.Base64;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.net.URLEncoder;

/**
 * 钉钉机器人工具类
 *
 * @time 2020-03-16 18:22
 */
public class DingUtil {
    private final static String webhookUrl = "https://oapi.dingtalk.com/robot/send?access_token=df52b12e37f29cdd50dc64ca0fa0472d28f144f8fde14bb3a62f1224aab50a3b";
    private final static String secret = "SECa9c39f7948dd28271a651b0dfb60d5c86b028c49f85b79d79b8522e4f2e27e13";

    public static String getUrl() {
        long timestamp = System.currentTimeMillis();
        String stringToSign = timestamp + "\n" + secret;
        Mac mac;
        try {
            mac = Mac.getInstance("HmacSHA256");
            mac.init(new SecretKeySpec(secret.getBytes("UTF-8"), "HmacSHA256"));
            byte[] signData = mac.doFinal(stringToSign.getBytes("UTF-8"));
            String sign = URLEncoder.encode(new String(new Base64().encode(signData)), "UTF-8");
            return webhookUrl + "&timestamp=" + timestamp + "&sign=" + sign;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static OapiRobotSendResponse sendText(String text) {
        DingTalkClient client = new DefaultDingTalkClient(getUrl());
        OapiRobotSendRequest request = new OapiRobotSendRequest();
        request.setMsgtype("markdown");
        OapiRobotSendRequest.Markdown markdown = new OapiRobotSendRequest.Markdown();
        markdown.setTitle(text);
        markdown.setText("# " + text);

        request.setMarkdown(markdown);
        try {
            return client.execute(request);
        } catch (ApiException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void main(String[] args) {
        String text = "gagew" + System.currentTimeMillis();
        OapiRobotSendResponse response = sendText(text);
        System.out.println(response.getErrmsg());

    }
}
