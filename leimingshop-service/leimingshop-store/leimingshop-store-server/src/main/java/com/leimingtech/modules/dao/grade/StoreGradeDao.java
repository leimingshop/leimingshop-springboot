/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dao.grade;


import com.leimingtech.commons.mybatis.dao.BaseDao;
import com.leimingtech.modules.entity.grade.StoreGradeEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * 店铺等级表
 *
 * @author xuzhch
 * @email x170518@163.com
 * @since 1.0.0 2019-05-30
 */
@Mapper
public interface StoreGradeDao extends BaseDao<StoreGradeEntity> {
    /**
     * 获取发布商品最少的店铺等级ID
     *
     * @return
     */
    Long selectGradeId();

}