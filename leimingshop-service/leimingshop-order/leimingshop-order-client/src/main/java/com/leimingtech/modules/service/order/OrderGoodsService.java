/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.service.order;


import com.leimingtech.commons.tools.page.PageData;
import com.leimingtech.modules.dto.order.*;
import com.leimingtech.modules.vo.order.PcOrderGoodsInfoVO;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

/**
 * 订单商品管理
 *
 * @author 刘远杰
 * @email 2634443725@qq.com
 * @since 1.0 2019-06-13
 */

public interface OrderGoodsService {

    /**
     * 功能描述:
     * 〈保存订单商品〉
     *
     * @param dto 订单商品
     * @author : 刘远杰
     */

    void saveOrderGoods(@RequestBody OrderGoodsDTO dto);

    /**
     * 功能描述:
     * 〈修改订单商品〉
     *
     * @param dto 订单商品
     * @author : 刘远杰
     */

    void updateOrderGoods(@RequestBody OrderGoodsDTO dto);

    /**
     * 功能描述:
     * 〈根据id查询订单商品〉
     *
     * @param id 订单商品id
     * @author : 刘远杰
     */

    OrderGoodsDTO getById(Long id,
                          @RequestParam(value = "buyerId", required = false) Long buyId,
                          @RequestParam(value = "storeId", required = false) Long storeId);

    /**
     * 功能描述:
     * 〈根据订单id查询订单商品集合〉
     *
     * @param orderId 订单id
     * @author : 刘远杰
     */

    List<OrderGoodsDTO> getByOrderId(Long orderId,
                                     @RequestParam(value = "buyerId", required = false) Long buyId,
                                     @RequestParam(value = "storeId", required = false) Long storeId);

    /**
     * 功能描述:
     * 〈根据paySn查询订单商品集合〉
     *
     * @param paySn 支付单号
     * @author : 刘远杰
     */

    List<OrderGoodsDTO> getByPaySn(Long paySn);

    /**
     * 根据父订单号查询订单商品信息
     *
     * @param parentOrderSn: 父订单编号
     * @return 订单商品列表信息
     * @date 2019/11/6 18:16
     * @author lixiangx@leimingtech.com
     **/

    List<OrderGoodsDTO> findOrderGoodsByParentOrderSn(@RequestParam(value = "parentOrderSn", required = false) Long parentOrderSn);

    /**
     * 功能描述:
     * 〈根据id删除订单商品〉
     *
     * @param id 订单商品id
     * @author : 刘远杰
     */

    int deleteById(Long id,
                   @RequestParam(value = "buyerId", required = false) Long buyerId,
                   @RequestParam(value = "storeId", required = false) Long storeId);

    /**
     * 功能描述:
     * 〈根据orderId删除订单商品〉
     *
     * @param orderId 订单id
     * @author : 刘远杰
     */

    int deleteByOrderId(Long orderId,
                        @RequestParam(value = "buyerId", required = false) Long buyerId,
                        @RequestParam(value = "storeId", required = false) Long storeId);

    /**
     * 功能描述:
     * 〈查询商品购买数量〉
     *
     * @param buyerId 买家id
     * @param goodsId 商品id
     * @author : 刘远杰
     */

    int countByGoodsId(@RequestParam("buyerId") Long buyerId, @RequestParam("goodsId") Long goodsId);

    /**
     * 分页查询未评价的商品
     *
     * @param params
     * @return
     */

    PageData<EvaluateOrderGoodsDTO> findNotEvaluateGoods(@RequestParam Map<String, Object> params);

    /**
     * 功能描述:
     * 〈查询未评价订单商品数量〉
     *
     * @param params 参数
     * @author : 刘远杰
     */

    Integer countNotEvaluateGoods(@RequestParam Map<String, Object> params);

    /**
     * 根据订单ID查询订单商品表中未评价的商品
     */

    List<EvaluateOrderGoodsDTO> findOrderGoodsList(@RequestParam("orderId") Long orderId);

    /**
     * @param id:订单项id
     * @Author: SWH ab4856812@163.com
     * @Description:可申请售后订单中商品
     * @Date: 2019/6/17 22:27
     * @Version: V1.0
     */

    AvailableAfterSaleOrderGoodsDTO getAvailGoodsDetail(Long id);

    /**
     * @param orderGoodsId:订单项id
     * @param applyNum:修改数量
     * @param updateType:修改类型（0：增加，1：减少）
     * @Author: SWH ab4856812@163.com
     * @Description:修改可售后数量
     * @Date: 2019/6/17 22:27
     * @Version: V1.0
     */

    void updateAftersaleQuantityById(@RequestParam("orderGoodsId") Long orderGoodsId, @RequestParam("applyNum") Integer applyNum, @RequestParam("updateType") Integer updateType);

    /**
     * 修改订单商品表是否已退分摊金额状态
     *
     * @param orderGoodsId
     * @param preferential
     * @author xuzhch
     */

    void updateReturnPreferential(@RequestParam("orderGoodsId") Long orderGoodsId, @RequestParam("preferential") int preferential);

    /**
     * @Author: SWH ab4856812@163.com
     * @Description: 判断订单中商品是否能售后
     * @Date: 2019/6/17 22:27
     * @Version: V1.0
     */

    AftersaleAvailTypeDTO findAvailType(@RequestBody AftersaleSettingDTO aftersaleSettingDTO);

    /**
     * 查询超时未评价的商品
     *
     * @param createTime
     * @return
     */

    List<OrderGoodsDTO> findOverTimeOrder(@RequestParam("createTime") String createTime);

    /**
     * 批量更新订单商品
     *
     * @param orderGoodsDTOS 参数
     */

    void updateBetch(@RequestBody List<OrderGoodsDTO> orderGoodsDTOS);

    /**
     * 更新订单表评价状态
     *
     * @param orderId 订单ID
     * @return
     */

    void updateOrderEvaluateStatus(@RequestParam Long orderId);

    /**
     * 功能描述:
     * 〈批量保存订单商品〉
     *
     * @param dtoList 订单商品
     * @author : 刘远杰
     */

    Boolean saveBatch(@RequestBody List<OrderGoodsDTO> dtoList);


    Map<String, Object> goodsSellRanking(@RequestParam Map params);

    /**
     * 根据订单ID 查询订单商品项
     *
     * @param orderId
     */

    List<PcOrderGoodsInfoVO> findOrderGoodsInfo(@RequestParam("orderId") Long orderId,
                                                @RequestParam(value = "orderGoodsId", required = false) Long orderGoodsId);
}
