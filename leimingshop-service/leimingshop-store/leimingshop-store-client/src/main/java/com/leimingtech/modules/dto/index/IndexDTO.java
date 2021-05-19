/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dto.index;

import com.leimingtech.modules.dto.store.PageStoreDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;


/**
 * @author chengqian chengqian@leimingtech.com
 * @since v1.0.0 2020-03-16
 */
@Data
@ApiModel(description = "IndexDTO")
public class IndexDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "店铺基本信息")
    private PageStoreDTO storeDTO;
    @ApiModelProperty(value = "用户菜单设置")
    private List<StoreUserFunctionDTO> indexDTO;
    @ApiModelProperty(value = "活动菜单")
    private List<StoreUserFunctionDTO> activityMenu;


}