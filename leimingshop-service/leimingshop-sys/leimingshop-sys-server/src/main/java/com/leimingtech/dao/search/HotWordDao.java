/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.dao.search;

import com.leimingtech.commons.mybatis.dao.BaseDao;
import com.leimingtech.entity.search.HotWordEntity;
import org.apache.ibatis.annotations.Mapper;


/**
 * 热词搜索
 *
 * @author qin
 * @email 49636174@qq.com
 * @since 1.0 2019-12-3
 */
@Mapper
public interface HotWordDao extends BaseDao<HotWordEntity> {


}
