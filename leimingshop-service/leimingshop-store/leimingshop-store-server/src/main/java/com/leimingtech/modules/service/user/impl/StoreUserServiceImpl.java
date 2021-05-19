/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.service.user.impl;

import cn.hutool.core.util.IdUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.leimingtech.commons.mybatis.service.impl.CrudServiceImpl;
import com.leimingtech.commons.tools.page.PageData;
import com.leimingtech.commons.tools.security.password.PasswordUtils;
import com.leimingtech.commons.tools.security.user.SellerDetail;
import com.leimingtech.commons.tools.utils.ConvertUtils;
import com.leimingtech.logging.MonitorLogger;
import com.leimingtech.logging.MonitorLoggerFactory;
import com.leimingtech.message.dto.MessageDTO;
import com.leimingtech.message.enums.MessageCodeEnum;
import com.leimingtech.message.service.SysMessageService;
import com.leimingtech.modules.dao.user.StoreUserDao;
import com.leimingtech.modules.dto.role.StoreRoleDTO;
import com.leimingtech.modules.dto.store.RegisterStoreDTO;
import com.leimingtech.modules.dto.user.*;
import com.leimingtech.modules.dto.usermanage.StoreUserManageDTO;
import com.leimingtech.modules.entity.user.StoreUserEntity;
import com.leimingtech.modules.service.role.StoreRoleService;
import com.leimingtech.modules.service.user.StoreUserService;
import com.leimingtech.modules.service.usermanage.StoreUserManageService;
import com.leimingtech.modules.utils.PasswdUtil;
import com.leimingtech.mq.constant.MqConstant;
import com.leimingtech.mq.service.RabbitMqSendService;
import com.leimingtech.service.message.MessageTextService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.beans.BeanCopier;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 店铺用户表
 *
 * @author chengqian
 * @email 543826372@qq.com
 * @since 1.0.0 2019-06-06
 */
@Service

public class StoreUserServiceImpl extends CrudServiceImpl<StoreUserDao, StoreUserEntity, StoreUserDTO> implements StoreUserService {

    private static MonitorLogger mlogger = MonitorLoggerFactory.getMonitorLogger(StoreUserServiceImpl.class);
    @Autowired
    private StoreUserDao storeUserDao;
    @Autowired

    private StoreUserManageService storeUserManageService;
    @Autowired

    private StoreRoleService storeRoleService;
    @Autowired
    private MessageTextService messageTextService;
    @Autowired
    private RabbitMqSendService rabbitMqSendService;
    @Autowired
    private SysMessageService sysMessageService;

    @Override
    public QueryWrapper<StoreUserEntity> getWrapper(Map<String, Object> params) {
        String id = (String) params.get("id");

        QueryWrapper<StoreUserEntity> wrapper = new QueryWrapper<>();
        wrapper.eq(StringUtils.isNotBlank(id), "id", id);

        return wrapper;
    }

    /**
     * 分页查询店铺下所有的用户
     *
     * @param params 分页参数
     * @return
     */
    @Override

    public PageData<FindStoreUserDTO> sellerUserPage(@RequestParam Map<String, Object> params) {
        //分页
        IPage<StoreUserEntity> page = getPage(params, "create_date", false);
        //查询
        List<FindStoreUserDTO> list = baseDao.findPage(params);
        return new PageData<>(list, page.getTotal());
    }

    /**
     * 根据ID获取信息
     *
     * @param id 主键ID
     * @return
     */
    @Override

    public StoreUserDTO get(Long id) {
        return super.get(id);
    }


    /**
     * 根据用户名获取用户信息
     *
     * @param username 用户名
     * @return StoreUserDTO 店铺用户信息
     * @autor kuangweiguo
     * @date 2019-06-22 17:08:20
     */
    @Override

    public StoreUserDTO getUserByUsername(String username) {
        return storeUserDao.getUserByUsername(username);
    }


    /**
     * 修改信息
     *
     * @param dto 参数提示
     */
    @Override

