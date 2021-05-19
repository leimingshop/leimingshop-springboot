/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.service.user.function;


import com.leimingtech.commons.tools.page.PageData;
import com.leimingtech.dto.index.IndexUserFunctionDTO;
import com.leimingtech.dto.user.function.UserFunctionDTO;
import com.leimingtech.dto.user.function.UserFunctionInfoDTO;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

/**
 * @author xuzhch x170518@163.com
 * @since v1.0.0 2020-03-16
 */

public interface UserFunctionService {

    /**
     * 分页查询
     *
     * @param params map
     * @return PageData<UserFunctionDTO> 分页数据
     * @date 2020/4/7/007 11:48
     * @author xuzhch
     */

    PageData<UserFunctionDTO> page(@RequestParam Map<String, Object> params);

    /**
     * 不分页
     *
     * @param params map
     * @return List<UserFunctionDTO> 集合
     * @date 2020/4/7/007 11:48
     * @author xuzhch
     */

    List<UserFunctionDTO> list(@RequestParam Map<String, Object> params);

    /**
     * 查询详情
     *
     * @param id 常用功能ID
     * @return UserFunctionDTO 数据
     * @date 2020/4/7/007 11:49
     * @author xuzhch
     */

    UserFunctionDTO get(Long id);

    /**
     * 保存常用功能
     *
     * @param dto UserFunctionDTO
     * @date 2020/4/7/007 11:49
     * @author xuzhch
     */

    void save(@RequestBody UserFunctionDTO dto);

    /**
     * 修改常用功能
     *
     * @param dto UserFunctionDTO
     * @date 2020/4/7/007 11:49
     * @author xuzhch
     */

    void update(@RequestBody UserFunctionDTO dto);

    /**
     * 删除常用功能
     *
     * @param ids 常用功能ID数组
     * @date 2020/4/7/007 11:51
     * @author xuzhch
     */

    void delete(@RequestBody Long[] ids);

    /**
     * 批量保存常用功能
     *
     * @param userId  用户ID
     * @param menuIds 菜单ID集合
     * @date 2020/4/7/007 11:52
     * @author xuzhch
     */

    void saveBatch(@RequestParam("userId") Long userId, @RequestBody List<Long> menuIds);

    /**
     * 批量修改常用功能
     *
     * @param dtos List<UserFunctionDTO>
     * @date 2020/4/7/007 11:52
     * @author xuzhch
     */

    void updateBatch(@RequestBody List<UserFunctionDTO> dtos);

    /**
     * 获取用户常用功能
     *
     * @param userId 用户ID
     * @return List<UserFunctionInfoDTO> 当前用户关联的常用功能
     * @date 2020/4/7/007 11:53
     * @author xuzhch
     */

    List<UserFunctionInfoDTO> getUserFunctionByUserId(Long userId);

    /**
     * 首页常用功能
     *
     * @param userId 用户ID
     * @return List<IndexUserFunctionDTO> 常用功能数据
     * @date 2020/4/7/007 11:46
     * @author xuzhch
     */

    List<IndexUserFunctionDTO> getListByUserId(Long userId);

//
//    List<UserFunctionInfoDTO> getAllUserFunctionByUserId(@RequestBody UserDetail userDetail);
}
