/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dto.member;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @Author tyl
 * @Date 2019/6/3 15:36
 * @Description
 **/


@Data
@ApiModel(description = "SecurityDTO")
public class SecurityDTO implements Serializable {


    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "id", required = true)
    private String id;

    @ApiModelProperty(value = "认证用户密码", required = true)
    private String password;

    @ApiModelProperty(value = "认证用户名", required = true)
    private String username;


    @ApiModelProperty(value = "认证应用标识", required = true)
    private String clientId;

    @ApiModelProperty(value = "用户是否禁用")
    private Integer memberState;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


}
