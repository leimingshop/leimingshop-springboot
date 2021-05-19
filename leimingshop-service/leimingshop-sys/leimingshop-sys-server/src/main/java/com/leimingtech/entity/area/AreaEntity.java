/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.entity.area;

import com.baomidou.mybatisplus.annotation.TableName;
import com.leimingtech.commons.mybatis.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * @author anjun
 * @email anjun_314914423@126.com
 * @since 1.0.0 2019-05-14
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("lmshop_area")
public class AreaEntity extends BaseEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 地区名称
     */
    private String areaName;

    /**
     * 父节点id
     */
    private Long areaParentId;

    /**
     * 所属大区ID
     */
    private Long regionId;

    /**
     *
     */
    private Integer areaSort;

    /**
     * 地区深度，从1开始
     */
    private Integer areaDeep;

    /**
     * 序号
     */
    private String seqNum;

    /**
     * 是否开启站点（1.开启，2.不开启）
     */
    private Integer areaOpen;

}