/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dto.goods.check;

import com.leimingtech.commons.tools.validator.group.AddGroup;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Null;
import java.io.Serializable;
import java.util.Date;


/**
 * 商品审核表
 *
 * @author dy
 * @email 1197793912@qq.com
 * @since 1.0.0 2019-06-13
 */
@Data
@ApiModel(description = "GoodsCheckDTO")
public class GoodsCheckDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    @Null(message = "{id.null}", groups = AddGroup.class)
    private Long id;

    @ApiModelProperty(value = "商品ID")
    @Null(message = "{id.null}", groups = AddGroup.class)
    private Long goodsId;

    @ApiModelProperty(value = "商品名称")
    private String goodsName;

    @ApiModelProperty(value = "商品结果，20:审核未通过，30:审核通过,40:违规下架")
    private Integer goodState;

    @ApiModelProperty(value = "操作类型，1:商品发布，2:商品跟新,3:审核通过,4:审核拒绝")
    private Integer operationType;

    @ApiModelProperty(value = "原因")
    private String remarks;

    @ApiModelProperty(value = "创建时间")
    private Date createDate;

    @ApiModelProperty(value = "创建人")
    private String creator;
    @ApiModelProperty(value = "更新人")
    private String updater;
    @ApiModelProperty(value = "更新时间")
    private Date updateDate;

    @ApiModelProperty(value = "商品ids")
    @NotEmpty(message = "商品id为必填", groups = AddGroup.class)
    private Long[] goodsIds;
    @ApiModelProperty(value = "状态填充remark时使用")
    private Integer status;

}