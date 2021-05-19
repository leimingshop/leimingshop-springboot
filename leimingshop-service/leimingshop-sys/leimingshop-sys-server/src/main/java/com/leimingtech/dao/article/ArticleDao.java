/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.dao.article;

import com.leimingtech.commons.mybatis.dao.BaseDao;
import com.leimingtech.dto.article.ArticleDTO;
import com.leimingtech.entity.article.ArticleEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * 文章表
 *
 * @author xuzhch
 * @email x170518@163.com
 * @since 1.0.0 2019-05-20
 */
@Mapper
public interface ArticleDao extends BaseDao<ArticleEntity> {
    /**
     * 分页条件查询
     *
     * @param params
     * @return
     */
    List<ArticleDTO> selectArticleList(Map<String, Object> params);

    /**
     * 根据文章ID查询详情
     *
     * @param id
     * @return
     */
    ArticleDTO selectDetail(Long id);
}