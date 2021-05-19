/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.service.picture.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.leimingtech.commons.mybatis.service.impl.CrudServiceImpl;
import com.leimingtech.commons.tools.constant.Constant;
import com.leimingtech.commons.tools.utils.ConvertUtils;
import com.leimingtech.dao.picture.PictureCategoryDao;
import com.leimingtech.dao.picture.PictureDao;
import com.leimingtech.dto.picture.*;
import com.leimingtech.entity.picture.PictureCategoryEntity;
import com.leimingtech.enums.picture.ErrorCodeEnum;
import com.leimingtech.service.picture.PictureCategoryService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 图片分类表
 *
 * @author chengQian
 * @email sunlightcs@gmail.com
 * @since 1.0 2019-05-10
 */
@Service
public class PictureCategoryServiceImpl extends CrudServiceImpl<PictureCategoryDao, PictureCategoryEntity, PictureCategoryDTO> implements PictureCategoryService {
    @Resource
    private PictureCategoryDao pictureCategoryDao;
    @Resource
    private PictureDao pictureDao;

    /**
     * 条件构造器
     *
     * @param params 分页参数
     * @return
     */
    @Override
    public QueryWrapper<PictureCategoryEntity> getWrapper(@RequestParam Map<String, Object> params) {
        String id = (String) params.get("id");

        QueryWrapper<PictureCategoryEntity> wrapper = new QueryWrapper<>();
        wrapper.eq(StringUtils.isNotBlank(id), "id", id);

        return wrapper;
    }

    /**
     * 查询出所有图片分组
     */

    @Override

    public List<AllCategoryDTO> selectCategoryAll(@RequestParam("storeId") Long storeId) {
        // 查询所有分组，并且查询出所有分组下的图片数量
        List<AllCategoryDTO> categoryDTOList = new ArrayList<>();
        List<FindPictureCategoryDTO> pictureCategoryDtoList = pictureCategoryDao.selectCategoryCounts(storeId);
//        List<PictureCategoryDTOs> pictureCategoryDTOs = pictureCategoryDao.findAll(storeId);
//        List<PictureCategoryDTOs> build = TreeUtils.build(pictureCategoryDTOs, Constant.MENU_ROOT);
//
        //算出父分组下所有的图片数量
        for (FindPictureCategoryDTO findPictureCategoryDTO : pictureCategoryDtoList) {
            //父分组的图片数量
            Integer pictureCount = findPictureCategoryDTO.getPictureCount();
            // 循环当前父分组下的子分组获取子分组的图片数量
            for (SubordinateCategoryDTO subordinateCategoryDTO : findPictureCategoryDTO.getList()) {
                pictureCount += subordinateCategoryDTO.getPictureCount();
            }
            findPictureCategoryDTO.setPictureCount(pictureCount);
        }


        // 查询未分组的图片数量
        Integer pictureNotCount = pictureDao.selectIsNotCategoryPicture(storeId);
        FindPictureCategoryDTO findPictureCategoryDTO = new FindPictureCategoryDTO();
        findPictureCategoryDTO.setCategoryName(ErrorCodeEnum.NO_CATEGORY_PICTURE.typeValue());
        findPictureCategoryDTO.setPictureCount(pictureNotCount);
        pictureCategoryDtoList.add(findPictureCategoryDTO);
        // 查询所有图片数量
        Integer pictureCount = pictureDao.findAllPictureCount(storeId);
        AllCategoryDTO allCategoryDTO = new AllCategoryDTO();
        allCategoryDTO.setCategoryName("所有图片数量");
        allCategoryDTO.setPictureCount(pictureCount);
        allCategoryDTO.setList(pictureCategoryDtoList);
        categoryDTOList.add(allCategoryDTO);
        return categoryDTOList;
    }

    /**
     * 更改图片分组名称
     *
     * @param updatePictureCategoryDTO 分组实体
     */
    @Override

    public Integer updateCategoryById(@RequestBody PictureCategoryDTO updatePictureCategoryDTO) {

        Integer categoryCount = pictureCategoryDao.selectByName(updatePictureCategoryDTO);
        if (ErrorCodeEnum.NO_CATEGORY_PICTURE.typeValue().equals(updatePictureCategoryDTO.getCategoryName()) || categoryCount > 0) {
            return ErrorCodeEnum.ILLEGAL_IMAGES.value();
        } else {
            PictureCategoryEntity pictureCategoryEntity = ConvertUtils.sourceToTarget(updatePictureCategoryDTO, PictureCategoryEntity.class);

            baseDao.updateById(pictureCategoryEntity);
        }
        return Constant.SUCCESS;
    }

    /**
     * 保存分组
     *
     * @param dto 图片分组实体
     */
    @Override

    public Integer saveCategory(@RequestBody PictureCategoryDTO dto) {

        Integer nameCount = pictureCategoryDao.selectByName(dto);
        if (ErrorCodeEnum.NO_CATEGORY_PICTURE.typeValue().equals(dto.getCategoryName()) || nameCount > 0) {
            return ErrorCodeEnum.CATEGORY_REPETITION.value();
        } else {
            PictureCategoryEntity pictureCategoryEntity = ConvertUtils.sourceToTarget(dto, PictureCategoryEntity.class);
            pictureCategoryDao.insert(pictureCategoryEntity);
        }

        return Constant.SUCCESS;
    }


    /**
     * 删除分组
     *
     * @param deletePictureCategoryDTO 参数
     */

    @Override

    public void delete(@RequestBody DeletePictureCategoryDTO deletePictureCategoryDTO) {

        baseDao.deleteCategory(deletePictureCategoryDTO.getId());
        pictureDao.deletePictureCategory(deletePictureCategoryDTO.getId(), deletePictureCategoryDTO.getDelFlag());


    }

    /***
     * 查出分组信息
     * @param params 查询条件
     * @return
     */
    @Override

    public List<PictureCategoryDTO> list(@RequestParam Map<String, Object> params) {
        return super.list(params);
    }

    /**
     * 根据分组ID 获取分组信息
     *
     * @param id 主键ID
     * @return
     */
    @Override

    public PictureCategoryDTO findById(Long id) {
        return super.get(id);
    }
}
