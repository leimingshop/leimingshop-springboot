/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dto.goodslable;

import com.leimingtech.commons.tools.validator.group.AddGroup;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;


/**
 * @Author: weixianchun
 * @Description: 商品标签管理DTO (保存用)
 * @Date :2019/5/20 14:27
 * @Version V1.0
 **/
@Data
@ApiModel(description = "GoodsLabelSaveDTO")
public class GoodsLabelSaveDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 商品标签名
     */
    @Length(min = 1, max = 5, message = "商品标签名长度必须介于1到5之间", groups = AddGroup.class)
    @NotBlank(message = "商品标签名不能为空", groups = AddGroup.class)
    @ApiModelProperty(value = "商品标签名")
    private String labelName;

    @ApiModelProperty(value = "关联的分组ID")
    private Long[] groupIds;


}
