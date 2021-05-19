/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.aftersale.service;


import com.leimingtech.commons.tools.page.PageData;
import com.leimingtech.modules.aftersale.dto.AftersaleReturnDTO;
import com.leimingtech.modules.aftersale.dto.AftersaleReturnDetailDTO;
import com.leimingtech.modules.aftersale.dto.AftersaleReturnPageDTO;
import com.leimingtech.modules.aftersale.dto.AftersaleReturnSaveDTO;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

/**
 * 订单退货
 *
 * @author huangkeyuan
 * @email huangkeyuan@leimingtech.dao
 * @since 1.0.0 2019-06-13
 */

public interface AftersaleReturnService {
    /**
     * 保存订单退货记录
     *
     * @param dto 订单退货信息
     * @author xuzhch
     * @date 2020年09月21日
     */

    void save(@RequestBody AftersaleReturnSaveDTO dto);

    /**
     * 修改订单退货记录
     *
     * @param dto 订单退货信息
     * @author xuzhch
     * @date 2020年09月21日
     */

    void update(@RequestBody AftersaleReturnDTO dto);

    /**
     * 删除订单退货记录
     *
     * @param ids 退货表ID
     * @author xuzhch
     * @date 2020年09月21日
     */

    void delete(@RequestBody Long[] ids);

    /**
     * 根据售后单号查询订单退货记录
     *
     * @param id 售后单号
     * @return return dto
     * @author xuzhch
     * @date 2020年09月21日
     */

    AftersaleReturnDTO get(Long id);

    /**
     * 查询所有的订单退货记录列表
     *
     * @param params 查询条件
     * @return java.util.List<dao.leimingtech.dto.reason.AftersaleReturnDTO>
     * @author xuzhch
     * @date 2020年09月21日
     */

    List<AftersaleReturnDTO> list(@RequestParam Map<String, Object> params);

    /**
     * 分页查询所有的订单退货记录列表
     *
     * @param params  查询条件
     * @param storeId 店铺ID
     * @return dao.leimingtech.commons.tools.page.PageData<dao.leimingtech.dto.reason.AftersaleReturnDTO> data
     * @author xuzhch
     * @date 2020年09月21日
     */

    PageData<AftersaleReturnPageDTO> pageData(@RequestParam Map<String, Object> params, @RequestParam(value = "storeId", required = false) Long storeId);

    /**
     * 退货详情
     *
     * @param aftersaleSn 售后单号
     * @return com.leimingtech.modules.aftersale.dto.AftersaleReturnDetailDTO return detail dto
     * @author xuzhch
     * @date 2020年09月21日
     */

    AftersaleReturnDetailDTO detail(Long aftersaleSn);

    /**
     * 退货商家确认收货接口
     *
     * @param aftersaleSn     售后单号
     * @param aftersaleStatus 售后状态
     * @return map
     * @author xuzhch
     * @date 2020年09月21日
     */

    Map<String, Object> confirmGoods(@RequestParam("aftersaleSn") Long aftersaleSn, @RequestParam("aftersaleStatus") Integer aftersaleStatus);

    /**
     * 微信退款
     *
     * @param id :退款单号
     * @return map
     * @author xuzhch
     * @date 2020年09月21日
     */

    Map<String, Object> wxRefund(@RequestParam("id") Long id);

    /**
     * 微信退款后修改状态
     *
     * @param id          :退款记录id
     * @param aftersaleSn :退款编号
     * @param status      :0成功       1失败
     * @return map
     * @author xuzhch
     * @date 2020年09月21日
     */

    Map<String, Object> changeStateAfterRefund(@RequestParam("id") Long id, @RequestParam("aftersaleSn") Long aftersaleSn, @RequestParam("status") Integer status);

    /**
     * 审核同意退货申请xx天后，买家未处理，自动取消退货处理
     *
     * @param days            天数
     * @param logisticsStatus 物流状态
     * @return list
     * @author xuzhch
     * @date 2020年09月21日
     */

    List<String> findSellerInOutTimeList(@RequestParam("days") String days, @RequestParam("logisticsStatus") String logisticsStatus);

    /**
     * 批量取消
     *
     * @param aftersaleReturnList 售后单号集合
     * @author xuzhch
     * @date 2020年09月21日
     */

    void batchCancel(@RequestBody List<String> aftersaleReturnList);

    /**
     * 根据售后单号和用户id查询退货信息
     *
     * @param aftersaleSn : 售后单号
     * @param memberId    :    用户id
     * @return the detail
     * @author xuzhch
     * @date 2020年09月21日
     */

    AftersaleReturnDTO getDetail(@RequestParam("aftersaleSn") Long aftersaleSn, @RequestParam("memberId") Long memberId);

    /**
     * 导出列表查询
     *
     * @param params 查询条件
     * @return list 退货列表
     * @author xuzhch
     * @date 2020年09月21日
     */

    List<AftersaleReturnPageDTO> findListExport(@RequestParam Map<String, Object> params);

    /**
     * 查询退换货数量
     *
     * @param storeId 店铺ID
     * @param type    1 换货 2 退货
     * @return 数量
     * @author xuzhch
     * @date 2020年09月21日
     */

    Integer findCount(@RequestParam("storeId") Long storeId, @RequestParam("type") Integer type);

    /**
     * 售后退款(MQ消费者)
     *
     * @param h5WxRefundBaseStr 微信退款数据
     * @author xuzhch
     * @date 2020年09月21日
     */

    void aftersaleReturn(@RequestParam("h5WxRefundBaseStr") String h5WxRefundBaseStr);

}
