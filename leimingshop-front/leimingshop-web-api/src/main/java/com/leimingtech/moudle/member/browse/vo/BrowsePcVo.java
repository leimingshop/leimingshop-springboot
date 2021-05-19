/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.moudle.member.browse.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 分页查询
 *
 * @author chengqian
 * @email 543826372@qq.com
 * @since 1.0.0 2019-06-03
 */
@Data
@ApiModel(description = "FindGoodsBrowseDTO")
public class BrowsePcVo implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "浏览时间")
    private String browseTime;
    @ApiModelProperty(value = "当天时间下记录")
    private List<BrowseGoodsPcVo> goodsBrowseDTOList;


}