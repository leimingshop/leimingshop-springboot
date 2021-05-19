/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.config;

import com.leimingtech.commons.tools.config.ModuleConfig;
import org.springframework.stereotype.Service;

/**
 * 模块名称配置类
 *
 * @author lixiangx@leimingtech.com
 * @date 2020/5/9 9:16
 **/
@Service
public class ModuleConfigImpl implements ModuleConfig {
    @Override
    public String getName() {
        return "portal";
    }
}