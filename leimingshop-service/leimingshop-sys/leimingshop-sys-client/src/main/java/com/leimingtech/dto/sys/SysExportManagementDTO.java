/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.dto.sys;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;


/**
 * 导入导出管理
 *
 * @author 刘远杰
 * @since v1.0.0 2019-11-14
 */
@Data
@ApiModel(description = "SysExportManagementDTO")
public class SysExportManagementDTO implements Serializable {
    private static final long serialVersionUID = 1L;


    @ApiModelProperty(value = "主键")
    private Long id;
    @ApiModelProperty(value = "店铺id")
    private Long storeId;
    @ApiModelProperty(value = "导入失败的条数")
    private Integer failureNumber;
    @ApiModelProperty(value = "导入成功的条数")
    private Integer successfulNumber;
    @ApiModelProperty(value = "任务名称")
    private String assignmentName;
    @ApiModelProperty(value = "状态 0失败 1进行中   2已完成")
    private Integer operationStatus;
    @ApiModelProperty(value = "完成时间")
    private Date finishTime;
    @ApiModelProperty(value = "下载地址")
    private String downloadLink;
    @ApiModelProperty(value = "更新人")
    private String updater;
    @ApiModelProperty(value = "创建者")
    private String creator;
    @ApiModelProperty(value = "创建时间")
    private Date createDate;
    @ApiModelProperty(value = "更新时间")
    private Date updateDate;
    @ApiModelProperty(value = "导入导出标识 0: 导入  1：导出")
    private Integer operatingLogo;
    @ApiModelProperty(value = "是否显示详情 0不显示 1显示")
    private Integer isShowDetail;
}
