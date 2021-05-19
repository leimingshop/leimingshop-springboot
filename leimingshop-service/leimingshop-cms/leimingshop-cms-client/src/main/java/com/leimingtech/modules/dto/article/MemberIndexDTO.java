/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dto.article;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author pixiaoyong@leimingtech.com
 * @date 2020/4/2 9:50
 * @email 1609973595@qq.com
 */
@Data
public class MemberIndexDTO {

    @ApiModelProperty(value = "昵称")
    private String nickName;

    @ApiModelProperty(value = "用户头像")
    private String memberAvatar;

    @ApiModelProperty(value = "文章作者id")
    private Long articleCreaterId;

    @ApiModelProperty(value = "关注标识（1：已关注 0：未关注）")
    private Integer attentionFlag;

    @ApiModelProperty(value = "粉丝数")
    private Integer fansNum;

    @ApiModelProperty(value = "关注数")
    private Integer attentionNum;

    @ApiModelProperty(value = "关注数")
    private Integer articleNum;

    @ApiModelProperty(value = "是否关注（1：已关注 0：未关注）")
    private Integer concernedFlag;

    @ApiModelProperty(value = "是否为当前登录用户（1：当前登录用户 0：其他用户）")
    private Integer loginFlag;

    @ApiModelProperty(value = "艾特我的未读")
    private Integer remindNum;

    @ApiModelProperty(value = "最新发布的文章内容")
    private String articleContent;

    @ApiModelProperty(value = "圈子文章发布类型标识（1：文字帖；2：图文帖；3：视频帖；4：文章帖）")
    private Integer articleFlag;
}
