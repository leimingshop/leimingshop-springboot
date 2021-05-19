/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.moudle.goods.goodsclass.controller;

import cn.hutool.core.bean.BeanUtil;
import com.leimingtech.commons.tools.annotation.LogContext;
import com.leimingtech.commons.tools.utils.ConvertUtils;
import com.leimingtech.commons.tools.utils.Result;
import com.leimingtech.modules.dto.goodsclass.GoodsClassDTO;
import com.leimingtech.modules.service.goodsclass.GoodsClassService;
import com.leimingtech.moudle.goods.goodsclass.code.GoodsClassCode;
import com.leimingtech.moudle.goods.goodsclass.vo.GoodsClassResultVo;
import com.leimingtech.moudle.goods.goodsclass.vo.GoodsClassVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * 商品模块API
 *
 * @author lixiang
 * @version V1.0
 * @date 2020/5/9 10:09
 **/
@Slf4j
@RestController
@RequestMapping("goodsclass")
@Api(tags = "商品分类")
public class GoodsClassController {

    @Autowired
    private GoodsClassService goodsClassService;


    @GetMapping("children/{id}")
    @ApiOperation("根据父级分类ID查询子级数据")
    @LogContext(code = GoodsClassCode.GOODS_CLASS_CODE, note = GoodsClassCode.GOODS_CLASS_DESC)
    public Result<GoodsClassResultVo> getCustomClassByParentId(@PathVariable("id") Long id) {
        GoodsClassResultVo resultVo = new GoodsClassResultVo();
        List<GoodsClassDTO> goodsClassDTOList = goodsClassService.selectClassByParentId(id);
        //查询出来的商品列表为0，直接返回  todo 首页
        if (goodsClassDTOList.size() == 0) {
            return new Result<GoodsClassResultVo>().ok(resultVo, "该分类下未查到商品信息");
        }
        List<GoodsClassVo> goodsClassVos = Optional.ofNullable(ConvertUtils.sourceToTarget(goodsClassDTOList, GoodsClassVo.class)).orElse(new ArrayList<GoodsClassVo>());
        resultVo.setGoodsClassVos(goodsClassVos);
        GoodsClassVo classVo = this.settingCurrentClassVo(id, null);
        resultVo.setCurrentClassVo(classVo);
        return new Result<GoodsClassResultVo>().ok(resultVo, "查询成功");
    }

    private GoodsClassVo settingCurrentClassVo(Long id, GoodsClassVo currentClassVo) {
        GoodsClassDTO goodsClassDTO = goodsClassService.findById(id);
        GoodsClassVo goodsClassVo = ConvertUtils.sourceToTarget(goodsClassDTO, GoodsClassVo.class);
        goodsClassVo.setChildren(currentClassVo);
        if (!BeanUtil.isEmpty(goodsClassVo) && goodsClassVo.getGcParentId() != 0) {
            GoodsClassVo goodsClassVo1 = this.settingCurrentClassVo(goodsClassVo.getGcParentId(), goodsClassVo);
            return goodsClassVo1;
        }
        return goodsClassVo;
    }
}
