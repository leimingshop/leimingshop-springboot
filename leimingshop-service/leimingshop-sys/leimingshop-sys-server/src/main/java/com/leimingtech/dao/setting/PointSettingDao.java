/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.dao.setting;

import com.leimingtech.commons.mybatis.dao.BaseDao;
import com.leimingtech.entity.setting.PointSettingEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * 积分及成长值设置表
 *
 * @author lixiang lixiangx@leimingtech.com
 * @since v1.0.0 2019-12-23
 */
@Mapper
public interface PointSettingDao extends BaseDao<PointSettingEntity> {

}