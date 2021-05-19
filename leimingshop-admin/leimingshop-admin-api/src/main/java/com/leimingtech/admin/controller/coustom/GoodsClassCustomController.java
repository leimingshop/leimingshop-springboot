/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.admin.controller.coustom;

import cn.hutool.core.bean.BeanUtil;
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
import com.leimingtech.logging.MonitorLogger;
import com.leimingtech.logging.MonitorLoggerFactory;
import com.leimingtech.modules.constants.custom.ResultCode;
import com.leimingtech.modules.dto.custom.*;
import com.leimingtech.modules.enums.custom.ShowFlagEnum;
import com.leimingtech.modules.enums.evaluate.EvaluateErrorCodeEnum;
import com.leimingtech.modules.excel.GoodsClassCustomTemplateExcel;
import com.leimingtech.modules.service.custom.GoodsClassCustomService;
import com.leimingtech.modules.statuscode.GoodsStatusCode;
import com.leimingtech.modules.statuscode.OperationStatusCode;
import com.leimingtech.modules.vo.goodsclasscustom.GoodsClassCustomImportVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import springfox.documentation.annotations.ApiIgnore;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;


/**
 * 商品自定义分类表
 *
 * @author xuzhch
 * @email 170518@163.dao
 * @since 1.0.0 2019-05-22
 */
@RestController
@RequestMapping("goods/classcustom")
@Api(tags = "商品展示类目管理")
@Slf4j
public class GoodsClassCustomController {

    MonitorLogger monitorLogger = MonitorLoggerFactory.getMonitorLogger(GoodsClassCustomController.class);

    @Resource
    private GoodsClassCustomService goodsClassCustomService;

    @GetMapping("page")
    @ApiOperation("分页")
    @ApiImplicitParams({
            @ApiImplicitParam(name = Constant.PAGE, value = "当前页码，从1开始", paramType = "query", required = true, dataType = "int"),
            @ApiImplicitParam(name = Constant.LIMIT, value = "每页显示记录数", paramType = "query", required = true, dataType = "int"),
            @ApiImplicitParam(name = Constant.ORDER_FIELD, value = "排序字段", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = Constant.ORDER, value = "排序方式，可选值(asc、desc)", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "gcParentId", value = "父ID;一级ID为0", paramType = "query", dataType = "Long")
    })
    public Result<PageData<GoodsClassCustomDTO>> page(@ApiIgnore @RequestParam Map<String, Object> params) {
        PageData<GoodsClassCustomDTO> page = goodsClassCustomService.page(params);
        return new Result<PageData<GoodsClassCustomDTO>>().ok(page);
    }

    /**
     * 查询展示分类
     *
     * @param showType 0 H5展示分类 1 pc展示分类
     * @return
     */
    @GetMapping("all")
    @ApiOperation("查询所有分类(分层级关系)")
    public Result<List<GoodsClassCustomListDTO>> selectAllCustom(@RequestParam(value = "showType", required = false) Integer showType) {
        List<GoodsClassCustomListDTO> goodsClassCustomListDTOS = goodsClassCustomService.selectAllCustom(showType);
        return new Result<List<GoodsClassCustomListDTO>>().ok(goodsClassCustomListDTOS);
    }

    @GetMapping("custom/tree")
    @ApiOperation("首页楼层查询分类(返回树形结构)")
    public Result<List<GoodsClassCustomTreeDTO>> sellectCustomTree(@RequestParam(value = "showType", required = false) Integer showType) {
        List<GoodsClassCustomTreeDTO> goodsClassCustomListDTOS = goodsClassCustomService.sellectCustomTree(showType);
        return new Result<List<GoodsClassCustomTreeDTO>>().ok(goodsClassCustomListDTOS);
    }


    @GetMapping("child/{id}")
    @ApiOperation("根据ID查询子级分类")
    public Result<List<GoodsClassCustomDTO>> child(@PathVariable("id") Long id) {
        List<GoodsClassCustomDTO> data = goodsClassCustomService.selectListByParentId(id, null, null);
        return new Result<List<GoodsClassCustomDTO>>().ok(data);
    }

    @GetMapping("child/show/{id}")
    @ApiOperation("根据ID查询正在展示中的子级分类")
    public Result<List<GoodsClassCustomDTO>> childShow(@PathVariable("id") Long id) {
        List<GoodsClassCustomDTO> data = goodsClassCustomService.selectListByParentId(id, ShowFlagEnum.YES.value(), null);
        return new Result<List<GoodsClassCustomDTO>>().ok(data);
    }

