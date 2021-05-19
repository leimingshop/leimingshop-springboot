/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.aftersale.dao;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.leimingtech.commons.mybatis.dao.BaseDao;
import com.leimingtech.modules.aftersale.dto.AftersaleApplyDTO;
import com.leimingtech.modules.aftersale.dto.AftersaleApplyPageDTO;
import com.leimingtech.modules.aftersale.dto.AftersaleApplyRecordDTO;
import com.leimingtech.modules.aftersale.entity.AftersaleApplyEntity;
import com.leimingtech.modules.dto.settle.ReturnBillDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 售后申请
 *
 * @author xuzhch
 * @date: 2019/6/17 14:59
 */
@Mapper
public interface AftersaleApplyDao extends BaseDao<AftersaleApplyEntity> {
    /**
     * 分页查询售后申请信息
     *
     * @param params 查询条件
     * @param page   分页条件
     * @return List<AftersaleApplyPageDTO> 分页结果
     * @author xuzhch
     * @date 2020年09月21日
     */
    List<AftersaleApplyPageDTO> findPage(@Param("params") Map<String, Object> params, IPage page);

    /**
     * H5端售后申请记录列表
     *
     * @param page   分页条件
     * @param params 查询条件
     * @return list 申请记录列表
     * @author xuzhch
     * @date 2020年09月21日
     */
    List<AftersaleApplyRecordDTO> applyRecordPage(IPage page, @Param("params") Map<String, Object> params);

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
    List<AftersaleApplyDTO> findAutoApplyList(@Param("aftersaleType") Integer aftersaleType, @Param("agreeReturn") String agreeReturn, @Param("auditStatus") Integer auditStatus);

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
    List<ReturnBillDTO> findBillOrderList(@Param("storeId") Long storeId,
                                          @Param("startDate") String startDate,
                                          @Param("endDate") String endDate);

    /**
     * 查询待审核订单
     *
     * @param storeId 店铺ID
     * @return 待审核订单数量
     * @author xuzhch
     * @date 2020年09月21日
     */
    Integer findAfterAuditCount(Long storeId);
}
