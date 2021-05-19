/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dao.order;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.leimingtech.commons.mybatis.dao.BaseDao;
import com.leimingtech.modules.dto.order.*;
import com.leimingtech.modules.entity.order.OrderEntity;
import com.leimingtech.modules.vo.order.PCOrderPageVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 订单管理
 *
 * @author 刘远杰
 * @email 2634443725@qq.com
 * @since 1.0 2019-06-13
 */
@Mapper
public interface OrderDao extends BaseDao<OrderEntity> {

    /**
     * 功能描述:
     * 〈运营端/商家端订单分页集合〉
     *
     * @param params 查询条件
     * @author : 刘远杰
     */
    List<OrderEntity> findOrderList(Map<String, Object> params);

    /**
     * 功能描述:拼团订单分页
     *
     * @param params 查询条件
     * @author : 刘远杰
     */
    List<OrderEntity> findPageGroupOrderList(Map<String, Object> params);

    /**
     * 功能描述:
     * 〈根据父订单号查询子订单集合〉
     *
     * @param parentOrderSn
     * @author : 刘远杰
     */
    List<OrderDTO> findChildOrderList(Long parentOrderSn);

    /**
     * 功能描述:
     * 〈h5订单分页集合〉
     *
     * @param params 查询条件
     * @author : 刘远杰
     */
    List<H5OrderPageDTO> findH5OrderList(@Param("params") Map<String, Object> params, @Param("page") IPage<OrderEntity> page);

    /**
     * pc订单分页集合
     *
     * @return
     * @date 2020-05-18 14:54
     * @author huangkeyuan@leimingtech.com
     **/
    List<PCOrderPageVO> findPcOrderList(@Param("params") Map<String, Object> params, @Param("page") IPage<OrderEntity> page);

    /**
     * 根据订单号查询子订单集合
     *
     * @return
     * @date 2020-05-19 15:14
     * @author huangkeyuan@leimingtech.com
     **/
    List<PCOrderPageVO> findChildrenOrderList(Long parentOrderSn);

    /**
     * 功能描述:
     * 〈查询订单数量〉
     *
     * @param params 查询条件
     * @author : 刘远杰
     */
    int countOrder(Map<String, Object> params);

    /**
     * 功能描述:
     * 〈查询会员中心订单数量〉
     *
     * @param buyerId 会员id
     * @author : 刘远杰
     */
    MemberOrderCountDTO countMemberOrder(@Param("buyerId") Long buyerId);

    /**
     * @param params:查询参数
     * @Author: SWH ab4856812@163.com
     * @Description:订单可申请售后服务列表
     * @Date: 2019/6/17 19:39
     * @Version: V1.0
     */
    List<AvailableAfterSaleOrderDTO> findAvailAfterList(IPage page, @Param("params") Map<String, Object> params);

    /**
     * @param orderId:订单id
     * @Author: SWH ab4856812@163.com
     * @Description:根据订单号查询订单完成时间
     * @Date: 2019/6/17 19:39
     * @Version: V1.0
     */
    String findCompleteTimeById(@Param("orderId") Long orderId);

    /**
     * 根据支付单号取消订单
     *
     * @param orderSnList
     * @param orderStatus
     * @param cancelCause
     */
    void updateOrderStatusByPaySn(@Param("orderSnList") List<Long> orderSnList,
                                  @Param("orderStatus") Integer orderStatus,
                                  @Param("appStatus") Integer appStatus,
                                  @Param("cancelCause") String cancelCause,
                                  @Param("cancelDate") Date orderDate);

    /**
     * 功能描述:
     * 〈根据订单sn取消订单与拆单子订单〉
     *
     * @param entity 修改订单实体
     * @author : 刘远杰
     */
    void updateOrderByOrderSnOrParent(OrderEntity entity);

    /**
     * 更新订单表中得评价状态
     *
     * @param orderId
     */
    void updateEvaluateStatus(Long orderId);

    Long findLastNoComplete(@Param("buyerId") Long id);

    /**
     * 功能描述:
     * 〈根据父订单号查询子订单留言〉
     *
     * @param parentOrderSn 父订单号
     * @author : 刘远杰
     */
    List<OrderDTO> findOrderMessageByParentOrderSn(@Param("parentOrderSn") Long parentOrderSn);

    /**
     * 功能描述:
     * 〈根据paySn获取最上级订单信息，如果有父订单查询父订单，不存在父订单查询子订单〉
     *
     * @param paySn 支付单号
     * @author : 刘远杰
     */
    OrderDTO getParentOrderByPaySn(Long paySn);

    /**
     * 查询周期内的订单
     *
     * @param storeId   店铺ID
     * @param startDate 开始时间
     * @param endDate   结束时间
     * @return
     */
    List<OrderDTO> findBillOrderList(@Param("storeId") Long storeId,
                                     @Param("startDate") String startDate,
                                     @Param("endDate") String endDate);

    /**
     * 查询订单实况信息
     *
     * @param storeId
     * @return
     */
    OrderLiveDTO orderLive(Long storeId);


    IndexOrderDataDTO selectIndexOrderData(@Param("startDate") Date startDate, @Param("endDate") Date endDate);


    List<IndexStoreSellRakingDTO> selectStoreSellRanking(@Param("startDate") Date startDate, @Param("endDate") Date endDate, @Param("orderBy") Integer orderBy);


    /**
     * 查询拼团相关的订单列表
     *
     * @return
     * @date 2020-03-17 10:33
     * @author huangkeyuan@leimingtech.com
     **/
    List<GroupOrderDetailDTO> findGroupOrderList(@Param("params") Map<String, Object> params);

    /**
     * 功能描述：
     * <统计用户秒杀商品购买数量>
     *
     * @param activityId 活动id
     * @param specId     规格id
     * @param buyerId    会员id
     * @return
     * @date 2020/3/23 14:55
     * @author 刘远杰
     **/
    Integer countSeckillOrderGoodsNum(@Param("activityId") Long activityId,
                                      @Param("specId") Long specId,
                                      @Param("buyerId") Long buyerId);

    /**
     * 功能描述：
     * <统计用户限时抢购商品购买数量>
     *
     * @param activityId 活动id
     * @param specId     规格id
     * @param buyerId    会员id
     * @return 购买数量
     * @date 2020/3/23 14:55
     * @author 刘远杰
     **/
    Integer countFlashSaleOrderGoodsNum(@Param("activityId") Long activityId,
                                        @Param("specId") Long specId,
                                        @Param("buyerId") Long buyerId);

    /**
     * 用户订单完成（已购买）商品ID
     *
     * @return
     */
    List<Long> getOrderGoodIds(Long memberId);

    List<EasyOrderExcelDTO> orderExport(Map<String, Object> params);


    /**
     * 查询未评价的订单
     *
     * @param params
     * @return
     */
    List<EvaluateOrderPageDTO> findNotEvaOrder(Map<String, Object> params);

    /**
     * pc查询已评价的订单
     *
     * @param params
     */
    List<EvaluateOrderPageDTO> pcEvaPage(Map<String, Object> params);

    List<PCOrderPageVO> findPcOrderInvoicePage(Map<String, Object> params);
}
