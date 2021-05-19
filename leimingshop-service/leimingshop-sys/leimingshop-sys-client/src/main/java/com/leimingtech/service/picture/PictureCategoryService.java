/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.service.picture;


import com.leimingtech.dto.picture.AllCategoryDTO;
import com.leimingtech.dto.picture.DeletePictureCategoryDTO;
import com.leimingtech.dto.picture.PictureCategoryDTO;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

/**
 * 图片分类表
 *
 * @author chengQian
 * @email sunlightcs@gmail.com
 * @since 1.0 2019-05-10
 */

public interface PictureCategoryService {
    /**
     * 查询出所有图片分组
     */

    List<AllCategoryDTO> selectCategoryAll(@RequestParam("storeId") Long storeId);

    /**
     * 更改图片分组名称
     *
     * @param updatePictureCategoryDTO 分组实体
     */

    Integer updateCategoryById(@RequestBody PictureCategoryDTO updatePictureCategoryDTO);

    /**
     * 保存分组
     *
     * @param dto 图片分组实体
     */

    Integer saveCategory(@RequestBody PictureCategoryDTO dto);

    /**
     * 删除分组
     *
     * @param deletePictureCategoryDTO 主键 ID
     */

    void delete(@RequestBody DeletePictureCategoryDTO deletePictureCategoryDTO);

    /**
     * @return java.util.List<com.leimingtech.dto.picture.PictureCategoryDTO>
     * @Author kuangweiguo
     * @Description 根据条件查询图片分组信息列表
     * @Date 2019/5/25  9:15 PM
     * @Param [params]
     **/

    List<PictureCategoryDTO> list(@RequestParam Map<String, Object> params);

    /**
     * 根据ID 获取分组信息
     *
     * @param id
     * @return
     */

    PictureCategoryDTO findById(Long id);

}