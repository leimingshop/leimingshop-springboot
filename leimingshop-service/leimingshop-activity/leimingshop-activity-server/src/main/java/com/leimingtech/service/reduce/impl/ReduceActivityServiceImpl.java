/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.service.reduce.impl;

import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.leimingtech.commons.mybatis.service.impl.BaseServiceImpl;
import com.leimingtech.commons.tools.page.PageData;
import com.leimingtech.commons.tools.utils.ConvertUtils;
import com.leimingtech.exception.ServiceException;
import com.leimingtech.modules.constant.ElasticSearchConstant;
import com.leimingtech.modules.dao.reduce.ReduceActivityDao;
import com.leimingtech.modules.dto.brand.BrandDTO;
import com.leimingtech.modules.dto.goods.GoodsDTO;
import com.leimingtech.modules.dto.reduce.*;
import com.leimingtech.modules.dto.storegoodsclass.StoreGoodsClassDTO;
import com.leimingtech.modules.entity.reduce.ReduceActivityEntity;
import com.leimingtech.modules.enums.ActivityTypeEnum;
import com.leimingtech.modules.enums.reduce.ReduceActivityEnum;
import com.leimingtech.modules.service.brand.BrandService;
import com.leimingtech.modules.service.cart.CartService;
import com.leimingtech.modules.service.goods.GoodsService;
import com.leimingtech.modules.service.order.OrderActivityService;
import com.leimingtech.modules.service.reduce.ReduceActivityService;
import com.leimingtech.modules.service.reduce.ReduceGoodsService;
import com.leimingtech.modules.service.reduce.ReduceRuleService;
import com.leimingtech.modules.service.storegoodsclass.StoreGoodsClassService;
import com.leimingtech.modules.statuscode.ActivityStatusCode;
import com.leimingtech.modules.utils.EsDataUtils;
import com.leimingtech.modules.vo.reduce.ReduceActivityIndexVo;
import com.leimingtech.mq.constant.MqConstant;
import com.leimingtech.mq.service.RabbitMqSendService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.beans.BeanCopier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 满减活动管理
 *
 * @author 刘远杰 2634443725@qq.com
 * @since v1.0.0 2019-12-26
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)

public class ReduceActivityServiceImpl extends BaseServiceImpl<ReduceActivityDao, ReduceActivityEntity> implements ReduceActivityService {

    @Autowired
    private ReduceGoodsService reduceGoodsService;

    @Autowired
    private StoreGoodsClassService storeGoodsClassService;

    @Autowired
    private ReduceRuleService reduceRuleService;

    @Autowired
    private GoodsService goodsService;

    @Autowired
    private BrandService brandService;

    @Autowired
    private CartService cartService;

    @Autowired
    private EsDataUtils esDataUtils;

    @Autowired
    private OrderActivityService orderActivityService;

    @Autowired
    private RabbitMqSendService rabbitMqSendService;

    /**
     * 满减列表
     *
     * @param params
     * @return java.util.List<com.leimingtech.modules.dto.reduce.ReduceActivityDTO>
     * @date 2019/12/27 17:28
     * @author weixianchun@leimingtech.com
     **/
    @Override

    public List<ReduceActivityDTO> list(@RequestParam Map<String, Object> params) {
        List<ReduceActivityEntity> entityList = baseDao.selectList(getWrapper(params));

        return ConvertUtils.sourceToTarget(entityList, ReduceActivityDTO.class);
    }

    /**
     * 构造器
     *
     * @param params
     * @return com.baomidou.mybatisplus.core.conditions.query.QueryWrapper<com.leimingtech.modules.entity.reduce.ReduceActivityEntity>
     * @date 2019/12/27 17:29
     * @author weixianchun@leimingtech.com
     **/
    private QueryWrapper<ReduceActivityEntity> getWrapper(Map<String, Object> params) {
        String id = (String) params.get("id");

        QueryWrapper<ReduceActivityEntity> wrapper = new QueryWrapper<>();
        wrapper.eq(StringUtils.isNotBlank(id), "id", id);

        return wrapper;
    }

    /**
     * 根据ID查询满减信息
     *
     * @param id 满减id
     * @return com.leimingtech.modules.dto.reduce.ReduceActivityDTO
     * @date 2019/12/27 17:29
     * @author weixianchun@leimingtech.com
     **/
    @Override

