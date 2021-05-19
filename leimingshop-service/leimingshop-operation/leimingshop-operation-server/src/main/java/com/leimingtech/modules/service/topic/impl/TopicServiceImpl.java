/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.service.topic.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.leimingtech.commons.mybatis.service.impl.BaseServiceImpl;
import com.leimingtech.commons.tools.constant.Constant;
import com.leimingtech.commons.tools.page.PageData;
import com.leimingtech.commons.tools.redis.RedisUtils;
import com.leimingtech.commons.tools.utils.ConvertUtils;
import com.leimingtech.modules.constants.RedisConstants;
import com.leimingtech.modules.dao.topic.TopicDao;
import com.leimingtech.modules.dto.goods.TopicGoodsDTO;
import com.leimingtech.modules.dto.topic.*;
import com.leimingtech.modules.entity.topic.TopicEntity;
import com.leimingtech.modules.service.evaluate.EvaluateGoodsService;
import com.leimingtech.modules.service.goods.GoodsService;
import com.leimingtech.modules.service.topic.TopicRelService;
import com.leimingtech.modules.service.topic.TopicService;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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
@Transactional
@Service
public class TopicServiceImpl extends BaseServiceImpl<TopicDao, TopicEntity> implements TopicService {

    @Autowired
    private TopicRelService topicRelService;

    @Autowired
    private RedisUtils redisUtils;

    @Autowired
    private GoodsService goodsService;

    @Autowired
    private EvaluateGoodsService evaluateGoodsService;

    /**
     * 专题页分页查询
     *
     * @param params 查询参数
     * @return
     */
    @Override

    public PageData<TopicPageDTO> page(@RequestParam Map<String, Object> params) {

        IPage<TopicEntity> page = getPage(params, Constant.UPDATE_DATE, false);
        List<TopicPageDTO> topic = baseDao.topicPage(page, params);
        return getPageData(topic, page.getTotal(), TopicPageDTO.class);
    }

    /**
     * 查询所有专题页
     *
     * @param params 查询参数
     * @return
     */
    @Override

    public List<TopicDTO> list(@RequestParam Map<String, Object> params) {
        List<TopicEntity> entityList = baseDao.selectList(getWrapper(params));

        return ConvertUtils.sourceToTarget(entityList, TopicDTO.class);
    }

    private QueryWrapper<TopicEntity> getWrapper(Map<String, Object> params) {
        String topicName = (String) params.get("topicName");

        QueryWrapper<TopicEntity> wrapper = new QueryWrapper<>();
        wrapper.eq(StringUtils.isNotBlank(topicName), "TOPIC_NAME", topicName);

        return wrapper;
    }

    /**
     * 专题页详情
     *
     * @param id 主键id
     * @return
     */
    @Override

    public UpdateTopicDTO get(Long id) {
        TopicEntity entity = baseDao.selectById(id);
        UpdateTopicDTO updateTopicDTO = ConvertUtils.sourceToTarget(entity, UpdateTopicDTO.class);
        List<Long> goodsList = topicRelService.getRecGoodsId(id);
        updateTopicDTO.setGoodsIdList(goodsList);
        return updateTopicDTO;
    }

    /**
     * 保存专题页
     *
     * @param dto 保存参数
     */
    @Override
    @Transactional(rollbackFor = Exception.class)

    public void save(@RequestBody SaveTopicDTO dto) {
        //保存专题页
        TopicEntity entity = ConvertUtils.sourceToTarget(dto, TopicEntity.class);
        insert(entity);
        // 保存专题页关联商品
        if (CollectionUtils.isNotEmpty(dto.getGoodsIdList())) {
            topicRelService.saveBatch(entity.getId(), dto.getGoodsIdList());
        }
        this.topicInfo(entity.getId());
    }

    /**
     * 修改专题页
     *
     * @param dto 修改参数
     */
    @Override
    @Transactional(rollbackFor = Exception.class)

    public void update(@RequestBody UpdateTopicDTO dto) {
        TopicEntity entity = ConvertUtils.sourceToTarget(dto, TopicEntity.class);
        updateById(entity);
        if (CollectionUtils.isNotEmpty(dto.getGoodsIdList())) {
            topicRelService.saveBatch(entity.getId(), dto.getGoodsIdList());
        }
        redisUtils.hDel(RedisConstants.TOPIC_PREFIX, entity.getId().toString());
        this.topicInfo(entity.getId());
    }

    /**
     * 删除专题页
     *
     * @param id
     */
    @Override
    @Transactional(rollbackFor = Exception.class)

    public void delete(@RequestParam("id") Long id) {
        deleteById(id);
        baseDao.deleteBatchByTopicId(id);
        redisUtils.hDel(RedisConstants.TOPIC_PREFIX, id.toString());

    }

    /**
     * 专题页详情
     *
     * @param id 主键id
     * @return
     */

    @Override
    public TopicInfoDTO topicInfo(@RequestParam("id") Long id) {
        Object object = redisUtils.hGet(RedisConstants.TOPIC_PREFIX, id.toString());
        if (object == null) {
            TopicEntity topicEntity = baseDao.selectById(id);
            TopicInfoDTO topicInfoDTO = ConvertUtils.sourceToTarget(topicEntity, TopicInfoDTO.class);
            List<Long> goodsList = topicRelService.getRecGoodsId(id);
            // 计算出好评率
            List<TopicGoodsDTO> topicGoodsDTO = goodsService.selectGoodsInfoByGoodsId(goodsList);
            topicGoodsDTO.stream().forEach(topic -> {
                String reputably = evaluateGoodsService.reputably(topic.getId());
                topic.setGoodEvaluate(reputably);
            });
            List<TopicGoodsInfoDTO> topicGoodsInfoDTOS = ConvertUtils.sourceToTarget(topicGoodsDTO, TopicGoodsInfoDTO.class);
            topicInfoDTO.setGoodsDTOList(topicGoodsInfoDTOS);
            redisUtils.hSet(RedisConstants.TOPIC_PREFIX, id.toString(), topicInfoDTO, RedisConstants.JEDIS_EXPIRE);
            return topicInfoDTO;
        } else {
            return (TopicInfoDTO) object;
        }
    }


    @Override
    public TopicInfoDTO topicPage(@RequestParam Map<String, Object> params) {
        Long id = Long.valueOf((String) params.get("id"));
        Object pageParams = params.get("page");
        Object limitParams = params.get("limit");
        int page = 1;
        int limit = 10;
        if (pageParams != null) {
            page = Integer.parseInt(pageParams.toString());
        }
        if (limitParams != null) {
            limit = Integer.parseInt(limitParams.toString());
        }
        TopicInfoDTO topicInfoDTO = topicInfo(id);
        List<TopicGoodsInfoDTO> list = topicInfoDTO.getGoodsDTOList();

        Integer count = list.size();
        topicInfoDTO.setTotal(count);
        Integer pageCount = 0;
        if (count % limit == 0) {
            pageCount = count / limit;
        } else {
            pageCount = count / limit + 1;
        }

        int fromIndex = 0;
        int toIndex = 0;

        if (page != pageCount) {
            fromIndex = (page - 1) * limit;
            toIndex = fromIndex + limit;
        } else {
            fromIndex = (page - 1) * limit;
            toIndex = count;
        }
        List<TopicGoodsInfoDTO> topicGoodsInfoDTOS = topicInfoDTO.getGoodsDTOList().subList(fromIndex, toIndex);
        topicInfoDTO.setGoodsDTOList(topicGoodsInfoDTOS);

        return topicInfoDTO;
    }
}
