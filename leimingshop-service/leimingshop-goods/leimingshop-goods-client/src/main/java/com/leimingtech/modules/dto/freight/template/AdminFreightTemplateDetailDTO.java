/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dto.freight.template;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;


/**
 * 后台运费模板详情实体
 *
 * @author 刘远杰 2634443725@qq.com
 * @since v1.0.0 2020-04-21
 */
@Data
@ApiModel(description = "AdminFreightTemplateDetailDTO")
public class AdminFreightTemplateDetailDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    private Long id;

    @ApiModelProperty(value = "模板名称")
    private String templateName;

    @ApiModelProperty(value = "模板类型：0按件数 1按重量")
    private Integer templateType;

    @ApiModelProperty(value = "是否默认运费模板：0否 1是")
    private Integer defaultFlag;

    @ApiModelProperty(value = "更新时间")
    private Date updateDate;

    @ApiModelProperty("运费模板规则集合")
    private List<FreightTemplateRuleDTO> freightTemplateRuleList;

}
