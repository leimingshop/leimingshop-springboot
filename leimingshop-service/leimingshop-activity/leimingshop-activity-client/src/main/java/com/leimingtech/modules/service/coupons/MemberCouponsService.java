/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.service.coupons;


import com.leimingtech.commons.tools.page.PageData;
import com.leimingtech.modules.dto.coupons.AdminMemberCouponsPageDTO;
import com.leimingtech.modules.dto.coupons.MemberCouponsDTO;
import com.leimingtech.modules.dto.coupons.MemberCouponsIndexDTO;

import java.util.List;
import java.util.Map;

/**
 * 会员优惠券service
 *
 * @author 刘远杰 2634443725@qq.com
 * @since v1.0.0 2019-12-05
 */

public interface MemberCouponsService {

    /**
     * 功能描述:
     * 〈后台会员优惠券分页〉
     *
     * @param params 查询条件
     * @author : 刘远杰
     */

    PageData<AdminMemberCouponsPageDTO> adminPage(Map<String, Object> params);


    List<MemberCouponsDTO> list(Map<String, Object> params);

    /**
     * 功能描述:
     * 〈根据会员优惠券id集合批量查询会员优惠券〉
     *
     * @param ids 会员优惠券集合
     * @author : 刘远杰
     */

    List<MemberCouponsDTO> getByIds(List<Long> ids);


    MemberCouponsDTO get(Long id);


    void save(MemberCouponsDTO dto);


    void update(MemberCouponsDTO dto);


    void delete(Long[] ids);

    /**
     * 功能描述:
     * 〈查询用户某优惠券领取集合〉
     *
     * @param activityId 优惠券id
     * @param memberId   会员id
     * @author : 刘远杰
     */

    List<MemberCouponsDTO> getMemberCouponsListByCouponsId(Long activityId,
                                                           Long memberId);

    /**
     * 功能描述:
     * 〈获取所有前台展示用户优惠券数据〉
     *
     * @param
     * @author : 刘远杰
     */

    List<MemberCouponsIndexDTO> selectAllFrontMemberCoupons();

    /**
     * 功能描述:
     * 〈获取会员优惠券及关联商品数据〉
     *
     * @param
     * @author : 刘远杰
     */

    MemberCouponsIndexDTO selectMemberCouponsAndGoodsList(Long id);

    /**
     * 功能描述:
     * 〈使用优惠券〉
     *
     * @param dtoList 会员优惠券集合
     * @author : 刘远杰
     */

    Boolean useMemberCoupons(List<MemberCouponsDTO> dtoList);

    /**
     * 功能描述:
     * 〈取消订单返回优惠券〉
     *
     * @param memberCouponsIds 会员优惠券集合
     * @author : 刘远杰
     */

    Boolean returnMemberCoupons(List<Long> memberCouponsIds, Long memberId);

    /**
     * 功能描述:
     * 〈会员优惠券不可用变为可用定时任务〉
     *
     * @param time 当前时间
     * @author : 刘远杰
     */

    void canCouponsTiming(Long time);

    /**
     * 功能描述:
     * 〈会员优惠券不可用/可用变为过期定时任务〉
     *
     * @param time 当前时间
     * @author : 刘远杰
     */

    void canntCouponsTiming(Long time);
}
