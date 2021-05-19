/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.service.user;


import com.leimingtech.commons.tools.page.PageData;
import com.leimingtech.commons.tools.security.user.SellerDetail;
import com.leimingtech.modules.dto.store.RegisterStoreDTO;
import com.leimingtech.modules.dto.user.*;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

/**
 * 店铺用户表
 *
 * @author chengqian
 * @email 543826372@qq.com
 * @since 1.0.0 2019-06-06
 */

public interface StoreUserService {
    /**
     * 分页查询信息
     *
     * @param params 分页参数
     * @return
     */

    PageData<FindStoreUserDTO> sellerUserPage(@RequestParam Map<String, Object> params);

    /**
     * 根据ID获取信息
     *
     * @param id 主键ID
     * @return
     */

    StoreUserDTO get(Long id);


    /**
     * 根据用户名获取用户信息
     *
     * @param username 用户名
     * @return StoreUserDTO 店铺用户信息
     * @autor kuangweiguo
     * @date 2019-06-22 17:08:20
     */

    StoreUserDTO getUserByUsername(String username);


    /**
     * 修改seller用户信息
     *
     * @param dto 参数提示
     */

    void updateSellerUser(@RequestBody UpdateStoreUserDTO dto);


    /**
     * 修改admin用户信息
     *
     * @param dto 参数提示
     */

    void update(@RequestBody StoreUserDTO dto);


    /**
     * 根据ID删除
     *
     * @param userId 主键ID
     */

    void delete(@RequestBody Long[] userId);

    /**
     * 验证账号是否重复
     *
     * @param account 账号
     * @param userId  用户id
     * @param storeId 店铺id
     * @return 返回重复数量
     */

    Integer verify(@RequestParam("account") String account,
                   @RequestParam(value = "storeId", required = false) Long storeId,
                   @RequestParam(value = "userId", required = false) Long userId);

    /**
     * 根据用户名获取用户登陆信息（仅在登陆使用）
     *
     * @param sellerName: seller用户名
     * @return seller用户基本信息
     * @date 2019/6/26 15:13
     * @author lixiang
     **/

    SellerDetail login(String sellerName);

    /**
     * 根据用户ID删除用户信息
     *
     * @param userId
     */

    void deleteById(@RequestParam("userId") Long userId);

    /**
     * 新增店铺用户
     *
     * @param saveStoreUserDTO 用户实体
     */

    void saveUser(@RequestBody SaveStoreUserDTO saveStoreUserDTO);

    /**
     * 验证用户是否是超级管理员
     *
     * @param ids
     * @return
     */

    Integer findUserMark(@RequestBody Long[] ids);

    /**
     * 根据ID 获取用户详细信息
     *
     * @param id
     * @return
     */

    UpdateStoreUserDTO findById(@RequestParam("id") Long id);

    /**
     * 查询角色下面是否关联用户
     *
     * @param ids
     * @return
     */

    Integer findUserCountByRole(@RequestBody Long[] ids);

    /**
     * 重置密码
     *
     * @param id 用户id
     * @return 返回是否成功
     */

    boolean resetPassword(@RequestParam("id") Long id);

    /**
     * 根据店铺ID 获取店铺手机号
     *
     * @param storeId
     * @return
     */

    String getStorePhone(@RequestParam("storeId") Long storeId);

    /**
     * 校验手机号是否重复
     *
     * @param mobile 手机号
     * @param userId 用户id
     * @return 返回重复数量
     */

    Integer verifyMobile(@RequestParam("mobile") String mobile,
                         @RequestParam(value = "userId", required = false) Long userId);

    /**
     * 校验邮箱是否重复
     *
     * @param email  邮箱
     * @param userId 用户id
     * @return 返回重复数量
     */

    Integer verifyEmail(@RequestParam("email") String email,
                        @RequestParam(value = "userId", required = false) Long userId);

    /**
     * 店铺注册
     *
     * @param registerStoreDTO
     */

    void storeRegister(@RequestBody RegisterStoreDTO registerStoreDTO);

    /**
     * 校验邮箱手机号账号是否存在
     *
     * @param userName
     * @return
     */

    Integer verifyUserPhone(@RequestParam("userName") String userName);

    /**
     * 获取手机号邮箱
     *
     * @param userName
     * @return
     */

    StoreUserPhoneEmailDTO getPhoneEmail(@RequestParam("userName") String userName);

    /**
     * 新增用户
     *
     * @param storeUserDTO 新增参数
     * @return 返回用户id
     */

    Long adminSave(@RequestBody StoreUserDTO storeUserDTO);

    /**
     * 功能描述:
     * <根据用户id查询商户个人中心信息>
     *
     * @param id 用户id
     * @return
     * @date 2020/3/5 11:22
     * @author weixianchun
     **/

    FindStoreUserPersonInfoDTO findPersonInfoById(long id);


    /**
     * admin 端店铺用户列表（所有的超级管理员和注册权限的用户）
     *
     * @param params
     * @return
     */

    PageData<StoreUserDTO> adminStoreUserPage(@RequestParam Map<String, Object> params);

    /**
     * 查询出未关联店铺的用户
     *
     * @param params
     * @return
     */

    PageData<StoreUserListDTO> findNotStoreUser(@RequestParam Map<String, Object> params);
}
