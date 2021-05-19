/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.config;


import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpHost;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.Objects;


/**
 * ES配置类 - RestClient
 *
 * @author lixiangx@leimingtech.com
 * @date 2019/11/16 19:27
 **/
@Slf4j
@Configuration
public class ESConfig {

    /**
     * 地址位数（ip:端口按:分割长度为2）
     */
    private static final int ADDRESS_LENGTH = 2;

    /**
     * 协议名称
     */
    private static final String HTTP_SCHEME = "http";

    /**
     * 权限验证
     */
    final CredentialsProvider credentialsProvider = new BasicCredentialsProvider();

    /**
     * ES地址信息（ip:port）
     */
    @Value("${data.elasticsearch.host}")
    private String[] address;

    @Value("${data.elasticsearch.maxresout}")
    private String maxresout;

    /**
     * 用户名
     */
    @Value("${data.elasticsearch.username}")
    private String userName;

    /**
     * 密码
     */
    @Value("${data.elasticsearch.password}")
    private String password;

    /**
     * 配置RestClient构造器
     *
     * @date 2019/12/10 15:49
     * @author lixiangx@leimingtech.com
     **/
    @Bean
    public RestClientBuilder restClientBuilder() {
        HttpHost[] hosts = Arrays.stream(address)
                .map(this::makeHttpHost)
                .filter(Objects::nonNull)
                .toArray(HttpHost[]::new);
        log.info("hosts:{}", Arrays.toString(hosts));

        //配置权限验证
//        credentialsProvider.setCredentials(AuthScope.ANY, new UsernamePasswordCredentials(userName, password));
        return RestClient.builder(hosts).setHttpClientConfigCallback(httpClientBuilder -> httpClientBuilder.setDefaultCredentialsProvider(credentialsProvider));
    }

    /**
     * 处理请求地址
     *
     * @param esAddress: ES地址信息
     * @date 2019/12/10 15:46
     * @author lixiangx@leimingtech.com
     **/
    private HttpHost makeHttpHost(String esAddress) {
        assert StringUtils.isNotEmpty(esAddress);
        String[] address = esAddress.split(":");
        if (address.length == ADDRESS_LENGTH) {
            String ip = address[0];
            int port = Integer.parseInt(address[1]);
            return new HttpHost(ip, port, HTTP_SCHEME);
        } else {
            return null;
        }
    }

    /**
     * 配置RestHighLevelClient Bean
     *
     * @date 2019/12/10 15:45
     * @author lixiangx@leimingtech.com
     **/
    @Bean(name = "highLevelClient")
    public RestHighLevelClient highLevelClient(@Autowired RestClientBuilder restClientBuilder) {
        return new RestHighLevelClient(restClientBuilder);
    }
}
