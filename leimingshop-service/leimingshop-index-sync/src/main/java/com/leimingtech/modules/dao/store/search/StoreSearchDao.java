/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dao.store.search;


import com.leimingtech.modules.dto.store.StoreDTO;
import com.leimingtech.modules.dto.store.StoreSearchDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author chengqian
 * @version 1.0
 * @date 2019/12-11
 */
@Mapper
public interface StoreSearchDao {

    /**
     * 同步店铺信息到es
     *
     * @param startTime
     * @param endTime
     * @return
     */
//    @DataSource("store")
    List<StoreSearchDTO> selectStoreIndexInfoByUpdateTime(@Param("startTime") String startTime,
                                                          @Param("endTime") String endTime,
                                                          @Param("storeId") Long storeId);

    //    @DataSource("store")
    StoreDTO get(Long storeId);

    //    @DataSource("store")
    String getNameById(Long gradeId);
}
