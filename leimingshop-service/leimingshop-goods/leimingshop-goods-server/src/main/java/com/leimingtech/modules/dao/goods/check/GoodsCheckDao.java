/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dao.goods.check;

import com.leimingtech.commons.mybatis.dao.BaseDao;
import com.leimingtech.modules.dto.goods.check.GoodsCheckDTO;
import com.leimingtech.modules.entity.goods.check.GoodsCheckEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 商品审核表
 *
 * @author dy
 * @email 1197793912@qq.com
 * @since 1.0.0 2019-06-13
 */
@Mapper
public interface GoodsCheckDao extends BaseDao<GoodsCheckEntity> {
    /**
     * 功能描述 批量保存 foreach
     *
     * @param * @param goodsCheckDTOList
     * @return void
     * @author lishuo
     * @date 24/7/2020
     */
    void insertBatch(@Param("goodsCheckDTOList") List<GoodsCheckDTO> goodsCheckDTOList);
}