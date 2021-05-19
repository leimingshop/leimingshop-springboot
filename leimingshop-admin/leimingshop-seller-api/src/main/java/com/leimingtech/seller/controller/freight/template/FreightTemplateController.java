/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.seller.controller.freight.template;

import com.alibaba.fastjson.JSONArray;
import com.leimingtech.commons.tools.constant.Constant;
import com.leimingtech.commons.tools.exception.ErrorCode;
import com.leimingtech.commons.tools.page.PageData;
import com.leimingtech.commons.tools.security.user.SellerDetail;
import com.leimingtech.commons.tools.utils.ConvertUtils;
import com.leimingtech.commons.tools.utils.IdGenerator;
import com.leimingtech.commons.tools.utils.Result;
import com.leimingtech.commons.tools.validator.ValidatorUtils;
import com.leimingtech.commons.tools.validator.group.AddGroup;
import com.leimingtech.commons.tools.validator.group.UpdateGroup;
import com.leimingtech.modules.dto.freight.template.*;
import com.leimingtech.modules.enums.freight.template.FreightTemplateEnum;
import com.leimingtech.modules.service.freight.template.FreightTemplateService;
import com.leimingtech.modules.service.goods.GoodsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <运费模板管理>
 *
 * @author 刘远杰
 * @email 2634443725@qq.com
 * @since 1.0 2020/4/21
 */
@RestController
@RequestMapping("freight/template")
@Api(tags = "运费模板管理")
public class FreightTemplateController {

//    private Long storeId = 1196266068939710466L;

    @Autowired
    private FreightTemplateService freightTemplateService;

    @Autowired
    private GoodsService goodsService;

    @GetMapping("admin/page")
    @ApiOperation("后台运费模板分页")
    @ApiImplicitParams({
            @ApiImplicitParam(name = Constant.PAGE, value = "当前页码，从1开始", paramType = "query", required = true, dataType = "int"),
            @ApiImplicitParam(name = Constant.LIMIT, value = "每页显示记录数", paramType = "query", required = true, dataType = "int"),
            @ApiImplicitParam(name = "templateName", value = "模版名称", paramType = "query", dataType = "String"),
    })
    public Result<PageData<AdminFreightTemplatePageDTO>> adminPage(@ApiIgnore @RequestParam Map<String, Object> params, @ApiIgnore SellerDetail sellerDetail) {
        // 获取店铺信息
        if (sellerDetail == null || sellerDetail.getStoreId() == null) {
            return new Result<PageData<AdminFreightTemplatePageDTO>>().error(ErrorCode.UNAUTHORIZED, "用户未登录");
        }
        Long storeId = sellerDetail.getStoreId();
        params.put("storeId", storeId.toString());

        // 查询运费模板
        PageData<AdminFreightTemplatePageDTO> adminPage = freightTemplateService.adminPage(params);
        return new Result<PageData<AdminFreightTemplatePageDTO>>().ok(adminPage, "查询成功");
    }

    @GetMapping("list")
    @ApiOperation("店铺运费模板列表")
    public Result<List<AdminFreightTemplatePageDTO>> list(@ApiIgnore SellerDetail sellerDetail) {
        // 获取店铺信息
        if (sellerDetail == null || sellerDetail.getStoreId() == null) {
            return new Result<List<AdminFreightTemplatePageDTO>>().error(ErrorCode.UNAUTHORIZED, "用户未登录");
        }
        Long storeId = sellerDetail.getStoreId();

        // 查询店铺运费模板
        List<AdminFreightTemplatePageDTO> list = freightTemplateService.storeList(storeId);
        return new Result<List<AdminFreightTemplatePageDTO>>().ok(list, "查询成功");
    }

    @GetMapping("admin/detail")
    @ApiOperation("查询运费模板")
    public Result<AdminFreightTemplateDetailDTO> adminDetail(@RequestParam("id") Long id, @ApiIgnore SellerDetail sellerDetail) {
        // 获取店铺信息
        if (sellerDetail == null || sellerDetail.getStoreId() == null) {
            return new Result<AdminFreightTemplateDetailDTO>().error(ErrorCode.UNAUTHORIZED, "用户未登录");
        }
        Long storeId = sellerDetail.getStoreId();

        AdminFreightTemplateDetailDTO adminFreightTemplateDetailDTO = freightTemplateService.adminDetail(id, storeId);

        return new Result<AdminFreightTemplateDetailDTO>().ok(adminFreightTemplateDetailDTO, "查询成功");

    }

