/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.utils;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author kuangweiguo
 * @version 1.0
 * @date 2019/6/29 1:24 PM
 */
@Data
@NoArgsConstructor
public class FieldMapping {

    /**
     * 名称
     */
    private String field;

    /**
     * 类型
     */
    private String type;

    /**
     * 分词器选择  0. not_analyzed   1. ik_smart 2. ik_max_word  3.ik-index(自定义分词器)
     */
    private int participle;

    /**
     * 是否使用拼音分词 默认0 否、1 是
     */
    private int pinyin;

    /**
     * 嵌套集合
     */
    private List<FieldMapping> fieldMappingList;


    public FieldMapping(String field, String type, int participle, int pinyin) {
        this.field = field;
        this.type = type;
        this.participle = participle;
        this.pinyin = pinyin;
    }
}
