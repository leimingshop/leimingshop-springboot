/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dao.webfloor;

import com.leimingtech.commons.mybatis.dao.BaseDao;
import com.leimingtech.modules.dto.webfloor.WebFloorLinkConfigDTO;
import com.leimingtech.modules.entity.webfloor.WebFloorLinkConfigEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * h5楼层图片链接
 *
 * @author 刘远杰
 * @email 2634443725@qq.com
 * @since 7.0 2019-05-10
 */
@Mapper
public interface WebFloorLinkConfigDao extends BaseDao<WebFloorLinkConfigEntity> {

    /**
     * 功能描述:
     * 〈根据条件查找跳转集合〉
     *
     * @param map 查询条件
     * @return 返回楼层配置信息
     * @author : 刘远杰
     */
    List<WebFloorLinkConfigEntity> findWebFloorLinkConfigList(Map<String, Object> map);

    /**
     * 功能描述:
     * 〈根据条件物理删除图片链接〉
     *
     * @param map 删除料件
     * @return : 删除数量
     * @author : 刘远杰
     */
    int deleteWebFloorLinkConfig(Map<String, Object> map);

    /**
     * 功能描述:
     * 〈根据条件逻辑删除图片链接〉
     *
     * @param map 删除条件
     * @return : int 删除数量
     * @author : 刘远杰
     */
    int logicDeleteWebFloorLinkConfig(Map<String, Object> map);

    /**
     * 查询楼层配置根据楼层ID
     *
     * @param floorId: 楼层ID
     * @param storeId  店铺ID
     * @return: 楼层配置集合
     * @date 2019/8/16 18:50
     * @author lixiang
     **/
    List<WebFloorLinkConfigDTO> findConfigListByFloorId(@Param("floorId") Long floorId, @Param("storeId") Long storeId);

    Integer findFloorNumByTopicId(Long id);
}