    public ReduceActivityDTO get(Long id) {
        ReduceActivityEntity entity = baseDao.selectById(id);
        ReduceActivityDTO reduceActivityDTO = new ReduceActivityDTO();
        BeanCopier.create(ReduceActivityEntity.class, ReduceActivityDTO.class, false)
                .copy(entity, reduceActivityDTO, null);
        return reduceActivityDTO;
    }

    /**
     * 功能描述：
     * <根据活动id集合促销活动>
     *
     * @param activityIds 满减活动id集合
     * @return
     * @date 2020/2/27 15:52
     * @author 刘远杰
     **/
    @Override

    public List<ReduceActivityDTO> getByIds(@RequestBody List<Long> activityIds) {
        QueryWrapper<ReduceActivityEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.in("id", activityIds);
        List<ReduceActivityEntity> entityList = baseDao.selectList(queryWrapper);
        return ConvertUtils.sourceToTarget(entityList, ReduceActivityDTO.class);
    }

    /**
     * 保存
     *
     * @param dto
     * @return void
     * @date 2019/12/27 17:30
     * @author weixianchun@leimingtech.com
     **/
    @Override
    @Transactional(rollbackFor = Exception.class)

    public void save(@RequestBody ReduceActivityDTO dto) {
        ReduceActivityEntity entity = ConvertUtils.sourceToTarget(dto, ReduceActivityEntity.class);

        insert(entity);
    }

    /**
     * 修改
     *
     * @param dto
     * @return java.lang.Boolean
     * @date 2019/12/27 17:30
     * @author weixianchun@leimingtech.com
     **/
    @Override

    public Boolean update(@RequestBody ReduceActivityDTO dto) {
        ReduceActivityEntity entity = ConvertUtils.sourceToTarget(dto, ReduceActivityEntity.class);

        return updateById(entity);
    }

    /**
     * 功能描述：
     * <满减活动停止>
     *
     * @param id 活动id
     * @return
     * @date 2020/1/10 10:06
     * @author 刘远杰
     **/

    @Override
    public Boolean stop(Long id) {
        // 创建满减活动修改实体，修改活动状态
        ReduceActivityDTO activityDTO = new ReduceActivityDTO();
        activityDTO.setId(id);
        activityDTO.setActivityState(ReduceActivityEnum.ACTIVITY_STATE_END.value());
        // 活动数据更新
        Boolean flag = this.update(activityDTO);

        if (flag) {
            List<Long> activityIds = Collections.singletonList(id);
            // 刪除满减活动es数据
            esDataUtils.bulkDelete(ElasticSearchConstant.REDUCE_ACTIVITY_INDEX, activityIds);
            // 异步维护满减标签
            rabbitMqSendService.sendMsg(MqConstant.LEIMINGTECH_GOODS_INDEX_REMOVE_REDUCE_TAG, JSONArray.toJSONString(activityIds));
            // 修改购物车
            cartService.deleteCartActivity(activityIds, ActivityTypeEnum.REDUCE_ACTIVITY.value());
        }

        return flag;
    }

    /**
     * 删除
     *
     * @param ids
     * @return void
     * @date 2019/12/27 17:30
     * @author weixianchun@leimingtech.com
     **/
    @Override
    @Transactional(rollbackFor = Exception.class)

    public void delete(@RequestBody Long[] ids) {

        //物理删除
        baseDao.deleteBatchIds(Arrays.asList(ids));
    }

    /**
     * 功能描述:
     * <分页查询满减列表>
     *
     * @param params
     * @return 满减分页实体
     * @date 2020/1/3 18:28
     * @author weixianchun
     **/

