/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.service.navigation;


import com.leimingtech.commons.tools.page.PageData;
import com.leimingtech.modules.dto.navigation.AddNavigationDTO;
import com.leimingtech.modules.dto.navigation.NavigationDTO;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

/**
 * 首页导航设置
 *
 * @author chengqian chengqian@leimingtech.com
 * @since v1.0.0 2020-06-11
 */

public interface NavigationService {
    /**
     * 分类查询
     *
     * @param params
     * @return
     */

    PageData<NavigationDTO> page(@RequestParam Map<String, Object> params);

    /**
     * 查询所有导航
     *
     * @param params
     * @return
     */

    List<NavigationDTO> list(@RequestParam Map<String, Object> params);

    /**
     * 导航详情
     *
     * @param id
     * @return
     */

    NavigationDTO get(Long id);

    /**
     * 保存导航配置
     *
     * @param dto
     */

    void save(@RequestBody AddNavigationDTO dto);

    /**
     * 修改导航
     *
     * @param dto
     */

    void update(@RequestBody NavigationDTO dto);

    /**
     * 删除导航
     *
     * @param ids
     */

    void delete(@RequestBody Long[] ids);
}