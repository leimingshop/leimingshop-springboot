/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.moudle.operation.index.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 站点设置
 *
 * @author chengqian
 * @version V1.0
 * @date 2020/5/16 14:37
 **/
@Data
@ApiModel(description = "站点设置")
public class SiteVO implements Serializable {

    @ApiModelProperty(value = "平台名称", required = true)
    @NotNull
    private String name;

    @ApiModelProperty(value = "平台Logo")
    @NotNull
    private String logoNow;

    @NotNull
    @ApiModelProperty(value = "关键词")
    private String key;

    @NotNull
    @ApiModelProperty(value = "描述")
    private String discription;

    @NotNull
    @ApiModelProperty(value = "备案号")
    private String ICP;

    @NotNull
    @ApiModelProperty(value = "版权信息")
    private String copy;

    @NotNull
    @ApiModelProperty(value = "公司地址")
    private String addresss;

    @NotNull
    @ApiModelProperty(value = "联系方式")
    private String link;

    @NotNull
    @ApiModelProperty(value = "网站第三方统计代码")
    private String other;

}
