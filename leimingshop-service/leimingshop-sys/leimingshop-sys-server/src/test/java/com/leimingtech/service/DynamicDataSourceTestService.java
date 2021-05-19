/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.service;

import com.leimingtech.commons.dynamic.datasource.annotation.DataSource;
import com.leimingtech.dao.sys.SysUserDao;
import com.leimingtech.entity.sys.SysUserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 测试多数据源
 *
 * @since 1.0.0
 */
@Service
public class DynamicDataSourceTestService extends DynamicService {
    @Autowired
    private SysUserDao sysUserDao;

    @Transactional
    public void updateUser(Long id) {
        SysUserEntity user = new SysUserEntity();
        user.setId(id);
        user.setMobile("13500000000");
        sysUserDao.updateById(user);
    }

    @DataSource("slave1")
    @Transactional
    public void updateUserBySlave1(Long id) {
        SysUserEntity user = new SysUserEntity();
        user.setId(id);
        user.setMobile("13500000001");
        sysUserDao.updateById(user);
    }

    @DataSource("slave2")
    @Transactional
    public void updateUserBySlave2(Long id) {
        SysUserEntity user = new SysUserEntity();
        user.setId(id);
        user.setMobile("13500000002");
        sysUserDao.updateById(user);

        //测试事物
        //int i = 1/0;
    }
}
