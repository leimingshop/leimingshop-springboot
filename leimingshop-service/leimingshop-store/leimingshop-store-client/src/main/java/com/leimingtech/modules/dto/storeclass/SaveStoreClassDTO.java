/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dto.storeclass;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;


/**
 * 新增店铺分类表
 *
 * @author chengqian
 * @email 543826372@qq.com
 * @since 1.0.0 2019-06-06
 */
@Data
@ApiModel(description = "SaveStoreClassDTO")
public class SaveStoreClassDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "店铺ID")
    private Long storeId;
    @ApiModelProperty(value = "分类ID")
    private Long classId;

}