/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.service.order.freight;


import com.leimingtech.commons.tools.page.PageData;
import com.leimingtech.modules.dto.order.freight.FreightRuleDTO;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

/**
 * 运费规则管理
 *
 * @author 刘远杰 2634443725@qq.com
 * @since v1.0.0 2020-04-22
 */

public interface FreightRuleService {


    PageData<FreightRuleDTO> page(@RequestParam Map<String, Object> params);


    List<FreightRuleDTO> list(@RequestParam Map<String, Object> params);


    FreightRuleDTO get(Long id);

    /**
     * 功能描述：
     * <查询店铺运费模板>
     *
     * @param storeId
     * @return
     * @date 2020/4/22 15:41
     * @author 刘远杰
     **/

    FreightRuleDTO getStoreFreightRule(@RequestParam("storeId") Long storeId);


    void save(@RequestBody FreightRuleDTO dto);


    void update(@RequestBody FreightRuleDTO dto);

    /**
     * 功能描述：
     * <修改运费规则>
     *
     * @param dto 运费规则修改实体
     * @return
     * @date 2020/4/22 15:36
     * @author 刘远杰
     **/

    void updateFreightRule(@RequestBody FreightRuleDTO dto);


    void delete(@RequestBody Long[] ids);
}
