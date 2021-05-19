/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.admin.controller.cartrecom;

import com.alibaba.fastjson.JSON;
import com.leimingtech.commons.tools.annotation.LogContext;
import com.leimingtech.commons.tools.annotation.LogOperation;
import com.leimingtech.commons.tools.constant.Constant;
import com.leimingtech.commons.tools.page.PageData;
import com.leimingtech.commons.tools.utils.Result;
import com.leimingtech.commons.tools.validator.AssertUtils;
import com.leimingtech.modules.dto.recommend.CartRecommendPageDTO;
import com.leimingtech.modules.dto.recommend.GoodsPageDTO;
import com.leimingtech.modules.dto.recommend.SaveCartRecommendDTO;
import com.leimingtech.modules.dto.recommend.UpdateCartRecommendDTO;
import com.leimingtech.modules.service.recommend.CartRecommendService;
import com.leimingtech.modules.statuscode.GoodsStatusCode;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.List;
import java.util.Map;

/**
 * @author yanrangnan
 * @date 2019/9/6
 */
@Slf4j
@Api(tags = "购物车推荐")
@RestController
@RequestMapping("/cartrecom")
public class CartRecemomendController {

    @Autowired
    private CartRecommendService cartRecommendService;


    /**
     * 商品推荐列表
     *
     * @param params
     * @return
     */
    @GetMapping("page")
    @ApiOperation("商品推荐列表")
    @LogContext(note = GoodsStatusCode.GOODS_PAGE_SUCCESS_MSG, code = GoodsStatusCode.GOODS_PAGE_SUCCESS_CODE)
    @ApiImplicitParams({
            @ApiImplicitParam(name = Constant.PAGE, value = "当前页码，从1开始", paramType = "query", required = true, dataType = "int"),
            @ApiImplicitParam(name = Constant.LIMIT, value = "每页显示记录数", paramType = "query", required = true, dataType = "int"),
            @ApiImplicitParam(name = "goodsId", value = "商品名称/商品货号", paramType = "query", dataType = "Long"),
            @ApiImplicitParam(name = "goodsName", value = "分类ID", paramType = "query", dataType = "Long")

    })
    public Result<PageData<CartRecommendPageDTO>> page(@ApiIgnore @RequestParam Map<String, Object> params) {
        PageData<CartRecommendPageDTO> page = cartRecommendService.pageList(params);

        return new Result<PageData<CartRecommendPageDTO>>().ok(page);
    }

    /**
     * 更新商品排序
     */
    @PutMapping("update")
    @ApiOperation("更新商品排序")
    @LogOperation("更新商品排序")
    public Result update(@RequestBody List<UpdateCartRecommendDTO> list) {
        cartRecommendService.updateSort(list);
        return new Result().ok(null, "修改成功");

    }

    /**
     * 删除推荐商品
     *
     * @param ids
     * @return
     */
    @DeleteMapping
    @ApiOperation("删除推荐商品")
    @LogOperation("删除推荐商品")
    public Result delete(@RequestBody Long[] ids) {
        //效验数据
        AssertUtils.isArrayEmpty(ids, "id不能为空");

        cartRecommendService.delete(ids);

        return new Result().ok(null, "删除成功");
    }

    /**
     * 保存推荐商品
     *
     * @param params
     * @return
     */
    @PostMapping("add")
    @ApiOperation("保存推荐商品")
    public Result save(@RequestBody Map<String, String> params) {
        String list = params.get("list");
        List<SaveCartRecommendDTO> dtos = JSON.parseArray(list, SaveCartRecommendDTO.class);
        cartRecommendService.save(dtos);

        return new Result().ok("保存成功");
    }

    @GetMapping("goods/page")
    @ApiOperation("商品列表分页")
    @ApiImplicitParams({
            @ApiImplicitParam(name = Constant.PAGE, value = "当前页码，从1开始", paramType = "query", required = true, dataType = "int"),
            @ApiImplicitParam(name = Constant.LIMIT, value = "每页显示记录数", paramType = "query", required = true, dataType = "int"),
            @ApiImplicitParam(name = "goodsName", value = "商品名称/商品货号", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "categoryId", value = "分类ID", paramType = "query", dataType = "Long")

    })
    public Result<PageData<GoodsPageDTO>> goodsPage(@ApiIgnore @RequestParam Map<String, Object> params) {
        PageData<GoodsPageDTO> goodsPageDTOPageData = cartRecommendService.goodsPage(params);
        return new Result<PageData<GoodsPageDTO>>().ok(goodsPageDTOPageData);
    }

}
