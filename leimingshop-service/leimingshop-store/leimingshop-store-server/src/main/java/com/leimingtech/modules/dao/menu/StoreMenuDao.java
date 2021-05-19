/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dao.menu;

import com.leimingtech.commons.mybatis.dao.BaseDao;
import com.leimingtech.commons.tools.security.bo.StoreMenuDTOs;
import com.leimingtech.modules.dto.index.StoreUserFunctionDTO;
import com.leimingtech.modules.entity.menu.StoreMenuEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 店铺菜单表
 *
 * @author chengqian
 * @email 543826372@qq.com
 * @since 1.0.0 2019-06-06
 */
@Mapper
public interface StoreMenuDao extends BaseDao<StoreMenuEntity> {
    /**
     * 获取上级菜单名称
     *
     * @param parentId 父id
     * @return 返回菜单名称
     */
    String getParentName(Long parentId);

    /**
     * 查询注册菜单
     *
     * @return 返回菜单信息
     */
    List<StoreMenuDTOs> findRegisterMenu();

    /**
     * 查询店铺首页默认菜单
     *
     * @param roleId   角色id
     * @param roleMark 角色表示
     * @return 店铺角色资源信息
     */
    List<StoreUserFunctionDTO> defaultMenu(@Param("roleId") String roleId,
                                           @Param("roleMark") Integer roleMark);

    /**
     * 查询活动菜单
     *
     * @return 返回店铺资源信息
     */
    List<StoreUserFunctionDTO> activityMenu();
}