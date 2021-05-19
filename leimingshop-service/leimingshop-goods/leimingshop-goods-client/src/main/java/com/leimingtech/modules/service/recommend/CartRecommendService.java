/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.service.recommend;


import com.leimingtech.commons.tools.page.PageData;
import com.leimingtech.modules.dto.goods.GoodsRecommendDTO;
import com.leimingtech.modules.dto.recommend.CartRecommendPageDTO;
import com.leimingtech.modules.dto.recommend.GoodsPageDTO;
import com.leimingtech.modules.dto.recommend.SaveCartRecommendDTO;
import com.leimingtech.modules.dto.recommend.UpdateCartRecommendDTO;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

/**
 * 购物车推荐商品表
 *
 * @author sunyanling
 * @since v1.0.0 2019-08-21
 */

public interface CartRecommendService {

    /**
     * 商品推荐分页
     *
     * @param params
     * @return
     */

    PageData<CartRecommendPageDTO> pageList(@RequestParam Map<String, Object> params);

    /**
     * 批量更新商品排序
     *
     * @param list
     */

    void updateSort(@RequestBody List<UpdateCartRecommendDTO> list);

    /**
     * 删除推荐商品
     *
     * @param ids
     */

    void delete(@RequestBody Long[] ids);

    /**
     * 保存推荐商品
     *
     * @param list
     */

    void save(@RequestBody List<SaveCartRecommendDTO> list);

    /**
     * 查询商品列表
     *
     * @param params 查询参数
     * @return 返回商品信息
     */

    PageData<GoodsPageDTO> goodsPage(@RequestParam Map<String, Object> params);

    /**
     * 查询购物车商品推荐
     *
     * @return 返回推荐商品
     */

    List<GoodsRecommendDTO> findList();

}