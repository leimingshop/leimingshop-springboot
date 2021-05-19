/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.admin.controller.labelrecommend;

import com.leimingtech.commons.tools.annotation.LogContext;
import com.leimingtech.commons.tools.constant.Constant;
import com.leimingtech.commons.tools.page.PageData;
import com.leimingtech.commons.tools.utils.ConvertUtils;
import com.leimingtech.commons.tools.utils.Result;
import com.leimingtech.commons.tools.validator.AssertUtils;
import com.leimingtech.commons.tools.validator.ValidatorUtils;
import com.leimingtech.commons.tools.validator.group.AddGroup;
import com.leimingtech.commons.tools.validator.group.DefaultGroup;
import com.leimingtech.commons.tools.validator.group.UpdateGroup;
import com.leimingtech.modules.dto.goods.GoodsListDTO;
import com.leimingtech.modules.dto.labelrecommend.LabelRecommendDTO;
import com.leimingtech.modules.dto.labelrecommend.LabelRecommendRelSaveDTO;
import com.leimingtech.modules.dto.labelrecommend.LabelRecommendSaveDTO;
import com.leimingtech.modules.service.goods.GoodsService;
import com.leimingtech.modules.service.labelrecommend.LabelRecommendRelService;
import com.leimingtech.modules.service.labelrecommend.LabelRecommendService;
import com.leimingtech.modules.statuscode.GoodsLabelRecommendCode;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.List;
import java.util.Map;


/**
 * 标签推荐管理
 *
 * @author weixianchun @leimingtech.com
 * @since v1.0.0 2020-01-09
 */
@RestController
@RequestMapping("labelrecommend")
@Api(tags = "标签推荐管理")
public class LabelRecommendController {

    @Autowired
    private LabelRecommendService labelRecommendService;
    @Autowired
    private LabelRecommendRelService labelRecommendRelService;
    @Autowired
    private GoodsService goodsService;

    @GetMapping("page")
    @ApiOperation("分页查询标签推荐信息")
    @LogContext(note = GoodsLabelRecommendCode.LABEL_RECOMMEND_PAGE_SUCCESS_MESSAGE, code = GoodsLabelRecommendCode.LABEL_RECOMMEND_PAGE_SUCCESS_CODE)
    @ApiImplicitParams({
            @ApiImplicitParam(name = Constant.PAGE, value = "当前页码，从1开始", paramType = "query", required = true, dataType = "int"),
            @ApiImplicitParam(name = Constant.LIMIT, value = "每页显示记录数", paramType = "query", required = true, dataType = "int"),
            @ApiImplicitParam(name = Constant.ORDER_FIELD, value = "排序字段", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = Constant.ORDER, value = "排序方式，可选值(asc、desc)", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "labelName", value = "标签名称", paramType = "query", dataType = "String")
    })
    public Result<PageData<LabelRecommendDTO>> page(@ApiIgnore @RequestParam Map<String, Object> params) {
        PageData<LabelRecommendDTO> page = labelRecommendService.page(params);

        return new Result<PageData<LabelRecommendDTO>>().ok(page, "查询成功");
    }

    @GetMapping("{id}")
    @ApiOperation("根据ID查询标签推荐信息")
    @LogContext(note = GoodsLabelRecommendCode.LABEL_RECOMMEND_BY_ID_SUCCESS_MESSAGE, code = GoodsLabelRecommendCode.LABEL_RECOMMEND_BY_ID_SUCCESS_CODE)
    public Result<LabelRecommendDTO> get(@PathVariable("id") Long id) {
        LabelRecommendDTO data = labelRecommendService.get(id);

        return new Result<LabelRecommendDTO>().ok(data, "查询成功");
    }

    @PostMapping
    @ApiOperation("保存标签推荐信息")
    @LogContext(note = GoodsLabelRecommendCode.LABEL_RECOMMEND_SAVE_SUCCESS_MESSAGE, code = GoodsLabelRecommendCode.LABEL_RECOMMEND_SAVE_SUCCESS_CODE)
    public Result save(@RequestBody LabelRecommendSaveDTO dto) {
        //效验数据
        ValidatorUtils.validateEntity(dto, AddGroup.class, DefaultGroup.class);

        //将标签名称标识空格去除
        String labelName = StringUtils.remove(dto.getLabelName(), " ");
        dto.setLabelName(labelName);
        String labelFlag = StringUtils.remove(dto.getLabelFlag(), " ");
        dto.setLabelFlag(labelFlag);

        //校验 标签名称标识不可重复
        LabelRecommendDTO recommendDTO = labelRecommendService.findByName(dto.getLabelName(), dto.getLabelFlag());
        if (null != recommendDTO) {
            return new Result().error("标签名称或标识不可重复");
        }
        //保存标签信息
        labelRecommendService.save(ConvertUtils.sourceToTarget(dto, LabelRecommendDTO.class));

        return new Result().ok(null, "保存成功");
    }

    @PutMapping
    @ApiOperation("修改标签推荐信息")
    @LogContext(note = GoodsLabelRecommendCode.LABEL_RECOMMEND_UPDATE_MESSAGE, code = GoodsLabelRecommendCode.LABEL_RECOMMEND_UPDATE_CODE)
    public Result update(@RequestBody LabelRecommendDTO dto) {
        //效验数据
        ValidatorUtils.validateEntity(dto, UpdateGroup.class, DefaultGroup.class);

        //将标签名称标识空格去除
        String labelName = StringUtils.remove(dto.getLabelName(), " ");
        dto.setLabelName(labelName);
        String labelFlag = StringUtils.remove(dto.getLabelFlag(), " ");
        dto.setLabelFlag(labelFlag);

        //校验 标签名称标识不可重复
        int count = labelRecommendService.findByLabelNameUpdate(dto.getId(), dto.getLabelName(), dto.getLabelFlag());
        if (count > 0) {
            return new Result().error("标签名称或标识不可重复");
        }
        //修改标签信息
        labelRecommendService.update(dto);

        return new Result().ok(null, "修改成功");
    }

