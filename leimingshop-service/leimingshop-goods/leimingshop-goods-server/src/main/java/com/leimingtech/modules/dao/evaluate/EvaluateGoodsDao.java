/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dao.evaluate;

import com.leimingtech.commons.mybatis.dao.BaseDao;
import com.leimingtech.modules.dto.evaluate.EvaluateGoodsDTO;
import com.leimingtech.modules.dto.evaluate.FindEvaluateGoodsDTO;
import com.leimingtech.modules.dto.evaluate.H5EvaluateGoodsDTO;
import com.leimingtech.modules.entity.evaluate.EvaluateGoodsEntity;
import com.leimingtech.modules.vo.evaluate.EvaluateGoodsInfoVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 商品评价管理
 *
 * @author chengqian
 * @email 2634443725@gmail.com
 * @since 7.0 2019-05-15
 */
@Mapper
public interface EvaluateGoodsDao extends BaseDao<EvaluateGoodsEntity> {
    /**
     * f分页查询当前用户已评价的商品
     *
     * @param params
     * @return
     */
    List<FindEvaluateGoodsDTO> findPage(Map<String, Object> params);

    /**
     * 根据id获取信息
     *
     * @param id           评价Id 或者 订单id
     * @param orderGoodsId 订单商品id
     * @return 返回评价详情
     */
    EvaluateGoodsDTO findById(@Param("id") Long id, @Param("orderGoodsId") Long orderGoodsId);

    /***
     * 计算出好评率
     * @param goodsId 商品id
     *
     * @return 返回好评率
     */
    String reputably(Long goodsId);

    /**
     * 查询各种评价数量
     *
     * @param params
     * @return
     */
    H5EvaluateGoodsDTO selectEvaCount(Map<String, Object> params);

    /**
     * 根据订单查询评价详情
     *
     * @param orderId
     * @return
     */
    List<EvaluateGoodsInfoVO> pcEvaInfo(Long orderId);

    /**
     * 根据商品ID 查询当前商品的评价数量
     *
     * @param goodsId
     * @return
     */
    Integer findEvaluateNum(Long goodsId);

    /**
     * 违规删除
     *
     * @param id
     */
    void illegalDel(@Param("id") Long[] id);

}
