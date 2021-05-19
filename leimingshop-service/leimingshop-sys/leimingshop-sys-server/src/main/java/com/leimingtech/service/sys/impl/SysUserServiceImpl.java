/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.service.sys.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.leimingtech.commons.mybatis.service.impl.BaseServiceImpl;
import com.leimingtech.commons.tools.constant.Constant;
import com.leimingtech.commons.tools.enums.SuperAdminEnum;
import com.leimingtech.commons.tools.page.PageData;
import com.leimingtech.commons.tools.security.password.PasswordUtils;
import com.leimingtech.commons.tools.security.user.SecurityUser;
import com.leimingtech.commons.tools.security.user.UserDetail;
import com.leimingtech.commons.tools.utils.ConvertUtils;
import com.leimingtech.dao.sys.SysUserDao;
import com.leimingtech.dto.sys.SysUserDTO;
import com.leimingtech.dto.sys.SysUserDetailDTO;
import com.leimingtech.entity.sys.SysUserEntity;
import com.leimingtech.service.sys.SysDeptService;
import com.leimingtech.service.sys.SysRoleService;
import com.leimingtech.service.sys.SysRoleUserService;
import com.leimingtech.service.sys.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

/**
 * @Author: weixianchun
 * @Description: 用户管理impl
 * @Date :2019/5/28 13:46
 * @Version V1.0
 **/

@Service
public class SysUserServiceImpl extends BaseServiceImpl<SysUserDao, SysUserEntity> implements SysUserService {

    @Autowired

    private SysRoleUserService sysRoleUserService;

    @Autowired

    private SysDeptService sysDeptService;

    @Autowired

    private SysRoleService sysRoleService;


    /**
     * @Author: weixianchun
     * @Description: 根据条件查询并分页
     * @Date :2019/5/27 17:27
     * @Param params
     * @Version V1.0
     **/
    @Override

    public PageData<SysUserDTO> page(@RequestParam Map<String, Object> params) {
        //转换成like
        paramsToLike(params, "username");

        //分页
        IPage<SysUserEntity> page = getPage(params, Constant.CREATE_DATE, false);

        //普通管理员，只能查询所属部门及子部门的数据
        UserDetail user = SecurityUser.getUser();
        if (user.getSuperAdmin() == SuperAdminEnum.NO.value()) {
            params.put("deptIdList", sysDeptService.getSubDeptIdList(user.getDeptId()));
        }

        //查询
        List<SysUserEntity> list = baseDao.getList(params);

        return getPageData(list, page.getTotal(), SysUserDTO.class);
    }

    /**
     * @Author: weixianchun
     * @Description: 查询所有用户信息
     * @Date :2019/5/28 12:06
     * @Param params
     * @Version V1.0
     **/
    @Override

    public List<SysUserDTO> list(@RequestParam Map<String, Object> params) {
        //普通管理员，只能查询所属部门及子部门的数据
        UserDetail user = SecurityUser.getUser();
        if (user.getSuperAdmin() == SuperAdminEnum.NO.value()) {
            params.put("deptIdList", sysDeptService.getSubDeptIdList(user.getDeptId()));
        }

        List<SysUserEntity> entityList = baseDao.getList(params);

        return ConvertUtils.sourceToTarget(entityList, SysUserDTO.class);
    }

    /**
     * @Author: weixianchun
     * @Description: 根据id查询信息
     * @Date :2019/5/28 11:59
     * @Param id
     * @Version V1.0
     **/
    @Override

    public SysUserDTO get(Long id) {
        SysUserEntity entity = baseDao.getById(id);

        return ConvertUtils.sourceToTarget(entity, SysUserDTO.class);
    }

    /**
     * @Author: weixianchun
     * @Description: 根据用户名查询信息
     * @Date :2019/5/28 11:59
     * @Param username
     * @Version V1.0
     **/
    @Override

    public SysUserDTO getByUsername(@RequestParam("username") String username) {
        SysUserEntity entity = baseDao.getByUsername(username);
        return ConvertUtils.sourceToTarget(entity, SysUserDTO.class);
    }

    /**
     * @Author: weixianchun
     * @Description: 保存用户信息
     * @Date :2019/5/28 11:58
     * @Param dto
     * @Version V1.0
     **/
    @Override

    @Transactional(rollbackFor = Exception.class)
    public void save(@RequestBody SysUserDTO dto) {
        SysUserEntity entity = ConvertUtils.sourceToTarget(dto, SysUserEntity.class);

        //密码加密
        String password = PasswordUtils.encode(entity.getPassword());
        entity.setPassword(password);

        entity.setSuperAdmin(SuperAdminEnum.NO.value());

        //保存用户
        insert(entity);

        //保存角色用户关系
        sysRoleUserService.saveOrUpdate(entity.getId(), dto.getRoleId());

    }

    /**
     * @Author: weixianchun
     * @Description: 修改用户信息
     * @Date :2019/5/28 11:57
     * @Param dto
     * @Version V1.0
     **/
    @Override

    @Transactional(rollbackFor = Exception.class)
    public void update(@RequestBody SysUserDetailDTO dto) {
        SysUserEntity entity = ConvertUtils.sourceToTarget(dto, SysUserEntity.class);


        // 单纯修改用户信息不进行密码修改
//        if (StringUtils.isBlank(dto.getPassword())) {
//            entity.setPassword(null);
//        } else {
//            String password = PasswordUtils.encode(entity.getPassword());
//            entity.setPassword(password);
//        }

        //更新用户
        updateById(entity);

        //更新角色用户关系
        sysRoleUserService.saveOrUpdate(entity.getId(), dto.getRoleId());
    }


    /**
     * @Author: weixianchun
     * @Description: 删除用户信息(逻辑删除)
     * @Date :2019/5/28 11:56
     * @Param ids
     * @Version V1.0
     **/
    @Override

    @Transactional(rollbackFor = Exception.class)
    public void delete(@RequestBody Long[] ids) {
        //逻辑删除
        logicDelete(ids);
        for (Long id : ids) {
            sysRoleUserService.deleteByUserId(id);
        }

        //角色用户关系，需要保留，不然逻辑删除就变成物理删除了
    }

    /**
     * @Author: weixianchun
     * @Description: 修改密码
     * @Date :2019/5/28 11:53
     * @Param id
     * @Param newPassword
     * @Version V1.0
     **/
    @Override

    @Transactional(rollbackFor = Exception.class)
    public void updatePassword(@RequestParam("id") Long id, @RequestParam("newPassword") String newPassword) {
        newPassword = PasswordUtils.encode(newPassword);

        baseDao.updatePassword(id, newPassword);
    }

    /**
     * @Author: weixianchun
     * @Description: 根据部门ID，查询用户数
     * @Date :2019/5/28 11:54
     * @Param deptId
     * @Version V1.0
     **/
    @Override

    public int getCountByDeptId(Long deptId) {
        return baseDao.getCountByDeptId(deptId);
    }


    /**
     * 重置密码
     *
     * @param id: 管理员ID
     * @return 重置结果
     * @date 2019/8/8 15:27
     * @author lixiang
     **/
    @Override

    public boolean resetPassword(@RequestParam("id") Long id) {
        String newPassword = PasswordUtils.encode("123456");
        int row = baseDao.updatePassword(id, newPassword);
        return row > 0 ? true : false;
    }
}