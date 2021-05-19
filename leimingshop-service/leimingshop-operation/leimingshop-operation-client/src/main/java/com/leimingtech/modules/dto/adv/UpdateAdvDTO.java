/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dto.adv;

import com.leimingtech.commons.tools.validator.group.UpdateGroup;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;


/**
 * 广告实体(修改)
 *
 * @author 刘远杰
 * @email 2634443725@gmail.com
 * @since 7.0 2019-05-13
 */
@Data
@ApiModel(description = "UpdateAdvDTO")
public class UpdateAdvDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    @NotNull(message = "{id.require}", groups = UpdateGroup.class)
    private Long id;

    @ApiModelProperty(value = "广告标题")
    @NotBlank(message = "广告标题不能为空", groups = UpdateGroup.class)
    @Size(message = "移动楼层菜单名称长度不能超过20", max = 20, groups = UpdateGroup.class)
    private String advTitle;

    @ApiModelProperty(value = "广告开始时间（精确到日）")
    private Date startDate;

    @ApiModelProperty(value = "广告结束时间（精确到日）")
    private Date endDate;

    @ApiModelProperty(value = "关联目标（链接地址、店铺ID等等）")
    @Size(message = "关联目标长度不能超过1000位", max = 1000, groups = UpdateGroup.class)
    private String relationTarget;
    @ApiModelProperty(value = "关联类型（链接、店铺、活动等等）")
    private String relationType;

    @ApiModelProperty(value = "分类idPath")
    private String classId;

    @ApiModelProperty(value = "图片地址")
    private String imageUrl;

    @ApiModelProperty(value = "排序")
    @NotNull(message = "排序不能为空", groups = UpdateGroup.class)
    @Max(value = 255, message = "排序优先级为0-255之间的整数", groups = UpdateGroup.class)
    @Min(value = 0, message = "排序优先级为0-255之间的整数", groups = UpdateGroup.class)
    private Integer sort;
    @ApiModelProperty(value = "pc展示分类广告配置")
    private List<AdvLinkConfigDTO> advLinkConfigDTOList;

}
