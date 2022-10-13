package plus.liguowen.api.service;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import plus.liguowen.api.Environment;

public class DingService {

    public static void dingRequest(String message) {
        message = "virmach: " + message;
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        String url = null;
        try {
            url = Environment.url;
        } catch (Exception e) {
            e.printStackTrace();
        }

        HttpPost httpPost = new HttpPost(url);
        //设置http的请求头，发送json字符串，编码UTF-8
        httpPost.setHeader("Content-Type", "application/json;charset=utf8");
        //生成json对象传入字符
        JSONObject result = new JSONObject();
        JSONObject text = new JSONObject();
        text.put("content", message);
        result.put("msgtype", "text");
        result.put("text", text);
        String jsonString = JSON.toJSONString(result);
        StringEntity entity = new StringEntity(jsonString, "UTF-8");
        System.out.println(jsonString);

        //设置http请求的内容
        httpPost.setEntity(entity);
        // 响应模型
        CloseableHttpResponse response = null;
        try {
            // 由客户端执行(发送)Post请求
            response = httpClient.execute(httpPost);
            // 从响应模型中获取响应实体
            HttpEntity responseEntity = response.getEntity();
            if (responseEntity != null) {
                System.out.println("响应内容长度为:" + responseEntity.getContentLength());
                System.out.println("响应内容为:" + EntityUtils.toString(responseEntity));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                // 释放资源
                if (httpClient != null) {
                    httpClient.close();
                }
                if (response != null) {
                    response.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
