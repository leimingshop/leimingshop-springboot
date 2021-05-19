/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.entity.traffic;

import lombok.Data;

import java.util.Date;

/**
 * @author xuzhch
 * @className TrafficStatisticEntity
 * @description 流量统计查询对象
 * @date 2020/3/2/002
 */
@Data
public class TrafficStatisticEntity {
    private Date createDayTime;
    private Integer uvCount;
    private Integer pvCount;
    private String ipDetail;
}
