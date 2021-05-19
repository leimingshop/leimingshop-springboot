/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.service.invoice;


import com.leimingtech.commons.tools.page.PageData;
import com.leimingtech.modules.dto.invoice.MemberInvoiceDTO;
import com.leimingtech.modules.dto.invoice.MemberInvoiceNameDTO;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

/**
 * 用户发票表
 *
 * @author xuzhch x170518@163.com
 * @since v1.0.0 2020-03-26
 */

public interface MemberInvoiceService {

    /**
     * 分页查询发票信息列表
     *
     * @param params 查询条件
     * @return 发票信息列表分页数据
     * @author xuzhch
     * @date 2020年09月18日
     */

    PageData<MemberInvoiceDTO> page(@RequestParam Map<String, Object> params);

    /**
     * 查询发票信息列表
     *
     * @param params 查询条件
     * @return list 发票信息列表
     * @author xuzhch
     * @date 2020年09月18日
     */

    List<MemberInvoiceDTO> list(@RequestParam Map<String, Object> params);

    /**
     * 根据发票信息ID获取用户发票信息
     *
     * @param id 发票信息ID
     * @return 用户发票信息
     * @author xuzhch
     * @date 2020年09月18日
     */

    MemberInvoiceDTO get(Long id);

    /**
     * 保存发票信息
     *
     * @param dto 用户发票信息
     * @author xuzhch
     * @date 2020年09月18日
     */

    void save(@RequestBody MemberInvoiceDTO dto);

    /**
     * 修改发票信息
     *
     * @param dto 用户发票信息
     * @author xuzhch
     * @date 2020年09月18日
     */

    void update(@RequestBody MemberInvoiceDTO dto);

    /**
     * 根据发票ID数组删除发票信息
     *
     * @param ids 发票ID数组
     * @author xuzhch
     * @date 2020年09月18日
     */

    void delete(@RequestBody Long[] ids);

    /**
     * 获取发票抬头名称列表
     *
     * @param memberId 用户ID
     * @return 发票抬头名称列表
     * @author xuzhch
     * @date 2020年09月18日
     */

    List<MemberInvoiceNameDTO> getInvoiceNames(Long memberId);

    /**
     * 保存或修改发票信息
     *
     * @param dto 用户发票信息
     * @author xuzhch
     * @date 2020年09月18日
     */

    void saveOrUpdate(@RequestBody MemberInvoiceDTO dto);

    /**
     * 获取默认发票信息
     *
     * @param memberId 用户ID
     * @return 默认发票信息
     * @author xuzhch
     * @date 2020年09月18日
     */

    MemberInvoiceDTO getDefaultInvoice(Long memberId);

    /**
     * 修改用户发票信息
     *
     * @param dto 用户发票信息
     * @author xuzhch
     * @date 2020年09月18日
     */

    void changeInvoiceInfo(@RequestBody MemberInvoiceDTO dto);

    /**
     * 根据发票表ID设置为默认发票
     *
     * @param invoiceId 发票表ID
     * @param memberId  用户ID
     * @author xuzhch
     * @date 2020年09月18日
     */

    void settingDefault(@RequestParam("invoiceId") Long invoiceId, @RequestParam("memberId") Long memberId);
}
