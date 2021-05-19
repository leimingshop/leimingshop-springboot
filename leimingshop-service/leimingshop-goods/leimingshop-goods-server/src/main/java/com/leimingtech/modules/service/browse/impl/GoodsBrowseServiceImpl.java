/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.service.browse.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.leimingtech.commons.mybatis.service.impl.CrudServiceImpl;
import com.leimingtech.commons.tools.page.PageData;
import com.leimingtech.commons.tools.utils.ConvertUtils;
import com.leimingtech.commons.tools.utils.DateUtils;
import com.leimingtech.modules.constants.mongodb.CollectionName;
import com.leimingtech.modules.dao.browse.GoodsBrowseDao;
import com.leimingtech.modules.dto.browse.BrowseCmsDTO;
import com.leimingtech.modules.dto.browse.FindGoodsBrowseDTO;
import com.leimingtech.modules.dto.browse.GoodsBrowseDTO;
import com.leimingtech.modules.dto.browse.MqGoodsBrowseDTO;
import com.leimingtech.modules.dto.goods.GoodsDTO;
import com.leimingtech.modules.dto.goods.spec.GoodsSpecDTO;
import com.leimingtech.modules.entity.browse.GoodsBrowseEntity;
import com.leimingtech.modules.entity.browse.GoodsBrowseMongoEntity;
import com.leimingtech.modules.service.activity.goods.ActivityGoodsService;
import com.leimingtech.modules.service.browse.GoodsBrowseService;
import com.leimingtech.modules.service.goods.GoodsService;
import com.leimingtech.modules.service.goods.spec.GoodsSpecService;
import com.leimingtech.modules.service.goodslabel.GoodsLabelRelService;
import com.mongodb.client.result.UpdateResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.beans.BeanCopier;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * 足迹记录表
 *
 * @author chengqian
 * @email 543826372@qq.com
 * @since 1.0.0 2019-06-03
 */
@Slf4j
@Service

public class GoodsBrowseServiceImpl extends CrudServiceImpl<GoodsBrowseDao, GoodsBrowseEntity, GoodsBrowseDTO> implements GoodsBrowseService {
    @Autowired
    private GoodsSpecService goodsSpecService;
    @Autowired
    private GoodsService goodsService;

    @Autowired
    private ActivityGoodsService activityGoodsService;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private GoodsLabelRelService goodsLabelRelService;

    /**
     * 条件构造器
     *
     * @param params 查询参数
     * @return
     */
    @Override
    public QueryWrapper<GoodsBrowseEntity> getWrapper(Map<String, Object> params) {
        QueryWrapper<GoodsBrowseEntity> wrapper = new QueryWrapper<>();
        wrapper.groupBy("goods_spec_id");

        return wrapper;
    }

    /**
     * 分页查询信息
     *
     * @param params 分页参数
     * @return
     */
    @Override

    public PageData<FindGoodsBrowseDTO> pageList(@RequestParam Map<String, Object> params) {

        String page = params.get("page").toString();
        String limit = params.get("limit").toString();
        Long memberId = Long.valueOf(params.get("memberId").toString());
        List<GoodsBrowseMongoEntity> mappedResults = getGoodsBrowseEntities(page, limit, memberId);

        // 取出日期集合
        List<Date> dates = mappedResults.stream().map(GoodsBrowseMongoEntity::getCreateDayTime).collect(Collectors.toList());

        // 根据日期查询对应数据
        Query query = new Query();
        query.addCriteria(Criteria.where("memberId").is(memberId).and("createDayTime").in(dates).and("delFlag").is(0));
        query.with(Sort.by(Sort.Order.desc("createTime")));
        List<GoodsBrowseMongoEntity> goodsBrowseMongoEntities = mongoTemplate.find(query, GoodsBrowseMongoEntity.class);

        // 去除重复的商品ID数据
        List<GoodsBrowseMongoEntity> browseMongoEntities = goodsBrowseMongoEntities.stream().filter(distinctByKey(GoodsBrowseMongoEntity::getGoodsId)).collect(Collectors.toList());
        // 根据创建时间（天数）
        // group by分组
        // 第一个参数：分组按照什么分类
        // 第二个参数：分组最后用什么容器保存返回
        // 第三个参数：按照第一个参数分类后，对应的分类的结果如何收集
        // 其实一个参数的Collectors.groupingBy方法 ，第二个参数默认是HashMap::new， 第三个参数收集器其实默认是Collectors.toList()
        // 详细请查看源码
        Map<Date, List<GoodsBrowseMongoEntity>> dataMap = browseMongoEntities.stream()
                .collect(Collectors.groupingBy(GoodsBrowseMongoEntity::getCreateDayTime, LinkedHashMap::new, Collectors.toList()));

        // 封装返回数据
        List<FindGoodsBrowseDTO> result = new ArrayList<>();
        dataMap.keySet().forEach(e -> {
            List<GoodsBrowseMongoEntity> entity = dataMap.get(e);
            FindGoodsBrowseDTO findGoodsBrowseDTO = new FindGoodsBrowseDTO();
            List<GoodsBrowseDTO> goodsBrowseList = ConvertUtils.sourceToTarget(entity, GoodsBrowseDTO.class);
            findGoodsBrowseDTO.setGoodsBrowseDTOList(goodsBrowseList);
            findGoodsBrowseDTO.setBrowseTime(DateUtils.format(e, DateUtils.DATE_PATTERN));
            result.add(findGoodsBrowseDTO);
        });
        return new PageData<>(result, 0);
    }

