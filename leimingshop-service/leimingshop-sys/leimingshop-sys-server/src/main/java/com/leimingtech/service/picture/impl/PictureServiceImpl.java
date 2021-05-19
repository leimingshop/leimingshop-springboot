/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.service.picture.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.leimingtech.commons.mybatis.service.impl.CrudServiceImpl;
import com.leimingtech.commons.tools.constant.Constant;
import com.leimingtech.commons.tools.page.PageData;
import com.leimingtech.commons.tools.utils.ConvertUtils;
import com.leimingtech.dao.picture.PictureDao;
import com.leimingtech.dto.picture.PictureDTO;
import com.leimingtech.dto.picture.SavePictureDTO;
import com.leimingtech.entity.picture.PictureEntity;
import com.leimingtech.service.picture.PictureService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 图片表
 *
 * @author chengQian
 * @email sunlightcs@gmail.com
 * @since 1.0 2019-05-10
 */
@Service
public class PictureServiceImpl extends CrudServiceImpl<PictureDao, PictureEntity, PictureDTO> implements PictureService {
    @Resource
    private PictureDao pictureDao;

    /**
     * 条件构造器
     *
     * @param params 查询参数
     * @return
     */
    @Override
    public QueryWrapper<PictureEntity> getWrapper(@RequestParam Map<String, Object> params) {
        String categoryId = (String) params.get("categoryId");
        String startTime = (String) params.get("startTime");
        String endTime = (String) params.get("endTime");
        String pictureName = (String) params.get("pictureName");
        String categoryName = (String) params.get("categoryName");
        String storeId = (String) params.get("storeId");
        if (StringUtils.isNotBlank(startTime) && StringUtils.isNotBlank(endTime)) {
            startTime = startTime + " 00:00:00";
            endTime = endTime + " 23:59:59";
            params.put("startTime", startTime);
            params.put("endTime", endTime);
        }

        QueryWrapper<PictureEntity> wrapper = new QueryWrapper<>();
        wrapper.ge(StringUtils.isNotBlank(startTime), Constant.CREATE_DATE, startTime);
        wrapper.le(StringUtils.isNotBlank(endTime), Constant.CREATE_DATE, endTime);
        wrapper.like(StringUtils.isNotBlank(pictureName), "picture_name", pictureName);
        wrapper.like(StringUtils.isNotBlank(categoryName), "category_name", categoryName);
        wrapper.eq(StringUtils.isNotBlank(storeId), "store_id", storeId);


        if (StringUtils.equals("0", categoryId)) {
            wrapper.isNull("category_id");
        } else {

            wrapper.eq(StringUtils.isNotBlank(categoryId), "category_id", categoryId);
        }


        return wrapper;
    }

    /**
     * 查询分组下图片的数量
     *
     * @param pictureCategoryEntityId 分组ID
     */
    @Override

    public Integer selectCategoryCount(@RequestParam(value = "pictureCategoryEntityId") Long pictureCategoryEntityId) {

        return pictureDao.selectCategoryCount(pictureCategoryEntityId);
    }


    /**
     * 批量修改图片分组
     *
     * @param ids        图片ID
     * @param categoryId 分组ID
     */
    @Override

    public void updateBatchByIds(@RequestBody Long[] ids, @RequestParam(value = "categoryId") Long categoryId) {
        List<PictureEntity> lmpictureEntityList = new ArrayList<>();
        for (Long pictureId : ids) {
            PictureEntity pictureEntity = new PictureEntity();
            pictureEntity.setId(pictureId);
            pictureEntity.setCategoryId(categoryId);
            lmpictureEntityList.add(pictureEntity);
        }
        updateBatchById(lmpictureEntityList);
    }

    /**
     * 批量上传图片
     *
     * @param savePictureDTO 图片信息
     */
    @Override

    public void savePicture(@RequestBody SavePictureDTO savePictureDTO) {

        PictureEntity pictureEntity = ConvertUtils.sourceToTarget(savePictureDTO, PictureEntity.class);
        insert(pictureEntity);
    }

    /***
     *
     * 根据ID查询图片信息
     * @param id 图片主键ID
     * @return
     */
    @Override

    public PictureDTO findById(Long id) {
        return super.get(id);
    }

    /**
     * 更新图片信息
     *
     * @param dto 图片
     */
    @Override

    public void update(@RequestBody PictureDTO dto) {
        super.update(dto);
    }

    /**
     * 导出查询
     *
     * @param params 查询参数
     * @return
     */

    @Override

    public List<PictureDTO> list(@RequestParam Map<String, Object> params) {
        return super.list(params);
    }

    /**
     * 根据ID删除图片信息
     *
     * @param ids 主键ids
     */
    @Override

    public void delete(@RequestBody Long[] ids) {
        super.delete(ids);
    }

    /**
     * 分页查询
     *
     * @param params 分页查询参数
     * @return
     */
    @Override

    public PageData<PictureDTO> page(@RequestParam Map<String, Object> params) {

        return super.page(params);
    }


}