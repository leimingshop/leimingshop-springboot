/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.service.reason;


import com.leimingtech.commons.tools.page.PageData;
import com.leimingtech.dto.reason.ReasonDescriptionDTO;
import com.leimingtech.dto.reason.ReasonDescriptionSaveDTO;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

/**
 * 原因描述
 *
 * @author huangkeyuan
 * @email huangkeyuan@leimingtech.com
 * @since 1.0.0 2019-06-10
 */

public interface ReasonDescriptionService {
    /**
     * 保存原因描述
     *
     * @param dto
     */

    void save(@RequestBody ReasonDescriptionSaveDTO dto);

    /**
     * 修改原因描述
     *
     * @param dto
     */

    void update(@RequestBody ReasonDescriptionDTO dto);

    /**
     * 删除原因描述
     *
     * @param ids
     */

    void delete(@RequestBody Long[] ids);

    /**
     * 根据ID查询原因描述
     *
     * @param id
     * @return
     */

    ReasonDescriptionDTO get(Long id);

    /**
     * @return java.util.List<com.leimingtech.dto.reason.ReasonDescriptionDTO>
     * @Description 查询所有的原因描述列表
     * @Param * @param params:
     * @Author huangkeyuan
     * @Date 18:13 2019-06-10
     */

    List<ReasonDescriptionDTO> list(@RequestParam Map<String, Object> params);

    /**
     * @return com.leimingtech.commons.tools.page.PageData<com.leimingtech.dto.reason.ReasonDescriptionDTO>
     * @Description 分页查询所有的原因描述列表
     * @Param * @param params:
     * @Author huangkeyuan
     * @Date 13:40 2019-06-11
     */

    PageData<ReasonDescriptionDTO> page(@RequestParam Map<String, Object> params);
}