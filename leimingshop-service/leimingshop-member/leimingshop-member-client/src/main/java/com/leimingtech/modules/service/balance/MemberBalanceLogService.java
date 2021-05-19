/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.service.balance;


import com.leimingtech.commons.tools.page.PageData;
import com.leimingtech.modules.dto.balance.MemberBalanceLogDTO;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

/**
 * 用户余额明细
 *
 * @author xuzhch kuangweiguo@leimingtech.com
 * @since v1.0.0 2020-07-07
 */

public interface MemberBalanceLogService {

    /**
     * 余额日志查询分页
     *
     * @param params 查询条件
     * @return 查询结果
     * @author xuzhch
     * @date 2020年09月18日
     */

    PageData<MemberBalanceLogDTO> page(@RequestParam Map<String, Object> params);

    /**
     * 查询余额日志列表
     *
     * @param params 查询条件
     * @return list 列表数据
     * @author xuzhch
     * @date 2020年09月18日
     */

    List<MemberBalanceLogDTO> list(@RequestParam Map<String, Object> params);

    /**
     * 根据余额记录ID用户余额记录
     *
     * @param id 余额记录ID
     * @return 用户余额记录
     * @author xuzhch
     * @date 2020年09月18日
     */

    MemberBalanceLogDTO get(Long id);

    /**
     * 保存用户余额日志
     *
     * @param dto 用户余额明细
     * @author xuzhch
     * @date 2020年09月18日
     */

    void save(@RequestBody MemberBalanceLogDTO dto);

    /**
     * 修改用户余额日志 .
     *
     * @param dto 用户余额明细
     * @author xuzhch
     * @date 2020年09月18日
     */

    void update(@RequestBody MemberBalanceLogDTO dto);

    /**
     * 删除用户余额日志
     *
     * @param ids 余额记录ID数组
     * @author xuzhch
     * @date 2020年09月18日
     */

    void delete(@RequestBody Long[] ids);

    /**
     * 批量保存用户余额日志
     *
     * @param balanceLogDTOList 用户余额明细集合
     * @author xuzhch
     * @date 2020年09月18日
     */

    void updateBatch(@RequestBody List<MemberBalanceLogDTO> balanceLogDTOList);
}
