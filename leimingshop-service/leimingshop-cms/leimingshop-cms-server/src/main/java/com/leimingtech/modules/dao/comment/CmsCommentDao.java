/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dao.comment;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.leimingtech.commons.mybatis.dao.BaseDao;
import com.leimingtech.modules.dto.comment.CmsCommentDTO;
import com.leimingtech.modules.dto.comment.CmsCommentFrontPageDTO;
import com.leimingtech.modules.entity.comment.CmsCommentEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 评论管理 CmsCommentDao
 *
 * @author yuhaifeng yuhaifeng@leimingtech.com
 * @since v1.0.0 2019-12-31
 */
@Mapper
public interface CmsCommentDao extends BaseDao<CmsCommentEntity> {

    /**
     * 资讯/圈子评论分页查询
     *
     * @param page
     * @param params
     * @return
     */
    List<CmsCommentDTO> selectCommentList(Page page, @Param("params") Map<String, Object> params);

    /**
     * 资讯/圈子前台一级评论分页查询
     *
     * @param page
     * @param params
     * @return
     */
    List<CmsCommentFrontPageDTO> selectCommentFrontFirstList(Page page, @Param("params") Map<String, Object> params);

    /**
     * 一条二级回复
     *
     * @param acCode
     * @param commentParentId
     * @return
     */
    List<CmsCommentFrontPageDTO> selectCommentSecond(@Param("acCode") Integer acCode, @Param("commentParentId") Long commentParentId);

    /**
     * 资讯/圈子前台二级评论分页查询
     *
     * @param page
     * @param params
     * @return
     */
    List<CmsCommentFrontPageDTO> selectCommentFrontSecondList(Page page, @Param("params") Map<String, Object> params);
}