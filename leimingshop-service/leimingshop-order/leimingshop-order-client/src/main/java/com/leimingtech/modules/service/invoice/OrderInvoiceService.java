/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.service.invoice;


import com.leimingtech.commons.tools.page.PageData;
import com.leimingtech.modules.dto.invoice.*;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 订单发票表
 *
 * @author xuzhch x170518@163.com
 * @since v1.0.0 2020-03-30
 */

public interface OrderInvoiceService {


    PageData<OrderInvoiceDTO> page(@RequestParam Map<String, Object> params);


    List<OrderInvoiceDTO> list(@RequestParam Map<String, Object> params);


    OrderInvoiceDTO get(Long id);


    void save(@RequestBody OrderInvoiceDTO dto);


    void update(@RequestBody OrderInvoiceDTO dto);


    void delete(@RequestBody Long[] ids);


    void saveBatch(@RequestBody List<OrderInvoiceSaveDTO> orderInvoiceSaveDTOS);


    List<OrderInvoiceDTO> selectOrderInvoiceList(Long orderConfirmId);


    void updateBatch(@RequestBody List<OrderInvoiceDTO> orderInvoiceDTOS);


    void makeInvoice(@RequestBody OrderInvoiceDTO dto);


    H5InvoiceDetailDTO h5InvoiceDetail(Long orderId);


    OrderInvoiceDTO getOrderInvoiceDTO(Long orderId);


    void applyInvoice(@RequestBody ApplyOrChangeInvoiceDTO dto);


    void changeInvoice(@RequestBody ApplyOrChangeInvoiceDTO dto);


    CanInvoiceDTO checkApplyInvoice(Long orderId);


    OrderInvoiceDetailDTO detail(Long id);


    void sendEmail(@RequestParam Map<String, String> params);

    /**
     * 检查是否可以开票或者换开
     *
     * @param type         0 开票  1 换开
     * @param completeTime 订单完成时间
     */

    Boolean check(@RequestParam("type") Integer type, @RequestParam("completeTime") Date completeTime);
}