    @Override
    public PageData<ReduceActivityPageDTO> reduceActivityPage(@RequestParam Map<String, Object> params) {

        IPage<ReduceActivityEntity> page = getPage(params, null, false);
        //分页列表查询
        List<ReduceActivityPageDTO> list = baseDao.reduceActivityPage(page, params);
        if (CollectionUtils.isNotEmpty(list)) {

            for (ReduceActivityPageDTO reduceActivityPageDTO : list) {
                //查询下单数量(满减活动)
                int orderNum = orderActivityService.findOrderNum(ActivityTypeEnum.REDUCE_ACTIVITY.value(), reduceActivityPageDTO.getId());
                reduceActivityPageDTO.setOrderNum(orderNum);

                //根据活动ID查询规则信息根据类型拼接活动内容
                List<ReduceRuleDTO> reduceRuleDTOList = reduceRuleService.getByActivityId(reduceActivityPageDTO.getId());
                for (ReduceRuleDTO reduceRuleDTO : reduceRuleDTOList) {
                    Integer ruleType = reduceRuleDTO.getRuleType();
                    // 根据类型拼接活动内容 0普通满减 1每满减 2 阶梯满减
                    if (ReduceActivityEnum.RULE_TYPE_NORMAL.value() == ruleType) {
                        //满xxx元减xxx元 (普通满减)
                        StringBuilder builder = new StringBuilder();
                        StringBuilder appendStr = builder.append("满").append(reduceRuleDTO.getLimitAmount()).append("元减").append(reduceRuleDTO.getReduceAmount()).append("元");
                        reduceActivityPageDTO.setContent(String.valueOf(appendStr));
                    } else if (ReduceActivityEnum.EACH_RULE_TYPE.value() == ruleType) {
                        //每满xxx元减xxx元 (每满减)
                        StringBuilder stringBuilder = new StringBuilder();
                        StringBuilder appendStr = stringBuilder.append("每满").append(reduceRuleDTO.getLimitAmount()).append("元减").append(reduceRuleDTO.getReduceAmount()).append("元");
                        reduceActivityPageDTO.setContent(String.valueOf(appendStr));
                    } else {
                        //满xxx元减xxx元(阶梯满减)
                        StringBuilder build = new StringBuilder();
                        StringBuilder appendStr = build.append("满").append(reduceRuleDTO.getLimitAmount()).append("元减").append(reduceRuleDTO.getReduceAmount()).append("元");
                        if (StringUtils.isNotBlank(reduceActivityPageDTO.getContent())) {
                            reduceActivityPageDTO.setContent(reduceActivityPageDTO.getContent() + "," + appendStr);
                        } else {
                            reduceActivityPageDTO.setContent(String.valueOf(appendStr));
                        }

                    }
                }

            }
        }
        return new PageData<>(list, page.getTotal());
    }

    /**
     * 功能描述:
     * <满减详情>
     *
     * @param id      满减id
     * @param storeId 店铺id
     * @return 详情实体
     * @date 2020/1/3 18:22
     * @author weixianchun
     **/

    @Override
    public ReduceActivityDeatilDTO reduceActivityDeatil(@RequestParam("id") Long id, @RequestParam("storeId") Long storeId) {
        return baseDao.reduceActivityDeatil(id, storeId);
    }

    /**
     * 功能描述:
     * <查询店铺满减活动>
     *
     * @param id
     * @param storeId
     * @return
     * @date 2020/1/3 18:23
     * @author weixianchun
     **/

    @Override
    public ReduceActivityDTO findByIdAndStoreId(Long id, @RequestParam("storeId") Long storeId) {
        QueryWrapper<ReduceActivityEntity> queryWrapper = new QueryWrapper<>();
        // 根据满减ID,店铺ID查询信息
        queryWrapper.eq("id", id).eq("store_id", storeId);
        ReduceActivityEntity entity = baseDao.selectOne(queryWrapper);
        return ConvertUtils.sourceToTarget(entity, ReduceActivityDTO.class);
    }

    /**
     * @Author weixianchun
     * @Description 根据ID删除满减及关联商品信息
     * @Param id
     * @Date 2019/12/26 14:06
     * @Return java.lang.Boolean
     * @version 1.0
     */

    @Override
    public Boolean deleteByActivityId(Long id) {
        //删除满减商品关联信息
        reduceGoodsService.deleteByActivityId(id);

        //删除数据库满减活动信息
        return super.deleteById(id);
    }

    /**
     * 功能描述:
     * 〈获取进行中的满减活动〉
     *
     * @param
     * @author : 刘远杰
     */

