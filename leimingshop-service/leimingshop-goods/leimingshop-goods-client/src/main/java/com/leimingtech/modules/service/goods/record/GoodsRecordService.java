/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.service.goods.record;


import com.leimingtech.commons.tools.page.PageData;
import com.leimingtech.modules.dto.goods.record.GoodsRecordDTO;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

/**
 * @Author: weixianchun
 * @Description: 商品上下架状态记录
 * @Date :2019/6/4 18:28
 * @Version V1.0
 **/

public interface GoodsRecordService {

    /**
     * 分页查询上下架记录
     *
     * @param params 查询参数
     * @return 返回商品上下架记录
     * @Author: weixianchun
     * @Description: 分页
     * @Date :2019/5/28 19:51
     * @Version V1.0
     **/

    PageData<GoodsRecordDTO> page(@RequestParam Map<String, Object> params);

    /**
     * 查询所有商品上下架状态记录
     *
     * @param params 查询参数
     * @return 返回商品上下架记录信息
     * @Author: weixianchun
     * @Description: 查询所有商品上下架状态记录
     * @Date :2019/5/28 19:51
     * @Version V1.0
     **/

    List<GoodsRecordDTO> list(@RequestParam Map<String, Object> params);

    /**
     * 根据商品上下架状态记录id查询
     *
     * @param id 商品上下架状态记录id
     * @return 返回商品上下架记录
     * @Author: weixianchun
     * @Description: 根据商品上下架状态记录id查询
     * @Date :2019/5/28 19:52
     * @Version V1.0
     **/

    GoodsRecordDTO get(Long id);

    /**
     * 保存商品上下架状态记录
     *
     * @param dto 保存参数
     * @Author: weixianchun
     * @Description: 保存商品上下架状态记录
     * @Date :2019/5/28 19:52
     * @Version V1.0
     **/

    void save(@RequestBody GoodsRecordDTO dto);

    /**
     * 修改商品上下架状态记录
     *
     * @param dto 实体
     * @Author: weixianchun
     * @Description: 修改商品上下架状态记录
     * @Date :2019/5/28 19:52
     * @Version V1.0
     **/

    void update(@RequestBody GoodsRecordDTO dto);

    /**
     * 根据商品上下架状态记录id删除
     *
     * @param ids 商品上下架状态记录id
     * @Author: weixianchun
     * @Description: 根据商品上下架状态记录id删除
     * @Date :2019/5/28 19:53
     * @Version V1.0
     **/

    void delete(@RequestBody Long[] ids);

}