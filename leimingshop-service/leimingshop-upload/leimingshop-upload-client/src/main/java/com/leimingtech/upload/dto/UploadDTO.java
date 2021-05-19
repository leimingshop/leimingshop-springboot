/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.upload.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 上传信息
 *
 * @since 1.1.0
 */
@Data
@ApiModel(description = "UploadDTO")
public class UploadDTO {
    @ApiModelProperty(value = "文件URL")
    private String url;
    @ApiModelProperty(value = "文件大小，单位字节")
    private Long size;

}
