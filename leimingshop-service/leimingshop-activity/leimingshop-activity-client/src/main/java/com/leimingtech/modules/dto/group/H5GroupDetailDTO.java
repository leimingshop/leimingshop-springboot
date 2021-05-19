/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dto.group;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;


/**
 * 拼团记录
 *
 * @author keyuan huangkeyuan@leimingtech.com
 * @since v1.0.0 2020-03-09
 */
@Data
@ApiModel(description = "H5GroupDetailDTO")
public class H5GroupDetailDTO implements Serializable {
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

    @ApiModelProperty(value = "规格名称")
    private String specName;

    @ApiModelProperty(value = "商品主图")
    private String goodsMainPicture;

    @ApiModelProperty(value = "商品数量")
    private Integer goodsNum;

    @ApiModelProperty("活动价格")
    private BigDecimal activityPrice;

    @ApiModelProperty("活动商品价格")
    private BigDecimal goodsPrice;

    @ApiModelProperty(value = "发起时间")
    private Date startTime;

    @ApiModelProperty(value = "成团超时时间")
    private Date overTime;

    @ApiModelProperty(value = "服务器当前时间")
    private Date currentTime;

    @ApiModelProperty(value = "实际成团时间")
    private Date actualTime;

    @ApiModelProperty(value = "剩余成团人数")
    private Integer needNum;

    @ApiModelProperty(value = "总的成团人数")
    private Integer needPeople;

    @ApiModelProperty(value = "拼团状态")
    private Integer groupStatus;

    @ApiModelProperty(value = "参与拼团的会员列表")
    private List<GroupMemberDTO> groupMemberDTOList;

    @ApiModelProperty(value = "当前用户是否参与此拼团(0:未参加，1已参加)")
    private Integer joinFlag;
}
