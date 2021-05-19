/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.entity.feedback;

import com.baomidou.mybatisplus.annotation.TableName;
import com.leimingtech.commons.mybatis.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * 意见反馈信息表
 *
 * @author chengqian chengqian@leimingtech.com
 * @since v1.0.0 2020-04-13
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("lmshop_feedback")
public class FeedbackEntity extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 会员账号
     */
    private String memberName;
    /**
     * 用户ID
     */
    private Long memberId;
    /**
     * 手机号码
     */
    private String mobileNumber;
    /**
     * 反馈类型（默认1：功能异常、2：优化建议、3：其他反馈）
     */
    private Integer feedbackType;
    /**
     * 详细说明
     */
    private String detailedDescription;
    /**
     * 图片数组（,分隔）
     */
    private String imagesArr;
    /**
     * 反馈来源（默认0:pc,1:h5,2:android,3:ios,4:微信,5:小程序）
     */
    private Integer feedbackSource;
    /**
     * 反馈判定（0:无效反馈、1:有效反馈、2:重点问题、)
     */
    private Integer decideType;
    /**
     * 客服回复
     */
    private String servicesReply;
    /**
     * 处理状态(默认0:未处理、1：已处理)
     */
    private Integer disposeStatus;
    /**
     * 处理时间
     */
    private Date disposeDate;
    /**
     * 处理人
     */
    private String operator;
    /**
     * 客服备注
     */
    private String remark;
    /**
     * 用户是否删除（默认为0未删除，1已删除）
     */
    private Integer userDelFlag;
}