/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dto.freight.template;

import com.alibaba.fastjson.JSONArray;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * 后台运费模板分页规则实体
 *
 * @author 刘远杰 2634443725@qq.com
 * @since v1.0.0 2020-04-21
 */
@Data
@ApiModel(description = "AdminFreightTemplatePageRuleDTO")
public class AdminFreightTemplatePageRuleDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    private Long id;

    @ApiModelProperty(value = "首件（个）/首重（kg）")
    private String firstFee;

    @ApiModelProperty(value = "首件（重）运费")
    private BigDecimal firstAmount;

    @ApiModelProperty(value = "续件（个）/续重（kg）")
    private String additionalFee;

    @ApiModelProperty(value = "续件（重）运费")
    private BigDecimal additionalAmount;

    @ApiModelProperty(value = "地区id")
    private String areaIds;

    @ApiModelProperty(value = "地区id")
    private List<Long> areaIdsArr;
    @ApiModelProperty(value = "地区描述")
    private String areaDescription;

    public List<Long> getAreaIdsArr() {
        if (StringUtils.isNotBlank(areaIds)) {
            return JSONArray.parseArray(areaIds, Long.class);
        }
        return new ArrayList<>();
    }
}
