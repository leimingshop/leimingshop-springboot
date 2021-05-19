/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.admin.controller.member.point;

import com.leimingtech.commons.tools.constant.Constant;
import com.leimingtech.commons.tools.page.PageData;
import com.leimingtech.commons.tools.utils.Result;
import com.leimingtech.modules.dto.log.point.PointLogDTO;
import com.leimingtech.modules.service.log.point.PointLogService;
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

import java.util.Map;

/**
 * 会员积分日志Contorller
 *
 * @author lixiang
 * @version V1.0
 * @date 2020/1/19 9:02
 **/
@Slf4j
@RestController
@RequestMapping("point")
@Api(tags = "会员积分管理")
public class MemberPointController {

    @Autowired
    private PointLogService pointLogService;


    @GetMapping("page")
    @ApiOperation("分页")
    @ApiImplicitParams({
            @ApiImplicitParam(name = Constant.PAGE, value = "当前页码，从1开始", paramType = "query", required = true, dataType = "int"),
            @ApiImplicitParam(name = Constant.LIMIT, value = "每页显示记录数", paramType = "query", required = true, dataType = "int"),
            @ApiImplicitParam(name = Constant.ORDER_FIELD, value = "排序字段", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "memberId", value = "会员Id", paramType = "query", dataType = "long"),
            @ApiImplicitParam(name = Constant.ORDER, value = "排序方式，可选值(asc、desc)", paramType = "query", dataType = "String")
    })
    public Result<PageData<PointLogDTO>> page(@ApiIgnore @RequestParam Map<String, Object> params) {
        PageData<PointLogDTO> page = pointLogService.page(params);
        return new Result<PageData<PointLogDTO>>().ok(page);
    }
}
