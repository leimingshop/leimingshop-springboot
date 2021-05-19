/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.entity.setting;

import com.baomidou.mybatisplus.annotation.TableName;
import com.leimingtech.commons.mybatis.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * 系统设置表
 *
 * @author kuangweiguo
 * @email kuangweiguo@leimingtech.com
 * @since 1.0.0 2019-05-10
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("lmshop_setting")
public class SettingEntity extends BaseEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 名称
     */
    private String name;

    /**
     * 值
     */
    private String value;

    /**
     * 店铺ID
     */
    private Long storeId;

}
