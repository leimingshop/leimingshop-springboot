/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.email;

import cn.hutool.core.map.MapUtil;
import com.leimingtech.commons.tools.constant.Constant;
import com.leimingtech.commons.tools.exception.CustomException;
import com.leimingtech.entity.SysMailTemplateEntity;
import com.leimingtech.message.email.EmailConfig;
import com.leimingtech.message.exception.ModuleErrorCode;
import com.leimingtech.message.utils.ModuleConstant;
import com.leimingtech.remote.ParamsRemoteService;
import com.leimingtech.service.impl.SysMailLogServiceImpl;
import com.leimingtech.service.impl.SysMailTemplateServiceImpl;
import freemarker.template.Template;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.Map;
import java.util.Properties;

/**
 * 邮件工具类
 */
@Slf4j
@Component
public class EmailUtils {

    private final static String KEY = ModuleConstant.MAIL_CONFIG_KEY;
    private Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    private ParamsRemoteService paramsRemoteService;
    @Autowired
    private SysMailTemplateServiceImpl sysMailTemplateService;
    @Autowired
    private SysMailLogServiceImpl sysMailLogService;

    private JavaMailSenderImpl createMailSender(EmailConfig config) {
        JavaMailSenderImpl sender = new JavaMailSenderImpl();
        sender.setHost(config.getSmtp());
        sender.setPort(config.getPort());
        sender.setUsername(config.getUsername());
        sender.setPassword(config.getPassword());
        sender.setDefaultEncoding("Utf-8");
        Properties p = new Properties();
        p.setProperty("mail.smtp.timeout", "10000");
        p.setProperty("mail.smtp.auth", "false");
        //设置端口
        p.setProperty("mail.smtp.port", Integer.toString(config.getPort()));
        p.setProperty("mail.smtp.socketFactory.port", Integer.toString(config.getPort()));
        p.setProperty("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");

        sender.setJavaMailProperties(p);
        return sender;
    }

    /**
     * 发送邮件
     *
     * @param templateId 模板ID
     * @param to         收件人
     * @param cc         抄送
     * @param params     模板参数
     * @return true：成功   false：失败
     */
    public boolean sendMail(Long templateId, String[] to, String[] cc, Map<String, Object> params) {
        SysMailTemplateEntity template = sysMailTemplateService.selectById(templateId);
        if (template == null) {
            throw new CustomException(ModuleErrorCode.MAIL_TEMPLATE_NOT_EXISTS);
        }

        EmailConfig config = paramsRemoteService.getValueObject(KEY, EmailConfig.class);
        JavaMailSenderImpl mailSender = createMailSender(config);
        MimeMessage mimeMessage = mailSender.createMimeMessage();

        //设置utf-8编码
        MimeMessageHelper messageHelper = null;
        int status = Constant.SUCCESS;
        try {
            messageHelper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
            messageHelper.setFrom(config.getUsername());

            //收件人
            messageHelper.setTo(to);

            //抄送
            if (cc != null && cc.length > 0) {
                messageHelper.setCc(cc);
            }
            //主题
            messageHelper.setSubject(template.getSubject());

            //邮件正文
            String content = getFreemarkerContent(template.getContent(), params);
            messageHelper.setText(content, true);
            //发送邮件
            mailSender.send(mimeMessage);
            sysMailLogService.save(templateId, config.getUsername(), to, cc, template.getSubject(), content, status);
        } catch (Exception e) {
            status = Constant.FAIL;
            log.error("错误信息为" + e);
        }

        return status == Constant.SUCCESS ? true : false;
    }

    /**
     * 获取Freemarker渲染后的内容
     *
     * @param content 模板内容
     * @param params  参数
     */
    private String getFreemarkerContent(String content, Map<String, Object> params) {
        if (MapUtil.isEmpty(params)) {
            return content;
        }

        //模板
        StringReader reader = new StringReader(content);
        Template template = null;
        StringWriter sw = null;

        try {
            template = new Template("mail", reader, null, "utf-8");
            //渲染模板
            sw = new StringWriter();
            template.process(params, sw);

            content = sw.toString();
        } catch (Exception e) {
            log.error("错误信息为" + e);
        }

        try {
            sw.close();
        } catch (IOException e) {
            log.error("错误信息为" + e);
        }

        return content;
    }

    /**
     * 发送邮件
     *
     * @param to      收件人
     * @param cc      抄送
     * @param subject 主题
     * @param content 邮件正文
     * @return true：成功   false：失败
     */
    public boolean sendMail(String[] to, String[] cc, String subject, String content) {
        EmailConfig config = paramsRemoteService.getValueObject(KEY, EmailConfig.class);
        JavaMailSenderImpl mailSender = createMailSender(config);
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        //设置utf-8编码
        MimeMessageHelper messageHelper = null;
        int status = Constant.SUCCESS;
        try {
            messageHelper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
            messageHelper.setFrom(config.getUsername());

            //收件人
            messageHelper.setTo(to);
            //抄送
            if (cc != null && cc.length > 0) {
                messageHelper.setCc(cc);
            }
            //主题
            messageHelper.setSubject(subject);
            //邮件正文
            messageHelper.setText(content, true);
            //发送邮件
            mailSender.send(mimeMessage);
            sysMailLogService.save(null, config.getUsername(), to, cc, subject, content, status);

        } catch (Exception e) {
            status = Constant.FAIL;
            logger.error("send error", e);
        }

        return status == Constant.SUCCESS ? true : false;
    }

}