    public void update(@RequestBody StoreUserDTO dto) {
        super.update(dto);
    }


    /**
     * 根据ID删除
     *
     * @param userId 主键ID
     */

    @Override
    public void delete(@RequestBody Long[] userId) {
        super.delete(userId);
        // 删除用户店铺关联信息
        storeUserManageService.deleteByUserId(userId);
    }

    /**
     * 验证账号是否重复
     *
     * @param account 账号
     * @param userId  用户ID
     * @return
     */
    @Override

    public Integer verify(@RequestParam("account") String account,
                          @RequestParam(value = "storeId", required = false) Long storeId,
                          @RequestParam(value = "userId", required = false) Long userId) {

        return storeUserDao.verify(account, storeId, userId);
    }

    /**
     * 根据用户名获取用户登陆信息（仅在登陆使用）
     *
     * @param sellerName: seller用户名
     * @return seller用户基本信息
     * @date 2019/6/26 15:13
     * @author lixiang
     **/
    @Override

    public SellerDetail login(String sellerName) {
        StoreUserDTO storeUserDTO = this.getUserByUsername(sellerName);
        SellerDetail sellerDetail = null;
        if (storeUserDTO != null) {
            sellerDetail = new SellerDetail();
            StoreUserManageDTO storeUserManageDTO = storeUserManageService.findUserManage(storeUserDTO.getId());
            //将DTO转化为SellerDetail
            BeanCopier.create(storeUserDTO.getClass(), SellerDetail.class, false)
                    .copy(storeUserDTO, sellerDetail, null);

            // 用户没有关联在店铺下
            if (storeUserManageDTO == null) {
                return null;
            }
            sellerDetail.setStoreId(storeUserManageDTO.getStoreId());
            sellerDetail.setRoleId(String.valueOf(storeUserManageDTO.getRoleId()));
        }
        return sellerDetail;
    }

    /**
     * 根据用户ID删除用户信息
     *
     * @param userId
     */

    @Override
    public void deleteById(@RequestParam("userId") Long userId) {
        super.deleteById(userId);

    }

    /**
     * 新增店铺用户
     *
     * @param saveStoreUserDTO 用户实体
     */

    @Override
    public void saveUser(@RequestBody SaveStoreUserDTO saveStoreUserDTO) {

        StoreUserEntity storeUserEntity = ConvertUtils.sourceToTarget(saveStoreUserDTO, StoreUserEntity.class);
        insert(storeUserEntity);
        // 新增店铺用户关联
        StoreUserManageDTO storeUserManageDTO = new StoreUserManageDTO();
        storeUserManageDTO.setIsEnable(saveStoreUserDTO.getIsEnable());
        storeUserManageDTO.setRoleId(saveStoreUserDTO.getRoleId());
        storeUserManageDTO.setStoreId(saveStoreUserDTO.getStoreId());
        storeUserManageDTO.setStoreUserId(storeUserEntity.getId());
        storeUserManageService.save(storeUserManageDTO);

    }

    /**
     * 验证用户是否是超级管理员
     *
     * @param ids
     * @return
     */

    @Override
    public Integer findUserMark(@RequestBody Long[] ids) {


        return baseDao.findUserMark(ids);
    }


    /**
     * 根据ID 获取用户详细信息
     *
     * @param id
     * @return
     */

    @Override
    public UpdateStoreUserDTO findById(@RequestParam("id") Long id) {
        return baseDao.findById(id);
    }

    /**
     * 查询角色下面是否关联用户
     *
     * @param ids 角色ID
     * @return
     */

    @Override
    public Integer findUserCountByRole(@RequestBody Long[] ids) {
        return baseDao.findUserCountByRole(ids);
    }

    /**
     * 修改信息
     *
     * @param dto 参数提示
     */
    @Override

