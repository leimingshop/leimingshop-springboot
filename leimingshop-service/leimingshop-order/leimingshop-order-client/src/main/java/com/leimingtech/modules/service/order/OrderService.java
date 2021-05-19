/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.service.order;


import com.leimingtech.commons.tools.page.PageData;
import com.leimingtech.modules.dto.order.*;
import com.leimingtech.modules.vo.order.PCOrderInvoicePageVO;
import com.leimingtech.modules.vo.order.PCOrderPageVO;
import com.leimingtech.modules.vo.order.PcOrderDetailVO;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

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
public interface OrderService {

    /**
     * 功能描述:
     * 〈订单分页查询〉
     *
     * @param params 查询条件
     * @author : 刘远杰
     */

    PageData<OrderDTO> page(@RequestParam Map<String, Object> params,
                            @RequestParam(value = "storeId", required = false) Long storeId);

    /**
     * 拼团订单列表
     *
     * @return
     * @date 2020-03-25 16:50
     * @author huangkeyuan@leimingtech.com
     **/

    PageData<OrderDTO> groupPage(@RequestParam Map<String, Object> params,
                                 @RequestParam(value = "storeId", required = false) Long storeId);

    /**
     * 功能描述:
     * 〈h5订单分页查询〉
     *
     * @param params 查询条件
     * @author : 刘远杰
     */

    PageData<H5OrderPageDTO> pageH5(@RequestParam Map<String, Object> params,
                                    @RequestParam("buyerId") Long buyerId);


    /**
     * pc订单分页查询
     *
     * @return
     * @date 2020-05-18 14:52
     * @author huangkeyuan@leimingtech.com
     **/

    PageData<PCOrderPageVO> pagePC(@RequestParam Map<String, Object> params,
                                   @RequestParam("buyerId") Long buyerId);

    /**
     * 功能描述:
     * 〈admin/seller订单导出〉
     *
     * @param params 查询条件
     * @author : 刘远杰
     */

    /**
     * @Author weixianchun
     * @Description 导出订单列表
     * @Param params
     * @Date 2019/12/4 21:24
     * @Return java.util.List<com.leimingtech.modules.dto.order.OrderDTO>
     * @version 1.0
     */

    List<OrderDTO> findOrderListExport(@RequestParam Map<String, Object> params);

    /**
     * 功能描述:
     * 〈订单详情〉
     *
     * @param id 订单id
     * @author : 刘远杰
     */

    AdminOrderDetailDTO getAdminOrderDetail(Long id,
                                            @RequestParam(value = "buyerId", required = false) Long buyerId,
                                            @RequestParam(value = "storeId", required = false) Long storeId);

    /**
     * H5查询订单详情
     *
     * @param orderId:  订单ID
     * @param memberId: 会员ID
     * @return 订单详情数据（基础、地址、商品数据）
     * @date 2019/11/6 17:48
     * @author lixiangx@leimingtech.com
     **/

    H5OrderDetailDTO findH5OrderDetail(@RequestParam("orderId") Long orderId,
                                       @RequestParam("memberId") Long memberId);

    /**
     * 查询pc端订单详情
     *
     * @return
     * @date 2020-05-21 11:47
     * @author huangkeyuan@leimingtech.com
     **/

    PcOrderDetailVO findPCOrderDetail(@RequestParam("orderId") Long orderId,
                                      @RequestParam("memberId") Long memberId);

    /**
     * 根据用户信息和订单信息查询订单
     *
     * @param orderId:  订单id
     * @param memberId: 会员ID
     * @return 订单基础信息
     * @date 2019/11/6 17:58
     * @author lixiangx@leimingtech.com
     **/

    OrderDTO findOrderDetail(@RequestParam("orderId") Long orderId,
                             @RequestParam("memberId") Long memberId);

    /**
     * 根据订单编号获取订单信息
     *
     * @param orderSn: 订单编号
     * @return 订单基础信息
     * @date 2020/4/23 16:59
     * @author lixiangx@leimingtech.com
     **/

    OrderDTO findOrderByOrderSn(@RequestParam("orderSn") String orderSn);

