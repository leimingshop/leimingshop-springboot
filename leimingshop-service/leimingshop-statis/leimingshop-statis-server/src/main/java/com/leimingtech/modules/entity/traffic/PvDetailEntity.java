/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.entity.traffic;


import com.leimingtech.modules.constants.CollectionName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

/**
 * PV统计保存对象
 *
 * @author xuzhch
 * @date 2020年9月17日
 */
@Data
@Document(collection = CollectionName.PV_DETAIL)
public class PvDetailEntity {
    @ApiModelProperty(value = "主键ID")
    private Long id;

    @ApiModelProperty(value = "商品ID")
    private Long goodsId;

    @ApiModelProperty(value = "店铺ID")
    private Long storeId;

    @ApiModelProperty(value = "访问IP")
    private String ipDetail;

    @ApiModelProperty(value = "浏览平台（1:H5、2:微信、3:APP、4:PC、5:其他）")
    private Integer browsePlatforms;

    @ApiModelProperty(value = "页面类型（1:首页、2:商品分类页、3:购物车、4:商品详情页、5:其他）")
    private Integer pageType;

    @ApiModelProperty(value = "浏览器")
    private String browseType;

    @ApiModelProperty(value = "操作系统")
    private String operatingSystem;

    @ApiModelProperty(value = "创建时间（精确到时）")
    private Date createHourTime;

    @ApiModelProperty(value = "创建时间（精确到日）")
    private Date createDayTime;

    @ApiModelProperty(value = "创建时间（精确到月）")
    private Date createMonthTime;

    @ApiModelProperty(value = "创建时间（精确到秒）")
    private Date createTime;
}
