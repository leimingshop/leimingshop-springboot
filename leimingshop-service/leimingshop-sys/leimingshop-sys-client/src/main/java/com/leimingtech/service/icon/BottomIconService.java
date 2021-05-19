/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.service.icon;


import com.leimingtech.commons.tools.page.PageData;
import com.leimingtech.dto.icon.BottomIconDTO;
import com.leimingtech.dto.icon.BottomIconUpdateDTO;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

/**
 * app底部图片配置表
 *
 * @author chengqian
 * @since v1.0.0 2019-12-9
 */

public interface BottomIconService {

    /**
     * 获取分页信息
     *
     * @param params
     * @return
     */

    PageData<BottomIconDTO> page(@RequestParam Map<String, Object> params);

    /**
     * 获取结果集
     *
     * @param params
     * @return
     */

    List<BottomIconDTO> list(@RequestParam Map<String, Object> params);

    /**
     * 获取实体信息
     *
     * @param id
     * @return
     */

    BottomIconDTO get(Long id);


    /**
     * 修改
     *
     * @param dto
     */

    Boolean update(@RequestBody BottomIconUpdateDTO dto);

    /**
     * 删除
     *
     * @param ids
     */

    void delete(@RequestBody Long[] ids);

    /**
     * 查询所有底部icon
     *
     * @return
     */

    List<BottomIconDTO> all();
}