    private List<GoodsBrowseMongoEntity> getGoodsBrowseEntities(String page, String limit, Long memberId) {
        // 封装聚合对象 查询条件：用户Id、删除状态，根据createDayTime分组， 按照createDayTime排序 并进行分页
        Aggregation aggregation = Aggregation.newAggregation(
                Aggregation.match(Criteria.where("memberId").is(memberId).and("delFlag").is(0)),
                Aggregation.group("createDayTime"),
                Aggregation.project().and("createDayTime").previousOperation(),
                Aggregation.sort(Sort.Direction.DESC, "createDayTime"),
                Aggregation.skip(Long.parseLong(page) > 1 ? (Long.parseLong(page) - 1) * Long.parseLong(limit) : 0),
                Aggregation.limit(Long.parseLong(limit))
        );
        // mongodb查询
        return mongoTemplate.aggregate(aggregation, CollectionName.BROWSE_RECORD, GoodsBrowseMongoEntity.class).getMappedResults();
    }


    /**
     * 自定义去重函数
     *
     * @param keyExtractor 函数式接口
     * @return Predicate函数
     * @date 2020/3/31 10:23
     * @author lixiangx@leimingtech.com
     **/
    public <T> Predicate<T> distinctByKey(Function<? super T, Object> keyExtractor) {
        Map<Object, Boolean> seen = new ConcurrentHashMap<>(10);
        return t -> seen.putIfAbsent(keyExtractor.apply(t), Boolean.TRUE) == null;
    }

    /**
     * 新增记录
     *
     * @param mqGoodsBrowseDTO
     */
    @Override

    public void save(@RequestBody MqGoodsBrowseDTO mqGoodsBrowseDTO) {

        GoodsSpecDTO goodsSpecDTO = goodsSpecService.get(mqGoodsBrowseDTO.getGoodsSpecId());
        if (BeanUtil.isEmpty(goodsSpecDTO)) {
            return;
        }
        GoodsDTO goodsDTO = goodsService.get(goodsSpecDTO.getGoodsId());
        if (BeanUtil.isEmpty(goodsDTO)) {
            return;
        }
        //2020年3月9日 足迹保存到MongoDB
        if (mqGoodsBrowseDTO.getMemberId() != null && !BeanUtil.isEmpty(goodsSpecDTO) && null != goodsSpecDTO.getId()) {
            String labelName = goodsLabelRelService.selectLabelNameByGoodsId(goodsDTO.getId());
            GoodsBrowseMongoEntity goodsBrowseMongoEntity = new GoodsBrowseMongoEntity();
            goodsBrowseMongoEntity.setId(IdWorker.getId());
            goodsBrowseMongoEntity.setDelFlag(0);
            goodsBrowseMongoEntity.setGoodsImage(goodsSpecDTO.getSpecMainPicture());
            goodsBrowseMongoEntity.setGoodsName(goodsDTO.getGoodsName());
            goodsBrowseMongoEntity.setGoodsPrice(goodsSpecDTO.getSpecSellPrice());
            goodsBrowseMongoEntity.setStoreId(goodsDTO.getStoreId());
            goodsBrowseMongoEntity.setGoodsSpecId(goodsSpecDTO.getId());
            goodsBrowseMongoEntity.setGoodsSubTitle(goodsDTO.getGoodsSubTitle());
            goodsBrowseMongoEntity.setLabelName(labelName);
            goodsBrowseMongoEntity.setGoodsId(goodsDTO.getId());
            goodsBrowseMongoEntity.setStoreName(goodsDTO.getStoreName());
            goodsBrowseMongoEntity.setStoreType(goodsDTO.getStoreType());
            goodsBrowseMongoEntity.setMemberId(mqGoodsBrowseDTO.getMemberId());
            goodsBrowseMongoEntity.setCreator(mqGoodsBrowseDTO.getCreator());
            Date date = new Date();
            goodsBrowseMongoEntity.setCreateTime(date);
            String dateStr = DateUtils.format(date, DateUtils.DATE_PATTERN);
            goodsBrowseMongoEntity.setCreateDayTime(DateUtils.parse(dateStr, DateUtils.DATE_PATTERN));
            mongoTemplate.insert(goodsBrowseMongoEntity, CollectionName.BROWSE_RECORD);
        }

        // 保存活动商品浏览记录
        activityGoodsService.saveActivityGoodsBrowser(goodsSpecDTO.getGoodsId(), mqGoodsBrowseDTO.getIp());
    }


