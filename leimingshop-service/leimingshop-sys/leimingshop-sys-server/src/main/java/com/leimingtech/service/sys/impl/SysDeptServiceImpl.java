/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.service.sys.impl;

import com.leimingtech.commons.mybatis.service.impl.BaseServiceImpl;
import com.leimingtech.commons.tools.constant.Constant;
import com.leimingtech.commons.tools.enums.SuperAdminEnum;
import com.leimingtech.commons.tools.exception.CustomException;
import com.leimingtech.commons.tools.exception.ErrorCode;
import com.leimingtech.commons.tools.security.user.SecurityUser;
import com.leimingtech.commons.tools.security.user.UserDetail;
import com.leimingtech.commons.tools.utils.ConvertUtils;
import com.leimingtech.commons.tools.utils.TreeUtils;
import com.leimingtech.dao.SysDeptDao;
import com.leimingtech.dto.SysDeptDTO;
import com.leimingtech.entity.SysDeptEntity;
import com.leimingtech.service.sys.SysDeptService;
import com.leimingtech.service.sys.SysUserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 部门管理
 *
 * @since 1.0.0
 */
@Service

public class SysDeptServiceImpl extends BaseServiceImpl<SysDeptDao, SysDeptEntity> implements SysDeptService {
    @Autowired

    private SysUserService sysUserService;

    /**
     * 查询 部门信息
     *
     * @param params 查询参数
     * @return
     */
    @Override

    public List<SysDeptDTO> list(@RequestParam Map<String, Object> params) {
        //普通管理员，只能查询所属部门及子部门的数据
        UserDetail user = SecurityUser.getUser();
        if (user != null) {
            if (user.getSuperAdmin() == SuperAdminEnum.NO.value()) {
                params.put("deptIdList", getSubDeptIdList(user.getDeptId()));
            }
        }
        //查询部门列表
        List<SysDeptEntity> entityList = baseDao.getList(params);

        List<SysDeptDTO> dtoList = ConvertUtils.sourceToTarget(entityList, SysDeptDTO.class);

        return TreeUtils.build(dtoList);
    }

    /**
     * 根据ID获取信息
     *
     * @param id 主键ID
     * @return
     */
    @Override

    public SysDeptDTO get(Long id) {
        //超级管理员，部门ID为null
        if (id == null) {
            return null;
        }

        SysDeptEntity entity = baseDao.getById(id);

        return ConvertUtils.sourceToTarget(entity, SysDeptDTO.class);
    }

    /**
     * 新增部门信息
     *
     * @param dto 部门实体
     */
    @Override
    @Transactional(rollbackFor = Exception.class)

    public void save(@RequestBody SysDeptDTO dto) {
        SysDeptEntity entity = ConvertUtils.sourceToTarget(dto, SysDeptEntity.class);

        entity.setPids(getPidList(entity.getPid()));
        insert(entity);
    }

    /**
     * 修改部门信息
     *
     * @param dto 部门实体类
     */
    @Override
    @Transactional(rollbackFor = Exception.class)

    public void update(@RequestBody SysDeptDTO dto) {
        SysDeptEntity entity = ConvertUtils.sourceToTarget(dto, SysDeptEntity.class);

        //上级部门不能为自身
        if (entity.getId().equals(entity.getPid())) {
            throw new CustomException(ErrorCode.SUPERIOR_DEPT_ERROR);
        }

        entity.setPids(getPidList(entity.getPid()));
        updateById(entity);
    }

    /**
     * 删除部门信息
     *
     * @param id 主键ID
     */
    @Override
    @Transactional(rollbackFor = Exception.class)

    public void delete(Long id) {
        //判断是否有子部门
        List<Long> subList = getSubDeptIdList(id);
        if (subList.size() > 1) {
            throw new CustomException(ErrorCode.DEPT_SUB_DELETE_ERROR);
        }

        //判断部门下面是否有用户
        int count = sysUserService.getCountByDeptId(id);
        if (count > 0) {
            throw new CustomException(ErrorCode.DEPT_USER_DELETE_ERROR);
        }

        //逻辑删除
        logicDelete(new Long[]{id}, SysDeptEntity.class);
    }

    /**
     * 根据部门ID，获取本部门及子部门ID列表
     *
     * @param id 部门ID
     */
    @Override

    public List<Long> getSubDeptIdList(@RequestParam("id") Long id) {
        List<Long> deptIdList = baseDao.getSubDeptIdList("%" + id + "%");

        deptIdList.add(id);

        return deptIdList;
    }

    /**
     * 获取所有上级部门ID
     *
     * @param pid 上级ID
     */
    private String getPidList(Long pid) {
        //顶级部门，无上级部门
        if (Constant.DEPT_ROOT.equals(pid)) {
            return Constant.DEPT_ROOT + "";
        }

        //所有部门的id、pid列表
        List<SysDeptEntity> deptList = baseDao.getIdAndPidList();

        //list转map
        Map<Long, SysDeptEntity> map = new HashMap<>(deptList.size());
        for (SysDeptEntity entity : deptList) {
            map.put(entity.getId(), entity);
        }

        //递归查询所有上级部门ID列表
        List<Long> pidList = new ArrayList<>();
        getPidTree(pid, map, pidList);

        return StringUtils.join(pidList, ",");
    }

    private void getPidTree(Long pid, Map<Long, SysDeptEntity> map, List<Long> pidList) {
        //顶级部门，无上级部门
        if (Constant.DEPT_ROOT.equals(pid)) {
            return;
        }

        //上级部门存在
        SysDeptEntity parent = map.get(pid);
        if (parent != null) {
            getPidTree(parent.getPid(), map, pidList);
        }

        pidList.add(pid);
    }
}