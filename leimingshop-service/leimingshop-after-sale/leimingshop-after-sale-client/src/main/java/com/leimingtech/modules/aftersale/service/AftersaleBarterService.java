/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.aftersale.service;


import com.leimingtech.commons.tools.page.PageData;
import com.leimingtech.modules.aftersale.dto.AftersaleBarterDTO;
import com.leimingtech.modules.aftersale.dto.AftersaleBarterDetailDTO;
import com.leimingtech.modules.aftersale.dto.AftersaleBarterSaveDTO;
import com.leimingtech.modules.aftersale.dto.AftersaleReturnPageDTO;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

/**
 * 订单换货
 *
 * @author huangkeyuan
 * @email huangkeyuan@leimingtech.dao
 * @since 1.0.0 2019-06-13
 */

public interface AftersaleBarterService {
    /**
     * 保存订单换货记录
     *
     * @param dto
     */

    void save(@RequestBody AftersaleBarterSaveDTO dto);

    /**
     * 修改订单换货记录
     *
     * @param dto
     */

    void update(@RequestBody AftersaleBarterDTO dto);

    /**
     * 删除订单换货记录
     *
     * @param ids
     */

    void delete(@RequestBody Long[] ids);

    /**
     * 根据ID查询订单换货记录
     *
     * @param id
     * @return
     */

    AftersaleBarterDTO get(Long id);

    /**
     * 查询所有的订单换货记录列表
     *
     * @param params 查询条件
     * @return java.util.List<dao.leimingtech.dto.reason.AftersaleBarterDTO>
     * @author huangkeyuan
     * @date 18 :13 2019-06-10
     */

    List<AftersaleBarterDTO> list(@RequestParam Map<String, Object> params);

    /**
     * 分页查询所有的订单换货记录列表
     *
     * @param params  查询条件
     * @param storeId 店铺ID
     * @return dao.leimingtech.commons.tools.page.PageData<dao.leimingtech.dto.reason.AftersaleBarterDTO> data
     * @author xuzhch
     * @date 2020年09月21日
     */

    PageData<AftersaleReturnPageDTO> pageData(@RequestParam Map<String, Object> params, @RequestParam(value = "storeId", required = false) Long storeId);

    /**
     * 换货详情
     *
     * @param aftersaleSn 售后单号
     * @return com.leimingtech.modules.aftersale.dto.AftersaleReturnDetailDTO barter detail dto
     * @author xuzhch
     * @date 2020年09月21日
     */

    AftersaleBarterDetailDTO detail(Long aftersaleSn);

    /**
     * 换货商家上传发货物流信息的接口
     *
     * @param params 物流信息
     * @author xuzhch
     * @date 2020年09月21日
     */

    void uploadExpress(@RequestParam Map<String, Object> params);

    /**
     * 换货商家确认收货接口
     *
     * @param aftersaleSn     售后单号
     * @param aftersaleStatus 售后状态
     * @author xuzhch
     * @date 2020年09月21日
     */

    void confirmGoods(@RequestParam("aftersaleSn") Long aftersaleSn, @RequestParam("aftersaleStatus") Integer aftersaleStatus);

    /**
     * 审核同意换货申请xx天后，买家未处理，自动取消换货处理
     *
     * @param days            天数
     * @param logisticsStatus 售后状态
     * @return list
     * @author xuzhch
     * @date 2020年09月21日
     */

    List<String> findSellerInOutTimeList(@RequestParam("days") String days, @RequestParam("logisticsStatus") String logisticsStatus);

    /**
     * 批量取消
     *
     * @param aftersaleBarterList 换货列表信息
     * @author xuzhch
     * @date 2020年09月21日
     */

    void batchCancel(@RequestBody List<String> aftersaleBarterList);

    /**
     * 根据售后单号和用户id查询退货信息
     *
     * @param aftersaleSn : 售后单号
     * @param memberId    :    用户id
     * @return the detail
     * @author xuzhch
     * @date 2020年09月21日
     */

    AftersaleBarterDTO getDetail(@RequestParam("aftersaleSn") Long aftersaleSn, @RequestParam("memberId") Long memberId);


    /**
     * 换货 换货列表导出
     *
     * @param params java.util.List<com.leimingtech.modules.aftersale.dto.AftersaleReturnPageDTO>
     * @return list
     * @author xuzhch
     * @date 2020年09月21日
     */

    List<AftersaleReturnPageDTO> findListExport(@RequestParam Map<String, Object> params);
}
