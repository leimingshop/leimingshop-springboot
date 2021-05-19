/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dto.store;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

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
@ApiModel(description = "PageStoreDTO")
public class PageStoreDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    private Long id;
    @ApiModelProperty(value = "店铺名称")
    private String storeName;
    @ApiModelProperty(value = "管理员账号")
    private String account;
    @ApiModelProperty(value = "店铺等级ID")
    private Long gradeId;
    @ApiModelProperty(value = "店铺等级名称")
    private String gradeName;
    @ApiModelProperty(value = "店铺logo")
    private String storeLogo;
    @ApiModelProperty(value = "店铺类型（1:自营商户，2:普通商户）")
    private Integer storeType;
    @ApiModelProperty(value = "状态（默认0：启用1：停用）")
    private Integer isEnable;
    @ApiModelProperty(value = "角色名称")
    private String roleName;
    @ApiModelProperty(value = "角色标识 1 店长 2 管理员 ")
    private Integer roleMark;

    @ApiModelProperty("店铺联系人")
    private String storeLinkman;
    @ApiModelProperty("联系人电话")
    private String linkmanPhone;
    @ApiModelProperty("10 待审核 20 审核通过 30 审核拒绝 40 待提交")
    private Integer registerAuditStatus;
    @ApiModelProperty("审核原因")
    private String registerAuditCause;
    @ApiModelProperty(value = "创建时间")
    private Date createDate;

}