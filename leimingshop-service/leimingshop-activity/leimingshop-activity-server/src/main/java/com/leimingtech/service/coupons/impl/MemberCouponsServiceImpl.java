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
import com.leimingtech.modules.constant.ElasticSearchConstant;
import com.leimingtech.modules.dao.coupons.MemberCouponsDao;
import com.leimingtech.modules.dto.coupons.AdminMemberCouponsPageDTO;
import com.leimingtech.modules.dto.coupons.MemberCouponsDTO;
import com.leimingtech.modules.dto.coupons.MemberCouponsIndexDTO;
import com.leimingtech.modules.entity.coupons.MemberCouponsEntity;
import com.leimingtech.modules.enums.coupons.CouponsEnum;
import com.leimingtech.modules.service.coupons.MemberCouponsService;
import com.leimingtech.modules.utils.EsDataUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.beans.BeanCopier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.*;

/**
 * 会员优惠券service
 *
 * @author 刘远杰 2634443725@qq.com
 * @since v1.0.0 2019-12-05
 */
@Service
@Transactional(rollbackFor = Exception.class)

public class MemberCouponsServiceImpl extends BaseServiceImpl<MemberCouponsDao, MemberCouponsEntity> implements MemberCouponsService {

    @Autowired
    private EsDataUtils esDataUtils;

    /**
     * 功能描述:
     * 〈后台会员优惠券分页〉
     *
     * @param params 查询条件
     * @author : 刘远杰
     */
    @Override

    public PageData<AdminMemberCouponsPageDTO> adminPage(@RequestParam Map<String, Object> params) {
        IPage<MemberCouponsEntity> page = getPage(params, null, false);
        //查询
        List<AdminMemberCouponsPageDTO> list = baseDao.adminPage(page, params);
        return new PageData<>(list, page.getTotal());
    }

    @Override

    public List<MemberCouponsDTO> list(Map<String, Object> params) {
        List<MemberCouponsEntity> entityList = baseDao.selectList(getWrapper(params));

        return ConvertUtils.sourceToTarget(entityList, MemberCouponsDTO.class);
    }

    private QueryWrapper<MemberCouponsEntity> getWrapper(Map<String, Object> params) {
        String id = (String) params.get("id");

        QueryWrapper<MemberCouponsEntity> wrapper = new QueryWrapper<>();
        wrapper.eq(StringUtils.isNotBlank(id), "id", id);

        return wrapper;
    }

    @Override

    public MemberCouponsDTO get(Long id) {
        MemberCouponsEntity entity = baseDao.selectById(id);
        MemberCouponsDTO memberCouponsDTO = new MemberCouponsDTO();
        BeanCopier.create(MemberCouponsEntity.class, MemberCouponsDTO.class, false)
                .copy(entity, memberCouponsDTO, null);
        return memberCouponsDTO;
    }

    /**
     * 功能描述:
     * 〈根据会员优惠券id集合批量查询会员优惠券〉
     *
     * @param ids 会员优惠券集合
     * @author : 刘远杰
     */

    @Override
    public List<MemberCouponsDTO> getByIds(@RequestBody List<Long> ids) {
        QueryWrapper<MemberCouponsEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.in("id", ids);
        List<MemberCouponsEntity> entityList = baseDao.selectList(queryWrapper);
        return ConvertUtils.sourceToTarget(entityList, MemberCouponsDTO.class);
    }

    @Override

    public void save(@RequestBody MemberCouponsDTO dto) {
        MemberCouponsEntity entity = ConvertUtils.sourceToTarget(dto, MemberCouponsEntity.class);

        insert(entity);
    }

    @Override

    public void update(@RequestBody MemberCouponsDTO dto) {
        MemberCouponsEntity entity = ConvertUtils.sourceToTarget(dto, MemberCouponsEntity.class);

        updateById(entity);
    }

    @Override

