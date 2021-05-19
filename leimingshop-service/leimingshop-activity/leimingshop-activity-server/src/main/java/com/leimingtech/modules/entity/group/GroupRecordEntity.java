/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.entity.group;

import com.baomidou.mybatisplus.annotation.TableName;
import com.leimingtech.commons.mybatis.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * 拼团记录表
 *
 * @author keyuan huangkeyuan@leimingtech.com
 * @since v1.0.0 2020-03-09
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("lmshop_group_record")
public class GroupRecordEntity extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 店铺id
     */
    private Long storeId;
    /**
     * 拼团id
     */
    private Long groupId;
    /**
     * 创建拼团会员id
     */
    private Long memberId;
    /**
     * 创建拼团会员名称
     */
    private String memberName;
    /**
     * 拼团活动名称
     */
    private String groupName;
    /**
     * 商品id
     */
    private Long goodsId;
    /**
     * 规格id
     */
    private Long specId;
    /**
     * 商品名称
     */
    private String goodsName;
    /**
     * 商品主图
     */
    private String goodsMainPicture;

    /**
     * 拼团商品数量
     */
    private Integer goodsNum;
    /**
     * 发起时间
     */
    private Date startTime;
    /**
     * 成团超时时间
     */
    private Date overTime;
    /**
     * 实际成团时间
     */
    private Date actualTime;
    /**
     * 所需成团人数
     */
    private Integer needNum;
    /**
     * 拼团状态
     */
    private Integer groupStatus;
}
