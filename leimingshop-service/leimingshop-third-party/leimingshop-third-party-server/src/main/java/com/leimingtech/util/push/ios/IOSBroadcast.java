/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.util.push.ios;


import com.leimingtech.util.push.IOSNotification;

public class IOSBroadcast extends IOSNotification {
    public IOSBroadcast(String appkey, String appMasterSecret) throws Exception {
        setAppMasterSecret(appMasterSecret);
        setPredefinedKeyValue("appkey", appkey);
        this.setPredefinedKeyValue("type", "broadcast");

    }
}
