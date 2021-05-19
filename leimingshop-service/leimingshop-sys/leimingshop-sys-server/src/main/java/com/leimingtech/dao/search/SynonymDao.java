/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.dao.search;

import com.leimingtech.commons.mybatis.dao.BaseDao;
import com.leimingtech.entity.search.SynonymEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;


/**
 * 同义词管理DAO
 *
 * @author lixiangx@leimingtech.com
 * @date 2019/12/10 10:33
 **/
@Mapper
public interface SynonymDao extends BaseDao<SynonymEntity> {

    /**
     * 查询全部同义词数据
     *
     * @return 全部同义词数据集合
     * @date 2019/12/10 10:36
     * @author lixiangx@leimingtech.com
     **/
    List<SynonymEntity> findAll();

}
