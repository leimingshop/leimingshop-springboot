/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.util.push.android;


import com.leimingtech.util.push.AbstractAndroidNotification;

public class AndroidBroadcast extends AbstractAndroidNotification {
    public AndroidBroadcast(String appkey, String appMasterSecret) throws Exception {
        setAppMasterSecret(appMasterSecret);
        setPredefinedKeyValue("appkey", appkey);
        this.setPredefinedKeyValue("type", "broadcast");
    }
}
