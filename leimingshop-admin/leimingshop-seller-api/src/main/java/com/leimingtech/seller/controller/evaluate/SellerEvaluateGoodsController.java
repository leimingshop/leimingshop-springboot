/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.seller.controller.evaluate;

import com.leimingtech.commons.tools.annotation.LogOperation;
import com.leimingtech.commons.tools.constant.Constant;
import com.leimingtech.commons.tools.exception.ErrorCode;
import com.leimingtech.commons.tools.page.PageData;
import com.leimingtech.commons.tools.security.user.SellerDetail;
import com.leimingtech.commons.tools.utils.EasyExcelUtils;
import com.leimingtech.commons.tools.utils.Result;
import com.leimingtech.commons.tools.validator.AssertUtils;
import com.leimingtech.exception.ServiceException;
import com.leimingtech.modules.dto.evaluate.EvaluateGoodsDTO;
import com.leimingtech.modules.dto.evaluate.UpdateEvaluateReadStateDTO;
import com.leimingtech.modules.enums.evaluate.EvaluateErrorCodeEnum;
import com.leimingtech.modules.service.evaluate.EvaluateGoodsService;
import com.leimingtech.seller.excel.evaluate.EvaluateGoodsExcel;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletResponse;
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
@Api(tags = "seller评价管理")
public class SellerEvaluateGoodsController {

    @Autowired
    private EvaluateGoodsService evaluateGoodsService;

    /**
     * 分页查询商品评价
     *
     * @param params 分页参数
     */
    @GetMapping("page")
    @ApiOperation("分页查询商品评价")
    @ApiImplicitParams({
            @ApiImplicitParam(name = Constant.PAGE, value = "当前页码，从1开始", paramType = "query", required = true, dataType = "int"),
            @ApiImplicitParam(name = Constant.LIMIT, value = "每页显示记录数", paramType = "query", required = true, dataType = "int"),
            @ApiImplicitParam(name = Constant.ORDER_FIELD, value = "排序字段", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "goodsName", value = "商品名称", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "evaluateState", value = "评价状态", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "replyState", value = "回复状态(0 未回复 1 已回复)", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "readState", value = "读取状态(0未读,1已读)", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "startDate", value = "开始时间", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "endDate", value = "结束时间", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = Constant.ORDER, value = "排序方式，可选值(asc、desc)", paramType = "query", dataType = "String")
    })
    public Result<PageData<EvaluateGoodsDTO>> page(@ApiIgnore @RequestParam Map<String, Object> params, @ApiIgnore SellerDetail sellerDetail) {
        if (sellerDetail == null) {
            return new Result<PageData<EvaluateGoodsDTO>>().error(ErrorCode.UNAUTHORIZED, "用户未登录");
        }
        params.put("storeId", sellerDetail.getStoreId().toString());
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
     * 批量显示 、隐藏 、回复
     *
     * @param updateEvaluateGoodsDTO
     * @return
     */
    @PutMapping("batch")
    @ApiOperation("批量显示 、隐藏 、回复")
    @LogOperation("批量显示 、隐藏 、回复")
    public Result<EvaluateGoodsDTO> batchUpdate(@RequestBody UpdateEvaluateReadStateDTO updateEvaluateGoodsDTO) {

        evaluateGoodsService.updateState(updateEvaluateGoodsDTO);

        return new Result<EvaluateGoodsDTO>().ok(null, "操作成功");

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
    public void export(@ApiIgnore @RequestParam Map<String, Object> params, HttpServletResponse response, @ApiIgnore SellerDetail sellerDetail) {
        if (sellerDetail == null || sellerDetail.getId() == null) {
            throw new ServiceException("用户未登录");
        }
        params.put("storeId", sellerDetail.getStoreId().toString());
        List<EvaluateGoodsDTO> list = evaluateGoodsService.list(params);

        try {
            EasyExcelUtils.exportExcelToTarget(response, null, list, EvaluateGoodsExcel.class);
        } catch (Exception e) {
            log.error("错误信息为" + e);
        }
    }
}
