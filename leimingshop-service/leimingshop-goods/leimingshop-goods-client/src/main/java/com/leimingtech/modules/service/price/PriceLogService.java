/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.service.price;


import com.leimingtech.commons.tools.page.PageData;
import com.leimingtech.modules.dto.price.PriceLogDTO;
import com.leimingtech.modules.dto.price.SavePriceLogDTO;
import com.leimingtech.modules.dto.price.UpdateBatchPriceLog;
import com.leimingtech.modules.dto.price.UpdatePriceLogDTO;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

/**
 * 价格修改记录管理
 *
 * @author chengqian
 * @email 543826372@qq.com
 * @since 1.0.0 2019-05-31
 */

public interface PriceLogService {
    /**
     * 分页查询
     *
     * @param params 分页参数
     * @return
     */

    PageData<PriceLogDTO> page(@RequestParam Map<String, Object> params);

    /**
     * 根据ID获取信息
     *
     * @param id 主键ID
     * @return
     */

    PriceLogDTO get(Long id);

    /**
     * 保存价格修改记录
     *
     * @param dto 参数实体
     */

    void save(@RequestBody SavePriceLogDTO dto);

    /**
     * 修改价格记录
     *
     * @param dto 参数实体
     */

    void update(@RequestBody UpdatePriceLogDTO dto);

    /**
     * 根据ID删除修改记录
     *
     * @param ids 主键ID
     */

    void delete(@RequestBody Long[] ids);

    /**
     * 查询出修改记录
     *
     * @param params 查询参数
     * @return
     */

    List<PriceLogDTO> list(@RequestParam Map<String, Object> params);

    /**
     * 批量更新数据
     *
     * @param updateBatchPriceLog 参数
     */

    void insertBatch(@RequestBody UpdateBatchPriceLog updateBatchPriceLog);

    /**
     * 导出查询列表
     *
     * @param params 查询参数
     * @return java.util.List<com.leimingtech.modules.dto.price.PriceLogDTO>
     * @Author weixianchun
     * @Description 导出查询列表
     * @Param params
     * @Date 2019/12/5 9:56
     * @version 1.0
     */

    List<PriceLogDTO> selectLogExport(@RequestParam Map<String, Object> params);

}
