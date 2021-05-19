/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.service.topic;

import com.leimingtech.commons.tools.page.PageData;
import com.leimingtech.modules.dto.topic.TopicRelDTO;
import com.leimingtech.modules.dto.topic.TopicRelGoodsPageDTO;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

/**
 * 专题页商品关联表
 *
 * @author kuangweiguo kuangweiguo
 * @since v1.0.0 2020-11-19
 */
public interface TopicRelService {


    PageData<TopicRelGoodsPageDTO> page(@RequestParam Map<String, Object> params);

    /**
     * 查询
     *
     * @param params
     * @return
     */

    List<TopicRelDTO> list(@RequestParam Map<String, Object> params);


    TopicRelDTO get(Long id);


    void save(@RequestBody TopicRelDTO dto);


    void update(@RequestBody TopicRelDTO dto);


    void delete(@RequestBody Long[] ids);

    /**
     * 保存专题页关联商品
     *
     * @param id          专题页id
     * @param goodsIdList 关联商品list
     */

    void saveBatch(@RequestParam("id") Long id, @RequestBody List<Long> goodsIdList);

    /**
     * 通过专题页id获取专题页所关联商品信息
     *
     * @param topicId 专题页id
     * @return
     */

    List<Long> getRecGoodsId(@RequestParam("topicId") Long topicId);
}