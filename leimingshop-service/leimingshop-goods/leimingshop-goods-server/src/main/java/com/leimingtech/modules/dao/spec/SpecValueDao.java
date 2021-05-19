/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dao.spec;

import com.leimingtech.commons.mybatis.dao.BaseDao;
import com.leimingtech.modules.entity.spec.SpecValueEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * 规格值管理
 *
 * @author 刘远杰
 * @email 2634443725@qq.com
 * @since 7.0 2019-05-16
 */
@Mapper
public interface SpecValueDao extends BaseDao<SpecValueEntity> {

    /**
     * 查询规格值集合
     *
     * @param params 条件
     * @return 返回规格值
     * @author 刘远杰
     * @Description 查询规格值集合
     * @Date 2019/5/17 10:09
     */
    List<SpecValueEntity> findSpecValueList(Map<String, Object> params);

    /**
     * 返回删除成功数量
     *
     * @param specId 规格id
     * @return 返回删除成功数量
     * @author 刘远杰
     * @Description 删除规格、规格值
     * @Date 2019/5/17 11:19
     */
    int deleteSpecValueBySpecId(Long specId);

}

