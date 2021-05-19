/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dto.remind;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;


/**
 * @author hecheng 347861695@qq.com
 * @since v1.0.0 2020-04-08
 */
@Data
@ApiModel(value = "")
public class CmsRemindDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    private Long id;
    @ApiModelProperty(value = "@人的id")
    private Long creatorId;
    @ApiModelProperty(value = "被@人id")
    private Long memberId;
    @ApiModelProperty(value = "@类型(1图文2评论)")
    private Integer remindType;
    @ApiModelProperty(value = "图文id,评论id")
    private Long remindTypeId;
    @ApiModelProperty(value = "创建者(后台获取)")
    private String creator;
    @ApiModelProperty(value = "更新人(后台获取)")
    private String updater;
}