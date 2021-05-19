/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.service.order;


import com.leimingtech.commons.tools.page.PageData;
import com.leimingtech.modules.dto.order.OrderVerifyCodeDTO;
import com.leimingtech.modules.dto.virtual.FetchCodeDTO;
import com.leimingtech.modules.dto.virtual.FetchCodeDetailDTO;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

/**
 * 电子提货码
 *
 * @author keyuan huangkeyuan@leimingtech.com
 * @since v1.0.0 2020-04-29
 */

public interface FetchCodeService {


    PageData<FetchCodeDTO> page(@RequestParam Map<String, Object> params);


    List<FetchCodeDTO> list(@RequestParam Map<String, Object> params);


    FetchCodeDTO get(Long id);

    /**
     * seller查询电子提货码详情
     *
     * @param id
     * @return
     * @date 2020-04-29 15:28
     * @author huangkeyuan@leimingtech.com
     **/

    FetchCodeDetailDTO sellerDetail(@RequestParam(value = "id", required = false) Long id, @RequestParam(value = "fetchCode", required = false) String fetchCode, @RequestParam(value = "storeId", required = false) Long storeId);


    void save(@RequestBody FetchCodeDTO dto);


    void update(@RequestBody FetchCodeDTO dto);


    void delete(@RequestBody Long[] ids);

    /**
     * 确认核销
     *
     * @param dto
     * @return
     * @date 2020-04-30 09:49
     * @author huangkeyuan@leimingtech.com
     **/

    void verifyCode(@RequestBody OrderVerifyCodeDTO dto);

    /**
     * 电子提货码过期定时
     *
     * @return
     * @date 2020-04-30 15:22
     * @author huangkeyuan@leimingtech.com
     **/

    void codeExpireTiming();
}
