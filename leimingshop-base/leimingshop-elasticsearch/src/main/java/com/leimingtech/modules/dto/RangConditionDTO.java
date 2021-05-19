/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

/**
 * 区间查询
 *
 * @author kviuff
 * @version 1.0
 * @date 2018/12/03
 */
@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class RangConditionDTO implements Serializable {

    private static final long serialVersionUID = 4872669865514539066L;
    /**
     * 开始区间
     */
    private String beginValue;

    /**
     * 结束区间
     */
    private String endValue;
}
