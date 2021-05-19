/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.dto.setting;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @Description 站点设置
 * @Author huangkeyuan
 * @Date 17:07 2019-05-14
 * @Param
 * @return
 */
@Data
@ApiModel(description = "SettingUpdateDTO")
public class SettingUpdateDTO implements Serializable {

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

/*
    @ApiModelProperty(value = "网站简称", required = true)
    @NotNull
    private String shortName;

    @ApiModelProperty(value = "公司名称", required = true)
    @NotNull
    private String companyName;

    @ApiModelProperty(value = "公司简称")
    @NotNull
    private String companyShortName;

    @ApiModelProperty(value = "网站标题", required = true)
    @NotNull
    private String title;

    @ApiModelProperty(value = "关键词")
    private String key;

    @ApiModelProperty(value = "描述")
    private String discription;

    @ApiModelProperty(value = "logoImage")
    private String logoImage;

    @ApiModelProperty(value = "备案号")
    private String ICP;

    @ApiModelProperty(value = "售后时间", required = true)
    @NotNull
    private String returnTime;

    @ApiModelProperty(value = "公司地址")
    private String addresss;

    @ApiModelProperty(value = "联系方式")
    private String link;

    @ApiModelProperty(value = "版权")
    private String copy;

    @ApiModelProperty(value = "网站备案信息代码")
    private String internal;

    @ApiModelProperty(value = "网站第三方统计代码")
    private String other;

    @ApiModelProperty(value = "收货时间(以天为单位)")
    private String time;

*/
}
