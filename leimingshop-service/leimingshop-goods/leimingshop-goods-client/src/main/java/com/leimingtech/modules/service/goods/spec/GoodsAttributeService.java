/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.service.goods.spec;


import com.leimingtech.commons.tools.page.PageData;
import com.leimingtech.modules.dto.goods.spec.GoodsAttributeDTO;
import com.leimingtech.modules.dto.goods.spec.GoodsAttributeInfoDTO;
import com.leimingtech.modules.dto.goods.spec.GoodsAttributeSaveDTO;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

/**
 * 商品属性表
 *
 * @author xuzhch
 * @email x170518@163.com
 * @since 1.0.0 2019-06-04
 */

public interface GoodsAttributeService {
    /**
     * 分页
     *
     * @param params
     * @return
     */

    PageData<GoodsAttributeDTO> page(@RequestParam Map<String, Object> params);

    /**
     * 根据ID查询商品属性
     *
     * @param goodsId
     * @return
     */

    List<GoodsAttributeInfoDTO> getByGoodsId(Long goodsId);

    /**
     * 保存商品属性
     *
     * @param dtoList
     */

    void save(@RequestBody List<GoodsAttributeSaveDTO> dtoList);

    /**
     * 修改商品属性
     *
     * @param dtoList
     */

    void update(@RequestBody List<GoodsAttributeSaveDTO> dtoList);

    /**
     * 根据ID删除商品属性
     *
     * @param ids
     */

    void delete(@RequestBody Long[] ids);

    /**
     * 导出商品属性
     *
     * @param params
     * @return
     */

    List<GoodsAttributeDTO> list(@RequestParam Map<String, Object> params);

    /**
     * 根据商品Id 删除属性信息
     *
     * @param goodsId
     */

    void deleteByGoodsId(Long goodsId);

    /**
     * 功能描述 根据goodsId 查找属性和属性值
     *
     * @param goodsId goodsId
     * @return java.util.List<com.leimingtech.modules.dto.goods.spec.GoodsAttributeDTO>
     * @author lishuo
     * @date 27/6/2020
     */

    List<GoodsAttributeDTO> findAttributeByGoodsId(@RequestParam("goodsId") Long goodsId);

    /**
     * 功能描述 批量保存
     *
     * @param * @param attributeSaveDTOList
     * @return void
     * @author lishuo
     * @date 22/7/2020
     */

    void saveBatch(@RequestParam("attributeSaveDTOList") List<GoodsAttributeSaveDTO> attributeSaveDTOList);

    /**
     * 根据商品id批量删除信息
     *
     * @param goodsIds 商品id
     */

    void deleteBatchBygoodsIds(@RequestBody Long[] goodsIds);
}
