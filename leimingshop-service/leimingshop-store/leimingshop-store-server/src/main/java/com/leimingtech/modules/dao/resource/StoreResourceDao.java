/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dao.resource;

import com.leimingtech.commons.mybatis.dao.BaseDao;
import com.leimingtech.commons.tools.security.bo.ResourceBO;
import com.leimingtech.commons.tools.security.bo.StoreMenuDTOs;
import com.leimingtech.modules.dto.menu.StoreMenuResourceDTO;
import com.leimingtech.modules.entity.resource.StoreResourceEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 资源管理
 *
 * @author chengqian chengqian@leimingtech.com
 * @since v1.0.0 2019-12-17
 */
@Mapper
public interface StoreResourceDao extends BaseDao<StoreResourceEntity> {
    /**
     * 删除资源信息
     *
     * @param value
     */
    void deleteByCode(String value);

    /**
     * 获取当前菜单资源
     *
     * @param menuId
     * @return
     */
    List<StoreMenuResourceDTO> getResourceByMenuId(Long menuId);

    /**
     * 获取全部资源列表
     *
     * @return 资源集合
     * @date 2019/12/19 15:23
     * @author lixiangx@leimingtech.com
     **/
    List<StoreResourceEntity> getResourceList();

    /**
     * 根据用户信息查询用户的授权资源
     *
     * @param userId: 用户ID
     * @return 资源集合信息
     * @date 2019/12/19 15:44
     * @author lixiangx@leimingtech.com
     **/
    List<ResourceBO> getUserResourceList(@Param("userId") Long userId);

    /**
     * 查询注册角色权限
     *
     * @return
     */
    List<StoreMenuDTOs> findRegisterResource();


}