/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.service.payment;


import com.leimingtech.commons.tools.page.PageData;
import com.leimingtech.modules.dto.payment.WithdrawalApplicationDTO;
import com.leimingtech.modules.dto.payment.WithdrawalAuditDTO;
import com.leimingtech.modules.dto.payment.WithdrawalDetailDTO;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

/**
 * 提现申请
 *
 * @author huangkeyuan huangkeyuan@leimingtech.com
 * @since v1.0.0 2020-10-19
 */

public interface WithdrawalApplicationService {


    PageData<WithdrawalApplicationDTO> page(@RequestParam Map<String, Object> params);


    List<WithdrawalApplicationDTO> list(@RequestParam Map<String, Object> params);


    WithdrawalApplicationDTO get(Long id);


    void save(@RequestBody WithdrawalApplicationDTO dto);


    void update(@RequestBody WithdrawalApplicationDTO dto);


    void delete(@RequestBody Long[] ids);

    /**
     * 提现申请审核
     *
     * @param dto
     * @return
     * @date 2020-10-20 16:25
     * @author huangkeyuan@leimingtech.com
     **/

    void audit(@RequestBody WithdrawalAuditDTO dto);

    /**
     * 提现发放审核列表
     *
     * @param params
     * @return
     * @date 2020-10-20 17:55
     * @author huangkeyuan@leimingtech.com
     **/

    PageData<WithdrawalApplicationDTO> issuePage(@RequestParam Map<String, Object> params);

    /**
     * 提现发放审核
     *
     * @param dto
     * @return
     * @date 2020-10-20 16:25
     * @author huangkeyuan@leimingtech.com
     **/

    void issue(@RequestBody WithdrawalAuditDTO dto);

    /**
     * 提现记录列表
     *
     * @param params
     * @return
     * @date 2020-10-20 17:56
     * @author huangkeyuan@leimingtech.com
     **/

    PageData<WithdrawalApplicationDTO> recordPage(@RequestParam Map<String, Object> params);

    /**
     * 获取用户提现明细
     *
     * @param params
     * @return
     * @date 2020-10-22 16:37
     * @author huangkeyuan@leimingtech.com
     **/

    PageData<WithdrawalApplicationDTO> memberList(@RequestParam Map<String, Object> params);

    /**
     * 根据申请id和用户id查询申请详情
     *
     * @param params
     * @return
     * @date 2020-10-22 17:01
     * @author huangkeyuan@leimingtech.com
     **/

    WithdrawalDetailDTO detail(@RequestParam Map<String, Object> params);

    /**
     * 取消提现申请
     *
     * @param params
     * @return
     * @date 2020-10-22 17:32
     * @author huangkeyuan@leimingtech.com
     **/

    String cancel(@RequestParam Map<String, Object> params);

    /**
     * 用户申请提现（h5）
     *
     * @param params
     * @return
     * @date 2020-10-28 17:42
     * @author huangkeyuan@leimingtech.com
     **/

    Map<String, Object> withdrawalApply(@RequestParam Map<String, Object> params);
}
