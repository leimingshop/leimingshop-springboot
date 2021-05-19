/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dto.adv;

import com.leimingtech.commons.tools.validator.group.AddGroup;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Date;


/**
 * 广告实体(新增)
 *
 * @author 刘远杰
 * @email 2634443725@gmail.com
 * @since 7.0 2019-05-13
 */
@Data
@ApiModel(description = "InsertAdvDTO")
public class InsertAdvDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "广告类别ID")
    @NotNull(message = "广告类别ID不能为空", groups = AddGroup.class)
    private Long categoryId;

    @ApiModelProperty(value = "广告标题")
    @NotBlank(message = "广告标题不能为空", groups = AddGroup.class)
    @Size(message = "移动楼层菜单名称长度不能超过20", max = 20, groups = AddGroup.class)
    private String advTitle;

    @ApiModelProperty(value = "广告开始时间（精确到日）")
    private Date startDate;

    @ApiModelProperty(value = "分类idPath")
    private String classId;

    @ApiModelProperty(value = "关联类型（链接、店铺、活动等等）")
    private String relationType;

    @ApiModelProperty(value = "广告结束时间（精确到日）")
    @Future(message = "广告结束时间不能早于当前时间", groups = AddGroup.class)
    private Date endDate;

    @ApiModelProperty(value = "关联目标（链接地址、店铺ID等等）")
    @Size(message = "关联目标长度不能超过1000位", max = 1000, groups = AddGroup.class)
    private String relationTarget;

    @ApiModelProperty(value = "图片地址")
    @NotBlank(message = "图片地址不能为空", groups = AddGroup.class)
    private String imageUrl;

    @ApiModelProperty(value = "排序")
    @NotNull(message = "排序不能为空", groups = AddGroup.class)
    @Max(value = 255, message = "排序为0-255之间的整数", groups = AddGroup.class)
    @Min(value = 0, message = "排序为0-255之间的整数", groups = AddGroup.class)
    private Integer sort;

}
