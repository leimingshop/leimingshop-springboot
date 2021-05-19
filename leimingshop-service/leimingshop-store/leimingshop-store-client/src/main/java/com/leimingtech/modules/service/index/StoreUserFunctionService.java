/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.service.index;


import com.leimingtech.commons.tools.page.PageData;
import com.leimingtech.modules.dto.index.StoreUserFunctionDTO;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

/**
 * 用户首页按钮配置
 *
 * @author chengqian chengqian@leimingtech.com
 * @since v1.0.0 2020-03-16
 */

public interface StoreUserFunctionService {
    /**
     * 分页查询
     *
     * @param params
     * @return
     */

    PageData<StoreUserFunctionDTO> page(@RequestParam Map<String, Object> params);

    /**
     * 条件查询相关信息
     *
     * @param params
     * @return
     */

    List<StoreUserFunctionDTO> list(@RequestParam Map<String, Object> params);

    /**
     * 获取信息
     *
     * @param id
     * @return
     */

    StoreUserFunctionDTO get(Long id);

    /**
     * 保存
     *
     * @param dto
     */

    void save(@RequestBody StoreUserFunctionDTO dto);

    /**
     * 更新
     *
     * @param dto
     */

    void update(@RequestBody StoreUserFunctionDTO dto);

    /**
     * 删除
     *
     * @param ids
     */

    void delete(@RequestBody Long[] ids);

    /**
     * 批量保存首页菜单按钮
     *
     * @param userId  用户id
     * @param menuIds 菜单资源id
     */

    void saveBatch(@RequestParam("userId") Long userId, @RequestBody List<Long> menuIds);

}