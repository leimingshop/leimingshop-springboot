/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.dto.stopword;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;


/**
 * 广告禁语管理
 *
 * @author chengqian
 * @since v1.0.0 2019-08-21
 */
@Data
@ApiModel(value = "广告禁语管理 ")
public class StopWordDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    private Long id;
    @ApiModelProperty(value = "名称")
    private String name;
    @ApiModelProperty(value = "敏感词编码")
    private String banCode;
    @ApiModelProperty(value = "创建时间")
    private Date createDate;

}