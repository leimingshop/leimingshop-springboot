/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.entity.index;

import com.baomidou.mybatisplus.annotation.TableName;
import com.leimingtech.commons.mybatis.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 用户首页按钮配置
 *
 * @author chengqian chengqian@leimingtech.com
 * @since v1.0.0 2020-03-16
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("lmshop_store_user_function")
public class StoreUserFunctionEntity extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 用户Id
     */
    private Long userId;
    /**
     * 角色Id
     */
    private Long roleId;
    /**
     * 菜单Id
     */
    private Long menuId;
}