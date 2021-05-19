/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.config;

import com.leimingtech.commons.tools.config.ModuleConfig;
import org.springframework.stereotype.Service;

/**
 * 模块配置信息
 *
 * @since 1.0.0
 */
@Service
public class ModuleConfigImpl implements ModuleConfig {
    @Override
    public String getName() {
        return "consumer";
    }
}