    @Override
    public List<ReduceActivityIndexDTO> selectStartReduceActivity() {
        QueryWrapper<ReduceActivityEntity> queryWrapper = new QueryWrapper<>();
        // 查询条件 活动状态为开始或者已结束
        queryWrapper.eq("activity_state", ReduceActivityEnum.ACTIVITY_STATE_START.value());
        List<ReduceActivityEntity> entityList = baseDao.selectList(queryWrapper);
        List<ReduceActivityIndexDTO> reduceActivityIndexDTOList = getReduceRuleAndGoods(entityList);
        return reduceActivityIndexDTOList;
    }

    /**
     * 功能描述:
     * 〈根据活动id获取满减活动es数据〉
     *
     * @param
     * @author : 刘远杰
     */

    @Override
    public List<ReduceActivityIndexDTO> selectReduceActivityEsByIds(@RequestBody List<Long> ids) {
        QueryWrapper<ReduceActivityEntity> queryWrapper = new QueryWrapper<>();
        // 查询条件 活动状态为开始或者已结束
        queryWrapper.in("id", ids);
        List<ReduceActivityEntity> entityList = baseDao.selectList(queryWrapper);
        List<ReduceActivityIndexDTO> reduceActivityIndexDTOList = getReduceRuleAndGoods(entityList);
        return reduceActivityIndexDTOList;
    }

    /**
     * 功能描述:
     * 〈获得满减活动规则及属性信息〉
     *
     * @param entityList 活动集合
     * @author : 刘远杰
     */
    private List<ReduceActivityIndexDTO> getReduceRuleAndGoods(List<ReduceActivityEntity> entityList) {
        List<ReduceActivityIndexDTO> reduceActivityIndexDTOList = new ArrayList<>();
        entityList.forEach(reduceActivityEntity -> {
            ReduceActivityIndexDTO reduceActivityIndexDTO = new ReduceActivityIndexDTO();
            BeanCopier.create(ReduceActivityEntity.class, ReduceActivityIndexDTO.class, false)
                    .copy(reduceActivityEntity, reduceActivityIndexDTO, null);
            // 获取活动关联商品类型
            List<ReduceGoodsDTO> reduceGoodsDTOList = reduceGoodsService.getByActivityId(reduceActivityEntity.getId());
            reduceActivityIndexDTO.setGoodsList(reduceGoodsDTOList);
            // 获取活动关联规则
            List<ReduceRuleDTO> reduceRuleDTOList = reduceRuleService.getByActivityId(reduceActivityEntity.getId());
            reduceActivityIndexDTO.setRuleList(reduceRuleDTOList);
            reduceActivityIndexDTOList.add(reduceActivityIndexDTO);
        });

        return reduceActivityIndexDTOList;
    }

    /**
     * 功能描述:
     * <满减活动保存>
     *
     * @param dto
     * @return
     * @date 2020/1/3 18:24
     * @author weixianchun
     **/

    @Override
    public Boolean saveReduceActivity(@RequestBody ReduceAndGoodsDTO dto) {

        ReduceActivityEntity reduceActivityEntity = ConvertUtils.sourceToTarget(dto, ReduceActivityEntity.class);

        // 封装满减商品数据并保存
        saveReduceGoodsBatch(dto);

        //满减规则信息批量保存
        String activityRuleName = saveReduceRule(dto.getReduceRuleSaveDTOList(), dto.getId());

        reduceActivityEntity.setActivityRuleName(activityRuleName);

        // 保存满减活动
        boolean flag = super.insert(reduceActivityEntity);

        return flag;
    }

