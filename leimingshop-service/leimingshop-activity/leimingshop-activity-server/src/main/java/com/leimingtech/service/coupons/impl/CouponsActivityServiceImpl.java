/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.service.coupons.impl;

import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.leimingtech.commons.mybatis.enums.DelFlagEnum;
import com.leimingtech.commons.mybatis.service.impl.BaseServiceImpl;
import com.leimingtech.commons.tools.page.PageData;
import com.leimingtech.commons.tools.utils.ConvertUtils;
import com.leimingtech.commons.tools.utils.DateUtils;
import com.leimingtech.commons.tools.utils.IdGenerator;
import com.leimingtech.exception.ServiceException;
import com.leimingtech.modules.constant.ElasticSearchConstant;
import com.leimingtech.modules.dao.coupons.CouponsActivityDao;
import com.leimingtech.modules.dto.brand.BrandDTO;
import com.leimingtech.modules.dto.coupons.*;
import com.leimingtech.modules.dto.goods.GoodsDTO;
import com.leimingtech.modules.dto.member.MemberDTO;
import com.leimingtech.modules.dto.storegoodsclass.StoreGoodsClassDTO;
import com.leimingtech.modules.entity.coupons.CouponsActivityEntity;
import com.leimingtech.modules.enums.coupons.CouponsEnum;
import com.leimingtech.modules.service.brand.BrandService;
import com.leimingtech.modules.service.coupons.CouponsActivityService;
import com.leimingtech.modules.service.coupons.CouponsGoodsService;
import com.leimingtech.modules.service.coupons.MemberCouponsService;
import com.leimingtech.modules.service.goods.GoodsService;
import com.leimingtech.modules.service.member.MemberService;
import com.leimingtech.modules.service.storegoodsclass.StoreGoodsClassService;
import com.leimingtech.modules.statuscode.ActivityStatusCode;
import com.leimingtech.modules.utils.EsDataUtils;
import com.leimingtech.modules.vo.coupons.CouponsActivityIndexVo;
import com.leimingtech.modules.vo.coupons.MemberCouponsIndexVo;
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
 * 优惠券活动service
 *
 * @author 刘远杰 2634443725@qq.com
 * @since v1.0.0 2019-12-05
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)

