/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dto.member;

import lombok.Data;

import java.io.Serializable;

/**
 * @author xuzhch
 * @className IndexMemberDataDTO
 * @description 首页用户数据
 * @date 2020/3/16/016
 */
@Data
public class IndexMemberDataDTO implements Serializable {
    private Integer newMemberCount;
    private Integer memberCount;
}