    public void updateRecord() {
        List<GoodsBrowseEntity> goodsBrowseEntities = baseDao.selectList(new QueryWrapper<GoodsBrowseEntity>().eq("del_flag", 1).or().eq("del_flag", 0));
        List<GoodsBrowseMongoEntity> goodsBrowseMongoEntities = new ArrayList<>();

        goodsBrowseEntities.stream().forEach(goodsBrowseEntity -> {
            GoodsBrowseMongoEntity goodsBrowseMongoEntity = new GoodsBrowseMongoEntity();
            goodsBrowseMongoEntity.setId(IdWorker.getId());
            goodsBrowseMongoEntity.setGoodsImage(goodsBrowseEntity.getGoodsImage());
            goodsBrowseMongoEntity.setGoodsName(goodsBrowseEntity.getGoodsName());
            goodsBrowseMongoEntity.setGoodsPrice(goodsBrowseEntity.getGoodsPrice());
            goodsBrowseMongoEntity.setDelFlag(goodsBrowseEntity.getDelFlag());
            goodsBrowseMongoEntity.setStoreId(goodsBrowseEntity.getStoreId());
            goodsBrowseMongoEntity.setGoodsSpecId(goodsBrowseEntity.getGoodsSpecId());
            goodsBrowseMongoEntity.setGoodsId(goodsBrowseEntity.getGoodsId());
            goodsBrowseMongoEntity.setMemberId(goodsBrowseEntity.getMemberId());
            goodsBrowseMongoEntity.setCreator(goodsBrowseEntity.getCreator());
            goodsBrowseMongoEntity.setCreateTime(goodsBrowseEntity.getCreateDate());
            String dateStr = DateUtils.format(goodsBrowseEntity.getCreateDate(), DateUtils.DATE_PATTERN);
            goodsBrowseMongoEntity.setCreateDayTime(DateUtils.parse(dateStr, DateUtils.DATE_PATTERN));
            goodsBrowseMongoEntities.add(goodsBrowseMongoEntity);
        });
        mongoTemplate.insert(goodsBrowseMongoEntities, CollectionName.BROWSE_RECORD);
    }

    /**
     * 修改浏览记录
     *
     * @param dto 参数实体
     */
    @Override

    public void update(@RequestBody GoodsBrowseDTO dto) {

        super.update(dto);

    }

    /**
     * 删除浏览记录
     *
     * @param ids   主键ID
     * @param state 清空记录
     */
    @Override

    public void delete(@RequestBody(required = false) Long[] ids, @RequestParam(value = "state", required = false) Integer state,
                       @RequestParam("memberId") Long memberId) {
        Long removeCount = 0L;
        if (memberId != null) {
            if (null != ids && ids.length > 0) {
                UpdateResult updateResult = mongoTemplate.updateMulti(Query.query(Criteria.where("memberId").is(memberId)
                        .andOperator(Criteria.where("goodsId").in(ids), Criteria.where("delFlag").is(0))), Update.update("delFlag", 1), CollectionName.BROWSE_RECORD);
                removeCount = updateResult.getModifiedCount();
            } else {
                UpdateResult updateResult = mongoTemplate.updateMulti(Query.query(Criteria.where("memberId").is(memberId).andOperator(Criteria.where("delFlag").is(0))),
                        Update.update("delFlag", 1), CollectionName.BROWSE_RECORD);
                removeCount = updateResult.getModifiedCount();
            }
        }
        log.info("用户ID：{}，删除足迹{}条", memberId, removeCount);
    }

