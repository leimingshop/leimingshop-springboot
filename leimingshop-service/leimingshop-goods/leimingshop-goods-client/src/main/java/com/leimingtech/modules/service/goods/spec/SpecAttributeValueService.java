/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.service.goods.spec;


import com.leimingtech.commons.tools.page.PageData;
import com.leimingtech.modules.dto.goods.detail.SpecAttributePictureDetailDTO;
import com.leimingtech.modules.dto.goods.spec.SpecAttributeAndValueSaveDTO;
import com.leimingtech.modules.dto.goods.spec.SpecAttributeValueDTO;
import com.leimingtech.modules.dto.goods.spec.SpecAttributeValueSaveDTO;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

/**
 * 商品规格属性值
 *
 * @author xuzhch
 * @email x170518@163.com
 * @since 1.0.0 2019-06-04
 */

public interface SpecAttributeValueService {
    /**
     * 分页
     *
     * @param params
     * @return
     */

    PageData<SpecAttributeValueDTO> page(@RequestParam Map<String, Object> params);

    /**
     * 根据ID查询规格属性值
     *
     * @param id
     * @return
     */

    SpecAttributeValueDTO get(Long id);

    /**
     * 保存商品规格属性值
     *
     * @param dtoList
     */

    void save(@RequestBody List<SpecAttributeValueSaveDTO> dtoList);

    /**
     * 修改商品规格属性值
     *
     * @param dto
     */

    void update(@RequestBody SpecAttributeValueDTO dto);

    /**
     * 根据ID删除商品规格属性值
     *
     * @param ids
     */

    void delete(@RequestBody Long[] ids);

    /**
     * 导出商品规格属性值
     *
     * @param params
     * @return
     */

    List<SpecAttributeValueDTO> list(@RequestParam Map<String, Object> params);

    /**
     * 保存商品规格属性值
     *
     * @param dtoList
     */

    void saveBatch(@RequestBody List<SpecAttributeAndValueSaveDTO> dtoList);

    /**
     * 查询主规格对应的图片
     *
     * @param goodsId 商品id
     * @return 返回规格信息
     */

    List<SpecAttributePictureDetailDTO> getPicListByGoodsId(Long goodsId);

    /**
     * 修改商品规格属性值
     *
     * @param dto
     */

    void updateSpecAttrVal(@RequestBody SpecAttributeAndValueSaveDTO dto);

    /**
     * 功能描述 list 批量保存
     *
     * @param @param specAttributeValueSaveDTOList
     * @return void
     * @author lishuo
     * @date 30/6/2020
     */

    void saveByList(@RequestParam("specAttributeValueSaveDTOList") List<SpecAttributeValueSaveDTO> specAttributeValueSaveDTOList);

    /**
     * 根据商品ID，批量删除
     *
     * @param goodsIds 商品id数组
     * @author xuzhch
     * @date 2020年8月26日
     */

    void deleteBatchBygoodsIds(@RequestBody Long[] goodsIds);
}
