/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.util.push.ios;


import com.leimingtech.util.push.IOSNotification;

public class IOSFilecast extends IOSNotification {
    public IOSFilecast(String appkey, String appMasterSecret) throws Exception {
        setAppMasterSecret(appMasterSecret);
        setPredefinedKeyValue("appkey", appkey);
        this.setPredefinedKeyValue("type", "filecast");
    }

    public void setFileId(String fileId) throws Exception {
        setPredefinedKeyValue("file_id", fileId);
    }
}
