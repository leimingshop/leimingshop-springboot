/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.dao.stopword;

import com.leimingtech.commons.mybatis.dao.BaseDao;
import com.leimingtech.entity.stopword.StopWordEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.Set;

/**
 * 广告禁语管理
 *
 * @author chengqian
 * @since v1.0.0 2019-08-21
 */
@Mapper
public interface StopWordDao extends BaseDao<StopWordEntity> {
    /**
     * 查询所有禁用词
     *
     * @return
     */
    Set<String> findAllName();

}
