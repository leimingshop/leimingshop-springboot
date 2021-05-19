/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.seller.controller.picture;

import com.leimingtech.commons.tools.annotation.LogOperation;
import com.leimingtech.commons.tools.constant.Constant;
import com.leimingtech.commons.tools.enums.ResultCodeEnum;
import com.leimingtech.commons.tools.exception.ErrorCode;
import com.leimingtech.commons.tools.page.PageData;
import com.leimingtech.commons.tools.security.user.SellerDetail;
import com.leimingtech.commons.tools.utils.ImageCheckUtils;
import com.leimingtech.commons.tools.utils.Result;
import com.leimingtech.commons.tools.utils.file.Base64Util;
import com.leimingtech.commons.tools.validator.AssertUtils;
import com.leimingtech.commons.tools.validator.ValidatorUtils;
import com.leimingtech.commons.tools.validator.group.AddGroup;
import com.leimingtech.commons.tools.validator.group.DefaultGroup;
import com.leimingtech.commons.tools.validator.group.UpdateGroup;
import com.leimingtech.dto.picture.PictureCategoryDTO;
import com.leimingtech.dto.picture.PictureDTO;
import com.leimingtech.dto.picture.SavePictureDTO;
import com.leimingtech.enums.picture.ErrorCodeEnum;
import com.leimingtech.logging.MonitorLogger;
import com.leimingtech.logging.MonitorLoggerFactory;
import com.leimingtech.service.picture.PictureCategoryService;
import com.leimingtech.service.picture.PictureService;
import com.leimingtech.upload.dto.Base64FileDTO;
import com.leimingtech.upload.dto.Base64PictureDTO;
import com.leimingtech.upload.dto.UploadDTO;
import com.leimingtech.upload.service.UploadService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.List;
import java.util.Map;


/**
 * 图片管理
 *
 * @author chengQian
 * @since 1.0 2019-05-10
 */
@Slf4j
@RestController
@RequestMapping("picture")
@Api(tags = "图片管理")
public class StorePictureController {
    private static MonitorLogger mlogger = MonitorLoggerFactory.getMonitorLogger(StorePictureController.class);
    @Autowired
    private PictureService pictureService;
    @Autowired
    private UploadService uploadService;
    @Autowired
    private PictureCategoryService pictureCategoryService;

