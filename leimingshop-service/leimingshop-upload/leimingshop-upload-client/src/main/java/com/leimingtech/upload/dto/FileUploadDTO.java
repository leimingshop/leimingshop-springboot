/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.upload.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 文件压缩对象
 *
 * @author xuzhch
 * @date 2020年9月16日
 */
@Data
@ApiModel(description = "FileUploadDTO")
public class FileUploadDTO {

    @ApiModelProperty(value = "文件byte数组")
    private byte[] logByteArr;

    @ApiModelProperty(value = "文件名称")
    private String fileName;

}
