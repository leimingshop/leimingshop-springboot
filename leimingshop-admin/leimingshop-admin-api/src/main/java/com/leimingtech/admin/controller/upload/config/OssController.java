/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.admin.controller.upload.config;

import com.alibaba.fastjson.JSON;
import com.leimingtech.commons.tools.annotation.LogOperation;
import com.leimingtech.commons.tools.utils.Result;
import com.leimingtech.commons.tools.validator.ValidatorUtils;
import com.leimingtech.commons.tools.validator.group.AliyunGroup;
import com.leimingtech.commons.tools.validator.group.QcloudGroup;
import com.leimingtech.commons.tools.validator.group.QiniuGroup;
import com.leimingtech.remote.ParamsRemoteService;
import com.leimingtech.upload.cloud.CloudStorageConfig;
import com.leimingtech.upload.enums.OssTypeEnum;
import com.leimingtech.upload.utils.ModuleConstant;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 文件上传
 */
@RestController
@RequestMapping("file")
@Api(tags = "文件上传")
public class OssController {

    private static final String KEY = ModuleConstant.CLOUD_STORAGE_CONFIG_KEY;
    @Autowired
    private ParamsRemoteService paramsRemoteService;

    @GetMapping("info")
    @ApiOperation(value = "云存储配置信息")
    public Result<CloudStorageConfig> info() {
        CloudStorageConfig config = paramsRemoteService.getValueObject(KEY, CloudStorageConfig.class);

        return new Result<CloudStorageConfig>().ok(config);
    }

    @PostMapping
    @ApiOperation(value = "保存云存储配置信息")
    @LogOperation("保存云存储配置信息")
    public Result saveConfig(@RequestBody CloudStorageConfig config) {
        //校验类型
        ValidatorUtils.validateEntity(config);

        if (config.getType() == OssTypeEnum.QINIU.value()) {
            //校验七牛数据
            ValidatorUtils.validateEntity(config, QiniuGroup.class);
        } else if (config.getType() == OssTypeEnum.ALIYUN.value()) {
            //校验阿里云数据
            ValidatorUtils.validateEntity(config, AliyunGroup.class);
        } else if (config.getType() == OssTypeEnum.QCLOUD.value()) {
            //校验腾讯云数据
            ValidatorUtils.validateEntity(config, QcloudGroup.class);
        }

        paramsRemoteService.updateValueByCode(KEY, JSON.toJSONString(config));

        return new Result();
    }


}