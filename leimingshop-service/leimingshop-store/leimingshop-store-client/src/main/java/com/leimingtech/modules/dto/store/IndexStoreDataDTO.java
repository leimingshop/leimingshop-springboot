/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dto.store;

import lombok.Data;

import java.io.Serializable;

/**
 * @author xuzhch
 * @className IndexStoreDataDTO
 * @description 首页店铺数据
 * @date 2020/3/17/017
 */
@Data
public class IndexStoreDataDTO implements Serializable {
    private Integer newStoreCount;
    private Integer storeCount;
}
