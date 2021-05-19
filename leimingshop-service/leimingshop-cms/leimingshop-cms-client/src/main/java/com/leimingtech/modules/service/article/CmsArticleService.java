/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.service.article;


import com.leimingtech.commons.tools.page.PageData;
import com.leimingtech.modules.dto.article.*;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

/**
 * 文章管理CmsArticleService
 *
 * @author yuhaifeng yuhaifeng@leimingtech.com
 * @since v1.0.0 2019-12-31
 */

public interface CmsArticleService {

    /**
     * 圈子文章分页查询
     *
     * @param params
     * @return
     */

    PageData<CmsArticleQzListDTO> pageQz(@RequestParam Map<String, Object> params);

    /**
     * 资讯文章分页查询
     *
     * @param params 查询细腻
     * @return 返回文章分页信息
     */

    PageData<CmsArticleZxListDTO> pageZx(@RequestParam Map<String, Object> params);

    /**
     * 服务指南文章分页查询
     *
     * @param params
     * @return
     */

    PageData<CmsArticleFwznListDTO> pageFwzn(@RequestParam Map<String, Object> params);

    /**
     * 圈子文章新增
     *
     * @param dto
     */

    void saveQz(@RequestBody CmsArticleQzSaveDTO dto);

    /**
     * 资讯文章新增
     *
     * @param dto
     */

    void saveZx(@RequestBody CmsArticleZxSaveDTO dto);

    /**
     * 服务指南文章新增
     *
     * @param dto
     */

    void saveFwzn(@RequestBody CmsArticleFwznSaveDTO dto);

    /**
     * 圈子文章修改
     *
     * @param dto
     */

    void updateQz(@RequestBody CmsArticleQzUpdateDTO dto);

    /**
     * 资讯文章修改
     *
     * @param dto
     */

    void updateZx(@RequestBody CmsArticleZxUpdateDTO dto);

    /**
     * 服务指南文章修改
     *
     * @param dto
     */

    void updateFwzn(@RequestBody CmsArticleFwznUpdateDTO dto);

    /**
     * 文章逻辑删除
     *
     * @param ids
     */

    void delete(@RequestBody Long[] ids);

    /**
     * 圈子文章详情
     *
     * @param id
     * @return
     */

    CmsArticleQzAdminInfoDTO getQzArticleAdmin(Long id);

    /**
     * 资讯文章详情
     *
     * @param id
     * @return
     */

    CmsArticleZxSaveDTO getZxArticleAdmin(Long id);

    /**
     * 服务指南文章详情
     *
     * @param id
     * @return
     */

    CmsArticleFwznSaveDTO getFwznArticleAdmin(Long id);

    /**
     * 圈子文章详情
     *
     * @param params
     * @return
     */

    CmsFrontArticleDetailDTO getFrontArticleDetail(@RequestParam Map<String, Object> params);

    /**
     * 资讯文章详情
     *
     * @param id
     * @return
     */

    CmsArticleZxFrontDTO getZxArticleFront(Long id);

    /**
     * 服务指南
     *
     * @param id
     * @return
     */

    CmsArticleFwznFrontDTO getFwznArticleFront(Long id);

    /**
     * 圈子文章前台列表
     *
     * @param params
     * @return
     */

    PageData<CmsFrontCircleArticleListDTO> frontArticleList(@RequestParam Map<String, Object> params);

    /**
     * 文章状态修改
     *
     * @param dto
     */

    void statusUpdate(@RequestBody CmsArticleStatusUpdateDTO dto);

    /**
     * H5圈子文章发布
     *
     * @param dto 保存对象
     */

    void frontArticlePublish(@RequestBody CmsFrontArticlePublishDTO dto);

    /**
     * H5圈子文章修改
     *
     * @param dto
     */

    void frontArticleUpdate(@RequestBody CmsFrontArticleUpdateDTO dto);

    /**
     * H5帖子修改回显详情
     *
     * @param id
     * @return
     */

    CmsFrontArticleUpdateDetailDTO getQzArticleFrontInfo(Long id);

    /**
     * 话题下的帖子分页列表（前台）
     *
     * @param params 查询参数
     * @return 返回帖子信息
     */

    PageData<CmsFrontCircleArticleListDTO> selectArticleListByTopicId(@RequestParam Map<String, Object> params);

    /**
     * 用户主页帖子列表
     *
     * @param params
     * @return
     */

    PageData<CmsFrontCircleArticleListDTO> userIndexArticleList(@RequestParam Map<String, Object> params);

    /**
     * 用户主页头部
     *
     * @param params
     * @return
     */

    MemberIndexDTO userIndexHead(@RequestParam Map<String, Object> params);

    /**
     * 查询用户信息
     *
     * @param params 查询条件
     * @return 返回用户信息
     */

    PageData<MemberIndexDTO> userSearch(@RequestParam Map<String, Object> params);

    /**
     * 文章点赞
     *
     * @param id 主键id
     */

    void frontArticlePraise(@RequestParam Long id);

    /**
     * 文章浏览量
     *
     * @param params 更新参数
     */

    void updateArticlePvNum(@RequestParam Map<String, Object> params);

    /**
     * 更新文章的分享数量
     *
     * @param id 主键id
     */

    void frontArticleShare(@RequestParam Long id);

    /**
     * 指定商品分页搜索
     *
     * @param params
     * @return
     */

    PageData<CmsFrontGoodsSearchDTO> circleGoodsSearch(@RequestParam Map<String, Object> params);

}
