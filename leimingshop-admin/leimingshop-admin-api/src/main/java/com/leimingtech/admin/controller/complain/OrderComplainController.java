/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.admin.controller.complain;

import com.leimingtech.admin.excel.complain.OrderComplainExcel;
import com.leimingtech.commons.tools.constant.Constant;
import com.leimingtech.commons.tools.page.PageData;
import com.leimingtech.commons.tools.security.user.SecurityUser;
import com.leimingtech.commons.tools.utils.ConvertUtils;
import com.leimingtech.commons.tools.utils.EasyExcelUtils;
import com.leimingtech.commons.tools.utils.Result;
import com.leimingtech.commons.tools.validator.ValidatorUtils;
import com.leimingtech.commons.tools.validator.group.UpdateGroup;
import com.leimingtech.dto.SysDictDTO;
import com.leimingtech.modules.dto.complain.DisposeOrderComplainDTO;
import com.leimingtech.modules.dto.complain.OrderComplainDTO;
import com.leimingtech.modules.dto.complain.OrderComplainDetailDTO;
import com.leimingtech.modules.enums.complain.OrderComplainEnum;
import com.leimingtech.modules.service.complain.OrderComplainService;
import com.leimingtech.service.sys.SysDictService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 订单投诉表
 *
 * @author chengqian chengqian@leimingtech.com
 * @since v1.0.0 2020-04-09
 */
@RestController
@RequestMapping("ordercomplain")
@Api(tags = "订单投诉")
public class OrderComplainController {

    @Autowired
    private OrderComplainService orderComplainService;

    @Autowired
    private SysDictService sysDictService;

    /**
     * 投诉分页
     *
     * @param params
     * @return
     */
    @GetMapping("page")
    @ApiOperation("分页")
    @ApiImplicitParams({
            @ApiImplicitParam(name = Constant.PAGE, value = "当前页码，从1开始", paramType = "query", required = true, dataType = "int"),
            @ApiImplicitParam(name = Constant.LIMIT, value = "每页显示记录数", paramType = "query", required = true, dataType = "int"),
            @ApiImplicitParam(name = Constant.ORDER_FIELD, value = "排序字段", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "account", value = "会员账号", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "status", value = "投诉状态 10：待处理 20：已处理", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "phone", value = "手机号", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "type", value = "投诉类型1:服务相关,2:物流相关,3:售后相关,4:商品相关5:其他", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "source", value = "投诉来源 0:pc,1:h5,2:android,3:ios,4:微信,5:小程序", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "verdict", value = "投诉判定 1：有效投诉，2：重点问题，3：无效投诉", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "disposeStartDate", value = "处理开始时间", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "disposeEndDate", value = "处理结束时间", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "createStartDate", value = "投诉开始时间", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "createEndDate", value = "投诉结束时间", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "disposePerson", value = "处理人", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = Constant.ORDER, value = "排序方式，可选值(asc、desc)", paramType = "query", dataType = "String")
    })
    public Result<PageData<OrderComplainDTO>> page(@ApiIgnore @RequestParam Map<String, Object> params) {
        PageData<OrderComplainDTO> page = orderComplainService.adminPage(params);

        return new Result<PageData<OrderComplainDTO>>().ok(page);
    }

    /**
     * 查看详情
     *
     * @param id
     * @return
     */
    @GetMapping("{id}")
    @ApiOperation("信息")
    public Result<OrderComplainDetailDTO> get(@PathVariable("id") Long id) {
        OrderComplainDetailDTO info = orderComplainService.info(id);

        return new Result<OrderComplainDetailDTO>().ok(info);
    }

    /**
     * 处理投诉
     *
     * @param dto
     * @return
     */
    @PutMapping
    @ApiOperation("处理投诉")
    public Result update(@RequestBody DisposeOrderComplainDTO dto) {
        //效验数据
        ValidatorUtils.validateEntity(dto, UpdateGroup.class);
        OrderComplainDTO orderComplainDTO = ConvertUtils.sourceToTarget(dto, OrderComplainDTO.class);
        orderComplainDTO.setStatus(OrderComplainEnum.STATUS_YES.value());
        orderComplainDTO.setDisposeDate(new Date());
        orderComplainDTO.setDisposePerson(SecurityUser.getUserName());
        orderComplainService.update(orderComplainDTO);
        return new Result().ok(null, "处理成功");
    }

    /**
     * 获取投诉类型
     *
     * @throws Exception
     */
    @GetMapping("type")
    @ApiOperation("获取投诉类型")
    public Result<List<SysDictDTO>> complainType() {
        //TODO  暂时从数据字典中获取，后期优化此功能
        Map<String, Object> map = new HashMap<>(16);
        map.put("dictType", "complain-type");
        map.put("pid", "1247703838610227201");
        List<SysDictDTO> list = sysDictService.list(map);
        return new Result<List<SysDictDTO>>().ok(list);
    }

    /**
     * 导出
     * s
     *
     * @param params
     * @param response
     * @throws Exception
     */
    @GetMapping("export")
    @ApiOperation("导出")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "account", value = "会员账号", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "status", value = "投诉状态 10：待处理 20：已处理", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "phone", value = "手机号", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "type", value = "投诉类型1:服务相关,2:物流相关,3:售后相关,4:商品相关5:其他", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "source", value = "投诉来源 0:pc,1:h5,2:android,3:ios,4:微信,5:小程序", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "verdict", value = "投诉判定 1：有效投诉，2：重点问题，3：无效投诉", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "disposeStartDate", value = "处理开始时间", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "disposeEndDate", value = "处理结束时间", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "createStartDate", value = "投诉开始时间", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "createEndDate", value = "投诉结束时间", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "disposePerson", value = "处理人", paramType = "query", dataType = "String")
    })
    public void export(@ApiIgnore @RequestParam Map<String, Object> params, HttpServletResponse response) throws Exception {
        List<OrderComplainDetailDTO> list = orderComplainService.list(params);

        EasyExcelUtils.exportExcelToTarget(response, null, list, OrderComplainExcel.class);
    }
}
