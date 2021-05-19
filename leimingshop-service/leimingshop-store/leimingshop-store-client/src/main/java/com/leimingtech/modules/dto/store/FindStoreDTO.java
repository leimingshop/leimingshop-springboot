/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dto.store;

import com.leimingtech.commons.tools.validator.group.AddGroup;
import com.leimingtech.commons.tools.validator.group.UpdateGroup;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;
import java.io.Serializable;


/**
 * 查询店铺表
 *
 * @author chengqian
 * @email 543826372@qq.com
 * @since 1.0.0 2019-06-06
 */
@Data
@ApiModel(description = "FindStoreDTO")
public class FindStoreDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    private Long id;
    @ApiModelProperty(value = "店铺名称")
    @Length(max = 20, message = "店铺名称不能超过20位", groups = AddGroup.class)
    private String storeName;
    @ApiModelProperty(value = "店铺认证（默认0未认证，1已认证）")
    private Integer storeAuth;
    @ApiModelProperty(value = "店铺等级ID")
    private Long gradeId;
    @ApiModelProperty(value = "地区id")
    private Long areaId;
    @ApiModelProperty(value = "店铺所在地（省）")
    private Long provinceId;
    @ApiModelProperty(value = "店铺坐在市id")
    private Long cityId;
    @ApiModelProperty(value = "地区内容（3级地址组合）")
    private String areaInfo;
    @ApiModelProperty(value = "地址（详细地址）")
    private String storeAddress;
    @ApiModelProperty(value = "店铺logo")
    private String storeLogo;
    @ApiModelProperty(value = "店铺类型（1:自营商户，2:普通商户）")
    private Integer storeType;
    @ApiModelProperty(value = "店铺简介")
    @Length(max = 200, message = "店铺简介不能超过200位", groups = UpdateGroup.class)
    private String storeIntro;
    @ApiModelProperty("店铺联系人")
    @NotNull(message = "店铺联系人不能为空", groups = UpdateGroup.class)
    private String storeLinkman;

    @ApiModelProperty("联系人电话")
    @Length(max = 11, message = "电话号码最长为11位", groups = UpdateGroup.class)
    @NotNull(message = "联系人电话不能为空", groups = UpdateGroup.class)
    private String linkmanPhone;

}