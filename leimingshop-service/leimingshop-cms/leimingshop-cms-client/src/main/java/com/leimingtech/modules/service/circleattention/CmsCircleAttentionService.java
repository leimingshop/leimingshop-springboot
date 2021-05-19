/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.service.circleattention;


import com.leimingtech.commons.tools.page.PageData;
import com.leimingtech.modules.dto.attention.CmsAttentionDTO;
import com.leimingtech.modules.dto.attention.CmsAttentionListDTO;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

/**
 * 圈子关注管理 CmsCircleAttentionService
 *
 * @author yuhaifeng yuhaifeng@leimingtech.com
 * @since v1.0.0 2019-12-31
 */

public interface CmsCircleAttentionService {
    /**
     * 分页查询
     *
     * @param params 查询条件
     * @return 返回圈子信息
     */

    PageData<CmsAttentionDTO> page(@RequestParam Map<String, Object> params);

    /**
     * 条件查询
     *
     * @param params 查询条件
     * @return 圈子关注信息
     */

    List<CmsAttentionDTO> list(@RequestParam Map<String, Object> params);

    /**
     * 根据id查询详情
     *
     * @param id 主键id
     * @return 返回详情
     */

    CmsAttentionDTO get(Long id);

    /**
     * 关注用户
     *
     * @param dto 关注信息
     */

    void save(@RequestBody CmsAttentionDTO dto);

    /**
     * 我关注的用户列表
     *
     * @param userId 用户id
     * @return 返回用户列表
     */

    List<CmsAttentionListDTO>
    selectAttentionUserList(@RequestParam Long userId);

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


}
