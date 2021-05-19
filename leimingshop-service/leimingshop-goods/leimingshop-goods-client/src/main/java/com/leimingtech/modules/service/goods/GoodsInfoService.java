/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.service.goods;


import com.leimingtech.commons.tools.page.PageData;
import com.leimingtech.modules.dto.goods.GoodsInfoDTO;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

/**
 * 商品附加信息表
 *
 * @author dy
 * @email 1197793912@qq.com
 * @since 1.0.0 2019-06-04
 */

public interface GoodsInfoService {
    /**
     * 分页查询
     *
     * @param params
     * @return
     */

    PageData<GoodsInfoDTO> page(@RequestParam Map<String, Object> params);

    /**
     * 列表
     *
     * @param params
     * @return
     */

    List<GoodsInfoDTO> list(@RequestParam Map<String, Object> params);

    /**
     * 查询详情
     *
     * @param id
     * @return
     */

    GoodsInfoDTO get(Long id);

    /**
     * 保存
     *
     * @param dto
     */

    void save(@RequestBody GoodsInfoDTO dto);

    /**
     * 修改用户基本信息
     *
     * @param dto
     */

    void update(@RequestBody GoodsInfoDTO dto);

    /**
     * 删除用户
     *
     * @param ids
     */

    void delete(@RequestBody Long[] ids);

    /**
     * 根据商品id获取商品详情
     *
     * @param goodsId
     * @return
     */

    GoodsInfoDTO getGoodsInfo(Long goodsId);

    /**
     * 修改商品售出数量
     *
     * @param goodsInfoDTOList
     * @return 返回修改成功数量
     */

    Integer updateList(List<GoodsInfoDTO> goodsInfoDTOList);

    /**
     * 修改评论次数
     *
     * @param goodsId
     */

    void updateEvalusteNum(@RequestParam("goodsId") Long goodsId);

    /**
     * 功能描述 批量保存
     *
     * @param * @param goodsInfoDTOList
     * @return void
     * @author lishuo
     * @date 15/7/2020
     */

    void saveBatch(@RequestParam("goodsInfoDTOList") List<GoodsInfoDTO> goodsInfoDTOList);

    /**
     * 功能描述 查询销量排行前二十
     *
     * @author lishuo
     * @date 2020/7/31 15:54
     * @return: java.util.List<java.lang.String>
     **/

    List<String> findTopSaleId();

    /**
     * 根据商品ID，批量删除
     *
     * @param goodsIds 商品id数组
     * @author xuzhch
     * @date 2020年8月26日
     */

    void deleteBatchBygoodsIds(@RequestBody Long[] goodsIds);
}
