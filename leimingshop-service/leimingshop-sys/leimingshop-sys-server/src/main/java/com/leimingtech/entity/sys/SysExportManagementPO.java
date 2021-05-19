/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.entity.sys;

import com.baomidou.mybatisplus.annotation.TableName;
import com.leimingtech.commons.mybatis.entity.BaseEntity;
import lombok.Data;

import java.util.Date;

/**
 * 导入导出管理
 *
 * @author 刘远杰
 * @since v1.0.0 2019-11-14
 */
@Data
@TableName("sys_export_management")
public class SysExportManagementPO extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 店铺id
     */
    private Long storeId;
    /**
     * 导入失败的条数
     */
    private Integer failureNumber;
    /**
     * 导入成功的条数
     */
    private Integer successfulNumber;
    /**
     * 任务名称
     */
    private String assignmentName;
    /**
     * 状态 0失败 1进行中   2已完成
     */
    private Integer operationStatus;
    /**
     * 完成时间
     */
    private Date finishTime;
    /**
     * 下载地址
     */
    private String downloadLink;

    /**
     * 导入导出标识 0: 导入  1：导出
     */
    private Integer operatingLogo;

    /**
     * 是否显示详情 0不显示 1显示
     */
    private Integer isShowDetail;
}