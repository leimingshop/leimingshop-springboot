/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.sender;

import org.springframework.mail.javamail.JavaMailSender;

/**
 * Java邮件发送器
 *
 * @author huangkeyuan@leimingtech.com
 * @date 2020-09-18 16:49
 **/
public interface CustomJavaMailSender {

    /**
     * 生成Java邮件发送器
     **/
    JavaMailSender generateJavaMailSender();
}
