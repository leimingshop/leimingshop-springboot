/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Field工具类
 *
 * @author lixiang
 * @version V1.0
 * @date 2019/12/10 17:14
 **/
@Slf4j
public class FieldMappingUtils {

    /**
     * 获取实体注解上的FieldMapping
     *
     * @param clazz: 对象的class
     * @return 实体的FieldMapping集合
     * @date 2019/12/10 17:15
     * @author lixiangx@leimingtech.com
     **/
    public static List<FieldMapping> getFieldInfo(Class clazz) {
        return getFieldInfo(clazz, null);
    }


    /**
     * 获取实体注解上的FieldMapping
     *
     * @param clazz:     对象的class
     * @param fieldName: 类名称
     * @return 实体的FieldMapping集合
     * @date 2019/12/10 17:15
     * @author lixiangx@leimingtech.com
     **/
    public static List<FieldMapping> getFieldInfo(Class clazz, String fieldName) {

        // 返回Class中所有的字段（包括私有字段）
        Field[] fields = clazz.getDeclaredFields();

        // 创建FieldMapping集合
        List<FieldMapping> fieldMappingList = new ArrayList<>();

        for (Field field : fields) {

            // 获取字段上的FieldInfo对象
            FieldInfo fieldInfo = field.getAnnotation(FieldInfo.class);
            if (fieldInfo == null) {
                continue;
            }

            if ("object".equals(fieldInfo.type()) || "nested".equals(fieldInfo.type())) {
                // 字段FieldInfo注解的type是object
                // 获取字段的类型
                Class fc = field.getType();

                // 判断指定的Class对象是否为基本类型
                // （有九种预定义的Class对象代表的八个基本类型和void。这些都是由Java虚拟机创建的，并且具有相同的名称，
                // 它们代表即boolean, byte, char, short, int, long, float, 和double 等原始类型）。
                if (fc.isPrimitive()) {

                    // 获取Class的类名称（基本类型）
                    String name = field.getName();
                    if (StringUtils.isNotBlank(fieldName)) {
                        name = name + "." + fieldName;
                    }
                    fieldMappingList.add(new FieldMapping(name, fieldInfo.type(), fieldInfo.participle(), fieldInfo.pinyin()));
                } else {
                    // 判断是否为List
                    if (fc.isAssignableFrom(List.class)) {
                        log.debug("List类型：{}", field.getName());
                        // 得到泛型类型
                        Type gt = field.getGenericType();
                        ParameterizedType pt = (ParameterizedType) gt;
                        Class listClass = (Class) pt.getActualTypeArguments()[0];

                        // 循环获取集合里面的实体FieldMapping
                        List<FieldMapping> fieldMappings = getFieldInfo(listClass, field.getName());
                        FieldMapping fieldMapping = new FieldMapping(field.getName(), fieldInfo.type(), fieldInfo.participle(), fieldInfo.pinyin());
                        fieldMapping.setFieldMappingList(fieldMappings);
                        fieldMappingList.add(fieldMapping);
                    } else {
                        // 循环获取集合里面的实体FieldMapping
                        List<FieldMapping> fieldMappings = getFieldInfo(fc, field.getName());
                        FieldMapping fieldMapping = new FieldMapping(field.getName(), fieldInfo.type(), fieldInfo.participle(), fieldInfo.pinyin());
                        fieldMapping.setFieldMappingList(fieldMappings);
                        fieldMappingList.add(fieldMapping);
                    }
                }

            } else {
                // 字段FieldInfo注解的type不是object
                String name = field.getName();
                fieldMappingList.add(new FieldMapping(name, fieldInfo.type(), fieldInfo.participle(), fieldInfo.pinyin()));
            }

        }
        return fieldMappingList;

    }
}
