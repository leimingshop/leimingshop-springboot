/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.seller.controller.goods;

import cn.hutool.core.collection.CollectionUtil;
import com.alibaba.fastjson.JSON;
import com.leimingtech.commons.tools.annotation.LogContext;
import com.leimingtech.commons.tools.annotation.LogOperation;
import com.leimingtech.commons.tools.constant.Constant;
import com.leimingtech.commons.tools.enums.ExcelEnum;
import com.leimingtech.commons.tools.exception.ErrorCode;
import com.leimingtech.commons.tools.page.PageData;
import com.leimingtech.commons.tools.redis.SellerDetailRedis;
import com.leimingtech.commons.tools.security.user.SellerDetail;
import com.leimingtech.commons.tools.utils.EasyExcelUtils;
import com.leimingtech.commons.tools.utils.ExcelCheckUtil;
import com.leimingtech.commons.tools.utils.Result;
import com.leimingtech.commons.tools.validator.ValidatorUtils;
import com.leimingtech.commons.tools.validator.group.AddGroup;
import com.leimingtech.commons.tools.validator.group.DefaultGroup;
import com.leimingtech.commons.tools.validator.group.UpdateGroup;
import com.leimingtech.dto.sys.SysExportManagementDTO;
import com.leimingtech.dto.sys.SysExportManagementVO;
import com.leimingtech.exception.ServiceException;
import com.leimingtech.modules.constants.spec.SpecRuleConstants;
import com.leimingtech.modules.dto.freight.template.FreightTemplateDTO;
import com.leimingtech.modules.dto.goods.GoodsConserveDTO;
import com.leimingtech.modules.dto.goods.GoodsDTO;
import com.leimingtech.modules.dto.goods.GoodsListDTO;
import com.leimingtech.modules.dto.goods.GoodsShowDTO;
import com.leimingtech.modules.dto.goods.check.GoodsCheckDTO;
import com.leimingtech.modules.dto.goods.detail.GoodsDatailDTO;
import com.leimingtech.modules.dto.goods.modify.GoodsModifyDTO;
import com.leimingtech.modules.dto.goods.modify.ValidSpecSerial;
import com.leimingtech.modules.dto.goods.time.GoodsTimeDTO;
import com.leimingtech.modules.enums.evaluate.GoodsEnum;
import com.leimingtech.modules.enums.goods.GoodsErrorCodeEnum;
import com.leimingtech.modules.enums.goods.GoodsStatusEnum;
import com.leimingtech.modules.enums.spec.SpecErrorCodeEnum;
import com.leimingtech.modules.excel.goods.GoodsTemplateExcel;
import com.leimingtech.modules.service.freight.template.FreightTemplateService;
import com.leimingtech.modules.service.goods.GoodsService;
import com.leimingtech.modules.service.goods.GoodsSyncIndexService;
import com.leimingtech.modules.service.goods.check.GoodsCheckService;
import com.leimingtech.modules.service.goods.time.GoodsTimeService;
import com.leimingtech.modules.statuscode.GoodsStatusCode;
import com.leimingtech.modules.vo.GoodsImportResultVO;
import com.leimingtech.mq.constant.MqConstant;
import com.leimingtech.mq.service.RabbitMqSendService;
import com.leimingtech.service.sys.SysExportManagementService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
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
    private SellerDetailRedis sellerDetailRedis;

    @Autowired
    private FreightTemplateService freightTemplateService;


    @Autowired
    private GoodsSyncIndexService goodsSyncIndexService;

    @Autowired
    private SysExportManagementService sysExportManagementService;

    @Autowired
    private RabbitMqSendService rabbitMqSendService;

    /**
     * 商品列表分页
     *
     * @param params
     * @return
     */
    @GetMapping("page")
    @ApiOperation("商品列表分页")
    @ApiImplicitParams({
            @ApiImplicitParam(name = Constant.PAGE, value = "当前页码，从1开始", paramType = "query", required = true, dataType = "int"),
            @ApiImplicitParam(name = Constant.LIMIT, value = "每页显示记录数", paramType = "query", required = true, dataType = "int"),
            @ApiImplicitParam(name = Constant.ORDER_FIELD, value = "排序字段", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = Constant.ORDER, value = "排序方式，可选值(asc、desc)", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "goodsName", value = "商品名称/商品货号", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "brandName", value = "品牌名称", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "gcId", value = "分类ID", paramType = "query", dataType = "Long"),
            @ApiImplicitParam(name = "storeName", value = "所属店铺", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "goodsStatus", value = "商品状态(60:草稿)", paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "goodsShow", value = "上下架状态:0下架;1上架", paramType = "query", dataType = "int")
    })
    @LogContext(note = GoodsStatusCode.GOODS_PAGE_SUCCESS_MSG, code = GoodsStatusCode.GOODS_PAGE_SUCCESS_CODE)
    public Result<PageData<GoodsListDTO>> page(@ApiIgnore @RequestParam Map<String, Object> params, @ApiIgnore SellerDetail sellerDetail) {
        if (sellerDetail == null || sellerDetail.getId() == null) {
            return new Result().error(ErrorCode.UNAUTHORIZED, "用户未登录");
        }
        if (null == sellerDetail.getStoreId()) {
            log.error("waring:  sellerDetail 缺少店铺ID");
            sellerDetailRedis.logout(sellerDetail.getId());
            throw new ServiceException("系统异常，请重新登录");
        }
        params.put("storeId", sellerDetail.getStoreId().toString());
        params.put(Constant.ORDER_FIELD, "g.create_date");
        params.put(Constant.ORDER, "desc");
        if (null != params.get("goodsStatus")) {
            params.put("goodsStatus", GoodsStatusEnum.GOODS_DRAFT.value());
        } else {
            params.put("goodsStatus", GoodsStatusEnum.GOODS_AUDIT_PASS.value());
        }
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
    @ApiImplicitParams({
            @ApiImplicitParam(name = Constant.PAGE, value = "当前页码，从1开始", paramType = "query", required = true, dataType = "int"),
            @ApiImplicitParam(name = Constant.LIMIT, value = "每页显示记录数", paramType = "query", required = true, dataType = "int"),
            @ApiImplicitParam(name = Constant.ORDER_FIELD, value = "排序字段", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = Constant.ORDER, value = "排序方式，可选值(asc、desc)", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "goodsName", value = "商品名称/商品货号", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "brandName", value = "品牌名称", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "gcId", value = "分类ID", paramType = "query", dataType = "Long"),
            @ApiImplicitParam(name = "goodsStatus", value = "审核状态:10:待审核，20:审核未通过，30:审核通过,40:违规下架,50:未发布",
                    paramType = "query", required = true, dataType = "Long"),
            @ApiImplicitParam(name = "storeName", value = "所属店铺", paramType = "query", dataType = "String")
    })
    @LogContext(note = GoodsStatusCode.GOODS_VERIFY_PAGE_SUCCESS_MSG, code = GoodsStatusCode.GOODS_VERIFY_PAGE_SUCCESS_CODE)
    public Result<PageData<GoodsListDTO>> pageVerify(@ApiIgnore @RequestParam Map<String, Object> params,
                                                     @ApiIgnore SellerDetail sellerDetail) {
        if (sellerDetail == null || sellerDetail.getId() == null) {
            return new Result().error(ErrorCode.UNAUTHORIZED, "请重新登录");
        }
        params.put("storeId", sellerDetail.getStoreId().toString());

        params.put(Constant.ORDER_FIELD, "g.create_date");
        params.put(Constant.ORDER, "desc");
        PageData<GoodsListDTO> page = goodsService.pageVerify(params);

        return new Result<PageData<GoodsListDTO>>().ok(page);
    }


    @DeleteMapping
    @ApiOperation("商品删除")
    @LogOperation("商品删除")
    @LogContext(note = GoodsStatusCode.GOODS_SAVE_SUCCESS_MSG, code = GoodsStatusCode.GOODS_SAVE_SUCCESS_CODE)
    public Result deleteBatch(@RequestBody Long[] goodsIds, @ApiIgnore SellerDetail sellerDetail) {
        if (sellerDetail == null || sellerDetail.getId() == null) {
            return new Result().error(ErrorCode.UNAUTHORIZED, "请登录后再操作");
        }
        goodsService.deleteBatch(goodsIds);

        return new Result().ok(null, "删除成功");
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
    public Result save(@RequestBody GoodsConserveDTO dto, @ApiIgnore SellerDetail sellerDetail) {
        //效验数据
        ValidatorUtils.validateEntity(dto, AddGroup.class, DefaultGroup.class);
        if (sellerDetail == null || sellerDetail.getId() == null) {
            return new Result().error(ErrorCode.UNAUTHORIZED, "用户未登录");
        }

        // 校验运费模板
        if (GoodsEnum.EXPRESS_FLAG_YES.value() != dto.getExpressFlag()) {
            dto.setFreightTemplateId(null);
        }
        if (dto.getFreightTemplateId() != null) {
            // 查询店铺运费模板
            FreightTemplateDTO freightTemplateDTO = freightTemplateService.getStoreFreightTemplateById(dto.getFreightTemplateId(), sellerDetail.getStoreId());
            if (freightTemplateDTO == null) {
                return new Result().error("运费模板不存在");
            }
        }

        dto.setStoreId(sellerDetail.getStoreId());

        goodsService.validGoodsParams(dto);
        //校验金额
        if (CollectionUtils.isEmpty(dto.getGoodsSpecSaveDTOList()) && dto.getSpecStorage() > SpecRuleConstants.STORAGE_MAX) {
            return new Result().error(SpecErrorCodeEnum.STORAGE_OUT.code(), SpecErrorCodeEnum.STORAGE_OUT.msg());
        }
        Long goodsId = goodsService.save(dto);

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
    public Result update(@RequestBody GoodsModifyDTO dto, @ApiIgnore SellerDetail sellerDetail) {
        //效验数据
        ValidatorUtils.validateEntity(dto, UpdateGroup.class, DefaultGroup.class);
        if (sellerDetail == null || sellerDetail.getId() == null) {
            return new Result().error(ErrorCode.UNAUTHORIZED, "用户未登录");
        }

        // 校验运费模板
        if (GoodsEnum.EXPRESS_FLAG_YES.value() != dto.getExpressFlag()) {
            dto.setFreightTemplateId(null);
        }
        if (dto.getFreightTemplateId() != null) {
            // 查询店铺运费模板
            FreightTemplateDTO freightTemplateDTO = freightTemplateService.getStoreFreightTemplateById(dto.getFreightTemplateId(), sellerDetail.getStoreId());
            if (freightTemplateDTO == null) {
                return new Result().error("运费模板不存在");
            }
        }

        //校验用户审核状态
        GoodsDTO goodsDTO = goodsService.get(dto.getId());
        if (goodsDTO.getGoodsStatus() == GoodsStatusEnum.GOODS_AUDIT_WAIT.value()) {
            return new Result().error(GoodsErrorCodeEnum.NOT_UPDATE.code(), GoodsErrorCodeEnum.NOT_UPDATE.msg());
        }
        //校验库存
        if (CollectionUtils.isEmpty(dto.getGoodsSpecSaveDTOList()) && dto.getSpecStorage() > SpecRuleConstants.STORAGE_MAX) {
            return new Result().error(SpecErrorCodeEnum.STORAGE_OUT.code(), SpecErrorCodeEnum.STORAGE_OUT.msg());
        }

        dto.setStoreId(sellerDetail.getStoreId());
        dto.setType(GoodsStatusEnum.IS_SELLER.value());
        Long goodsId = goodsService.update(dto);
        goodsSyncIndexService.goodsIndexSyncByGoodsId(goodsId);

        return new Result().ok(null, "修改成功");
    }

    @PostMapping("valid/goodsSerial")
    @ApiOperation("校验规格货号")
    @LogOperation("校验规格货号")
    public Result beforeSave(@RequestBody ValidSpecSerial specSerials) {
        List<String> result = goodsService.validSpecSerials(specSerials);
        return new Result().ok(result);
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
    public void checkExport(@ApiIgnore @RequestParam Map<String, Object> params, HttpServletResponse response, @ApiIgnore SellerDetail sellerDetail) {
        if (sellerDetail == null || sellerDetail.getId() == null) {
            throw new ServiceException("用户未登录");
        }
        // 保存导出记录表 状态为导出中
        String assignmentName = ExcelEnum.GOODS_CHECK_EXPORT.getName();
        SysExportManagementDTO sysExportManagementDTO = new SysExportManagementDTO();
        sysExportManagementDTO.setAssignmentName(assignmentName);
        sysExportManagementDTO.setOperationStatus(1);
        sysExportManagementDTO.setOperatingLogo(1);
        sysExportManagementDTO.setStoreId(sellerDetail.getStoreId());
        SysExportManagementVO sysExportManagementVO = sysExportManagementService.save(sysExportManagementDTO);

        params.put(Constant.PAGE, 1);
        params.put(Constant.LIMIT, 10000);
        params.put("storeId", sellerDetail.getStoreId().toString());
        params.put("managementId", sysExportManagementVO.getId());

        // 发送异步消息将参数传递过去，消费者进行导出处理
        rabbitMqSendService.sendMsg(MqConstant.LEIMINGTECH_GOODS_CHECK_EXPORT_QUEUE, JSON.toJSONString(params));
        log.info("商品审核列表导出结束");

    }

    /**
     * 根据商品Id查询商品详情
     *
     * @param goodsId
     * @throws Exception
     */
    @GetMapping("detail/{goodsId}")
    @ApiOperation("根据商品Id查询商品详情")
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
        // 更新商品和规格索引

        return new Result().ok(null, "定时操作保存成功");
    }

    /**
     * @param id
     * @return 校验货号是否重复
     */
    @GetMapping("serial/{id}")
    public Result checkGoodsSerial(@PathVariable("id") Long id) {
        Map<String, Object> res = goodsService.checkGoodsSerial(id);
        if (CollectionUtil.isNotEmpty(res)) {
            return new Result().error(Integer.valueOf(res.get("code").toString()), res.get("msg").toString());
        }
        return new Result().ok(null, "货号可使用");
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
     * 查询商品上下架定时状态
     *
     * @return
     */
    @GetMapping("remarks/{goodsId}")
    @ApiOperation("查询商品审核原因")
    public Result<GoodsCheckDTO> remarks(@PathVariable("goodsId") Long goodsId) {

        GoodsCheckDTO goodsCheckDTO = goodsCheckService.selectByGoodsId(goodsId);

        return new Result<GoodsCheckDTO>().ok(goodsCheckDTO, "查询成功");
    }

    @GetMapping("export")
    @ApiOperation("下载商品的导出模板")
    @LogOperation("商品导出到模板")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "goodsName", value = "商品名称/商品货号", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "brandName", value = "品牌名称", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "gcId", value = "分类ID", paramType = "query", dataType = "Long"),
            @ApiImplicitParam(name = "goodsStatus", value = "审核状态:10:待审核，20:审核未通过，30:审核通过,40:违规下架,50:未发布",
                    paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "storeName", value = "所属店铺", paramType = "query", dataType = "String")
    })
    @LogContext(note = GoodsStatusCode.GOODS_LIST_EXPORT_MSG, code = GoodsStatusCode.GOODS_LIST_EXPORT_CODE)
    public void exportTemplate(@ApiIgnore @RequestParam Map<String, Object> params, @ApiIgnore SellerDetail sellerDetail) {
        if (sellerDetail == null || sellerDetail.getId() == null) {
            throw new ServiceException("用户未登录");
        }
        String assignmentName = ExcelEnum.GOODS_EXPORT_TEMPLATE.getName();
        SysExportManagementDTO sysExportManagementDTO = new SysExportManagementDTO();
        sysExportManagementDTO.setAssignmentName(assignmentName);
        sysExportManagementDTO.setOperationStatus(1);
        sysExportManagementDTO.setOperatingLogo(1);
        sysExportManagementDTO.setStoreId(sellerDetail.getStoreId());
        SysExportManagementVO sysExportManagementVO = sysExportManagementService.save(sysExportManagementDTO);
        params.put("storeId", sellerDetail.getStoreId().toString());
        params.put("managementId", sysExportManagementVO.getId());
        params.put(Constant.PAGE, 1);
        params.put(Constant.LIMIT, 20);
        // 发送异步消息将参数传递过去，消费者进行导出处理
        rabbitMqSendService.sendMsg(MqConstant.LEIMINGTECH_GOODS_EXPORT_QUEUE, JSON.toJSONString(params));
        log.info("商品导出结束");
    }

    @PostMapping(value = "import", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ApiOperation("根据模板导入商品")
    public Result<GoodsImportResultVO> importGoods(@RequestPart("file") MultipartFile file, @ApiIgnore SellerDetail sellerDetail) {
        if (sellerDetail == null || sellerDetail.getId() == null) {
            return new Result<GoodsImportResultVO>().error(ErrorCode.UNAUTHORIZED, "用户未登录");
        }
        try {
            InputStream inputStream = file.getInputStream();
            if (ExcelCheckUtil.isExcel(inputStream)) {
                GoodsImportResultVO goodsImportResultVO = goodsService.importGoods(file, sellerDetail.getStoreId());
                if (CollectionUtil.isEmpty(goodsImportResultVO.getErrorMessage())) {
                    return new Result<GoodsImportResultVO>().ok(goodsImportResultVO, "导入成功");
                } else {
                    StringBuilder stringBuilder = new StringBuilder();
                    goodsImportResultVO.getErrorMessage().forEach(stringStringMap -> stringBuilder.append(stringStringMap).append("\\b"));
                    return new Result<GoodsImportResultVO>().error(500, stringBuilder.toString());
                }
            } else {
                log.error("文件类型异常");
                return new Result<GoodsImportResultVO>().error("文件类型异常");
            }
        } catch (Exception e) {
            log.error("商品导入异常：" + e);
            return new Result<GoodsImportResultVO>().error("商品导入异常");
        }
    }

    @GetMapping("download/template")
    @ApiOperation("下载商品导入模板")
    public void downloadGoodsTemplate(HttpServletResponse response) {
        List<GoodsTemplateExcel> list = new ArrayList<>();
        // 样本数据
        GoodsTemplateExcel goodsTemplateExcel = new GoodsTemplateExcel();
        goodsTemplateExcel.setGoodsGcName("商品一级分类名称;商品二级分类名称;商品三级分类名称");
        goodsTemplateExcel.setStoreGcName("店铺商品一级分类;店铺商品二级分类");
        goodsTemplateExcel.setGoodsName("商品的名称");
        goodsTemplateExcel.setGoodsSubName("商品的副标题名称");
        goodsTemplateExcel.setGoodsLabel("商品标签 ");
        goodsTemplateExcel.setGoodsSellPrice(new BigDecimal(1000));
        goodsTemplateExcel.setGoodsCostPrice(new BigDecimal(800));
        goodsTemplateExcel.setGoodsBrand("商品品牌");
        goodsTemplateExcel.setExpressFlag(1);
        goodsTemplateExcel.setFreightTemplate("京东模板");
        goodsTemplateExcel.setFreightBearType(1);
        goodsTemplateExcel.setInvoice(1);
        goodsTemplateExcel.setInvoiceType("1,3");
        goodsTemplateExcel.setInvoiceContent("1,2");
        goodsTemplateExcel.setGoodsAttrAndValue("{属性值名称:属性值};{属性值名称:属性值}");
        goodsTemplateExcel.setGoodsSepcNameValue("{规格名称:规格值,规格名称:规格值};{规格名称:规格值,规格名称:规格值}");
        goodsTemplateExcel.setGoodsSetting("1000.00,800.00,100");
        goodsTemplateExcel.setAfterSaleTemplate("<p>厂家服务</p>" +
                "<p><br></p><p>本产品全国联保，享受三包服务，质保期为：一年质保</p><p><br></p>" +
                "<p>如因质量问题或故障，凭厂商维修中心或特约维修点的质量检测证明，享受7日内退货，15日内换货，15日以上在质保期内享受免费保修等三包服务！</p>" +
                "<p><br></p><p>(注:如厂家在商品介绍中有售后保障的说明,则此商品按照厂家说明执行售后保障服务。)</p>" +
                "<p><br></p><p>平台承诺</p><p><br></p><p>本平台卖家销售并发货的商品，由平台卖家提供发票和相应的售后服务。请您放心购买！</p><p><br></p><p>注：因厂家会在没有任何提前通知的情况下更改产品包装、产地或者一些附件，本司不能确保客户收到的货物与商城图片、产地、附件说明完全一致。只能确保为原厂正货！并且保证与当时市场上同样主流新品一致。若本商城没有及时更新，请大家谅解！</p>" +
                "<p><br></p><p>正品行货</p><p><br></p><p>本商城向您保证所售商品均为正品行货，自营商品开具机打发票或电子发票。</p><p><br></p><p>全国联保</p>" +
                "<p><br></p><p>凭质保证书及京东商城发票，可享受全国联保服务（奢侈品、钟表除外；奢侈品、钟表由平台联系保修，享受法定三包售后服务），与您亲临商场选购的商品享受相同的质量保证。本商城还为您提供具有竞争力的商品价格和运费政策，请您放心购买！</p>");
        goodsTemplateExcel.setPictureUrl("/group1/M00/00/2C/wKgAD13SMriAMAqSAAJVXrx0aEk432.png;/group1/M00/00/2C/wKgAD13SMr2AbFxoAAJVXrx0aEk889.png");
        goodsTemplateExcel.setSkuPictureUrl("/group1/M00/00/2C/wKgAD13SMriAMAqSAAJVXrx0aEk432.png,/group1/M00/00/2C/wKgAD13SMriAMAqSAAJVXrx0aEk432.png;/group1/M00/00/2C/wKgAD13SMriAMAqSAAJVXrx0aEk432.png");
        goodsTemplateExcel.setGoodsInfo("[{\"file\":{\"src\":\"/group1/M00/00/2E/wKgAD13TqCmANkUmAAAJ9Fu1v5456.jpeg\"}}," +
                "{\"file\":{\"src\":\"/group1/M00/00/2E/wKgAD13TqWGASaehAATVL_dvHMo26.jpeg\"}}," +
                "{\"file\":{\"name\":\"blob:http://152.136.176.161/4a9de399-da4d-4be3-ad3c-108129fcbd46\"" +
                ",\"src\":\"/group1/M00/00/2E/wKgAD13TqAWAKP0GAAQJ8op1Prw193.png\"}}]");
        for (int i = 0; i < 10; i++) {
            list.add(goodsTemplateExcel);
        }
        try {
            EasyExcelUtils.exportExcelToTarget(response, null, list, GoodsTemplateExcel.class);
        } catch (Exception e) {
            log.error("下载商品导入模板异常:" + e);
        }
    }

}
