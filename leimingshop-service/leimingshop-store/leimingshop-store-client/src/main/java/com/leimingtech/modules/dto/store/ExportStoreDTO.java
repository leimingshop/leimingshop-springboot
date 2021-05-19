/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dto.store;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;


/**
 * 店铺表
 *
 * @author chengqian
 * @email 543826372@qq.com
 * @since 1.0.0 2019-06-06
 */
@Data
@ApiModel(description = "ExportStoreDTO")
public class ExportStoreDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键ID")
    private String id;
    @ApiModelProperty(value = "店铺名称")
    private String storeName;
    @ApiModelProperty(value = "管理员账号")
    private String account;
    @ApiModelProperty(value = "店铺等级ID")
    private String gradeId;
    @ApiModelProperty(value = "店铺等级名称")
    private String gradeName;
    @ApiModelProperty(value = "店铺类型")
    private String storeType;
    @ApiModelProperty(value = "状态")
    private String isEnable;
    @ApiModelProperty(value = "创建时间")
    private String createDate;

}