    /**
     * 功能描述:
     * <满减规则信息批量保存(封装)>
     *
     * @param reduceRuleSaveDTOList
     * @param activityId
     * @return
     * @date 2020/1/3 18:25
     * @author weixianchun
     **/
    private String saveReduceRule(List<ReduceRuleSaveDTO> reduceRuleSaveDTOList, Long activityId) {
        StringBuilder appendStr = new StringBuilder();
        if (CollectionUtils.isNotEmpty(reduceRuleSaveDTOList)) {
            reduceRuleSaveDTOList.forEach(reduceRuleSaveDTO -> {
                Integer ruleType = reduceRuleSaveDTO.getRuleType();
                // 根据类型拼接活动内容 0普通满减 1每满减 2 阶梯满减
                if (ReduceActivityEnum.RULE_TYPE_NORMAL.value() == ruleType) {
                    //满xxx元减xxx元 (普通满减)
                    appendStr.append("满")
                            .append(reduceRuleSaveDTO.getLimitAmount().stripTrailingZeros().toPlainString())
                            .append("元减")
                            .append(reduceRuleSaveDTO.getReduceAmount().stripTrailingZeros().toPlainString())
                            .append("元");
                } else if (ReduceActivityEnum.EACH_RULE_TYPE.value() == ruleType) {
                    //每满xxx元减xxx元 (每满减)
                    appendStr.append("每满")
                            .append(reduceRuleSaveDTO.getLimitAmount().stripTrailingZeros().toPlainString())
                            .append("元减")
                            .append(reduceRuleSaveDTO.getReduceAmount().stripTrailingZeros().toPlainString())
                            .append("元");
                } else {
                    //满xxx元减xxx元(阶梯满减)
                    if (appendStr.length() > 0) {
                        appendStr.append(",满")
                                .append(reduceRuleSaveDTO.getLimitAmount().stripTrailingZeros().toPlainString())
                                .append("元减")
                                .append(reduceRuleSaveDTO.getReduceAmount().stripTrailingZeros().toPlainString())
                                .append("元");
                    } else {
                        appendStr.append(",满")
                                .append(reduceRuleSaveDTO.getLimitAmount().stripTrailingZeros().toPlainString())
                                .append("元减")
                                .append(reduceRuleSaveDTO.getReduceAmount().stripTrailingZeros().toPlainString())
                                .append("元");
                    }
                }
                reduceRuleSaveDTO.setActivityId(activityId);
            });
            reduceRuleService.saveBatch(reduceRuleSaveDTOList);
        }
        return appendStr.toString();
    }

    /**
     * 功能描述:
     * 〈根据指定关联id获取进行中的活动〉
     *
     * @param relationId         关联id
     * @param activityGoodsScope 关联商品类型
     * @author : 刘远杰
     */

    @Override
    public List<ReduceActivityDTO> getStartByRelationId(@RequestParam("relationId") Long relationId,
                                                        @RequestParam("activityGoodsScope") Integer activityGoodsScope) {
        List<ReduceActivityEntity> entityList = baseDao.selectStartByRelationId(relationId, activityGoodsScope);
        return ConvertUtils.sourceToTarget(entityList, ReduceActivityDTO.class);
    }

    /**
     * 功能描述:
     * 〈查询指定商品id，店铺id，品牌id商品的最优满减〉
     *
     * @param goodsId 商品id
     * @param storeId 店铺id
     * @param brandId 品牌id
     * @author : 刘远杰
     */

    @Override
    public ReduceActivityDTO getGoodsBestActivity(@RequestParam("goodsId") Long goodsId,
                                                  @RequestParam(value = "storeId") Long storeId,
                                                  @RequestParam(value = "brandId", required = false) Long brandId,
                                                  @RequestParam(value = "secondStoreClassId", required = false) Long secondStoreClassId) {
        ReduceActivityEntity entity = baseDao.getGoodsBestActivity(goodsId, storeId, brandId, secondStoreClassId);
        if (entity != null) {
            ReduceActivityDTO reduceActivityDTO = new ReduceActivityDTO();
            BeanCopier.create(ReduceActivityEntity.class, ReduceActivityDTO.class, false)
                    .copy(entity, reduceActivityDTO, null);
            return reduceActivityDTO;
        } else {
            return null;
        }
    }

    /**
     * 功能描述:
     * 〈查询指定商品id，店铺id，品牌id商品的全部满减活动及活动规则〉
     *
     * @param goodsId 商品id
     * @param storeId 店铺id
     * @param brandId 品牌id
     * @author : 刘远杰
     */

    @Override
    public List<ReduceActivityIndexDTO> getGoodsAllActivity(@RequestParam("goodsId") Long goodsId,
                                                            @RequestParam(value = "storeId") Long storeId,
                                                            @RequestParam(value = "brandId", required = false) Long brandId,
                                                            @RequestParam(value = "secondStoreClassId", required = false) Long secondStoreClassId) {
        List<ReduceActivityIndexDTO> goodsAllActivity = baseDao.getGoodsAllActivity(goodsId, storeId, brandId, secondStoreClassId);
        return goodsAllActivity;
    }

