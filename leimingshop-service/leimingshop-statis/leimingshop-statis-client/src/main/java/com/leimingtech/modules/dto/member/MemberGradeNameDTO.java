/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dto.member;

import lombok.Data;

import java.io.Serializable;

/**
 * 会员等级名称
 *
 * @author xuzhch
 * @date 2020年9月17日
 */
@Data
public class MemberGradeNameDTO implements Serializable {
    private Long gradeId;
    private Integer integral;
    private String gradeName;
    private Integer start;
    private Integer end;
}
