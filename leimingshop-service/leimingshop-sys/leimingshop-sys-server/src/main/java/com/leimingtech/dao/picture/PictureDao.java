/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.dao.picture;

import com.leimingtech.commons.mybatis.dao.BaseDao;
import com.leimingtech.entity.picture.PictureEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 图片表
 *
 * @author chengQian
 * @email sunlightcs@gmail.com
 * @since 1.0 2019-05-10
 */
@Mapper
public interface PictureDao extends BaseDao<PictureEntity> {
    /**
     * 查询分组的数量
     *
     * @param pictureCategoryEntityId
     * @return
     */
    Integer selectCategoryCount(Long pictureCategoryEntityId);

    /**
     * 查询未分组的图片
     *
     * @return
     */
    Integer selectIsNotCategoryPicture(Long storeId);

    /**
     * 删除图片所在的分组
     *
     * @param ids
     */
    void deletePictureCategory(@Param("ids") Long[] ids, @Param("delFlag") Integer delFlag);

    /**
     * 查询所有图片数量
     *
     * @return
     */
    Integer findAllPictureCount(Long storeId);

}