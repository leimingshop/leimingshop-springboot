/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.service.payment;


import com.leimingtech.commons.tools.page.PageData;
import com.leimingtech.modules.dto.payment.WithdrawalApplicationLogDTO;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

/**
 * 提现日志表
 *
 * @author huangkeyuan huangkeyuan@leimingtech.com
 * @since v1.0.0 2020-10-19
 */

public interface WithdrawalApplicationLogService {


    PageData<WithdrawalApplicationLogDTO> page(@RequestParam Map<String, Object> params);


    List<WithdrawalApplicationLogDTO> list(@RequestParam Map<String, Object> params);


    WithdrawalApplicationLogDTO get(Long id);


    void save(@RequestBody WithdrawalApplicationLogDTO dto);


    void update(@RequestBody WithdrawalApplicationLogDTO dto);


    void delete(@RequestBody Long[] ids);
}
