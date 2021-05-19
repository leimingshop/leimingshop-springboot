/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dto.member;

import lombok.Data;

/**
 * @author: lishuo
 * @Date: 2020/7/27 22:01
 * @email: lishuo@leimingtech.com
 * @Description: 用户信息
 */
@Data
public class MemberLabelDTO {
    private Integer sex;
    private Integer age;
    private String country;
    private String city;
}