/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.service.goodsclass;


import com.leimingtech.modules.dto.goodsclass.GoodsClassSpecDTO;
import com.leimingtech.modules.dto.goodsclass.GoodsClassUpdateDTO;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

/**
 * 商品分类和规格关联
 *
 * @author xuzhch
 * @email x170518@163.com
 * @since 1.0.0 2019-06-03
 */


public interface GoodsClassSpecService {
    /**
     * 批量保存商品分类关联属性
     *
     * @param dto
     */

    void insertBatch(@RequestBody GoodsClassUpdateDTO dto);

    /**
     * 根据分类ID查询关联规格
     *
     * @param params
     * @return
     */

    List<GoodsClassSpecDTO> list(@RequestParam Map<String, Object> params);

    /**
     * 修改商品分类关联规格
     *
     * @param dto
     */

    void updateGoodsClassSpec(@RequestBody GoodsClassUpdateDTO dto);

//    void deleteByGcClassId(Long[] dto);

    /**
     * 根据分类id查询关联数量
     *
     * @param gcId
     * @return
     */

    Integer findSpecByGcId(Long gcId);
}