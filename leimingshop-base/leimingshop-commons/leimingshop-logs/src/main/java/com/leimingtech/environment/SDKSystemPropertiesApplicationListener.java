/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.environment;

import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;

public class SDKSystemPropertiesApplicationListener implements ApplicationListener<ContextRefreshedEvent> {

    private String systemId = "";

    public SDKSystemPropertiesApplicationListener() {
    }

    public SDKSystemPropertiesApplicationListener(String systemId) {
        this.systemId = systemId;
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {

        String systemId = System.getProperty("SYSTEM_ID");
        if (systemId == null || "".equals(systemId.trim())) {
            System.setProperty("SYSTEM_ID", this.systemId);
        }

    }


}
