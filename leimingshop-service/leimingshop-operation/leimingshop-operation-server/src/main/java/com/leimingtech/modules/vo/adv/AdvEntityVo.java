/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.vo.adv;

import com.leimingtech.modules.entity.adv.AdvEntity;
import lombok.Data;

/**
 * @Description 广告实体超类
 * @Author 刘远杰
 * @Date 2019/5/14 16:06
 * Version 7.0
 **/
@Data
public class AdvEntityVo extends AdvEntity {

    /**
     * 广告分类名称
     */
    private String categoryName;

    /**
     * h5楼层名称
     */
    private String floorName;

    /**
     * 广告类别名称
     */
    private String gcName;

    /**
     * 广告类别 1 h5 2 pc
     */
    private Integer type;
}
