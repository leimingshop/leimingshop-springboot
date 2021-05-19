/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dao.webfloor;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.leimingtech.commons.mybatis.dao.BaseDao;
import com.leimingtech.modules.entity.webfloor.WebFloorEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * h5楼层设置表
 *
 * @author 刘远杰
 * @email 2634443725@qq.com
 * @since 7.0 2019-05-10
 */
@Mapper
public interface WebFloorDao extends BaseDao<WebFloorEntity> {

    /**
     * 功能描述:
     * 〈楼层分页查询〉
     *
     * @param params 查询条件
     * @return : java.util.List<com.leimingtech.modules.webfloor.entity.WebFloorEntity>
     * @author : 刘远杰
     */
    List<WebFloorEntity> findShopWebFloorPagerList(Map<String, Object> params);

    /**
     * 功能描述:
     * 〈楼层分页查询〉
     *
     * @param params 查询条件
     * @param page   分页信息
     * @return : java.util.List<com.leimingtech.modules.webfloor.entity.WebFloorEntity>
     * @author : 刘远杰
     */
    List<WebFloorEntity> findShopWebFloorPage(@Param("params") Map<String, Object> params, IPage page);

    /**
     * 功能描述:
     * 〈楼层编辑页面修改〉
     *
     * @param entity 楼层实体
     * @return : int
     * @author : 刘远杰
     */
    int editShopWebFloorPagerList(WebFloorEntity entity);

    /**
     * 查找H5floor
     *
     * @param storeId 店铺ID
     * @return 返回楼层信息
     * @Author: SWH ab4856812@163.com
     * @Description: 查找H5floor
     * @Date: 2019/11/8 14:12
     * @Version: V1.0
     */
    List<WebFloorEntity> findH5WebFloor(Long storeId);
}
