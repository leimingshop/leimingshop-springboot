/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dao.group;

import com.leimingtech.commons.mybatis.dao.BaseDao;
import com.leimingtech.modules.entity.group.GroupMemberEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * 拼团记录用户管理
 *
 * @author keyuan huangkeyuan@leimingtech.com
 * @since v1.0.0 2020-03-09
 */
@Mapper
public interface GroupMemberDao extends BaseDao<GroupMemberEntity> {

}
