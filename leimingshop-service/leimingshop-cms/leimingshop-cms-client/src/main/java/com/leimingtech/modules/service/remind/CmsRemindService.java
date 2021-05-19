/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.service.remind;


import com.leimingtech.commons.tools.page.PageData;
import com.leimingtech.modules.dto.article.CmsFrontCircleArticleListDTO;
import com.leimingtech.modules.dto.remind.CmsRemindDTO;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

/**
 * @author hecheng 347861695@qq.com
 * @since v1.0.0 2020-04-08
 */

public interface CmsRemindService {
    /**
     * 分页查询
     *
     * @param params 查询条件
     * @return返回查询信息
     */

    PageData<CmsRemindDTO> page(@RequestParam Map<String, Object> params);

    /**
     * 条件查询
     *
     * @param params 查询条件
     * @return 返回提醒信息
     */

    List<CmsRemindDTO> list(@RequestParam Map<String, Object> params);

    /**
     * 根据id查询详情
     *
     * @param id 主键id
     * @return 返回详情信息
     */

    CmsRemindDTO get(Long id);

    /**
     * 保存信息
     *
     * @param dto 保存参数
     */

    void save(@RequestBody CmsRemindDTO dto);

    /**
     * 更新信息
     *
     * @param dto 更新参数
     */

    void update(@RequestBody CmsRemindDTO dto);

    /**
     * 删除信息
     *
     * @param ids 主键id
     */

    void delete(@RequestBody Long[] ids);

    /**
     * 统计未读艾特我信息数量
     *
     * @param creatorId
     * @return
     */

    Integer readAndMemberCount(Long creatorId);

    /**
     * 查询艾特我的信息
     *
     * @param params
     * @return
     */

    PageData<CmsFrontCircleArticleListDTO> pageList(@RequestParam Map<String, Object> params);

    /**
     * 标记已读
     *
     * @param id
     * @return
     */

    Integer read(Long id);

    /**
     * 根据文章id 查询评论中艾特我的数据记录id
     *
     * @param params
     * @return
     */

    List<Map<String, Object>> commentGetIdList(@RequestParam Map<String, Object> params);

    /**
     * 根据文章id 查询文章中艾特我的数据记录id
     *
     * @param params
     * @return
     */

    List<Map<String, Object>> articleGetIdList(@RequestParam Map<String, Object> params);

    /**
     * 根据文章id或者评论id逻辑删除艾特记录
     *
     * @param params
     */

    void deleteRemind(@RequestParam Map<String, Object> params);
}