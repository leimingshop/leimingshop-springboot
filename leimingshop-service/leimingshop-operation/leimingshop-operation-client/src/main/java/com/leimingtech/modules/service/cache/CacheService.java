/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.service.cache;


import com.leimingtech.modules.dto.index.H5IndexDTO;
import com.leimingtech.modules.dto.index.H5StoreIndexDTO;
import com.leimingtech.modules.dto.webfloor.WebFloorAndLinkDTO;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * 功能描述：
 * Operation缓存层
 *
 * @author 宋文豪
 * @email: songwenhao@leimingtech.com
 * @Date : 2020/3/6
 **/


public interface CacheService {

    /**
     * 功能描述：
     * 首页缓存
     *
     * @return 返回H5首页信息
     * @author 宋文豪
     * @email: songwenhao@leimingtech.com
     * @Date : 2020/3/6
     **/

    H5IndexDTO indexCache();

    /**
     * 店铺首页缓存
     *
     * @param storeId
     * @return
     */

    H5StoreIndexDTO storeIndexCache(@RequestParam(value = "storeId") Long storeId);

    /**
     * 查询楼层信息和楼层内商品信息
     *
     * @param storeId   店铺id
     * @param floorType 楼层类型
     * @return 返回楼层配置信息
     */

    List<WebFloorAndLinkDTO> floor(@RequestParam(value = "storeId", required = false) Long storeId,
                                   @RequestParam(value = "floorType", required = false) Integer floorType);
}
