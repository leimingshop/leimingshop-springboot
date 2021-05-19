/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.dao.reason;

import com.leimingtech.commons.mybatis.dao.BaseDao;
import com.leimingtech.entity.reason.ReasonDescriptionEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * 原因描述表
 *
 * @author huangkeyuan
 * @email huangkeyuan@leimingtech.com
 * @since 1.0.0 2019-06-10
 */
@Mapper
public interface ReasonDescriptionDao extends BaseDao<ReasonDescriptionEntity> {

}