/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.seller.controller.brand;

import com.leimingtech.commons.tools.annotation.LogContext;
import com.leimingtech.commons.tools.constant.Constant;
import com.leimingtech.commons.tools.exception.ErrorCode;
import com.leimingtech.commons.tools.page.PageData;
import com.leimingtech.commons.tools.security.user.SellerDetail;
import com.leimingtech.commons.tools.utils.Result;
import com.leimingtech.modules.dto.brand.BrandDTO;
import com.leimingtech.modules.service.brand.BrandService;
import com.leimingtech.modules.statuscode.GoodsBrandStatusCode;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import java.util.List;
import java.util.Map;


/**
 * @Author: weixianchun
 * @Description: 品牌管理BrandController
 * @Date :2019/5/20 13:49
 * @Version V1.0
 **/
@RestController
@RequestMapping("brand")
@Api(tags = "品牌管理")
@Slf4j
public class BrandController {

    @Autowired
    private BrandService brandService;


    @GetMapping("store/list")
    @ApiOperation("店铺品牌列表")
    public Result<List<BrandDTO>> storeBrandList(@ApiIgnore SellerDetail sellerDetail) {
        // 获取店铺ID
        if (sellerDetail == null || sellerDetail.getStoreId() == null) {
            return new Result<List<BrandDTO>>().error(ErrorCode.UNAUTHORIZED, "用户未登录");
        }
        Long storeId = sellerDetail.getStoreId();
        List<BrandDTO> brandDTOS = brandService.storeBrandList(storeId);
        return new Result<List<BrandDTO>>().ok(brandDTOS, "查询成功");
    }

    /**
     * @Author: weixianchun
     * @Description: 查询所有品牌信息
     * @Date :2019/5/20 13:56
     * @Version V1.0
     **/
    @GetMapping("list")
    @ApiOperation("查询所有品牌信息")
    @ApiImplicitParam(name = "brandName", value = "品牌名称", paramType = "query", dataType = "String")
    public Result<List<BrandDTO>> list(@ApiIgnore @RequestParam Map<String, Object> params) {

        List<BrandDTO> list = brandService.list(params);

        return new Result<List<BrandDTO>>().ok(list, "查询成功");
    }

    /**
     * @Author: weixianchun
     * @Description: 分页并条件查询
     * @Date :2019/5/20 13:50
     * @Param :[params] 可变参数
     * @Version V1.0
     **/
    @GetMapping("page")
    @ApiOperation("分页并条件查询")
    @LogContext(note = GoodsBrandStatusCode.BRAND_PAGE_SUCCESS_MESSAGE, code = GoodsBrandStatusCode.BRAND_PAGE_SUCCESS_CODE)
    @ApiImplicitParams({
            @ApiImplicitParam(name = Constant.PAGE, value = "当前页码，从1开始", paramType = "query", required = true, dataType = "int"),
            @ApiImplicitParam(name = Constant.LIMIT, value = "每页显示记录数", paramType = "query", required = true, dataType = "int"),
            @ApiImplicitParam(name = Constant.ORDER_FIELD, value = "排序字段", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = Constant.ORDER, value = "排序方式，可选值(asc、desc)", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "brandInitials", value = "品牌首字母 ", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "brandNameEn", value = "英文名称", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "brandName", value = "品牌名称", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "brandApply", value = "审核状态0申请中，1通过,2未通过", paramType = "query", dataType = "String")
    })
    public Result<PageData<BrandDTO>> page(@ApiIgnore @RequestParam Map<String, Object> params) {

        PageData<BrandDTO> page = brandService.page(params);

        return new Result<PageData<BrandDTO>>().ok(page, "查询成功");
    }
}
