/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dao.complain;

import com.leimingtech.commons.mybatis.dao.BaseDao;
import com.leimingtech.modules.dto.complain.OrderComplainDetailDTO;
import com.leimingtech.modules.dto.complain.OrderComplainGoodsDetail;
import com.leimingtech.modules.dto.complain.OrderComplainPageDTO;
import com.leimingtech.modules.entity.complain.OrderComplainEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 订单投诉表
 *
 * @author chengqian chengqian@leimingtech.com
 * @since v1.0.0 2020-04-08
 */
@Mapper
public interface OrderComplainDao extends BaseDao<OrderComplainEntity> {
    /**
     * 查询H5投诉分页
     *
     * @param params
     * @return
     */
    List<OrderComplainPageDTO> h5Page(Map<String, Object> params);

    /**
     * 查询订单商品信息
     */
    List<OrderComplainGoodsDetail> orderDetail(Long orderId);

    /**
     * 用户删除投诉
     *
     * @param ids
     */
    void updateBatch(@Param("ids") Long[] ids);

    /**
     * admin端投诉详情
     *
     * @param id
     * @return
     */
    OrderComplainDetailDTO info(Long id);

    /***
     * 导出查询
     * @param params
     * @return
     */
    List<OrderComplainDetailDTO> excel(Map<String, Object> params);
}
