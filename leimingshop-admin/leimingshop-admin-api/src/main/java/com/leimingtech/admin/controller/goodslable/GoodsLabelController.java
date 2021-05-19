/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.admin.controller.goodslable;

import com.leimingtech.commons.tools.annotation.LogContext;
import com.leimingtech.commons.tools.annotation.LogOperation;
import com.leimingtech.commons.tools.constant.Constant;
import com.leimingtech.commons.tools.page.PageData;
import com.leimingtech.commons.tools.utils.Result;
import com.leimingtech.commons.tools.validator.AssertUtils;
import com.leimingtech.commons.tools.validator.ValidatorUtils;
import com.leimingtech.commons.tools.validator.group.AddGroup;
import com.leimingtech.commons.tools.validator.group.DefaultGroup;
import com.leimingtech.commons.tools.validator.group.UpdateGroup;
import com.leimingtech.modules.dto.goodslable.GoodsLabelDTO;
import com.leimingtech.modules.dto.goodslable.GoodsLabelFindDTO;
import com.leimingtech.modules.dto.goodslable.GoodsLabelSaveDTO;
import com.leimingtech.modules.dto.goodslable.GoodsLabelUpdateDTO;
import com.leimingtech.modules.service.goodslabel.GoodsLabelService;
import com.leimingtech.modules.statuscode.GoodsLabelStatusCode;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * @Author: weixianchun
 * @Description: 商品标签管理
 * @Date :2019/5/20 14:14
 * @Version V1.0
 **/
@RestController
@RequestMapping("label")
@Api(tags = "商品标签管理")
public class GoodsLabelController {

    @Autowired
    private GoodsLabelService goodsLabelService;

    /**
     * @Author: weixianchun
     * @Description: 分页查询
     * @Date :2019/5/20 14:14
     * @Param :[params] 可变参数
     * @Version V1.0
     **/
    @GetMapping("page")
    @ApiOperation("分页查询")
    @LogContext(note = GoodsLabelStatusCode.LABEL_PAGE_SUCCESS_MESSAGE, code = GoodsLabelStatusCode.LABEL_PAGE_SUCCESS_CODE)
    @ApiImplicitParams({
            @ApiImplicitParam(name = Constant.PAGE, value = "当前页码，从1开始", paramType = "query", required = true, dataType = "int"),
            @ApiImplicitParam(name = Constant.LIMIT, value = "每页显示记录数", paramType = "query", required = true, dataType = "int"),
            @ApiImplicitParam(name = Constant.ORDER_FIELD, value = "排序字段", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = Constant.ORDER, value = "排序方式，可选值(asc、desc)", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "labelName", value = "标签名称", paramType = "query", dataType = "String")
    })
    public Result<PageData<GoodsLabelDTO>> page(@ApiIgnore @RequestParam Map<String, Object> params) {

        PageData<GoodsLabelDTO> page = goodsLabelService.page(params);

        return new Result<PageData<GoodsLabelDTO>>().ok(page, "查询成功");
    }


    @GetMapping("label/group/{id}")
    @LogContext(note = GoodsLabelStatusCode.LABEL_BY_ID_SUCCESS_MESSAGE, code = GoodsLabelStatusCode.LABEL_BY_ID_SUCCESS_CODE)
    @ApiOperation("根据商品标签ID查询查询标签,分组信息")
    public Result<GoodsLabelFindDTO> findByLabelId(@PathVariable("id") Long id) {

        GoodsLabelFindDTO data = goodsLabelService.findByLabelId(id);
        if (null == data) {
            return new Result<GoodsLabelFindDTO>().error("标签不存在");
        }
        return new Result<GoodsLabelFindDTO>().ok(data, "查询标签,分组信息成功");
    }

