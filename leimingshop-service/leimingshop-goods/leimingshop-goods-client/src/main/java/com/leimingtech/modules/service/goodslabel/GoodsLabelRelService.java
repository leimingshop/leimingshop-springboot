/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.service.goodslabel;


import com.leimingtech.modules.dto.goods.GoodsDTO;
import com.leimingtech.modules.dto.goodslable.GoodsLabelRelDTO;
import com.leimingtech.modules.dto.goodslable.GoodsLabelRelSaveDTO;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

/**
 * 商品与标签关联表
 *
 * @author weixianchun weixianchun@leimingtech.com
 * @since v1.0.0 2019-12-13
 */

public interface GoodsLabelRelService {

    /**
     * 关联列表查询
     *
     * @param params 查询条件
     * @return 返回商品标签信息
     * @Author weixianchun
     * @Description 关联列表查询
     * @Date 2019/12/17 10:40
     * @version 1.0
     */

    List<GoodsLabelRelDTO> list(@RequestParam Map<String, Object> params);

    /**
     * 查询标签关联的商品数量, 店铺数量
     *
     * @param goodsId 商品id
     * @param labelId 标签id
     * @return 返回标签关联的商品数量, 店铺数量
     * @Author weixianchun
     * @Description 查询标签关联的商品数量, 店铺数量
     * @Date 2019/12/13 17:11
     * @version 1.0
     */

    GoodsLabelRelDTO findNumGoodsAndStore(@RequestParam("goodsId") Long goodsId, @RequestParam("labelId") Long labelId);

    /**
     * 批量保存商品标签关联信息
     *
     * @param dtoList 保存参数
     * @return 返回是否保存成功
     * @Author weixianchun
     * @Description 批量保存商品标签关联信息
     * @Date 2019/12/11 9:26
     * @version 1.0
     */

    boolean insertBatch(@RequestBody List<GoodsLabelRelSaveDTO> dtoList);

    /**
     * 根据商品ID删除数据
     *
     * @param goodsId 商品id
     * @return 返回商品删除数量
     * @Author weixianchun
     * @Description 根据商品ID删除数据
     * @Date 2019/12/12 10:43
     * @version 1.0
     */

    int deleteByGoodsId(Long goodsId);

    /**
     * 根据标签ID查询商品列表
     *
     * @param labelId 标签id
     * @return 返回商品信息
     * @Author weixianchun
     * @Description 根据标签ID查询商品列表
     * @Date 2019/12/17 16:56
     * @version 1.0
     */

    List<GoodsDTO> findGoodsByLabelId(@RequestParam(("labelId")) Long labelId);

    /**
     * 根据商品Id 查找标签名称
     *
     * @param goodsId 商品id
     * @return 返回标签名称
     */

    String selectLabelNameByGoodsId(Long goodsId);

    /**
     * 功能描述 批量保存 手写sql
     *
     * @param goodsLabelRelSaveDTOList 保存参数
     * @author lishuo
     * @date 22/7/2020
     */

    void saveBatch(@RequestBody List<GoodsLabelRelSaveDTO> goodsLabelRelSaveDTOList);

    /**
     * 根据商品ID，批量删除
     *
     * @param goodsIds 商品id数组
     * @author xuzhch
     * @date 2020年8月26日
     */

    void deleteBatchBygoodsIds(@RequestBody Long[] goodsIds);

    /**
     * 根据标签id批量删除信息
     *
     * @param ids 主键id
     */

    void deleteBatchByLabelIds(@RequestBody Long[] ids);
}
