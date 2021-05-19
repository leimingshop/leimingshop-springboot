/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dto.adv;

import com.leimingtech.commons.tools.validator.group.AddGroup;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;


/**
 * pc展示分类图片链接（新增）
 *
 * @author 刘远杰
 * @email 2634443725@qq.com
 * @since 7.0 2019-05-10
 */
@Data
@ApiModel(description = "AdvLinkConfigDTO")
public class AdvLinkConfigDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "标题")
    private String title;

    @ApiModelProperty(value = "广告ID")
    private Long advId;

    @ApiModelProperty(value = "副标题")
    private String subTitle;

    @ApiModelProperty(value = "跳转到不同的页面所需要的字段")
    private String typeKeyWord;

    @ApiModelProperty(value = "图片url")
    @NotBlank(message = "图片url不能为空", groups = AddGroup.class)
    private String imgUrl;

    @NotBlank(message = "链接类型不能为空", groups = AddGroup.class)
    @ApiModelProperty(value = "链接类型：link:链接，searchGoodsByClass:分类查询结构列表界面，searchByKeyWord:按关键字检索，goodsDetail:商品详情，xianshiqiangList:限时抢购列表")
    private String linkType;

    @ApiModelProperty(value = "位置标识(第几张图片)")
    @NotBlank(message = "位置标识不能为空", groups = AddGroup.class)
    private String imgMarking;

    @ApiModelProperty(value = "商品名称")
    private String goodsName;

    @ApiModelProperty(value = "商品类别名称")
    private String gcName;

    @ApiModelProperty(value = "展示类别ids")
    private String classIds;

}