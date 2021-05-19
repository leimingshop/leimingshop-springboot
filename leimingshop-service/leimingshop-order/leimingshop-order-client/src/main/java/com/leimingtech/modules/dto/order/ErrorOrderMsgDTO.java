/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dto.order;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;

/**
 * 提交订单 异常订单提示类
 *
 * @author lixiang
 * @version V1.0
 * @date 2020/4/1 11:57
 **/
@Data
@ToString
@ApiModel("异常订单提示类")
public class ErrorOrderMsgDTO implements Serializable {
    private static final long serialVersionUID = 1635577499687117056L;


    @ApiModelProperty("错误提示标题")
    private String errorMsgTitle;

    @ApiModelProperty(value = "校验未通过的商品")
    private List<ErrorGoodsMsgDTO> errorGoodsMsgDTOList;
}
