/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.util.push.android;

import com.leimingtech.util.push.AbstractAndroidNotification;
import org.json.JSONObject;


public class AndroidGroupcast extends AbstractAndroidNotification {
    public AndroidGroupcast(String appkey, String appMasterSecret) throws Exception {
        setAppMasterSecret(appMasterSecret);
        setPredefinedKeyValue("appkey", appkey);
        this.setPredefinedKeyValue("type", "groupcast");
    }

    public void setFilter(JSONObject filter) throws Exception {
        setPredefinedKeyValue("filter", filter);
    }
}
