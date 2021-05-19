/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.remote;

import com.alibaba.fastjson.JSON;
import com.leimingtech.commons.tools.exception.CustomException;
import com.leimingtech.commons.tools.exception.ErrorCode;
import com.leimingtech.service.sys.SysParamsService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 参数
 *
 * @since 1.1.0
 */
@Component
public class ParamsRemoteService {

    @Autowired
    private SysParamsService sysParamsService;

    /**
     * 根据参数编码，获取value的Object对象
     *
     * @param paramCode 参数编码
     * @param clazz     Object对象
     */
    public <T> T getValueObject(String paramCode, Class<T> clazz) {
        String paramValue = sysParamsService.getValue(paramCode);
        if (StringUtils.isNotBlank(paramValue)) {
            return JSON.parseObject(paramValue, clazz);
        }

        try {
            return clazz.newInstance();
        } catch (Exception e) {
            throw new CustomException(ErrorCode.PARAMS_GET_ERROR);
        }
    }

    /**
     * 根据参数编码，更新value
     *
     * @param paramCode  参数编码
     * @param paramValue 参数值
     */
    public void updateValueByCode(String paramCode, String paramValue) {
        sysParamsService.updateValueByCode(paramCode, paramValue);
    }

}