/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.dto.upload;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;


/**
 * 文件上传记录表
 *
 * @author chengqian
 * @email 543826372@gmail.com
 * @since 7.0 2019-05-24
 */
@Data
@ApiModel(description = "UploadRecordDTO")
public class UploadRecordDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "id")
    private Long id;
    @ApiModelProperty(value = "文件路径url")
    private String fileUrl;
    @ApiModelProperty(value = "文件真实名称")
    private String fileRealName;
    @ApiModelProperty(value = "文件后缀")
    private String fileSuffix;
    @ApiModelProperty(value = "文件大小（KB为单位）")
    private Integer fileSize;
    @ApiModelProperty(value = "业务分类（在什么场景下上传）")
    private String uploadType;

}