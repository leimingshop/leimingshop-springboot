/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dao.attention;

import com.leimingtech.commons.mybatis.dao.BaseDao;
import com.leimingtech.modules.dto.attention.*;
import com.leimingtech.modules.entity.attention.CmsAttentionEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 圈子关注管理 CmsAttentionDao
 *
 * @author yuhaifeng yuhaifeng@leimingtech.com
 * @since v1.0.0 2019-12-31
 */
@Mapper
public interface CmsAttentionDao extends BaseDao<CmsAttentionEntity> {
    /**
     * 查询用户关注帖子信息
     *
     * @param id 主键id
     * @return 返回被关注的帖子信息
     */
    List<CmsMemberFocusDTO> getFocusList(@Param("id") Long id);

    /**
     * 查询当前关注帖子的用户
     *
     * @param id 主键id
     * @return 返回用户信息
     */
    List<CmsFocusMemberDTO> getFocusMemberList(@Param("id") Long id);


    /**
     * 推荐关注用户列表
     *
     * @param memberId
     * @return
     */
    List<CmsRecommendAttentionDTO> getRecommendUser(@Param("memberId") Long memberId);

    /***
     * 查询用户关注数
     * @param memberId 用户id
     * @return 返回关注数量
     */
    Integer selectAttentionNum(@Param("memberId") Long memberId);

    /**
     * 查询用户粉丝数
     *
     * @param memberId 用户id
     * @return 返回用户用户粉丝数量
     */
    Integer selectFansNum(@Param("memberId") Long memberId);

    /**
     * 查询用户是否被关注
     *
     * @param memberId      用户ID
     * @param loginMemberId 登陆id
     * @return 返回是否被关注
     */
    Integer concernedFlag(@Param("memberId") Long memberId, @Param("loginMemberId") Long loginMemberId);

    /**
     * 查询我关注的用户列表
     *
     * @param userId 用户id
     * @return 返回被关注的用户信息
     */
    List<CmsAttentionListDTO> selectAttentionUserList(@Param("userId") Long userId);

    /**
     * 查询我关注的分类列表
     *
     * @param userId 用户id+
     * @return 返回关注的分类信息
     */
    List<CmsAttentionClassListDTO> selectAttentionClassList(@Param("userId") Long userId);

    /**
     * 删除信息
     *
     * @param id 主键id
     */
    void deleteAttention(@Param("id") Long id);

    /**
     * 查询粉丝列表
     *
     * @param memberId 用户id
     * @return 返回粉丝列表
     */
    List<CmsRecommendAttentionDTO> selectFansList(@Param("memberId") Long memberId);

}
