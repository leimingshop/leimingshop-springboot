/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.service.topic.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.leimingtech.commons.mybatis.service.impl.BaseServiceImpl;
import com.leimingtech.commons.tools.page.PageData;
import com.leimingtech.commons.tools.utils.ConvertUtils;
import com.leimingtech.modules.dao.topic.TopicRelDao;
import com.leimingtech.modules.dto.topic.TopicRelDTO;
import com.leimingtech.modules.dto.topic.TopicRelGoodsPageDTO;
import com.leimingtech.modules.entity.topic.TopicRelEntity;
import com.leimingtech.modules.service.topic.TopicRelService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * 专题页商品关联表
 *
 * @author kuangweiguo kuangweiguo
 * @since v1.0.0 2020-11-19
 */
@Transactional
@Service
public class TopicRelServiceImpl extends BaseServiceImpl<TopicRelDao, TopicRelEntity> implements TopicRelService {

    @Override

    public PageData<TopicRelGoodsPageDTO> page(@RequestParam Map<String, Object> params) {
        IPage<TopicRelEntity> page = getPage(params, null, false);
        List<TopicRelGoodsPageDTO> topic = baseDao.topicPage(page, params);
        return getPageData(topic, page.getTotal(), TopicRelGoodsPageDTO.class);
    }

    @Override

    public List<TopicRelDTO> list(@RequestParam Map<String, Object> params) {
        List<TopicRelEntity> entityList = baseDao.selectList(getWrapper(params));

        return ConvertUtils.sourceToTarget(entityList, TopicRelDTO.class);
    }

    private QueryWrapper<TopicRelEntity> getWrapper(Map<String, Object> params) {
        String id = (String) params.get("id");

        QueryWrapper<TopicRelEntity> wrapper = new QueryWrapper<>();
        wrapper.eq(StringUtils.isNotBlank(id), "id", id);
        //wrapper.eq(Constant.DEL_FLAG, DelFlagEnum.NORMAL.value());

        return wrapper;
    }

    @Override

    public TopicRelDTO get(Long id) {
        TopicRelEntity entity = baseDao.selectById(id);

        return ConvertUtils.sourceToTarget(entity, TopicRelDTO.class);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)

    public void save(@RequestBody TopicRelDTO dto) {
        TopicRelEntity entity = ConvertUtils.sourceToTarget(dto, TopicRelEntity.class);

        insert(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)

    public void update(@RequestBody TopicRelDTO dto) {
        TopicRelEntity entity = ConvertUtils.sourceToTarget(dto, TopicRelEntity.class);

        updateById(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)

    public void delete(@RequestBody Long[] ids) {
        //逻辑删除
        //logicDelete(ids, TopicRelEntity.class);

        //物理删除
        baseDao.deleteBatchIds(Arrays.asList(ids));
    }

    /**
     * 保存专题页关联商品
     *
     * @param id          专题页id
     * @param goodsIdList 关联商品list
     */

    @Override
    public void saveBatch(@RequestParam("id") Long id, @RequestBody List<Long> goodsIdList) {

        baseDao.deleteByTopicId(id);
        List<TopicRelEntity> list = new ArrayList<>();
        goodsIdList.stream().forEach(g -> {
            TopicRelEntity topicRelEntity = new TopicRelEntity();
            topicRelEntity.setGoodsId(g);
            topicRelEntity.setTopicId(id);
            list.add(topicRelEntity);
        });
        insertBatch(list);
    }

    /**
     * 通过专题页id获取专题页所关联商品信息
     *
     * @param topicId 专题页id
     * @return
     */

    @Override
    public List<Long> getRecGoodsId(@RequestParam("topicId") Long topicId) {

        return baseDao.getRecGoodsId(topicId);
    }
}
