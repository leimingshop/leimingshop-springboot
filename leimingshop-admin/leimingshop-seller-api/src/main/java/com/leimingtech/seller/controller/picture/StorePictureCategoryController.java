/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.seller.controller.picture;

import com.leimingtech.commons.tools.annotation.LogOperation;
import com.leimingtech.commons.tools.constant.Constant;
import com.leimingtech.commons.tools.exception.ErrorCode;
import com.leimingtech.commons.tools.security.user.SellerDetail;
import com.leimingtech.commons.tools.utils.ConvertUtils;
import com.leimingtech.commons.tools.utils.Result;
import com.leimingtech.commons.tools.validator.ValidatorUtils;
import com.leimingtech.commons.tools.validator.group.AddGroup;
import com.leimingtech.commons.tools.validator.group.DefaultGroup;
import com.leimingtech.commons.tools.validator.group.UpdateGroup;
import com.leimingtech.dto.picture.*;
import com.leimingtech.enums.picture.ErrorCodeEnum;
import com.leimingtech.logging.MonitorLogger;
import com.leimingtech.logging.MonitorLoggerFactory;
import com.leimingtech.service.picture.PictureCategoryService;
import com.leimingtech.statuscode.SysStatusCode;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 图片分类管理
 *
 * @author chengQian
 * @since 1.0 2019-05-10
 */
@RestController
@RequestMapping("picture/category")
@Api(tags = "图片分类管理")
public class StorePictureCategoryController {
    private static MonitorLogger mlogger = MonitorLoggerFactory.getMonitorLogger(StorePictureCategoryController.class);
    @Autowired
    private PictureCategoryService pictureCategoryService;

    /**
     * 保存分组
     *
     * @param dto 保存分组实体
     */
    @PostMapping
    @ApiOperation("保存分组")
    @LogOperation("保存分组")
    public Result save(@RequestBody SavePictureCategoryDTO dto, @ApiIgnore SellerDetail sellerDetail) {
        //效验数据
        ValidatorUtils.validateEntity(dto, AddGroup.class, DefaultGroup.class);
        PictureCategoryDTO pictureCategoryDTO = ConvertUtils.sourceToTarget(dto, PictureCategoryDTO.class);
        if (sellerDetail == null) {
            return new Result().error(ErrorCode.UNAUTHORIZED, "用户未登录");
        }
        if (dto.getParentCategoryId() == null) {
            dto.setParentCategoryId(0L);
        }
        pictureCategoryDTO.setStoreId(sellerDetail.getStoreId());
        Integer code = pictureCategoryService.saveCategory(pictureCategoryDTO);
        Map<String, String> logMap = new HashMap<>(16);
        logMap.put("pictureCategoryDTO", pictureCategoryDTO.toString());
        mlogger.info(SysStatusCode.SELLER_PICTURE_CATEGORY_SAVE_SUCCESS,
                SysStatusCode.SELLER_PICTURE_CATEGORY_SAVE_SUCCESS_MESSAGE
                , logMap);

        if (code != Constant.SUCCESS) {
            return new Result().error(ErrorCodeEnum.CATEGORY_REPETITION.value(), "添加失败，同一文件夹下分组名称不可重复");
        }

        return new Result().ok(null, "保存成功");
    }

    /**
     * 修改图片分组信息
     *
     * @param dto 修改分组实体
     */
    @PutMapping
    @ApiOperation("修改图片分组信息")
    @LogOperation("修改图片分组信息")
    public Result update(@RequestBody UpdatePictureCategoryDTO dto, @ApiIgnore SellerDetail sellerDetail) {

        if (sellerDetail == null) {
            return new Result().error(ErrorCode.UNAUTHORIZED, "用户未登录");
        }
        //效验数据
        ValidatorUtils.validateEntity(dto, UpdateGroup.class, DefaultGroup.class);
        PictureCategoryDTO pictureCategoryDTO = ConvertUtils.sourceToTarget(dto, PictureCategoryDTO.class);
        pictureCategoryDTO.setStoreId(sellerDetail.getStoreId());
        Integer code = pictureCategoryService.updateCategoryById(pictureCategoryDTO);
        if (code != Constant.SUCCESS) {
            return new Result().error(ErrorCodeEnum.ILLEGAL_IMAGES.value(), "修改失败，同一文件夹下分组名称不可重复");
        }

        return new Result().ok(null, "保存成功");
    }

    /**
     * 删除分组
     *
     * @param deletePictureCategoryDTO 删除参数
     */

    @DeleteMapping
    @ApiOperation("删除分组")
    @LogOperation("删除分组")
    public Result delete(@RequestBody DeletePictureCategoryDTO deletePictureCategoryDTO) {

        pictureCategoryService.delete(deletePictureCategoryDTO);

        return new Result().ok(null, "删除成功");
    }


    /**
     * 查询所有图片分组
     */
    @GetMapping("all")
    @ApiOperation("查询所有图片分组")
    @LogOperation("查询所有图片分组")
    public Result<List<AllCategoryDTO>> getCategoryAll(@ApiIgnore SellerDetail sellerDetail) {
        if (sellerDetail == null) {
            return new Result<List<AllCategoryDTO>>().error(ErrorCode.UNAUTHORIZED, "用户未登录");
        }
        List<AllCategoryDTO> allCategoryDTOS = pictureCategoryService.selectCategoryAll(sellerDetail.getStoreId());

        return new Result<List<AllCategoryDTO>>().ok(allCategoryDTOS);
    }


}
