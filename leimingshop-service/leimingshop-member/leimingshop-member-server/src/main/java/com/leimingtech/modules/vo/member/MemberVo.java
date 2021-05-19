/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.vo.member;

import com.leimingtech.modules.entity.member.MemberEntity;
import com.leimingtech.modules.entity.member.MemberInfoEntity;
import lombok.Data;

/**
 * @ClassName memberVo
 * @Description
 * @Author DY
 * @Date 2019/5/10 11:18
 * @Version 1.0
 **/
@Data
public class MemberVo extends MemberEntity {

    /**
     * 会员详情
     */
    MemberInfoEntity memberInfoEntity;
}
