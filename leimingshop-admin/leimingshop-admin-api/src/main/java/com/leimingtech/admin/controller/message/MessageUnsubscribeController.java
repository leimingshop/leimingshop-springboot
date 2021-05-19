//
//package com.leimingtech.admin.controller.message;
//
//import com.leimingtech.commons.tools.constant.Constant;
//import com.leimingtech.commons.tools.page.PageData;
//import com.leimingtech.commons.tools.utils.Result;
//import com.leimingtech.commons.tools.validator.AssertUtils;
//import com.leimingtech.commons.tools.validator.ValidatorUtils;
//import com.leimingtech.commons.tools.validator.group.AddGroup;
//import com.leimingtech.commons.tools.validator.group.DefaultGroup;
//import com.leimingtech.commons.tools.validator.group.UpdateGroup;
//import com.leimingtech.message.dto.MessageUnsubscribeDTO;
//import com.leimingtech.message.service.MessageUnsubscribeService;
//import io.swagger.annotations.Api;
//import io.swagger.annotations.ApiImplicitParam;
//import io.swagger.annotations.ApiImplicitParams;
//import io.swagger.annotations.ApiOperation;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.*;
//import springfox.documentation.annotations.ApiIgnore;
//
//import javax.servlet.http.HttpServletResponse;
//import java.util.List;
//import java.util.Map;
//
//
///**
// * 短信消息退订表
// *
// * @author kuangweiguo xuzhch@leimingtech.com
// * @since v1.0.0 2020-07-28
// */
//@RestController
//@RequestMapping("messageunsubscribe" )
//@Api(tags = "短信消息退订表" )
//public class MessageUnsubscribeController {
//
//    @Autowired
//    private MessageUnsubscribeService messageUnsubscribeService;
//
//    @GetMapping("page" )
//    @ApiOperation("分页" )
//    @ApiImplicitParams({
//            @ApiImplicitParam(name = Constant.PAGE, value = "当前页码，从1开始", paramType = "query", required = true, dataType = "int" ),
//            @ApiImplicitParam(name = Constant.LIMIT, value = "每页显示记录数", paramType = "query", required = true, dataType = "int" ),
//            @ApiImplicitParam(name = Constant.ORDER_FIELD, value = "排序字段", paramType = "query", dataType = "String" ),
//            @ApiImplicitParam(name = Constant.ORDER, value = "排序方式，可选值(asc、desc)", paramType = "query", dataType = "String" )
//    })
//    public Result<PageData<MessageUnsubscribeDTO>> page(@ApiIgnore @RequestParam Map<String, Object> params) {
//        PageData<MessageUnsubscribeDTO> page = messageUnsubscribeService.page(params);
//
//        return new Result<PageData<MessageUnsubscribeDTO>>().ok(page);
//    }
//
//    @GetMapping("{id}" )
//    @ApiOperation("信息" )
//    public Result<MessageUnsubscribeDTO> get(@PathVariable("id" ) Long id) {
//            MessageUnsubscribeDTO data = messageUnsubscribeService.get(id);
//
//        return new Result<MessageUnsubscribeDTO>().ok(data);
//    }
//
//    @PostMapping
//    @ApiOperation("保存" )
//    public Result save(@RequestBody MessageUnsubscribeDTO dto) {
//        //效验数据
//        ValidatorUtils.validateEntity(dto, AddGroup.class, DefaultGroup.class);
//
//            messageUnsubscribeService.save(dto);
//
//        return new Result();
//    }
//
//    @PutMapping
//    @ApiOperation("修改" )
//    public Result update(@RequestBody MessageUnsubscribeDTO dto) {
//        //效验数据
//        ValidatorUtils.validateEntity(dto, UpdateGroup.class, DefaultGroup.class);
//
//            messageUnsubscribeService.update(dto);
//
//        return new Result();
//    }
//
//    @DeleteMapping
//    @ApiOperation("删除" )
//    public Result delete(@RequestBody Long[] ids) {
//        //效验数据
//        AssertUtils.isArrayEmpty(ids, "id" );
//
//            messageUnsubscribeService.delete(ids);
//
//        return new Result();
//    }
//
////    @GetMapping("export" )
////    @ApiOperation("导出" )
////    public void export(@ApiIgnore @RequestParam Map<String, Object> params, HttpServletResponse response) throws Exception {
////        List<MessageUnsubscribeDTO> list = messageUnsubscribeService.list(params);
////
////        EasyExcelUtils.exportExcelToTarget(response, null, list, MessageUnsubscribeExcel.class);
////    }
//
//}