    @GetMapping("name/{id}")
    @ApiOperation("根据ID获取名称(新增下级使用)")
    public Result<String> addChild(@PathVariable("id") Long id) {
        String parentName = goodsClassCustomService.selectNameById(id);
        return new Result<String>().ok(parentName);
    }


    @GetMapping("{id}")
    @ApiOperation("查询展示类目详情")
    public Result<GoodsClassCustomDetailDTO> get(@PathVariable("id") Long id) {
        GoodsClassCustomDetailDTO data = goodsClassCustomService.selectDetailById(id);
        return new Result<GoodsClassCustomDetailDTO>().ok(data);
    }


    @PostMapping
    @ApiOperation("保存商品展示类目")
    @LogOperation("保存商品展示类目")
    @LogContext(code = OperationStatusCode.CUSTOM_SAVE_OPERATION_CODE, note = OperationStatusCode.CUSTOM_SAVE_OPERATION_MESSAGE)
    public Result save(@RequestBody GoodsClassCustomSaveDTO dto) {
        //效验数据
        ValidatorUtils.validateEntity(dto, AddGroup.class, DefaultGroup.class);
        if (dto.getClassId() == 0) {
            return new Result().error("后台分类不能为空");
        }
        // 验证分类名称是否重复
        Integer count = goodsClassCustomService.findClassName(dto.getGcParentId(), dto.getGcName(), dto.getShowType());
        if (count > 0) {
            return new Result().error("分类名称不能重复");
        }
        // 校验子分组名称不能和父级分组名称一样
        if (dto.getGcParentId() != 0) {
            // 查询出父级分组 的名称
            Integer parentName = goodsClassCustomService.findParentName(dto.getGcParentId(), dto.getGcName());
            if (parentName > 0) {
                return new Result().error("分类名称不能与上级分类名称重复");
            }

        }

        goodsClassCustomService.save(dto);
        return new Result().ok(null, "商品展示类目保存成功");
    }


    @PutMapping
    @ApiOperation("修改商品展示类目")
    @LogOperation("修改商品展示类目")
    @LogContext(code = OperationStatusCode.CUSTOM_UPDATE_OPERATION_CODE, note = OperationStatusCode.CUSTOM_UPDATE_OPERATION_MESSAGE)
    public Result update(@RequestBody GoodsClassCustomUpdateDTO dto) {
        //效验数据
        ValidatorUtils.validateEntity(dto, UpdateGroup.class, DefaultGroup.class);
        if (dto.getClassId() == 0) {
            return new Result().error("后台分类不能为空");
        }

        // 验证分类名称是否重复
        Integer count = goodsClassCustomService.updateVerifyClassName(dto.getGcParentId(), dto.getGcName(), dto.getId(), dto.getShowType());
        if (count > 0) {
            return new Result().error("分类名称不能重复");
        }

        //查询当前分类是否有子分类，如果有则不能更改上级分类
        GoodsClassCustomDetailDTO goodsClassCustomDetailDTO = goodsClassCustomService.selectDetailById(dto.getId());
        List<GoodsClassCustomDTO> childrenList = goodsClassCustomService.selectListByParentId(dto.getId(), null, dto.getShowType());
        if (null == childrenList || BeanUtil.isEmpty(goodsClassCustomDetailDTO)) {
            return new Result().error(ErrorCode.INTERNAL_SERVER_ERROR, "服务异常");
        }
        if (CollectionUtils.isNotEmpty(childrenList) && goodsClassCustomDetailDTO.getGcParentId().compareTo(dto.getGcParentId()) != 0) {
            return new Result().error("当前分类有子分类不能进行上级分类更改");
        }

        //验证当前分类是否可以编辑上级分类
        if (dto.getGcParentId() != null) {
            //查询当前分类是否有子分类  如果有则不能更改分类
            List<GoodsClassCustomDTO> list = goodsClassCustomService.findGcSon(dto.getId());
            if (list.size() > 0) {
                return new Result().error("当前分类有子分类不能进行上级分类更改");
            }
        }

        goodsClassCustomService.update(dto);
        return new Result().ok(null, "商品展示类目修改成功");

    }


    @DeleteMapping
    @ApiOperation("删除商品展示类目及其子分类")
    @LogOperation("删除商品展示类目及其子分类")
    @LogContext(code = OperationStatusCode.CUSTOM_DELETE_BRATCH_OPERATION_CODE,
            note = OperationStatusCode.CUSTOM_DELETE_BRATCH_OPERATION_MESSAGE)
    public Result deleteCorrelationIds(@RequestBody Long[] ids) {
        //效验数据
        AssertUtils.isArrayEmpty(ids, "展示类目ID不能为空");
        goodsClassCustomService.deleteCorrelationIds(ids);
        return new Result().ok(null, "商品展示类目删除成功");

    }


