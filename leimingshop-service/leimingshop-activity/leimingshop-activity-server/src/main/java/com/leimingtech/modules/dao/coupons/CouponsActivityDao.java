/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dao.coupons;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.leimingtech.commons.mybatis.dao.BaseDao;
import com.leimingtech.modules.dto.coupons.*;
import com.leimingtech.modules.entity.coupons.CouponsActivityEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 优惠券活动dao
 *
 * @author 刘远杰 2634443725@qq.com
 * @since v1.0.0 2019-12-05
 */
@Mapper
public interface CouponsActivityDao extends BaseDao<CouponsActivityEntity> {

    /**
     * 功能描述:
     * 〈后台优惠券活动分页查询〉
     *
     * @param page   分页对象
     * @param params 查询条件
     * @author : 刘远杰
     */
    List<AdminCouponsActivityPageDTO> adminPage(IPage page, @Param("params") Map<String, Object> params);

    /**
     * 功能描述:
     * 〈后台优惠券详情查询〉
     *
     * @param id      优惠券活动id
     * @param storeId 店铺id
     * @author : 刘远杰
     */
    AdminCouponsActivityDetailDTO adminDetail(@Param("id") Long id, @Param("storeId") Long storeId);

    /**
     * 功能描述:
     * 〈更新优惠券剩余数量〉
     *
     * @param couponsActivityDTO 优惠券活动id
     * @author : 刘远杰
     */
    Integer updateCouponsActivotySurplusNum(CouponsActivityDTO couponsActivityDTO);

    /**
     * 功能描述：
     * <查询所有优惠券商品>
     *
     * @param storeId 店铺id
     * @param brandId 品牌id
     * @param goodIds 商品id
     * @return
     * @date 2020/3/10 12:04
     * @author 刘远杰
     **/
    List<CouponsGoodsDTO> selectAllCouponsGoods(@Param("storeId") Long storeId, @Param("brandId") Long brandId, @Param("goodsId") Long goodIds);

    /**
     * 功能描述：
     * <根据店铺id查询所有优惠券商品>
     *
     * @param storeId 店铺id
     * @return
     * @date 2020/3/10 12:04
     * @author 刘远杰
     **/
    List<CouponsGoodsDTO> selectAllCouponsGoodsByStoreId(@Param("storeId") Long storeId);

    /**
     * 查询失效优惠券
     *
     * @param memberId
     * @return
     */
    List<MemberCouponsDTO> getDisableActivity(Long memberId);
}
