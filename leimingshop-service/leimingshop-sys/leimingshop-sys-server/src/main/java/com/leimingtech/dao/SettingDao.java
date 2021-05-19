/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.dao;

import com.leimingtech.commons.mybatis.dao.BaseDao;
import com.leimingtech.entity.setting.SettingEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 系统设置表
 *
 * @author kuangweiguo
 * @email kuangweiguo@leimingtech.com
 * @since 1.0.0 2019-05-10
 */
@Mapper
public interface SettingDao extends BaseDao<SettingEntity> {

    /**
     * 根据系统设置的名称查询
     *
     * @param settingName 对应模块的名称
     * @return
     */
    SettingEntity queryByName(String settingName);

    /**
     * 修改系统设置中的设置内容
     *
     * @param siteInfo    设置内容
     * @param settingName 设置模块名称
     */
    void updateSite(@Param("siteInfo") String siteInfo, @Param("settingName") String settingName);

    /**
     * 根据名称获取系统设置内容
     *
     * @param settingName
     * @return
     */
    String queryRedisByName(String settingName);
}
