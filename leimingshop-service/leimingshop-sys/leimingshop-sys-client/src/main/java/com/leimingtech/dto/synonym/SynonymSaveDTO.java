/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.dto.synonym;

import com.leimingtech.commons.tools.validator.group.AddGroup;
import com.leimingtech.commons.tools.validator.group.UpdateGroup;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import java.io.Serializable;


/**
 * 同义词管理DTO
 *
 * @author lixiangx@leimingtech.com
 * @date 2019/12/10 10:39
 **/
@Data
@ApiModel(description = "SynonymSaveDTO")
public class SynonymSaveDTO implements Serializable {

    private static final long serialVersionUID = 5009176975623417499L;

    /*
    @NotNull    验证对象是否不为null, 无法查检长度为0的字符串
    @NotBlank 检查约束 (字符串) 是不是Null还有被Trim的长度是否大于0,只对字符串,且会去掉前后空格.
    @NotEmpty 检查(集合)约束元素是否为NULL或者是EMPTY.
*/
    @NotNull(message = "编辑主键ID不能为空", groups = UpdateGroup.class)
    @Null(message = "新增主键ID必须为空", groups = AddGroup.class)
    @ApiModelProperty(value = "主键")
    private Long id;

    @NotBlank(message = "词库名称不能为空", groups = {UpdateGroup.class, AddGroup.class})
    @ApiModelProperty(value = "词库名称")
    private String name;

    @ApiModelProperty(value = "0:停用，1：启用")
    private Integer state;
}