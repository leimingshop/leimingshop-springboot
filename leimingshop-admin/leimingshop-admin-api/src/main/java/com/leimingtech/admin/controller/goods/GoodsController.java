/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.admin.controller.goods;

import com.alibaba.fastjson.JSON;
import com.leimingtech.commons.tools.annotation.LogContext;
import com.leimingtech.commons.tools.annotation.LogOperation;
import com.leimingtech.commons.tools.constant.Constant;
import com.leimingtech.commons.tools.enums.ExcelEnum;
import com.leimingtech.commons.tools.page.PageData;
import com.leimingtech.commons.tools.utils.Result;
import com.leimingtech.commons.tools.validator.AssertUtils;
import com.leimingtech.commons.tools.validator.ValidatorUtils;
import com.leimingtech.commons.tools.validator.group.AddGroup;
import com.leimingtech.commons.tools.validator.group.DefaultGroup;
import com.leimingtech.commons.tools.validator.group.UpdateGroup;
import com.leimingtech.dto.sys.SysExportManagementDTO;
import com.leimingtech.dto.sys.SysExportManagementVO;
import com.leimingtech.modules.constants.spec.SpecRuleConstants;
import com.leimingtech.modules.dto.activity.goods.UpdateGoodsCheckActivityDTO;
import com.leimingtech.modules.dto.freight.template.FreightTemplateDTO;
import com.leimingtech.modules.dto.goods.GoodsConserveDTO;
import com.leimingtech.modules.dto.goods.GoodsDTO;
import com.leimingtech.modules.dto.goods.GoodsListDTO;
import com.leimingtech.modules.dto.goods.GoodsShowDTO;
import com.leimingtech.modules.dto.goods.check.GoodsCheckDTO;
import com.leimingtech.modules.dto.goods.check.GoodsCheckSaveDTO;
import com.leimingtech.modules.dto.goods.detail.GoodsDatailDTO;
import com.leimingtech.modules.dto.goods.modify.GoodsModifyDTO;
import com.leimingtech.modules.dto.goods.modify.ValidSpecSerial;
import com.leimingtech.modules.dto.goods.time.GoodsTimeDTO;
import com.leimingtech.modules.enums.evaluate.GoodsEnum;
import com.leimingtech.modules.enums.goods.GoodsErrorCodeEnum;
import com.leimingtech.modules.enums.goods.GoodsStatusEnum;
import com.leimingtech.modules.enums.spec.SpecErrorCodeEnum;
import com.leimingtech.modules.service.activity.goods.ActivityGoodsService;
import com.leimingtech.modules.service.freight.template.FreightTemplateService;
import com.leimingtech.modules.service.goods.GoodsService;
import com.leimingtech.modules.service.goods.GoodsSyncIndexService;
import com.leimingtech.modules.service.goods.check.GoodsCheckService;
import com.leimingtech.modules.service.goods.spec.GoodsSpecService;
import com.leimingtech.modules.service.goods.time.GoodsTimeService;
import com.leimingtech.modules.statuscode.GoodsStatusCode;
import com.leimingtech.mq.constant.MqConstant;
import com.leimingtech.mq.service.RabbitMqSendService;
import com.leimingtech.service.sys.SysExportManagementService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;


/**
 * 商品管理
 *
 * @author dy
 * @email 1197793912@qq.com
 * @since 1.0.0 2019-06-04
 */
@RestController
@RequestMapping("goods")
@Api(tags = "商品管理")
@Slf4j
public class GoodsController {
    @Autowired
    private GoodsService goodsService;

    @Autowired
    private GoodsTimeService goodsTimeService;

    @Autowired
    private GoodsCheckService goodsCheckService;

    @Autowired
    private GoodsSyncIndexService goodsSyncIndexService;

    @Autowired
    private GoodsSpecService goodsSpecService;

    @Autowired
    private SysExportManagementService sysExportManagementService;

    @Autowired
    private RabbitMqSendService rabbitMqSendService;

    @Autowired
    private ActivityGoodsService activityGoodsService;

    @Autowired
    private FreightTemplateService freightTemplateService;

