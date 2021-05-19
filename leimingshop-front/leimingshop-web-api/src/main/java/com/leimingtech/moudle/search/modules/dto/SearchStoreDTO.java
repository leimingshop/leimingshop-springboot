/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.moudle.search.modules.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author chenqgian
 * @version 1.0
 * @date 2019/12/5 2:46 PM
 */
@ApiModel(description = "SearchStoreDTO")
@Data
public class SearchStoreDTO implements Serializable {

    private static final long serialVersionUID = -2329345733029038572L;

    @ApiModelProperty("搜索关键字:saleNum 销量")
    private String keyword;

    @ApiModelProperty(value = "排序字段")
    private String sortField;

    @ApiModelProperty(value = "排序类型：DESC倒叙，ASC正序", required = true)
    private String sortType;

    @ApiModelProperty(value = "每页显示条数", required = true)
    private Integer pageSize;

    @ApiModelProperty(value = "第几页", required = true)
    private Integer pageNo;
}
