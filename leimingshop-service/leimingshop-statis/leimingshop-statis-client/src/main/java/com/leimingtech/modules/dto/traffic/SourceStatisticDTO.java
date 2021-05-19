/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dto.traffic;

import lombok.Data;

/**
 * @author xuzhch
 * @className SourceStatisticDTO
 * @description
 * @date 2020/3/13/013
 */
@Data
public class SourceStatisticDTO {
    private Integer browsePlatforms;
    private String ipDetail;
    private Integer pvCount;
}
