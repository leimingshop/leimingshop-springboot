/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.cloud;

import com.github.tobato.fastdfs.domain.StorePath;
import com.github.tobato.fastdfs.service.DefaultGenerateStorageClient;
import com.leimingtech.commons.tools.exception.CustomException;
import com.leimingtech.commons.tools.utils.SpringContextUtils;
import com.leimingtech.exception.ModuleErrorCode;
import com.leimingtech.upload.cloud.CloudStorageConfig;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

/**
 * FastDFS
 */
public class FastDfsCloudStorageService extends AbstractCloudStorageService {
    private static DefaultGenerateStorageClient defaultGenerateStorageClient;

    static {
        defaultGenerateStorageClient = (DefaultGenerateStorageClient) SpringContextUtils.getBean("defaultGenerateStorageClient");
    }

    public FastDfsCloudStorageService(CloudStorageConfig config) {
        this.config = config;
    }

    @Override
    public String upload(byte[] data, String path) {
        return upload(new ByteArrayInputStream(data), path);
    }

    @Override
    public String upload(InputStream inputStream, String suffix) {
        StorePath storePath;
        try {
            storePath = defaultGenerateStorageClient.uploadFile("group1", inputStream, inputStream.available(), suffix);
        } catch (Exception ex) {

            throw new CustomException(ModuleErrorCode.OSS_UPLOAD_FILE_ERROR, ex, ex.getMessage());
        }
        return "/" + storePath.getGroup() + "/" + storePath.getPath();
    }

    @Override
    public String uploadSuffix(byte[] data, String suffix) {
        return upload(data, suffix);
    }

    @Override
    public String uploadSuffix(InputStream inputStream, String suffix) {
        return upload(inputStream, suffix);
    }
}
