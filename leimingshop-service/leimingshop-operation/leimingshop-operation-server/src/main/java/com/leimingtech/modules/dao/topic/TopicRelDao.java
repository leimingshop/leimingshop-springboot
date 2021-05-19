/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dao.topic;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.leimingtech.commons.mybatis.dao.BaseDao;
import com.leimingtech.modules.dto.topic.TopicRelGoodsPageDTO;
import com.leimingtech.modules.entity.topic.TopicRelEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 专题页商品关联表
 *
 * @author kuangweiguo kuangweiguo
 * @since v1.0.0 2020-11-19
 */
@Mapper
public interface TopicRelDao extends BaseDao<TopicRelEntity> {
    /**
     * 根据专题页id删除所关联的商品信息
     *
     * @param id 专题页id
     */
    void deleteByTopicId(Long id);

    /**
     * 获取专题页关联商品id
     *
     * @param topicId 专题页id
     * @return
     */
    List<Long> getRecGoodsId(Long topicId);

    /**
     * 专题也关联商品分页查询
     *
     * @param page   分页信息
     * @param params 查询参数
     * @return
     */
    List<TopicRelGoodsPageDTO> topicPage(IPage<TopicRelEntity> page, @Param("params") Map<String, Object> params);
}