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
public class CmsAttentionDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
    @ApiModelProperty(value = "主键")
    private Long id;

    /**
     * 用户ID
     */
    @ApiModelProperty(value = "用户ID")
    private Long attentionCreaterId;

    /**
     * 用户ID
     */
    @ApiModelProperty(value = "创建人")
    private String creator;


    /**
     * 被关注ID
     */
    @ApiModelProperty(value = "被关注ID")
    private Long focusedId;

    /**
     * 关注区分标识（1：关注用户；2：关注分类）
     */
    @ApiModelProperty(value = "关注区分标识（1：关注用户；2：关注分类）")
    private Integer attentionFlag;


}
