/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dto.group;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;


/**
 * 拼团记录
 *
 * @author keyuan huangkeyuan@leimingtech.com
 * @since v1.0.0 2020-03-09
 */
@Data
@ApiModel(description = "GroupRecordOrderDTO")
public class GroupRecordOrderDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    private Long id;
    @ApiModelProperty(value = "店铺id")
    private Long storeId;
    @ApiModelProperty(value = "拼团id")
    private Long groupId;
    @ApiModelProperty(value = "创建拼团会员id")
    private Long memberId;
    @ApiModelProperty(value = "创建拼团会员名称")
    private String memberName;
    @ApiModelProperty(value = "拼团活动名称")
    private String groupName;
    @ApiModelProperty(value = "商品id")
    private Long goodsId;
    @ApiModelProperty(value = "规格id")
    private Long specId;
    @ApiModelProperty(value = "商品名称")
    private String goodsName;
    @ApiModelProperty(value = "商品主图")
    private String goodsMainPicture;
    @ApiModelProperty(value = "发起时间")
    private Date startTime;
    @ApiModelProperty(value = "成团超时时间")
    private Date overTime;
    @ApiModelProperty(value = "实际成团时间")
    private Date actualTime;
    @ApiModelProperty(value = "所需成团人数")
    private Integer needNum;
    @ApiModelProperty(value = "拼团状态")
    private Integer groupStatus;

    @ApiModelProperty(value = "拼团订单列表")
    private List<GroupOrderListDTO> groupOrderListDTOList;
}
