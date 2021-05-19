/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.admin.controller.goodsclass;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import com.leimingtech.admin.excel.GoodsClassExcel;
import com.leimingtech.commons.tools.annotation.LogContext;
import com.leimingtech.commons.tools.annotation.LogOperation;
import com.leimingtech.commons.tools.constant.Constant;
import com.leimingtech.commons.tools.exception.ErrorCode;
import com.leimingtech.commons.tools.page.PageData;
import com.leimingtech.commons.tools.utils.EasyExcelUtils;
import com.leimingtech.commons.tools.utils.Result;
import com.leimingtech.commons.tools.validator.AssertUtils;
import com.leimingtech.commons.tools.validator.ValidatorUtils;
import com.leimingtech.commons.tools.validator.group.AddGroup;
import com.leimingtech.commons.tools.validator.group.DefaultGroup;
import com.leimingtech.commons.tools.validator.group.UpdateGroup;
import com.leimingtech.modules.dto.goodsclass.*;
import com.leimingtech.modules.enums.evaluate.EvaluateErrorCodeEnum;
import com.leimingtech.modules.enums.goodsclass.GoodsClassErrorCodeEnum;
import com.leimingtech.modules.excel.goods.GoodsClassTemplateExcel;
import com.leimingtech.modules.service.goodsclass.GoodsClassService;
import com.leimingtech.modules.statuscode.GoodsStatusCode;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;


/**
 * 商品分类表
 *
 * @author huangkeyuan
 * @email huangkeyuan@leimingtech.com
 * @since 1.0.0 2019-05-22
 */
@Slf4j
@RestController
@RequestMapping("goodsclass")
@Api(tags = "商品分类")
public class GoodsClassController {
    @Autowired
    private GoodsClassService goodsClassService;

    @GetMapping("page")
    @ApiOperation("查询全部一级分类/根据父级分类查出子级分类(一级ID为0)")
    @ApiImplicitParams({
            @ApiImplicitParam(name = Constant.PAGE, value = "当前页码，从1开始", paramType = "query", required = true, dataType = "int"),
            @ApiImplicitParam(name = Constant.LIMIT, value = "每页显示记录数", paramType = "query", required = true, dataType = "int"),
            @ApiImplicitParam(name = Constant.ORDER_FIELD, value = "排序字段", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = Constant.ORDER, value = "排序方式，可选值(asc、desc)", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "gcParentId", value = "父ID;一级ID为0", paramType = "query", dataType = "Long")
    })
    public Result<PageData<GoodsClassDTO>> page(@ApiIgnore @RequestParam Map<String, Object> params) {
        PageData<GoodsClassDTO> page = goodsClassService.page(params);

        return new Result<PageData<GoodsClassDTO>>().ok(page);
    }

    @GetMapping("child/{id}")
    @ApiOperation("根据ID查询子级分类(一级ID为0)")
    public Result<List<GoodsClassDTO>> child(@PathVariable("id") Long id) {
        List<GoodsClassDTO> data = goodsClassService.selectListByParentId(id, null);
        return new Result<List<GoodsClassDTO>>().ok(data);
    }

    @GetMapping("{id}")
    @ApiOperation("根据分类id查询分类关联的规格、属性、品牌信息(一级ID为0)")
    public Result<GoodsClassDetailDTO> get(@PathVariable("id") Long id) {
        GoodsClassDetailDTO data = goodsClassService.getGoodsClassById(id);

        return new Result<GoodsClassDetailDTO>().ok(data, "查询成功");
    }

    @GetMapping("name/{id}")
    @ApiOperation("根据id查询分类名称(一级ID为0)")
    public Result<String> getGoodsClassName(@PathVariable("id") Long id) {
        String goodsClassName = goodsClassService.queryGoodsClassName(id);
        return new Result<String>().ok(goodsClassName, "查询成功");
    }

    @PostMapping
    @ApiOperation("保存商品分类信息")
    @LogOperation("保存商品分类信息")
    @LogContext(code = GoodsStatusCode.GOODS_CLASS_SAVE_OPERATION_CODE, note = GoodsStatusCode.GOODS_CLASS_SAVE_OPERATION_MESSAGE)
    public Result save(@RequestBody GoodsClassSaveDTO dto) {
        //效验数据
        ValidatorUtils.validateEntity(dto, AddGroup.class, DefaultGroup.class);
        //根据分类名称查询(防重校验)
        Integer className = goodsClassService.findByGcName(dto.getGcName(), dto.getGcParentId());
        if (className > 0) {
            return new Result().error(GoodsClassErrorCodeEnum.GOODS_GCNAME_REPEAT.value(), "商品分类名称不可重复");
        }

        if (dto.getGcParentId() != 0) {
            // 校验子分组名称是否和父分组名称一样
            Integer count = goodsClassService.findParentName(dto.getGcParentId(), dto.getGcName());
            if (count > 0) {
                return new Result().error("子分类名称不能和父分类名称一样");
            }
        }


        goodsClassService.saveGoodsClass(dto);
        return new Result().ok(null, "商品分类信息保存成功");
    }

