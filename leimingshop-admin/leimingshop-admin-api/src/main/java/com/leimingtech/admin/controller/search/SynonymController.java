/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.admin.controller.search;

import com.leimingtech.commons.tools.constant.Constant;
import com.leimingtech.commons.tools.page.PageData;
import com.leimingtech.commons.tools.utils.HttpContextUtils;
import com.leimingtech.commons.tools.utils.Result;
import com.leimingtech.commons.tools.validator.AssertUtils;
import com.leimingtech.commons.tools.validator.ValidatorUtils;
import com.leimingtech.commons.tools.validator.group.AddGroup;
import com.leimingtech.commons.tools.validator.group.DefaultGroup;
import com.leimingtech.commons.tools.validator.group.UpdateGroup;
import com.leimingtech.dto.synonym.SynonymDTO;
import com.leimingtech.dto.synonym.SynonymSaveDTO;
import com.leimingtech.service.search.SynonymService;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.List;
import java.util.Map;


/**
 * 同义词Controller
 *
 * @author lixiangx@leimingtech.com
 * @date 2019/12/10 11:01
 **/
@Slf4j
@RestController
@RequestMapping("synonym")
@Api(tags = "同义词管理 ")
public class SynonymController {

    @Autowired
    private SynonymService synonymService;

    /**
     * 分页查询同义词列表
     *
     * @param params
     * @return
     */
    @GetMapping("page")
    @ApiOperation("分页")
    @ApiImplicitParams({
            @ApiImplicitParam(name = Constant.PAGE, value = "当前页码，从1开始", paramType = "query", required = true, dataType = "int"),
            @ApiImplicitParam(name = Constant.LIMIT, value = "每页显示记录数", paramType = "query", required = true, dataType = "int"),
            @ApiImplicitParam(name = "name", value = "同义词", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = Constant.ORDER_FIELD, value = "排序字段", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = Constant.ORDER, value = "排序方式，可选值(asc、desc)", paramType = "query", dataType = "String")
    })
    public Result<PageData<SynonymDTO>> page(@ApiIgnore @RequestParam Map<String, Object> params) {
        PageData<SynonymDTO> page = synonymService.page(params);

        return new Result<PageData<SynonymDTO>>().ok(page);
    }

    /**
     * 根据id获取同义词
     *
     * @param id
     * @return
     */
    @GetMapping("{id}")
    @ApiOperation("信息")
    public Result<SynonymDTO> get(@PathVariable("id") Long id) {
        SynonymDTO SynonymDTO = synonymService.get(id);

        return new Result<SynonymDTO>().ok(SynonymDTO);
    }

    /**
     * 保存同义词
     *
     * @param dto
     * @return
     */
    @PostMapping
    @ApiOperation("保存")
    public Result save(@RequestBody SynonymSaveDTO dto) {
        //效验数据
        ValidatorUtils.validateEntity(dto, AddGroup.class, DefaultGroup.class);

        synonymService.save(dto);

        return new Result().ok(null, "保存成功");
    }


    /**
     * 修改同义词
     *
     * @param dto
     * @return
     */
    @PutMapping
    @ApiOperation("修改")
    public Result update(@RequestBody SynonymSaveDTO dto) {
        //效验数据
        ValidatorUtils.validateEntity(dto, UpdateGroup.class, DefaultGroup.class);

        synonymService.update(dto);

        return new Result().ok(null, "修改成功");
//        return new Result();
    }

    @PutMapping("update/batch")
    @ApiModelProperty("批量修改")
    public Result updateBatch(@RequestBody List<SynonymSaveDTO> synonymSaveDTOList) {
        for (SynonymSaveDTO synonymSaveDTO : synonymSaveDTOList) {
            ValidatorUtils.validateEntity(synonymSaveDTO, UpdateGroup.class, DefaultGroup.class);
        }
        synonymService.updateBatch(synonymSaveDTOList);

        return new Result().ok(null, "修改成功");
    }

    @PutMapping("state")
    @ApiOperation("修改同义词状态")
    public Result updateStatus(@RequestParam("state") Integer state, @RequestParam("id") Long id) {
        int count = synonymService.updateStatus(state, id);
        if (count > 0) {
            return new Result<>().ok(null, "修改状态成功");
        } else {
            return new Result().error("修改状态失败");
        }
    }

    /**
     * 删除统一词
     *
     * @param ids
     * @return
     */
    @DeleteMapping
    @ApiOperation("删除")
    public Result delete(@RequestBody Long[] ids) {
        //效验数据
        AssertUtils.isArrayEmpty(ids, "id");

        synonymService.delete(ids);

        return new Result().ok(null, "删除成功");
    }

    /**
     * 获取同义词库数据
     *
     * @return 同义词库配置
     * @date 2019/12/18 15:23
     * @author lixiangx@leimingtech.com
     **/
    @GetMapping("es")
    public String getSynonymToEs() {
        HttpServletResponse response = HttpContextUtils.getHttpServletResponse();
        StringBuilder builder = new StringBuilder();
        try {
            List<SynonymDTO> synonymDTOList = synonymService.listForEs();
            if (!CollectionUtils.isEmpty(synonymDTOList)) {
                Date updateDate = synonymDTOList.get(0).getUpdateDate();
                response.setHeader("Last-Modified", String.valueOf(updateDate.getTime()));
                response.setHeader("Etag", String.valueOf(updateDate.getTime()));
                for (SynonymDTO synonymDTO : synonymDTOList) {
                    builder.append(StringUtils.trim(synonymDTO.getName()));
                    builder.append("\n");
                }
            } else {
                // 需要设置一组默认词库
                long currentTimeMillis = System.currentTimeMillis();
                response.setHeader("Last-Modified", String.valueOf(currentTimeMillis));
                response.setHeader("Etag", String.valueOf(currentTimeMillis));
                builder.append("苹果,apple\n");
            }

        } catch (Exception e) {
            log.info("获取同义词词库失败,报错信息为:{}", e);
        }
        return builder.toString();
    }
}
