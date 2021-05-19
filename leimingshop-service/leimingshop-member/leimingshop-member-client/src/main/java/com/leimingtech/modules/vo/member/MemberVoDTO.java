/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.vo.member;

import com.leimingtech.modules.dto.member.MemberDTO;
import com.leimingtech.modules.dto.member.MemberInfoDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @ClassName MemberVoDTO
 * @Description 会员实体vo
 * @Author DY
 * @Date 2019/5/10 11:50
 * @Version 1.0
 **/
@Data
@ApiModel(description = "MemberVoDTO")
public class MemberVoDTO extends MemberDTO {
    /**
     * 会员详情
     */
    @ApiModelProperty(value = "会员详情")
    MemberInfoDTO memberInfoDTO;
}