public class CouponsActivityServiceImpl extends BaseServiceImpl<CouponsActivityDao, CouponsActivityEntity>
        implements CouponsActivityService {

    @Autowired
    private CouponsGoodsService couponsGoodsService;
    @Autowired
    private MemberCouponsService memberCouponsService;
    @Autowired
    private EsDataUtils esDataUtils;

    @Autowired
    private GoodsService goodsService;

    @Autowired
    private BrandService brandService;

    @Autowired
    private MemberService memberService;

    @Autowired
    private StoreGoodsClassService storeGoodsClassService;

    /**
     * 功能描述：
     * <后台优惠券分页>
     *
     * @param params 查询条件
     * @return
     * @date 2020/1/17 14:53
     * @author 刘远杰
     **/
    @Override

    public PageData<AdminCouponsActivityPageDTO> adminPage(@RequestParam Map<String, Object> params) {
        IPage<CouponsActivityEntity> page = getPage(params, null, false);
        //查询
        List<AdminCouponsActivityPageDTO> list = baseDao.adminPage(page, params);

        return new PageData<>(list, page.getTotal());
    }

    /**
     * 功能描述：
     * <优惠券条件查询>
     *
     * @param params 查询条件
     * @date 2020/1/17 14:53
     * @author 刘远杰
     **/
    @Override

    public List<CouponsActivityDTO> list(Map<String, Object> params) {
        List<CouponsActivityEntity> entityList = baseDao.selectList(getWrapper(params));

        return ConvertUtils.sourceToTarget(entityList, CouponsActivityDTO.class);
    }

    private QueryWrapper<CouponsActivityEntity> getWrapper(Map<String, Object> params) {
        String id = (String) params.get("id");

        QueryWrapper<CouponsActivityEntity> wrapper = new QueryWrapper<>();
        wrapper.eq(StringUtils.isNotBlank(id), "id", id);

        return wrapper;
    }

    @Override

    public CouponsActivityDTO get(Long id) {
        CouponsActivityEntity entity = baseDao.selectById(id);

        return ConvertUtils.sourceToTarget(entity, CouponsActivityDTO.class);
    }

    /**
     * 功能描述：
     * <查询优惠券活动集合>
     *
     * @param ids 优惠券活动id集合
     * @return
     * @date 2020/2/24 10:23
     * @author 刘远杰
     **/

    @Override
    public List<CouponsActivityDTO> getByIds(@RequestBody List<Long> ids) {
        // 构造查询条件 根据id范围查询
        QueryWrapper<CouponsActivityEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.in("id", ids);
        // 查询
        List<CouponsActivityEntity> entityList = baseDao.selectList(queryWrapper);
        return ConvertUtils.sourceToTarget(entityList, CouponsActivityDTO.class);
    }

    /**
     * 功能描述:
     * 〈获取店铺优惠券活动〉
     *
     * @param id      活动id
     * @param storeId 店铺id
     * @author : 刘远杰
     */

    @Override
    public CouponsActivityDTO getByIdAndStoreId(@RequestParam("id") Long id, @RequestParam("storeId") Long storeId) {
        // 构造查询条件  根据店铺id、优惠券id等值查询
        QueryWrapper<CouponsActivityEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id", id).eq("store_id", storeId);
        // 查询
        CouponsActivityEntity entity = baseDao.selectOne(queryWrapper);
        return ConvertUtils.sourceToTarget(entity, CouponsActivityDTO.class);
    }

    /**
     * 功能描述:
     * 〈后台优惠券详情查询〉
     *
     * @param id      优惠券活动id
     * @param storeId 店铺id
     * @author : 刘远杰
     */

    @Override
    public AdminCouponsActivityDetailDTO adminDetail(Long id,
                                                     @RequestParam(value = "storeId", required = false) Long storeId) {
        // 根据店铺id、优惠券id等值查询 计算领券数量、使用数量
        return baseDao.adminDetail(id, storeId);
    }

    @Override

    public void save(@RequestBody CouponsActivityDTO dto) {
        CouponsActivityEntity entity = ConvertUtils.sourceToTarget(dto, CouponsActivityEntity.class);

        insert(entity);
    }

    /**
     * 功能描述:
     * 〈新增优惠券活动〉
     *
     * @param dto 优惠券活动及活动商品
     * @author : 刘远杰
     */

    @Override
    public Boolean saveCouponsActivity(@RequestBody CouponsActivityAndGoodsDTO dto) {
        CouponsActivityEntity couponsActivityEntity = ConvertUtils.sourceToTarget(dto, CouponsActivityEntity.class);
        // 1.封装优惠券商品数据并保存
        saveCouponGoodsBatch(dto);
        // 2.保存优惠券活动
        return super.insert(couponsActivityEntity);
    }

    /**
     * 功能描述:
     * 〈活动编辑〉
     *
     * @param dto 修改实体
     * @author : 刘远杰
     */
    @Override

    public Boolean update(@RequestBody CouponsActivityDTO dto) {
        CouponsActivityEntity entity = ConvertUtils.sourceToTarget(dto, CouponsActivityEntity.class);

        return updateById(entity);
    }

    /**
     * 功能描述：
     * <优惠券活动停止>
     *
     * @param id 活动id
     * @date 2020/1/10 10:06
     * @author 刘远杰
     **/

    @Override
    public Boolean stop(Long id) {
        // 1.创建优惠券活动修改实体，修改活动状态
        CouponsActivityDTO activityDTO = new CouponsActivityDTO();
        activityDTO.setId(id);
        activityDTO.setActivityState(CouponsEnum.ACTIVITY_STATE_END.value());
        // 2.活动状态更新
        Boolean flag = this.update(activityDTO);

        if (flag) {
            // 2.更新优惠券活动es
            CouponsActivityIndexDTO couponsActivityIndexDTO = new CouponsActivityIndexDTO();
            BeanCopier.create(CouponsActivityDTO.class, CouponsActivityIndexDTO.class, false)
                    .copy(activityDTO, couponsActivityIndexDTO, null);
            esDataUtils.updateDataBatch(ElasticSearchConstant.COUPONS_ACTIVITY_INDEX, "id",
                    JSONArray.toJSONString(Collections.singletonList(couponsActivityIndexDTO)));
        }
        return flag;
    }

    /**
     * 功能描述:
     * 〈编辑优惠券活动〉
     *
     * @param dto 优惠券活动及活动商品
     * @author : 刘远杰
     */

    @Override
    public Boolean editCouponsActivity(@RequestBody CouponsActivityAndGoodsDTO dto) {
        CouponsActivityEntity couponsActivityEntity = ConvertUtils.sourceToTarget(dto, CouponsActivityEntity.class);
        // 1.删除原优惠券关联商品
        couponsGoodsService.noLogicDeleteByActivityId(dto.getId());
        // 2.封装优惠券商品数据并保存
        saveCouponGoodsBatch(dto);
        // 3.修改优惠券信息
        return super.updateById(couponsActivityEntity);
    }

    @Override

    public void delete(@RequestBody Long[] ids) {

        //物理删除
        baseDao.deleteBatchIds(Arrays.asList(ids));
    }

    /**
     * 功能描述:
     * 〈优惠券活动删除〉
     *
     * @param id 优惠券活动id
     * @author : 刘远杰
     */

    @Override
    public Boolean deleteCouponsActivityById(Long id) {
        // 逻辑删除关联商品及优惠券活动
        couponsGoodsService.deleteByActivityId(id);
        return super.deleteById(id);
    }

    /**
     * 功能描述:
     * 〈优惠券活动开始定时任务〉
     *
     * @param time 当前时间
     * @author : 刘远杰
     */

    @Override
    public void startActivityTiming(@RequestParam("time") Long time) {
        Date now = new Date(time);
        // 未开始，已审核，未删除，开始时间小于等于当前时间，结束时间大于当前时间
        UpdateWrapper<CouponsActivityEntity> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("del_flag", DelFlagEnum.NORMAL.value())
                .le("get_start_date", now).gt("get_end_date", now)
                .eq("activity_state", CouponsEnum.ACTIVITY_STATE_PREPARE.value())
                .eq("audit_state", CouponsEnum.AUDIT_STATE_PASS.value());
        CouponsActivityEntity entity = new CouponsActivityEntity();
        // 获取所有即将更新的优惠券活动集合
        List<CouponsActivityEntity> entityList = baseDao.selectList(updateWrapper);

        if (CollectionUtils.isNotEmpty(entityList)) {
            // 更新活动状态
            entity.setActivityState(CouponsEnum.ACTIVITY_STATE_START.value());
            baseDao.update(entity, updateWrapper);

            // 修改优惠券es
            List<CouponsActivityIndexDTO> couponsActivityIndexList = new ArrayList<>();
            entityList.forEach(couponsActivityEntity -> {
                CouponsActivityIndexDTO couponsActivityIndexDTO = new CouponsActivityIndexDTO();
                BeanCopier.create(CouponsActivityEntity.class, CouponsActivityIndexDTO.class, false)
                        .copy(couponsActivityEntity, couponsActivityIndexDTO, null);
                couponsActivityIndexDTO.setActivityState(CouponsEnum.ACTIVITY_STATE_START.value());
                // 获取活动关联商品类型
                List<CouponsGoodsDTO> couponsGoodsList = couponsGoodsService.getByActivityId(couponsActivityEntity.getId());
                couponsActivityIndexDTO.setGoodsList(couponsGoodsList);
                couponsActivityIndexList.add(couponsActivityIndexDTO);
            });
            // 发送mq更新es活动数据
            esDataUtils.saveDataBatch(ElasticSearchConstant.COUPONS_ACTIVITY_INDEX, "id",
                    JSONArray.toJSONString(couponsActivityIndexList), CouponsActivityIndexVo.class);
        }

    }

    /**
     * 功能描述:
     * 〈优惠券活动结束定时任务〉
     *
     * @param time 当前时间
     * @author : 刘远杰
     */

    @Override
    public void stopActivityTiming(@RequestParam("time") Long time) {
        Date now = new Date(time);
        // 未开始或已开始，已审核，未删除，结束时间小于等于当前时间
        UpdateWrapper<CouponsActivityEntity> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("del_flag", DelFlagEnum.NORMAL.value())
                .le("get_end_date", now)
                .in("activity_state", CouponsEnum.ACTIVITY_STATE_PREPARE.value(), CouponsEnum.ACTIVITY_STATE_START.value())
                .eq("audit_state", CouponsEnum.AUDIT_STATE_PASS.value());
        // 获取所有即将更新的优惠券活动集合
        List<CouponsActivityEntity> entityList = baseDao.selectList(updateWrapper);

        if (CollectionUtils.isNotEmpty(entityList)) {
            CouponsActivityEntity entity = new CouponsActivityEntity();
            entity.setActivityState(CouponsEnum.ACTIVITY_STATE_END.value());
            baseDao.update(entity, updateWrapper);

            // 修改优惠券es
            List<CouponsActivityIndexDTO> couponsActivityIndexDTOList = new ArrayList<>();
            entityList.forEach(couponsActivityEntity -> {
                CouponsActivityIndexDTO couponsActivityIndexDTO = new CouponsActivityIndexDTO();
                BeanCopier.create(CouponsActivityEntity.class, CouponsActivityIndexDTO.class, false)
                        .copy(couponsActivityEntity, couponsActivityIndexDTO, null);
                couponsActivityIndexDTO.setActivityState(CouponsEnum.ACTIVITY_STATE_END.value());
                // 获取活动关联商品类型
                List<CouponsGoodsDTO> couponsGoodsDTOList = couponsGoodsService.getByActivityId(couponsActivityEntity.getId());
                couponsActivityIndexDTO.setGoodsList(couponsGoodsDTOList);
                couponsActivityIndexDTOList.add(couponsActivityIndexDTO);
            });
            // 发送mq更新es活动数据
            esDataUtils.saveDataBatch(ElasticSearchConstant.COUPONS_ACTIVITY_INDEX, "id",
                    JSONArray.toJSONString(couponsActivityIndexDTOList), CouponsActivityIndexVo.class);
        }

    }

    /**
     * 功能描述:
     * 〈获取进行中、已结束的优惠券活动〉
     *
     * @author : 刘远杰
     */

    @Override
    public List<CouponsActivityIndexDTO> selectStartCouponsActivity() {
        QueryWrapper<CouponsActivityEntity> queryWrapper = new QueryWrapper<>();
        // 查询条件 活动状态为开始或者已结束
        queryWrapper.in("activity_state", CouponsEnum.ACTIVITY_STATE_START.value(), CouponsEnum.ACTIVITY_STATE_END.value());
        List<CouponsActivityEntity> entityList = baseDao.selectList(queryWrapper);
        List<CouponsActivityIndexDTO> couponsActivityIndexDTOList = new ArrayList<>();
        entityList.forEach(couponsActivityEntity -> {
            CouponsActivityIndexDTO couponsActivityIndexDTO = new CouponsActivityIndexDTO();
            BeanCopier.create(CouponsActivityEntity.class, CouponsActivityIndexDTO.class, false)
                    .copy(couponsActivityEntity, couponsActivityIndexDTO, null);
            // 获取活动关联商品
            List<CouponsGoodsDTO> couponsGoodsDTOList = couponsGoodsService.getByActivityId(couponsActivityEntity.getId());
            couponsActivityIndexDTO.setGoodsList(couponsGoodsDTOList);
            couponsActivityIndexDTOList.add(couponsActivityIndexDTO);
        });
        return couponsActivityIndexDTOList;
    }

    // =====================================front接口======================================//

    /**
     * 功能描述:
     * 〈优惠券领取〉
     *
     * @param activityId 优惠券活动id
     * @param memberId   会员id
     * @author : 刘远杰
     */

    @Override
    public Boolean receivedCoupons(@RequestParam("activityId") Long activityId, @RequestParam("memberId") Long memberId) {
        // 1.查询当前优惠券活动信息
        CouponsActivityDTO couponsActivityDTO = this.get(activityId);

        // 2.数据校验 活动状态，是否可领
        try {
            this.validReceivedCoupons(couponsActivityDTO, memberId);
        } catch (Exception e) {
            log.error("领取优惠券异常", e);
            throw new ServiceException(ActivityStatusCode.RECEIVED_COUPONS_ACTIVITY_END);
        }

        // 3.保存会员优惠券
        MemberCouponsDTO memberCouponsDTO = this.createMemberCouponsDTO(memberId, couponsActivityDTO);
        memberCouponsService.save(memberCouponsDTO);
        log.info("领取优惠券,保存会员优惠券成功:{}", memberCouponsDTO);

        // 4.扣减剩余优惠券数量   不限量不进行扣除
        if (couponsActivityDTO.getTotalNum() != 0) {
            couponsActivityDTO.setSurplusNum(couponsActivityDTO.getTotalNum() - 1 < 0 ? 0 : couponsActivityDTO.getSurplusNum() - 1);
            int count = baseDao.updateCouponsActivotySurplusNum(couponsActivityDTO);
            if (count == 0) {
                // 扣减数量失败
                log.info("领券异常，扣减优惠券数量异常，参数:memberId{}.:activityId{}", memberId, activityId);
                throw new ServiceException(ActivityStatusCode.RECEIVED_COUPONS_REDUCE_EXCEPTION);
            }
        }
        // 获取活动关联商品类型
        List<CouponsGoodsDTO> couponsGoodsDTOList = couponsGoodsService.getByActivityId(couponsActivityDTO.getId());
        // 保存会员优惠券es
        saveMemberCouponsEs(couponsActivityDTO, memberCouponsDTO, couponsGoodsDTOList);
        // 保存优惠券活动es
//        couponsActivityDTO.setSurplusNum(couponsActivityDTO.getTotalNum() - 1);
        saveCouponsActivityEs(couponsActivityDTO, couponsGoodsDTOList);
        log.info("用户领取普通优惠券成功,会员id:{},优惠券:{}", memberId, memberCouponsDTO);
        return true;
    }

    /**
     * 功能描述：
     * <查询所有优惠券商品>
     *
     * @param storeId 店铺id
     * @param brandId 品牌id
     * @param goodsId 商品id
     * @return
     * @date 2020/3/10 12:07
     * @author 刘远杰
     **/
    @Override

    public List<CouponsGoodsDTO> getAllCouponsGoods(@RequestParam("storeId") Long storeId,
                                                    @RequestParam(value = "brandId", required = false) Long brandId,
                                                    @RequestParam(value = "goodsId", required = false) Long goodsId) {
        // 查询所有优惠券商品
        return baseDao.selectAllCouponsGoods(storeId, brandId, goodsId);
    }

    /**
     * 功能描述：
     * <根据店铺id查询所有优惠券商品>
     *
     * @param storeId 店铺id
     * @return
     * @date 2020/3/10 12:07
     * @author 刘远杰
     **/
    @Override

    public List<CouponsGoodsDTO> selectAllCouponsGoodsByStoreId(@RequestParam("storeId") Long storeId) {
        // 查询所有优惠券商品
        return baseDao.selectAllCouponsGoodsByStoreId(storeId);
    }

    /***
     * 查询失效优惠券
     * @param memberId
     * @return
     */

    @Override
    public List<MemberCouponsDTO> getDisableActivity(@RequestParam("memberId") Long memberId) {
        return baseDao.getDisableActivity(memberId);
    }

    /**
     * 功能描述:
     * 〈封装优惠券商品数据并保存〉
     *
     * @param dto 活动及商品实体
     * @author : 刘远杰
     */
    private void saveCouponGoodsBatch(@RequestBody CouponsActivityAndGoodsDTO dto) {
        // 1.前台获取优惠券关联商品数据
        List<CouponsGoodsDTO> couponsGoodsDTOList = dto.getCouponsGoodsDTOList();
        if (CollectionUtils.isNotEmpty(couponsGoodsDTOList)) {
            // 2.优惠券商品集合不为空，封装保存参数
            if (CouponsEnum.ACTIVITY_SCOPE_STORE.value() == dto.getActivityScope()) {
                // 3-1.店铺优惠券活动
                if (CouponsEnum.ACTIVITY_GOODS_SCOPE_ALL.value() == dto.getActivityGoodsScope()) {
                    // 3-1-1.全场通用  填充店铺名称
                    couponsGoodsDTOList.forEach(couponsGoodsDTO -> couponsGoodsDTO.setRelationName(dto.getStoreName()));
                } else if (CouponsEnum.ACTIVITY_GOODS_SCOPE_CLASS.value() == dto.getActivityGoodsScope()) {
                    List<Long> classId = couponsGoodsDTOList.stream().map(CouponsGoodsDTO::getRelationId).collect(Collectors.toList());
                    List<StoreGoodsClassDTO> classDTOList = storeGoodsClassService.getClassById(classId);
                    if (classDTOList.size() != classId.size()) {
                        // 存在未查询到的分类，抛出异常
                        throw new ServiceException(ActivityStatusCode.SAVE_COUPONS_ACTIVITY_NOTFOUND_CLASS);
                    }
                    // 清空集合重新填充
                    couponsGoodsDTOList.clear();
                    classDTOList.forEach(classDTO -> {
                        CouponsGoodsDTO couponsGoodsDTO = new CouponsGoodsDTO();
                        couponsGoodsDTO.setActivityId(dto.getId());
                        couponsGoodsDTO.setActivityGoodsScope(dto.getActivityGoodsScope());
                        couponsGoodsDTO.setRelationId(classDTO.getId());
                        couponsGoodsDTO.setRelationName(classDTO.getGcName());
                        couponsGoodsDTOList.add(couponsGoodsDTO);
                    });

                } else if (CouponsEnum.ACTIVITY_GOODS_SCOPE_GOODS.value() == dto.getActivityGoodsScope()) {
                    // 3-1-3.指定商品，填充商品名称
                    // 查询选择的商品信息
                    List<Long> goodsIds = couponsGoodsDTOList.stream().map(CouponsGoodsDTO::getRelationId).collect(Collectors.toList());
                    List<GoodsDTO> goodsDTOList = goodsService.getByGoodsIds(goodsIds, dto.getStoreId());
                    if (goodsDTOList.size() != goodsIds.size()) {
                        // 存在未查询到的商品，抛出异常
                        throw new ServiceException(ActivityStatusCode.SAVE_COUPONS_ACTIVITY_NOTFOUND_GOODS);
                    }
                    // 清空集合重新填充
                    couponsGoodsDTOList.clear();
                    goodsDTOList.forEach(goodsDTO -> {
                        CouponsGoodsDTO couponsGoodsDTO = new CouponsGoodsDTO();
                        couponsGoodsDTO.setActivityId(dto.getId());
                        couponsGoodsDTO.setActivityGoodsScope(dto.getActivityGoodsScope());
                        couponsGoodsDTO.setRelationId(goodsDTO.getId());
                        couponsGoodsDTO.setRelationName(goodsDTO.getGoodsName());
                        couponsGoodsDTOList.add(couponsGoodsDTO);
                    });
                } else if (CouponsEnum.ACTIVITY_GOODS_SCOPE_BRAND.value() == dto.getActivityGoodsScope()) {
                    // 3-1-4.指定品牌，查询品牌名称填充
                    // 查询选择的品牌信息
                    List<Long> brandIds = couponsGoodsDTOList.stream().map(CouponsGoodsDTO::getRelationId).collect(Collectors.toList());
                    List<BrandDTO> brandDTOList = brandService.getByBrandIds(brandIds, null);
                    if (brandDTOList.size() != brandIds.size()) {
                        // 存在未查询到的商品，抛出异常
                        throw new ServiceException(ActivityStatusCode.SAVE_COUPONS_ACTIVITY_NOTFOUND_BRAND);
                    }
                    // 清空集合重新填充
                    couponsGoodsDTOList.clear();
                    brandDTOList.forEach(brandDTO -> {
                        CouponsGoodsDTO couponsGoodsDTO = new CouponsGoodsDTO();
                        couponsGoodsDTO.setActivityId(dto.getId());
                        couponsGoodsDTO.setActivityGoodsScope(dto.getActivityGoodsScope());
                        couponsGoodsDTO.setRelationId(brandDTO.getId());
                        couponsGoodsDTO.setRelationName(brandDTO.getBrandName());
                        couponsGoodsDTOList.add(couponsGoodsDTO);
                    });
                }
            } else if (CouponsEnum.ACTIVITY_SCOPE_PLATFORM.value() == dto.getActivityScope()) {
                // TODO: 2019/12/5  本期不做平台优惠券
                dto.setCouponsGoodsDTOList(Collections.emptyList());
            }
            // 4.批量保存优惠券商品数据
            couponsGoodsService.saveBatch(couponsGoodsDTOList);
        }
    }

    /**
     * 功能描述:
     * 〈领券优惠券校验〉
     *
     * @param couponsActivityDTO 优惠券活动信息
     * @param memberId           会员id
     * @author : 刘远杰
     */
    private void validReceivedCoupons(CouponsActivityDTO couponsActivityDTO, Long memberId) {
        if (couponsActivityDTO == null) {
            // 活动数据不能为空
            throw new ServiceException(ActivityStatusCode.RECEIVED_COUPONS_NOTFOUND_ACTIVITY);
        } else if (CouponsEnum.ACTIVITY_STATE_PREPARE.value() == couponsActivityDTO.getActivityState()) {
            // 活动状态不能为未开始
            throw new ServiceException(ActivityStatusCode.RECEIVED_COUPONS_ACTIVITY_PREPAER);
        } else if (CouponsEnum.ACTIVITY_STATE_END.value() == couponsActivityDTO.getActivityState()) {
            // 活动状态不能为已结束
            throw new ServiceException(ActivityStatusCode.RECEIVED_COUPONS_ACTIVITY_END);
        } else if (couponsActivityDTO.getTotalNum() != 0 && couponsActivityDTO.getSurplusNum() <= 0) {
            // 剩余优惠券数量必须大于0
            throw new ServiceException(ActivityStatusCode.RECEIVED_COUPONS_ACTIVITY_SURPLUS_EMPTY);
        } else {
            // 校验用户是否领取优惠券及是否超出领取上限
            List<MemberCouponsDTO> memberCouponsDTOList = memberCouponsService
                    .getMemberCouponsListByCouponsId(couponsActivityDTO.getId(), memberId);
            if (CollectionUtils.isNotEmpty(memberCouponsDTOList)) {
                // 校验优惠券是否领取
                for (MemberCouponsDTO memberCouponsDTO : memberCouponsDTOList) {
                    if (CouponsEnum.COUPON_CANNT_USE.value() == memberCouponsDTO.getCouponsState()
                            || CouponsEnum.COUPON_CAN_USE.value() == memberCouponsDTO.getCouponsState()) {
                        // 优惠券未使用不可领取
                        throw new ServiceException(ActivityStatusCode.RECEIVED_COUPONS_ACTIVITY_JOINED);
                    }
                }
                if (couponsActivityDTO.getPersonLimit() > 0) {
                    // 校验领取数量限制
                    if (memberCouponsDTOList.size() >= couponsActivityDTO.getPersonLimit()) {
                        throw new ServiceException(ActivityStatusCode.RECEIVED_COUPONS_ACTIVITY_OVER_JOIN_NUM);
                    }
                }
            }
        }
    }

    /**
     * 功能描述:
     * 〈创建用户优惠券实体〉
     *
     * @param memberId           会员id
     * @param couponsActivityDTO 优惠券活动实体
     * @author : 刘远杰
     */
    private MemberCouponsDTO createMemberCouponsDTO(Long memberId, CouponsActivityDTO couponsActivityDTO) {
        // 保存用户优惠券信息
        MemberCouponsDTO memberCouponsDTO = new MemberCouponsDTO();

        memberCouponsDTO.setId(IdGenerator.defaultSnowflakeId());
        memberCouponsDTO.setMemberId(memberId);
        memberCouponsDTO.setCreateDate(new Date());
        // 查询会员名称保存
        MemberDTO memberDTO = memberService.getById(memberId);
        if (memberDTO == null) {
            throw new ServiceException(ActivityStatusCode.NOTFUND_MEMBER_EXCEPTION);
        }
        memberCouponsDTO.setMemberName(memberDTO.getMemberName());
        memberCouponsDTO.setActivityId(couponsActivityDTO.getId());

        // 保存使用日期
        if (CouponsEnum.VALIDITY_TYPE_RANGE.value() == couponsActivityDTO.getValidityType()) {
            // 日期范围
            memberCouponsDTO.setStartDate(couponsActivityDTO.getUseStartDate());
            memberCouponsDTO.setEndDate(couponsActivityDTO.getUseEndDate());
            Date now = new Date();
            if (now.compareTo(DateUtils.addDateDays(couponsActivityDTO.getUseEndDate(), 1)) >= 0) {
                // 当前时间晚于使用结束时间加一天，设置状态为过期
                memberCouponsDTO.setCouponsState(CouponsEnum.COUPONS_INVALID.value());
            } else if (now.compareTo(couponsActivityDTO.getUseStartDate()) < 0) {
                // 当前时间早于使用开始时间加，设置状态为不可用
                memberCouponsDTO.setCouponsState(CouponsEnum.COUPON_CANNT_USE.value());
            } else {
                // 设置状态为可用
                memberCouponsDTO.setCouponsState(CouponsEnum.COUPON_CAN_USE.value());
            }
        } else if (CouponsEnum.VALIDITY_TYPE_DAYS.value() == couponsActivityDTO.getValidityType()) {
            // 固定天数
            Date startTime = new Date();
            Date endTime = DateUtils.addDateDays(startTime, couponsActivityDTO.getValidityDays());
            memberCouponsDTO.setStartDate(startTime);
            memberCouponsDTO.setEndDate(endTime);
            // 直接设置优惠券状态为可用
            memberCouponsDTO.setCouponsState(CouponsEnum.COUPON_CAN_USE.value());
        }
        return memberCouponsDTO;
    }

    /**
     * 功能描述:
     * 〈保存会员优惠券es〉
     *
     * @param couponsActivityDTO  优惠券活动
     * @param memberCouponsDTO    会员优惠券
     * @param couponsGoodsDTOList 活动商品集合
     * @author : 刘远杰
     */
    private void saveMemberCouponsEs(CouponsActivityDTO couponsActivityDTO, MemberCouponsDTO memberCouponsDTO,
                                     List<CouponsGoodsDTO> couponsGoodsDTOList) {
        MemberCouponsIndexDTO memberCouponsIndexDTO = new MemberCouponsIndexDTO();
        // 1.封装es同步会员优惠券数据
        BeanCopier.create(MemberCouponsDTO.class, MemberCouponsIndexDTO.class, false)
                .copy(memberCouponsDTO, memberCouponsIndexDTO, null);
        // 2.封装es同步活动数据
        memberCouponsIndexDTO.setActivityId(couponsActivityDTO.getId());
        memberCouponsIndexDTO.setCouponsType(couponsActivityDTO.getCouponsType());
        memberCouponsIndexDTO.setActivityGoodsScope(couponsActivityDTO.getActivityGoodsScope());
        memberCouponsIndexDTO.setLimitAmount(couponsActivityDTO.getLimitAmount());
        memberCouponsIndexDTO.setFaceValue(couponsActivityDTO.getFaceValue());
        memberCouponsIndexDTO.setStoreId(couponsActivityDTO.getStoreId());
        memberCouponsIndexDTO.setStoreName(couponsActivityDTO.getStoreName());
        memberCouponsIndexDTO.setGoodsList(couponsGoodsDTOList);
        // 3.更新会员优惠券es
        esDataUtils.saveDataBatch(ElasticSearchConstant.MEMBER_COUPONS_INDEX, "id",
                JSONArray.toJSONString(Collections.singletonList(memberCouponsIndexDTO)), MemberCouponsIndexVo.class);
    }

    /**
     * 功能描述:
     * 〈保存优惠券活动es〉
     *
     * @param couponsActivityDTO  优惠券活动信息
     * @param couponsGoodsDTOList 活动商品数据
     * @author : 刘远杰
     */
    private void saveCouponsActivityEs(CouponsActivityDTO couponsActivityDTO, List<CouponsGoodsDTO> couponsGoodsDTOList) {
        List<CouponsActivityIndexDTO> couponsActivityIndexList = new ArrayList<>();
        CouponsActivityIndexDTO couponsActivityIndexDTO = new CouponsActivityIndexDTO();
        // 1.封装es同步活动数据
        BeanCopier.create(CouponsActivityDTO.class, CouponsActivityIndexDTO.class, false)
                .copy(couponsActivityDTO, couponsActivityIndexDTO, null);

        couponsActivityIndexDTO.setGoodsList(couponsGoodsDTOList);
        couponsActivityIndexList.add(couponsActivityIndexDTO);
        // 2.更新优惠券活动es
        esDataUtils.saveDataBatch(ElasticSearchConstant.COUPONS_ACTIVITY_INDEX, "id",
                JSONArray.toJSONString(couponsActivityIndexList), CouponsActivityIndexVo.class);
    }

}