    /**
     * 功能描述：
     * <查询所有满减活动关联商品>
     *
     * @param storeId 店铺id
     * @param brandId 品牌id
     * @param goodsId 商品id
     * @return
     * @date 2020/3/10 13:59
     * @author 刘远杰
     **/
    @Override

    public List<ReduceGoodsDTO> getAllReduceGoods(@RequestParam("storeId") Long storeId,
                                                  @RequestParam(value = "brandId", required = false) Long brandId,
                                                  @RequestParam(value = "goodsId", required = false) Long goodsId) {
        return baseDao.selectAllReduceGoods(storeId, brandId, storeId);
    }

    /**
     * 功能描述：
     * <根据店铺查询所有满减活动关联商品>
     *
     * @param storeId 店铺id
     * @return
     * @date 2020/3/10 13:59
     * @author 刘远杰
     **/
    @Override

    public List<ReduceGoodsDTO> selectAllReduceGoodsByStoreId(@RequestParam("storeId") Long storeId) {
        return baseDao.selectAllReduceGoodsByStoreId(storeId);
    }

    /**
     * 功能描述:
     * <封装满减商品数据并保存>
     *
     * @param dto
     * @return
     * @date 2020/1/3 18:24
     * @author weixianchun
     **/
    private void saveReduceGoodsBatch(@RequestBody ReduceAndGoodsDTO dto) {

        //满减活动关联商品集合
        List<ReduceGoodsDTO> reduceGoodsDTOList = dto.getReduceGoodsDTOList();
        if (CollectionUtils.isNotEmpty(reduceGoodsDTOList)) {
            if (ReduceActivityEnum.ACTIVITY_SCOPE_STORE.value() == dto.getActivityScope()) {
                // 店铺满减活动，填充店铺名称
                if (ReduceActivityEnum.ACTIVITY_GOODS_SCOPE_ALL.value() == dto.getActivityGoodsScope()) {
                    // 全场通用
                    for (ReduceGoodsDTO reduceGoodsDTO : reduceGoodsDTOList) {
                        reduceGoodsDTO.setRelationName(dto.getStoreName());
                    }
                } else if (ReduceActivityEnum.ACTIVITY_GOODS_SCOPE_CLASS.value() == dto.getActivityGoodsScope()) {

                    List<Long> classId = reduceGoodsDTOList.stream().map(ReduceGoodsDTO::getRelationId).collect(Collectors.toList());
                    List<StoreGoodsClassDTO> classDTOList = storeGoodsClassService.getClassById(classId);
                    if (classDTOList.size() != classId.size()) {
                        // 存在未查询到的分类，抛出异常
                        throw new ServiceException(ActivityStatusCode.SAVE_REDUCE_ACTIVITY_NOTFOUND_CLASS);
                    }
                    // 清空集合重新填充
                    reduceGoodsDTOList.clear();
                    classDTOList.forEach(classDTO -> {
                        ReduceGoodsDTO reduceGoodsDTO = new ReduceGoodsDTO();
                        reduceGoodsDTO.setActivityId(dto.getId());
                        reduceGoodsDTO.setActivityGoodsScope(dto.getActivityGoodsScope());
                        reduceGoodsDTO.setRelationId(classDTO.getId());
                        reduceGoodsDTO.setRelationName(classDTO.getGcName());
                        reduceGoodsDTOList.add(reduceGoodsDTO);
                    });

                } else if (ReduceActivityEnum.ACTIVITY_GOODS_SCOPE_GOODS.value() == dto.getActivityGoodsScope()) {
                    // 指定商品，查询商品名称填充
                    List<Long> goodsIds = reduceGoodsDTOList.stream().map(ReduceGoodsDTO::getRelationId).collect(Collectors.toList());
                    List<GoodsDTO> goodsDTOList = goodsService.getByGoodsIds(goodsIds, dto.getStoreId());
                    if (goodsDTOList.size() != goodsIds.size()) {
                        // 存在未查询到的商品
                        throw new ServiceException(ActivityStatusCode.SAVE_REDUCE_ACTIVITY_NOTFOUND_GOODS);
                    }
                    // 清空数组重新填充
                    reduceGoodsDTOList.clear();
                    goodsDTOList.forEach(goodsDTO -> {
                        ReduceGoodsDTO reduceGoodsDto = new ReduceGoodsDTO();
                        reduceGoodsDto.setActivityId(dto.getId());
                        reduceGoodsDto.setActivityGoodsScope(dto.getActivityGoodsScope());
                        reduceGoodsDto.setRelationId(goodsDTO.getId());
                        reduceGoodsDto.setRelationName(goodsDTO.getGoodsName());
                        reduceGoodsDTOList.add(reduceGoodsDto);
                    });
                } else if (ReduceActivityEnum.ACTIVITY_GOODS_SCOPE_BRAND.value() == dto.getActivityGoodsScope()) {
                    // 指定品牌，查询品牌名称填充
                    List<Long> brandIds = reduceGoodsDTOList.stream().map(ReduceGoodsDTO::getRelationId).collect(Collectors.toList());
                    List<BrandDTO> brandDTOList = brandService.getByBrandIds(brandIds, null);
                    if (brandDTOList.size() != brandIds.size()) {
                        // 存在未查询到的商品
                        throw new ServiceException(ActivityStatusCode.SAVE_REDUCE_ACTIVITY_NOTFOUND_BRAND);
                    }
                    // 清空数组重新填充
                    reduceGoodsDTOList.clear();
                    brandDTOList.forEach(brandDTO -> {
                        ReduceGoodsDTO reduceGoodsDto = new ReduceGoodsDTO();
                        reduceGoodsDto.setActivityId(dto.getId());
                        reduceGoodsDto.setActivityGoodsScope(dto.getActivityGoodsScope());
                        reduceGoodsDto.setRelationId(brandDTO.getId());
                        reduceGoodsDto.setRelationName(brandDTO.getBrandName());
                        reduceGoodsDTOList.add(reduceGoodsDto);
                    });
                }
            } else if (ReduceActivityEnum.ACTIVITY_SCOPE_PLATFORM.value() == dto.getActivityScope()) {
                // TODO: 2019/12/5  本期不做平台满减
                dto.setReduceGoodsDTOList(Collections.emptyList());
            }
            // 批量保存满减商品数据
            reduceGoodsService.saveBatch(reduceGoodsDTOList);
        }
    }

