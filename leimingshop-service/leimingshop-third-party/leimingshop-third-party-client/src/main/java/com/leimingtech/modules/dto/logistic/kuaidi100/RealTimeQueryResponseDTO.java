/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dto.logistic.kuaidi100;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;


/**
 * @Author: SWH ab4856812@163.com
 * @Description: 快递100实时查询接口响应实体
 * @Date: 2019/7/31 9:48
 * @Version: V1.0
 */
@Data
@ToString
@ApiModel(description = "RealTimeQueryResponseDTO")
public class RealTimeQueryResponseDTO implements Serializable {

    @ApiModelProperty("消息体，请忽略")
    private String message;

    @ApiModelProperty("快递单当前状态，包括0在途，1揽收，2疑难，3签收，4退签，5派件，6退回等7个状态")
    private Integer state;

    @ApiModelProperty("快递单明细状态标记，暂未实现，请忽略")
    private String condition;

    @ApiModelProperty("是否签收标记")
    private Integer ischeck;

    @ApiModelProperty("快递公司编码,一律用小写字母")
    private String com;

    @ApiModelProperty("快递单号")
    private String nu;

    @ApiModelProperty("返回数据")
    private List<RealTimeQueryResponseDataDTO> data;

}
