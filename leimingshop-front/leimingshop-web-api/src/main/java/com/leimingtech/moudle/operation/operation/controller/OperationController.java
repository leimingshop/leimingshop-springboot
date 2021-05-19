/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.moudle.operation.operation.controller;

import cn.hutool.core.bean.BeanUtil;
import com.leimingtech.commons.tools.annotation.LogContext;
import com.leimingtech.commons.tools.annotation.LogOperation;
import com.leimingtech.commons.tools.redis.RedisUtils;
import com.leimingtech.commons.tools.utils.ConvertUtils;
import com.leimingtech.commons.tools.utils.Result;
import com.leimingtech.constants.SysRedisConstants;
import com.leimingtech.dto.area.AreaAllMiniDTO;
import com.leimingtech.dto.area.AreaPcDTO;
import com.leimingtech.dto.area.AreaTreeDTO;
import com.leimingtech.dto.hotword.HotWordDTO;
import com.leimingtech.modules.dto.custom.GoodsClassCustomDTO;
import com.leimingtech.modules.dto.custom.GoodsClassCustomDetailDTO;
import com.leimingtech.modules.enums.custom.ShowClassEnum;
import com.leimingtech.modules.enums.custom.ShowFlagEnum;
import com.leimingtech.modules.service.custom.GoodsClassCustomService;
import com.leimingtech.moudle.operation.operation.code.OperationCode;
import com.leimingtech.moudle.operation.operation.vo.*;
import com.leimingtech.service.area.AreaService;
import com.leimingtech.service.search.HotWordService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 运营模块API
 *
 * @author lixiang
 * @version V1.0
 * @date 2020/5/9 10:09
 **/
@Slf4j
@RestController
@RequestMapping("operation")
@Api(tags = "运营")
public class OperationController {

    private static final String VERSION = "version";

    @Autowired
    private AreaService areaService;

    @Autowired
    private GoodsClassCustomService goodsClassCustomService;

    @Autowired
    private HotWordService hotWordService;

    @Autowired
    private RedisUtils redisUtils;

    @GetMapping("/custom/class/{id}")
    @ApiOperation("根据查询子级数据")
    @LogOperation("根据查询子级数据")
    @LogContext(code = OperationCode.GET_GOODS_CUSTOM_CLASS_CODE, note = OperationCode.GET_GOODS_CUSTOM_CLASS_DESC)
    public Result<GoodsCustomClassResultVo> getCustomClassByParentId(@PathVariable("id") Long id) {
        GoodsCustomClassResultVo resultVo = new GoodsCustomClassResultVo();
        List<GoodsClassCustomDTO> goodsClassCustomDTOList = goodsClassCustomService.selectListByParentId(id, ShowFlagEnum.YES.value(), ShowClassEnum.SHOW_CLASS_TYPE_PC.value());
        List<GoodsClassCustomVo> goodsClassCustomVos = ConvertUtils.sourceToTarget(goodsClassCustomDTOList, GoodsClassCustomVo.class);
        resultVo.setGoodsClassCustomVos(goodsClassCustomVos);

        GoodsClassCurrentCustomVo goodsClassCurrentCustomVo = this.settingCurrentCustomVo(id, null);
        resultVo.setCurrentCustomVo(goodsClassCurrentCustomVo);
        return new Result<GoodsCustomClassResultVo>().ok(resultVo, "查询成功");
    }


    private GoodsClassCurrentCustomVo settingCurrentCustomVo(Long id, GoodsClassCurrentCustomVo currentCustomVo) {
        GoodsClassCustomDetailDTO goodsClassCustomDetailDTO = goodsClassCustomService.selectDetailById(id);
        GoodsClassCurrentCustomVo goodsClassCurrentCustomVo = ConvertUtils.sourceToTarget(goodsClassCustomDetailDTO, GoodsClassCurrentCustomVo.class);
        goodsClassCurrentCustomVo.setChildren(currentCustomVo);
        if (!BeanUtil.isEmpty(goodsClassCurrentCustomVo) && goodsClassCurrentCustomVo.getGcParentId() != 0) {
            GoodsClassCurrentCustomVo goodsClassCurrentCustomVo1 = this.settingCurrentCustomVo(goodsClassCurrentCustomVo.getGcParentId(), goodsClassCurrentCustomVo);
            return goodsClassCurrentCustomVo1;
        }
        return goodsClassCurrentCustomVo;
    }

    @GetMapping("/area/parent/{id}")
    @ApiOperation("根据父id查询地区（ps：一级地区为0）")
    @LogOperation("根据父id查询地区")
    @LogContext(code = OperationCode.AREA_PARENT_CODE, note = OperationCode.AREA_PARENT_DESC)
    public Result<List<AreaVo>> findAreasByParentId(@PathVariable("id") Long parentId) {

        List<AreaPcDTO> areasList = areaService.getCurrentAndChildren(parentId);
        List<AreaVo> areaVos = ConvertUtils.sourceToTarget(areasList, AreaVo.class);
        return new Result<List<AreaVo>>().ok(areaVos, "查询成功");
    }

    @GetMapping("/area/tree")
    @ApiOperation("查询所有地区信息（返回树结构）")
    public Result<List<AreaTreeVo>> treeAreaList() {
        Object o = redisUtils.get(SysRedisConstants.AREA_TREE);
        List<AreaTreeVo> areaTreeVos = new ArrayList<AreaTreeVo>();
        if (null == o) {
            List<AreaTreeDTO> areasList = areaService.getAreasTree();
            areaTreeVos = ConvertUtils.sourceToTarget(areasList, AreaTreeVo.class);
        } else {
            areaTreeVos = (List<AreaTreeVo>) o;
        }
        return new Result<List<AreaTreeVo>>().ok(areaTreeVos, "查询成功");
    }

    @GetMapping("mini/all")
    @ApiOperation("查询压缩地址信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "version", value = "version", paramType = "query", dataType = "int")
    })
    public Result<AreaAllMiniVo> getMini(@ApiIgnore @RequestParam Map<String, Object> params) {
        String version = "";
        if (null != params.get(VERSION)) {
            version = params.get(VERSION).toString();
        }
        AreaAllMiniDTO data = areaService.findAreaMiniAll(version);
        AreaAllMiniVo areaAllMiniVo = ConvertUtils.sourceToTarget(data, AreaAllMiniVo.class);
        return new Result<AreaAllMiniVo>().ok(areaAllMiniVo, "查询压缩版地址信息成功");
    }


    @GetMapping("/hot/list")
    @ApiOperation("热词列表展示")
    public Result<List<HotWordDTO>> findHotList() {
        List<HotWordDTO> hotWordDTOList = hotWordService.findDataRedis();
        return new Result<List<HotWordDTO>>().ok(hotWordDTOList);
    }
}
