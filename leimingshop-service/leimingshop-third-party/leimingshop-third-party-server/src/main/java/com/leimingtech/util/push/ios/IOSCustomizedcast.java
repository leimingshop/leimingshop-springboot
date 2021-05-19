/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.util.push.ios;


import com.leimingtech.util.push.IOSNotification;

public class IOSCustomizedcast extends IOSNotification {
    public IOSCustomizedcast(String appkey, String appMasterSecret) throws Exception {
        setAppMasterSecret(appMasterSecret);
        setPredefinedKeyValue("appkey", appkey);
        this.setPredefinedKeyValue("type", "customizedcast");
    }

    public void setAlias(String alias, String aliasType) throws Exception {
        setPredefinedKeyValue("alias", alias);
        setPredefinedKeyValue("alias_type", aliasType);
    }

    public void setFileId(String fileId, String aliasType) throws Exception {
        setPredefinedKeyValue("file_id", fileId);
        setPredefinedKeyValue("alias_type", aliasType);
    }

}
