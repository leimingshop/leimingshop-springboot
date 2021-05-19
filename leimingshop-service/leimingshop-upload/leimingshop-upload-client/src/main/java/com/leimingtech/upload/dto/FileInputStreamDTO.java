/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.upload.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.InputStream;

/**
 * @author pixiaoyong@leimingtech.com
 * @date 2020/4/10 14:16
 * @email 1609973595@qq.com
 */
@Data
@ApiModel("视频上传实体")
public class FileInputStreamDTO {

    @ApiModelProperty(value = "文件真实名称")
    private String fileRealName;

    @ApiModelProperty(value = "文件后缀")
    private String fileSuffix;

    @ApiModelProperty(value = "文件大小（KB为单位）")
    private Long fileSize;

    @ApiModelProperty(value = "输入流")
    private InputStream inputStream;
}
