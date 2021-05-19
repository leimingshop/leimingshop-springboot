/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.service.goods.spec;


import com.leimingtech.commons.tools.page.PageData;
import com.leimingtech.modules.dto.goods.detail.SpecAttributeDetailDTO;
import com.leimingtech.modules.dto.goods.price.SpecAttrNameDTO;
import com.leimingtech.modules.dto.goods.spec.SpecAttributeAndValueSaveDTO;
import com.leimingtech.modules.dto.goods.spec.SpecAttributeDTO;
import com.leimingtech.modules.dto.goods.spec.SpecAttributeSaveDTO;
import com.leimingtech.modules.dto.goods.spec.SpecAttributeUpdateDTO;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

/**
 * 商品规格属性
 *
 * @author xuzhch
 * @email x170518@163.com
 * @since 1.0.0 2019-06-04
 */

public interface SpecAttributeService {
    /**
     * 分页
     *
     * @param params
     * @return
     */

    PageData<SpecAttributeDTO> page(@RequestParam Map<String, Object> params);

    /**
     * 根据ID查询商品规格属性
     *
     * @param id
     * @return
     */

    SpecAttributeDTO get(Long id);

    /**
     * 根据ID查询商品规格属性
     *
     * @param goodsId
     * @return
     */

    List<SpecAttributeDetailDTO> getByGoodsId(Long goodsId);

    /**
     * 保存商品规格属性
     *
     * @param dtoList
     */

    void save(@RequestBody List<SpecAttributeSaveDTO> dtoList);

    /**
     * 保存商品规格属性
     *
     * @param dtoList
     */

    void saveBatch(@RequestBody List<SpecAttributeAndValueSaveDTO> dtoList);

    /**
     * 修改商品规格属性
     *
     * @param dtoList
     */

    void updateSpecAttrVal(@RequestBody List<SpecAttributeAndValueSaveDTO> dtoList);

    /**
     * 修改商品规格属性
     *
     * @param dtoList
     */

    void update(@RequestBody List<SpecAttributeUpdateDTO> dtoList);

    /**
     * 根据ID删除商品规格属性
     *
     * @param ids
     */

    void delete(@RequestBody Long[] ids);

    /**
     * 导出商品规格属性
     *
     * @param params
     * @return
     */

    List<SpecAttributeDTO> list(@RequestParam Map<String, Object> params);

    /**
     * 查询商品规格属性名集合
     *
     * @param goodsId
     * @return
     */

    List<SpecAttrNameDTO> getNameListByGoodsId(Long goodsId);

    /**
     * 功能描述 根据goodsId和specValue 查找goods选中的specAttr
     *
     * @param goodsId
     * @param specValues
     * @return com.leimingtech.modules.dto.goods.spec.SpecAttributeDTO
     * @author lishuo
     * @date 28/6/2020
     */

    List<SpecAttributeDTO> findSpecNameByGoodsId(@RequestParam("goodsId") Long goodsId, @RequestParam("specValues") List<String> specValues);

    /**
     * 功能描述 批量保存 手写sql
     *
     * @param specAttributeSaveTotalDTO
     * @return void
     * @author lishuo
     * @date 22/7/2020
     */

    void insertBatch(@RequestParam("specAttributeSaveTotalDTO") List<SpecAttributeSaveDTO> specAttributeSaveTotalDTO);

    /**
     * 根据商品ID，批量删除
     *
     * @param goodsIds 商品id数组
     * @author xuzhch
     * @date 2020年8月26日
     */

    void deleteBatchBygoodsIds(@RequestBody Long[] goodsIds);
}
