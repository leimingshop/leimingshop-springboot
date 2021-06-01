/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.index.goods.service.activity.coupons.impl;

import com.alibaba.fastjson.JSONArray;
import com.leimingtech.modules.constant.IndexContant;
import com.leimingtech.modules.dto.activity.coupons.MemberCouponsIndexVo;
import com.leimingtech.modules.dto.coupons.CouponsActivityIndexDTO;
import com.leimingtech.modules.dto.coupons.MemberCouponsIndexDTO;
import com.leimingtech.modules.index.goods.service.activity.coupons.CouponActivityIndexService;
import com.leimingtech.modules.service.coupons.CouponsActivityService;
import com.leimingtech.modules.service.coupons.MemberCouponsService;
import com.leimingtech.modules.utils.EsDataUtils;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <优惠券活动索引管理>
 *
 * @author 刘远杰
 * @email 2634443725@qq.com
 * @since 1.0 2019/12/9
 */
@Slf4j
@Service
public class CouponActivityIndexServiceImpl implements CouponActivityIndexService {

    @Autowired
    private EsDataUtils esDataUtils;

    @Autowired
    private RestHighLevelClient restHighLevelClient;
    @Autowired
    private CouponsActivityService couponsActivityService;
    @Autowired
    private MemberCouponsService memberCouponsService;

    /**
     * 功能描述:
     * 〈同步优惠券活动数据〉
     *
     * @param
     * @author : 刘远杰
     */
    @Override
    public void syncCouponsActivity() {
        // 获取进行中、已结束的优惠券活动
        List<CouponsActivityIndexDTO> couponsActivityIndexDTOList = couponsActivityService.selectStartCouponsActivity();
        // 获取所有会员优惠券
        List<MemberCouponsIndexDTO> memberCouponsIndexDTOList = memberCouponsService.selectAllFrontMemberCoupons();
        // 删除原es
        esDataUtils.deleteIndex(IndexContant.COUPONS_ACTIVITY_NAME);
        esDataUtils.deleteIndex(IndexContant.MEMBER_COUPONS_NAME);

//        //list 空值判断
//        if (CollectionUtils.isEmpty(couponsActivityIndexDTOList)) {
//            log.info("暂无数据更新");
//            return;
//        }
//        // 保存优惠券活动
//        esDataUtils.saveDataBatch(IndexContant.COUPONS_ACTIVITY_NAME, "id",
//                JSONArray.toJSONString(couponsActivityIndexDTOList), CouponsActivityIndexVo.class);
//
//        //list 空值判断
//        if (CollectionUtils.isEmpty(memberCouponsIndexDTOList)) {
//            log.info("暂无数据更新");
//            return;
//        }

        esDataUtils.saveDataBatch(IndexContant.MEMBER_COUPONS_NAME, "id",
                JSONArray.toJSONString(memberCouponsIndexDTOList), MemberCouponsIndexVo.class);
    }

}
