/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.job.config;

import org.jasypt.encryption.StringEncryptor;
import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.jasypt.encryption.pbe.config.EnvironmentStringPBEConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Jasypt加密规则配置
 *
 * @author lixiang
 * @version V1.0
 * @date 2019/8/1 9:47
 **/
@Configuration
public class JasyptConfig {

    @Value("${jasypt.encryptor.password}")
    private String password;

    @Bean(name = "jasyptStringEncryptor")
    public StringEncryptor stringEncryptor() {

        // 加密工具
        StandardPBEStringEncryptor encryptor = new StandardPBEStringEncryptor();
        EnvironmentStringPBEConfig config = new EnvironmentStringPBEConfig();
        // 加密配置
        config.setAlgorithm("PBEWithMD5AndDES");
        // 自己在用的时候更改此密钥
        config.setPassword(password);
        config.setProviderName(null);
        config.setStringOutputType("base64");
        config.setSaltGeneratorClassName("org.jasypt.salt.RandomSaltGenerator");
        config.setProviderClassName(null);
        config.setProviderName(null);
        config.setKeyObtentionIterations(1000);
        //应用配置
        encryptor.setConfig(config);
        return encryptor;
    }

}
