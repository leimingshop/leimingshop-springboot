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
 * 系统文章表
 *
 * @author xuzhch
 * @email x170518@163.com
 * @since 1.0.0 2019-05-20
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("lmshop_document")
public class DocumentEntity extends BaseEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 文章分类ID
     */
    private Long acId;

    /**
     * 标识码
     */
    private String docCode;

    /**
     * 标题
     */
    private String docTitle;

    /**
     * 内容
     */
    private String docContent;

    /**
     * 排序字段
     */
    private Integer sort;

}