/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.service.sys.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.leimingtech.commons.mybatis.enums.DelFlagEnum;
import com.leimingtech.commons.mybatis.service.impl.BaseServiceImpl;
import com.leimingtech.commons.tools.constant.Constant;
import com.leimingtech.commons.tools.page.PageData;
import com.leimingtech.commons.tools.utils.ConvertUtils;
import com.leimingtech.dao.SysRoleDao;
import com.leimingtech.dto.SysRoleDTO;
import com.leimingtech.entity.SysRoleEntity;
import com.leimingtech.service.sys.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

/**
 * 角色管理
 *
 * @since 1.0.0
 */
@Service

public class SysRoleServiceImpl extends BaseServiceImpl<SysRoleDao, SysRoleEntity> implements SysRoleService {

    @Autowired

    private SysRoleUserService sysRoleUserService;

    @Autowired

    private SysRoleMenuService sysRoleMenuService;
    @Autowired

    private SysRoleDataScopeService sysRoleDataScopeService;

    @Autowired

    private SysDeptService sysDeptService;

    /**
     * 分页
     *
     * @param params
     * @return
     */
    @Override

    public PageData<SysRoleDTO> page(@RequestParam Map<String, Object> params) {

        IPage<SysRoleEntity> page = getPage(params, "sr.create_date", false);
        //查询角色数量及信息,并实现角色名称模糊查询
        List<SysRoleDTO> list = baseDao.selectCountInfo(params);
        return new PageData<>(list, page.getTotal());
    }

    /**
     * 查询列表
     *
     * @param params
     * @return
     */
    @Override

    public List<SysRoleDTO> list(@RequestParam Map<String, Object> params) {
        List<SysRoleEntity> entityList = baseDao.selectList(getWrapper(params));

        return ConvertUtils.sourceToTarget(entityList, SysRoleDTO.class);
    }

    private QueryWrapper<SysRoleEntity> getWrapper(Map<String, Object> params) {
        String name = (String) params.get("name");

        QueryWrapper<SysRoleEntity> wrapper = new QueryWrapper<>();
        wrapper.eq(Constant.DEL_FLAG, DelFlagEnum.NORMAL.value());
        wrapper.like(StringUtils.isNotBlank(name), "name", name);

        //普通管理员，只能查询所属部门及子部门的数据
//        UserDetail user = SecurityUser.getUser();
//        if (ObjectUtil.isNotNull(user) && user.getSuperAdmin() == SuperAdminEnum.NO.value()) {
//            List<Long> deptIdList = sysDeptService.getSubDeptIdList(user.getDeptId());
//            wrapper.in(deptIdList != null, "dept_id", deptIdList);
//        }

        return wrapper;
    }

    /**
     * 根据id查询详情
     *
     * @param id
     * @return
     */
    @Override

    public SysRoleDTO get(Long id) {
        SysRoleEntity entity = baseDao.selectById(id);

        return ConvertUtils.sourceToTarget(entity, SysRoleDTO.class);
    }

    /**
     * 保存
     *
     * @param dto
     */
    @Override

    @Transactional(rollbackFor = Exception.class)
    public void save(@RequestBody SysRoleDTO dto) {
        SysRoleEntity entity = ConvertUtils.sourceToTarget(dto, SysRoleEntity.class);

        //保存角色
        insert(entity);

        //保存角色菜单关系
        sysRoleMenuService.saveOrUpdate(entity.getId(), dto.getMenuIdList());

        //保存角色数据权限关系
        sysRoleDataScopeService.saveOrUpdate(entity.getId(), dto.getDeptIdList());
    }

    /**
     * 修改
     *
     * @param dto
     */
    @Override

    @Transactional(rollbackFor = Exception.class)
    public void update(@RequestBody SysRoleDTO dto) {
        SysRoleEntity entity = ConvertUtils.sourceToTarget(dto, SysRoleEntity.class);

        //更新角色
        updateById(entity);

        //更新角色菜单关系
        sysRoleMenuService.saveOrUpdate(entity.getId(), dto.getMenuIdList());

        //更新角色数据权限关系
        sysRoleDataScopeService.saveOrUpdate(entity.getId(), dto.getDeptIdList());
    }

    /**
     * 删除
     *
     * @param ids
     */
    @Override

    @Transactional(rollbackFor = Exception.class)
    public void delete(@RequestBody Long[] ids) {
        //逻辑删除角色
        logicDelete(ids);

        //删除角色用户关系
//        sysRoleUserService.deleteByRoleIds(ids);

        //[角色菜单关系、角色数据权限关系]，需要保留，不然逻辑删除就变成物理删除了
    }

    /**
     * 校验角色名称是否唯一
     *
     * @param name 角色名称
     * @return 是或者否
     * @date 2019/7/4 15:48
     * @author lixiang
     **/
    @Override

    public Boolean checkName(@RequestParam("name") String name) {
        QueryWrapper<SysRoleEntity> wrapper = new QueryWrapper<>();
        wrapper.eq(Constant.DEL_FLAG, DelFlagEnum.NORMAL.value());
        wrapper.eq(StringUtils.isNotBlank(name), "name", name);
        Integer rowNum = baseDao.selectCount(wrapper);
        return rowNum >= 1 ? false : true;
    }

}