    /**
     * 功能描述:
     * <满减活动开始定时任务>
     *
     * @param time
     * @return
     * @date 2020/1/3 18:25
     * @author weixianchun
     **/

    @Override
    public void startActivityTiming(@RequestParam("time") Long time) {

        Date now = new Date(time);
        // 未开始，已审核，开始时间小于等于当前时间，结束时间大于当前时间
        UpdateWrapper<ReduceActivityEntity> updateWrapper = new UpdateWrapper<>();
        updateWrapper.le("start_date", now).gt("end_date", now)
                .eq("activity_state", ReduceActivityEnum.ACTIVITY_STATE_PREPARE.value())
                .eq("audit_state", ReduceActivityEnum.AUDIT_STATE_PASS.value());
        ReduceActivityEntity entity = new ReduceActivityEntity();
        // 获取所有即将更新的满减活动集合
        List<ReduceActivityEntity> entityList = baseDao.selectList(updateWrapper);
        if (CollectionUtils.isNotEmpty(entityList)) {
            // 更新数据库活动状态(开始)
            entity.setActivityState(ReduceActivityEnum.ACTIVITY_STATE_START.value());
            baseDao.update(entity, updateWrapper);

            // 修改满减活动es状态
            List<ReduceActivityIndexDTO> reduceActivityIndexDTOList = new ArrayList<>();
            entityList.forEach(reduceActivityEntity -> {
                ReduceActivityIndexDTO reduceActivityIndexDTO = new ReduceActivityIndexDTO();
                BeanCopier.create(ReduceActivityEntity.class, ReduceActivityIndexDTO.class, false)
                        .copy(reduceActivityEntity, reduceActivityIndexDTO, null);

                // 获取活动关联商品类型
                List<ReduceGoodsDTO> reduceGoodsDTOList = reduceGoodsService.getByActivityId(reduceActivityEntity.getId());
                reduceActivityIndexDTO.setGoodsList(reduceGoodsDTOList);
                // 获取活动关联商品类型
                reduceActivityIndexDTO.setRuleType(reduceActivityEntity.getRuleType());
                List<ReduceRuleDTO> ruleList = reduceRuleService.getByActivityId(reduceActivityEntity.getId());
                reduceActivityIndexDTO.setRuleList(ruleList);
                reduceActivityIndexDTOList.add(reduceActivityIndexDTO);
            });
            // 发送mq更新es活动数据
            esDataUtils.saveDataBatch(ElasticSearchConstant.REDUCE_ACTIVITY_INDEX, "id",
                    JSONArray.toJSONString(reduceActivityIndexDTOList), ReduceActivityIndexVo.class);
            // 异步维护满减标签
            List<Long> activityIds = reduceActivityIndexDTOList.stream().map(ReduceActivityIndexDTO::getId).collect(Collectors.toList());
            rabbitMqSendService.sendMsg(MqConstant.LEIMINGTECH_GOODS_INDEX_ADD_REDUCE_TAG, JSONArray.toJSONString(activityIds));
        }
    }


