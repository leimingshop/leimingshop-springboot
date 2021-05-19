/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.aftersale.dao;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.leimingtech.commons.mybatis.dao.BaseDao;
import com.leimingtech.modules.aftersale.dto.AftersaleReturnPageDTO;
import com.leimingtech.modules.aftersale.entity.AftersaleBarterEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 订单换货表
 *
 * @author huangkeyuan
 * @email huangkeyuan@leimingtech.dao
 * @since 1.0.0 2019-06-13
 */
@Mapper
public interface AftersaleBarterDao extends BaseDao<AftersaleBarterEntity> {
    /**
     * 分页查询换货信息
     *
     * @param params 查询条件
     * @param page   分页参数
     * @return 换货分页列表
     * @author xuzhch
     * @date 2020年09月21日
     */
    List<AftersaleReturnPageDTO> findPage(@Param("params") Map<String, Object> params, IPage page);


    /**
     * 换货商家确认收货
     *
     * @param aftersaleSn     售后单号
     * @param aftersaleStatus 售后单状态
     * @param logisticsStatus 物流状态
     * @author xuzhch
     * @date 2020年09月21日
     */
    void confirmGoods(@Param("aftersaleSn") Long aftersaleSn,
                      @Param("aftersaleStatus") Integer aftersaleStatus,
                      @Param("logisticsStatus") Integer logisticsStatus);

    /**
     * 商家上传物流信息
     *
     * @param params 物流信息参数
     * @author xuzhch
     * @date 2020年09月21日
     */
    void uploadExpress(@Param("params") Map<String, Object> params);

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
     * 定时自动取消
     *
     * @param aftersaleBarterList 售后换货列表
     * @author xuzhch
     * @date 2020年09月21日
     */
    void batchCancel(@Param("aftersaleBarterList") List<String> aftersaleBarterList);
}
