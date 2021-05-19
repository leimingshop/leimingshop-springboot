/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.seller.controller.goodsclass;

import com.leimingtech.commons.tools.constant.Constant;
import com.leimingtech.commons.tools.exception.ErrorCode;
import com.leimingtech.commons.tools.page.PageData;
import com.leimingtech.commons.tools.security.user.SellerDetail;
import com.leimingtech.commons.tools.utils.Result;
import com.leimingtech.modules.dto.goodsclass.GoodsClassBrandDTO;
import com.leimingtech.modules.dto.goodsclass.GoodsClassDTO;
import com.leimingtech.modules.dto.goodsclass.GoodsClassDetailDTO;
import com.leimingtech.modules.dto.goodsclass.GoodsClassListDTO;
import com.leimingtech.modules.enums.goodsclass.GoodsClassErrorCodeEnum;
import com.leimingtech.modules.service.goodsclass.GoodsClassService;
import com.leimingtech.modules.service.storeclass.StoreClassService;
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
    @Autowired
    private StoreClassService storeClassService;

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
    public Result<List<GoodsClassDTO>> child(@PathVariable("id") Long id, @ApiIgnore SellerDetail sellerDetail) {
        if (sellerDetail == null) {
            return new Result<List<GoodsClassDTO>>().error(ErrorCode.UNAUTHORIZED, "用户未登录");
        }
        List<GoodsClassDTO> data = goodsClassService.selectListByParentId(id, sellerDetail.getStoreId());
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

    @GetMapping("tree/goods/class")
    @ApiOperation("查询所有分类(分层级关系)")
    public Result<List<GoodsClassListDTO>> findGoodsClassByStoreId(@ApiIgnore SellerDetail sellerDetail) {
        if (sellerDetail == null) {
            return new Result<List<GoodsClassListDTO>>().error(ErrorCode.UNAUTHORIZED, "用户未登录");
        }
        List<GoodsClassListDTO> goodsClassCustomListDTOS = goodsClassService.selectGoodsClassByStoreId(sellerDetail.getStoreId());
        return new Result<List<GoodsClassListDTO>>().ok(goodsClassCustomListDTOS);
    }


    @GetMapping("all/goodclass")
    @ApiOperation("查询所有分类(分层级关系)")
    public Result<List<GoodsClassListDTO>> findAllGoodsClass() {
        List<GoodsClassListDTO> goodsClassCustomListDTOS = goodsClassService.selectAllGoodsClass(GoodsClassErrorCodeEnum.GOODS_CLASS_THREE.value());
        return new Result<List<GoodsClassListDTO>>().ok(goodsClassCustomListDTOS);
    }

    @GetMapping("brand/id")
    @ApiOperation("根据分类id查询品牌信息")
    public Result<List<GoodsClassBrandDTO>> findBrandByGcId(@RequestParam("gcClassId") Long gcClassId, @RequestParam(value = "brandName", required = false) String brandName) {
        List<GoodsClassBrandDTO> dtoList = goodsClassService.brandByGcId(gcClassId, brandName);
        return new Result<List<GoodsClassBrandDTO>>().ok(dtoList, "查询成功");
    }


}
