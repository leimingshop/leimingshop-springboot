/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.entity.article;

import com.baomidou.mybatisplus.annotation.TableName;
import com.leimingtech.commons.mybatis.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * 文章表
 *
 * @author xuzhch
 * @email x170518@163.com
 * @since 1.0.0 2019-05-20
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("lmshop_article")
public class ArticleEntity extends BaseEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 分类id
     */
    private Long acId;

    /**
     * 跳转链接
     */
    private String articleUrl;

    /**
     * 标题
     */
    private String articleTitle;

    /**
     * 内容
     */
    private String articleContent;

    /**
     * 是否显示（0不显示，默认为1显示）
     */
    private Integer showFlag;

    /**
     * 排序
     */
    private Integer sort;

}