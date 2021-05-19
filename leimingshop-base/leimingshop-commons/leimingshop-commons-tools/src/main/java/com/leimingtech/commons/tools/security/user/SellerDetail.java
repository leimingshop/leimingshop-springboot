/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.commons.tools.security.user;

import com.leimingtech.commons.tools.security.bo.ResourceBO;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @Author: lixiang
 * @Description: Seller登录用户信息
 * @Date: 2019/6/26 14:54
 * @Version: V1.0
 */
@Data
public class SellerDetail implements Serializable {

    private static final long serialVersionUID = 8628341772498364793L;

    /**
     * 主键
     */
    private Long id;

    /**
     * 店铺ID
     */
    private Long storeId;

    /**
     * 登陆账号
     */
    private String account;

    /**
     * 登陆密码
     */
    private String password;

    /**
     * 昵称
     */
    private String nickname;

    /**
     * 性别（默认0:保密，1:男，2:女）
     */
    private Integer sex;

    /**
     * 手机号
     */
    private String mobilePhone;

    /**
     * 角色ID
     */
    private String roleId;

    /**
     * 是否被管理员踢出   0：正常   1：被踢出，无权调用接口
     */
    private int kill;

    /**
     * 是否为超级管理员 0否 1是
     */
    private Integer superAdmin;

    /**
     * 用户资源列表
     */
    private List<ResourceBO> resourceList;

    /**
     * 启用禁用(0 启用，1 禁用)
     */
    private Integer isEnable;
}