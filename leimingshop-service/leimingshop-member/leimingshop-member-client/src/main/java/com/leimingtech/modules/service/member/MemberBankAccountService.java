/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.service.member;


import com.leimingtech.commons.tools.page.PageData;
import com.leimingtech.modules.dto.member.MemberBankAccountDTO;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

/**
 * 用户银行账户信息
 *
 * @author huangkeyuan huangkeyuan@leimingtech.com
 * @since v1.0.0 2020-10-21
 */

public interface MemberBankAccountService {


    PageData<MemberBankAccountDTO> page(@RequestParam Map<String, Object> params);


    List<MemberBankAccountDTO> list(@RequestParam Map<String, Object> params);


    MemberBankAccountDTO get(Long id);


    void save(@RequestBody MemberBankAccountDTO dto);


    void update(@RequestBody MemberBankAccountDTO dto);


    void delete(@RequestBody Long[] ids);

    /**
     * 根据银行卡号查询银行名称
     *
     * @param bankAccount 银行卡号
     * @return
     * @date 2020-10-21 16:21
     * @author huangkeyuan@leimingtech.com
     **/

    String getBankName(String bankAccount);


    List<MemberBankAccountDTO> bankList(@RequestParam Map<String, Object> params);
}
