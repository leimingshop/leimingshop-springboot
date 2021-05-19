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
 * @Description: 快递100订阅推送数据
 * @Date: 2019年7月31日
 * @Version: V1.0
 */
@Data
@ToString
@ApiModel(description = "SubsrcibeResponseDTO")
public class SubsrcibeResponseDTO implements Serializable {

    @ApiModelProperty("监控状态:polling:监控中，shutdown:结束，abort:中止，updateall：重新推送。其中当快递单为已签收时status=shutdown，当message为“3天查询无记录”或“60天无变化时”status= abort ，对于stuatus=abort的状度，需要增加额外的处理逻辑")
    private String status;

    @ApiModelProperty("监控状态相关消息，如:3天查询无记录，60天无变化")
    private String message;

    @ApiModelProperty("快递公司编码是否出错")
    private String autoCheck;

    @ApiModelProperty("贵司提交的原始的快递公司编码。详细见autoCheck后说明。若开启了国际版（即在订阅请求中增加字段interCom=1），则回调请求中暂无此字段")
    private String comOld;

    @ApiModelProperty("我司纠正后的新的快递公司编码。详细见autoCheck后说明。若开启了国际版（即在订阅请求中增加字段interCom=1），则回调请求中暂无此字段")
    private String comNew;

    @ApiModelProperty("最新查询结果，若在订阅报文中通过interCom字段开通了国际版，则此lastResult表示出发国的查询结果，全量，倒序（即时间最新的在最前）")
    private RealTimeQueryResponseDTO lastResult;

    @ApiModelProperty("返回数据")
    private List<RealTimeQueryResponseDataDTO> data;


}
