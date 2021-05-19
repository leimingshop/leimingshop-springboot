/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * 时间区间查询
 *
 * @author kviuff
 * @version 1.0
 * @date 2018/12/03
 */
@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class RangConditionsToTimeModelDTO implements Serializable {

    private static final long serialVersionUID = -1649669156877508723L;
    /**
     * 开始时间
     */
    private Timestamp beginTime;

    /**
     * 结束时间
     */
    private Timestamp endTime;
}
