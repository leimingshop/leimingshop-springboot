/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.entity.browse;

import com.leimingtech.commons.mybatis.entity.BaseEntity;
import com.leimingtech.modules.constants.mongodb.CollectionName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 足迹记录表
 *
 * @author chengqian
 * @email 543826372@qq.com
 * @since 1.0.0 2019-06-03
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Document(collection = CollectionName.BROWSE_RECORD)
public class GoodsBrowseMongoEntity extends BaseEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 用户id
     */
    private Long memberId;

    /**
     * 店铺id
     */
    private Long storeId;
    /**
     * 商品ID
     */
    private Long goodsId;

    /**
     * 商品名称
     */
    private String goodsName;

    /**
     * 商品图片
     */
    private String goodsImage;
    /**
     * 商品副标题
     */
    private String goodsSubTitle;

    /**
     * 商品标签
     */
    private String labelName;
    /**
     * 商品价格
     */
    private BigDecimal goodsPrice;

    /**
     * 商品规格Id
     */
    private Long goodsSpecId;
    private Date createDayTime;
    private Date createTime;

    /**
     * 店铺名称
     */
    private String storeName;
    /**
     * 店铺类型（1:自营商户，2:普通商户）
     */
    private Integer storeType;


}