    /**
     * 商品列表分页
     *
     * @param params
     * @return
     */
    @GetMapping("page")
    @ApiOperation("商品列表分页")
    @LogContext(note = GoodsStatusCode.GOODS_PAGE_SUCCESS_MSG, code = GoodsStatusCode.GOODS_PAGE_SUCCESS_CODE)
    @ApiImplicitParams({
            @ApiImplicitParam(name = Constant.PAGE, value = "当前页码，从1开始", paramType = "query", required = true, dataType = "int"),
            @ApiImplicitParam(name = Constant.LIMIT, value = "每页显示记录数", paramType = "query", required = true, dataType = "int"),
            @ApiImplicitParam(name = Constant.ORDER_FIELD, value = "排序字段", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = Constant.ORDER, value = "排序方式，可选值(asc、desc)", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "goodsName", value = "商品名称/商品货号", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "brandName", value = "品牌名称", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "goodsId", value = "商品ID", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "gcId", value = "分类ID", paramType = "query", dataType = "Long"),
            @ApiImplicitParam(name = "storeName", value = "所属店铺", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "goodsShow", value = "商品上下架状态（默认:2未上架,0下架状态，1上架状态）",
                    paramType = "query", dataType = "int")
    })
    public Result<PageData<GoodsListDTO>> page(@ApiIgnore @RequestParam Map<String, Object> params) {
        params.put(Constant.ORDER_FIELD, "g.create_date");
        params.put(Constant.ORDER, "desc");
        params.put("goodsStatus", GoodsStatusEnum.GOODS_AUDIT_PASS.value());
        PageData<GoodsListDTO> page = goodsService.pageGoods(params);

        return new Result<PageData<GoodsListDTO>>().ok(page);
    }

    /**
     * 审核列表
     *
     * @param params
     * @return
     */
    @GetMapping("verify/page")
    @ApiOperation("商品审核列表")
    @LogContext(note = GoodsStatusCode.GOODS_VERIFY_PAGE_SUCCESS_MSG, code = GoodsStatusCode.GOODS_VERIFY_PAGE_SUCCESS_CODE)
    @ApiImplicitParams({
            @ApiImplicitParam(name = Constant.PAGE, value = "当前页码，从1开始", paramType = "query", required = true, dataType = "int"),
            @ApiImplicitParam(name = Constant.LIMIT, value = "每页显示记录数", paramType = "query", required = true, dataType = "int"),
            @ApiImplicitParam(name = Constant.ORDER_FIELD, value = "排序字段", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = Constant.ORDER, value = "排序方式，可选值(asc、desc)", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "goodsName", value = "商品名称/商品货号", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "brandName", value = "品牌名称", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "gcId", value = "分类ID", paramType = "query", dataType = "Long"),
            @ApiImplicitParam(name = "goodsStatus", value = "审核状态:10:待审核，20:审核未通过，30:审核通过,40:违规下架,50:未发布",
                    paramType = "query", required = true, dataType = "int"),
            @ApiImplicitParam(name = "storeName", value = "所属店铺", paramType = "query", dataType = "String")
    })
    public Result<PageData<GoodsListDTO>> pageVerify(@ApiIgnore @RequestParam Map<String, Object> params) {
        params.put(Constant.ORDER_FIELD, "g.create_date");
        params.put(Constant.ORDER, "desc");
        params.put("ignore", GoodsStatusEnum.GOODS_AUDIT_UNPUBLISHED.value());
        PageData<GoodsListDTO> page = goodsService.pageVerify(params);

        return new Result<PageData<GoodsListDTO>>().ok(page);
    }

