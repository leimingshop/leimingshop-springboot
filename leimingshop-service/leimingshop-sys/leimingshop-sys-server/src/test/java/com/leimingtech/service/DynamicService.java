/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.service;

import com.leimingtech.commons.dynamic.datasource.annotation.DataSource;
import com.leimingtech.commons.mybatis.service.impl.BaseServiceImpl;
import com.leimingtech.dao.sys.SysUserDao;
import com.leimingtech.entity.sys.SysUserEntity;

/**
 * 测试多数据源
 *
 * @since 1.1.0
 */
@DataSource("slave2")
public class DynamicService extends BaseServiceImpl<SysUserDao, SysUserEntity> {
}
