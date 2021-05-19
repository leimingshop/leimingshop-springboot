/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dto.topic;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;


/**
 * 专题页
 *
 * @author kuangweiguo kuangweiguo
 * @since v1.0.0 2020-11-19
 */
@Data
@ApiModel(value = "专题页详情")
public class TopicInfoDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    private Long id;
    @ApiModelProperty(value = "专题页名称")
    private String topicName;
    @ApiModelProperty(value = "pc专题页图片")
    private String topicPicturePc;
    @ApiModelProperty(value = "H5专题页图片")
    private String topicPictureH5;
    @ApiModelProperty("专题页商品信息")
    private List<TopicGoodsInfoDTO> goodsDTOList;
    @ApiModelProperty("商品总条数")
    private Integer total;

}