/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.util.push.android;


import com.leimingtech.util.push.AbstractAndroidNotification;

public class AndroidUnicast extends AbstractAndroidNotification {
    public AndroidUnicast(String appkey, String appMasterSecret) throws Exception {
        setAppMasterSecret(appMasterSecret);
        setPredefinedKeyValue("appkey", appkey);
        this.setPredefinedKeyValue("type", "unicast");
    }

    public void setDeviceToken(String token) throws Exception {
        setPredefinedKeyValue("device_tokens", token);
    }

}
