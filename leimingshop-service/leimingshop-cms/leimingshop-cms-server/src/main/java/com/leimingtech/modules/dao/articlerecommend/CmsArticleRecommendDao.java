/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dao.articlerecommend;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.leimingtech.commons.mybatis.dao.BaseDao;
import com.leimingtech.modules.dto.articlerecommend.CmsArticleAddRecommendDTO;
import com.leimingtech.modules.dto.articlerecommend.CmsArticleRecommendDTO;
import com.leimingtech.modules.dto.articlerecommend.CmsFrontArticleRecommendDTO;
import com.leimingtech.modules.entity.articlerecommend.CmsArticleRecommendEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 文章推荐管理 CmsArticleRecommendDao
 *
 * @author yuhaifeng yuhaifeng@leimingtech.com
 * @since v1.0.0 2019-12-31
 */
@Mapper
public interface CmsArticleRecommendDao extends BaseDao<CmsArticleRecommendEntity> {

    /**
     * 资讯相关推荐 相关资讯推荐
     *
     * @param page   分页信息
     * @param params 查询参数
     * @return 返回推荐信息
     */
    List<CmsArticleRecommendDTO> selectCmsRecommendList(Page page, @Param("params") Map<String, Object> params);

    /**
     * 资讯添加推荐列表
     *
     * @param page   分页信息
     * @param params 查询条件
     * @return 返回推荐列表信息
     */
    List<CmsArticleAddRecommendDTO> selectAddRecommendList(Page page, @Param("params") Map<String, Object> params);

    /**
     * 物理删除相关推荐
     *
     * @param ids
     */
    void deleteRecommends(@Param("ids") Long[] ids);

    /**
     * 前台资讯相关推荐
     *
     * @param page   分页信息
     * @param params 查询条件
     * @return 返回相关推荐信息
     */
    List<CmsFrontArticleRecommendDTO> pageFront(Page page, @Param("params") Map<String, Object> params);

}