/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.leimingtech.commons.mybatis.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * 参数管理
 *
 * @since 1.0.0
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("sys_params")
public class SysParamsEntity extends BaseEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 参数编码
     */
    private String paramCode;
    /**
     * 参数值
     */
    private String paramValue;
    /**
     * 类型   0：系统参数   1：非系统参数
     */
    private Integer paramType;
    /**
     * 备注
     */
    private String remark;


}