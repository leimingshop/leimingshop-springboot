/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dao.labelrecommend;


import com.leimingtech.commons.mybatis.dao.BaseDao;
import com.leimingtech.modules.entity.labelrecommend.LabelRecommendRelEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * 标签推荐关联表
 *
 * @author weixianchun weixianchun@leimingtech.com
 * @since v1.0.0 2020-01-09
 */
@Mapper
public interface LabelRecommendRelDao extends BaseDao<LabelRecommendRelEntity> {

}
