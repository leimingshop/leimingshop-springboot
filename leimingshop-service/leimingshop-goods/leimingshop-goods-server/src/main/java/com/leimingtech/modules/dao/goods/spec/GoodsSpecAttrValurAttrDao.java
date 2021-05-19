/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dao.goods.spec;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.leimingtech.commons.mybatis.dao.BaseDao;
import com.leimingtech.modules.dto.spec.GoodsAllSpecDTO;
import com.leimingtech.modules.dto.spec.GoodsSpecAttrValurAttrDTO;
import com.leimingtech.modules.dto.spec.GoodsSpecListAttrValurDTO;
import com.leimingtech.modules.entity.goods.spec.GoodsSpecEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @Author: tyl
 * @Description: 商品规格查询
 * @Date :2019/6/4 17:54
 * @Version V1.0
 **/
@Mapper
public interface GoodsSpecAttrValurAttrDao extends BaseDao<GoodsSpecEntity> {

    /**
     * 查询属性关联信息
     *
     * @param page 分页信息
     * @return
     */
    List<GoodsSpecAttrValurAttrDTO> findListAttrRel(IPage page);


    /**
     * 查询规格信息
     *
     * @param page 分頁信息
     * @return 返回规格信息
     */
    List<GoodsSpecListAttrValurDTO> findListSpecAttrRel(IPage page);

    /**
     * 商品所有规格，goodid维度
     *
     * @param page
     * @return
     */
    List<GoodsAllSpecDTO> goodsAllSpec(IPage page);


}