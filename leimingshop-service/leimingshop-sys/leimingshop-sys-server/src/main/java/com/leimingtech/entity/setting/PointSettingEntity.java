/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.entity.setting;

import com.baomidou.mybatisplus.annotation.TableName;
import com.leimingtech.commons.mybatis.entity.BaseEntity;
import lombok.Data;

/**
 * 积分及成长值设置表
 *
 * @author lixiang lixiangx@leimingtech.com
 * @since v1.0.0 2019-12-23
 */
@Data
//@EqualsAndHashCode(callSuper = false)
@TableName("lmshop_point_setting")
public class PointSettingEntity extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 名称
     */
    private String name;
    /**
     * 值
     */
    private String value;
}