    @PutMapping
    @ApiOperation("修改商品分类信息")
    @LogOperation("修改商品分类信息")
    @LogContext(code = GoodsStatusCode.GOODS_CLASS_UPDATE_OPERATION_CODE, note = GoodsStatusCode.GOODS_CLASS_UPDATE_OPERATION_MESSAGE)
    public Result update(@RequestBody GoodsClassUpdateDTO dto) {
        //效验数据
        ValidatorUtils.validateEntity(dto, UpdateGroup.class, DefaultGroup.class);
        //根据分类名称查询(防重校验)
        int updateCount = goodsClassService.gcNameConutCheck(dto);
        if (updateCount > 0) {
            return new Result().error(GoodsClassErrorCodeEnum.GOODS_GCNAME_REPEAT.value(), "商品分类名称不可重复");
        }

        if (dto.getGcParentId() != 0) {
            // 校验子分组名称是否和父分组名称一样
            Integer count = goodsClassService.findParentName(dto.getGcParentId(), dto.getGcName());
            if (count > 0) {
                return new Result().error("子分类名称不能和父分类名称一样");
            }
        }

        //验证当前分类是否可以编辑上级分类
        List<GoodsClassDTO> childrenList = goodsClassService.selectClassByParentId(dto.getId());
        GoodsClassDTO classDTO = goodsClassService.findById(dto.getId());
        if (null == childrenList || BeanUtil.isEmpty(classDTO)) {
            return new Result().error(ErrorCode.INTERNAL_SERVER_ERROR, "服务异常");
        }
        if (CollectionUtils.isNotEmpty(childrenList) && dto.getGcParentId().compareTo(classDTO.getGcParentId()) != 0) {
            return new Result().error("当前分类有子分类不能进行上级分类更改");
        }

        goodsClassService.updateGoodsClass(dto);
        return new Result().ok(null, "商品分类信息修改成功");
    }

    @DeleteMapping
    @ApiOperation("删除商品分类")
    @LogOperation("删除商品分类")
    @LogContext(code = GoodsStatusCode.GOODS_CLASS_DELETE_OPERATION_CODE, note = GoodsStatusCode.GOODS_CLASS_DELETE_OPERATION_MESSAGE)
    public Result delete(@RequestBody Long[] ids) {

        //效验数据
        AssertUtils.isArrayEmpty(ids, "分类ID不能为空");

        Map<String, Object> res = goodsClassService.deleteGoodsClassIds(ids);

        if (CollectionUtil.isEmpty(res)) {
            return new Result().ok(null, "商品分类信息删除成功");
        }

        return new Result().error(Integer.valueOf(res.get("code").toString()), res.get("msg").toString());
    }

    @GetMapping("export")
    @ApiOperation("导出商品分类")
    @LogOperation("导出商品分类")
    @LogContext(code = GoodsStatusCode.GOODS_CLASS_EXPORT_OPERATION_CODE, note = GoodsStatusCode.GOODS_CLASS_EXPORT_OPERATION_MESSAGE)
    public void export(@ApiIgnore @RequestParam Map<String, Object> params, HttpServletResponse response) {
        List<GoodsClassDTO> list = goodsClassService.list(params);

        try {
            EasyExcelUtils.exportExcelToTarget(response, null, list, GoodsClassExcel.class);
        } catch (Exception e) {
            log.error("错误信息为" + e);
        }
    }


    @GetMapping("all/goodclass")
    @ApiOperation("查询所有分类(分层级关系)")
    public Result<List<GoodsClassListDTO>> findAllGoodsClass() {
        List<GoodsClassListDTO> goodsClassCustomListDTOS = goodsClassService.selectAllGoodsClass(null);
        return new Result<List<GoodsClassListDTO>>().ok(goodsClassCustomListDTOS);
    }

    @GetMapping("brand/id")
    @ApiOperation("根据分类id查询品牌信息")
    public Result<List<GoodsClassBrandDTO>> findBrandByGcId(@RequestParam("gcClassId") Long gcClassId, @RequestParam(value = "brandName", required = false) String brandName) {
        List<GoodsClassBrandDTO> dtoList = goodsClassService.brandByGcId(gcClassId, brandName);
        return new Result<List<GoodsClassBrandDTO>>().ok(dtoList, "查询成功");
    }

    @GetMapping("export/template")
    @ApiOperation("下载分类导入的模板")
    public void export(HttpServletResponse response) {
        List<GoodsClassTemplateExcel> list = new ArrayList<>();
        try {
            EasyExcelUtils.exportExcelToTarget(response, new Date().toString() + "-后台分类模板", list, GoodsClassTemplateExcel.class);
        } catch (Exception e) {
            log.error("错误信息为" + e);
        }
    }

    @PostMapping(value = "import", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ApiOperation("导入后台分类")
    @LogContext(code = GoodsStatusCode.GOODS_CLASS_IMPORT_OPERATION_CODE, note = GoodsStatusCode.GOODS_CLASS_IMPORT_OPERATION_MESSAGE)
    public Result importGoodsClass(@RequestPart("files") MultipartFile files) {
        try {
            goodsClassService.importGoodsClass(files);
        } catch (Exception e) {
            log.error("导入商品分类文件异常，异常信息如下：", e);
            return new Result().error(EvaluateErrorCodeEnum.UPLOAD_ERROR.hashCode(), "上传失败");
        }
        return new Result().ok(null, "上传成功");
    }
}
