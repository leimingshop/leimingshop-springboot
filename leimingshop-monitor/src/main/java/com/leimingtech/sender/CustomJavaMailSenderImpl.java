/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.sender;

import com.leimingtech.properties.SpringMailProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Service;

import java.util.Properties;

/**
 * Java邮件发送器
 *
 * @author huangkeyuan@leimingtech.com
 * @date 2020-09-18 16:44
 **/
@Slf4j
@Service
public class CustomJavaMailSenderImpl implements CustomJavaMailSender {

    @Autowired
    private SpringMailProperties springMailProperties;

    /**
     * 生成Java邮件发送器
     **/
    @Override
    public JavaMailSender generateJavaMailSender() {
        JavaMailSenderImpl javaMailSender = new JavaMailSenderImpl();
        javaMailSender.setHost(this.springMailProperties.getHost());
        if (null != this.springMailProperties.getPort()) {
            javaMailSender.setPort(this.springMailProperties.getPort());
        }
        javaMailSender.setUsername(this.springMailProperties.getUsername());
        javaMailSender.setPassword(this.springMailProperties.getPassword());
        javaMailSender.setDefaultEncoding("UTF-8");

        Properties properties = new Properties();
        properties.setProperty("mail.smtp.auth", "true");
        properties.setProperty("mail.smtp.ssl.enable", "true");
        javaMailSender.setJavaMailProperties(properties);
        return javaMailSender;
    }
}
