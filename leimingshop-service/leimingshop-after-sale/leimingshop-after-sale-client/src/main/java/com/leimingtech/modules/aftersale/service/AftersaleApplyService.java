/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.aftersale.service;


import com.leimingtech.commons.tools.page.PageData;
import com.leimingtech.modules.aftersale.dto.*;
import com.leimingtech.modules.dto.settle.ReturnBillDTO;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

/**
 * 售后申请
 *
 * @author huangkeyuan
 * @email huangkeyuan@leimingtech.dao
 * @since 1.0.0 2019-06-13 fallback = FavoritesFallback.class
 */

public interface AftersaleApplyService {

    /**
     * 保存售后申请
     *
     * @param dto 售后申请信息
     * @return integer
     * @author xuzhch
     * @date 2020年09月21日
     */

    Integer save(@RequestBody AftersaleApplySaveDTO dto);

    /**
     * 修改售后申请
     *
     * @param dto 售后申请信息
     * @author xuzhch
     * @date 2020年09月21日
     */

    void update(@RequestBody AftersaleApplyDTO dto);

    /**
     * 删除售后申请
     *
     * @param ids 售后申请ID数组
     * @author xuzhch
     * @date 2020年09月21日
     */

    void delete(@RequestBody Long[] ids);

    /**
     * 根据ID查询售后申请
     *
     * @param id 售后申请ID
     * @return AftersaleApplyDTO 售后申请信息
     * @author xuzhch
     * @date 2020年09月21日
     */

    AftersaleApplyDTO get(Long id);

    /**
     * 根据售后单号查询售后申请
     *
     * @param aftersaleSn 售后单号
     * @return AftersaleApplyDTO  售后申请信息
     * @author huangkeyuan @leimingtech.com
     * @date 2020 -09-08 14:46
     */

    AftersaleApplyDTO getApply(Long aftersaleSn);

    /**
     * 根据售后单号和用户id查询退货信息
     *
     * @param aftersaleSn : 售后单号
     * @param memberId    : 用户id
     * @return the detail
     * @author xuzhch
     * @date 2020年09月21日
     */

    AftersaleApplyDTO getDetail(@RequestParam("aftersaleSn") Long aftersaleSn, @RequestParam("memberId") Long memberId);

    /**
     * 查询所有的售后申请列表
     *
     * @param params 查询条件
     * @return java.util.List<dao.leimingtech.dto.reason.AftersaleApplyDTO>
     * @author xuzhch
     * @date 2020年09月21日
     */

    List<AftersaleApplyDTO> list(@RequestParam Map<String, Object> params);

    /**
     * 分页查询所有的售后申请列表
     *
     * @param params  查询条件
     * @param storeId 店铺ID
     * @return dao.leimingtech.commons.tools.page.PageData<dao.leimingtech.dto.reason.AftersaleApplyDTO>   data
     * @author xuzhch
     * @date 2020年09月21日
     */

    PageData<AftersaleApplyPageDTO> pageData(@RequestParam Map<String, Object> params, @RequestParam(value = "storeId", required = false) Long storeId);

    /**
     * 售后申请详情
     *
     * @param aftersaleSn : 售后单号
     * @return com.leimingtech.modules.aftersale.dto.AftersaleReturnDetailDTO apply detail dto
     * @author xuzhch
     * @date 2020年09月21日
     * @Author huangkeyuan
     * @Date 11 :16 2019-06-17
     */

    AftersaleApplyDetailDTO detail(Long aftersaleSn);

    /**
     * 售后申请审核结果
     *
     * @param dto 售后审核信息
     * @return map 保存结果
     * @author xuzhch
     * @date 2020年09月21日
     */

    Map<String, Object> saveApplyResult(@RequestBody AftersaleAuditLogSaveDTO dto);

    /**
     * H5端售后申请记录列表
     *
     * @param params   :查询条件
     * @param memberId :用户Id
     * @return page data
     * @author xuzhch
     * @date 2020年09月21日
     */

    PageData<AftersaleApplyRecordDTO> applyRecordPage(@RequestParam Map<String, Object> params, @RequestParam("memberId") Long memberId);


    /**
     * H5端售后详情
     *
     * @param params   :查询条件
     * @param memberId 用户ID
     * @return H5售后详情
     * @author xuzhch
     * @date 2020年09月21日
     */

    AfterSaleDetailDTO findAftersaleDetail(@RequestParam Map<String, Object> params, @RequestParam("memberId") Long memberId);

    /**
     * H5端售后买家上传物流信息
     *
     * @param aftersaleDeliveryDTO :保存实体
     * @param memberId             用户ID
     * @return map
     * @author xuzhch
     * @date 2020年09月21日
     */

    Map<String, Object> saveDelivery(@RequestBody AftersaleDeliveryDTO aftersaleDeliveryDTO, @RequestParam("memberId") Long memberId);

    /**
     * H5端售后取消申请接口
     *
     * @param aftersaleSn :售后单号
     * @param memberId    用户ID
     * @return map 取消结果
     * @author xuzhch
     * @date 2020年09月21日
     */

    Map<String, Object> cancelApply(@RequestParam("aftersaleSn") Long aftersaleSn, @RequestParam("memberId") Long memberId);

    /**
     * H5端售后买家确认收货接口
     *
     * @param aftersaleSn :售后单号
     * @param memberId    用户ID
     * @return map
     * @author xuzhch
     * @date 2020年09月21日
     */

    Map<String, Object> confirmReceipt(@RequestParam("aftersaleSn") Long aftersaleSn, @RequestParam("memberId") Long memberId);

    /**
     * 保存售后申请记录
     *
     * @param dataList :售后申请实体
     * @return map
     * @author xuzhch
     * @date 2020年09月21日
     */

    Map<String, Object> saveAfterSaleApply(@RequestBody List<AftersaleApplyFrontSaveDTO> dataList);

    /**
     * 查询申请记录
     *
     * @param aftersaleType 售后类型
     * @param agreeReturn   售后设置信息
     * @param auditStatus   审核人
     * @return list
     * @author xuzhch
     * @date 2020年09月21日
     */

    List<AftersaleApplyDTO> findAutoApplyList(@RequestParam("aftersaleType") Integer aftersaleType, @RequestParam("agreeReturn") String agreeReturn, @RequestParam("auditStatus") Integer auditStatus);

    /**
     * 查询退款订单
     *
     * @param storeId   店铺ID
     * @param startDate 开始时间
     * @param endDate   结束时间
     * @return 退款订单列表
     * @author xuzhch
     * @date 2020年09月21日
     */

    List<ReturnBillDTO> findBillOrderList(@RequestParam("storeId") Long storeId,
                                          @RequestParam(value = "startDate", required = false) String startDate,
                                          @RequestParam("endDate") String endDate);

    /**
     * 查询待审核订单
     *
     * @param storeId 店铺ID
     * @return 待审核订单数量
     * @author xuzhch
     * @date 2020年09月21日
     */

    Integer findAfterAuditCount(@RequestParam("storeId") Long storeId);
}