    /**
     * 功能描述:
     * 〈根据父订单号查询子订单集合〉
     *
     * @param parentOrderSn 父订单编号
     * @author : 刘远杰
     */

    List<OrderDTO> childOrderList(Long parentOrderSn);

    /**
     * 功能描述:
     * 〈根据订单编号查询订单详情〉
     *
     * @param orderSn 订单编号
     * @param buyerId 买家id
     * @param storeId 店铺id
     * @author : 刘远杰
     */

    AdminOrderDetailDTO getAdminOrderDetailByOrderSn(Long orderSn,
                                                     @RequestParam(value = "buyerId", required = false) Long buyerId,
                                                     @RequestParam(value = "storeId", required = false) Long storeId);

    /**
     * 功能描述：
     * <根据订单编号查询订单详情及订单活动信息>
     *
     * @param orderSn 订单编号
     * @param storeId 店铺id
     * @return
     * @date 2020/2/24 10:51
     * @author 刘远杰
     **/

    AdminOrderActivityDetailDTO getOrderActivityDetail(Long orderSn,
                                                       @RequestParam(value = "storeId", required = false) Long storeId);

    /**
     * 功能描述:
     * 〈保存订单〉
     *
     * @param dto 订单实体
     * @author : 刘远杰
     */

    void save(@RequestBody OrderDTO dto);

    /**
     * 功能描述:
     * 〈查询某支付状态订单数量〉
     *
     * @param buyerId 会员id
     * @author : 刘远杰
     */

    int countOrder(Integer orderStatus, @RequestParam("buyerId") Long buyerId);

    /**
     * 功能描述:
     * 〈会员中心订单数量查询〉
     *
     * @param buyerId 会员id
     * @author : 刘远杰
     */

    MemberOrderCountDTO countMemberOrder(@RequestParam("buyerId") Long buyerId);

    /**
     * @param params:查询参数
     * @Author: SWH ab4856812@163.com
     * @Description:订单可申请售后服务列表
     * @Date: 2019/6/17 19:39
     * @Version: V1.0
     */

    PageData<AvailableAfterSaleOrderDTO> findAvailAfterList(@RequestParam Map<String, Object> params);


    /**
     * 功能描述:
     * 〈提交立即购买订单〉
     *
     * @param dto 订单保存实体
     * @author : 刘远杰
     */

    OrderSaveResultDTO saveOrderNow(@RequestBody InsertNowOrderVoDTO dto, @RequestParam("buyerId") Long buyerId);

    /**
     * 拼团保存订单
     *
     * @param dto     提交订单实体
     * @param buyerId 会员id
     * @return
     * @date 2020-03-23 10:27
     * @author huangkeyuan@leimingtech.com
     **/

    OrderSaveResultDTO saveGroupOrderNow(@RequestBody InsertNowOrderVoDTO dto, @RequestParam("buyerId") Long buyerId);

    /**
     * @Author weixianchun
     * @Description 保存商家备注信息
     * @Param dto
     * @Param buyerId
     * @Date 2019/11/11 10:07
     * @Return java.lang.Long
     * @version 1.0
     */

    void updateSellerRemark(@RequestBody OrderSellerRemarkDTO dto);

    /**
     * 根据orderSN更新订单余额信息
     *
     * @param orderDTO 订单信息
     * @date 2020-07-09 20:11
     * @author huangkeyuan@leimingtech.com
     **/

    void putOrderDTO(@RequestBody OrderDTO orderDTO);

    /**
     * @Author hjn
     * @Description 开票--修改订单开票状态
     * @Param dto
     * @Param id
     * @Date 2020/4/1
     * @Return java.lang.Long
     * @version 1.0
     */

    void updateByInvoiceFlag(@RequestBody OrderDTO dto);


    /**
     * @param orderId:订单id
     * @Author: SWH ab4856812@163.com
     * @Description:根据订单号查询订单完成时间
     * @Date: 2019/6/17 19:39
     * @Version: V1.0
     */

    String findCompleteTimeById(@RequestParam("orderId") Long orderId);

    /**
     * 功能描述:
     * 〈保存立即购买订单确认表，订单商品确认表〉
     *
     * @param dto     立即购买提交订单参数实体
     * @param buyerId 买家id
     * @return : void
     * @author : 刘远杰
     */