    /**
     * 商品审核列表导出
     *
     * @param params
     * @param response
     * @throws Exception
     */
    @GetMapping("check/export")
    @ApiOperation("商品审核列表导出")
    @LogOperation("商品审核列表导出")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "goodsName", value = "商品名称/商品货号", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "brandName", value = "品牌名称", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "gcId", value = "分类ID", paramType = "query", dataType = "Long"),
            @ApiImplicitParam(name = "goodsStatus", value = "审核状态:10:待审核，20:审核未通过，30:审核通过,40:违规下架,50:未发布",
                    paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "storeName", value = "所属店铺", paramType = "query", dataType = "String")
    })
    @LogContext(note = GoodsStatusCode.GOODS_STATUS_EXPORT_MSG, code = GoodsStatusCode.GOODS_STATUS_EXPORT_CODE)
    public void checkExport(@ApiIgnore @RequestParam Map<String, Object> params, HttpServletResponse response) {

        // 保存导出记录表 状态为导出中
        String assignmentName = ExcelEnum.GOODS_CHECK_EXPORT.getName();
        SysExportManagementDTO sysExportManagementDTO = new SysExportManagementDTO();
        sysExportManagementDTO.setAssignmentName(assignmentName);
        sysExportManagementDTO.setOperationStatus(1);
        sysExportManagementDTO.setOperatingLogo(1);
        sysExportManagementDTO.setStoreId(0L);
        SysExportManagementVO sysExportManagementVO = sysExportManagementService.save(sysExportManagementDTO);

        params.put(Constant.PAGE, 1);
        params.put(Constant.LIMIT, 10000);
        params.put("managementId", sysExportManagementVO.getId());

        // 发送异步消息将参数传递过去，消费者进行处理
        rabbitMqSendService.sendMsg(MqConstant.LEIMINGTECH_GOODS_CHECK_EXPORT_QUEUE, JSON.toJSONString(params));
        log.info("商品导出结束");
//        List<GoodsListDTO> list = goodsService.selectGoodsListExport(params);

//        try {
//            ExcelUtils.exportExcelToTarget(response, null, list, GoodsCheckExcel.class);
//        } catch (Exception e) {
//            log.error("错误信息为" + e);
//        }
    }

    /**
     * 根据id查询商品信息
     *
     * @param id
     * @return
     */
    @GetMapping("{id}")
    @ApiOperation("商品基本信息")
    public Result<GoodsDTO> get(@PathVariable("id") Long id) {
        GoodsDTO data = goodsService.get(id);

        return new Result<GoodsDTO>().ok(data);
    }

    @PostMapping("valid/goodsSerial")
    @ApiOperation("校验规格货号")
    @LogOperation("校验规格货号")
    public Result<List> beforeSave(@RequestBody ValidSpecSerial specSerials) {
        ValidatorUtils.validateEntity(specSerials, UpdateGroup.class, DefaultGroup.class);
        List<String> result = goodsSpecService.checkSpecSerial(specSerials);
        return new Result<List>().ok(result);
    }

    /**
     * 商品保存
     *
     * @param dto
     * @return
     */
    @PostMapping
    @ApiOperation("商品保存")
    @LogOperation("商品保存")
    @LogContext(note = GoodsStatusCode.GOODS_SAVE_SUCCESS_MSG, code = GoodsStatusCode.GOODS_SAVE_SUCCESS_CODE)
    public Result save(@RequestBody GoodsConserveDTO dto) {
        //效验数据
        ValidatorUtils.validateEntity(dto, AddGroup.class, DefaultGroup.class);
        //校验金额
        if (CollectionUtils.isEmpty(dto.getGoodsSpecSaveDTOList()) && dto.getSpecStorage() > SpecRuleConstants.STORAGE_MAX) {
            return new Result().error(SpecErrorCodeEnum.STORAGE_OUT.code(), SpecErrorCodeEnum.STORAGE_OUT.msg());
        }

        // 校验运费模板
        if (GoodsEnum.EXPRESS_FLAG_YES.value() != dto.getExpressFlag()) {
            dto.setFreightTemplateId(null);
        }
        if (dto.getFreightTemplateId() != null) {
            // 查询店铺运费模板
            FreightTemplateDTO freightTemplateDTO = freightTemplateService.getStoreFreightTemplateById(dto.getFreightTemplateId(), dto.getStoreId());
            if (freightTemplateDTO == null) {
                return new Result().error("运费模板不存在");
            }
        }

        Long goodsId = goodsService.save(dto);
        goodsSyncIndexService.goodsIndexSyncByGoodsId(goodsId);

        return new Result().ok(null, "保存成功");
    }

    /**
     * 商品修改
     *
     * @param dto
     * @return
     */
    @PutMapping
    @ApiOperation("商品修改")
    @LogOperation("商品修改")
    @LogContext(note = GoodsStatusCode.GOODS_UPDATE_SUCCESS_MSG, code = GoodsStatusCode.GOODS_UPDATE_SUCCESS_CODE)
    public Result update(@RequestBody GoodsModifyDTO dto) {
        //效验数据
        ValidatorUtils.validateEntity(dto, UpdateGroup.class, DefaultGroup.class);
        //校验库存
        if (CollectionUtils.isEmpty(dto.getGoodsSpecSaveDTOList()) && dto.getSpecStorage() > SpecRuleConstants.STORAGE_MAX) {
            return new Result().error(SpecErrorCodeEnum.STORAGE_OUT.code(), SpecErrorCodeEnum.STORAGE_OUT.msg());
        }

        // 校验运费模板
        if (GoodsEnum.EXPRESS_FLAG_YES.value() != dto.getExpressFlag()) {
            dto.setFreightTemplateId(null);
        }
        if (dto.getFreightTemplateId() != null) {
            // 查询店铺运费模板
            FreightTemplateDTO freightTemplateDTO = freightTemplateService.getStoreFreightTemplateById(dto.getFreightTemplateId(), dto.getStoreId());
            if (freightTemplateDTO == null) {
                return new Result().error("运费模板不存在");
            }
        }

        dto.setType(GoodsStatusEnum.IS_ADMIN.value());
        Long goodsId = goodsService.update(dto);
        goodsSyncIndexService.goodsIndexSyncByGoodsId(goodsId);

        return new Result().ok(null, "修改成功");
    }

    /**
     * 商品删除
     *
     * @param ids
     * @return
     */
    @DeleteMapping
    @ApiOperation("商品删除")
    @LogOperation("商品删除")
    @LogContext(note = GoodsStatusCode.GOODS_DELETE_SUCCESS_MSG, code = GoodsStatusCode.GOODS_DELETE_SUCCESS_CODE)
    public Result delete(@RequestBody Long[] ids) {
        //效验数据
        AssertUtils.isArrayEmpty(ids, "id");

        goodsService.delete(ids);

        return new Result().ok(null, "删除成功");
    }

    /**
     * 导出
     *
     * @param params
     * @param response
     * @throws Exception
     */
    @GetMapping("export")
    @ApiOperation("商品导出")
    @LogOperation("商品导出")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "goodsName", value = "商品名称/商品货号", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "brandName", value = "品牌名称", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "gcId", value = "分类ID", paramType = "query", dataType = "Long"),
            @ApiImplicitParam(name = "storeName", value = "所属店铺", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "storeName", value = "所属店铺", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "goodsShow", value = "商品上下架状态（默认:2未上架,0下架状态，1上架状态）",
                    paramType = "query", dataType = "int")
    })
    @LogContext(note = GoodsStatusCode.GOODS_LIST_EXPORT_MSG, code = GoodsStatusCode.GOODS_LIST_EXPORT_CODE)
    public void export(@ApiIgnore @RequestParam Map<String, Object> params, HttpServletResponse response) {
        // 保存导出记录表 状态为导出中
        String assignmentName = ExcelEnum.GOODS_EXPORT.getName();
        SysExportManagementDTO sysExportManagementDTO = new SysExportManagementDTO();
        sysExportManagementDTO.setAssignmentName(assignmentName);
        sysExportManagementDTO.setOperationStatus(1);
        sysExportManagementDTO.setOperatingLogo(1);
        sysExportManagementDTO.setStoreId(0L);
        SysExportManagementVO sysExportManagementVO = sysExportManagementService.save(sysExportManagementDTO);
        params.put(Constant.PAGE, 1);
        params.put(Constant.LIMIT, 20);
        params.put("managementId", sysExportManagementVO.getId());

        // 发送异步消息将参数传递过去，消费者进行导出处理
        rabbitMqSendService.sendMsg(MqConstant.LEIMINGTECH_GOODS_EXPORT_QUEUE, JSON.toJSONString(params));
        log.info("商品导出结束");
    }

    /**
     * 根据商品Id查询商品详情
     *
     * @param goodsId
     * @throws Exception
     */
    @GetMapping("detail/{goodsId}")
    @ApiOperation("根据商品Id查询商品详情")
    @LogContext(note = GoodsStatusCode.GOODS_DETAIL_SUCCESS_MSG, code = GoodsStatusCode.GOODS_DETAIL_SUCCESS_CODE)
    public Result<GoodsDatailDTO> getDetail(@PathVariable("goodsId") Long goodsId) {
        return new Result<GoodsDatailDTO>().ok(goodsService.getDetail(goodsId), "查询成功");
    }


    /**
     * 商品立即上下架操作
     *
     * @param dto
     * @return
     * @date 2020/3/4/004 15:05
     * @author xuzhch
     **/
    @PutMapping("show")
    @ApiOperation("商品上下架状态修改")
    @LogOperation("商品上下架状态修改")
    @LogContext(note = GoodsStatusCode.GOODS_SHOW_UPDATE_MSG, code = GoodsStatusCode.GOODS_SHOW_UPDATE_CODE)
    public Result updateGoodsShow(@RequestBody GoodsShowDTO dto) {
        ValidatorUtils.validateEntity(dto, UpdateGroup.class, DefaultGroup.class);
        // 上架操作需要校验当前店铺上架商品的数量是否超过店铺等级限制的数量
        if (dto.getGoodsShow() == GoodsStatusEnum.GOODS_SHOW_ON.value()) {
            Boolean flag = goodsService.adminValidatePublishGoodsNum(dto.getIds().toArray(new Long[dto.getIds().size()]));
            if (!flag) {
                return new Result().error("部分发布的商品已达到店铺等级的最大值");
            }
        }
        // 上下架操作
        goodsService.updateGoodsShow(dto);
        // 更新商品和规格索引
        goodsSyncIndexService.batchGoodsIndexSync(dto.getIds());
        return new Result().ok(null, "商品状态修改成功");
    }

    /**
     * 商品定时上下架信息保存、更新
     *
     * @param dto
     * @return
     * @date 2020/3/4/004 15:05
     * @author xuzhch
     **/
    @PutMapping("timed/show")
    @ApiOperation("商品定时上下架状态修改")
    @LogOperation("商品定时上下架状态修改")
    @LogContext(note = GoodsStatusCode.TIMED_GOODS_SHOW_UPDATE_MSG, code = GoodsStatusCode.TIMED_GOODS_SHOW_UPDATE_CODE)
    public Result timedUpdateGoodsShow(@RequestBody GoodsShowDTO dto) {
        ValidatorUtils.validateEntity(dto, UpdateGroup.class, DefaultGroup.class);
        // 上架操作需要校验当前店铺上架商品的数量是否超过店铺等级限制的数量
        if (dto.getGoodsShow() == GoodsStatusEnum.GOODS_SHOW_ON.value()) {
            Boolean flag = goodsService.adminValidatePublishGoodsNum(dto.getIds().toArray(new Long[dto.getIds().size()]));
            if (!flag) {
                return new Result().error("部分发布的商品已达到店铺等级的最大值");
            }
        }
        // 上下架操作
        goodsService.timedUpdateGoodsShow(dto);
        return new Result().ok(null, "定时操作保存成功");
    }

    /**
     * 修改审核状态
     *
     * @param dto
     * @return
     */
    @PutMapping("check")
    @ApiOperation("修改审核状态")
    @LogOperation("修改审核状态")
    @LogContext(note = GoodsStatusCode.GOODS_STATUS_UPDATE_MSG, code = GoodsStatusCode.GOODS_STATUS_UPDATE_CODE)
    public Result checkGoodsStatus(@RequestBody GoodsCheckSaveDTO dto) {
        //效验数据
        ValidatorUtils.validateEntity(dto, AddGroup.class, DefaultGroup.class);
        //如果商品未通过审核,返回已拒绝提示
        if (GoodsStatusEnum.GOODS_AUDIT_NOTPASS.value() == dto.getGoodState()) {
            //商品未通过审核时,拒绝原因不可为空
            if (StringUtils.isBlank(dto.getRemarks())) {
                return new Result().error(GoodsErrorCodeEnum.REMARKS_IS_NULL.code(),
                        GoodsErrorCodeEnum.REMARKS_IS_NULL.msg());
            }
            goodsService.checkGoodsStatus(dto);
            return new Result().ok(null, "审核已拒绝");
        }
        goodsService.checkGoodsStatus(dto);
        return new Result().ok(null, "审核已通过");
    }

    /**
     * 查询商品上下架定时状态
     *
     * @return
     */
    @GetMapping("show/timing/{goodsId}")
    @ApiOperation("商品定时上下架状态查询")
    public Result<GoodsTimeDTO> show(@PathVariable("goodsId") Long goodsId) {

        GoodsTimeDTO goodsTimeDTO = goodsTimeService.selectByGoodsId(goodsId);

        return new Result<GoodsTimeDTO>().ok(goodsTimeDTO, "查询成功");
    }

    /**
     * 查询商品审核原因
     *
     * @return
     */
    @GetMapping("remarks/{goodsId}")
    @ApiOperation("查询商品审核原因")
    public Result<GoodsCheckDTO> remarks(@PathVariable("goodsId") Long goodsId) {

        GoodsCheckDTO goodsCheckDTO = goodsCheckService.selectByGoodsId(goodsId);

        return new Result<GoodsCheckDTO>().ok(goodsCheckDTO, "查询成功");
    }

    @PostMapping("check/goods/activity")
    @ApiOperation("校验商品是否参加促销活动")
    public Result<UpdateGoodsCheckActivityDTO> checkGoodsActivity(@RequestBody List<Long> goodsIds) {
        UpdateGoodsCheckActivityDTO updateGoodsCheckActivityDTO = activityGoodsService.checkGoodsActivity(goodsIds);
        if (updateGoodsCheckActivityDTO != null) {
            return new Result<UpdateGoodsCheckActivityDTO>().ok(updateGoodsCheckActivityDTO, "查询成功");
        } else {
            return new Result<UpdateGoodsCheckActivityDTO>().error("查询失败");
        }
    }
}
