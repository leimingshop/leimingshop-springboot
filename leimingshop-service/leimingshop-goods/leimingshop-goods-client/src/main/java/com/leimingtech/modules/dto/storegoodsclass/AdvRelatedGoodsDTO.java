/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dto.storegoodsclass;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;


/**
 * 广告管理
 *
 * @author 刘远杰
 * @email 2634443725@gmail.com
 * @since 7.0 2019-05-13
 */
@Data
@ApiModel(description = "AdvRelatedGoodsDTO")
public class AdvRelatedGoodsDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    private Long id;

    @ApiModelProperty(value = "广告类别ID")
    private Long categoryId;

    @ApiModelProperty(value = "广告类别标识")
    private String advKey;

    @ApiModelProperty(value = "广告标题")
    private String advTitle;

    @ApiModelProperty(value = "广告开始时间（精确到日）")
    private Date startDate;

    @ApiModelProperty(value = "广告结束时间（精确到日）")
    private Date endDate;

    @ApiModelProperty(value = "广告类型 0：普通广告 1：楼层广告 2：分类广告")
    private Integer advType;

    @ApiModelProperty(value = "楼层广告、分类广告关联内容id，普通广告为空")
    private Long relationId;

    @ApiModelProperty(value = "关联类型（链接、店铺、活动等等）")
    private String relationType;

    @ApiModelProperty(value = "关联目标（链接地址、店铺ID等等）")
    private String relationTarget;

    @ApiModelProperty(value = "图片地址")
    private String imageUrl;

    @ApiModelProperty(value = "排序")
    private Integer sort;

    @ApiModelProperty(value = "店铺ID")
    private Long storeId;

    @ApiModelProperty(value = "区域ID")
    private String siteId;

    @ApiModelProperty(value = "广告状态（默认0:未启用、1:启用、2:停用）")
    private Integer advState;

    @ApiModelProperty(value = "广告审批状态（默认0:待审核、1:审核通过、2:审核未通过）")
    private Integer applyState;

    @ApiModelProperty(value = "审批人")
    private String applyBy;

    @ApiModelProperty(value = "审批时间")
    private Date applyDate;

    @ApiModelProperty(value = "广告类别名称")
    private String categoryName;

    @ApiModelProperty(value = "h5楼层名称")
    private String floorName;

    @ApiModelProperty(value = "广告类别名称")
    private String gcName;

}
