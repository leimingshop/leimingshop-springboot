/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.admin.controller.area;

import com.leimingtech.commons.tools.annotation.LogOperation;
import com.leimingtech.commons.tools.constant.Constant;
import com.leimingtech.commons.tools.page.PageData;
import com.leimingtech.commons.tools.utils.Result;
import com.leimingtech.commons.tools.validator.ValidatorUtils;
import com.leimingtech.commons.tools.validator.group.AddGroup;
import com.leimingtech.commons.tools.validator.group.UpdateGroup;
import com.leimingtech.dto.area.*;
import com.leimingtech.service.area.AreaService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.io.*;
import java.util.List;
import java.util.Map;


/**
 * @Author: weixianchun
 * @Description: 地区管理controller
 * @Date :2019/5/28 8:56
 * @Version V1.0
 **/
@RestController
@RequestMapping("area")
@Api(tags = "地区管理")
public class AreaController {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private AreaService areaService;

    /**
     * @Author: weixianchun
     * @Description: 根据父id查询地区
     * @Date :2019/5/27 17:07
     * @Param parentId
     * @Version V1.0
     **/
    @GetMapping("parent/{id}")
    @ApiOperation("根据父id查询地区")
    @LogOperation("根据父id查询地区")
    public Result findAreasByParentId(@PathVariable("id") Long parentId) {
        List<AreaDTO> areasList = areaService.getAreasByParentId(parentId);
        return new Result<List<AreaDTO>>().ok(areasList, "查询成功");
    }


    /**
     * @Author: LX 17839193044@162.com
     * @Description: 查询全部一级地址信息
     * @Date: 2019/5/28 16:19
     * @Version: V1.0
     */
    @GetMapping("first/list")
    @ApiOperation("查询全部一级地区")
    @LogOperation("查询全部一级地区")
    public Result findFirstList() {
        List<AreaDTO> areasList = areaService.findFirstList();
        return new Result<List<AreaDTO>>().ok(areasList, "查询成功");
    }

    @GetMapping("create/areajs")
    @ApiOperation("查询所有地区（导出js文件）")
    @LogOperation("查询所有地区（导出js文件）")
    public void createAreasForJs() throws IOException {
        String fullPath = "C:/js/area.js";

        // 生成json格式文件
        Writer write = null;
        try {
            // 保证创建一个新文件
            File file = new File(fullPath);
            // 如果父目录不存在，创建父目录
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }
            // 如果已存在,删除旧文件
            if (file.exists()) {
                boolean deleteFile = file.delete();
                if (!deleteFile) {
                    logger.error("文件删除失败");
                }
            }
            boolean newFile = file.createNewFile();
            if (!newFile) {
                logger.error("创建新文件失败");
            }

            // 获得输出对象字符串
            StringBuilder jsContent = new StringBuilder();
            jsContent.append("// 一级地区数据\n");
            jsContent.append("export const arr1 = ");
            String arr1 = areaService.getAreaByAeraDeep(1);
            jsContent.append(arr1).append("\n");
            jsContent.append("// 二级地区数据\n");
            jsContent.append("export const arr2 = ");
            String arr2 = areaService.getAreaByAeraDeep(2);
            jsContent.append(arr2).append("\n");
            jsContent.append("// 三级地区数据\n");
            jsContent.append("export const arr3 = ");
            String arr3 = areaService.getAreaByAeraDeep(3);
            jsContent.append(arr3).append("\n");
            jsContent.append("// 四级地区数据\n");
            jsContent.append("export const arr4 = ");
            String arr4 = areaService.getAreaByAeraDeep(4);
            jsContent.append(arr4).append("\n");


            // 将格式化后的字符串写入文件
            write = new OutputStreamWriter(new FileOutputStream(file), "UTF-8");
            write.write(jsContent.toString());
            write.flush();
        } catch (Exception e) {
            logger.error("错误信息为" + e);
        } finally {
            write.close();
        }
    }

    /**
     * 修改地区
     *
     * @return
     */
    @PutMapping("update")
    @ApiOperation("修改地区")
    @LogOperation("修改地区")
    public Result updateArea(@RequestBody AreaUpdateDTO areaUpdateDTO) {
        //效验数据
        ValidatorUtils.validateEntity(areaUpdateDTO, UpdateGroup.class);
        areaService.update(areaUpdateDTO);
        return new Result().ok(null, "修改成功");
    }

    /**
     * 删除地区
     *
     * @param id
     * @return
     */
    @DeleteMapping("{id}")
    @ApiOperation("删除地区")
    @LogOperation("删除地区")
    public Result deletaAreaByIds(@PathVariable("id") Long id) {

        areaService.delete(id);
        return new Result().ok(null, "删除成功");
    }

    /**
     * 新增地区
     *
     * @param areaAddDTO
     * @return
     */
    @PostMapping("save")
    @ApiOperation("新增地区")
    @LogOperation("新增地区")
    public Result saveArea(@RequestBody AreaAddDTO areaAddDTO) {
        //效验数据
        ValidatorUtils.validateEntity(areaAddDTO, AddGroup.class);
        areaService.save(areaAddDTO);
        return new Result().ok(null, "新增成功");
    }

    /**
     * 地区详情
     *
     * @param id
     * @return
     */
    @GetMapping("info")
    @ApiOperation("地区详情")
    @LogOperation("地区详情")
    public Result<AreaDTO> info(@RequestParam("id") Long id) {

        AreaDTO info = areaService.info(id);
        return new Result<AreaDTO>().ok(info);
    }

    /**
     * 分页查询地区
     *
     * @param params
     * @return
     */
    @GetMapping("page")
    @ApiOperation("分页查询地区")
    @LogOperation("根据父id查询地区")
    @ApiImplicitParams({
            @ApiImplicitParam(name = Constant.PAGE, value = "当前页码，从1开始", paramType = "query", required = true, dataType = "int"),
            @ApiImplicitParam(name = Constant.LIMIT, value = "每页显示记录数", paramType = "query", required = true, dataType = "int"),
            @ApiImplicitParam(name = Constant.ORDER_FIELD, value = "排序字段", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "areaName", value = "地区名称", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "parentId", value = "地区父ID", paramType = "query", dataType = "int", defaultValue = "0")
    })
    public Result<PageData<AreaPageDTO>> page(@ApiIgnore @RequestParam Map params) {

        PageData<AreaPageDTO> page = areaService.page(params);

        return new Result<PageData<AreaPageDTO>>().ok(page, "查询成功");
    }

    /**
     * 查询所有地区
     *
     * @return
     */
    @GetMapping("tree")
    @ApiOperation("查询所有地区")
    @LogOperation("查询所有地区")
    public Result<List<AreaTreeDTO>> treeList() {

        List<AreaTreeDTO> pageData = areaService.treeList();

        return new Result<List<AreaTreeDTO>>().ok(pageData, "查询成功");
    }

    /**
     * 查询父ID信息
     *
     * @return
     */
    @GetMapping("parent")
    @ApiOperation("查询父ID信息")
    @LogOperation("查询父ID信息")
    public Result<AreaDTO> parentInfo(@RequestParam("parentId") Long parentId) {

        AreaDTO areaDTO = areaService.parentArea(parentId);

        return new Result<AreaDTO>().ok(areaDTO, "查询成功");
    }

    /**
     * 查询所有大区
     *
     * @return
     */
    @GetMapping("region")
    @ApiOperation("查询所有大区")
    @LogOperation("查询所有大区")
    public Result<List<RegionDTO>> regionList() {

        List<RegionDTO> list = areaService.regionList();

        return new Result<List<RegionDTO>>().ok(list, "查询成功");
    }

}
