/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dto.store;

import com.leimingtech.modules.utils.FieldInfo;
import lombok.Data;

import java.io.Serializable;

/**
 * @Description
 * @Data: 2019/12/5 10:06
 * @Author: chengqian
 * @Version: 1.0
 */
@Data
public class StoreSearchDTO implements Serializable {

    /**
     * 店铺ID
     */
    @FieldInfo(type = "long")
    private Long id;

    /**
     * 店铺名称
     */
    @FieldInfo(participle = 2)
    private String storeName;

    /**
     * 店铺LOGO
     */
    @FieldInfo
    private String storeLogo;

    /**
     * 店铺类型
     */
    @FieldInfo(type = "integer")
    private Integer storeType;

    /**
     * 店铺关注数量
     */
    @FieldInfo(type = "integer")
    private Integer storeFavNum;

    /**
     * 店铺等级
     */
    @FieldInfo
    private String storeGradeName;

    /**
     * 启用禁用
     */
    @FieldInfo(type = "integer")
    private Integer isEnable;

    /**
     * 店铺销量
     */
    @FieldInfo(type = "integer")
    private Integer saleNum;

    /**
     * 评论星级
     */
    @FieldInfo(type = "double")
    private String goodEvaluate;
}
