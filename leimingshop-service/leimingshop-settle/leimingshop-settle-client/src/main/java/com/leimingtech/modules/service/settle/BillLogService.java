/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.service.settle;


import com.leimingtech.commons.tools.page.PageData;
import com.leimingtech.modules.dto.settle.BillLogDTO;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

/**
 * 对账操作记录
 *
 * @author chengqian chengqian@leimingtech.com
 * @since v1.0.0 2019-12-27
 */

public interface BillLogService {
    /**
     * 分页查询操作记录
     *
     * @param params 查询参数
     * @return 返回操作记录分页信息
     */

    PageData<BillLogDTO> page(@RequestParam Map<String, Object> params);

    /**
     * 查询操作记录
     *
     * @param params 查询条件
     * @return 返回操作记录
     */

    List<BillLogDTO> list(@RequestParam Map<String, Object> params);

    /**
     * 根据ID获取信息
     *
     * @param id 主键id
     * @return 返回操作详情
     */

    BillLogDTO get(Long id);

    /**
     * 保存操作记录
     *
     * @param dto 保存实体
     */

    void save(@RequestBody BillLogDTO dto);

    /**
     * 修改操作记录
     *
     * @param dto
     */

    void update(@RequestBody BillLogDTO dto);

    /**
     * 删除操作记录
     *
     * @param ids
     */

    void delete(@RequestBody Long[] ids);

}