    public void delete(@RequestBody Long[] ids) {
        //逻辑删除
        //logicDelete(ids, MemberCouponsEntity.class);

        //物理删除
        baseDao.deleteBatchIds(Arrays.asList(ids));
    }

    /**
     * 功能描述:
     * 〈查询用户某优惠券领取集合〉
     *
     * @param activityId 优惠券id
     * @param memberId   会员id
     * @author : 刘远杰
     */

    @Override
    public List<MemberCouponsDTO> getMemberCouponsListByCouponsId(@RequestParam("activityId") Long activityId,
                                                                  @RequestParam("memberId") Long memberId) {
        QueryWrapper<MemberCouponsEntity> wrapper = new QueryWrapper<>();
        wrapper.eq("member_id", memberId).eq("activity_id", activityId);
        List<MemberCouponsEntity> entityList = baseDao.selectList(wrapper);
        return ConvertUtils.sourceToTarget(entityList, MemberCouponsDTO.class);
    }

    /**
     * 功能描述:
     * 〈获取所有前台展示用户优惠券数据〉
     *
     * @author : 刘远杰
     */

    @Override
    public List<MemberCouponsIndexDTO> selectAllFrontMemberCoupons() {
        return baseDao.selectAllFrontMemberCoupons();
    }

    /**
     * 功能描述:
     * 〈获取会员优惠券及关联商品数据〉
     *
     * @param id 用户优惠券id
     * @author : 刘远杰
     */

    @Override
    public MemberCouponsIndexDTO selectMemberCouponsAndGoodsList(Long id) {
        return baseDao.selectMemberCouponsAndGoodsList(id);
    }

    /**
     * 功能描述:
     * 〈使用优惠券〉
     *
     * @param dtoList 会员优惠券
     * @author : 刘远杰
     */

