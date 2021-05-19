/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.service.goods.spec;


import com.leimingtech.commons.tools.page.PageData;
import com.leimingtech.modules.dto.goods.spec.SpecAttributeRelationDTO;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

/**
 * 商品规格属性与属性值关联
 *
 * @author xuzhch
 * @email x170518@163.com
 * @since 1.0.0 2019-06-04
 */

public interface SpecAttributeRelationService {
    /**
     * 分页
     *
     * @param params
     * @return
     */

    PageData<SpecAttributeRelationDTO> page(@RequestParam Map<String, Object> params);

    /**
     * 根据ID查询商品属性
     *
     * @param id
     * @return
     */

    SpecAttributeRelationDTO get(Long id);

    /**
     * 保存商品属性
     *
     * @param dtoList
     */

    void save(@RequestBody List<SpecAttributeRelationDTO> dtoList);

    /**
     * 修改商品属性
     *
     * @param dto
     */

    void update(@RequestBody SpecAttributeRelationDTO dto);

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

    List<SpecAttributeRelationDTO> list(@RequestParam Map<String, Object> params);

    /**
     * 查询规格属性信息
     *
     * @param ids 主键id
     * @return 查询规格属性信息
     */

    List<SpecAttributeRelationDTO> selectSpecIdBySpecAttrValueId(@RequestBody Long[] ids);

    /**
     * 根据属性值判断是否为原有属性
     *
     * @param specValueIdList
     * @param num
     * @return
     */

    Long selectSpecId(@RequestBody List<Long> specValueIdList, @RequestParam("num") Integer num);

    /**
     * 根据规格属性id查询是否存在
     *
     * @param specAttributeModifyId
     * @return
     */

    boolean selectBySpecAttrId(Long specAttributeModifyId);

    /**
     * 根据规格id删除中间表
     *
     * @param specIds
     */

    void deleteBySpecId(@RequestBody Long[] specIds);

    /**
     * 根据商品id删除
     *
     * @param goodsId
     */

    void deleteByGoodsId(Long goodsId);

    /**
     * 获取属性数量
     *
     * @param goodsId 商品id
     * @return 返回数量
     */

    Integer selectCountBygoodsId(@RequestParam("goodsId") Long goodsId);

    /**
     * 保存信息
     *
     * @param specAttributeRelationDTOList 保存参数
     */

    void saveBatch(@RequestParam("specAttributeRelationDTOS") List<SpecAttributeRelationDTO> specAttributeRelationDTOList);

    /**
     * 根据商品ID，批量删除
     *
     * @param goodsIds 商品id数组
     * @author xuzhch
     * @date 2020年8月26日
     */

    void deleteBatchBygoodsIds(@RequestBody Long[] goodsIds);
}
