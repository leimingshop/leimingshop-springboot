/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.dao.updatelog;

import com.leimingtech.commons.mybatis.dao.BaseDao;
import com.leimingtech.entity.updatelog.UpdateLogEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * 版本更新日志
 *
 * @author huangjianeng huangjianeng@leimingtech.com
 * @since v1.0.0 2020-03-09
 */
@Mapper
public interface UpdateLogDao extends BaseDao<UpdateLogEntity> {

}