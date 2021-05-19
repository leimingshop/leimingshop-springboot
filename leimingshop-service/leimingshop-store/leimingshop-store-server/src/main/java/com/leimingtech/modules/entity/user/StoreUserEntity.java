/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.entity.user;

import com.baomidou.mybatisplus.annotation.*;
import com.leimingtech.commons.mybatis.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * 店铺用户表
 *
 * @author chengqian
 * @email 543826372@qq.com
 * @since 1.0.0 2019-06-06
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("lmshop_store_user")
public class StoreUserEntity extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 登陆账号
     */
    private String account;

    /**
     * 商户头像
     */
    private String logo;

    /**
     * 登陆密码
     */
    private String password;

    /**
     * 昵称
     */
    private String nickname;

    /**
     * 性别（默认0:保密，1:男，2:女）
     */
    private Integer sex;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 手机号
     */
    private String mobilePhone;
    /**
     * 角色ID
     */
    private Long roleId;

    /**
     * 启用禁用 0 启用 1 禁用
     */
    private Integer isEnable;

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