    @Override
    public Boolean useMemberCoupons(@RequestBody List<MemberCouponsDTO> dtoList) {
        Integer count = baseDao.useCouponsBatch(dtoList);
        if (count != dtoList.size()) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        }
        // 更新会员优惠券es为未使用状态
        esDataUtils.updateDataBatch(ElasticSearchConstant.MEMBER_COUPONS_INDEX, "id",
                JSONArray.toJSONString(dtoList));
        return count == dtoList.size();
    }

    /**
     * 功能描述:
     * 〈取消订单返回优惠券〉
     *
     * @param memberCouponsIds 会员优惠券集合
     * @author : 刘远杰
     */

    @Override
    public Boolean returnMemberCoupons(@RequestBody List<Long> memberCouponsIds, @RequestParam("memberId") Long memberId) {
        List<MemberCouponsDTO> dtoList = new ArrayList<>();
        Date now = new Date();
        for (Long memberCouponsId : memberCouponsIds) {
            MemberCouponsDTO memberCouponsDTO = new MemberCouponsDTO();
            MemberCouponsEntity entity = baseDao.selectById(memberCouponsId);
            if (entity != null) {
                memberCouponsDTO.setId(memberCouponsId);
                memberCouponsDTO.setMemberId(memberId);
                if (entity.getEndDate().before(DateUtils.addDateDays(now, -1))) {
                    // 结束时间早于当前时间
                    memberCouponsDTO.setCouponsState(CouponsEnum.COUPONS_INVALID.value());
                } else if (entity.getStartDate().before(now)) {
                    // 开始早于当前时间
                    memberCouponsDTO.setCouponsState(CouponsEnum.COUPON_CAN_USE.value());
                } else {
                    // 开始晚于当前时间
                    memberCouponsDTO.setCouponsState(CouponsEnum.COUPON_CANNT_USE.value());
                }
            } else {
                return false;
            }
            dtoList.add(memberCouponsDTO);
        }
        Integer count = baseDao.returnCouponsBatch(dtoList);
        if (count != dtoList.size()) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        }
        // 更新会员es状态为未使用状态
        esDataUtils.updateDataBatch(ElasticSearchConstant.MEMBER_COUPONS_INDEX, "id",
                JSONArray.toJSONString(dtoList));

        return count == dtoList.size();
    }

    /**
     * 功能描述:
     * 〈会员优惠券不可用变为可用定时任务〉
     *
     * @param time 当前时间
     * @author : 刘远杰
     */

    @Override
    public void canCouponsTiming(@RequestParam("time") Long time) {
        Date now = new Date(time);
        // 不可用，未删除，开始时间小于等于当前时间，结束时间大于当前时间
        UpdateWrapper<MemberCouponsEntity> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("del_flag", DelFlagEnum.NORMAL.value())
                .le("start_date", now).gt("end_date", DateUtils.addDateDays(now, -1))
                .eq("coupons_state", CouponsEnum.COUPON_CANNT_USE.value());
        MemberCouponsEntity entity = new MemberCouponsEntity();
        // 获取所有可变为可用的会员优惠券集合
        List<MemberCouponsEntity> entityList = baseDao.selectList(updateWrapper);

        if (CollectionUtils.isNotEmpty(entityList)) {
            // 更新活动状态
            entity.setCouponsState(CouponsEnum.COUPON_CAN_USE.value());
            baseDao.update(entity, updateWrapper);

            // 修改优惠券es
            List<MemberCouponsIndexDTO> memberCouponsIndexList = new ArrayList<>();
            entityList.forEach(memberCouponsEntity -> {
                MemberCouponsIndexDTO memberCouponsIndexDTO = new MemberCouponsIndexDTO();
                memberCouponsIndexDTO.setId(memberCouponsEntity.getId());
                memberCouponsIndexDTO.setCouponsState(CouponsEnum.COUPON_CAN_USE.value());
                memberCouponsIndexList.add(memberCouponsIndexDTO);
            });
            esDataUtils.updateDataBatch(ElasticSearchConstant.MEMBER_COUPONS_INDEX, "id",
                    JSONArray.toJSONString(memberCouponsIndexList));
        }

    }

    /**
     * 功能描述:
     * 〈会员优惠券不可用/可用变为过期定时任务〉
     *
     * @param time 当前时间
     * @author : 刘远杰
     */

    @Override
    public void canntCouponsTiming(@RequestParam("time") Long time) {
        Date now = new Date(time);
        // 不可用/可用，未删除，结束时间大于当前时间
        UpdateWrapper<MemberCouponsEntity> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("del_flag", DelFlagEnum.NORMAL.value())
                .le("end_date", DateUtils.addDateDays(now, -1))
                .in("coupons_state", CouponsEnum.COUPON_CANNT_USE.value(), CouponsEnum.COUPON_CAN_USE.value());
        MemberCouponsEntity entity = new MemberCouponsEntity();
        // 获取所有可变为可用的会员优惠券集合
        List<MemberCouponsEntity> entityList = baseDao.selectList(updateWrapper);

        if (CollectionUtils.isNotEmpty(entityList)) {
            // 更新活动状态
            entity.setCouponsState(CouponsEnum.COUPONS_INVALID.value());
            baseDao.update(entity, updateWrapper);

            // 修改优惠券es
            List<MemberCouponsIndexDTO> memberCouponsIndexList = new ArrayList<>();
            entityList.forEach(memberCouponsEntity -> {
                MemberCouponsIndexDTO memberCouponsIndexDTO = new MemberCouponsIndexDTO();
                memberCouponsIndexDTO.setId(memberCouponsEntity.getId());
                memberCouponsIndexDTO.setCouponsState(CouponsEnum.COUPONS_INVALID.value());
                memberCouponsIndexList.add(memberCouponsIndexDTO);
            });
            esDataUtils.updateDataBatch(ElasticSearchConstant.MEMBER_COUPONS_INDEX, "id",
                    JSONArray.toJSONString(memberCouponsIndexList));

        }
    }

}
