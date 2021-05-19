/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dto.report;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 前台举报分页管理
 *
 * @author chengqian
 */
@Data
@ApiModel(value = "前台举报分页管理")
public class CmsReportPageDto implements Serializable {
    private static final long serialVersionUID = 1L;
    @ApiModelProperty(value = "id")
    private Long id;

    @ApiModelProperty(value = "举报人ID")
    private Long reportCreaterId;

    @ApiModelProperty(value = "举报人")
    private String creator;

    @ApiModelProperty(value = "举报信息")
    private String reportContent;

    @ApiModelProperty(value = "举报区分id(文章id)")
    private Long reportFlagId;

    @ApiModelProperty(value = "举报内容名称名称")
    private String reportFlagName;

    @ApiModelProperty(value = "发布用户id")
    private Long publishUserId;

    @ApiModelProperty(value = "发布用户姓名")
    private String publishUserName;

    @ApiModelProperty(value = "举报区分标识（1：文章；）")
    private Integer reportFlag;

    @ApiModelProperty(value = "举报处理状态(1：待处理，2：通过，3未通过)")
    private Integer reportStatus;

    @ApiModelProperty(value = "举报结果")
    private String reportResult;

    @ApiModelProperty(value = "举报时间")
    private Date createDate;

}
