/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.seller.controller.goods.spec;

import com.leimingtech.commons.tools.annotation.LogContext;
import com.leimingtech.commons.tools.annotation.LogOperation;
import com.leimingtech.commons.tools.constant.Constant;
import com.leimingtech.commons.tools.page.PageData;
import com.leimingtech.commons.tools.utils.Result;
import com.leimingtech.commons.tools.validator.AssertUtils;
import com.leimingtech.commons.tools.validator.ValidatorUtils;
import com.leimingtech.commons.tools.validator.group.DefaultGroup;
import com.leimingtech.commons.tools.validator.group.UpdateGroup;
import com.leimingtech.modules.dto.goods.detail.GoodsSkuDetailDTO;
import com.leimingtech.modules.dto.goods.price.GoodsSpecPriceAndStorageListDTO;
import com.leimingtech.modules.dto.goods.price.GoodsSpecPriceUpdateDTO;
import com.leimingtech.modules.dto.goods.price.GoodsSpecStorageUpdateDTO;
import com.leimingtech.modules.dto.goods.spec.GoodsSpecUpdateSkuDTO;
import com.leimingtech.modules.dto.goods.spec.list.GoodsSpecListDTO;
import com.leimingtech.modules.dto.goods.time.GoodsTimeDTO;
import com.leimingtech.modules.dto.spec.GoodsSpecShowDTO;
import com.leimingtech.modules.service.goods.GoodsSyncIndexService;
import com.leimingtech.modules.service.goods.spec.GoodsSpecService;
import com.leimingtech.modules.service.goods.time.GoodsTimeService;
import com.leimingtech.modules.statuscode.GoodsStatusCode;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


/**
 * @Author: weixianchun
 * @Description: 商品规格管理
 * @Date :2019/6/4 18:56
 * @Version V1.0
 **/
@RestController
@RequestMapping("goods/spec")
@Api(tags = "商品规格管理")
public class GoodsSpecController {
    @Autowired
    private GoodsSpecService goodsSpecService;

    @Autowired
    private GoodsTimeService goodsTimeService;

    @Autowired
    private GoodsSyncIndexService goodsSyncIndexService;

