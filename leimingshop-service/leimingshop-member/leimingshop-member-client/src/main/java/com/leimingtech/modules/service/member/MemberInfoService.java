/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.service.member;


import com.leimingtech.modules.dto.balance.BatchChangeBalanceDTO;
import com.leimingtech.modules.dto.balance.MemberChangeBalanceDTO;
import com.leimingtech.modules.dto.member.MemberBalanceInfoDTO;
import com.leimingtech.modules.dto.member.MemberInfoDTO;
import com.leimingtech.modules.dto.member.MemberPayPasswordInfoDTO;
import com.leimingtech.modules.dto.member.MemberUpdateDTO;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

/**
 * 会员详细信息表
 *
 * @author DY
 * @email 1197793912@qq.com
 * @since 1.0.0 2019-05-10
 */

public interface MemberInfoService {

    /**
     * 修改用户详情
     *
     * @param memberUpdateDTO 用户信息
     * @author xuzhch
     * @date 2020年09月18日
     */

    void updateByMemberId(@RequestBody MemberUpdateDTO memberUpdateDTO);

    /**
     * 根据id查询用户详情
     *
     * @param id
     * @return
     */

    MemberInfoDTO get(Long id);

    /**
     * 保存用户详情
     *
     * @param memberInfoDTO
     */

    void save(@RequestBody MemberInfoDTO memberInfoDTO);

    /**
     * 修改用户详情
     *
     * @param memberInfoDTO
     */

    void update(@RequestBody MemberInfoDTO memberInfoDTO);

    /**
     * 逻辑删除
     *
     * @param ids
     */

    void logicDelete(@RequestBody Long[] ids);

    /**
     * 根据等级积分查询相应人数
     *
     * @param min 最小
     * @param max 最大
     * @return
     */

    Long selectMemberNum(@RequestParam("min") Integer min, @RequestParam("max") Integer max);

    /**
     * 修改会员成长值
     *
     * @param memberId:   会员ID
     * @param gradePoint: 会员等级积分（成长值）
     * @date 2019/12/26 10:34
     * @author lixiangx@leimingtech.com
     **/

    void updateGradePoint(@RequestParam("memberId") Long memberId,
                          @RequestParam("gradePoint") Integer gradePoint);

    /**
     * 查询用户余额信息
     *
     * @param memberId 用户ID
     * @return MemberBalanceInfoDTO 用户余额信息
     * @date 2020年7月7日15:57:25
     * @author xuzhch@leimingtech.com
     **/

    MemberBalanceInfoDTO getMemberBalanceInfo(Long memberId);

    /**
     * 修改用户支付密码
     *
     * @param params 用户密码信息
     * @date 2020年7月7日15:57:25
     * @author xuzhch@leimingtech.com
     **/

    void updatePayPassword(@RequestParam Map<String, Object> params);

    /**
     * 根据用户DI查询用户附加信息
     *
     * @param memberId 用户ID
     * @return member info dto
     * @author xuzhch
     * @date 2020年09月18日
     */

    MemberInfoDTO selectInfoBymemberId(Long memberId);

    /**
     * 校验支付密码是否正确
     *
     * @param params 用户ID，支付密码
     * @return 用户支付信息
     * @author xuzhch
     * @date 2020年09月18日
     */

    MemberPayPasswordInfoDTO checkPayPassword(@RequestParam Map<String, Object> params);

    /**
     * 根据用户ID集合查询用户附加信息列表
     *
     * @param memberIds 用户ID集合
     * @return list 用户附加信息列表
     * @author xuzhch
     * @date 2020年09月18日
     */

    List<MemberInfoDTO> selectList(@RequestBody List<Long> memberIds);

    /**
     * 变更用户余额
     *
     * @param memberChangeBalanceDTO 用户余额变更明细
     * @author xuzhch
     * @date 2020年09月18日
     */

    void changeMemberBalance(@RequestBody MemberChangeBalanceDTO memberChangeBalanceDTO);

    /**
     * 批量变更用户余额
     *
     * @param batchChangeBalanceDTO 批量变更用户余额信息
     * @author xuzhch
     * @date 2020年09月18日
     */

    void batchChangeBalance(@RequestBody BatchChangeBalanceDTO batchChangeBalanceDTO);

    /**
     * 修改用户地区信息
     *
     * @param memberUpdateDTO 用户信息
     * @author xuzhch
     * @date 2020年09月18日
     */

    void updateMemberBaseInfo(@RequestBody MemberUpdateDTO memberUpdateDTO);
}