    /**
     * 查询用户足迹数量
     *
     * @param id
     * @return
     */

    @Override
    public Integer findNum(Long id) {
        Aggregation aggregation = Aggregation.newAggregation(Aggregation.match(Criteria.where("memberId").is(id).andOperator(Criteria.where("delFlag").is(0))),
                Aggregation.group("goodsId").count().as("num"),
                Aggregation.project("num").and("goodsId").previousOperation()
        );
        List<Object> mappedResults = mongoTemplate.aggregate(aggregation, CollectionName.BROWSE_RECORD, Object.class).getMappedResults();
        if (CollectionUtils.isNotEmpty(mappedResults)) {
            return mappedResults.size();
        } else {
            return 0;
        }
    }


    /**
     * 用户浏览商品ID
     *
     * @param memberId 用户ID
     * @return
     * @Author: yuhaifeng
     */
    @Override

    public List<BrowseCmsDTO> getBrowseGoodIds(@RequestParam("memberId") Long memberId) {
        // 封装聚合对象 查询条件：用户Id、删除状态，按照createDayTime排序
        Aggregation aggregation = Aggregation.newAggregation(
                Aggregation.match(Criteria.where("memberId").is(memberId).and("delFlag").is(0)),
                Aggregation.sort(Sort.Direction.DESC, "createDayTime")
        );
        // mongodb查询
        List<GoodsBrowseMongoEntity> mappedResults = mongoTemplate.aggregate(aggregation, CollectionName.BROWSE_RECORD, GoodsBrowseMongoEntity.class).getMappedResults();

        List<BrowseCmsDTO> browseCmsDTOList = new ArrayList<>();
        mappedResults.forEach(r -> {
            BrowseCmsDTO browseCmsDTO = new BrowseCmsDTO();
            browseCmsDTO.setGoodsId(r.getGoodsId());
            browseCmsDTO.setUpdateDate(r.getUpdateDate());
            browseCmsDTOList.add(browseCmsDTO);
        });
        return browseCmsDTOList;
    }


    @Override
    public List<MqGoodsBrowseDTO> pcList(@RequestParam Map<String, Object> params) {
        String page = params.get("page").toString();
        String limit = params.get("limit").toString();
        Long memberId = Long.valueOf(params.get("memberId").toString());
        List<MqGoodsBrowseDTO> mqGoodsBrowseDTOList = new ArrayList<>();
        Query query = new Query();
        query.addCriteria(Criteria.where("memberId").is(memberId).and("delFlag").is(0));
        List<Long> goodsIds = mongoTemplate.findDistinct(query, "goodsId", GoodsBrowseMongoEntity.class, Long.class);
        if (CollectionUtils.isNotEmpty(goodsIds)) {
            Collections.reverse(goodsIds);
            Integer num = 12;
            if (goodsIds.size() > num) {
                goodsIds = goodsIds.subList(0, 12);
            }
            List<GoodsBrowseMongoEntity> goodsBrowseMongoEntities = mongoTemplate.find(
                    Query.query(Criteria.where("goodsId").in(goodsIds)
                            .and("delFlag").is(0).and("memberId").is(memberId)), GoodsBrowseMongoEntity.class);
            Map<Long, List<GoodsBrowseMongoEntity>> collect = goodsBrowseMongoEntities.stream().collect(Collectors.groupingBy(GoodsBrowseMongoEntity::getGoodsId));
            for (List<GoodsBrowseMongoEntity> values : collect.values()) {
                MqGoodsBrowseDTO mqGoodsBrowseDTO = new MqGoodsBrowseDTO();
                GoodsBrowseMongoEntity goodsBrowseMongoEntity = values.get(values.size() - 1);
                BeanCopier.create(GoodsBrowseMongoEntity.class, MqGoodsBrowseDTO.class, false)
                        .copy(goodsBrowseMongoEntity, mqGoodsBrowseDTO, null);
                mqGoodsBrowseDTOList.add(mqGoodsBrowseDTO);
            }
            Collections.sort(mqGoodsBrowseDTOList, ((o1, o2) -> o2.getCreateTime().compareTo(o1.getCreateTime())));
        }
        return mqGoodsBrowseDTOList;
    }

}
