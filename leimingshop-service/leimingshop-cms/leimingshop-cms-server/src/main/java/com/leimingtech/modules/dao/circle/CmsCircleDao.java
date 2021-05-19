/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dao.circle;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.leimingtech.commons.mybatis.dao.BaseDao;
import com.leimingtech.modules.dto.circle.CmsCircleTopicPageDTO;
import com.leimingtech.modules.dto.circle.CmsTopicIndexFrontDTO;
import com.leimingtech.modules.dto.circle.CmsTopicSelectFrontDTO;
import com.leimingtech.modules.entity.circle.CmsCircleEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 圈子管理 CmsCircleDao
 *
 * @author yuhaifeng yuhaifeng@leimingtech.com
 * @since v1.0.0 2019-12-31
 */
@Mapper
public interface CmsCircleDao extends BaseDao<CmsCircleEntity> {

    /**
     * 圈子话题查询
     *
     * @param page
     * @param params
     * @return
     */
    List<CmsCircleTopicPageDTO> selectCmsCircleTopicList(Page page, @Param("params") Map<String, Object> params);

    /**
     * 根据分类id查询关注的数量
     *
     * @param ids 主键id
     * @return 返回关注的数量
     */
    List<String> findCountByClassId(@Param("ids") Long[] ids);

    /**
     * 发布话题搜索页（前台）
     *
     * @param page
     * @param params
     * @return
     */
    List<CmsTopicSelectFrontDTO> topicSelectFront(Page page, @Param("params") Map<String, Object> params);

    /**
     * H5话题主页
     *
     * @param params
     * @return
     */
    CmsTopicIndexFrontDTO selectTopicIndex(@Param("params") Map<String, Object> params);
}