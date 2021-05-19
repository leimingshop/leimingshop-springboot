/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dto.topic;

import com.leimingtech.commons.tools.validator.group.UpdateGroup;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;


/**
 * 专题页
 *
 * @author kuangweiguo kuangweiguo
 * @since v1.0.0 2020-11-19
 */
@Data
@ApiModel(value = "修改专题页")
public class UpdateTopicDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("主键id")
    @NotNull(message = "主键id不能为空", groups = UpdateGroup.class)
    private Long id;
    @ApiModelProperty(value = "专题页名称")
    @NotNull(message = "专题页名称不能为空", groups = UpdateGroup.class)
    private String topicName;
    @ApiModelProperty(value = "pc专题页图片")
    @NotNull(message = "pc专题页图片不能为空", groups = UpdateGroup.class)
    private String topicPicturePc;
    @ApiModelProperty(value = "H5专题页图片")
    @NotNull(message = "h5专题页图片不能为空", groups = UpdateGroup.class)
    private String topicPictureH5;
    @ApiModelProperty("专题页关联商品id")
    private List<Long> goodsIdList;
}