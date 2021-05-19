/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.service.log.point;


import com.leimingtech.commons.tools.page.PageData;
import com.leimingtech.modules.dto.log.point.PointLogDTO;
import com.leimingtech.modules.dto.log.point.PointLogPackDTO;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;


/**
 * 积分日志Service
 *
 * @author lixiang lixiangx@leimingtech.com
 * @since v1.0.0 2019-12-24
 */

public interface PointLogService {

    /**
     * 分页
     *
     * @param params 查询条件
     * @return PageData 分页结果
     * @author xuzhch
     * @date 2020年09月18日
     */

    PageData<PointLogDTO> page(@RequestParam Map<String, Object> params);

    /**
     * 列表查询
     *
     * @param params 查询条件
     * @return list 查询结果
     * @author xuzhch
     * @date 2020年09月18日
     */

    List<PointLogDTO> list(@RequestParam Map<String, Object> params);

    /**
     * 查询积分日志
     *
     * @param id 积分记录ID
     * @return 积分日志信息
     * @author xuzhch
     * @date 2020年09月18日
     */

    PointLogDTO get(Long id);

    /**
     * 保存积分日志
     *
     * @param dto 积分日志
     * @return boolean
     * @author xuzhch
     * @date 2020年09月18日
     */

    Boolean save(@RequestBody PointLogDTO dto);

    /**
     * 批量保存
     *
     * @param dto : 批量保存集合
     * @return 操作结果
     * @author lixiangx @leimingtech.com
     * @date 2019 /12/24 14:44
     */

    Boolean saveBatch(@RequestBody List<PointLogDTO> dto);

    /**
     * 更新积分记录.
     *
     * @param dto 积分信息
     * @author xuzhch
     * @date 2020年09月18日
     */

    void update(@RequestBody PointLogDTO dto);

    /**
     * 删除积分记录
     *
     * @param ids 积分ID数组
     * @author xuzhch
     * @date 2020年09月18日
     */

    void delete(@RequestBody Long[] ids);

    /**
     * 封装积分/成长值 并发送MQ进行保存、修改用户可用积分数量|成长值
     *
     * @param pointLogPackDTO : 封装积分/成长值DTO
     * @author lixiangx @leimingtech.com
     * @date 2019 /12/24 16:43
     */

    void packPointLog(@RequestBody PointLogPackDTO pointLogPackDTO);

    /**
     * 清除用户成长值（定时任务调用）
     *
     * @return 操作结果
     * @author lixiangx @leimingtech.com
     * @date 2019 /12/25 15:08
     */

    Boolean clearGrowth();

    /**
     * 根据用户id和积分类型查询
     *
     * @param params : 用户id和积分类型
     * @return List 用户积分信息
     * @author xuzhch
     * @date 2020年09月18日
     * @Author huangkeyuan
     * @Date 14:31 2019-12-25
     */

    List<PointLogDTO> queryWithMemberId(@RequestParam Map<String, Object> params);

    /**
     * 根据时间查询日志
     *
     * @param params 查询条件
     * @return
     * @author huangkeyuan @leimingtech.com
     * @date 2020 -01-14 16:29
     */

    List<PointLogDTO> queryByTime(@RequestParam Map<String, Object> params);
}
