/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dto.topic;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;


/**
 * 专题页商品关联表
 *
 * @author kuangweiguo kuangweiguo
 * @since v1.0.0 2020-11-19
 */
@Data
@ApiModel(value = "专题页商品关联表")
public class TopicRelDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    private Long id;
    @ApiModelProperty(value = "创建人")
    private String creator;
    @ApiModelProperty(value = "创建时间")
    private Date createDate;
    @ApiModelProperty(value = "更新人")
    private String updater;
    @ApiModelProperty(value = "更新时间")
    private Date updateDate;
    @ApiModelProperty(value = "删除标记（默认0:未删除,1:已删除）")
    private Integer delFlag;
    @ApiModelProperty(value = "乐观锁")
    private Integer version;
    @ApiModelProperty(value = "专题页id")
    private Long topicId;
    @ApiModelProperty(value = "商品id")
    private Long goodsId;
    @ApiModelProperty("排序")
    private Integer sort;

}