/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dto.remind;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 文章
 *
 * @author chengqian
 */
@Data
@ApiModel(value = "")
public class CmsRemindMqDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "@人的id")
    private Long creatorId;
    @ApiModelProperty(value = "@类型(1图文2评论)")
    private Integer remindType;
    @ApiModelProperty(value = "图文id,评论id")
    private Long remindTypeId;
    @ApiModelProperty(value = "创建者(后台获取)")
    private String creator;
    /**
     * @的人json数组[{"name":"","id":""}]
     */
    private String memberIdList;
}
