/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.service.labelrecommend;


import com.leimingtech.modules.dto.labelrecommend.LabelRecommendRelDTO;
import com.leimingtech.modules.dto.labelrecommend.LabelRecommendRelSaveDTO;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

/**
 * 标签推荐关联表
 *
 * @author weixianchun weixianchun@leimingtech.com
 * @since v1.0.0 2020-01-09
 */

public interface LabelRecommendRelService {

    /**
     * 查询所有标签关联信息
     *
     * @param params 查询参数
     * @return 返回标签推荐关联信息
     */

    List<LabelRecommendRelDTO> list(@RequestParam Map<String, Object> params);

    /**
     * 根据id获取 推荐标签详情
     *
     * @param id 主键id
     * @return 返回推荐标签关联信息
     */

    LabelRecommendRelDTO get(Long id);

    /**
     * 保存推荐标签
     *
     * @param dto 保存参数
     */

    void save(@RequestBody LabelRecommendRelDTO dto);

    /**
     * 修改推荐标签关联信息
     *
     * @param dto 修改参数
     */

    void update(@RequestBody LabelRecommendRelDTO dto);

    /**
     * 删除推荐标签
     *
     * @param ids 主键id
     */

    void delete(@RequestBody Long[] ids);

    /**
     * 功能描述:
     * <根据商品id删除标签关联的商品信息>
     *
     * @param goodsId 商品id
     * @return 根据数量判断是否删除成功
     * @date 2020/1/10 10:25
     * @author weixianchun
     **/

    int deleteByGoodsId(@RequestParam("goodsId") Long goodsId);

    /**
     * 功能描述:
     * <批量保存数据>
     *
     * @param dtoList
     * @return 判断是否添加成功
     * @date 2020/1/10 18:34
     * @author weixianchun
     **/

    boolean insertBatch(@RequestBody List<LabelRecommendRelSaveDTO> dtoList);
}
