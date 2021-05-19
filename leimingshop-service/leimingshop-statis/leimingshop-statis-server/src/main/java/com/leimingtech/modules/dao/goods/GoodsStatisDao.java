/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dao.goods;

import com.leimingtech.commons.mybatis.dao.BaseDao;
import com.leimingtech.modules.dto.goods.GoodsClassNameDTO;
import com.leimingtech.modules.dto.goods.GoodsStatisDTO;
import com.leimingtech.modules.entity.goods.GoodsPicEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.poi.ss.formula.functions.T;

import java.util.List;

/**
 * 商品统计DB查询接口
 *
 * @author xuzhch
 * @date 2020年9月17日
 */
@Mapper
public interface GoodsStatisDao extends BaseDao<T> {
    /**
     * Select class name list.
     *
     * @param []
     * @return java.util.List<com.leimingtech.modules.dto.goods.GoodsClassNameDTO> list
     * @author xuzhch
     * @date 2020 /9/17/017
     */
    List<GoodsClassNameDTO> selectClassName();


    /**
     * Goods statis sales list.
     *
     * @param date the date
     * @return the list
     * @author xuzhch
     */
    List<GoodsStatisDTO> goodsStatisSales(@Param("date") String date);

    /**
     * Select goods main pic list.
     *
     * @param goodsIds the goods ids
     * @return the list
     */
    List<GoodsPicEntity> selectGoodsMainPic(@Param("goodsIds") List<Long> goodsIds);
}
