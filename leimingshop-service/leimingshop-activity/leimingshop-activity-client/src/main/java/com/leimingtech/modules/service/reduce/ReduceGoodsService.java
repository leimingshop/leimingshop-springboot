/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.service.reduce;


import com.leimingtech.commons.tools.page.PageData;
import com.leimingtech.modules.dto.reduce.ReduceGoodsDTO;

import java.util.List;
import java.util.Map;

/**
 * 满减活动商品管理
 *
 * @author 刘远杰 2634443725@qq.com
 * @since v1.0.0 2019-12-26
 */

public interface ReduceGoodsService {


    PageData<ReduceGoodsDTO> page(Map<String, Object> params);


    List<ReduceGoodsDTO> list(Map<String, Object> params);


    ReduceGoodsDTO get(Long id);


    void save(ReduceGoodsDTO dto);


    void update(ReduceGoodsDTO dto);


    void delete(Long[] ids);

    /**
     * @Author weixianchun
     * @Description 批量保存满减商品关联信息
     * @Param dtoList
     * @Date 2019/12/26 18:45
     * @Return java.lang.Boolean
     * @version 1.0
     */

    Boolean saveBatch(List<ReduceGoodsDTO> dtoList);

    /**
     * @Author weixianchun
     * @Description 根据活动ID删除商品关联信息
     * @Param activityId
     * @Date 2019/12/26 14:11
     * @Return void
     * @version 1.0
     */

    void deleteByActivityId(Long activityId);

    /**
     * 功能描述:
     * 〈获取满减活动关联的商品集合〉
     *
     * @param activityId 满减活动id
     * @author : 刘远杰
     */

    List<ReduceGoodsDTO> getByActivityId(Long activityId);

}
