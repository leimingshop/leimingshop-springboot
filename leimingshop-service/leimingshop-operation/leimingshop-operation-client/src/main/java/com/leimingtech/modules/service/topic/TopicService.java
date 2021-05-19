/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.service.topic;

import com.leimingtech.commons.tools.page.PageData;
import com.leimingtech.modules.dto.topic.*;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

/**
 * 专题页
 *
 * @author kuangweiguo kuangweiguo
 * @since v1.0.0 2020-11-19
 */
public interface TopicService {
    /**
     * 专题页分页查询
     *
     * @param params 查询参数
     * @return
     */

    PageData<TopicPageDTO> page(@RequestParam Map<String, Object> params);

    /**
     * 查询所有专题页
     *
     * @param params 查询参数
     * @return
     */

    List<TopicDTO> list(@RequestParam Map<String, Object> params);

    /**
     * 专题页详情
     *
     * @param id 主键id
     * @return
     */

    UpdateTopicDTO get(Long id);

    /**
     * ’
     * 保存专题页
     *
     * @param dto 保存参数
     */

    void save(@RequestBody SaveTopicDTO dto);

    /**
     * 修改专题页
     *
     * @param dto 修改参数
     */

    void update(@RequestBody UpdateTopicDTO dto);

    /**
     * ‘
     * 删除专题页
     *
     * @param ids
     */

    void delete(@RequestParam("id") Long ids);

    /**
     * 专题页详情
     *
     * @param id 主键id
     * @return
     */

    TopicInfoDTO topicInfo(@RequestParam("id") Long id);


    TopicInfoDTO topicPage(@RequestParam Map<String, Object> params);
}