/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.service.circle;


import com.leimingtech.commons.tools.page.PageData;
import com.leimingtech.modules.dto.attention.CmsFocusMemberDTO;
import com.leimingtech.modules.dto.attention.CmsMemberFocusDTO;
import com.leimingtech.modules.dto.circle.*;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

/**
 * 圈子管理 CmsCircleService
 *
 * @author pixiaoyong@leimingtech.com
 * @since v1.0.0 2019-12-31
 */

public interface CmsCircleService {
    /**
     * 分页查询
     *
     * @param params 查询条件纪检
     * @return 返回查询信息
     */

    PageData<CmsCircleTopicPageDTO> page(@RequestParam Map<String, Object> params);

    /**
     * 根据id查询信息
     *
     * @param id 主键id
     * @return 返回详情信息
     */

    CmsCircleTopicDTO get(Long id);

    /**
     * 保存信息想
     *
     * @param dto 保存参数
     */

    void save(@RequestBody CmsCircleTopicSaveDTO dto);

    /**
     * 修改信息
     *
     * @param dto 修改参数
     */

    void update(@RequestBody CmsCircleTopicDTO dto);

    /**
     * 删除信息
     *
     * @param ids 主键id
     */

    void delete(@RequestBody Long[] ids);

    /**
     * 通过用户ID查找关注的圈子列表
     *
     * @param id 主键id
     * @return 返回关注的圈子；列表
     */

    List<CmsMemberFocusDTO> memberFocus(Long id);

    /**
     * 通过圈子ID查找有哪些用户关注该圈子
     *
     * @param id 主键id
     * @return 返回用户信息
     */

    List<CmsFocusMemberDTO> focusMember(Long id);

    /**
     * 新增话题
     *
     * @param cmsCircleTopicSaveDTO 新增参数
     */

    void createTopic(@RequestParam CmsCircleTopicSaveDTO cmsCircleTopicSaveDTO);

    /**
     * 发布话题搜索页（前台）
     *
     * @param params 查询条件
     * @return 返回话题信息
     */

    PageData<CmsTopicSelectFrontDTO> topicSelectFront(@RequestParam Map<String, Object> params);

    /**
     * H5话题主页
     *
     * @param params 查询 参数
     * @return 返回查询信息
     */

    CmsTopicIndexFrontDTO topicIndex(@RequestParam Map<String, Object> params);
}
