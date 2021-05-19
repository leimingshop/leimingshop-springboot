//
//package com.leimingtech.entity;
//
//import com.baomidou.mybatisplus.annotation.TableId;
//import com.baomidou.mybatisplus.annotation.FieldFill;
//import com.baomidou.mybatisplus.annotation.TableField;
//import com.baomidou.mybatisplus.annotation.TableName;
//
//import com.leimingtech.commons.mybatis.entity.BaseEntity;
//import lombok.Data;
//import lombok.EqualsAndHashCode;
//
//import java.util.Date;
//
///**
// * 短信消息退订表
// *
// * @author kuangweiguo xuzhch@leimingtech.com
// * @since v1.0.0 2020-07-28
// */
//@Data
//@EqualsAndHashCode(callSuper=false)
//@TableName("lmshop_message_unsubscribe")
//public class MessageUnsubscribeEntity extends BaseEntity {
//	private static final long serialVersionUID = 1L;
//
//			/**
//	 * 用户ID
//	 */
//	private Long memberId;
//			/**
//	 * 用户名
//	 */
//	private String memberName;
//			/**
//	 * 手机号
//	 */
//	private String memberMobile;
//			/**
//	 * 是否退订(0:未退订，默认1:退订)
//	 */
//	private Integer unsubscribeFlag;
//			/**
//	 * 退订消息类型ID
//	 */
//	private Long messageTypeId;
//							}
