/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dto.log.point;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

/**
 * 会员成长值DTO
 *
 * @author lixiang
 * @version V1.0
 * @date 2019/12/26 10:17
 **/
@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class MemberGrowthValueDTO implements Serializable {

    private static final long serialVersionUID = 4053482439816471521L;

    /**
     * 会员ID
     */
    private Long memberId;

    /**
     * 成长值总和
     */
    private Integer growthValue;
}
