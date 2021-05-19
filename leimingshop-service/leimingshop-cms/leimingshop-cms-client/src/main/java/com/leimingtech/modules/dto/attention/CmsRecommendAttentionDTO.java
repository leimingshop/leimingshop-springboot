/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dto.attention;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 推荐关注用户列表DTO
 *
 * @author yuhaifeng yuhaifeng@leimingtech.com
 * @since v1.0.0 2019-12-31
 */
@Data
@ApiModel(value = "推荐关注用户列表DTO")
public class CmsRecommendAttentionDTO implements Serializable {
    private static final long serialVersionUID = 1L;


    /**
     * 用户ID
     */
    @ApiModelProperty(value = "用户ID")
    private Long attentionCreaterId;

    /**
     * 被关注用户ID
     */
    @ApiModelProperty(value = "被关注用户ID")
    private Long focusedId;

    /**
     * 关注区分标识（1：关注用户；2：关注分类）
     */
    @ApiModelProperty(value = "关注区分标识（1：关注用户；2：关注分类）")
    private Integer attentionFlag;


    /**
     * 用户头像
     */
    @ApiModelProperty(value = "用户头像")
    private String memberAvatar;

    /**
     * 昵称
     */
    @ApiModelProperty(value = "昵称")
    private String nickName;

    /**
     * 帖子发布数量
     */
    @ApiModelProperty(value = "帖子发布数量")
    private Integer articleNum;

    /**
     * 粉丝数
     */
    @ApiModelProperty(value = "粉丝数")
    private Integer fansNum;

    @ApiModelProperty(value = "是否关注（1：已关注 0：未关注）")
    private Integer concernedFlag;

    @ApiModelProperty(value = "最新发布的文章内容")
    private String articleContent;

    @ApiModelProperty(value = "圈子文章发布类型标识（1：文字帖；2：图文帖；3：视频帖；4：文章帖）")
    private Integer articleFlag;
}
