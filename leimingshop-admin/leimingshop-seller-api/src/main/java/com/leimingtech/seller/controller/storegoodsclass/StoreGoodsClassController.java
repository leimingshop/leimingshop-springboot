/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.seller.controller.storegoodsclass;

import cn.hutool.core.collection.CollectionUtil;
import com.leimingtech.commons.tools.annotation.LogOperation;
import com.leimingtech.commons.tools.exception.ErrorCode;
import com.leimingtech.commons.tools.security.user.SellerDetail;
import com.leimingtech.commons.tools.utils.EasyExcelUtils;
import com.leimingtech.commons.tools.utils.Result;
import com.leimingtech.commons.tools.validator.AssertUtils;
import com.leimingtech.commons.tools.validator.ValidatorUtils;
import com.leimingtech.commons.tools.validator.group.AddGroup;
import com.leimingtech.commons.tools.validator.group.UpdateGroup;
import com.leimingtech.modules.dto.storegoodsclass.AddStoreGoodsClassDTO;
import com.leimingtech.modules.dto.storegoodsclass.StoreGoodsClassDTO;
import com.leimingtech.modules.dto.storegoodsclass.StoreGoodsClassListDTO;
import com.leimingtech.modules.dto.storegoodsclass.UpdateStoreGoodsClassDTO;
import com.leimingtech.modules.enums.evaluate.EvaluateErrorCodeEnum;
import com.leimingtech.modules.excel.goods.StoreGoodsClassTemplateExcel;
import com.leimingtech.modules.service.storegoodsclass.StoreGoodsClassService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * 店铺商品分类表
 *
 * @author chengqian chengqian@leimingtech.com
 * @since v1.0.0 2020-05-12
 */
@RestController
@RequestMapping("storegoodsclass")
@Api(tags = "店铺商品分类表")
@Slf4j
public class StoreGoodsClassController {

    @Autowired
    private StoreGoodsClassService storeGoodsClassService;

    /**
     * 分类列表
     *
     * @param params
     * @param sellerDetail
     * @return
     */
    @GetMapping("list")
    @ApiOperation("分类列表")
    public Result<List<StoreGoodsClassListDTO>> list(@RequestParam Map<String, Object> params, @ApiIgnore SellerDetail sellerDetail) {
        params.put("storeId", sellerDetail.getStoreId().toString());
        List<StoreGoodsClassListDTO> list = storeGoodsClassService.list(params);
        return new Result<List<StoreGoodsClassListDTO>>().ok(list);
    }

    /**
     * 查询所有一级分类
     *
     * @param sellerDetail
     * @return
     */
    @GetMapping("first/class")
    @ApiOperation("查询所有一级分类")
    public Result<List<StoreGoodsClassListDTO>> firstClass(@RequestParam(value = "type", required = false) Integer type, @ApiIgnore SellerDetail sellerDetail) {
        List<StoreGoodsClassListDTO> list = storeGoodsClassService.firstClass(sellerDetail.getStoreId(), type);
        return new Result<List<StoreGoodsClassListDTO>>().ok(list);
    }


    /**
     * 获取分类详情
     *
     * @param id
     * @return
     */
    @GetMapping("{id}")
    @ApiOperation("信息")
    public Result<StoreGoodsClassDTO> find(@PathVariable("id") Long id) {
        StoreGoodsClassDTO data = storeGoodsClassService.get(id);

        return new Result<StoreGoodsClassDTO>().ok(data);
    }

    /**
     * 保存分类
     *
     * @param dto
     * @return
     */
    @PostMapping
    @ApiOperation("保存")
    public Result save(@RequestBody AddStoreGoodsClassDTO dto, @ApiIgnore SellerDetail sellerDetail) {
        //效验数据
        ValidatorUtils.validateEntity(dto, AddGroup.class);
        dto.setStoreId(sellerDetail.getStoreId());

        //同一级别下分类名称是否重复
        Integer nameCount = storeGoodsClassService.classNameCount(dto.getGcName(), sellerDetail.getStoreId(), dto.getGcParentId(), 1, null);
        if (nameCount > 0) {
            return new Result().error(null, "分类名称不能重复");
        }
        // 校验子分类不能和父分类名称重复
        Integer parentNameCount = storeGoodsClassService.classNameCount(dto.getGcName(), sellerDetail.getStoreId(), dto.getGcParentId(), 2, null);
        if (parentNameCount > 0) {
            return new Result().error(null, "分类名称不能和上级分类名称重复");
        }

        storeGoodsClassService.save(dto);

        return new Result().ok(null, "保存成功");
    }

