/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.moudle.goods.goods.vo;

import com.leimingtech.commons.tools.utils.TreeNode;
import com.leimingtech.moudle.operation.index.vo.AdvVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;


/**
 * 店铺商品分类表
 *
 * @author chengqian chengqian@leimingtech.com
 * @since v1.0.0 2020-05-12
 */
@Data
@ApiModel(value = "店铺商品分类表")
public class StoreGoodsClassListVO extends TreeNode implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键ID")
    private Long id;
    @ApiModelProperty(value = "店铺ID")
    private Long storeId;
    @ApiModelProperty(value = "店铺商品分类名称")
    private String gcName;
    @ApiModelProperty(value = "父ID")
    private Long gcParentId;
    @ApiModelProperty(value = "展示类目图片")
    private String gcPic;
    @ApiModelProperty(value = "前台展示（0不展示，默认为1展示）")
    private Integer showFlag;
    @ApiModelProperty(value = "排序（0-255内的整数，数值越小排序越靠前）")
    private Integer sort;
    @ApiModelProperty(value = "分类下商品数量")
    private Integer num;
    @ApiModelProperty("创建时间")
    private Date createDate;

    @ApiModelProperty("分类下广告")
    private List<AdvVO> list;

    @Override
    public Long getPid() {
        return gcParentId;
    }
}