/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.service.transport;


import com.leimingtech.commons.tools.page.PageData;
import com.leimingtech.modules.dto.transport.TransportCompanyDTO;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

/**
 * 物流公司表
 *
 * @author kuangweiguo kuangweiguo@leimingtech.com
 * @since v1.0.0 2019-08-13
 */

public interface TransportCompanyService {

    /**
     * 分页查询物流公司列表
     *
     * @param params 查询条件
     * @return 分页查询结果
     * @author xuzhch
     * @date 2020年9月17日
     */

    PageData<TransportCompanyDTO> page(@RequestParam Map<String, Object> params);

    /**
     * 查询物流公司列表
     *
     * @param params 查询条件
     * @return 查询结果
     * @author xuzhch
     * @date 2020年9月17日
     */

    List<TransportCompanyDTO> list(@RequestParam Map<String, Object> params);

    /**
     * 根据ID查询物流公司信息
     *
     * @param id 物流公司表ID
     * @return 查询结果
     * @author xuzhch
     * @date 2020年9月17日
     */

    TransportCompanyDTO get(Long id);

    /**
     * 批量修改物流公司信息
     *
     * @param dtoList 被修改的物流公司信息
     * @author xuzhch
     * @date 2020年9月17日
     */

    void updateBatchByCompanyId(@RequestBody List<TransportCompanyDTO> dtoList);
}
