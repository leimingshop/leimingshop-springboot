/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dto.traffic;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 页面流量来源统计
 *
 * @author xuzhch
 * @date 2020/3/13/013
 */
@Data
@ApiModel(description = "PageSourceStatisticDTO")
public class PageSourceStatisticDTO {
    @ApiModelProperty(value = "所有浏览量")
    private Integer pvAllCount;
    @ApiModelProperty(value = "PC浏览量")
    private Integer pvPcCount;
    @ApiModelProperty(value = "微信浏览量")
    private Integer pvWeChatCount;
    @ApiModelProperty(value = "安卓浏览量")
    private Integer pvAndroidCount;
    @ApiModelProperty(value = "IOS浏览量")
    private Integer pvIosCount;
    @ApiModelProperty(value = "其他来源浏览量")
    private Integer pvOtherCount;
    @ApiModelProperty(value = "小程序浏览量")
    private Integer pvAppletsCount;
    @ApiModelProperty(value = "PC浏览量占比")
    private Double pvPcProportion;
    @ApiModelProperty(value = "微信浏览量占比")
    private Double pvWeChatProportion;
    @ApiModelProperty(value = "安卓浏览量占比")
    private Double pvAndroidProportion;
    @ApiModelProperty(value = "IOS浏览量占比")
    private Double pvIosProportion;
    @ApiModelProperty(value = "其他来源浏览量占比")
    private Double pvOtherProportion;
    @ApiModelProperty(value = "小程序浏览量占比")
    private Double pvAppletsProportion;

    @ApiModelProperty(value = "所有访问量数量")
    private Integer uvAllCount;
    @ApiModelProperty(value = "PC访问量")
    private Integer uvPcCount;
    @ApiModelProperty(value = "微信访问量")
    private Integer uvWeChatCount;
    @ApiModelProperty(value = "安卓访问量")
    private Integer uvAndroidCount;
    @ApiModelProperty(value = "IOS访问量")
    private Integer uvIosCount;
    @ApiModelProperty(value = "其他来源访问量")
    private Integer uvOtherCount;
    @ApiModelProperty(value = "小程序访问量")
    private Integer uvAppletsCount;
    @ApiModelProperty(value = "PC访问量占比")
    private Double uvPcProportion;
    @ApiModelProperty(value = "微信访问量占比")
    private Double uvWeChatProportion;
    @ApiModelProperty(value = "安卓访问量占比")
    private Double uvAndroidProportion;
    @ApiModelProperty(value = "IOS访问量占比")
    private Double uvIosProportion;
    @ApiModelProperty(value = "其他来源访问量占比")
    private Double uvOtherProportion;
    @ApiModelProperty(value = "小程序访问量占比")
    private Double uvAppletsProportion;

    public static PageSourceStatisticDTO newPageSourceStatisticDTO() {
        PageSourceStatisticDTO pageSourceStatisticDTO = new PageSourceStatisticDTO();
        pageSourceStatisticDTO.setPvAllCount(0);
        pageSourceStatisticDTO.setPvPcCount(0);
        pageSourceStatisticDTO.setPvWeChatCount(0);
        pageSourceStatisticDTO.setPvAndroidCount(0);
        pageSourceStatisticDTO.setPvIosCount(0);
        pageSourceStatisticDTO.setPvOtherCount(0);
        pageSourceStatisticDTO.setPvAppletsCount(0);
        pageSourceStatisticDTO.setUvAllCount(0);
        pageSourceStatisticDTO.setUvPcCount(0);
        pageSourceStatisticDTO.setUvWeChatCount(0);
        pageSourceStatisticDTO.setUvAndroidCount(0);
        pageSourceStatisticDTO.setUvIosCount(0);
        pageSourceStatisticDTO.setUvOtherCount(0);
        pageSourceStatisticDTO.setUvAppletsCount(0);
        pageSourceStatisticDTO.setPvPcProportion(0.0);
        pageSourceStatisticDTO.setPvWeChatProportion(0.0);
        pageSourceStatisticDTO.setPvAndroidProportion(0.0);
        pageSourceStatisticDTO.setPvIosProportion(0.0);
        pageSourceStatisticDTO.setPvOtherProportion(0.0);
        pageSourceStatisticDTO.setPvAppletsProportion(0.0);
        pageSourceStatisticDTO.setUvPcProportion(0.0);
        pageSourceStatisticDTO.setUvWeChatProportion(0.0);
        pageSourceStatisticDTO.setUvAndroidProportion(0.0);
        pageSourceStatisticDTO.setUvIosProportion(0.0);
        pageSourceStatisticDTO.setUvOtherProportion(0.0);
        pageSourceStatisticDTO.setUvAppletsProportion(0.0);
        return pageSourceStatisticDTO;
    }
}
