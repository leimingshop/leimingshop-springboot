/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dao.adv;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.leimingtech.commons.mybatis.dao.BaseDao;
import com.leimingtech.modules.entity.adv.AdvCategoryEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 广告类别管理
 *
 * @author 刘远杰
 * @email 2634443725@gmail.com
 * @since 7.0 2019-05-13
 */
@Mapper
public interface AdvCategoryDao extends BaseDao<AdvCategoryEntity> {

    /**
     * 广告类别查询
     *
     * @param params 查询参数
     * @return 返回广告分类
     * @Author 刘远杰
     * @Description 广告类别查询
     * @Date 2019/5/13 16:19
     */
    List<AdvCategoryEntity> findAdvCategoryList(Map<String, Object> params);

    /**
     * 广告类别查询
     *
     * @param params 查询参数
     * @param page   分页信息
     * @return 返回广告分类信息
     * @Author 刘远杰
     * @Description 广告类别查询
     * @Date 2019/5/13 16:19
     */
    List<AdvCategoryEntity> findAdvCategoryPage(@Param("params") Map<String, Object> params, IPage page);

    /**
     * 统计系广告分类数
     *
     * @param ids
     * @return 返回广告分类数量
     * @Author 刘远杰
     * @Description 统计系广告分类数
     * @Date 2019/5/15 13:29
     */
    Integer countAdvCategory(@Param("ids") Long[] ids);

    /**
     * 根据广告分类标识查询广告分类
     *
     * @param advKey 广告分类标识
     * @return 返回广告分类
     * @Author 刘远杰
     * @Description 根据广告分类标识查询广告分类
     * @Date 2019/5/16 11:16
     */
    AdvCategoryEntity findAdvCategoryByAdvKey(String advKey);

}
