/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.entity.address;

import com.baomidou.mybatisplus.annotation.*;
import com.leimingtech.commons.mybatis.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * 会员地址表
 *
 * @author DY
 * @email 1197793912@qq.com
 * @since 1.0.0 2019-05-10
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("lmshop_member_address")
public class MemberAddressEntity extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 会员ID
     */
    private Long memberId;

    /**
     * 收件人名称
     */
    private String trueName;

    /**
     * 收件人电话
     */
    private String mobPhone;

    /**
     * 地区ID
     */
    private Long areaId;

    /**
     * 省级id
     */
    private Long provinceId;

    /**
     * 市级ID
     */
    private Long cityId;

    /**
     * 市级ID
     */
    private Long stressId;

    /**
     * 地址内容
     */
    private String areaInfo;

    /**
     * 地址
     */
    private String address;

    /**
     * 邮编
     */
    private String zipCode;

    /**
     * 上次选择(默认未选择:0,上次选择:1)
     */
    private Integer lastSelected;
    /**
     * 是否默认（ 默认为0:非默认，1:已默认）
     */
    private Integer defaultFlag;

    /**
     * 更新人
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private String updater;

    /**
     * 更新时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateDate;

    /**
     * 删除标记（默认为0未删除，1已删除）
     */
    @TableLogic
    @TableField(fill = FieldFill.INSERT)
    private Integer delFlag;

    /**
     * 乐观锁
     */
    @Version
    private Integer version;

}