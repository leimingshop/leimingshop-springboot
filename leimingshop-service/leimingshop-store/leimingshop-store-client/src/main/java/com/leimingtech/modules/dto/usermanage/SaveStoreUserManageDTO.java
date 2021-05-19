/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dto.usermanage;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;


/**
 * 新增用户和店铺的管理表
 *
 * @author chengqian
 * @email 543826372@qq.com
 * @since 1.0.0 2019-06-06
 */
@Data
@ApiModel(description = "SaveStoreUserManageDTO")
public class SaveStoreUserManageDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "店铺ID")
    private Long storeId;
    @ApiModelProperty(value = "店铺用户ID")
    private Long storeUserId;
    @ApiModelProperty(value = "角色ID")
    private Long roleId;
    @ApiModelProperty(value = "状态（默认0：启用1：停用）")
    private Integer isEnable;

}