    @DeleteMapping("{id}")
    @ApiOperation("根据ID删除商品展示类目")
    @LogOperation("根据ID删除商品展示类目")
    @LogContext(code = OperationStatusCode.CUSTOM_DELETE_OPERATION_CODE, note = OperationStatusCode.CUSTOM_DELETE_OPERATION_MESSAGE)
    public Result delete(@PathVariable("id") Long id) {
        //效验数据
        AssertUtils.isNull(id, "ID不能为空");
        Integer result = goodsClassCustomService.deleteCustomById(id);
        if (result == ResultCode.SUCCESS) {
            return new Result().ok(null, "商品展示类目删除成功");
        }
        return new Result().error(ResultCode.ERR_PARAMS, "请先删除子级分类");
    }


    @GetMapping("export")
    @ApiOperation("导出展示类目信息")
    @LogOperation("导出展示类目信息")
    @LogContext(code = OperationStatusCode.CUSTOM_EXPORT_OPERATION_CODE, note = OperationStatusCode.CUSTOM_EXPORT_OPERATION_MESSAGE)
    public void export(@ApiIgnore @RequestParam Map<String, Object> params, HttpServletResponse response) {

        ArrayList<GoodsClassCustomTemplateExcel> goodsClassCustomTemplateExcels = new ArrayList<>();
        try {
            EasyExcelUtils.exportExcelToTarget(response, new Date().toString() + "-展示分类模板", goodsClassCustomTemplateExcels, GoodsClassCustomTemplateExcel.class);
        } catch (Exception e) {
            log.error("错误信息为:" + e);
        }
    }


    @GetMapping("update/show")
    @ApiOperation("启用/禁用展示类目")
    @LogOperation("启用/禁用展示类目")
    @LogContext(code = OperationStatusCode.CUSTOM_UPDATE_SHOW_BRATCH_OPERATION_CODE,
            note = OperationStatusCode.CUSTOM_UPDATE_SHOW_OPERATION_MESSAGE)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "展示类目ID", paramType = "query", required = true, dataType = "int"),
            @ApiImplicitParam(name = "showFlag", value = "前台展示（0不展示，默认为1展示）",
                    paramType = "query", required = true, dataType = "int")
    })
    public Result updateShowFlag(@ApiIgnore @RequestParam Map<String, Object> params) {
        String id = params.get("id").toString();
        String showFlag = params.get("showFlag").toString();
        if (StringUtils.isNotBlank(id) && StringUtils.isNotBlank(showFlag)) {
            goodsClassCustomService.updateShowFlag(params);
        } else {
            return new Result().error("请选择正确的类目");
        }
        return new Result().ok(null, "操作成功");
    }

    @GetMapping("export/template")
    @ApiOperation("商品分类展示模板")
    @LogOperation("导出展示模板")
    public void getExportTemplate(HttpServletResponse response) {
        List<GoodsClassCustomTemplateExcel> list = new ArrayList<>();
        try {
            EasyExcelUtils.exportExcelToTarget(response, new Date().toString() + "-展示分类模板", list, GoodsClassCustomTemplateExcel.class);
        } catch (Exception e) {
            log.error("错误信息为" + e);
        }
    }

    @PostMapping(value = "import", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ApiOperation("导入展示分类")
    @LogContext(code = GoodsStatusCode.GOODS_CLASS_IMPORT_OPERATION_CODE, note = GoodsStatusCode.GOODS_CLASS_IMPORT_OPERATION_CODE)
    public Result<GoodsClassCustomImportVo> importGoodsClassCustom(@RequestPart("files") MultipartFile files) {
        try {
            GoodsClassCustomImportVo goodsClassCustomImportVo = goodsClassCustomService.importGoodsClassCustom(files);
            if (CollectionUtils.isEmpty(goodsClassCustomImportVo.getErrorMessage())) {
                return new Result<GoodsClassCustomImportVo>().ok(goodsClassCustomImportVo, "上传成功");
            } else {
                return new Result<GoodsClassCustomImportVo>().error(500, goodsClassCustomImportVo.getErrorMessage().toString());
            }
        } catch (Exception e) {
            log.error("导入商品分类文件异常，异常信息如下：", e);
            return new Result<GoodsClassCustomImportVo>().error(EvaluateErrorCodeEnum.UPLOAD_ERROR.hashCode(), "上传失败");
        }
    }
}