    @PostMapping("add")
    @ApiOperation("新增运费模板")
    public Result save(@RequestBody InsertFreightTemplateDTO insertDto, @ApiIgnore SellerDetail sellerDetail) {
        // 获取店铺信息
        if (sellerDetail == null || sellerDetail.getStoreId() == null) {
            return new Result<>().error(ErrorCode.UNAUTHORIZED, "用户未登录");
        }
        Long storeId = sellerDetail.getStoreId();

        // 模板数据校验
        ValidatorUtils.validateEntity(insertDto, AddGroup.class);
        // 数据转化
        FreightTemplateDTO freightTemplate = ConvertUtils.sourceToTarget(insertDto, FreightTemplateDTO.class);
        freightTemplate.setId(IdGenerator.defaultSnowflakeId());
        freightTemplate.setStoreId(storeId);
        // TODO: 2020/4/21 只支持按件模板
        freightTemplate.setFreightTemplateRuleList(new ArrayList<>());

        // 校验名称是否重复
        Map<String, Object> params = new HashMap<>(1);
        params.put("templateName", insertDto.getTemplateName());
        params.put("storeId", storeId.toString());
        List<FreightTemplateDTO> freightTemplateList = freightTemplateService.list(params);
        if (CollectionUtils.isNotEmpty(freightTemplateList)) {
            return new Result<>().error(ErrorCode.FORBIDDEN, "模板名称已存在");
        }

        for (InsertFreightTemplateRuleDTO insertFreightTemplateRule : insertDto.getFreightTemplateRuleList()) {
            // 规则数据校验
            ValidatorUtils.validateEntity(insertFreightTemplateRule, AddGroup.class);

            // 数据转化
            FreightTemplateRuleDTO freightTemplateRule = ConvertUtils.sourceToTarget(insertFreightTemplateRule, FreightTemplateRuleDTO.class);
            freightTemplateRule.setAreaIds(JSONArray.toJSONString(insertFreightTemplateRule.getAreaIdsArr()));
            freightTemplateRule.setTemplateId(freightTemplate.getId());
            freightTemplate.getFreightTemplateRuleList().add(freightTemplateRule);
        }

        // 保存
        Boolean flag = freightTemplateService.saveFreightTemplate(freightTemplate);
        if (flag) {
            return new Result<>().ok(null, "保存成功");
        } else {
            return new Result<>().error("保存失败");
        }
    }

    @PutMapping("edit")
    @ApiOperation("修改运费模板")
    public Result save(@RequestBody EditFreightTemplateDTO editDto, @ApiIgnore SellerDetail sellerDetail) {
        // 获取店铺信息
        if (sellerDetail == null || sellerDetail.getStoreId() == null) {
            return new Result<>().error(ErrorCode.UNAUTHORIZED, "用户未登录");
        }
        Long storeId = sellerDetail.getStoreId();

        // 模板数据校验
        ValidatorUtils.validateEntity(editDto, UpdateGroup.class);

        // 查询运费模板
        FreightTemplateDTO storeFreightTemplate = freightTemplateService.getStoreFreightTemplateById(editDto.getId(), storeId);
        if (storeFreightTemplate == null) {
            return new Result<>().error("运费模板不存在");
        }

        // 数据转化
        FreightTemplateDTO freightTemplate = ConvertUtils.sourceToTarget(editDto, FreightTemplateDTO.class);
        freightTemplate.setStoreId(storeId);
        freightTemplate.setFreightTemplateRuleList(new ArrayList<>());

        // 校验名称是否重复
        Map<String, Object> params = new HashMap<>(1);
        params.put("templateName", editDto.getTemplateName());
        params.put("storeId", storeId.toString());
        List<FreightTemplateDTO> freightTemplateList = freightTemplateService.list(params);
        if (CollectionUtils.isNotEmpty(freightTemplateList)) {
            for (FreightTemplateDTO freightTemplateDTO : freightTemplateList) {
                if (!freightTemplateDTO.getId().equals(editDto.getId())) {
                    return new Result<>().error(ErrorCode.FORBIDDEN, "模板名称已存在");
                }
            }
        }

        for (InsertFreightTemplateRuleDTO insertFreightTemplateRule : editDto.getFreightTemplateRuleList()) {
            // 规则数据校验
            ValidatorUtils.validateEntity(insertFreightTemplateRule, AddGroup.class);

            // 数据转化
            FreightTemplateRuleDTO freightTemplateRule = ConvertUtils.sourceToTarget(insertFreightTemplateRule, FreightTemplateRuleDTO.class);
            freightTemplateRule.setAreaIds(JSONArray.toJSONString(insertFreightTemplateRule.getAreaIdsArr()));
            freightTemplateRule.setTemplateId(freightTemplate.getId());
            freightTemplate.getFreightTemplateRuleList().add(freightTemplateRule);
        }

        // 保存
        Boolean flag = freightTemplateService.updateFreightTemplate(freightTemplate);
        if (flag) {
            return new Result<>().ok(null, "保存成功");
        } else {
            return new Result<>().error("保存失败");
        }
    }

