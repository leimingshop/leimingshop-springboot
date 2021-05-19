/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.dao.article;

import com.leimingtech.commons.mybatis.dao.BaseDao;
import com.leimingtech.entity.article.ArticleClassEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * 文章分类表
 *
 * @author xuzhch
 * @email x170518@163.com
 * @since 1.0.0 2019-05-20
 */
@Mapper
public interface ArticleClassDao extends BaseDao<ArticleClassEntity> {

}