    OrderConfirmDTO saveNowOrderConfirm(@RequestBody InsertNowOrderVoDTO dto, @RequestParam("buyerId") Long buyerId);

    /**
     * 功能描述:
     * 〈商家订单发货〉
     *
     * @param orderDeliverDTO 订单发货实体
     * @param storeId         店铺id
     * @author : 刘远杰
     */

    void shippment(@RequestBody OrderDeliverDTO orderDeliverDTO,
                   @RequestParam("storeId") Long storeId);

    /**
     * 确认收货
     *
     * @param id
     */

    Map<String, Object> confirmReceipt(Long id, @RequestParam(value = "buyerId", required = false) Long buyerId);

    /**
     * 定时确认收货
     */

    void orderReceivingTiming();

    /**
     * 取消订单(h5)
     *
     * @param dto
     */

    void cancelOrder(@RequestBody OrderCancelDTO dto, @RequestParam(value = "buyerId", required = false) Long buyerId);

    /**
     * 取消订单(seller)
     *
     * @param dto
     */

    void cancelOrderSeller(@RequestBody OrderCancelDTO dto, @RequestParam(value = "storeId", required = false) Long storeId);


    /**
     * 获取所有未付款可取消的订单list
     *
     * @param
     * @return
     */

    List<OrderDTO> orderCanceledList(@RequestParam("delDate") Date delDate);

    /**
     * 删除订单
     *
     * @param id
     */

    void delOrder(Long id, @RequestParam("buyerId") Long buyerId);

    /**
     * 购物车提交订单
     *
     * @param insertCartSaveOrderVoDTO: 会员优惠券id json {storeId:memberCouponsId}
     * @return 封装返回信息 code:状态码 msg:错误信息描述 data:数据
     * @date 2019/6/22 14:16
     * @author LX lixiangx@leimingtech.com
     **/

    OrderSaveResultDTO saveOrderToCart(@RequestBody InsertCartSaveOrderVoDTO insertCartSaveOrderVoDTO);


    /**
     * 封装并保存订单确认信息数据
     *
     * @param buyerId:      会员Id
     * @param addressId:    地址ID
     * @param orderMessage: 订单留言json格式:[{店铺id:买家留言}]
     * @param cartDTOList:  用户勾选购物车信息集合
     * @date 2019/6/22 14:45
     * @author LX lixiangx@leimingtech.com
     **/
//
//    OrderConfirmDTO saveOrderConfirm(@RequestParam("buyerId") Long buyerId, @RequestParam("addressId") Long addressId,
//                                     @RequestParam("orderMessage") String orderMessage, @RequestBody List cartDTOList,
//                                     @RequestParam Map<String, Long> memberCouponsId,
//                                     @RequestParam(value = "orderPlatform", required = false) Integer orderPlatform);


    /**
     * 功能描述:
     * 〈根据支付单号查询订单〉
     *
     * @param paySn 支付单号
     * @author : 刘远杰
     */

    List<OrderDTO> getByPaySn(@RequestParam("paySn") Long paySn);

    /**
     * @Author: SWH ab4856812@163.com
     * @Description:根据自定义参数查询订单信息
     * @Date: 2019/6/21 16:30
     * @Version: V1.0
     */

    OrderDTO getOrderByMap(@RequestParam Map<String, Object> map);

    /**
     * 功能描述:
     * 〈微信支付异步修改订单状态〉
     *
     * @param updateOrderDTO
     * @author : 刘远杰
     */

    boolean updateOrderStatePayFinish(@RequestBody UpdateOrderDTO updateOrderDTO);

    /**
     * CartConfirmOrderConsumer消费者中订单处理 (内部模块使用)
     *
     * @param orderConfirmId: 订单确认表ID
     * @throws Exception 抛出订单保存失败异常
     * @date 2019/6/25 14:32
     * @author lixiang
     **/

    void packOrderFromConsumer(@RequestParam("orderConfirmId") Long orderConfirmId);

