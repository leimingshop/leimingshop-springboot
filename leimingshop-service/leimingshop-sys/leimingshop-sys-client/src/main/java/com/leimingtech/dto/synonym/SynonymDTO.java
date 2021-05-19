/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.dto.synonym;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;


/**
 * 同义词管理DTO
 *
 * @author lixiangx@leimingtech.com
 * @date 2019/12/10 10:39
 **/
@Data
@ApiModel(description = "SynonymDTO")
public class SynonymDTO implements Serializable {

    private static final long serialVersionUID = 5009176975623417499L;

    @ApiModelProperty(value = "主键")
    private Long id;
    @ApiModelProperty(value = "词库名称")
    private String name;
    @ApiModelProperty(value = "0:停用，1：启用")
    private Integer state;
    @ApiModelProperty(value = "创建人")
    private String creator;
    @ApiModelProperty(value = "创建时间")
    private Date createDate;
    @ApiModelProperty(value = "更新人")
    private String updater;
    @ApiModelProperty(value = "更新时间")
    private Date updateDate;
    @ApiModelProperty(value = "删除标记（默认为0未删除，1已删除）")
    private Integer delFlag;
    @ApiModelProperty(value = "乐观锁")
    private Long version;
}