    public void updateSellerUser(@RequestBody UpdateStoreUserDTO dto) {
        StoreUserEntity storeUserDTO = ConvertUtils.sourceToTarget(dto, StoreUserEntity.class);
        baseDao.updateById(storeUserDTO);
        if (dto.getStoreId() == null) {
            return;
        }

        //删除店铺用户关联
        storeUserManageService.deleteByUserId(new Long[]{dto.getId()});

        // 修改用户店铺关联信息
        StoreUserManageDTO storeUserManageDTO = new StoreUserManageDTO();
        storeUserManageDTO.setIsEnable(dto.getIsEnable());
        storeUserManageDTO.setRoleId(dto.getRoleId());
        storeUserManageDTO.setStoreId(dto.getStoreId());
        storeUserManageDTO.setStoreUserId(storeUserDTO.getId());
        storeUserManageService.save(storeUserManageDTO);
    }

    /**
     * 重置密码
     *
     * @param id: 管理员ID
     **/
    @Override

    public boolean resetPassword(@RequestParam("id") Long id) {
        // 查询用户手机号
        StoreUserEntity storeUserEntity = baseDao.selectById(id);

        if (storeUserEntity != null && StringUtils.isNotBlank(storeUserEntity.getMobilePhone())) {
            //生成密码
            String passwd = PasswdUtil.getGeneratePassword();
            // 明文密码加密
            storeUserEntity.setPassword(PasswordUtils.encode(passwd));
            baseDao.updateById(storeUserEntity);

            //封装发送短信参数并发送短信消息
            this.sendMessage(storeUserEntity.getMobilePhone(), passwd);

            return true;
        }
        return false;
    }

    /**
     * 封装发送短信参数并发送短信消息
     *
     * @param mobile: 手机号
     * @param passwd: 密码铭文
     * @date 2020/4/10 9:24
     * @author lixiangx@leimingtech.com
     **/
    private void sendMessage(String mobile, String passwd) {

        Map<String, String> paramsMap = new HashMap<>(5);
        paramsMap.put("passwd", passwd);

        Map<String, Object> map = new HashMap<>(16);
        map.put("mobile", mobile);
        map.put("paramJson", JSON.toJSONString(paramsMap));

        MessageDTO messageDTO = new MessageDTO();
        messageDTO.setMessageCode(MessageCodeEnum.TEMPLATECODE_RESET_PWD_CODE.value());
        messageDTO.setSendName("系统");
        messageDTO.setParamJson(JSON.toJSONString(map));
        messageDTO.setUniqueMark(IdUtil.fastSimpleUUID());
        sysMessageService.save(messageDTO);
        rabbitMqSendService.sendMsg(MqConstant.LEIMINGTECH_SEND_MESSAGE_QUEUE, JSON.toJSONString(messageDTO));
    }

    /**
     * 根据店铺ID 获取店铺手机号
     *
     * @param storeId
     * @return
     */

    @Override
    public String getStorePhone(@RequestParam("storeId") Long storeId) {

        return baseDao.getStorePhone(storeId);
    }

    /**
     * 校验手机号是否重复
     *
     * @param mobile 手机号
     * @param userId 用户ID
     * @return
     */

    @Override
    public Integer verifyMobile(@RequestParam("mobile") String mobile,
                                @RequestParam(value = "userId", required = false) Long userId) {
        return baseDao.verifyMobile(mobile, userId);
    }

    /**
     * 校验邮箱是否重复
     *
     * @param email
     * @return
     */

    @Override
    public Integer verifyEmail(@RequestParam("email") String email,
                               @RequestParam(value = "userId", required = false) Long userId) {
        return baseDao.verifyEmail(email, userId);
    }

    /**
     * 店铺注册
     *
     * @param registerStoreDTO
     */