    /**
     * 余额支付完成后调用
     *
     * @param paySn 支付单号
     * @return
     * @date 2020-07-10 14:11
     * @author huangkeyuan@leimingtech.com
     **/

    void balanceFinsh(@RequestParam("paySn") Long paySn);

    /**
     * ConfirmGroupOrderConsumer消费者中订单处理
     *
     * @param orderConfirmId 确认订单id
     * @return
     * @date 2020-03-24 10:31
     * @author huangkeyuan@leimingtech.com
     **/

    void packGroupOrderFromConsumer(@RequestParam("orderConfirmId") Long orderConfirmId);

    /**
     * 定时取消订单
     */

    Boolean orderCanceledTiming(@RequestBody List<Long> orderSnList);

    /**
     * 根据用户id 订单状态查询订单数量
     *
     * @param id
     * @param orderStatus
     * @return
     */

    Integer findOrderNum(Long id, @RequestParam("orderStatus") Integer orderStatus, @RequestParam(value = "evaluateStatus", required = false) Integer evaluateStatus);

    /**
     * 更新订单评价状态
     *
     * @param orderId
     */

    void updateEvaluateStatus(@RequestParam("orderId") Long orderId);


    Long findLastNoComplete(Long id);

    /**
     * 查询周期内的订单
     *
     * @param storeId   店铺id
     * @param startDate 开始时间
     * @param endDate   结束时间
     * @return
     */

    List<OrderDTO> findBillOrderList(@RequestParam("storeId") Long storeId,
                                     @RequestParam(value = "startDate", required = false) String startDate,
                                     @RequestParam("endDate") String endDate);

    /**
     * 拼团订单列表
     *
     * @return
     * @date 2020-03-17 10:16
     * @author huangkeyuan@leimingtech.com
     **/

    List<GroupOrderDetailDTO> findGroupOrderList(@RequestParam Map<String, Object> map);

    /**
     * 批量修改拼团关联的订单状态
     *
     * @param orderIds         拼团订单id集合
     * @param orderGroupStatus 拼团订单状态
     * @return
     * @date 2020-03-18 11:12
     * @author huangkeyuan@leimingtech.com
     **/

    void updateGroupStatus(@RequestParam("orderIds") List<Long> orderIds, @RequestParam("orderGroupStatus") Integer orderGroupStatus);


    /**
     * 订单实况数据
     *
     * @param storeId
     * @param type    是否刷新数据 1 刷新
     * @return
     */

    OrderLiveDTO orderLive(@RequestParam("storeId") Long storeId, @RequestParam(value = "type", required = false) Integer type);

    /**
     * 首页>基础概况>订单数据查询
     *
     * @param startDateStr 开始时间字符串
     * @param endDateStr   结束时间字符串
     * @return IndexOrderDataDTO
     * @date 2020/4/7/007 11:59
     * @author xuzhch
     */

    IndexOrderDataDTO indexOrderData(@RequestParam("startDateStr") String startDateStr, @RequestParam("endDateStr") String endDateStr);

    /**
     * 商户销售排名
     *
     * @param params
     * @return
     * @date 2020/3/16/016 16:25
     * @author xuzhch
     **/

    Map<String, Object> storeSellRanking(@RequestParam Map params);

    /**
     * 用户订单完成（已购买）商品ID
     *
     * @param memberId 用户ID
     * @return
     * @Author: yuhaifeng
     */

    List<Long> getOrderGoodIds(@RequestParam("memberId") Long memberId);


    PageData<EasyOrderExcelDTO> orderExport(@RequestParam Map<String, Object> params);


    OrderDTO get(Long orderId);


    void updateAfterFlag(@RequestBody List<Long> orderIds, @RequestParam("afterFlag") Integer afterFlag);

    /**
     * pc查询未评价的订单
     *
     * @param params
     */

    PageData<EvaluateOrderPageDTO> findNotEvaOrder(@RequestParam Map<String, Object> params);

    /**
     * pc查询已评价订单
     *
     * @param params
     * @return
     */

    PageData<EvaluateOrderPageDTO> pcEvaPage(@RequestParam Map<String, Object> params);


    PageData<PCOrderInvoicePageVO> invoicePage(@RequestParam Map<String, Object> params);
}
