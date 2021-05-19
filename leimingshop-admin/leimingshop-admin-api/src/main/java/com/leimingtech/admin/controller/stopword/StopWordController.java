/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.admin.controller.stopword;

import com.leimingtech.commons.tools.constant.Constant;
import com.leimingtech.commons.tools.page.PageData;
import com.leimingtech.commons.tools.utils.Result;
import com.leimingtech.commons.tools.validator.ValidatorUtils;
import com.leimingtech.commons.tools.validator.group.AddGroup;
import com.leimingtech.commons.tools.validator.group.UpdateGroup;
import com.leimingtech.dto.stopword.AddStopWordDTO;
import com.leimingtech.dto.stopword.StopWordDTO;
import com.leimingtech.dto.stopword.UpdateStopWordDTO;
import com.leimingtech.modules.enums.evaluate.EvaluateErrorCodeEnum;
import com.leimingtech.service.stopword.StopWordService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import springfox.documentation.annotations.ApiIgnore;

import java.util.Map;


/**
 * 广告禁语管理
 *
 * @author chengqian
 * @since v1.0.0 2019-08-21
 */
@RestController
@RequestMapping("stopword")
@Api(tags = "广告禁语管理 ")
@Slf4j
public class StopWordController {

    @Autowired
    private StopWordService stopWordService;

    /**
     * Description: 分页查询AdvertisingBan
     *
     * @param params
     * @author chengqian
     * @date 2019-08-21
     */
    @GetMapping("page")
    @ApiOperation("分页")
    @ApiImplicitParams({
            @ApiImplicitParam(name = Constant.PAGE, value = "当前页码，从1开始", paramType = "query", required = true, dataType = "int"),
            @ApiImplicitParam(name = Constant.LIMIT, value = "每页显示记录数", paramType = "query", required = true, dataType = "int"),
            @ApiImplicitParam(name = Constant.ORDER_FIELD, value = "排序字段", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = Constant.ORDER, value = "排序方式，可选值(asc、desc)", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "name", value = "广告名", paramType = "query", dataType = "String")
    })
    public Result<PageData<StopWordDTO>> page(@ApiIgnore @RequestParam Map<String, Object> params) {

        PageData<StopWordDTO> page = stopWordService.page(params);

        return new Result<PageData<StopWordDTO>>().ok(page);
    }

    /**
     * Description: 根据id查询AdvertisingBan
     *
     * @param id
     * @author chengqian
     * @date 2019-08-21
     */
    @GetMapping("{id}")
    @ApiOperation("信息")
    public Result<StopWordDTO> fingOne(@PathVariable("id") Long id) {
        StopWordDTO data = stopWordService.get(id);

        return new Result<StopWordDTO>().ok(data);
    }


    /**
     * Description: 添加AdvertisingBan
     *
     * @param dto
     * @return Result
     * @author chengqian
     * @date 2019-08-21
     */
    @PostMapping("add")
    @ApiOperation("保存")
    public Result save(@RequestBody AddStopWordDTO dto) {
        //效验数据
        ValidatorUtils.validateEntity(dto, AddGroup.class);
        stopWordService.save(dto);
        return new Result().ok(null, "保存成功");
    }

    /**
     * Description: 修改AdvertisingBan
     *
     * @param dto
     * @author chengqian
     * @date 2019-08-21
     */
    @PutMapping("update")
    @ApiOperation("修改")
    public Result update(@RequestBody UpdateStopWordDTO dto) {
        //效验数据
        ValidatorUtils.validateEntity(dto, UpdateGroup.class);

        stopWordService.update(dto);

        return new Result().ok(null, "修改成功");
    }

    /**
     * Description: 删除AdvertisingBan
     *
     * @return Result
     * @author chengqian
     * @date 2019-08-21
     */
    @DeleteMapping("{id}")
    @ApiOperation("删除")
    public Result deleteById(@PathVariable("id") Long id) {

        stopWordService.delete(new Long[]{id});
        return new Result().ok(null, "删除成功");
    }

    /**
     * 导入禁用词
     *
     * @return
     */
    @PostMapping(value = "/import")
    @ApiOperation("导入禁用词")
    public Result uploadExcel(@RequestPart("file") MultipartFile file) {


        try {
            stopWordService.importForbiddenInfo(file);
        } catch (Exception e) {
            return new Result().error(EvaluateErrorCodeEnum.UPLOAD_ERROR.hashCode(), "上传失败");
        }

        return new Result().ok(null, "上传成功");
    }

}
