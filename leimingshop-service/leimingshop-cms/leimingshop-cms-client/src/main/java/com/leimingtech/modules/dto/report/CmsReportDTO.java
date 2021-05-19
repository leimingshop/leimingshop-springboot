/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dto.report;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;


/**
 * @author hecheng 347861695@qq.com
 * @since v1.0.0 2020-04-02
 */
@Data
@ApiModel(value = "举报信息")
public class CmsReportDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "id")
    private Long id;

    @ApiModelProperty(value = "用户ID(后台获取)")
    private Long reportCreaterId;

    @NotNull(message = "举报信息不能为空")
    @ApiModelProperty(value = "举报信息")
    private String reportContent;

    @NotNull(message = "文章id不能为空")
    @ApiModelProperty(value = "举报区分id(文章id)")
    private Long reportFlagId;

    @ApiModelProperty(value = "举报区分标识（1：文章；）")
    private Integer reportFlag;

    @ApiModelProperty(value = "举报处理状态(1：待处理，2：通过，3未通过)")
    private Integer reportStatus;

    @ApiModelProperty(value = "举报结果")
    private String reportResult;

    @ApiModelProperty(value = "创建者(后台获取)")
    private String creator;

    @ApiModelProperty(value = "更新人(后台获取)")
    private String updater;
}