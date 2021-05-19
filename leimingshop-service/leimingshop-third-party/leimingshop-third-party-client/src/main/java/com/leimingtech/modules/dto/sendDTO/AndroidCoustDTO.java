/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dto.sendDTO;

import lombok.Data;

/**
 * @Author tyl
 * @Date 2019/8/28 16:37
 * @Description android 推送实体
 **/
@Data
public class AndroidCoustDTO {

    private String userName;
    private String orderId;
    private String orderMoney;
    private String afterId;
    private String typeValue;
    private String messAgeType;
    private String goodsId;
    private String goodsSkuId;
    /**
     * 店铺id
     */
    private String storeId;
    /**
     * 品牌id
     */
    private String brandId;
    /**
     * 消息id
     */
    private String messAgeId;


}
