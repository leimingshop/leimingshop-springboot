/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.notify;

import de.codecentric.boot.admin.server.domain.entities.Instance;
import de.codecentric.boot.admin.server.domain.entities.InstanceRepository;
import de.codecentric.boot.admin.server.domain.events.InstanceEvent;
import de.codecentric.boot.admin.server.notify.AbstractStatusChangeNotifier;
import org.springframework.context.expression.MapAccessor;
import org.springframework.expression.Expression;
import org.springframework.expression.ParserContext;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

/**
 * DingTalkNotifier
 *
 * @author huangkeyuan@leimingtech.com
 * @date 2020-09-18 16:50
 **/
public class DingTalkNotifier extends AbstractStatusChangeNotifier {
    private static final String DEFAULT_MESSAGE = "*#{instance.registration.name}* (#{instance.id}) is *#{event.statusInfo.status}**";

    private final SpelExpressionParser parser = new SpelExpressionParser();
    private RestTemplate restTemplate = new RestTemplate();
    private String webhookToken;
    private String msgtype = "markdown";
    private String title = "服务告警";
    private Expression message;

    public DingTalkNotifier(InstanceRepository repository) {
        super(repository);
        this.message = parser.parseExpression(DEFAULT_MESSAGE, ParserContext.TEMPLATE_EXPRESSION);
    }

    @Override
    protected Mono<Void> doNotify(InstanceEvent event, Instance instance) {
        return Mono.fromRunnable(() -> restTemplate.postForEntity(webhookToken, createMessage(event, instance), Void.class));
    }


    private HttpEntity<Map<String, Object>> createMessage(InstanceEvent event, Instance instance) {
        Map<String, Object> messageJson = new HashMap<>(16);
        HashMap<String, String> params = new HashMap<>(16);
        params.put("text", this.getMessage(event, instance));
        params.put("title", this.title);
        messageJson.put("msgtype", this.msgtype);
        messageJson.put(this.msgtype, params);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
        return new HttpEntity<>(messageJson, headers);
    }

    private String getMessage(InstanceEvent event, Instance instance) {
        Map<String, Object> root = new HashMap<>(3);
        root.put("event", event);
        root.put("instance", instance);
        root.put("lastStatus", getLastStatus(event.getInstance()));
        StandardEvaluationContext context = new StandardEvaluationContext(root);
        context.addPropertyAccessor(new MapAccessor());
        String value = message.getValue(context, String.class);
        return value.concat(this.getDetail(instance.getStatusInfo().getDetails()));
    }

    public String getDetail(Map<String, Object> map) {
        String result = "";
        for (Map.Entry<String, Object> m : map.entrySet()) {
            Map<String, Object> valueMap = (Map) m.getValue();
            String temp = m.getKey() + ":" + valueMap.get("status") + ";";
            result = result.concat(temp);
        }
        return result;
    }

    public void setRestTemplate(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public String getWebhookToken() {
        return webhookToken;
    }

    public void setWebhookToken(String webhookToken) {
        this.webhookToken = webhookToken;
    }

    public String getMsgtype() {
        return msgtype;
    }

    public void setMsgtype(String msgtype) {
        this.msgtype = msgtype;
    }

    public Expression getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = (Expression) this.parser.parseExpression(message, ParserContext.TEMPLATE_EXPRESSION);
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
