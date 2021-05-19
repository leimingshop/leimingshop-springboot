/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.service.attention;


import com.leimingtech.commons.tools.page.PageData;
import com.leimingtech.modules.dto.attention.CmsAttentionClassListDTO;
import com.leimingtech.modules.dto.attention.CmsAttentionDTO;
import com.leimingtech.modules.dto.attention.CmsAttentionListDTO;
import com.leimingtech.modules.dto.attention.CmsRecommendAttentionDTO;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

/**
 * 圈子关注管理 CmsAttentionService
 *
 * @author yuhaifeng yuhaifeng@leimingtech.com
 * @since v1.0.0 2019-12-31
 */

public interface CmsAttentionService {
    /**
     * 分页查询
     *
     * @param params 查询参数
     * @return 返回分页信息
     */

    PageData<CmsAttentionDTO> page(@RequestParam Map<String, Object> params);

    /**
     * 条件查询关注信息
     *
     * @param params 查询条件
     * @return 返回关注信息
     */

    List<CmsAttentionDTO> list(@RequestParam Map<String, Object> params);

    /**
     * 根据id查询详情
     *
     * @param id 主键id
     * @return 返回关注详情
     */

    CmsAttentionDTO get(Long id);

    /**
     * 关注用户
     *
     * @param dto 关注参数
     */

    void save(@RequestBody CmsAttentionDTO dto);

    /**
     * 我关注的用户列表
     *
     * @param userId 用户id
     * @return 返回用户信息
     */

    List<CmsAttentionListDTO> selectAttentionUserList(@RequestParam Long userId);

    /**
     * 我关注的分类列表
     *
     * @param userId
     * @return 返回关注的分类信息
     */

    List<CmsAttentionClassListDTO> selectAttentionClassList(@RequestParam Long userId);

    /**
     * 更新信息
     *
     * @param dto 更新参数
     */

    void update(@RequestBody CmsAttentionDTO dto);

    /**
     * 删除信息
     *
     * @param ids 主键id
     */

    void delete(@RequestBody Long[] ids);

    /**
     * 取消关注
     *
     * @param params 修改条件
     */

    void cancelAttention(@RequestParam Map<String, Object> params);


    /**
     * 推荐关注用户列表
     *
     * @param memberId 用户id
     * @return 返回用户信息
     */

    List<CmsRecommendAttentionDTO> selectRecommendUserList(@RequestParam Long memberId);

    /**
     * 粉丝列表
     *
     * @param memberId 用户id
     * @return 返回用户信息
     */

    List<CmsRecommendAttentionDTO> selectFansList(@RequestParam Long memberId);

}
