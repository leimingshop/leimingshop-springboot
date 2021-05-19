/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dao.labelrecommend;

import com.leimingtech.commons.mybatis.dao.BaseDao;
import com.leimingtech.modules.dto.labelrecommend.LabelRecommendDTO;
import com.leimingtech.modules.entity.labelrecommend.LabelRecommendEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 标签推荐表
 *
 * @author weixianchun weixianchun@leimingtech.com
 * @since v1.0.0 2020-01-09
 */
@Mapper
public interface LabelRecommendDao extends BaseDao<LabelRecommendEntity> {

    /**
     * 功能描述:
     * <保存时-根据标签名称 标识查询标签推荐信息>
     *
     * @param labelName 标签名称
     * @param labelFlag 标识
     * @return 返回标签信息
     * @date 2020/1/9 16:23
     * @author weixianchun
     **/
    LabelRecommendDTO findByName(@Param("labelName") String labelName, @Param("labelFlag") String labelFlag);

    /**
     * 功能描述:
     * <修改时-根据标签名称 标识查询标签推荐信息>
     *
     * @param labelName 标签名称
     * @param labelFlag 标识
     * @param id        标签id
     * @return 返回标签名称数量
     * @date 2020/1/9 16:23
     * @author weixianchun
     **/
    int findByLabelNameUpdate(@Param("id") Long id, @Param("labelName") String labelName, @Param("labelFlag") String labelFlag);
}
