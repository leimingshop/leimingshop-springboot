/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dto.store;

import com.leimingtech.commons.tools.validator.group.AddGroup;
import com.leimingtech.modules.dto.auth.StoreAuthDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

/**
 * @Description 更新店铺信息
 * @Data: 2019/6/6 16:48
 * @Author: chengqian
 * @Version: 1.0
 */
@Data
@ApiModel(description = "UpdateStoreInfo")
public class UpdateStoreInfo {
    @ApiModelProperty(value = "店铺分类")
    Long[] classId;
    @ApiModelProperty(value = "店铺用户ID")
    private Long id;
    @ApiModelProperty(value = "昵称")
    private String nickname;
    @ApiModelProperty(value = "性别（默认0:保密，1:男，2:女）")
    private Integer sex;
    @ApiModelProperty(value = "邮箱")
    private String email;
    @ApiModelProperty(value = "手机号")
    @Length(max = 11, message = "手机号不能超过11位", groups = AddGroup.class)
    private String mobilePhone;
    @ApiModelProperty(value = "角色ID")
    private Long roleId;
    @ApiModelProperty(value = "启用禁用 （默认0：启用1：停用）")
    private Integer isEnable;
    @ApiModelProperty(value = "店铺信息")
    private FindStoreDTO findStoreDTO;
    @ApiModelProperty(value = "认证信息")
    private StoreAuthDTO storeAuthDTO;


}
