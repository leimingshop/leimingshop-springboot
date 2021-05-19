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
 * @Description: 快递100订阅接口入参
 * @Date: 2019年7月31日
 * @Version: V1.0
 */
@Data
@ToString
@ApiModel(description = "SubsrcibeRequestDTO")
public class SubsrcibeRequestDTO implements Serializable {

    @ApiModelProperty("快递100分配给贵司的公司编号（必填）")
    private String customer;

    @ApiModelProperty("快递100分配给贵司的客户授权Key（必填，用于生成签名）")
    private String key;

    @ApiModelProperty("查询的快递公司的编码，一律用小写字母（必填）")
    private String company;

    @ApiModelProperty("查询的快递单号， 单号的最大长度是32个字符（必填）")
    private String number;

    @ApiModelProperty("收件人或寄件人的手机号或固话（顺丰单号必填，也可以填写后四位，如果是固话，请不要上传分机号）")
    private String phone;

    @ApiModelProperty("回调接口的地址。如果需要在推送信息回传自己业务参数，可以在回调地址URL后面拼接上去，如示例中的orderId")
    private String callbackurl;

    @ApiModelProperty("出发地城市，省-市-区（不必填）")
    private String from;

    @ApiModelProperty("目的地城市，省-市-区（不必填）")
    private String to;

    @ApiModelProperty("添加此字段表示开通行政区域解析功能。0：关闭（默认），1：开通行政区域解析功能，2：开通行政解析功能并且返回出发、目的及当前城市信息（不必填）")
    private Integer resultv2;

}
