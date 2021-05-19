/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dao.article;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.leimingtech.commons.mybatis.dao.BaseDao;
import com.leimingtech.modules.dto.article.*;
import com.leimingtech.modules.entity.article.CmsArticleEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 文章管理 CmsArticleDao
 *
 * @author yuhaifeng yuhaifeng@leimingtech.com
 * @since v1.0.0 2019-12-31
 */
@Mapper
public interface CmsArticleDao extends BaseDao<CmsArticleEntity> {

    /**
     * 圈子文章分页条件查询
     *
     * @param params
     * @param page   分页信息
     * @return 返回文章列表
     */
    List<CmsArticleQzListDTO> selectCmsArticleQzList(Page page, @Param("params") Map<String, Object> params);

    /**
     * 资讯文章分页条件查询
     *
     * @param params 查询参数
     * @param page   分页信息
     * @return 返回文章信息
     */
    List<CmsArticleZxListDTO> selectCmsArticleZxList(Page page, @Param("params") Map<String, Object> params);

    /**
     * 服务指南文章分页条件查询
     *
     * @param params 查询条件
     * @param page   分页信息
     * @return 返回文章信息
     */
    List<CmsArticleFwznListDTO> selectCmsArticleFwznList(Page page, @Param("params") Map<String, Object> params);

    /**
     * 圈子后台文章详情
     *
     * @param id
     * @return
     */
    CmsArticleQzAdminInfoDTO selectCmsArticleQzInfo(@Param("id") Long id);

    /**
     * 圈子前台热门文章列表
     *
     * @param params
     * @param page   查询条件
     * @return 返回文章信息
     */
    List<CmsFrontCircleArticleListDTO> selectFrontHotArticleList(Page page, @Param("params") Map<String, Object> params);

    /**
     * 查询我的关注帖子列表
     *
     * @param page   分页信息
     * @param params 查询条件
     * @return 返回关注的帖子信息
     */
    List<CmsFrontCircleArticleListDTO> selectFrontConcernArticleList(Page page, @Param("params") Map<String, Object> params);

    /**
     * 根据分类id查询关联文章名称
     *
     * @param ids 主键id
     * @return 返回文章名称
     */
    List<String> findCountByClassId(@Param("ids") Long[] ids);


    /**
     * 话题下的帖子分页列表（前台）
     *
     * @param params 查询条件
     * @param page   分页条件
     * @return 返回键话题信息
     */
    List<CmsFrontCircleArticleListDTO> selectArticleListByTopicId(Page page, @Param("params") Map<String, Object> params);

    /**
     * 查询文章信息
     *
     * @param page   分页条件
     * @param params 查询参数
     * @return 返回文章信息
     */
    List<CmsFrontCircleArticleListDTO> selectArticleListByMemberId(Page page, @Param("params") Map<String, Object> params);

    /**
     * 查询用户发布文章数
     *
     * @param memberId 用户id
     * @return 返回发布文章的数量够
     */
    Integer selectCountByMemberId(@Param("memberId") Long memberId);

    /**
     * 查询用户发布所有帖子数
     *
     * @param memberId 用户id
     * @return 返回用户发布的帖子数量
     */
    Integer selectArticleCount(@Param("memberId") Long memberId);

    /**
     * 返回用户信息
     *
     * @param params 查询条件
     * @return 返回用户信息
     */
    MemberIndexDTO selectNumByMemberId(@Param("params") Map<String, Object> params);

    /**
     * 查询用户最新发布的帖子信息
     *
     * @param params 查询参数
     * @return 返回用户信息
     */
    MemberIndexDTO selectFirstArticle(@Param("params") Map<String, Object> params);


    /**
     * 修改文章时的文章详情
     *
     * @param id
     * @return
     */
    CmsFrontArticleUpdateDetailDTO selectQzInfoById(@Param("id") Long id);

    /**
     * 查询文章详情
     *
     * @param params 查询参数
     * @return 返回文章详情
     */
    CmsFrontArticleDetailDTO selectArticleById(@Param("params") Map<String, Object> params);

    /**
     * 帖子点赞数更新
     *
     * @param id 主键id
     */
    void updateArticlePraise(@Param("id") Long id);

    /**
     * 帖子取消点赞数更新
     *
     * @param id 主键id
     */
    void cancelArticlePraise(@Param("id") Long id);

    /**
     * 帖子浏览量
     *
     * @param id 主键id
     */
    void updateArticlePvNum(@Param("id") Long id);


    /**
     * 帖子分享数
     *
     * @param id 主键id、
     */
    void updateArticleShareNum(@Param("id") Long id);


    /**
     * 帖子评论 数量更新
     *
     * @param id 主键id
     */
    void updateArticleCommentNum(@Param("id") Long id);

    /***
     * 删除评论 数量更新
     * @param id 主键id
     */
    void cancelArticleCommentNum(@Param("id") Long id);

    /**
     * 删除评论 数量更新（新）
     *
     * @param id
     * @param commentId
     */
    void deleteArticleCommentNum(@Param("id") Long id, Long commentId);

    List<Long> selectArticleCommentIds(@Param("id") Long id);
}