    @PutMapping("status")
    @ApiOperation("修改标签状态(1启用,2禁用)")
    @LogContext(note = GoodsLabelRecommendCode.LABEL_RECOMMEND_UPDATE_STATUS_MESSAGE, code = GoodsLabelRecommendCode.LABEL_RECOMMEND_UPDATE_STATUS_CODE)
    public Result updateLabelStatus(@RequestParam("labelStatus") Integer labelStatus, @RequestParam("id") Long id) {

        int count = labelRecommendService.updateLabelStatus(labelStatus, id);
        if (count > 0) {
            return new Result().ok(null, "修改成功");
        }
        return new Result().error("修改失败");
    }


    @DeleteMapping
    @ApiOperation("删除标签推荐信息")
    @LogContext(note = GoodsLabelRecommendCode.LABEL_RECOMMEND_DELETE_MESSAGE, code = GoodsLabelRecommendCode.LABEL_RECOMMEND_DELETE_CODE)
    public Result delete(@RequestBody Long[] ids) {
        //效验数据
        AssertUtils.isArrayEmpty(ids, "id");

        labelRecommendService.delete(ids);

        return new Result().ok(null, "删除成功");
    }

    @GetMapping("by/label/id")
    @ApiOperation("根据标签推荐id查询关联商品信息")
    @LogContext(note = GoodsLabelRecommendCode.LABEL_RECOMMEND_BY_ID_REL_SUCCESS_MESSAGE, code = GoodsLabelRecommendCode.LABEL_RECOMMEND_BY_ID_REL_SUCCESS_CODE)
    @ApiImplicitParams({
            @ApiImplicitParam(name = Constant.PAGE, value = "当前页码，从1开始", paramType = "query", required = true, dataType = "int"),
            @ApiImplicitParam(name = Constant.LIMIT, value = "每页显示记录数", paramType = "query", required = true, dataType = "int"),
            @ApiImplicitParam(name = Constant.ORDER_FIELD, value = "排序字段", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = Constant.ORDER, value = "排序方式，可选值(asc、desc)", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "labelId", required = false, value = "标签id", paramType = "query", dataType = "Long"),
            @ApiImplicitParam(name = "goodsName", required = false, value = "商品名称", paramType = "query", dataType = "String")
    })
    public Result<PageData<GoodsListDTO>> findByLabelId(@ApiIgnore @RequestParam Map<String, Object> params) {
        PageData<GoodsListDTO> pageData = goodsService.findByLabelId(params);
        return new Result<PageData<GoodsListDTO>>().ok(pageData, "查询成功");

    }

    @GetMapping("label/unassociated")
    @ApiOperation("查询未关联商品信息并分页")
    @LogContext(note = GoodsLabelRecommendCode.LABEL_RECOMMEND_LIST_SUCCESS_MESSAGE, code = GoodsLabelRecommendCode.LABEL_RECOMMEND_LIST_SUCCESS_CODE)
    @ApiImplicitParams({
            @ApiImplicitParam(name = Constant.PAGE, value = "当前页码，从1开始", paramType = "query", required = true, dataType = "int"),
            @ApiImplicitParam(name = Constant.LIMIT, value = "每页显示记录数", paramType = "query", required = true, dataType = "int"),
            @ApiImplicitParam(name = Constant.ORDER_FIELD, value = "排序字段", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = Constant.ORDER, value = "排序方式，可选值(asc、desc)", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "labelId", required = false, value = "标签id", paramType = "query", dataType = "Long")
    })
    public Result<PageData<GoodsListDTO>> labelUnassociated(@ApiIgnore @RequestParam Map<String, Object> params) {
        PageData<GoodsListDTO> pageData = goodsService.labelUnassociated(params);
        return new Result<PageData<GoodsListDTO>>().ok(pageData, "查询成功");

    }

    @DeleteMapping("by/goods/id")
    @ApiOperation("根据商品id删除标签关联的商品信息")
    @LogContext(note = GoodsLabelRecommendCode.LABEL_RECOMMEND_REL_DELETE_MESSAGE, code = GoodsLabelRecommendCode.LABEL_RECOMMEND_REL_DELETE_CODE)
    public Result deleteByGoodsId(@RequestParam("goodsId") Long goodsId) {

        int count = labelRecommendRelService.deleteByGoodsId(goodsId);
        if (count > 0) {
            return new Result().ok(null, "删除成功");
        }

        return new Result().error("删除失败");
    }

    @PostMapping("batch")
    @ApiOperation("批量保存商品关联信息")
    @LogContext(note = GoodsLabelRecommendCode.LABEL_RECOMMEND_BATCH_SAVE_SUCCESS_MESSAGE, code = GoodsLabelRecommendCode.LABEL_RECOMMEND_BATCH_SAVE_SUCCESS_CODE)
    public Result saveBatch(@RequestBody List<LabelRecommendRelSaveDTO> dtoList) {

        //批量保存商品关联信息
        boolean flag = labelRecommendRelService.insertBatch(dtoList);
        if (flag) {
            return new Result().ok(null, "保存成功");
        }

        return new Result().error("保存失败");
    }

}
