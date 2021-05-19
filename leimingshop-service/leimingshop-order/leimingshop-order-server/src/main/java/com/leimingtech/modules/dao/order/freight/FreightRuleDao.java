/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dao.order.freight;

import com.leimingtech.commons.mybatis.dao.BaseDao;
import com.leimingtech.modules.entity.order.freight.FreightRuleEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * 运费规则管理
 *
 * @author 刘远杰 2634443725@qq.com
 * @since v1.0.0 2020-04-22
 */
@Mapper
public interface FreightRuleDao extends BaseDao<FreightRuleEntity> {

}
