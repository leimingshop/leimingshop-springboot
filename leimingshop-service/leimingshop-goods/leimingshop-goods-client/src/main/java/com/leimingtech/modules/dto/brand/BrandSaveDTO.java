/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dto.brand;

import com.leimingtech.commons.tools.validator.group.AddGroup;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;


/**
 * @Author: weixianchun
 * @Description: 品牌管理DTO (保存用)
 * @Date :2019/5/20 14:26
 * @Version V1.0
 **/
@Data
@ApiModel(description = "BrandSaveDTO")
public class BrandSaveDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 品牌名称
     */
    @NotBlank(message = "品牌名称不能为空", groups = AddGroup.class)
    @ApiModelProperty(value = "品牌名称")
    private String brandName;
    /**
     * 品牌英文名称
     */
    @ApiModelProperty(value = "品牌英文名称")
    @Length(max = 40, message = "品牌首英文名称长度最大40", groups = AddGroup.class)
    private String brandNameEn;
    /**
     * 品牌首字母
     */
    @Length(max = 1, message = "品牌首字母长度不能超过1", groups = AddGroup.class)
    @ApiModelProperty(value = "品牌首字母")
    private String brandInitials;
    /**
     * 品牌图片
     */
    @NotBlank(message = "品牌图片不能为空", groups = AddGroup.class)
    @ApiModelProperty(value = "品牌图片")
    private String brandPic;
    /**
     * 排序
     */
    @ApiModelProperty(value = "排序")
    private Integer brandSort;
    /**
     * 店铺ID
     */
    @ApiModelProperty(value = "店铺ID")
    private Long storeId;
    /**
     * 品牌申请,0为申请中，1为通过，默认为1，申请功能是会员使用，系统后台默认为1
     */
    @ApiModelProperty(value = "品牌申请，0为申请中，1为通过，默认为1，申请功能是会员使用，系统后台默认为1")
    private Integer brandApply;

}
