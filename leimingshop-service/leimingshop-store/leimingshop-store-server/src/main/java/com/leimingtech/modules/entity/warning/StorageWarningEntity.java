/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.entity.warning;

import com.baomidou.mybatisplus.annotation.TableName;
import com.leimingtech.commons.mybatis.entity.BaseEntity;
import lombok.Data;

/**
 * 库存预警表
 *
 * @author chengqian chengqian@leimingtech.com
 * @since v1.0.0 2019-12-12
 */
@Data
@TableName("lmshop_storage_warning")
public class StorageWarningEntity extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 商品id
     */
    private Long goodsId;
    /**
     * 库存预警值
     */
    private Integer storage;
    /**
     * 是否发送短信（0 发送，1 不发送）
     */
    private Integer isSendSms;
    /**
     * 是否启用（0 启用，1禁用）
     */
    private Integer isEnable;
    /**
     * 店铺ID
     */
    private Long storeId;
}