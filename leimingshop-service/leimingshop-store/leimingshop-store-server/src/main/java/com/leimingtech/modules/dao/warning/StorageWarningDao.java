/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dao.warning;

import com.leimingtech.commons.mybatis.dao.BaseDao;
import com.leimingtech.modules.entity.warning.StorageWarningEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * 库存预警表
 *
 * @author chengqian chengqian@leimingtech.com
 * @since v1.0.0 2019-12-12
 */
@Mapper
public interface StorageWarningDao extends BaseDao<StorageWarningEntity> {

}