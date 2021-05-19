/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.service.goods.spec;


import com.leimingtech.commons.tools.page.PageData;
import com.leimingtech.modules.dto.spec.GoodsSpecAttrValurAttrDTO;
import com.leimingtech.modules.dto.spec.GoodsSpecListAttrValurDTO;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

/**
 * @Author: weixianchun
 * @Description: 商品规格管理
 * @Date :2019/6/4 17:57
 * @Version V1.0
 **/

public interface GoodsSpecAttrValurAttrService {


    /**
     * 查询规格属性关联信息
     *
     * @param params 查询参数
     * @return 返回规格属性信息
     */

    PageData<GoodsSpecAttrValurAttrDTO> findListAttrRel(@RequestParam Map<String, Object> params);

    /**
     * 查询规格 信息
     *
     * @param params 查询条件
     * @return
     */

    PageData<GoodsSpecListAttrValurDTO> findListSpecAttrRel(@RequestParam Map<String, Object> params);

}