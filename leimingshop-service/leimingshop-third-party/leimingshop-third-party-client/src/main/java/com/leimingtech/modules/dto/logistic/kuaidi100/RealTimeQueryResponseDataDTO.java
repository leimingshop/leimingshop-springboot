/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dto.logistic.kuaidi100;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;


/**
 * @Author: SWH ab4856812@163.com
 * @Description: 快递100实时查询接口响应返回数据实体
 * @Date: 2019/7/31 9:48
 * @Version: V1.0
 */
@Data
@ToString
@ApiModel(description = "RealTimeQueryResponseDataDTO")
public class RealTimeQueryResponseDataDTO implements Serializable {

    @ApiModelProperty("物流轨迹节点内容：如上海分拨中心")
    private String context;

    @ApiModelProperty("时间，原始格式")
    private String time;

    @ApiModelProperty("格式化后时间")
    private String ftime;

    @ApiModelProperty("本数据元对应的签收状态。只有在开通签收状态服务（见上面\"status\"后的说明）且在订阅接口中提交resultv2标记后才会出现，如：在途")
    private String status;

    @ApiModelProperty("本数据元对应的行政区域的编码，只有在开通签收状态服务（见上面\"status\"后的说明）且在订阅接口中提交resultv2标记后才会出现，如：310000000000")
    private String areaCode;

    @ApiModelProperty("本数据元对应的行政区域的名称，开通签收状态服务（见上面\"status\"后的说明）且在订阅接口中提交resultv2标记后才会出现，如：上海市")
    private String areaName;

}
