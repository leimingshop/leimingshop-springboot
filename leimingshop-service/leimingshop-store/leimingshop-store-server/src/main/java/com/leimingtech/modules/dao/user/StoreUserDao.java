/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dao.user;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.leimingtech.commons.mybatis.dao.BaseDao;
import com.leimingtech.modules.dto.user.*;
import com.leimingtech.modules.entity.user.StoreUserEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 店铺用户表
 *
 * @author chengqian
 * @email 543826372@qq.com
 * @since 1.0.0 2019-06-06
 */
@Mapper
public interface StoreUserDao extends BaseDao<StoreUserEntity> {
    /**
     * 验证账号是否重复
     *
     * @param account 账号
     * @param storeId 店铺id
     * @param userId  用户id
     * @return 返回重复数量
     */
    Integer verify(@Param("account") String account, @Param("storeId") Long storeId, @Param("userId") Long userId);

    /**
     * 获取用户信息
     *
     * @param username 用户名
     * @return 返回用户信息
     */
    StoreUserDTO getUserByUsername(@Param("username") String username);

    /***
     * 分页查询当前店铺下所有的用户
     * @param params
     * @return
     */
    List<FindStoreUserDTO> findPage(Map<String, Object> params);

    /**
     * 验证用户是否是超级管理员
     *
     * @param ids
     * @return
     */
    Integer findUserMark(@Param("ids") Long[] ids);

    /**
     * 根据用户ID 获取详细信息
     *
     * @param id
     * @return
     */
    UpdateStoreUserDTO findById(Long id);

    /**
     * 查询角色下面是否关联用户
     *
     * @param ids
     * @return
     */
    Integer findUserCountByRole(@Param("ids") Long[] ids);

    /**
     * 修改密码
     *
     * @param id          主键
     * @param newPassword 新密码
     * @return 返回修改成功数量
     */
    int updatePassWord(@Param("id") Long id, @Param("newPassword") String newPassword);

    /**
     * 根据店铺ID获取店铺手机号
     *
     * @param storeId
     * @return
     */
    String getStorePhone(Long storeId);

    /**
     * 校验手机号是否重复
     *
     * @param mobile 手机号
     * @param userId 用户id
     * @return 返回重复数量
     */
    Integer verifyMobile(@Param("mobile") String mobile, @Param("userId") Long userId);

    /**
     * 校验邮箱是否重复
     *
     * @param email  邮箱
     * @param userId 用户id
     * @return 返回重复数量
     */
    Integer verifyEmail(@Param("email") String email, @Param("userId") Long userId);

    /**
     * 校验邮箱手机号账号是否存在
     *
     * @param userName
     * @return
     */
    Integer verifyUserPhone(String userName);

    /**
     * 获取手机号邮箱
     *
     * @param userName
     * @return
     */
    StoreUserPhoneEmailDTO getPhoneEmail(String userName);

    /**
     * 功能描述:
     * <根据用户id查询商户个人中心信息>
     *
     * @param id
     * @return
     * @date 2020/3/5 11:22
     * @author weixianchun
     **/
    FindStoreUserPersonInfoDTO findPersonInfoById(long id);

    /**
     * admin端店铺用户分页
     *
     * @param params
     * @return
     */
    List<StoreUserDTO> adminStoreUserPage(Map<String, Object> params);

    /**
     * 查询出未关联店铺的用户
     *
     * @param params 查询参数
     * @param page   分页参数
     * @return 返回用户信息
     */
    List<StoreUserListDTO> findNotStoreUser(@Param("params") Map<String, Object> params, IPage<StoreUserEntity> page);

}
