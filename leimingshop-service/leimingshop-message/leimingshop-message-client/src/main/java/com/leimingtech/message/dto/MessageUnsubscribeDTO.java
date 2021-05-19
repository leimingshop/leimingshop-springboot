//
//package com.leimingtech.message.dto;
//
//import io.swagger.annotations.ApiModel;
//import io.swagger.annotations.ApiModelProperty;
//
//import java.io.Serializable;
//import java.util.Date;
//
//import lombok.Data;
//
//
///**
// * 短信消息退订表
// *
// * @author kuangweiguo xuzhch@leimingtech.com
// * @since v1.0.0 2020-07-28
// */
//@Data
//@ApiModel(value = "短信消息退订表")
//public class MessageUnsubscribeDTO implements Serializable {
//    private static final long serialVersionUID = 1L;
//
//    @ApiModelProperty(value = "主键")
//    private Long id;
//    @ApiModelProperty(value = "用户ID")
//    private Long memberId;
//    @ApiModelProperty(value = "用户名")
//    private String memberName;
//    @ApiModelProperty(value = "手机号")
//    private String memberMobile;
//    @ApiModelProperty(value = "是否退订(0:未退订，默认1:退订)")
//    private Integer unsubscribeFlag;
//    @ApiModelProperty(value = "退订消息类型ID")
//    private Long messageTypeId;
//}
