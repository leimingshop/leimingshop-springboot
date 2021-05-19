/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.service.payment;


import com.leimingtech.commons.tools.page.PageData;
import com.leimingtech.modules.dto.payment.OrderPayDTO;
import com.leimingtech.modules.dto.payment.OrderToPayDTO;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * @Author: weixianchun
 * @Description: 订单支付管理
 * @Date :2019/6/18 10:50
 * @Version V1.0
 **/

public interface OrderPayService {

    /**
     * @Author: weixianchun
     * @Description: 分页
     * @Date :2019/5/28 19:51
     * @Param params 查询条件
     * @Version V1.0
     **/

    PageData<OrderPayDTO> page(@RequestParam Map<String, Object> params);

    /**
     * @Author: weixianchun
     * @Description: 查询所有订单支付信息
     * @Date :2019/5/28 19:51
     * @Param params 查询条件
     * @Version V1.0
     **/

    List<OrderPayDTO> list(@RequestParam Map<String, Object> params);

    /**
     * @Author: weixianchun
     * @Description: 根据id查询订单支付信息
     * @Date :2019/5/28 19:52
     * @Param id 订单支付id
     * @Version V1.0
     **/

    OrderPayDTO get(Long id);

    /**
     * 功能描述:
     * 〈根据支付单号查询支付信息〉
     *
     * @param paySn 支付单号
     * @author : 刘远杰
     */

    OrderPayDTO getNoByPaySn(@RequestParam("paySn") Long paySn);

    /**
     * 功能描述:
     * 〈根据支付单号查询未支付信息〉
     *
     * @param paySn 支付单号
     * @author : 刘远杰
     */

    OrderPayDTO getByPaySn(Long paySn);


    /**
     * 功能描述:
     * 〈根据第三方支付单号查询支付信息〉
     *
     * @param transactionId 第三方交易单号
     * @author : 匡卫国
     */

    OrderPayDTO getByTransactionId(String transactionId);

    /**
     * @Author: weixianchun
     * @Description: 保存订单支付信息
     * @Date :2019/5/28 19:52
     * @Param dto 实体
     * @Version V1.0
     **/

    Boolean saveOrderPay(@RequestBody OrderPayDTO dto);

    /**
     * @Author: weixianchun
     * @Description: 修改订单支付信息
     * @Date :2019/5/28 19:52
     * @Param dto 实体
     * @Version V1.0
     **/

    void update(@RequestBody OrderPayDTO dto);

    /**
     * 功能描述:
     * 〈取消支付〉
     *
     * @param paySn 支付单号
     * @author : 刘远杰
     */

    Boolean cancelOrderPay(Long paySn);

    /**
     * @Author: weixianchun
     * @Description: 根据id删除订单支付信息
     * @Date :2019/5/28 19:53
     * @Param ids 订单支付id
     * @Version V1.0
     **/

    void delete(@RequestBody Long[] ids);

    /**
     * 功能描述:
     * 〈根据id删除订单支付信息〉
     *
     * @param id 订单支付id
     * @author : 刘远杰
     */

    void deleteById(Long id);

    /**
     * 功能描述:
     * 〈修改订单支付信息〉
     *
     * @param paySn 支付单号
     * @author : 刘远杰
     */

    int updatePayStateByPaySn(@RequestParam("paySn") Long paySn, @RequestParam("transactionId") String transactionId);

    /**
     * 功能描述:
     * 〈根据支付单号删除订单支付信息〉
     *
     * @param paySn
     * @return : void
     * @author : 刘远杰
     */

    int deleteByPaySn(@RequestParam("paySn") Long paySn);


    /**
     * 保存订单支付信息
     *
     * @param memberId: 会员ID
     * @return 保存成功的订单支付信息
     * @date 2019/6/22 20:22
     * @author LX lixiangx@leimingtech.com
     **/

    OrderPayDTO saveOrderPay(@RequestParam("memberId") Long memberId,
                             @RequestParam("orderSn") Long orderSn,
                             @RequestParam("payAmount") BigDecimal payAmount,
                             @RequestParam("goodsName") String goodsName,
                             @RequestParam(value = "cancelOrderTime") Long cancelOrderTime);

    /**
     * 定时取消订单
     */

    void canceledOrderTiming();

    /**
     * 功能描述:
     * 〈待付款订单去支付〉
     *
     * @param paySn   支付单号
     * @param buyerId 买家id
     * @return :
     * @author : 刘远杰
     */

    OrderToPayDTO payOrder(@RequestParam("paySn") Long paySn, @RequestParam("buyerId") Long buyerId);


    void cancelOrderPayByOrderSnList(@RequestBody List<Long> orderSnList);
}
