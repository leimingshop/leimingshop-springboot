/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.aftersale.dao;

import com.leimingtech.commons.mybatis.dao.BaseDao;
import com.leimingtech.modules.aftersale.dto.AftersaleRefundRecordsDTO;
import com.leimingtech.modules.aftersale.entity.AftersaleRefundRecordsEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 售后退款日志
 *
 * @author: SWH ab4856812@163.com
 * @date: 2019/6/24 9:16
 */
@Mapper
public interface AftersaleRefundRecordsDao extends BaseDao<AftersaleRefundRecordsEntity> {

    /**
     * 查询退款失败的售后记录信息列表
     *
     * @return list 售后退款记录列表
     * @author xuzhch
     * @date 2020年09月21日
     */
    List<AftersaleRefundRecordsDTO> findPayFail();
}
