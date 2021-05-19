/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.entity.articlerecommend;

import com.baomidou.mybatisplus.annotation.TableName;
import com.leimingtech.commons.mybatis.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;


/**
 * 文章推荐管理 CmsArticleRecommendEntity
 *
 * @author yuhaifeng yuhaifeng@leimingtech.com
 * @since v1.0.0 2019-12-31
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("lmshop_cms_article_recommend")
public class CmsArticleRecommendEntity extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 主文章ID
     */
    private Long mainArticleId;
    /**
     * 被推荐文章ID
     */
    private Long recommendArticleId;

}