/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.service.goods.spec;


import com.leimingtech.commons.tools.page.PageData;
import com.leimingtech.modules.dto.spec.GoodsAllSpecDTO;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

/**
 * @Author: tyl
 * @Description:
 * @Date :2019/6/4 17:57
 * @Version V1.0
 **/

public interface GoodsAllSpecService {
    /**
     * 分页查询商品规格信息
     *
     * @param params 查询参数
     * @return
     */

    PageData<GoodsAllSpecDTO> findAllSpec(@RequestParam Map<String, Object> params);

}