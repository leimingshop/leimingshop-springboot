/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.cloud;

import com.leimingtech.commons.tools.utils.SpringContextUtils;
import com.leimingtech.remote.ParamsRemoteService;
import com.leimingtech.upload.cloud.CloudStorageConfig;
import com.leimingtech.upload.enums.OssTypeEnum;
import com.leimingtech.upload.utils.ModuleConstant;

/**
 * 文件上传Factory
 */
public final class OssFactory {
    private static ParamsRemoteService paramsRemoteService;

    static {
        OssFactory.paramsRemoteService = SpringContextUtils.getBean(ParamsRemoteService.class);
    }

    private OssFactory() {
    }

    public static AbstractCloudStorageService build() {
        //获取云存储配置信息
        CloudStorageConfig config = paramsRemoteService.getValueObject(ModuleConstant.CLOUD_STORAGE_CONFIG_KEY, CloudStorageConfig.class);

        if (config.getType() == OssTypeEnum.QINIU.value()) {
            return new QiniuCloudStorageService(config);
        } else if (config.getType() == OssTypeEnum.ALIYUN.value()) {
            return new AliyunCloudStorageService(config);
        } else if (config.getType() == OssTypeEnum.QCLOUD.value()) {
            return new QcloudCloudStorageService(config);
        } else if (config.getType() == OssTypeEnum.FASTDFS.value()) {
            return new FastDfsCloudStorageService(config);
        } else if (config.getType() == OssTypeEnum.LOCAL.value()) {
            return new LocalCloudStorageService(config);
        }

        return null;
    }

}
