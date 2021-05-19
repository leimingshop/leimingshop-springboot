/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.admin.controller.brand;

import com.leimingtech.commons.tools.annotation.LogContext;
import com.leimingtech.commons.tools.annotation.LogOperation;
import com.leimingtech.commons.tools.constant.Constant;
import com.leimingtech.commons.tools.page.PageData;
import com.leimingtech.commons.tools.utils.ConvertUtils;
import com.leimingtech.commons.tools.utils.EasyExcelUtils;
import com.leimingtech.commons.tools.utils.Result;
import com.leimingtech.commons.tools.validator.AssertUtils;
import com.leimingtech.commons.tools.validator.ValidatorUtils;
import com.leimingtech.commons.tools.validator.group.AddGroup;
import com.leimingtech.commons.tools.validator.group.DefaultGroup;
import com.leimingtech.commons.tools.validator.group.UpdateGroup;
import com.leimingtech.modules.dto.brand.BrandDTO;
import com.leimingtech.modules.dto.brand.BrandExcelDTO;
import com.leimingtech.modules.dto.brand.BrandSaveDTO;
import com.leimingtech.modules.enums.brand.BrandErrorCodeEnum;
import com.leimingtech.modules.service.brand.BrandService;
import com.leimingtech.modules.statuscode.GoodsBrandStatusCode;
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

    /**
     * @Author: weixianchun
     * @Description: 查询所有品牌信息
     * @Date :2019/5/20 13:56
     * @Version V1.0
     **/
    @GetMapping("list")
    @ApiOperation("查询所有品牌信息")
    @LogContext(note = GoodsBrandStatusCode.BRAND_LIST_SUCCESS_MESSAGE, code = GoodsBrandStatusCode.BRAND_LIST_SUCCESS_CODE)
    @ApiImplicitParam(name = "brandName", value = "品牌名称", paramType = "query", dataType = "String")
    public Result<List<BrandDTO>> list(@ApiIgnore @RequestParam Map<String, Object> params) {

        List<BrandDTO> list = brandService.list(params);

        return new Result<List<BrandDTO>>().ok(list, "查询成功");
    }

    /**
     * @Author: weixianchun
     * @Description: 根据品牌id获取信息
     * @Date :2019/5/20 13:56
     * @Param :[id] 品牌id
     * @Version V1.0
     **/
    @GetMapping("{id}")
    @ApiOperation("根据品牌id获取信息")
    @LogContext(note = GoodsBrandStatusCode.BRAND_BY_ID_SUCCESS_MESSAGE, code = GoodsBrandStatusCode.BRAND_BY_ID_SUCCESS_CODE)
    public Result<BrandDTO> findById(@PathVariable("id") Long id) {

        BrandDTO data = brandService.get(id);

        return new Result<BrandDTO>().ok(data, "查询成功");
    }

    /**
     * @Author: weixianchun
     * @Description: 保存品牌信息
     * @Date :2019/5/20 13:57
     * @Param :[dto] (保存)实体
     * @Version V1.0
     **/
    @PostMapping
    @ApiOperation("保存品牌信息")
    @LogOperation("保存品牌信息")
    @LogContext(note = GoodsBrandStatusCode.BRAND_SAVE_SUCCESS_MESSAGE, code = GoodsBrandStatusCode.BRAND_SAVE_SUCCESS_CODE)
    public Result save(@RequestBody BrandSaveDTO saveDTO) {
        //效验数据
        ValidatorUtils.validateEntity(saveDTO, AddGroup.class, DefaultGroup.class);
//        //将品牌名称,品牌英文名称空格替换为空
//        saveDTO.setBrandNameEn(StringUtils.remove(saveDTO.getBrandNameEn(), " "));
//        saveDTO.setBrandName(StringUtils.remove(saveDTO.getBrandName(), " "));

        BrandDTO brandDTO = ConvertUtils.sourceToTarget(saveDTO, BrandDTO.class);

        Integer count = brandService.selectCountByCondition(brandDTO);

        //查看品牌名称,品牌英文名称在数据库数量(保存时)
        if (count == 0) {
            //获取中文品牌名称转换为拼音,截取后的第一个字母作为首字母
            if (StringUtils.isNotBlank(saveDTO.getBrandNameEn())) {
                saveDTO.setBrandInitials(saveDTO.getBrandNameEn().substring(0, 1).toUpperCase());
            }
            brandService.save(saveDTO);
        } else {
            return new Result().error(BrandErrorCodeEnum.CATEGORY_REPETITION.value(), "品牌名称或品牌英文名称重复");
        }
        return new Result().ok(null, "保存品牌信息成功");
    }

    /**
     * @Author: weixianchun
     * @Description: 修改品牌信息
     * @Date :2019/5/20 13:58
     * @Param :[dto] 实体
     * @Version V1.0
     **/
    @PutMapping
    @ApiOperation("修改品牌信息")
    @LogOperation("修改品牌信息")
    @LogContext(note = GoodsBrandStatusCode.BRAND_UPDATE_MESSAGE, code = GoodsBrandStatusCode.BRAND_UPDATE_CODE)
    public Result update(@RequestBody BrandDTO dto) {

        //效验数据
        ValidatorUtils.validateEntity(dto, UpdateGroup.class, DefaultGroup.class);
        //将品牌名称,品牌英文名称空格替换为空
        String brandNameEn = StringUtils.remove(dto.getBrandNameEn(), " ");
        dto.setBrandNameEn(brandNameEn);
        String brandName = StringUtils.remove(dto.getBrandName(), " ");
        dto.setBrandName(brandName);

        //查看品牌名称,品牌英文名称在数据库数量
        Integer count = brandService.selectCountByCondition(dto);
        if (count == 0) {
            //获取中文品牌名称转换为拼音,截取后的第一个字母作为首字母
            if (StringUtils.isNotBlank(brandNameEn)) {
                dto.setBrandInitials(brandNameEn.substring(0, 1).toUpperCase());
            }
            brandService.update(dto);
        } else {
            //数量大于0,给出提示信息
            return new Result().error(BrandErrorCodeEnum.CATEGORY_REPETITION.value(), "品牌名称或品牌英文名称重复");
        }
        return new Result().ok(null, "修改成功");

    }

    /**
     * @Author: weixianchun
     * @Description: 删除品牌信息
     * @Date :2019/5/20 13:58
     * @Param :[ids] 品牌id数组
     * @Version V1.0
     **/
    @DeleteMapping
    @ApiOperation("删除品牌信息")
    @LogOperation("删除品牌信息")
    @LogContext(note = GoodsBrandStatusCode.BRAND_DELETE_MESSAGE, code = GoodsBrandStatusCode.BRAND_DELETE_CODE)
    public Result delete(@RequestBody Long[] ids) {

        //效验数据,如果数组为空提示id不能为空
        AssertUtils.isArrayEmpty(ids, "id不能为空");

        List<String> list = brandService.deleteByBrandId(ids);
        if (CollectionUtils.isEmpty(list)) {
            return new Result().ok(null, "删除品牌成功");
        }
        //字符串拼接
        StringBuilder message = new StringBuilder();
        message.append("存在关联商品品牌:");
        for (String str : list) {
            message.append(str).append(",");
        }
        message.append("不可做删除操作");
        return new Result().error(BrandErrorCodeEnum.BRAND_RELATION_GOODS.value(), message.toString());
    }

    /**
     * @Author: weixianchun
     * @Description: 导出品牌信息
     * @Date :2019/5/20 13:59
     * @Param :[params, response]
     * @Version V1.0
     **/
    @GetMapping("export")
    @ApiOperation("导出品牌信息")
    @LogOperation("导出品牌信息")
    @LogContext(note = GoodsBrandStatusCode.BRAND_EXPORT_MESSAGE, code = GoodsBrandStatusCode.BRAND_EXPORT_CODE)
    @ApiImplicitParams({
            @ApiImplicitParam(name = Constant.ORDER_FIELD, value = "排序字段", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = Constant.ORDER, value = "排序方式，可选值(asc、desc)", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "brandInitials", value = "品牌首字母 ", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "brandNameEn", value = "英文名称", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "brandName", value = "品牌名称", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "brandApply", value = "审核状态0申请中，1通过,2未通过", paramType = "query", dataType = "String")
    })
    public void export(@ApiIgnore @RequestParam Map<String, Object> params, HttpServletResponse response) {

        List<BrandExcelDTO> list = brandService.exportList(params);

        try {
            EasyExcelUtils.exportExcelToTarget(response, null, list, BrandExcelDTO.class);
        } catch (Exception e) {
            log.error("错误信息为" + e);
        }
    }

    //TODO: 店铺id  一期不做处理
}
