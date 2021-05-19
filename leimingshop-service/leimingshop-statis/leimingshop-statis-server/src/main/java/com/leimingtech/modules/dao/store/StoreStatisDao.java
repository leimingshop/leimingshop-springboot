/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dao.store;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 店铺表DB查询接口
 *
 * @author xuzhch
 * @date 2020年9月17日
 */
@Mapper
public interface StoreStatisDao {
    /**
     * 查询店铺ID集合
     *
     * @return list 店铺ID集合
     * @author xuzhch
     * @date 2020年09月18日
     */
    List<Long> selectList();
}
