/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.util.push.ios;

import com.leimingtech.util.push.IOSNotification;
import org.json.JSONObject;


public class IOSGroupcast extends IOSNotification {
    public IOSGroupcast(String appkey, String appMasterSecret) throws Exception {
        setAppMasterSecret(appMasterSecret);
        setPredefinedKeyValue("appkey", appkey);
        this.setPredefinedKeyValue("type", "groupcast");
    }

    public void setFilter(JSONObject filter) throws Exception {
        setPredefinedKeyValue("filter", filter);
    }
}
