/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.service.goodsclass;


import com.leimingtech.modules.dto.goodsclass.GoodsClassBrandDTO;
import com.leimingtech.modules.dto.goodsclass.GoodsClassBrandSaveDTO;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

/**
 * 商品分类和品牌关联表
 *
 * @author xuzhch x170518@163.com
 * @since v1.0.0 2019-06-25
 */

public interface GoodsClassBrandService {

    /**
     * 查询信息
     *
     * @param params 条件查询
     * @return 返回分类品牌信息
     */

    List<GoodsClassBrandDTO> list(@RequestParam Map<String, Object> params);

    /**
     * 根据id查询详情
     *
     * @param id 主键id
     * @return 返回详情
     */

    GoodsClassBrandDTO get(String id);

    /**
     * 批量保存信息
     *
     * @param dto          保存参数
     * @param goodsClassId 商品分类id
     */

    void saveBatch(@RequestBody List<GoodsClassBrandSaveDTO> dto,
                   @RequestParam("goodsClassId") Long goodsClassId);

    /**
     * 删除
     *
     * @param ids 主键id
     */

    void delete(@RequestBody String[] ids);

    /**
     * 根据分类id查询数量
     *
     * @param gcId 分类id
     * @return 返回数量
     */

    Integer findBrandByGcId(Long gcId);

}