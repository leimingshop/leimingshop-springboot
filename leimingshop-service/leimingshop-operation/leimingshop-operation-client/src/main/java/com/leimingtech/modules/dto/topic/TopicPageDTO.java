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
 * 专题页
 *
 * @author kuangweiguo kuangweiguo
 * @since v1.0.0 2020-11-19
 */
@Data
@ApiModel(value = "专题页分页")
public class TopicPageDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    private Long id;
    @ApiModelProperty(value = "创建时间")
    private Date createDate;
    @ApiModelProperty(value = "专题页名称")
    private String topicName;
    @ApiModelProperty("商品数量")
    private Integer goodsNum;

}