/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.service.invoice;


import com.leimingtech.commons.tools.page.PageData;
import com.leimingtech.modules.dto.invoice.InvoiceInfoDTO;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

/**
 * 发票信息表
 *
 * @author xuzhch x170518@163.com
 * @since v1.0.0 2020-03-30
 */

public interface InvoiceInfoService {


    PageData<InvoiceInfoDTO> page(@RequestParam Map<String, Object> params);


    List<InvoiceInfoDTO> list(@RequestParam Map<String, Object> params);


    InvoiceInfoDTO get(Long id);


    void save(@RequestBody InvoiceInfoDTO dto);


    void update(@RequestBody InvoiceInfoDTO dto);


    void delete(@RequestBody Long[] ids);


    void saveBatch(@RequestBody List<InvoiceInfoDTO> invoiceInfoDTOS);


    List<InvoiceInfoDTO> getDetailInvoiceID(Long invoiceId);


    List<Long> selectGoodsIdByInvoiceId(Long id);
}
