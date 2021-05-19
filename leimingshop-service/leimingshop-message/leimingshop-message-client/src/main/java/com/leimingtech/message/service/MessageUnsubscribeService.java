//
//package com.leimingtech.message.service;
//
//
//import com.leimingtech.message.dto.AliSmsUpInfoDTO;
//import com.leimingtech.message.dto.MessageUnsubscribeDTO;
//import com.leimingtech.message.service.fallback.MessageUnsubscribeServiceFallback;
//import org.springframework.web.bind.annotation.*;
//
//
//import com.leimingtech.commons.tools.page.PageData;
//
//
//
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//
//import java.util.List;
//import java.util.Map;
//
///**
// * 短信消息退订表
// *
// * @author kuangweiguo xuzhch@leimingtech.com
// * @since v1.0.0 2020-07-28
// */
//public interface MessageUnsubscribeService {
//
//
//    PageData<MessageUnsubscribeDTO> page(@RequestParam Map<String, Object> params);
//
//
//    List<MessageUnsubscribeDTO> list(@RequestParam Map<String, Object> params);
//
//
//    MessageUnsubscribeDTO get( Long id);
//
//
//    void save(@RequestBody MessageUnsubscribeDTO dto);
//
//
//    void update(@RequestBody MessageUnsubscribeDTO dto);
//
//
//    void delete(@RequestBody Long[] ids);
//
//    /**
//     * 获取SmsUp（上行短信消息）
//     * 上行短信指用户发送给通信服务提供商的短信，用于定制某种服务、完成某种查询、或是办理某种业务等。
//     * 通过订阅SmsReport短信下行状态报告，可以获知每条短信的发送情况，了解短信是否达到终端用户的状态与相关信息。
//     * 采用通过HTTP批量推送方式可以拉取上行短信消息（SmsUp）
//     */
//
//    void smsUpInfoGet(@RequestBody List<AliSmsUpInfoDTO> aliSmsUpInfoDTOS);
//
//
//    MessageUnsubscribeDTO selectBlacklistByMobile(@RequestParam("mobile") String mobile,
//                                                  @RequestParam(required = false, name = "messageTypeId") Long messageTypeId,
//                                                  @RequestParam(required = false, name = "flag") Integer flag);
//}
