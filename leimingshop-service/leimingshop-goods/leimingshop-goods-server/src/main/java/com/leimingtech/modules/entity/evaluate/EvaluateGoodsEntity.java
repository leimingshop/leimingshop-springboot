/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.entity.evaluate;

import com.baomidou.mybatisplus.annotation.*;
import com.leimingtech.commons.mybatis.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 信誉评价表
 *
 * @author chengqian
 * @email 2634443725@gmail.com
 * @since 7.0 2019-05-15
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("lmshop_evaluate_goods")
public class EvaluateGoodsEntity extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 订单表ID
     */
    private Long orderId;

    /**
     * 订单编号
     */
    private Long orderSn;

    /**
     * 订单商品表编号
     */
    private Long orderGoodsId;

    /**
     * 商品表编号
     */
    private Long goodsId;

    /**
     * 商品名称
     */
    private String goodsName;

    /**
     * 商品价格
     */
    private BigDecimal goodsPrice;

    /**
     * 评价分数（1-5分）
     */
    private Integer evaluateScores;

    /**
     * 信誉评价内容
     */
    private String evaluateContent;

    /**
     * 0表示不是 1表示是匿名评价
     */
    private Integer isAnonymous;

    /**
     * 店铺编号
     */
    private Long storeId;

    /**
     * 店铺名称
     */
    private String storeName;

    /**
     * 评价人编号
     */
    private Long memberId;

    /**
     * 评价人名称
     */
    private String memberName;

    /**
     * 评价人的头像
     */
    private String memberAvatar;
    /**
     * 评价信息的状态 0为正常 1为禁止显示
     */
    private Integer evaluateState;

    /**
     * 读取状态 默认0未读 1已读
     */
    private Integer readState;

    /**
     * 管理员对评价的处理备注
     */
    private String evaluateRemark;

    /**
     * 解释内容
     */
    private String evaluateExplain;

    /**
     * 晒单图片
     */
    private String evaluateImage;
    /**
     * 回复内容
     */
    private String replyContent;

    /**
     * 回复时间
     */
    private Date replyDate;

    /**
     * 商品规格信息
     */
    private String specInfo;

    /**
     * 点赞数
     */
    private Long likeCount;
    /**
     * 商品规格主图
     */
    private String specMainPicture;

    /**
     * 商品规格ID
     */
    private Long goodsSpecId;

    /**
     * 浏览数
     */
    private Long browseNum;

    /**
     * 违规删除0:未删除;1.已删除
     */
    private Integer illegalDel;

    /**
     * 是否删除0:未删除;1.已删除
     */
    @TableLogic
    @TableField(fill = FieldFill.INSERT)
    private Integer delFlag;

    /**
     * 修改人
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private String updater;

    /**
     * 修改时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateDate;

    /**
     * 版本号
     */
    @Version
    private Integer version;


}
