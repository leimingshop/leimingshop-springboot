/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dao.storegoodsclass;

import com.leimingtech.commons.mybatis.dao.BaseDao;
import com.leimingtech.modules.dto.storegoodsclass.StoreGoodsClassListDTO;
import com.leimingtech.modules.entity.storegoodsclass.StoreGoodsClassEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 店铺商品分类表
 *
 * @author chengqian chengqian@leimingtech.com
 * @since v1.0.0 2020-05-12
 */
@Mapper
public interface StoreGoodsClassDao extends BaseDao<StoreGoodsClassEntity> {

    /**
     * 查询分类名称是否重复
     *
     * @param gcName     分类名称
     * @param storeId    店铺id
     * @param gcParentId 父id
     * @param type       类型
     * @param id         主键
     * @return 返回重复数量
     */
    Integer classNameCount(@Param("gcName") String gcName,
                           @Param("storeId") Long storeId,
                           @Param("gcParentId") Long gcParentId,
                           @Param("type") Integer type,
                           @Param("id") Long id);

    /**
     * 查询店铺商品分类
     *
     * @param storeId
     * @return
     */
    List<StoreGoodsClassListDTO> findStoreGoodsClass(Long storeId);

    /**
     * 查询所有一级分类
     *
     * @param storeId 店铺id
     * @return 返回店铺分类信息
     */
    List<StoreGoodsClassListDTO> firstClass(Long storeId);

    /**
     * 功能描述 根据分类名称 父类id 店铺id 查找分类id 判断分类名称是否重复
     *
     * @param storeGoodsClassName 分类名称
     * @param parentId            父类id
     * @param storeId             店铺id
     * @return Long  查到的id
     * @author lishuo
     * @date 2020/6/17
     */
    Long findStoreGoodsClassByGcName(@Param("storeGoodsClassName") String storeGoodsClassName, @Param("parentId") long parentId, @Param("storeId") Long storeId);
}