/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dto.store;

import com.leimingtech.commons.tools.validator.group.UpdateGroup;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;
import java.io.Serializable;


/**
 * 修改店铺基本信息
 *
 * @author chengqian
 * @email 543826372@qq.com
 * @since 1.0.0 2019-06-06
 */
@Data
@ApiModel(description = "UpdateStoreBasicDTO")
public class UpdateStoreBasicDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("主键ID")
    private Long id;

    @ApiModelProperty("店铺ID")
    @NotNull(message = "店铺ID不能为空", groups = UpdateGroup.class)
    private Long storeId;

    @ApiModelProperty(value = "店铺名称")
    @NotNull(message = "店铺名称不能为空", groups = UpdateGroup.class)
    @Length(max = 20, message = "店铺名称不能超过20位", groups = UpdateGroup.class)
    private String storeName;

    @ApiModelProperty(value = "店铺logo")
    @NotNull(message = "店铺logo不能为空", groups = UpdateGroup.class)
    private String storeLogo;
    @ApiModelProperty(value = "店铺简介")

    @Length(max = 200, message = "店铺简介不能超过200位", groups = UpdateGroup.class)
    @NotNull(message = "店铺简介不能为空", groups = UpdateGroup.class)
    private String storeIntro;

    @ApiModelProperty(value = "店铺类型")
    @NotNull(message = "店铺类型不能为空", groups = UpdateGroup.class)
    private Integer storeType;

    @ApiModelProperty("店铺联系人")
    @NotNull(message = "店铺联系人不能为空", groups = UpdateGroup.class)
    private String storeLinkman;

    @ApiModelProperty("联系人电话")
    @Length(max = 11, message = "电话号码最长为11位", groups = UpdateGroup.class)
    @NotNull(message = "联系人电话不能为空", groups = UpdateGroup.class)
    private String linkmanPhone;

    @ApiModelProperty(value = "店铺分类ID")
    private Long[] storeClassId;


}