/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.service.coupons;


import com.leimingtech.commons.tools.page.PageData;
import com.leimingtech.modules.dto.coupons.CouponsGoodsDTO;

import java.util.List;
import java.util.Map;

/**
 * 优惠券活动商品service
 *
 * @author 刘远杰 2634443725@qq.com
 * @since v1.0.0 2019-12-05
 */

public interface CouponsGoodsService {


    PageData<CouponsGoodsDTO> page(Map<String, Object> params);


    List<CouponsGoodsDTO> list(Map<String, Object> params);


    CouponsGoodsDTO get(Long id);

    /**
     * 功能描述:
     * 〈获取活动关联的商品集合〉
     *
     * @param activityId 优惠券活动id
     * @author : 刘远杰
     */

    List<CouponsGoodsDTO> getByActivityId(Long activityId);


    void save(CouponsGoodsDTO dto);

    /**
     * 功能描述:
     * 〈优惠券商品批量保存〉
     *
     * @param dtoList 优惠券商品集合
     * @author : 刘远杰
     */

    Boolean saveBatch(List<CouponsGoodsDTO> dtoList);


    void update(CouponsGoodsDTO dto);


    void delete(Long[] ids);

    /**
     * 功能描述:
     * 〈根据优惠券活动id物理删除优惠券商品〉
     *
     * @param activityId 优惠券活动id
     * @author : 刘远杰
     */

    void noLogicDeleteByActivityId(Long activityId);

    /**
     * 功能描述:
     * 〈根据优惠券活动id逻辑删除优惠券商品〉
     *
     * @param activityId 优惠券活动id
     * @author : 刘远杰
     */

    void deleteByActivityId(Long activityId);
}
