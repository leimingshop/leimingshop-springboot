/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.vo.reduce;

import com.leimingtech.modules.dto.reduce.ReduceGoodsDTO;
import com.leimingtech.modules.dto.reduce.ReduceRuleDTO;
import com.leimingtech.modules.utils.FieldInfo;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * <满减活动ES索引同步实体>
 *
 * @author 刘远杰
 * @email 2634443725@qq.com
 * @since 1.0 2019/12/10
 */
@Data
public class ReduceActivityIndexVo implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    @FieldInfo(type = "long")
    private Long id;

    /**
     * 活动范围 0平台 1店铺
     */
    @FieldInfo(type = "integer")
    private Integer activityScope;

    /**
     * 店铺ID
     */
    @FieldInfo(type = "long")
    private Long storeId;

    /**
     * 店铺名称
     */
    @FieldInfo(participle = 0)
    private String storeName;

    /**
     * 活动规则 0普通满减 1每满减 2阶梯满减
     */
    @FieldInfo(type = "integer")
    private Integer ruleType;

    /**
     * 活动开始时间
     */
    @FieldInfo(type = "date")
    private Date startDate;

    /**
     * 活动结束时间
     */
    @FieldInfo(type = "date")
    private Date endDate;

    /**
     * 积分是否可用 0不可用 1可用
     */
    @FieldInfo(type = "integer")
    private Integer pointFlag;

    /**
     * 余额是否可用 0不可用 1可用
     */
    @FieldInfo(type = "integer")
    private Integer balanceFlag;

    /**
     * 优惠券是否可用 0不可用 1可用
     */
    @FieldInfo(type = "integer")
    private Integer couponsFlag;

    /**
     * 秒杀活动是否可用 0不可用 1可用
     */
    @FieldInfo(type = "integer")
    private Integer seckillFlag;

    /**
     * 活动商品范围 0全场通用 1指定店铺分类 2指定商品 3指定品牌
     */
    @FieldInfo(type = "integer")
    private Integer activityGoodsScope;

    /**
     * 活动商品集合
     */
    @FieldInfo(type = "nested")
    private List<ReduceGoodsDTO> goodsList;

    /**
     * 活动规则集合
     */
    @FieldInfo(type = "nested")
    private List<ReduceRuleDTO> ruleList;

}
