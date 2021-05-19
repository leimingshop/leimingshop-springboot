/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dto.store;

import com.leimingtech.commons.tools.validator.group.AddGroup;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import java.io.Serializable;
import java.util.Date;


/**
 * 店铺表
 *
 * @author chengqian
 * @email 543826372@qq.com
 * @since 1.0.0 2019-06-06
 */
@Data
@ApiModel(description = "StoreDTO")
public class StoreDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    private Long id;
    @ApiModelProperty(value = "店铺名称")
    private String storeName;
    @ApiModelProperty(value = "店铺认证（默认0未认证，1已认证）")
    private Integer storeAuth;
    @ApiModelProperty(value = "店铺等级ID")
    private Long gradeId;
    @ApiModelProperty(value = "店铺logo")
    private String storeLogo;
    @ApiModelProperty(value = "店铺类型（1:自营商户，2:普通商户）")
    private Integer storeType;
    @ApiModelProperty(value = "店铺简介")
    private String storeIntro;
    @ApiModelProperty("店铺联系人")
    private String storeLinkman;
    @ApiModelProperty(value = "店铺二维码")
    private String qrCode;
    @ApiModelProperty("联系人电话")
    @Length(max = 11, message = "电话号码最长为11位", groups = AddGroup.class)
    private String linkmanPhone;
    @ApiModelProperty("10 待审核 20 审核通过 30 审核拒绝 40 待提交")
    private Integer registerAuditStatus;
    @ApiModelProperty("审核原因")
    private String registerAuditCause;
    @ApiModelProperty(value = "启用状态 0 启用 1 禁用")
    private Integer isEnable;
    @ApiModelProperty(value = "创建时间")
    private Date createDate;
    @ApiModelProperty(value = "更新时间")
    private Date updateDate;

}