    @GetMapping("page")
    @ApiOperation("分页查询商品SKU列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = Constant.PAGE, value = "当前页码，从1开始", paramType = "query", required = true, dataType = "int"),
            @ApiImplicitParam(name = Constant.LIMIT, value = "每页显示记录数", paramType = "query", required = true, dataType = "int"),
            @ApiImplicitParam(name = Constant.ORDER_FIELD, value = "排序字段", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = Constant.ORDER, value = "排序方式，可选值(asc、desc)", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "specShow", value = "规格上下架状态:0下架;1上架;2未上架", paramType = "query", dataType = "Integer"),
            @ApiImplicitParam(name = "goodsId", value = "商品id", paramType = "query", required = true, dataType = "Long"),
    })
    public Result<PageData<GoodsSpecListDTO>> page(@ApiIgnore @RequestParam Map<String, Object> params) {

        PageData<GoodsSpecListDTO> goodsSpecListDTOPageData = goodsSpecService.pageSpec(params);
        return new Result<PageData<GoodsSpecListDTO>>().ok(goodsSpecListDTOPageData, "查询成功");
    }


    @GetMapping("{specId}")
    @ApiOperation("根据id查询商品sku信息")
    public Result<GoodsSkuDetailDTO> get(@PathVariable("specId") Long specId) {
        GoodsSkuDetailDTO goodsSkuDetailDTO = goodsSpecService.getBySpecId(specId);
        return new Result<GoodsSkuDetailDTO>().ok(goodsSkuDetailDTO, "查询成功");
    }

    @GetMapping("price/storage/{goodsId}")
    @ApiOperation("查询商品价格和库存")
    public Result<GoodsSpecPriceAndStorageListDTO> getListByGoodsId(@PathVariable("goodsId") Long goodsId) {
        GoodsSpecPriceAndStorageListDTO goodsSpecPriceAndStorageListDTO = goodsSpecService.selectListByGoodsId(goodsId);
        return new Result().ok(goodsSpecPriceAndStorageListDTO, "查询成功");
    }

    @PutMapping("sku")
    @ApiOperation("修改商品sku")
    @LogOperation("修改商品sku")
    @LogContext(code = GoodsStatusCode.SKU_UPDATE_OPERATION_CODE, note = GoodsStatusCode.SKU_UPDATE_OPERATION_MESSAGE)
    public Result updateSku(@RequestBody GoodsSpecUpdateSkuDTO goodsSpecUpdateSkuDTO) {
        //todo 修改价格\库存 时发消息
        ValidatorUtils.validateEntity(goodsSpecUpdateSkuDTO, UpdateGroup.class, DefaultGroup.class);
        Long goodsId = goodsSpecService.update(goodsSpecUpdateSkuDTO);
        goodsSyncIndexService.goodsIndexSyncByGoodsId(goodsId);
        return new Result().ok(null, "修改SKU成功");
    }

    @PutMapping("update/price")
    @ApiOperation("批量修改商品规格价格")
    @LogOperation("批量修改商品规格价格")
    @LogContext(code = GoodsStatusCode.GOODS_SPEC_PRICE_UPDATE_OPERATION_CODE, note = GoodsStatusCode.GOODS_SPEC_PRICE_UPDATE_OPERATION_MESSAGE)
    public Result updateSpecPrice(@RequestBody List<GoodsSpecPriceUpdateDTO> goodsSpecPriceUpdateDTOList) {
        for (GoodsSpecPriceUpdateDTO goodsSpecPriceUpdateDTO : goodsSpecPriceUpdateDTOList) {
            ValidatorUtils.validateEntity(goodsSpecPriceUpdateDTO, UpdateGroup.class, DefaultGroup.class);
        }
        Long goodsId = goodsSpecService.updateSpecPrice(goodsSpecPriceUpdateDTOList);
        List<Long> specIds = goodsSpecPriceUpdateDTOList.stream().map(GoodsSpecPriceUpdateDTO::getId).collect(Collectors.toList());
        goodsSyncIndexService.specPriceIndexUpdate(specIds, goodsId);
        return new Result().ok(null, "修改成功");
    }

    @PutMapping("update/storage")
    @ApiOperation("批量修改商品规格库存")
    @LogOperation("批量修改商品规格库存")
    @LogContext(code = GoodsStatusCode.GOODS_SPEC_STORAGE_UPDATE_OPERATION_CODE, note = GoodsStatusCode.GOODS_SPEC_STORAGE_UPDATE_OPERATION_MESSAGE)
    public Result updateSpecStorage(@RequestBody List<GoodsSpecStorageUpdateDTO> goodsSpecStorageUpdateDTOList) {

        for (GoodsSpecStorageUpdateDTO goodsSpecStorageUpdateDTO : goodsSpecStorageUpdateDTOList) {
            ValidatorUtils.validateEntity(goodsSpecStorageUpdateDTO, UpdateGroup.class, DefaultGroup.class);
        }
        goodsSpecService.updateSpecStorageLog(goodsSpecStorageUpdateDTOList);
        List<Long> specIds = goodsSpecStorageUpdateDTOList.stream().map(GoodsSpecStorageUpdateDTO::getId).collect(Collectors.toList());
        goodsSyncIndexService.specIndexSyncBySpecIds(specIds);
        return new Result().ok(null, "修改成功");
    }


    /**
     * 商品规格定时上下架操作
     *
     * @param dto
     * @return
     * @date 2020/3/5/005 15:59
     * @author xuzhch
     **/
    @PutMapping("show")
    @ApiOperation("规格上下架")
    @LogOperation("规格上下架")
    @LogContext(code = GoodsStatusCode.SPEC_SHOW_UPDATE_OPERATION_CODE, note = GoodsStatusCode.SPEC_SHOW_UPDATE_OPERATION_MESSAGE)
    public Result updateShow(@RequestBody GoodsSpecShowDTO dto) {
        //数据校验
        AssertUtils.isListEmpty(dto.getIds(), "请至少选择一条规格");
        goodsSpecService.updateSpecShow(dto);
        goodsSyncIndexService.goodsIndexSyncByGoodsId(dto.getGoodsId());
        return new Result().ok(null, "规格状态修改成功");
    }

    /**
     * 规格定时上下架信息保存、更新
     *
     * @param dto
     * @return
     * @date 2020/3/5/005 15:58
     * @author xuzhch
     **/
    @PutMapping("timed/show")
    @ApiOperation("规格定时上下架信息保存")
    @LogOperation("规格定时上下架信息保存")
    @LogContext(code = GoodsStatusCode.SPEC_SHOW_UPDATE_OPERATION_CODE, note = GoodsStatusCode.SPEC_SHOW_UPDATE_OPERATION_MESSAGE)
    public Result timdeUpdateSpecShow(@RequestBody GoodsSpecShowDTO dto) {
        //数据校验
        AssertUtils.isListEmpty(dto.getIds(), "请至少选择一条规格");
        goodsSpecService.timedUpdateSpecShow(dto);

        return new Result().ok(null, "规格定时上下架信息保存成功");
    }

    /**
     * 查询商品上下架定时状态
     *
     * @return
     */
    @GetMapping("show/timing/{specId}")
    @ApiOperation("商品定时上下架状态查询")
    public Result<GoodsTimeDTO> show(@PathVariable("specId") Long specId) {

        GoodsTimeDTO goodsTimeDTO = goodsTimeService.selectBySpecId(specId);

        return new Result<GoodsTimeDTO>().ok(goodsTimeDTO, "查询成功");
    }

}
