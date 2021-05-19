/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.dto.user.function;

import lombok.Data;

import java.io.Serializable;

/**
 * @author xuzhch
 * @className UserFunctionInfoDTO
 * @description 用户首页常用功能详情
 * @date 2020/3/17/017
 */
@Data
public class UserFunctionInfoDTO implements Serializable {
    private Long id;
    private Long menuId;
    private String menuName;
    private Integer menuLevel;
}
