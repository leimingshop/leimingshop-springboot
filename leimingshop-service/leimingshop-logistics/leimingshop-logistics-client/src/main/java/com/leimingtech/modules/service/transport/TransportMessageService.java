/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.service.transport;


import com.leimingtech.commons.tools.page.PageData;
import com.leimingtech.modules.dto.transport.TransportFollowDTO;
import com.leimingtech.modules.dto.transport.TransportMessageDTO;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

/**
 * 物流信息表
 *
 * @author kuangweiguo kuangweiguo@leimingtech.com
 * @since v1.0.0 2019-08-13
 */

public interface TransportMessageService {

    /**
     * 分页查询物流信息
     *
     * @param params 查询条件
     * @return 查询结果
     * @author xuzhch
     * @date 2020年9月17日
     */

    PageData<TransportMessageDTO> page(@RequestParam Map<String, Object> params);

    /**
     * 查询物流信息
     *
     * @param params 查询条件
     * @return 查询结果
     * @author xuzhch
     * @date 2020年9月17日
     */

    List<TransportMessageDTO> list(@RequestParam Map<String, Object> params);

    /**
     * 根据ID查询物流信息
     *
     * @param id id
     * @return 查询结果
     * @author xuzhch
     * @date 2020年9月17日
     */

    TransportMessageDTO get(Long id);

    /**
     * 保存物流信息
     *
     * @param dto 物流信息
     * @author xuzhch
     * @date 2020年9月17日
     */

    void save(@RequestBody TransportMessageDTO dto);

    /**
     * 修改物流信息
     *
     * @param dto 物流信息
     * @author xuzhch
     * @date 2020年9月17日
     */

    void update(@RequestBody TransportMessageDTO dto);

    /**
     * 查询正在投递中的物流单号
     *
     * @return 查询结果
     * @author: SWH ab4856812@163.com
     * @date: 2019/8/13 22:10
     * @version: V1.0
     */

    List<TransportMessageDTO> progressList();

    /**
     * 批量修改数据
     *
     * @param transportMessageDTOList 快递信息
     * @return 修改结果
     * @author: SWH ab4856812@163.com
     * @date: 2019/8/14 9:50
     * @version: V1.0
     */

    Boolean updateData(@RequestBody List<TransportMessageDTO> transportMessageDTOList);

    /**
     * 根据物流单号查询物流信息
     *
     * @param logisticsNo 物流单号
     * @return 物流信息状态
     * @author: SWH ab4856812@163.com
     * @date: 2019/8/14 16:23
     * @version: V1.0
     */

    TransportFollowDTO findMongoLogisticsByNu(String logisticsNo);


}
