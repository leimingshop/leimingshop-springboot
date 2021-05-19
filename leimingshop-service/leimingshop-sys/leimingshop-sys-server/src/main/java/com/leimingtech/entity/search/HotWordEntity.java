/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.entity.search;


import com.baomidou.mybatisplus.annotation.TableName;
import com.leimingtech.commons.mybatis.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * 热词搜索
 *
 * @author qin
 * @email 49636174@qq.com
 * @since 1.0.0 2019-12-3
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("lmshop_hot_word")
public class HotWordEntity extends BaseEntity implements Serializable {

    /**
     * 搜索词
     */
    private String hotWord;

    /**
     * 排序字段
     */
    private int sort;
}
