/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dto.attention;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 圈子关注管理DTO
 *
 * @author yuhaifeng yuhaifeng@leimingtech.com
 * @since v1.0.0 2019-12-31
 */
@Data
@ApiModel(value = "圈子关注管理")
public class CmsAttentionListDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    private Long id;

    @ApiModelProperty(value = "用户id")
    private Long attentionCreaterId;

    @ApiModelProperty(value = "被关注ID")
    private Long focusedId;

    @ApiModelProperty(value = "用户头像")
    private String memberAvatar;

    @ApiModelProperty(value = "用户昵称")
    private String nickName;

    @ApiModelProperty(value = "粉丝数")
    private Integer fansNum;

    @ApiModelProperty(value = "帖子数量")
    private Integer articleNum;

    @ApiModelProperty(value = "是否关注（1：已关注 0：未关注）")
    private Integer concernedFlag;

    @ApiModelProperty(value = "最新发布的文章内容")
    private String articleContent;

    @ApiModelProperty(value = "圈子文章发布类型标识（1：文字帖；2：图文帖；3：视频帖；4：文章帖）")
    private Integer articleFlag;
}
