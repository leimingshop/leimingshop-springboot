/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dto.browse;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 浏览记录
 *
 * @author 余海锋
 * @since 1.0.0 2020-04-14
 */
@Data
public class BrowseCmsDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    private Long goodsId;
    @ApiModelProperty(value = "更新时间")
    private Date updateDate;
}