    /**
     * 更新分类
     *
     * @param dto
     * @return
     */
    @PutMapping
    @ApiOperation("修改")
    public Result update(@RequestBody UpdateStoreGoodsClassDTO dto, @ApiIgnore SellerDetail sellerDetail) {
        //效验数据
        ValidatorUtils.validateEntity(dto, UpdateGroup.class);
        //同一级别下分类名称是否重复
        Integer nameCount = storeGoodsClassService.classNameCount(dto.getGcName(), sellerDetail.getStoreId(), dto.getGcParentId(), 3, dto.getId());
        if (nameCount > 0) {
            return new Result().error(null, "分类名称不能重复");
        }
        // 校验子分类不能和父分类名称重复
        Integer parentNameCount = storeGoodsClassService.classNameCount(dto.getGcName(), sellerDetail.getStoreId(), dto.getGcParentId(), 2, null);
        if (parentNameCount > 0) {
            return new Result().error(null, "分类名称不能和上级分类名称重复");
        }
        dto.setStoreId(sellerDetail.getStoreId());
        storeGoodsClassService.update(dto);

        return new Result().ok(null, "更新成功");
    }

    @GetMapping("update/show")
    @ApiOperation("启用/禁用展示类目")
    @LogOperation("启用/禁用展示类目")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "展示类目ID", paramType = "query", required = true, dataType = "int"),
            @ApiImplicitParam(name = "showFlag", value = "前台展示（0不展示，默认为1展示）",
                    paramType = "query", required = true, dataType = "int")
    })
    public Result updateShowFlag(@ApiIgnore @RequestParam Map<String, Object> params, @ApiIgnore SellerDetail sellerDetail) {
        String id = params.get("id").toString();
        String showFlag = params.get("showFlag").toString();
        if (StringUtils.isNotBlank(id) && StringUtils.isNotBlank(showFlag)) {
            UpdateStoreGoodsClassDTO dto = new UpdateStoreGoodsClassDTO();
            dto.setShowFlag(Integer.valueOf(showFlag));
            dto.setId(Long.valueOf(id));
            dto.setStoreId(sellerDetail.getStoreId());
            storeGoodsClassService.update(dto);
        } else {
            return new Result().error("请选择正确的类目");
        }
        return new Result().ok(null, "操作成功");
    }

    /**
     * 删除分类
     *
     * @param ids
     * @return
     */
    @DeleteMapping
    @ApiOperation("删除")
    public Result delete(@RequestBody Long[] ids, @ApiIgnore SellerDetail sellerDetail) {
        //效验数据
        AssertUtils.isArrayEmpty(ids, "id不能为空");

        Map<String, Object> map = storeGoodsClassService.delete(ids, sellerDetail.getStoreId());

        if (CollectionUtil.isEmpty(map)) {
            return new Result().ok(null, "删除成功");
        }
        return new Result().error(Integer.valueOf(map.get("code").toString()), map.get("msg").toString());
    }

    @GetMapping("export/template")
    @ApiOperation("导出店铺商品分类模板")
    @LogOperation("导出店铺商品分类模板")
    public void exportTemplate(HttpServletResponse response) {
        List<StoreGoodsClassTemplateExcel> list = new ArrayList<>();
        try {
            EasyExcelUtils.exportExcelToTarget(response, null, list, StoreGoodsClassTemplateExcel.class);
        } catch (Exception e) {
            log.error("错误信息为" + e);
        }
    }

    @PostMapping(value = "import", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ApiOperation("根据模板从excel中导入")
    @LogOperation("导入商铺商品分类")
    public Result importFromExcel(@RequestPart("file") MultipartFile file, @ApiIgnore SellerDetail sellerDetail) {
        if (sellerDetail == null) {
            return new Result().error(ErrorCode.UNAUTHORIZED, "用户未登录");
        }
        try {

            storeGoodsClassService.importFromExcel(file, sellerDetail.getStoreId());
        } catch (Exception e) {
            log.error("导入商品分类文件异常，异常信息如下：" + e);
            return new Result().error(EvaluateErrorCodeEnum.UPLOAD_ERROR.hashCode(), "上传失败");
        }
        return new Result().ok(null, "上传成功");
    }
}