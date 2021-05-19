/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dto.log.point;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

/**
 * 会员成长值详情DTO
 *
 * @author lixiang
 * @version V1.0
 * @date 2019/12/26 10:17
 **/
@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class GrowthValueDetailDTO implements Serializable {

    private static final long serialVersionUID = 4497428873804484326L;

    @ApiModelProperty(value = "用户名")
    private String memberName;

    @ApiModelProperty(value = "用户等级名称")
    private String gradeName;

    @ApiModelProperty(value = "昵称")
    private String nickName;

    @ApiModelProperty(value = "成长值")
    private Integer growthValue;
}
