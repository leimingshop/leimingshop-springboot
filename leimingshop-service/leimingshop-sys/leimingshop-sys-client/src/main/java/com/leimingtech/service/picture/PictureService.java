/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.service.picture;


import com.leimingtech.commons.tools.page.PageData;
import com.leimingtech.dto.picture.PictureDTO;
import com.leimingtech.dto.picture.SavePictureDTO;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

/**
 * 图片表
 *
 * @author chengQian
 * @email sunlightcs@gmail.com
 * @since 1.0 2019-05-10
 */

public interface PictureService {

    /**
     * 分页查询
     *
     * @param params 分页参数
     * @return
     */

    PageData<PictureDTO> page(@RequestParam Map<String, Object> params);

    /**
     * 根据ID获取信息
     *
     * @param id 图片主键ID
     */

    PictureDTO findById(Long id);


    /**
     * 修改图片信息
     *
     * @param dto 图片
     */

    void update(@RequestBody PictureDTO dto);


    /**
     * 删除图片信息
     *
     * @param ids 主键ids
     */

    void delete(@RequestBody Long[] ids);

    /**
     * 查询分组下图片的数量
     *
     * @param pictureCategoryEntityId 分组ID
     */

    Integer selectCategoryCount(@RequestParam(value = "pictureCategoryEntityId") Long pictureCategoryEntityId);


    /**
     * 批量修改图片分组
     *
     * @param ids        图片ID
     * @param categoryId 分组ID
     */

    void updateBatchByIds(@RequestBody Long[] ids, @RequestParam(value = "categoryId") Long categoryId);

    /**
     * @return void
     * @Author kuangweiguo
     * @Description 新增图片
     * @Date 2019/5/25  9:00 PM
     * @Param [savePictureDTO]
     **/

    void savePicture(@RequestBody SavePictureDTO savePictureDTO);

    /**
     * @return java.util.List<com.leimingtech.dto.picture.PictureDTO>
     * @Author kuangweiguo
     * @Description 根据条件查询图片列表
     * @Date 2019/5/25  9:13 PM
     * @Param [params]
     **/

    List<PictureDTO> list(@RequestParam Map<String, Object> params);
}