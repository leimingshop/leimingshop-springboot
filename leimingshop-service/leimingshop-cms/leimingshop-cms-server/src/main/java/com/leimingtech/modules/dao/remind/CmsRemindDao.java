/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dao.remind;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.leimingtech.commons.mybatis.dao.BaseDao;
import com.leimingtech.modules.dto.article.CmsFrontCircleArticleListDTO;
import com.leimingtech.modules.entity.remind.CmsRemindEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @author hecheng 347861695@qq.com
 * @since v1.0.0 2020-04-08
 */
@Mapper
public interface CmsRemindDao extends BaseDao<CmsRemindEntity> {


    /**
     * 查询在发布文章中艾特我的数据
     *
     * @param page
     * @param params
     * @return
     */
    List<CmsFrontCircleArticleListDTO> selectArticleList(Page page, @Param("params") Map<String, Object> params);

    /**
     * 查询在评论中艾特我的数据
     *
     * @param page
     * @param params
     * @return
     */
    List<CmsFrontCircleArticleListDTO> selectCommentList(Page page, @Param("params") Map<String, Object> params);

    /**
     * 查询文章和评论中@我的数据
     *
     * @param page
     * @param params
     * @return
     */
    List<CmsFrontCircleArticleListDTO> selectAllList(Page page, @Param("params") Map<String, Object> params);

    /**
     * 根据文章id 查询评论中艾特我的数据记录id
     *
     * @param params
     * @return
     */
    List<Map<String, Object>> commentGetIdList(@Param("params") Map<String, Object> params);

    /**
     * 根据文章id 查询文章中艾特我的数据记录id
     *
     * @param params
     * @return
     */
    List<Map<String, Object>> articleGetIdList(@Param("params") Map<String, Object> params);

    /**
     * 根据id逻辑删除
     *
     * @param id
     * @return
     */
    int updateDelById(@Param("id") Long id);

}