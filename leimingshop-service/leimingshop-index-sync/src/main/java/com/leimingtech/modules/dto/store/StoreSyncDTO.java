/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dto.store;

import lombok.Data;

import java.io.Serializable;

/**
 * @Description
 * @Data: 2019/12/5 10:06
 * @Author: chengqian
 * @Version: 1.0
 */
@Data
public class StoreSyncDTO implements Serializable {

    /**
     * 店铺ID
     */
    private Long id;

    /**
     * 店铺名称
     */
    private String storeName;

    /**
     * 店铺LOGO
     */
    private String storeLogo;

    /**
     * 店铺类型
     */
    private Integer storeType;

    /**
     * 店铺关注数量
     */
    private Integer storeFavNum;

    /**
     * 店铺等级
     */
    private String storeGradeName;

    /**
     * 启用禁用
     */
    private Integer isEnable;
}
