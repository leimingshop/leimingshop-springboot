/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.upload.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author pixiaoyong@leimingtech.com
 * @date 2020/4/10 14:06
 * @email 1609973595@qq.com
 */
@Data
@ApiModel(value = "视频上传")
public class UploadVideoDTO implements Serializable {

    @ApiModelProperty(value = "文件URL")
    private String url;

    @ApiModelProperty(value = "视频封面图URL")
    private String indexImage;

    @ApiModelProperty(value = "文件大小，单位字节")
    private Long size;


}
