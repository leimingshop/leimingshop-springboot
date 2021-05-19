/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.admin.controller.member.balance;

import com.leimingtech.commons.tools.constant.Constant;
import com.leimingtech.commons.tools.page.PageData;
import com.leimingtech.commons.tools.utils.Result;
import com.leimingtech.commons.tools.validator.AssertUtils;
import com.leimingtech.commons.tools.validator.ValidatorUtils;
import com.leimingtech.commons.tools.validator.group.AddGroup;
import com.leimingtech.commons.tools.validator.group.DefaultGroup;
import com.leimingtech.commons.tools.validator.group.UpdateGroup;
import com.leimingtech.modules.dto.balance.MemberBalanceLogDTO;
import com.leimingtech.modules.service.balance.MemberBalanceLogService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.Map;


/**
 * 用户余额明细表
 *
 * @author xuzhch kuangweiguo@leimingtech.com
 * @since v1.0.0 2020-07-07
 */
@RestController
@RequestMapping("memberbalancelog")
@Api(tags = "用户余额明细表")
public class MemberBalanceLogController {

    @Autowired
    private MemberBalanceLogService memberBalanceLogService;

    @GetMapping("page")
    @ApiOperation("分页")
    @ApiImplicitParams({
            @ApiImplicitParam(name = Constant.PAGE, value = "当前页码，从1开始", paramType = "query", required = true, dataType = "int"),
            @ApiImplicitParam(name = Constant.LIMIT, value = "每页显示记录数", paramType = "query", required = true, dataType = "int"),
            @ApiImplicitParam(name = "memberId", value = "用户ID", paramType = "query", required = true, dataType = "long"),
            @ApiImplicitParam(name = Constant.ORDER_FIELD, value = "排序字段", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = Constant.ORDER, value = "排序方式，可选值(asc、desc)", paramType = "query", dataType = "String")
    })
    public Result<PageData<MemberBalanceLogDTO>> page(@ApiIgnore @RequestParam Map<String, Object> params) {
        PageData<MemberBalanceLogDTO> page = memberBalanceLogService.page(params);

        return new Result<PageData<MemberBalanceLogDTO>>().ok(page);
    }

    @GetMapping("{id}")
    @ApiOperation("信息")
    public Result<MemberBalanceLogDTO> get(@PathVariable("id") Long id) {
        MemberBalanceLogDTO data = memberBalanceLogService.get(id);

        return new Result<MemberBalanceLogDTO>().ok(data);
    }

    @PostMapping
    @ApiOperation("保存")
    public Result save(@RequestBody MemberBalanceLogDTO dto) {
        //效验数据
        ValidatorUtils.validateEntity(dto, AddGroup.class, DefaultGroup.class);

        memberBalanceLogService.save(dto);

        return new Result();
    }

    @PutMapping
    @ApiOperation("修改")
    public Result update(@RequestBody MemberBalanceLogDTO dto) {
        //效验数据
        ValidatorUtils.validateEntity(dto, UpdateGroup.class, DefaultGroup.class);

        memberBalanceLogService.update(dto);

        return new Result();
    }

    @DeleteMapping
    @ApiOperation("删除")
    public Result delete(@RequestBody Long[] ids) {
        //效验数据
        AssertUtils.isArrayEmpty(ids, "id");

        memberBalanceLogService.delete(ids);

        return new Result();
    }

//    @GetMapping("export" )
//    @ApiOperation("导出" )
//    public void export(@ApiIgnore @RequestParam Map<String, Object> params, HttpServletResponse response) throws Exception {
//        List<MemberBalanceLogDTO> list = memberBalanceLogService.list(params);
//
//        EasyExcelUtils.exportExcelToTarget(response, null, list, MemberBalanceLogExcel.class);
//    }

}