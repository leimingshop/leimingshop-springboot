/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.entity.message;

import com.baomidou.mybatisplus.annotation.TableName;
import com.leimingtech.commons.mybatis.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * 短消息接收人表
 *
 * @author dy
 * @email 1197793912@qq.com
 * @since 1.0.0 2019-05-16
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("lmshop_message_receiver")
public class MessageReceiverEntity extends BaseEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 站内信编号
     */
    private Long messageId;

    /**
     * 发送者id
     */
    private Long sendId;

    /**
     * 发送者名称
     */
    private String sendName;

    /**
     * 接受者id
     */
    private Long receiveId;

    /**
     * 接受者名称
     */
    private String receiveName;

    /**
     * 查看状态 0未读 1已读
     */
    private Integer status;

    /**
     * 用户是否删除站内信 0未删除 1已删除
     */
    private Integer showFlag;


}
