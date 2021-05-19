/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dto.custom;

import com.leimingtech.commons.tools.utils.TreeNode;
import com.leimingtech.modules.dto.adv.AdvDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;


/**
 * 商品自定义分类表
 *
 * @author xuzhch
 * @email 170518@163.dao
 * @since 1.0.0 2019-05-22
 */
@Data
@ApiModel(description = "GoodsClassCustomListDTO")
public class GoodsClassCustomListDTO extends TreeNode implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "ID")
    private Long id;

    @ApiModelProperty(value = "父ID,一级分类默认为0")
    private Long gcParentId;

    @ApiModelProperty(value = "父ID,一级分类默认为0")
    private Long pid;

    @ApiModelProperty(value = "展示类目名称")
    private String gcName;

    @ApiModelProperty(value = "展示类目图片")
    private String gcPic;

    @ApiModelProperty(value = "展示类目url")
    private String gcUrl;

    @ApiModelProperty(value = "关联商品分类id")
    private Long classId;

    @ApiModelProperty(value = "层级path。格式：1,2,3")
    private String idPath;

    @ApiModelProperty(value = "前台展示（0不展示，默认为1展示）")
    private Integer showFlag;

    @ApiModelProperty(value = "创建时间")
    private Date createDate;

    @ApiModelProperty(value = "排序")
    private Integer sort;

    @ApiModelProperty(value = "子级分类")
    private List<GoodsClassCustomListDTO> goodsClassCustomListDTO;

    @ApiModelProperty(value = "H5广告位")
    private List<AdvDTO> advDTOList;

    @ApiModelProperty(value = "PC广告位")
    private AdvDTO advDTO;

    @ApiModelProperty(value = "当前分类关联的商品数量")
    private Integer goodsNum;

    @ApiModelProperty(value = "商品分类名称")
    private String className;


}