    @DeleteMapping
    @ApiOperation("删除运费模板")
    public Result delete(@RequestBody Long[] ids, @ApiIgnore SellerDetail sellerDetail) {
        // 获取店铺信息
        if (sellerDetail == null || sellerDetail.getStoreId() == null) {
            return new Result<>().error(ErrorCode.UNAUTHORIZED, "用户未登录");
        }
        Long storeId = sellerDetail.getStoreId();

        freightTemplateService.deleteStoreFreightTemplate(ids, storeId);
        return new Result<>().ok(null, "删除成功");
    }

    @PutMapping("default")
    @ApiOperation("修改运费模板")
    public Result delete(@RequestParam("id") Long id, @RequestParam("defaultFlag") Integer defaultFlag, @ApiIgnore SellerDetail sellerDetail) {
        // 获取店铺信息
        if (sellerDetail == null || sellerDetail.getStoreId() == null) {
            return new Result<>().error(ErrorCode.UNAUTHORIZED, "用户未登录");
        }
        Long storeId = sellerDetail.getStoreId();

        if (FreightTemplateEnum.DEFAULT_FLAG_YES.value() != defaultFlag && FreightTemplateEnum.DEFAULT_FLAG_NO.value() != defaultFlag) {
            return new Result<>().error("参数错误");
        }

        FreightTemplateDTO freightTemplate = freightTemplateService.getStoreFreightTemplateById(id, storeId);
        if (freightTemplate == null) {
            return new Result<>().error("运费模板不存在");
        }

        // 修改默认运费模板
        freightTemplateService.updateDefaultFreightTemplate(id, defaultFlag, storeId);
        return new Result<>().ok(null, "修改成功");
    }

    @GetMapping("count/goods")
    @ApiOperation("查询运费模板关联商品数量")
    public Result<Integer> countGoodsByFreightTemplateId(@RequestParam("id") Long id) {
        Integer count = goodsService.countGoodsByFreightTemplateId(id);
        if (count != null) {
            return new Result<Integer>().ok(count, "查询成功");
        } else {
            return new Result<Integer>().error("服务器内部异常");
        }
    }

    @DeleteMapping("goods/template")
    @ApiOperation("更新原运费模板并删除")
    public Result updateGoodsFreightTemplate(@RequestParam("oldFreightTemplateId") Long oldFreightTemplateId,
                                             @RequestParam("newFreightTemplateId") Long newFreightTemplateId,
                                             @ApiIgnore SellerDetail sellerDetail) {
        // 获取店铺信息
        if (sellerDetail == null || sellerDetail.getStoreId() == null) {
            return new Result<>().error(ErrorCode.UNAUTHORIZED, "用户未登录");
        }
        Long storeId = sellerDetail.getStoreId();

        // 更新并删除
        freightTemplateService.deleteStoreFreightTemplate(oldFreightTemplateId, newFreightTemplateId, storeId);

        return new Result<>().ok(null, "修改成功");
    }

}
