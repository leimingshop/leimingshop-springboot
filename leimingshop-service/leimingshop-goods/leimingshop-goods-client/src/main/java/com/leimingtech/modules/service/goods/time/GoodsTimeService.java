/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.service.goods.time;


import com.leimingtech.commons.tools.page.PageData;
import com.leimingtech.modules.dto.goods.time.GoodsTimeDTO;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @Author: weixianchun
 * @Description: 定时上架商品
 * @Date :2019/6/4 18:42
 * @Version V1.0
 **/

public interface GoodsTimeService {

    /**
     * 分页
     *
     * @param params 可变参数
     * @return 返回定时上架的商品
     * @Author: weixianchun
     * @Description:
     * @Date :2019/5/28 19:51
     * @Version V1.0
     **/

    PageData<GoodsTimeDTO> page(@RequestParam Map<String, Object> params);

    /**
     * 、
     * 查询所有定时上架商品
     *
     * @param params 查询参数
     * @return 返回定时上架的商品
     * @Author: weixianchun
     * @Description: 查询所有定时上架商品
     * @Date :2019/5/28 19:51
     * @Version V1.0
     **/

    List<GoodsTimeDTO> list(@RequestParam Map<String, Object> params);

    /**
     * 根据定时上架商品id查询信息
     *
     * @param id 定时上架商品id
     * @return 返回定时上架的商品
     * @Author: weixianchun
     * @Description: 根据定时上架商品id查询信息
     * @Date :2019/5/28 19:52
     * @Version V1.0
     **/

    GoodsTimeDTO get(Long id);

    /**
     * 保存定时上架商品
     *
     * @param dto 实体
     * @Author: weixianchun
     * @Description: 保存定时上架商品
     * @Date :2019/5/28 19:52
     * @Version V1.0
     **/

    void save(@RequestBody GoodsTimeDTO dto);

    /**
     * 修改定时上架商品
     *
     * @param dto 实体
     * @Author: weixianchun
     * @Description: 修改定时上架商品
     * @Date :2019/5/28 19:52
     * @Version V1.0
     **/

    void update(@RequestBody GoodsTimeDTO dto);

    /**
     * 根据定时上架商品id删除定时上架商品
     *
     * @param ids 定时上架商品id
     * @Author: weixianchun
     * @Description: 根据定时上架商品id删除定时上架商品
     * @Date :2019/5/28 19:53
     * @Version V1.0
     **/

    void delete(@RequestBody Long[] ids);

    /**
     * 根据规格id删除定时规格
     *
     * @param specIds 规格id
     * @Author: weixianchun
     * @Description: 根据规格id删除定时规格
     * @Date :2019/6/13 21:02
     * @Version V1.0
     **/

    void deleteBySpecIds(@RequestBody List<Long> specIds);

    /**
     * 根据商品id删除定时规格
     *
     * @param goodsIds 商品id
     * @Author: weixianchun
     * @Description: 根据商品id删除定时规格
     * @Date :2019/6/13 21:02
     * @Version V1.0
     **/

    void deleteByGoodsId(@RequestBody List<Long> goodsIds);

    /**
     * 未删除定时任务
     *
     * @param nowDate 现在时间
     * @param type    类型
     * @return 返回定时上架的商品
     */

    List<GoodsTimeDTO> selectList(@RequestParam("nowDate") Date nowDate, @RequestParam("type") Integer type);

    /**
     * 根据商品id查询定时任务
     *
     * @param goodsId 商品id
     * @return 返回定时上架的商品
     */

    GoodsTimeDTO selectByGoodsId(Long goodsId);

    /**
     * 根据规格id查询定时任务
     *
     * @param specId 规格id
     * @return 返回定时上架商品信息
     */

    GoodsTimeDTO selectBySpecId(Long specId);

    /**
     * 删除商品定时上下架
     *
     * @param specId 规格id
     */

    void deleteBySpecId(Long specId);

    /**
     * 修改上下架状态与实践
     *
     * @param goodsTimeDTO: 封装实体
     * @date 2020/1/6 11:56
     * @author lixiangx@leimingtech.com
     **/

    void updateStatusAndTime(@RequestBody GoodsTimeDTO goodsTimeDTO);
}