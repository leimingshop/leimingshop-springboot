/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.admin.controller.evaluate;

import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import com.leimingtech.admin.excel.evaluate.EvaluateGoodsExcel;
import com.leimingtech.commons.tools.annotation.LogContext;
import com.leimingtech.commons.tools.annotation.LogOperation;
import com.leimingtech.commons.tools.constant.Constant;
import com.leimingtech.commons.tools.page.PageData;
import com.leimingtech.commons.tools.utils.ConvertUtils;
import com.leimingtech.commons.tools.utils.EasyExcelUtils;
import com.leimingtech.commons.tools.utils.Result;
import com.leimingtech.commons.tools.validator.AssertUtils;
import com.leimingtech.commons.tools.validator.ValidatorUtils;
import com.leimingtech.commons.tools.validator.group.AddGroup;
import com.leimingtech.commons.tools.validator.group.DefaultGroup;
import com.leimingtech.modules.dto.evaluate.EvaluateGoodsDTO;
import com.leimingtech.modules.dto.evaluate.SaveEvaluateGoodsDTO;
import com.leimingtech.modules.dto.evaluate.UpdateEvaluateReadStateDTO;
import com.leimingtech.modules.enums.evaluate.EvaluateErrorCodeEnum;
import com.leimingtech.modules.service.evaluate.EvaluateGoodsService;
import com.leimingtech.modules.statuscode.GoodsStatusCode;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.util.List;
import java.util.Map;


/**
 * 商品评价管理
 *
 * @author chengqian
 * @since 7.0 2019-05-15
 */
@Slf4j
@RestController
@RequestMapping("evaluate")
@Api(tags = "商品评价管理")
public class EvaluateGoodsController {

    @Autowired
    private EvaluateGoodsService evaluateGoodsService;

    /**
     * 分页查询商品评价
     *
     * @param params 分页参数
     */
    @GetMapping("page")
    @ApiOperation("分页查询商品评价")
    @LogContext(code = GoodsStatusCode.ADMIN_EVALUATE_PAGE_SUCCESS, note = GoodsStatusCode.ADMIN_EVALUATE_PAGE_SUCCESS_MESSAGE)
    @ApiImplicitParams({
            @ApiImplicitParam(name = Constant.PAGE, value = "当前页码，从1开始", paramType = "query", required = true, dataType = "int"),
            @ApiImplicitParam(name = Constant.LIMIT, value = "每页显示记录数", paramType = "query", required = true, dataType = "int"),
            @ApiImplicitParam(name = Constant.ORDER_FIELD, value = "排序字段", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "goodsName", value = "商品名称", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "evaluateState", value = "评价状态", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "readState", value = "读取状态(0未读,1已读)", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "startDate", value = "开始时间", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "endDate", value = "结束时间", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = Constant.ORDER, value = "排序方式，可选值(asc、desc)", paramType = "query", dataType = "String")
    })
    public Result<PageData<EvaluateGoodsDTO>> page(@ApiIgnore @RequestParam Map<String, Object> params) {
        PageData<EvaluateGoodsDTO> page = evaluateGoodsService.page(params);

        return new Result<PageData<EvaluateGoodsDTO>>().ok(page);
    }

    /**
     * 根据id删除评论
     *
     * @param ids 评论表主键id'
     */
    @DeleteMapping
    @ApiOperation("根据id删除评论")
    @LogOperation("根据id删除评论")
    public Result delete(@RequestBody Long[] ids) {
        //效验数据
        AssertUtils.isArrayEmpty(ids, "ids不能为空");

        evaluateGoodsService.delete(ids);

        return new Result().ok(null, "删除成功");
    }


