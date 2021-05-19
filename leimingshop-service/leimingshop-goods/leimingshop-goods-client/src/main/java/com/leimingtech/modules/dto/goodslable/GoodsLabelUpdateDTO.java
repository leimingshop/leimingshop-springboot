/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dto.goodslable;

import com.leimingtech.commons.tools.validator.group.UpdateGroup;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.io.Serializable;

/**
 * @Author: weixianchun
 * @Description: 商品标签管理(修改实体)
 * @Date :2019/5/20 14:28
 * @Version V1.0
 **/
@Data
@ApiModel(description = "GoodsLabelUpdateDTO")
public class GoodsLabelUpdateDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    private Long id;

    @NotBlank(message = "商品标签名不能为空", groups = UpdateGroup.class)
    @Length(min = 1, max = 5, message = "商品标签名长度必须介于1到5之间", groups = UpdateGroup.class)
    @ApiModelProperty(value = "商品标签名")
    private String labelName;

    @ApiModelProperty(value = "关联的分组ID")
    @NotEmpty(message = "关联的分组ID不能为空", groups = UpdateGroup.class)
    private Long[] groupIds;

}
