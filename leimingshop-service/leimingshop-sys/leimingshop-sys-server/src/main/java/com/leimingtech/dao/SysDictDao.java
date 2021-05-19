/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.dao;

import com.leimingtech.commons.mybatis.dao.BaseDao;
import com.leimingtech.entity.SysDictEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * 数据字典
 *
 * @since 1.0.0
 */
@Mapper
public interface SysDictDao extends BaseDao<SysDictEntity> {

}
