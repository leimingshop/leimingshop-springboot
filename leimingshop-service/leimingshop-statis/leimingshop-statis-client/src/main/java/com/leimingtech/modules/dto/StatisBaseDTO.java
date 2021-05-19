/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;
import java.util.Map;


/**
 * @Author: SWH ab4856812@163.com
 * @Description: 统计echart图基础DTO
 * @Date: 2019/7/15 15:22
 * @Version: V1.0
 */
@Data
@ApiModel(description = "StatisBaseDTO")
public class StatisBaseDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "数据描述（标签）")
    private List<String> columns;

    @ApiModelProperty(value = "数据")
    private List<Map<String, Object>> rows;
}