    /**
     * 功能描述:
     * <满减活动结束定时任务>
     *
     * @param time
     * @return
     * @date 2020/1/3 18:25
     * @author weixianchun
     **/

    @Override
    public void stopActivityTiming(@RequestParam("time") Long time) {

        Date now = new Date(time);
        // 未开始或已开始，已审核,结束时间小于等于当前时间
        UpdateWrapper<ReduceActivityEntity> updateWrapper = new UpdateWrapper<>();
        updateWrapper.le("end_date", now)
                .in("activity_state", ReduceActivityEnum.ACTIVITY_STATE_PREPARE.value(), ReduceActivityEnum.ACTIVITY_STATE_START.value())
                .eq("audit_state", ReduceActivityEnum.AUDIT_STATE_PASS.value());
        // 获取所有即将更新的满减活动集合
        List<ReduceActivityEntity> entityList = baseDao.selectList(updateWrapper);
        if (CollectionUtils.isNotEmpty(entityList)) {
            ReduceActivityEntity entity = new ReduceActivityEntity();
            entity.setActivityState(ReduceActivityEnum.ACTIVITY_STATE_END.value());
            //更新数据库活动状态(结束)
            baseDao.update(entity, updateWrapper);

            List<Long> activityIds = entityList.stream().map(ReduceActivityEntity::getId).collect(Collectors.toList());
            // 修改购物车
            cartService.deleteCartActivity(activityIds, ActivityTypeEnum.REDUCE_ACTIVITY.value());

            // 发送mq删除es活动数据goGoodsList
            esDataUtils.bulkDelete(ElasticSearchConstant.REDUCE_ACTIVITY_INDEX, activityIds);
            // 满减活动商品es满减标签维护
            List<ReduceActivityIndexDTO> reduceActivityIndexDTOList = this.selectReduceActivityEsByIds(activityIds);
            // 异步维护满减标签
            rabbitMqSendService.sendMsg(MqConstant.LEIMINGTECH_GOODS_INDEX_REMOVE_REDUCE_TAG, JSONArray.toJSONString(activityIds));
        }
    }

    /**
     * 功能描述:
     * <满减活动编辑>
     *
     * @param dto
     * @return
     * @date 2020/1/3 18:26
     * @author weixianchun
     **/

    @Override
    public Boolean editReduceActivity(@RequestBody ReduceAndGoodsDTO dto) {
        ReduceActivityEntity reduceActivityEntity = ConvertUtils.sourceToTarget(dto, ReduceActivityEntity.class);

        //删除原满减关联商品数据
        reduceGoodsService.deleteByActivityId(reduceActivityEntity.getId());

        //删除原满减规则
        reduceRuleService.deleteByActivityId(reduceActivityEntity.getId());

        // 封装满减商品数据并保存
        saveReduceGoodsBatch(dto);

        //满减规则信息批量保存
        String activityRuleName = saveReduceRule(dto.getReduceRuleSaveDTOList(), dto.getId());

        reduceActivityEntity.setActivityRuleName(activityRuleName);

        //修改满减信息
        return super.updateById(reduceActivityEntity);
    }

}

