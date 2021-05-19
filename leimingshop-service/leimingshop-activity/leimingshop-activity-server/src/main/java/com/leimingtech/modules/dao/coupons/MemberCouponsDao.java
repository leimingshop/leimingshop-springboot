/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dao.coupons;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.leimingtech.commons.mybatis.dao.BaseDao;
import com.leimingtech.modules.dto.coupons.AdminMemberCouponsPageDTO;
import com.leimingtech.modules.dto.coupons.MemberCouponsDTO;
import com.leimingtech.modules.dto.coupons.MemberCouponsIndexDTO;
import com.leimingtech.modules.entity.coupons.MemberCouponsEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 会员优惠券dao
 *
 * @author 刘远杰 2634443725@qq.com
 * @since v1.0.0 2019-12-05
 */
@Mapper
public interface MemberCouponsDao extends BaseDao<MemberCouponsEntity> {

    /**
     * 功能描述:
     * 〈优惠券〉
     *
     * @param page
     * @param params
     * @author : 刘远杰
     */
    List<AdminMemberCouponsPageDTO> adminPage(IPage page, @Param("params") Map<String, Object> params);

    /**
     * 功能描述:
     * 〈获取front会员优惠券es数据〉
     *
     * @param
     * @author : 刘远杰
     */
    List<MemberCouponsIndexDTO> selectAllFrontMemberCoupons();

    /**
     * 功能描述:
     * 〈获取会员优惠券及关联商品数据〉
     *
     * @param id 会员优惠券id
     * @author : 刘远杰
     */
    MemberCouponsIndexDTO selectMemberCouponsAndGoodsList(Long id);

    /**
     * 功能描述:
     * 〈批量使用优惠券〉
     *
     * @param dtoList
     * @author : 刘远杰
     */
    Integer useCouponsBatch(@Param("list") List<MemberCouponsDTO> dtoList);

    /**
     * 功能描述:
     * 〈批量返还优惠券〉
     *
     * @param dtoList
     * @author : 刘远杰
     */
    Integer returnCouponsBatch(@Param("list") List<MemberCouponsDTO> dtoList);

}
