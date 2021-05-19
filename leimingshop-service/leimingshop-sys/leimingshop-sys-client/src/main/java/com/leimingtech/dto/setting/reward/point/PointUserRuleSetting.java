/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.dto.setting.reward.point;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

/**
 * 积分使用规格
 *
 * @author lixiang
 * @version V1.0
 * @date 2019/12/23 10:03
 **/
@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class PointUserRuleSetting implements Serializable {
    private static final long serialVersionUID = -2025885043266024817L;

    /**
     * 订单金额大于
     */
    private Integer orderAmountGt;

    /**
     * 积分数量大于
     */
    private Integer pointCountGt;

    /**
     * 积分支付不得超过订单应付金额的*%
     */
    private Integer pointMoreOrderAmountPercentage;

    /**
     * 使用积分数量为*的整数倍
     */
    private Integer pointCountMultiple;

    /**
     * 每*积分抵1元
     */
    private Integer pointDeduction;

    /**
     * 积分使用说明
     */
    private String pointUserDescribe;
}
