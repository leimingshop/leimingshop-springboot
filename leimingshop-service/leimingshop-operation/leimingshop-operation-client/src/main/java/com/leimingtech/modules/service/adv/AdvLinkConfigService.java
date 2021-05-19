/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.service.adv;


import com.leimingtech.modules.dto.adv.AdvLinkConfigDTO;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * h5楼层图片链接
 *
 * @author 刘远杰
 * @email 2634443725@qq.com
 * @since 7.0 2019-05-10
 */

public interface AdvLinkConfigService {

    /**
     * 批量保存广告配置
     *
     * @param advLinkConfigDTOList
     */

    void saveBatch(@RequestBody List<AdvLinkConfigDTO> advLinkConfigDTOList);

    /**
     * 删除广告配置
     *
     * @param advId 广告ID
     */

    void delete(@RequestBody List<Long> advId);

    /**
     * 查询广告配置
     *
     * @param id
     * @return
     */

    List<AdvLinkConfigDTO> getLinkList(@RequestParam("id") Long id);
}
