/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dao.goods.time;


import com.leimingtech.commons.mybatis.dao.BaseDao;
import com.leimingtech.modules.dto.goods.time.GoodsTimeDTO;
import com.leimingtech.modules.entity.goods.time.GoodsTimeEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Author: weixianchun
 * @Description: 定时上架商品
 * @Date :2019/6/4 17:55
 * @Version V1.0
 **/
@Mapper
public interface GoodsTimeDao extends BaseDao<GoodsTimeEntity> {

    /**
     * 根据规格id删除定时规格
     *
     * @param specIds 规格ids
     * @Author: weixianchun
     * @Description: 根据规格id删除定时规格
     * @Date :2019/6/13 21:02
     * @Version V1.0
     **/
    void deleteBySpecIds(@Param("ids") List<Long> specIds);

    /**
     * 根据商品id删除定时规格
     *
     * @param goodsIds 商品ids
     * @Author: weixianchun
     * @Description: 根据商品id删除定时规格
     * @Date :2019/6/13 21:02
     * @Version V1.0
     **/
    void deleteByGoodsId(@Param("ids") List<Long> goodsIds);

    /**
     * 根据goodsId查询定时表
     *
     * @param specId 规格id
     * @return 返回定时信息
     */
    GoodsTimeDTO selectByspecId(@Param("specId") Long specId);


}