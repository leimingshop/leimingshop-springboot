/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.entity.order;

import com.baomidou.mybatisplus.annotation.TableName;
import com.leimingtech.commons.mybatis.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 订单地址信息实体
 *
 * @author 刘远杰
 * @email 2634443725@qq.com
 * @since 1.0 2019-06-13
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("lmshop_order_address")
public class OrderAddressEntity extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 会员ID
     */
    private Long buyerId;

    /**
     * 会员姓名
     */
    private String trueName;

    /**
     * 省级ID
     */
    private Long provinceId;

    /**
     * 市级ID
     */
    private Long cityId;

    /**
     * 地区ID
     */
    private Long areaId;

    /**
     * 街道ID
     */
    private Long stressId;

    /**
     * 地址
     */
    private String address;

    /**
     * 地区内容
     */
    private String areaInfo;

    /**
     * 手机电话
     */
    private String mobPhone;
}
