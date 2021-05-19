/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.upload.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <base64文件上传实体>
 *
 * @author 刘远杰
 * @email 2634443725@qq.com
 * @since 1.0 2019/8/2
 */
@ApiModel(description = "Base64FileDTO")
@Data
public class Base64FileDTO {

    @ApiModelProperty(value = "文件真实名称")
    private String fileRealName;

    @ApiModelProperty(value = "文件后缀")
    private String fileSuffix;

    @ApiModelProperty(value = "文件大小（KB为单位）")
    private Long fileSize;

    @ApiModelProperty(value = "base64字符串转化的byte数组")
    private byte[] base64ByteArr;

    @ApiModelProperty(value = "是否是上传店铺资质信息 0 是 1 否")
    private Integer type;

}
