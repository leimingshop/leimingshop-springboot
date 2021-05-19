/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.notify;

import cn.hutool.core.date.DateUtil;
import com.leimingtech.enums.InstanceStatus;
import com.leimingtech.properties.SpringBootAdminNotifyEmailProperties;
import com.leimingtech.sender.CustomJavaMailSender;
import de.codecentric.boot.admin.server.domain.entities.Instance;
import de.codecentric.boot.admin.server.domain.entities.InstanceRepository;
import de.codecentric.boot.admin.server.domain.events.InstanceEvent;
import de.codecentric.boot.admin.server.domain.events.InstanceStatusChangedEvent;
import de.codecentric.boot.admin.server.notify.AbstractStatusChangeNotifier;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import reactor.core.publisher.Mono;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * CustomNotifier
 *
 * @author huangkeyuan@leimingtech.com
 * @date 2020-09-18 16:50
 **/
@Slf4j
@Component
public class CustomNotifier extends AbstractStatusChangeNotifier {

    @Value("${spring.profiles.active}")
    private String profileActive;

    @Autowired
    private CustomJavaMailSender customJavaMailSender;

    @Autowired
    private SpringBootAdminNotifyEmailProperties springBootAdminNotifyEmailProperties;

    @Autowired
    private TemplateEngine templateEngine;

    public CustomNotifier(InstanceRepository repositpry) {
        super(repositpry);
    }

    @Override
    protected Mono<Void> doNotify(InstanceEvent instanceEvent, Instance instance) {
        JavaMailSender javaMailSender = this.customJavaMailSender.generateJavaMailSender();
        return Mono.fromRunnable(() -> {
            String profileActiveMessage = null;
            if ("dev".equals(profileActive)) {
                profileActiveMessage = "开发";
            }
            if ("test".equals(profileActive)) {
                profileActiveMessage = "测试";
            }
            if ("prod".equals(profileActive)) {
                profileActiveMessage = "生产";
            }
            String subjectPrefix = instance.getRegistration().getName();
            String warnMessagePrefix = "SpringBootAdmin发送" + profileActiveMessage + "环境应用报警：";

            if (instanceEvent instanceof InstanceStatusChangedEvent) {
                String status = ((InstanceStatusChangedEvent) instanceEvent).getStatusInfo().getStatus();
                InstanceStatus instanceStatus = InstanceStatus.valueOf(status);
                MimeMessage mimeMessage = javaMailSender.createMimeMessage();
                try {
                    MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);
                    mimeMessageHelper.setFrom(springBootAdminNotifyEmailProperties.getFrom());
                    mimeMessageHelper.setTo(springBootAdminNotifyEmailProperties.getTo().split(","));
                    if (StringUtils.isNotBlank(springBootAdminNotifyEmailProperties.getCc())) {
                        mimeMessageHelper.setCc(springBootAdminNotifyEmailProperties.getCc().split(","));
                    }
                    Date currentDate = new Date();
                    mimeMessageHelper.setSentDate(currentDate);
                    Map<String, Object> contextMap = new LinkedHashMap<>();
                    contextMap.put("event", "实例健康状态变更事件通知");
                    mimeMessageHelper.setSubject(subjectPrefix + "健康检查没通过");
                    if (InstanceStatus.DOWN.equals(instanceStatus)) {
                        mimeMessageHelper.setSubject(subjectPrefix + "健康检查没通过");
                        log.warn(warnMessagePrefix + "{}健康检查没通过！！", instance.getRegistration().getName());
                    } else if (InstanceStatus.OFFLINE.equals(instanceStatus)) {
                        mimeMessageHelper.setSubject(subjectPrefix + "实例已离线");
                        log.warn(warnMessagePrefix + "{}实例已离线！！", instance.getRegistration().getName());
                    } else if (InstanceStatus.UP.equals(instanceStatus)) {
                        mimeMessageHelper.setSubject(subjectPrefix + "实例已上线");
                        log.warn(warnMessagePrefix + "{}实例已上线！！", instance.getRegistration().getName());
                    } else if (InstanceStatus.UNKNOWN.equals(instanceStatus)) {
                        mimeMessageHelper.setSubject(subjectPrefix + "实例未知异常");
                        log.warn(warnMessagePrefix + "{}实例未知异常！！", instance.getRegistration().getName());
                    } else {
                        mimeMessageHelper.setSubject(subjectPrefix + "实例不支持通知");
                        log.warn(warnMessagePrefix + "{}实例不支持通知，当前状态 {}！！", instance.getRegistration().getName(), status);
                    }
                    contextMap.put("app", instance.getRegistration().getName());
                    contextMap.put("status", status);
                    contextMap.put("environment", profileActiveMessage + "环境");
                    contextMap.put("ip", instance.getRegistration().getHealthUrl());
                    contextMap.put("date", DateUtil.format(currentDate, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")));
                    contextMap.put("lastDate", DateUtil.format(currentDate, new SimpleDateFormat("yyyy年MM月dd日 HH时mm分ss秒")));
                    Context context = new Context();
                    context.setVariables(contextMap);
                    String content = this.templateEngine.process("mail_notify", context);
                    mimeMessageHelper.setText(content, true);
                    javaMailSender.send(mimeMessageHelper.getMimeMessage());
                    log.info("应用{}报警邮件发送成功！！", instance.getRegistration().getName());
                } catch (MessagingException e) {
                    log.error("应用" + instance.getRegistration().getName() + "发送报警邮件异常：", e);
                }
            } else {
                log.warn("实例 {} ({}) {}", instance.getRegistration().getName()
                        , instanceEvent.getInstance()
                        , instanceEvent.getType()
                );
            }
        });
    }
}
