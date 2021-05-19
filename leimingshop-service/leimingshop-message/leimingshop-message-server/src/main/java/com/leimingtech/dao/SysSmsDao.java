/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.dao;

import com.leimingtech.commons.mybatis.dao.BaseDao;
import com.leimingtech.entity.SysSmsEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * 短信
 */
@Mapper
public interface SysSmsDao extends BaseDao<SysSmsEntity> {

}
