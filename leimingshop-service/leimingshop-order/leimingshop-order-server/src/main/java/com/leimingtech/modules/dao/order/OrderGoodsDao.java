/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dao.order;

import com.leimingtech.commons.mybatis.dao.BaseDao;
import com.leimingtech.modules.dto.order.EvaluateOrderGoodsDTO;
import com.leimingtech.modules.dto.order.IndexGoodsSellRakingDTO;
import com.leimingtech.modules.dto.order.OrderGoodsDTO;
import com.leimingtech.modules.entity.order.OrderGoodsEntity;
import com.leimingtech.modules.vo.order.PcOrderGoodsInfoVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 订单商品管理
 *
 * @author 刘远杰
 * @email 2634443725@qq.com
 * @since 1.0 2019-06-13
 */
@Mapper
public interface OrderGoodsDao extends BaseDao<OrderGoodsEntity> {

    /**
     * 功能描述:
     * 〈查询商品购买数量〉
     *
     * @param buyerId 买家id
     * @param goodsId 商品id
     * @author : 刘远杰
     */
    int countByGoodsId(@Param("buyerId") Long buyerId, @Param("goodsId") Long goodsId);

    /**
     * 功能描述:
     * 〈根据id查询订单商品〉
     *
     * @param orderId 订单id
     * @author : 刘远杰
     */
    OrderGoodsDTO findByOrderId(Long orderId);

    /**
     * 功能描述:
     * 〈根据orderSn查询订单商品〉
     *
     * @param orderSn
     * @author : 刘远杰
     */
    OrderGoodsDTO findByOrderSn(Long orderSn);

    /**
     * 分页查未评价的商品
     *
     * @param params 参数
     * @return
     */
    List<EvaluateOrderGoodsDTO> findPage(Map<String, Object> params);

    /**
     * 功能描述:
     * 〈查询未评价订单数量〉
     *
     * @param params 参数
     * @author : 刘远杰
     */
    int countNotEvaluate(Map<String, Object> params);

    /**
     * 根据订单ID 查询出订单商品表中未评价的商品
     *
     * @param orderId 订单ID
     * @return
     */
    List<EvaluateOrderGoodsDTO> findOrderGoodsList(Long orderId);

    /**
     * @param orderGoodsId:订单商品表主键
     * @param applyNum:申请数量
     * @Author: SWH ab4856812@163.com
     * @Description:更新可售后数量(减)
     * @Date: 2019/6/18 23:26
     * @Version: V1.0
     */
    void updateAftersaleQuantityById(@Param("id") Long orderGoodsId, @Param("applyNum") Integer applyNum);

    /**
     * @param orderGoodsId:订单商品表主键
     * @param applyNum:申请数量
     * @Author: SWH ab4856812@163.com
     * @Description:更新可售后数量（加）
     * @Date: 2019/6/18 23:26
     * @Version: V1.0
     */
    void updateAftersaleQuantityByIdAdd(@Param("id") Long orderGoodsId, @Param("applyNum") Integer applyNum);

    /**
     * 查询超时未评价的订单
     *
     * @param createTime 创建时间
     * @return
     */
    List<OrderGoodsDTO> findOverTimeOrder(String createTime);


    /**
     * 查询订单中得商品是否全部评价
     *
     * @param orderId
     * @return
     */
    Integer findEvaluateNum(Long orderId);

    /**
     * 根据父订单号查询订单商品信息
     *
     * @param parentOrderSn: 父订单编号
     * @return 订单商品列表信息
     * @date 2019/11/6 18:16
     * @author lixiangx@leimingtech.com
     **/
    List<OrderGoodsDTO> findOrderGoodsByParentOrderSn(Long parentOrderSn);

    /**
     * 功能描述:
     * 〈根据paySn查询订单商品〉
     *
     * @param paySn 支付单号
     * @author : 刘远杰
     */
    List<OrderGoodsEntity> findByPaySn(@Param("paySn") Long paySn);

    List<IndexGoodsSellRakingDTO> selectGoodsSellRanking(@Param("startDate") Date startDate, @Param("endDate") Date endDate, @Param("orderBy") Integer orderBy);

    /**
     * 查询 订单内的未评价的订单商品
     *
     * @param orderSn
     * @return
     */
    List<OrderGoodsDTO> fingNotOrderGoods(Long orderSn);

    /**
     * 根据订单id查询订单商品项
     *
     * @param orderGoodsId
     * @param orderId
     * @return
     */
    List<PcOrderGoodsInfoVO> findOrderGoodsInfo(@Param("orderId") Long orderId,
                                                @Param("orderGoodsId") Long orderGoodsId);

    /**
     * 查询已评价的订单商品
     *
     * @param orderSn
     * @return
     */
    List<OrderGoodsDTO> fingYesOrderGoods(Long orderSn);
}
