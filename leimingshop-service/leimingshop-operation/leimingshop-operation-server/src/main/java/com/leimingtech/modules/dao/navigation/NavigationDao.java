/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dao.navigation;

import com.leimingtech.commons.mybatis.dao.BaseDao;
import com.leimingtech.modules.entity.navigation.NavigationEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * 首页导航设置
 *
 * @author chengqian chengqian@leimingtech.com
 * @since v1.0.0 2020-06-11
 */
@Mapper
public interface NavigationDao extends BaseDao<NavigationEntity> {

}