    /**
     * @Author: weixianchun
     * @Description: 保存商品标签
     * @Date :2019/5/20 14:15
     * @Param :[dto] 实体
     * @Version V1.0
     **/
    @PostMapping
    @ApiOperation("保存商品标签")
    @LogOperation("保存商品标签")
    @LogContext(note = GoodsLabelStatusCode.LABEL_SAVE_SUCCESS_MESSAGE, code = GoodsLabelStatusCode.LABEL_SAVE_SUCCESS_CODE)
    public Result save(@RequestBody GoodsLabelSaveDTO dto) {

        //效验数据
        ValidatorUtils.validateEntity(dto, AddGroup.class, DefaultGroup.class);
        //移除空格
        String labelName = StringUtils.remove(dto.getLabelName(), " ");
        dto.setLabelName(labelName);
        //校验标签数量
        int count = goodsLabelService.checkLabelNameSave(dto.getLabelName());
        if (count > 0) {
            return new Result().error("商品标签不可重复");
        }
        goodsLabelService.save(dto);
        return new Result().ok(null, "保存商品标签成功");
    }

    /**
     * @Author: weixianchun
     * @Description: 修改商品标签信息
     * @Date :2019/5/20 14:16
     * @Param :[dto] 实体
     * @Version V1.0
     **/
    @PutMapping
    @ApiOperation("修改商品标签")
    @LogOperation("修改商品标签")
    @LogContext(note = GoodsLabelStatusCode.LABEL_UPDATE_MESSAGE, code = GoodsLabelStatusCode.LABEL_UPDATE_CODE)
    public Result update(@RequestBody GoodsLabelUpdateDTO dto) {

        //效验数据
        ValidatorUtils.validateEntity(dto, UpdateGroup.class, DefaultGroup.class);

        //移除空格
        String labelName = StringUtils.remove(dto.getLabelName(), " ");
        dto.setLabelName(labelName);
        //校验数据库标签名称数量(防重)
        int count = goodsLabelService.checkLabelNameUpate(dto);
        if (count > 0) {
            return new Result().error("商品标签不可重复");
        }
        goodsLabelService.update(dto);
        return new Result().ok(null, "修改商品标签成功");
    }

    /**
     * @Author: weixianchun
     * @Description: 通过标签ID删除数据(逻辑删除)
     * @Date :2019/5/20 14:16
     * @Param :[ids] 标签id数组
     * @Version V1.0
     **/
    @DeleteMapping
    @ApiOperation("通过标签ID删除数据(逻辑删除)")
    @LogOperation("通过标签ID删除数据(逻辑删除)")
    @LogContext(note = GoodsLabelStatusCode.LABEL_DELETE_MESSAGE, code = GoodsLabelStatusCode.LABEL_DELETE_CODE)
    public Result delete(@RequestBody Long[] ids) {

        //效验数据:数组为空提示ids不可为空
        AssertUtils.isArrayEmpty(ids, "ids不可为空");

        //逻辑删除
        goodsLabelService.delete(ids);

        return new Result().ok(null, "删除商品标签成功");
    }

    @GetMapping("list")
    @ApiOperation("标签列表查询")
    @LogContext(note = GoodsLabelStatusCode.LABEL_LIST_SUCCESS_MESSAGE, code = GoodsLabelStatusCode.LABEL_LIST_SUCCESS_CODE)
    @ApiImplicitParam(name = "labelName", value = "标签名称", paramType = "query", dataType = "String")
    public Result<List<GoodsLabelDTO>> findList(@RequestParam(value = "labelName", required = false) String labelName) {
        Map<String, Object> params = new HashMap<>(16);

        // 查询标签列表
        params.put(Constant.ORDER_FIELD, "createDate");
        params.put(Constant.ORDER, "desc");
        if (StringUtils.isNotBlank(labelName)) {
            params.put("labelName", labelName);
        }
        List<GoodsLabelDTO> list = goodsLabelService.list(params);

        if (CollectionUtils.isNotEmpty(list)) {
            return new Result<List<GoodsLabelDTO>>().ok(list, "查询标签数据成功");
        } else {
            return new Result<List<GoodsLabelDTO>>().ok(null, "暂无标签");
        }
    }

}
