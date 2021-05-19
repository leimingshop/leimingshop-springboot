/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.service.order;


import com.leimingtech.commons.tools.page.PageData;
import com.leimingtech.modules.dto.virtual.VerifyRecordDTO;
import com.leimingtech.modules.dto.virtual.VerifyRecordDetailDTO;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

/**
 * 核销记录
 *
 * @author keyuan huangkeyuan@leimingtech.com
 * @since v1.0.0 2020-04-29
 */

public interface VerifyRecordService {


    PageData<VerifyRecordDTO> page(@RequestParam Map<String, Object> params);


    List<VerifyRecordDTO> list(@RequestParam Map<String, Object> params);


    VerifyRecordDTO get(Long id);


    void save(@RequestBody VerifyRecordDTO dto);


    void update(@RequestBody VerifyRecordDTO dto);


    void delete(@RequestBody Long[] ids);

    /**
     * 查询核销记录详情
     *
     * @param id      核销记录id
     * @param storeId 店铺id
     * @return
     * @date 2020-04-29 15:48
     * @author huangkeyuan@leimingtech.com
     **/

    VerifyRecordDetailDTO sellerDetail(Long id, @RequestParam(value = "storeId", required = false) Long storeId);
}