    @Override
    public void storeRegister(@RequestBody RegisterStoreDTO registerStoreDTO) {
        StoreUserEntity userEntity = ConvertUtils.sourceToTarget(registerStoreDTO, StoreUserEntity.class);
        // 创建角色
        StoreRoleDTO storeRoleDTO = new StoreRoleDTO();
        storeRoleDTO.setRoleRemark("新注册商户，未通过资深审核");
        storeRoleDTO.setRoleMark(2);
        storeRoleDTO.setId(IdWorker.getId());
        storeRoleDTO.setRoleName("新注册商户");
        storeRoleService.saveRole(storeRoleDTO);
        userEntity.setRoleId(storeRoleDTO.getId());
        userEntity.setPassword(PasswordUtils.encode(registerStoreDTO.getPassword()));
        insert(userEntity);
        StoreUserManageDTO storeUserManageDTO = new StoreUserManageDTO();
        storeUserManageDTO.setRoleId(storeRoleDTO.getId());
        storeUserManageDTO.setStoreUserId(userEntity.getId());
        storeUserManageService.save(storeUserManageDTO);
    }

    /**
     * 校验邮箱手机号账号是否存在
     *
     * @param userName
     * @return
     */

    @Override
    public Integer verifyUserPhone(@RequestParam("userName") String userName) {
        return baseDao.verifyUserPhone(userName);
    }

    /**
     * 获取手机号邮箱
     *
     * @param userName
     * @return
     */

    @Override
    public StoreUserPhoneEmailDTO getPhoneEmail(@RequestParam("userName") String userName) {
        return baseDao.getPhoneEmail(userName);
    }

    /**
     * 新增用户
     *
     * @param storeUserDTO
     */

    @Override
    public Long adminSave(@RequestBody StoreUserDTO storeUserDTO) {
        StoreUserEntity entity = ConvertUtils.sourceToTarget(storeUserDTO, StoreUserEntity.class);
        baseDao.insert(entity);

        return entity.getId();
    }

    /**
     * 功能描述:
     * <根据用户id查询商户个人中心信息>
     *
     * @param id 用户id
     * @return
     * @date 2020/3/5 11:22
     * @author weixianchun
     **/

    @Override
    public FindStoreUserPersonInfoDTO findPersonInfoById(long id) {
        FindStoreUserPersonInfoDTO personInfoDTO = baseDao.findPersonInfoById(id);
        if (personInfoDTO != null) {

            //加密手机号
            String phone = personInfoDTO.getMobilePhone();
            if (StringUtils.isNotBlank(phone)) {
                StringBuilder builder = new StringBuilder();
                String prefixPhone = phone.substring(0, 3);
                String suffixPhone = phone.substring(7);
                StringBuilder appendPhone = builder.append(prefixPhone).append("****").append(suffixPhone);
                personInfoDTO.setEncryptionPhone(String.valueOf(appendPhone));
            }

            //加密邮箱
            String email = personInfoDTO.getEmail();
            if (StringUtils.isNotBlank(email)) {
                StringBuilder emailBuilder = new StringBuilder(email);
                emailBuilder.replace(0, emailBuilder.indexOf("@"), "*****");
                String prefixEmail = email.substring(0, 4);
                String suffixEmail = String.valueOf(emailBuilder);
                StringBuilder stringBuilder = new StringBuilder();
                StringBuilder appendEmail = stringBuilder.append(prefixEmail).append(suffixEmail);
                personInfoDTO.setEncryptionEmail((String.valueOf(appendEmail)));
            }
        }
        return personInfoDTO;
    }

    /**
     * admin 端店铺用户列表（所有的超级管理员和注册权限的用户）
     *
     * @param params
     * @return
     */

    @Override
    public PageData<StoreUserDTO> adminStoreUserPage(@RequestParam Map<String, Object> params) {

        //分页
        IPage<StoreUserEntity> page = getPage(params, "lsu.create_date", false);
        //查询
        List<StoreUserDTO> list = baseDao.adminStoreUserPage(params);
        return new PageData<>(list, page.getTotal());
    }

    /**
     * 查询出未关联店铺的用户
     *
     * @return
     */

    @Override
    public PageData<StoreUserListDTO> findNotStoreUser(@RequestParam Map<String, Object> params) {

        //分页
        IPage<StoreUserEntity> page = getPage(params, "create_date", false);
        //查询
        List<StoreUserListDTO> list = baseDao.findNotStoreUser(params, page);
        return new PageData<>(list, page.getTotal());
    }
}
