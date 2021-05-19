/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.service.sys.impl;

import com.leimingtech.commons.mybatis.service.impl.BaseServiceImpl;
import com.leimingtech.dao.SysLanguageDao;
import com.leimingtech.entity.SysLanguageEntity;
import com.leimingtech.service.sys.SysLanguageService;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 国际化
 */
@Service

public class SysLanguageServiceImpl extends BaseServiceImpl<SysLanguageDao, SysLanguageEntity> implements SysLanguageService {
    /**
     * 保存或更新
     *
     * @param tableName  表名
     * @param tableId    表主键
     * @param fieldName  字段名
     * @param fieldValue 字段值
     * @param language   语言
     */
    @Override

    public void saveOrUpdate(@RequestParam(value = "tableName") String tableName, @RequestParam(value = "tableId") Long tableId,
                             @RequestParam(value = "fieldName") String fieldName, @RequestParam(value = "fieldValue") String fieldValue,
                             @RequestParam(value = "language") String language) {
        SysLanguageEntity entity = new SysLanguageEntity();
        entity.setTableName(tableName);
        entity.setTableId(tableId);
        entity.setFieldName(fieldName);
        entity.setFieldValue(fieldValue);
        entity.setLanguage(language);

        //判断是否有数据
        if (baseDao.getLanguage(entity) == null) {
            baseDao.insert(entity);
        } else {
            baseDao.updateLanguage(entity);
        }
    }
}