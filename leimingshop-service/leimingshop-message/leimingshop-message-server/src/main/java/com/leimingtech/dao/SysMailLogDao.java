/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.dao;

import com.leimingtech.commons.mybatis.dao.BaseDao;
import com.leimingtech.entity.SysMailLogEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * 邮件发送记录
 */
@Mapper
public interface SysMailLogDao extends BaseDao<SysMailLogEntity> {

}