    /**
     * 分页查询图片信息
     *
     * @param params 分页参数map
     */
    @GetMapping("page")
    @ApiOperation("分页查询图片信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = Constant.PAGE, value = "当前页码，从1开始", paramType = "query", required = true, dataType = "int"),
            @ApiImplicitParam(name = Constant.LIMIT, value = "每页显示记录数", paramType = "query", required = true, dataType = "int"),
            @ApiImplicitParam(name = Constant.ORDER_FIELD, value = "排序字段", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "categoryId", value = "分组ID", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "startTime", value = "开始时间", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "endTime", value = "结束时间", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "pictureName", value = "图片名称", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "categoryName", value = "分组名称", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = Constant.ORDER, value = "排序方式，可选值(asc、desc)", paramType = "query", dataType = "String")
    })
    public Result<PageData<PictureDTO>> page(@ApiIgnore @RequestParam Map<String, Object> params, @ApiIgnore SellerDetail sellerDetail) {
        if (sellerDetail == null) {
            new Result<PageData<PictureDTO>>().error(ErrorCode.UNAUTHORIZED, "用户未登录");
        }
        if (sellerDetail.getStoreId() != null) {
            params.put("storeId", sellerDetail.getStoreId().toString());
        } else {
            return new Result<PageData<PictureDTO>>().ok(null);
        }
        PageData<PictureDTO> page = pictureService.page(params);

        return new Result<PageData<PictureDTO>>().ok(page);
    }

    /**
     * 根据ID获取信息
     *
     * @param id 图片主键ID
     */
    @GetMapping("{id}")
    @ApiOperation("根据ID获取图片")
    public Result<PictureDTO> findById(@PathVariable("id") Long id) {

        PictureDTO data = pictureService.findById(id);
        if (data == null) {
            return new Result().error(ErrorCodeEnum.PICTURE_IS_NULL.value(), "图片不存在");
        }
        return new Result<PictureDTO>().ok(data);
    }

    /**
     * 保存图片数据
     *
     * @param imgList 上传的文件集合
     * @param id      分组ID
     */
    @PostMapping
    @ApiOperation("批量保存图片")
    public Result save(
            @RequestParam(required = false, value = "id") Long id,
            @RequestParam(required = false, value = "type") Integer type,
            @RequestBody List<Base64PictureDTO> imgList,
            @ApiIgnore SellerDetail sellerDetail) throws Exception {
        if (sellerDetail == null) {
            return new Result().error(ErrorCode.UNAUTHORIZED, "用户未登录");
        }
        Result<UploadDTO> result = new Result<>();
        // 查出分组信息
        PictureCategoryDTO pictureCategoryDTO = new PictureCategoryDTO();
        // 限制图片格式
        String imageTypes = "gif,jpg,jpeg,png,bmp,PNG,JEPG,JPG";
        if (id != null) {
            pictureCategoryDTO = pictureCategoryService.findById(id);
        }
        for (Base64PictureDTO base64PictureDTO : imgList) {
            //效验数据
            ValidatorUtils.validateEntity(base64PictureDTO, AddGroup.class, DefaultGroup.class);
            // 图片大小、格式校验
            Map<String, Object> imageMap = ImageCheckUtils.isImage(base64PictureDTO.getImgStr());
            String code = (String) imageMap.get("code");
            if (StringUtils.isNotBlank(code) && !"200".equals(code)) {
                return new Result().error((String) imageMap.get("msg"));
            }

            // 获得图片格式
            String imageType = String.valueOf(imageMap.get("imageType"));
            // 获取图片大小
            Long picSize = Long.valueOf(String.valueOf(imageMap.get("imageSize")));

            // 将图片base64字符串转化为byte数组
            byte[] base64ByteArr = Base64Util.imgBase64ToByte(base64PictureDTO.getImgStr());

            // 构建文件上传实体
            Base64FileDTO base64FileDTO = new Base64FileDTO();
            if (StringUtils.isNotBlank(base64PictureDTO.getPictureName())) {
                base64FileDTO.setFileRealName(base64PictureDTO.getPictureName());
            } else {
                base64FileDTO.setFileRealName(System.currentTimeMillis() + "." + imageType);
            }
            base64FileDTO.setFileSize(picSize);
            base64FileDTO.setFileSuffix(imageType);
            base64FileDTO.setBase64ByteArr(base64ByteArr);
            base64FileDTO.setType(type);
            Map<String, Object> resultMap = uploadService.uploadPicBase64(base64FileDTO);
            if (ErrorCode.SUCCESS == (int) resultMap.get("code")) {
                UploadDTO uploadDTO = (UploadDTO) resultMap.get("data");
                if (uploadDTO.getUrl() == null || uploadDTO.getSize() == null) {
                    return new Result<UploadDTO>().error(ErrorCodeEnum.ILLEGAL_IMAGES.value(), "无法获取图片路径和大小");
                } else {
                    SavePictureDTO savePictureDTO = new SavePictureDTO();
                    savePictureDTO.setCategoryId(id);
                    savePictureDTO.setStoreId(sellerDetail.getStoreId());
                    if (pictureCategoryDTO != null) {
                        savePictureDTO.setCategoryName(pictureCategoryDTO.getCategoryName());
                    }
                    savePictureDTO.setPicturePath(uploadDTO.getUrl());
                    savePictureDTO.setPictureName(base64FileDTO.getFileRealName());
                    pictureService.savePicture(savePictureDTO);
                }
            } else {
                return new Result<UploadDTO>().error((int) resultMap.get("code"), (String) resultMap.get("msg"));
            }
        }
        return new Result().ok(null, "上传成功");
    }

    /**
     * 功能描述:
     * 〈base64图片上传〉
     *
     * @param id               分组id
     * @param base64PictureDTO base64文件实体
     * @author : 刘远杰
     */
    @PostMapping("base64")
    @ApiOperation("保存base64图片")
    public Result<UploadDTO> save(
            @RequestParam(required = false, value = "id") Long id,
            @RequestParam(required = false, value = "type") Integer type,
            @RequestBody Base64PictureDTO base64PictureDTO,
            @ApiIgnore SellerDetail sellerDetail) throws Exception {

        Result<UploadDTO> result = new Result<>();
        //效验数据
        ValidatorUtils.validateEntity(base64PictureDTO, AddGroup.class, DefaultGroup.class);
        // 图片大小、格式校验
        Map<String, Object> imageMap = ImageCheckUtils.isImage(base64PictureDTO.getImgStr());
        String code = (String) imageMap.get("code");
        if (StringUtils.isNotBlank(code) && !"200".equals(code)) {
            return new Result().error((String) imageMap.get("msg"));
        }

        // 获得图片格式
        String imageType = String.valueOf(imageMap.get("imageType"));
        // 获取图片大小
        Long picSize = Long.valueOf(String.valueOf(imageMap.get("imageSize")));

        // 将图片base64字符串转化为byte数组
        byte[] base64ByteArr = Base64Util.imgBase64ToByte(base64PictureDTO.getImgStr());
        // 1.获取图片分组
        PictureCategoryDTO pictureCategoryDTO = new PictureCategoryDTO();
        if (id != null) {
            pictureCategoryDTO = pictureCategoryService.findById(id);
        }

        // 构建文件上传实体
        Base64FileDTO base64FileDTO = new Base64FileDTO();
        if (StringUtils.isNotBlank(base64PictureDTO.getPictureName())) {
            base64FileDTO.setFileRealName(base64PictureDTO.getPictureName());
        } else {
            base64FileDTO.setFileRealName(System.currentTimeMillis() + "." + imageType);
        }
        base64FileDTO.setFileSize(picSize);
        base64FileDTO.setFileSuffix(imageType);
        base64FileDTO.setBase64ByteArr(base64ByteArr);
        base64FileDTO.setType(type);

        Map<String, Object> resultMap = uploadService.uploadPicBase64(base64FileDTO);
        if (ErrorCode.SUCCESS == (int) resultMap.get("code")) {
            UploadDTO uploadDTO = (UploadDTO) resultMap.get("data");
            if (uploadDTO.getUrl() == null || uploadDTO.getSize() == null) {
                result = new Result<UploadDTO>().error(ErrorCodeEnum.ILLEGAL_IMAGES.value(), "无法获取图片路径和大小");
            } else {
                SavePictureDTO savePictureDTO = new SavePictureDTO();
                savePictureDTO.setCategoryId(id);
                savePictureDTO.setStoreId(sellerDetail.getStoreId());
                if (pictureCategoryDTO != null) {
                    savePictureDTO.setCategoryName(pictureCategoryDTO.getCategoryName());
                }
                savePictureDTO.setPicturePath(uploadDTO.getUrl());
                savePictureDTO.setPictureName(base64FileDTO.getFileRealName());
                pictureService.savePicture(savePictureDTO);
                result = new Result<UploadDTO>().ok(uploadDTO, "上传成功");
                mlogger.info(String.valueOf(ResultCodeEnum.SUCCESS.value()), "图片上传成功");
            }
        } else {
            result = new Result<UploadDTO>().error((int) resultMap.get("code"), (String) resultMap.get("msg"));
        }

        return result;

    }

    /**
     * 修改图片数据
     *
     * @param dto 图片实体
     */
    @PutMapping
    @ApiOperation("修改图片数据")
    @LogOperation("修改图片数据")
    public Result update(@RequestBody PictureDTO dto) {
        //效验数据
        ValidatorUtils.validateEntity(dto, UpdateGroup.class, DefaultGroup.class);
        pictureService.update(dto);

        return new Result().ok(null, "修改成功");
    }

    /**
     * 删除图片
     *
     * @param ids 图片主键ID数组
     * @return
     */
    @DeleteMapping
    @ApiOperation("删除图片")
    @LogOperation("删除图片")
    public Result delete(@RequestBody Long[] ids) {
        //效验数据
        AssertUtils.isArrayEmpty(ids, "请选择要删除的图片");

        pictureService.delete(ids);


        return new Result().ok(null, "删除成功");
    }

    /**
     * 批量修改分组
     *
     * @param ids        图片id数组
     * @param categoryId 分组id
     */
    @PutMapping("/update/batch")
    @ApiOperation("批量修改分组")
    @LogOperation("批量修改分组")
    public Result updateBatch(@RequestBody Long[] ids,
                              @RequestParam(required = true, value = "categoryId") Long categoryId) {
        //效验数据
        AssertUtils.isArrayEmpty(ids, "图片ID不能为空");
        AssertUtils.isNull(categoryId, "图片分组ID不能为空");
        pictureService.updateBatchByIds(ids, categoryId);

        return new Result().ok(null, "修改成功");
    }

}
