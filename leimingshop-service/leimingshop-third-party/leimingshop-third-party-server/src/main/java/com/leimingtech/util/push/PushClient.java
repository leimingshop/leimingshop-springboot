/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.util.push;

import com.leimingtech.config.UmengConfig;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.io.BufferedReader;
import java.io.InputStreamReader;

@Slf4j
@Component
public class PushClient {

    @Autowired
    static UmengConfig umengConfig;
    // The user agent
    protected final String USER_AGENT = "Mozilla/5.0";

    //    private static RequestConfig requestConfig = null;
    // This object is used for sending the post request to Umeng
    protected CloseableHttpClient client = HttpClients.createDefault();
    @Resource
    private UmengConfig myUmengConfig;

    @PostConstruct
    public void init() {
        umengConfig = myUmengConfig;
    }

    public boolean send(UmengNotification msg) throws Exception {
        String timestamp = Integer.toString((int) (System.currentTimeMillis() / 1000));
        msg.setPredefinedKeyValue("timestamp", timestamp);
        String host = umengConfig.HTTP_FLAG == 1 ? umengConfig.HTTPS_HOST : umengConfig.HTTP_HOST;
        String url = host + umengConfig.POST_PATH;
        String postBody = msg.getPostBody();
        String sign = DigestUtils.md5Hex(("POST" + url + postBody + msg.getAppMasterSecret()).getBytes("UTF-8"));
        url = url + "?sign=" + sign;
        HttpPost post = new HttpPost(url);
        //post.setConfig(requestConfig);
        post.setHeader("User-Agent", USER_AGENT);
        StringEntity se = new StringEntity(postBody, "UTF-8");
        post.setEntity(se);
        // Send the post request and get the response
        log.info("推送的数据为{}", post);
        HttpResponse response = client.execute(post);
        log.info("响应的数据为{}", response);
        int status = response.getStatusLine().getStatusCode();
        System.out.println("Response Code : " + status);
        BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
        StringBuffer result = new StringBuffer();
        String line = "";
        while ((line = rd.readLine()) != null) {
            result.append(line);
        }
        System.out.println(result.toString());
        if (status == 200) {
            System.out.println("Notification sent successfully.");
        } else {
            System.out.println("Failed to send the notification!");
        }
        return true;
    }

    // Upload file with device_tokens to Umeng
    public String uploadContents(String appkey, String appMasterSecret, String contents) throws Exception {
        // Construct the json string
        JSONObject uploadJson = new JSONObject();
        uploadJson.put("appkey", appkey);
        String timestamp = Integer.toString((int) (System.currentTimeMillis() / 1000));
        uploadJson.put("timestamp", timestamp);
        uploadJson.put("content", contents);
        // Construct the request
        String host = umengConfig.HTTP_FLAG == 1 ? umengConfig.HTTPS_HOST : umengConfig.HTTP_HOST;
        String url = host + umengConfig.UPLOAD_PATH;
        String postBody = uploadJson.toString();
        String sign = DigestUtils.md5Hex(("POST" + url + postBody + appMasterSecret).getBytes("utf8"));
        url = url + "?sign=" + sign;
        HttpPost post = new HttpPost(url);
//        post.setConfig(requestConfig);
        post.setHeader("User-Agent", USER_AGENT);
        StringEntity se = new StringEntity(postBody, "UTF-8");
        post.setEntity(se);
        // Send the post request and get the response
        HttpResponse response = client.execute(post);
        System.out.println("Response Code : " + response.getStatusLine().getStatusCode());
        BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
        StringBuffer result = new StringBuffer();
        String line = "";
        while ((line = rd.readLine()) != null) {
            result.append(line);
        }
        System.out.println(result.toString());
        // Decode response string and get file_id from it
        JSONObject respJson = new JSONObject(result.toString());
        String ret = respJson.getString("ret");
        if (!"SUCCESS".equals(ret)) {
            throw new Exception("Failed to upload file");
        }
        JSONObject data = respJson.getJSONObject("data");
        String fileId = data.getString("file_id");
        // Set file_id into rootJson using setPredefinedKeyValue

        return fileId;
    }

}
