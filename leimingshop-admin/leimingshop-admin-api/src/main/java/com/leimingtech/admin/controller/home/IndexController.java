/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.admin.controller.home;

import com.leimingtech.commons.tools.security.user.UserDetail;
import com.leimingtech.commons.tools.utils.Result;
import com.leimingtech.commons.tools.validator.AssertUtils;
import com.leimingtech.dto.index.IndexBaseDataDTO;
import com.leimingtech.dto.index.IndexUserFunctionDTO;
import com.leimingtech.modules.service.order.OrderGoodsService;
import com.leimingtech.modules.service.order.OrderService;
import com.leimingtech.service.index.IndexService;
import com.leimingtech.service.user.function.UserFunctionService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import java.util.List;
import java.util.Map;

/**
 * @author xuzhch
 * @className IndexController
 * @description admin首页管理
 * @date 2020/3/16/016
 */
@RestController
@RequestMapping("home")
@Api(tags = "admin首页管理")
public class IndexController {
    @Autowired
    private IndexService indexService;
    @Autowired
    private OrderService orderService;
    @Autowired
    private UserFunctionService userFunctionService;
    @Autowired
    private OrderGoodsService orderGoodsService;


    /**
     * 管理端基础概况
     *
     * @param params
     * @return
     * @date 2020/3/16/016 16:25
     * @author xuzhch
     **/
    @GetMapping("base")
    @ApiOperation("管理端基础概况")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "dateType", value = "日期类型（默认0:今天，1:昨天，2：近七天，3：近15天，4：近30天）", paramType = "query", defaultValue = "0", required = true, dataType = "Integer"),
            @ApiImplicitParam(name = "date", value = "时间", paramType = "query", dataType = "Date"),
            @ApiImplicitParam(name = "refresh", value = "刷新标记（默认0：不刷新，1刷新）", required = true, defaultValue = "0", paramType = "query", dataType = "Integer")

    })
    public Result<IndexBaseDataDTO> baseManage(@ApiIgnore UserDetail userDetail,
                                               @ApiIgnore @RequestParam Map<String, Object> params) {
        AssertUtils.isNull(userDetail, "抱歉，您未登录");
        params.put("userId", userDetail.getId());
        IndexBaseDataDTO baseDataDTO = indexService.baseManage(params);
        return new Result<IndexBaseDataDTO>().ok(baseDataDTO);
    }


    /**
     * 商户销售排名
     *
     * @param params
     * @return
     * @date 2020/3/16/016 16:25
     * @author xuzhch
     **/
    @GetMapping("store/sell")
    @ApiOperation("商户销售排名")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "dateType", value = "日期类型（默认2：近七天，3：近15天，4：近30天）", paramType = "query", defaultValue = "2", required = true, dataType = "Integer"),
            @ApiImplicitParam(name = "orderBy", value = "排序方式（'默认1：成交额，2：订单数'）", paramType = "query", defaultValue = "1", dataType = "Integer"),
            @ApiImplicitParam(name = "date", value = "时间", paramType = "query", dataType = "Date"),
            @ApiImplicitParam(name = "refresh", value = "刷新标记（默认0：不刷新，1刷新）", required = true, defaultValue = "0", paramType = "query", dataType = "Integer")

    })
    public Result<Map<String, Object>> storeSellRanking(@ApiIgnore UserDetail userDetail, @ApiIgnore @RequestParam Map<String, Object> params) {
        Long userId = userDetail.getId();
        params.put("userId", userId);
        Map<String, Object> result = orderService.storeSellRanking(params);
        return new Result<Map<String, Object>>().ok(result);
    }

    /**
     * 商品销售排名
     *
     * @param params
     * @return
     * @date 2020/3/16/016 16:25
     * @author xuzhch
     **/
    @GetMapping("goods/sell")
    @ApiOperation("商品销售排名")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "dateType", value = "日期类型（默认2：近七天，3：近15天，4：近30天）", paramType = "query", defaultValue = "2", required = true, dataType = "Integer"),
            @ApiImplicitParam(name = "orderBy", value = "排序方式（'默认1：成交额，2：订单数'）", paramType = "query", defaultValue = "1", dataType = "Integer"),
            @ApiImplicitParam(name = "date", value = "时间", paramType = "query", dataType = "Date"),
            @ApiImplicitParam(name = "refresh", value = "刷新标记（默认0：不刷新，1刷新）", required = true, defaultValue = "0", paramType = "query", dataType = "Integer")

    })
    public Result<Map<String, Object>> goodsSellRanking(@ApiIgnore UserDetail userDetail, @ApiIgnore @RequestParam Map<String, Object> params) {
        Long userId = userDetail.getId();
        params.put("userId", userId);
        Map<String, Object> result = orderGoodsService.goodsSellRanking(params);
        return new Result<Map<String, Object>>().ok(result);
    }


    /**
     * 首页常用功能
     *
     * @param userDetail 用户信息
     * @return List<IndexUserFunctionDTO> 常用功能数据
     * @date 2020/4/7/007 11:46
     * @author xuzhch
     */
    @GetMapping("user/function")
    @ApiOperation("常用功能数据")
    public Result<List<IndexUserFunctionDTO>> userFunctionList(@ApiIgnore UserDetail userDetail) {
        AssertUtils.isNull(userDetail, "请重新登陆");
        Long userId = userDetail.getId();
//        Long userId = 1L;
        List<IndexUserFunctionDTO> userFunctionDTOS = userFunctionService.getListByUserId(userId);
        return new Result<List<IndexUserFunctionDTO>>().ok(userFunctionDTOS);
    }
}

