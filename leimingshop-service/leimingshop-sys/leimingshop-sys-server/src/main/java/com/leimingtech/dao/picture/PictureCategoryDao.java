/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.dao.picture;

import com.leimingtech.commons.mybatis.dao.BaseDao;
import com.leimingtech.dto.picture.FindPictureCategoryDTO;
import com.leimingtech.dto.picture.PictureCategoryDTO;
import com.leimingtech.dto.picture.PictureCategoryDTOs;
import com.leimingtech.dto.picture.SubordinateCategoryDTO;
import com.leimingtech.entity.picture.PictureCategoryEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 图片分类表
 *
 * @author chengQian
 * @email sunlightcs@gmail.com
 * @since 1.0 2019-05-10
 */
@Mapper
public interface PictureCategoryDao extends BaseDao<PictureCategoryEntity> {
    /**
     * 查询出所有的分组
     *
     * @return
     */
    List<FindPictureCategoryDTO> selectCategoryCounts(Long storeId);

    /**
     * 查出所有的子分组
     */
    List<SubordinateCategoryDTO> selectCategory(Long id);

    /**
     * 根据分组名称名称查出实体
     *
     * @return
     */
    Integer selectByName(PictureCategoryDTO dto);

    List<PictureCategoryDTOs> findAll(Long storeId);

    /**
     * 删除分组
     *
     * @param id
     */
    void deleteCategory(@Param("ids") Long[] id);
}