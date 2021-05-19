/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.service.sys;


import org.springframework.web.bind.annotation.RequestParam;

/**
 * 国际化
 */

public interface SysLanguageService {

    /**
     * 保存或更新
     *
     * @param tableName  表名
     * @param tableId    表主键
     * @param fieldName  字段名
     * @param fieldValue 字段值
     * @param language   语言
     */

    void saveOrUpdate(@RequestParam(value = "tableName") String tableName, @RequestParam(value = "tableId") Long tableId,
                      @RequestParam(value = "fieldName") String fieldName, @RequestParam(value = "fieldValue") String fieldValue,
                      @RequestParam(value = "language") String language);
}

