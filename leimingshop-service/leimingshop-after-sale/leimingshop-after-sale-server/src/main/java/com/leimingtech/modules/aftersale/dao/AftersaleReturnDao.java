/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.aftersale.dao;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.leimingtech.commons.mybatis.dao.BaseDao;
import com.leimingtech.modules.aftersale.dto.AftersaleReturnPageDTO;
import com.leimingtech.modules.aftersale.entity.AftersaleReturnEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 订单退货表
 *
 * @author huangkeyuan
 * @email huangkeyuan@leimingtech.dao
 * @since 1.0.0 2019-06-13
 */
@Mapper
public interface AftersaleReturnDao extends BaseDao<AftersaleReturnEntity> {
    /**
     * 分页查询退货信息
     *
     * @param params 查询条件
     * @param page   分页参数
     * @return 退货分页列表
     * @author xuzhch
     * @date 2020年09月21日
     */
    List<AftersaleReturnPageDTO> findPage(@Param("params") Map<String, Object> params, IPage page);

    /**
     * 导出列表查询
     *
     * @param params 查询条件
     * @return list 退货列表
     * @author xuzhch
     * @date 2020年09月21日
     */
    List<AftersaleReturnPageDTO> findListExport(@Param("params") Map<String, Object> params);

    /**
     * 退货商家确认收货
     *
     * @param aftersaleSn     售后单号
     * @param aftersaleStatus 售后状态
     * @param logisticsStatus 物流状态
     * @author xuzhch
     * @date 2020年09月21日
     */
    void confirmGoods(@Param("aftersaleSn") Long aftersaleSn,
                      @Param("aftersaleStatus") Integer aftersaleStatus,
                      @Param("logisticsStatus") Integer logisticsStatus);

    /**
     * 查询超时的售后单
     *
     * @param days            天数
     * @param logisticsStatus 物流状态
     * @return list
     * @author xuzhch
     * @date 2020年09月21日
     */
    List<String> findSellerInOutTimeList(@Param("days") String days, @Param("logisticsStatus") String logisticsStatus);

    /**
     * 批量取消
     *
     * @param aftersaleReturnList 退货列表
     * @author xuzhch
     * @date 2020年09月21日
     */
    void batchCancel(@Param("aftersaleReturnList") List<String> aftersaleReturnList);

    /**
     * 修改售后状态
     *
     * @param aftersaleSn 售后单号
     * @param value       售后单状态
     * @author xuzhch
     * @date 2020年09月21日
     */
    void updateStatus(@Param("aftersaleSn") Long aftersaleSn, @Param("value") Integer value);

    /**
     * 查询退换货数量
     *
     * @param storeId 店铺ID
     * @param type    1 换货 2 退货
     * @return 退换货数量
     * @author xuzhch
     * @date 2020年09月21日
     */
    Integer findCount(@Param("storeId") Long storeId, @Param("type") Integer type);
}
