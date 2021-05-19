/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.aftersale.service;


import com.leimingtech.commons.tools.page.PageData;
import com.leimingtech.modules.aftersale.dto.AftersaleRefundRecordsDTO;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

/**
 * @Author: SWH ab4856812@163.com
 * @Description:
 * @Date: 2019/6/23 22:07
 * @Version: V1.0
 */

public interface AftersaleRefundRecordsService {

    /**
     * Page page data .
     *
     * @param params
     * @return page data
     * @author xuzhch
     * @date 2020年09月21日
     */

    PageData<AftersaleRefundRecordsDTO> page(@RequestParam Map<String, Object> params);

    /**
     * List list .
     *
     * @param params
     * @return list
     * @author xuzhch
     * @date 2020年09月21日
     */

    List<AftersaleRefundRecordsDTO> list(@RequestParam Map<String, Object> params);

    /**
     * Get aftersale refund records dto .
     *
     * @param id
     * @return aftersale refund records dto
     * @author xuzhch
     * @date 2020年09月21日
     */

    AftersaleRefundRecordsDTO get(Long id);

    /**
     * 保存退款记录信息
     *
     * @param dto 退款记录信息
     * @return boolean
     * @author xuzhch
     * @date 2020年09月21日
     */

    boolean saveRecords(@RequestBody AftersaleRefundRecordsDTO dto);

    /**
     * 修改退款记录信息
     *
     * @param dto 退款记录信息
     * @author xuzhch
     * @date 2020年09月21日
     */

    void update(@RequestBody AftersaleRefundRecordsDTO dto);

    /**
     * 删除退款记录信息
     *
     * @param ids 退款记录信息表ID数组
     * @author xuzhch
     * @date 2020年09月21日
     */

    void delete(@RequestBody String[] ids);

    /**
     * 查询退款失败售后单
     *
     * @return list 退款失败售后单
     * @author xuzhch
     * @date 2020年09月21日
     */

    List<AftersaleRefundRecordsDTO> findPayFail();
}
