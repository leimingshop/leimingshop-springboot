/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.service.webfloor;


import com.leimingtech.modules.dto.webfloor.WebFloorLinkConfigDTO;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

/**
 * h5楼层图片链接
 *
 * @author 刘远杰
 * @email 2634443725@qq.com
 * @since 7.0 2019-05-10
 */

public interface WebFloorLinkConfigService {

    /**
     * 功能描述:
     * 〈楼层图片跳转链接条件查询〉
     *
     * @param params 查询条件
     * @return 返回楼层配置信息
     * @author : 刘远杰
     */

    List<WebFloorLinkConfigDTO> list(@RequestParam Map<String, Object> params);

    /**
     * 功能描述:
     * 〈批量保存楼层图片链接〉
     *
     * @param webFloorLinkConfigDTOList 楼层图片链接集合
     * @return : boolean
     * @author : 刘远杰
     */

    boolean insertBatch(@RequestBody List<WebFloorLinkConfigDTO> webFloorLinkConfigDTOList);

    /**
     * 功能描述:
     * 〈根据条件物理删除图片链接〉
     *
     * @param params 删除条件
     * @return : int 删除数量
     * @author : 刘远杰
     */

    Integer deleteWebFloorLinkConfig(@RequestParam Map<String, Object> params);

    /**
     * 功能描述:
     * 〈根据条件逻辑删除图片链接〉
     *
     * @param params 删除条件
     * @return : int 删除数量
     * @author : 刘远杰
     */

    Integer logicDeletewebFloorLinkConfig(@RequestParam Map<String, Object> params);

    /**
     * 根据专题页id查询关联数量
     *
     * @param id 专题页id
     * @return
     */

    Integer findFloorNumByTopicId(@RequestParam("id") Long id);

    /**
     * 查询楼层配置根据楼层ID
     *
     * @param floorId: 楼层ID
     * @param storeId  店铺ID
     * @return: 楼层配置集合
     * @date 2019/8/16 18:50
     * @author lixiang
     **/

    List<WebFloorLinkConfigDTO> findConfigListByFloorId(Long floorId,
                                                        @RequestParam(value = "storeId", required = false) Long storeId);
}
