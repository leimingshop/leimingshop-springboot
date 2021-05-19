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
 * 文章分类表
 *
 * @author xuzhch
 * @email x170518@163.com
 * @since 1.0.0 2019-05-20
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("lmshop_article_class")
public class ArticleClassEntity extends BaseEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 分类标识码
     */
    private String acCode;

    /**
     * 分类名称
     */
    private String acName;

    /**
     * 父ID
     */
    private Long acParentId;

    /**
     * 分类状态（0停用，默认为1启用）
     */
    private Integer status;

    /**
     * 排序
     */
    private Integer sort;

}