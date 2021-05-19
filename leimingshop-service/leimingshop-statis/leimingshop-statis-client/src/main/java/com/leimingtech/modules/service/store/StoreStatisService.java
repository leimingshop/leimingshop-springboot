/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.service.store;

import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 店铺业务接口
 *
 * @author xuzhch
 * @date 2019年12月30日16:06
 */
@Service
public interface StoreStatisService {

    /**
     * 查询所有店铺ID
     *
     * @return 所有点ID集合
     * @date 2019/12/30/030 16:04
     * @author xuzhch
     **/
    List<Long> selectList();

}