    /**
     * 新增商品评价
     *
     * @param dto 商品评价实体类
     * @return
     */
    @PostMapping
    @ApiOperation("新增商品评价")
    @LogOperation("新增商品评价")
    @LogContext(code = GoodsStatusCode.ADMIN_EVALUATE_SAVE_SUCCESS, note = GoodsStatusCode.ADMIN_EVALUATE_SAVE_SUCCESS_MESSAGE)
    public Result save(@RequestBody SaveEvaluateGoodsDTO dto) {
        //效验数据
        ValidatorUtils.validateEntity(dto, AddGroup.class, DefaultGroup.class);
        EvaluateGoodsDTO evaluateGoodsDTO = ConvertUtils.sourceToTarget(dto, EvaluateGoodsDTO.class);
        evaluateGoodsService.saveEvaluateGoods(evaluateGoodsDTO);

        return new Result().ok(null, "新增成功");
    }

    /**
     * 上传评论文件
     *
     * @param files 文件信息
     */
    @PostMapping("/upload")
    @ApiOperation("上传评论文件")
    public Result uploadExcel(@RequestParam MultipartFile files) {

        try {
            InputStream inputStream = files.getInputStream();
            ImportParams params = new ImportParams();
            params.setHeadRows(1);
            List<EvaluateGoodsExcel> evaluateGoodsExcels = ExcelImportUtil.importExcel(inputStream, EvaluateGoodsExcel.class, params);
            if (CollectionUtils.isNotEmpty(evaluateGoodsExcels)) {
                List<EvaluateGoodsDTO> evaluateGoodsDTOList = ConvertUtils.sourceToTarget(evaluateGoodsExcels, EvaluateGoodsDTO.class);
                //保存评价
                evaluateGoodsService.saveEvaluate(evaluateGoodsDTOList);
                return new Result().ok(null, "上传成功");
            }
        } catch (Exception e) {
            log.error("上传评价文件异常，异常信息如下：", e);
            return new Result().error(EvaluateErrorCodeEnum.UPLOAD_ERROR.value(), "上传失败");
        }

        return new Result().ok(null, "上传成功");
    }

    /**
     * 根据ID获取信息
     *
     * @param id 图片主键ID
     */
    @GetMapping("{id}")
    @ApiOperation("根据ID获取信息")
    @LogOperation("根据ID获取信息")
    public Result<EvaluateGoodsDTO> findById(@PathVariable("id") Long id) {

        EvaluateGoodsDTO data = evaluateGoodsService.findById(id, null);
        if (data == null) {
            return new Result<EvaluateGoodsDTO>().error(EvaluateErrorCodeEnum.NOT_BLANK.value(), "评论不存在");
        }
        return new Result<EvaluateGoodsDTO>().ok(data);
    }


    /**
     * 批量修改读取状态或者违规删除状态
     */
    @PutMapping("batch/read/state")
    @ApiOperation("批量修改读取状态或者违规删除状态")
    @LogOperation("批量修改读取状态或者违规删除状态")
    public Result<EvaluateGoodsDTO> updateReadState(@RequestBody UpdateEvaluateReadStateDTO evaluateReadStateDTO) {

        //批量修改已读
        boolean flag = evaluateGoodsService.updateState(evaluateReadStateDTO);
        if (!flag) {
            return new Result<EvaluateGoodsDTO>().error(null, "修改读取状态失败");
        }

        return new Result<EvaluateGoodsDTO>().ok(null, "修改读取状态成功");

    }

    /**
     * 导出记录
     *
     * @param params
     * @param response
     * @throws Exception
     */
    @GetMapping("export")
    @ApiOperation("导出记录")
    @LogOperation("导出记录")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "goodsName", value = "商品名称", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "evaluateState", value = "评价状态", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "startDate", value = "开始时间", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "endDate", value = "结束时间", paramType = "query", dataType = "String")
    })
    public void export(@ApiIgnore @RequestParam Map<String, Object> params, HttpServletResponse response) {
        List<EvaluateGoodsDTO> list = evaluateGoodsService.list(params);

        try {
            EasyExcelUtils.exportExcelToTarget(response, null, list, EvaluateGoodsExcel.class);
        } catch (Exception e) {
            log.error("错误信息为" + e);
        }
    }
}
