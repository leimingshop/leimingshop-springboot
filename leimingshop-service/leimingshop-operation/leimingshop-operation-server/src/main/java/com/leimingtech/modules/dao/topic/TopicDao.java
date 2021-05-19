/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dao.topic;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.leimingtech.commons.mybatis.dao.BaseDao;
import com.leimingtech.modules.dto.topic.TopicPageDTO;
import com.leimingtech.modules.entity.topic.TopicEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 专题页
 *
 * @author kuangweiguo kuangweiguo
 * @since v1.0.0 2020-11-19
 */
@Mapper
public interface TopicDao extends BaseDao<TopicEntity> {

    /**
     * 分页查询专题页
     *
     * @param page   分页参数
     * @param params 查询条件
     * @return
     */
    List<TopicPageDTO> topicPage(IPage<TopicEntity> page, @Param("params") Map<String, Object> params);

    /**
     * 根据专题页id批量删除
     *
     * @param id
     */
    void deleteBatchByTopicId(Long id);
}