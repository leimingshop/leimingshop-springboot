/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.service.evaluate;


import com.leimingtech.commons.tools.page.PageData;
import com.leimingtech.modules.dto.evaluate.EvaluateGoodsDTO;
import com.leimingtech.modules.dto.evaluate.FindEvaluateGoodsDTO;
import com.leimingtech.modules.dto.evaluate.H5EvaluateGoodsDTO;
import com.leimingtech.modules.dto.evaluate.UpdateEvaluateReadStateDTO;
import com.leimingtech.modules.vo.evaluate.EvaluateGoodsInfoVO;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

/**
 * 商品评价管理
 *
 * @author chengqian
 * @email 2634443725@gmail.com
 * @since 7.0 2019-05-15
 */

public interface EvaluateGoodsService {
    /**
     * 分页查询当前店铺商品评价
     *
     * @param params 分页参数
     * @return
     */

    PageData<EvaluateGoodsDTO> storePage(@RequestParam Map<String, Object> params);

    /**
     * 保存商品评价 （导入版）
     *
     * @param list 保存参数
     */

    void saveEvaluate(@RequestBody List<EvaluateGoodsDTO> list);

    /**
     * 新增商品评价
     *
     * @param evaluateGoodsDTO 保存参数
     * @return 返回评价id
     */

    Long saveEvaluateGoods(@RequestBody EvaluateGoodsDTO evaluateGoodsDTO);

    /**
     * 分页查询商品评价
     *
     * @param params 商品评价实体
     * @return
     */


    PageData<EvaluateGoodsDTO> page(@RequestParam Map<String, Object> params);

    /**
     * 删除商品评价
     *
     * @param ids 商品评价ID
     */

    void delete(@RequestBody Long[] ids);

    /**
     * 根据ID获取信息
     *
     * @param id           评价Id 或者 订单id
     * @param orderGoodsId 订单商品id
     * @return 返回评价详情
     */

    EvaluateGoodsDTO findById(@PathVariable(value = "id", required = false) Long id,
                              @RequestParam(value = "orderGoodsId", required = false) Long orderGoodsId);

    /**
     * 修改信息
     *
     * @param dto 实体
     */

    void update(@RequestBody EvaluateGoodsDTO dto);

    /**
     * 批量显示或者隐藏信息
     *
     * @param ids   主键ID
     * @param state 状态  0 显示   1 隐藏
     */

    void batchUpdate(@RequestBody Long[] ids, @RequestParam("state") int state);

    /**
     * 分页查询当前用户已评价的商品（front）
     *
     * @param params 查询参数
     * @return
     */

    PageData<FindEvaluateGoodsDTO> evaluatePage(@RequestParam Map<String, Object> params);

    /**
     * 计算出好评率
     *
     * @param goodsId
     * @return
     */

    String reputably(@RequestParam("goodsId") Long goodsId);

    /**
     * 订单已完成，自动评价定时任务
     */

    void saveEvaluateTime();

    /**
     * 查询评价信息
     *
     * @param params 查询条件
     * @return 返回评价信息
     */

    List<EvaluateGoodsDTO> list(@RequestParam Map<String, Object> params);

    /**
     * 功能描述:
     * <批量修改读取状态或者违规删除状态>
     *
     * @param evaluateReadStateDTO 修改参数
     * @return 返回是否是修改成功
     * @date 2020/3/16 17:56
     * @author weixianchun
     **/

    boolean updateState(@RequestBody UpdateEvaluateReadStateDTO evaluateReadStateDTO);

    /**
     * pc端评价列表
     *
     * @param params
     * @return
     */

    H5EvaluateGoodsDTO pcEvaluatePage(@RequestParam Map<String, Object> params);

    /**
     * 根据订单id查询已评价信息
     *
     * @param orderId
     * @return
     */

    List<EvaluateGoodsInfoVO> pcEvaInfo(@RequestParam("orderId") Long orderId);

    /**
     * 根据goodsid查询当前商品的评价数量
     *
     * @param goodsId
     * @return
     */

    Integer findEvaluateNum(@RequestParam("goodsId") Long goodsId);

}
