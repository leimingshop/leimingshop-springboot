/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.service.menu;


import com.leimingtech.commons.tools.page.PageData;
import com.leimingtech.commons.tools.security.bo.StoreMenuDTOs;
import com.leimingtech.modules.dto.index.StoreUserFunctionDTO;
import com.leimingtech.modules.dto.menu.StoreMenuDTO;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

/**
 * 店铺菜单表
 *
 * @author chengqian
 * @email 543826372@qq.com
 * @since 1.0.0 2019-06-06
 */

public interface StoreMenuService {
    /**
     * 分页查询信息
     *
     * @param params 分页参数
     * @return
     */

    PageData<StoreMenuDTO> page(@RequestParam Map<String, Object> params);

    /**
     * 根据ID获取信息
     *
     * @param id 主键ID
     * @return
     */

    StoreMenuDTO get(Long id);

    /**
     * 新增数据
     *
     * @param dto 参数实体
     */

    void save(@RequestBody StoreMenuDTO dto);

    /**
     * 修改信息
     *
     * @param dto 参数提示
     */

    void update(@RequestBody StoreMenuDTO dto);


    /**
     * 根据ID删除
     *
     * @param ids 主键ID
     */

    void delete(@RequestBody Long[] ids);

    /**
     * 查询注册菜单
     *
     * @return
     */

    List<StoreMenuDTOs> findRegisterMenu();

    /**
     * 查询用户默认首页菜单按钮
     *
     * @param roleId   角色ID
     * @param roleMark 0 普通 1 超级管理员
     * @param type     1 查询活动菜单
     * @return
     */

    List<StoreUserFunctionDTO> defaultMenu(@RequestParam("roleId") String roleId,
                                           @RequestParam(value = "roleMark", required = false) Integer roleMark,
                                           @RequestParam(value = "type", required = false) Integer type);
}