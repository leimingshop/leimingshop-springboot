/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.service.stock;


import com.leimingtech.commons.tools.page.PageData;
import com.leimingtech.modules.dto.stock.SaveStockLogDTO;
import com.leimingtech.modules.dto.stock.StockLogDTO;
import com.leimingtech.modules.dto.stock.UpdataStockLogDTO;
import com.leimingtech.modules.dto.stock.UpdateBatchStockLog;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;


/**
 * 库存修改记录管理
 *
 * @author chengqian
 * @email 543826372@qq.com
 * @since 1.0.0 2019-05-31
 */

public interface StockLogService {

    /**
     * 分页查询
     *
     * @param params 分页参数
     * @return
     */

    PageData<StockLogDTO> page(@RequestParam Map<String, Object> params);

    /**
     * 根据ID获取信息
     *
     * @param id 主键ID
     * @return
     */

    StockLogDTO get(@RequestParam("id") Long id);

    /**
     * 保存价格修改记录
     *
     * @param dto 参数实体
     */

    void save(@RequestBody SaveStockLogDTO dto);

    /**
     * 修改价格记录
     *
     * @param dto 参数实体
     */

    void update(@RequestBody UpdataStockLogDTO dto);

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

    List<StockLogDTO> list(@RequestParam Map<String, Object> params);

    /**
     * 批量保存数据
     *
     * @param updateBatchStockLog 参数
     */

    void insertBatch(@RequestBody UpdateBatchStockLog updateBatchStockLog);

    /**
     * 导出查询列表
     *
     * @param params 查询参数
     * @return 返回导出信息
     * @Author weixianchun
     * @Description 导出查询列表
     * @Date 2019/12/5 9:56
     * @version 1.0
     */

    List<StockLogDTO> selectLogExport(@RequestParam Map<String, Object> params);
}
