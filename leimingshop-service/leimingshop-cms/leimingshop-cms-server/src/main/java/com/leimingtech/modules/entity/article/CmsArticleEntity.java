/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.entity.article;

import com.baomidou.mybatisplus.annotation.TableName;
import com.leimingtech.commons.mybatis.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;


/**
 * 文章管理 CmsArticleEntity
 *
 * @author yuhaifeng yuhaifeng@leimingtech.com
 * @since v1.0.0 2019-12-31
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("lmshop_cms_article")
public class CmsArticleEntity extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 分类ID
     */
    private Long acId;
    /**
     * 话题ID
     */
    private Long topicId;
    /**
     * 文章标题
     */
    private String articleTitle;
    /**
     * 文章短标题
     */
    private String articleShortTitle;
    /**
     * 文章创建人ID
     */
    private Long articleCreaterId;
    /**
     * 文章修改人ID
     */
    private Long articleUpdateId;
    /**
     * 文章所属
     */
    private String articleBelong;
    /**
     * 文章作者
     */
    private String author;
    /**
     * 文章来源
     */
    private String articleSource;
    /**
     * URL路径
     */
    private String articleUrl;
    /**
     * 文章图片URL
     */
    private String articlePicture;
    /**
     * 文章内容
     */
    private String articleContent;
    /**
     * 文章摘要
     */
    private String articleAbstract;
    /**
     * 排序
     */
    private Integer sort;
    /**
     * 点赞数
     */
    private Integer articlePraiseNum;
    /**
     * 评论数
     */
    private Integer articleCommentNum;
    /**
     * 浏览数
     */
    private Integer pvNum;
    /**
     * 分享数
     */
    private Integer shareNum;
    /**
     * 置顶标识（0：未置顶（默认），1：已置顶）
     */
    private Integer topFlag;
    /**
     * 头条标识（0：未头条（默认），1：已头条）
     */
    private Integer headFlag;
    /**
     * 精华帖标识（0：非精华帖（默认），1：精华帖）
     */
    private Integer essenceFlag;
    /**
     * 状态标识（0：停用 1：启用（默认））
     */
    private Integer status;
    /**
     * 评论标识（0：停用 1：启用（默认））
     */
    private Integer commentFlag;
    /**
     * 审核标识（0：未审核（默认），1：审核通过，2：审核驳回）
     */
    private Integer audit;
    /**
     * 审核时间
     */
    private Date auditDate;
    /**
     * 文章区分标识（1：圈子2：资讯3：服务指南）
     */
    private Integer acCode;

    /**
     * 视频URL
     */
    private String videoUrl;

    /**
     * 圈子文章发布类型标识（1：文字帖；2：图文帖；3：视频帖；4：文章帖）
     */
    private Integer articleFlag;

    /**
     * 稿件封面图
     */
    private String indexImage;

    /**
     * 位置
     */
    private String location;

    /**
     * 关联话题名称集合
     */
    private String topicName;

    /**
     * @的json数组[{"name":"","id":""}]
     */
    private String remind;
}
