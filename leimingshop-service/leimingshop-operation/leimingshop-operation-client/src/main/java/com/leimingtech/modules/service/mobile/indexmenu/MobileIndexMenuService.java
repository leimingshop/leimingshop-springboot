/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.service.mobile.indexmenu;


import com.leimingtech.commons.tools.page.PageData;
import com.leimingtech.modules.dto.mobile.indexmenu.InsertMobileIndexMenuDTO;
import com.leimingtech.modules.dto.mobile.indexmenu.MobileIndexMenuDTO;
import com.leimingtech.modules.dto.mobile.indexmenu.UpdateMobileIndexMenuDTO;
import com.leimingtech.modules.dto.mobile.indexmenu.UpdateShowDTO;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

/**
 * 移动首页菜单管理
 *
 * @author 刘远杰
 * @email 2634443725@qq.com
 * @since 1.0 2019-06-11
 */

public interface MobileIndexMenuService {

    /**
     * 功能描述:
     * 〈移动首页菜单分页查询〉
     *
     * @param params 查询条件
     * @return 返回H5首页菜单分页信息
     * @author : 刘远杰
     */

    PageData<MobileIndexMenuDTO> page(@RequestParam Map<String, Object> params);


    /**
     * 功能描述:
     * 〈移动首页菜单展示中集合〉
     *
     * @return 返回H5首页菜单信息
     * @author : 刘远杰
     */

    List<MobileIndexMenuDTO> listIng();

    /**
     * 功能描述:
     * 〈查询移动首页菜单详情〉
     *
     * @param id 移动首页菜单id
     * @return 返回H5首页菜单信息
     * @author : 刘远杰
     */

    MobileIndexMenuDTO get(long id);

    /**
     * 功能描述:
     * 〈保存移动首页菜单〉
     *
     * @param dto 保存移动楼层菜单实体
     * @return 返回保存结果
     * @author : 刘远杰
     */

    Map<String, Object> saveMobileIndexMenu(@RequestBody InsertMobileIndexMenuDTO dto);

    /**
     * 功能描述:
     * 〈编辑移动首页菜单〉
     *
     * @param dto 编辑移动楼层菜单实体
     * @return 返回编辑结果
     * @author : 刘远杰
     */

    Map<String, Object> updateMobileIndexMenu(@RequestBody UpdateMobileIndexMenuDTO dto);

    /**
     * 启用禁用
     *
     * @param dto
     * @return
     */

    void isShow(@RequestBody UpdateShowDTO dto);

    /**
     * 功能描述:
     * 〈逻辑删除移动首页菜单〉
     *
     * @param id 移动首页菜单id
     * @return 返回删除成功数量
     * @author : 刘远杰
     */

    int deleteById(Long id);

    /**
     * 功能描述:
     * 〈批量逻辑删除移动首页菜单〉
     *
     * @param ids 移动首页菜单id数组
     * @author : 刘远杰
     */

    void delete(@RequestBody Long[] ids);

    /**
     * 功能描述:
     * 〈新增移动首页菜单校验规格菜单名称重复性〉
     *
     * @param menuName 移动首页菜单名称
     * @return 返回保存成功结果
     * @author : 刘远杰
     */

    Map<String, Object> checkMenuNameWhenAdd(@RequestParam("menuName") String menuName);

    /**
     * 功能描述:
     * 〈修改移动首页菜单校验规格菜单名称重复性〉
     *
     * @param menuName 移动首页菜单名称
     * @param id       移动首页菜单id
     * @return 返回修改结果
     * @author : 刘远杰
     */

    Map<String, Object> checkMenuNameWhenUpdate(@RequestParam("menuName") String menuName, @RequestParam("id") Long id);

}
