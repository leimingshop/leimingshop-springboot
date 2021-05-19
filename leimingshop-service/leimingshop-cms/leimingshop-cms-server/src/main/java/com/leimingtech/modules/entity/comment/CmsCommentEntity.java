/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.entity.comment;

import com.baomidou.mybatisplus.annotation.TableName;
import com.leimingtech.commons.mybatis.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;


/**
 * 评论管理 CmsCommentEntity
 *
 * @author yuhaifeng yuhaifeng@leimingtech.com
 * @since v1.0.0 2019-12-31
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("lmshop_cms_comment")
public class CmsCommentEntity extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 被评论文章ID
     */
    private Long articleId;
    /**
     * 评论内容
     */
    private String commentContent;
    /**
     * 评论图片url
     */
    private String commentPicture;
    /**
     * 评论人ID
     */
    private Long commentCreaterId;
    /**
     * 被评论人ID
     */
    private Long commentedId;
    /**
     * 被评论的评论ID（0为一级评论）
     */
    private Long commentParentId;
    /**
     * 评论一级ID（0为一级评论）
     */
    private Long commentFirstId;
    /**
     * 评论层级
     */
    private Integer commentLevel;
    /**
     * 分类区分标识（1：圈子2：资讯）
     */
    private Integer acCode;
    /**
     * 状态标识（0：屏蔽 1：展示（默认））
     */
    private Integer status;
    /**
     * 圈人的json数组[{"name":"","id":""}]
     */
    private String remind;
}