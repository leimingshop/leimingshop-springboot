/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.moudle.goods.goods.controller;

import com.leimingtech.commons.tools.annotation.LogContext;
import com.leimingtech.commons.tools.constant.Constant;
import com.leimingtech.commons.tools.utils.ConvertUtils;
import com.leimingtech.commons.tools.utils.Result;
import com.leimingtech.modules.dto.evaluate.EvaluateGoodsDTO;
import com.leimingtech.modules.dto.evaluate.H5EvaluateGoodsDTO;
import com.leimingtech.modules.dto.goods.GoodsDTO;
import com.leimingtech.modules.enums.evaluate.EvaluateErrorCodeEnum;
import com.leimingtech.modules.service.evaluate.EvaluateGoodsService;
import com.leimingtech.modules.service.favorites.FavoritesService;
import com.leimingtech.modules.service.goods.GoodsService;
import com.leimingtech.modules.service.goods.spec.GoodsSpecService;
import com.leimingtech.moudle.goods.goods.code.PcGoodsCode;
import com.leimingtech.moudle.goods.goods.vo.EvaluateGoodsVO;
import com.leimingtech.moudle.goods.goods.vo.EvaluateListVO;
import com.leimingtech.moudle.goods.goods.vo.GoodsVO;
import com.leimingtech.moudle.member.favorities.code.PcFavoriteCode;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.List;
import java.util.Map;

/**
 * 商品模块API
 *
 * @author lixiang
 * @version V1.0
 * @date 2020/5/9 10:09
 **/
@Slf4j
@RestController
@RequestMapping("goods")
@Api(tags = "商品")
public class GoodsController {

    private static final String MAP_PARAMS_GOODS_ID_STR = "goodsId";

    @Autowired
    private GoodsService goodsService;

    @Autowired
    private EvaluateGoodsService evaluateGoodsService;
    @Autowired
    private GoodsSpecService goodsSpecService;

    @Autowired
    private FavoritesService favoritesService;

    /**
     * 店铺商品推荐
     *
     * @return
     */
    @GetMapping("recommend")
    @ApiOperation("店铺商品推荐")
    @LogContext(code = PcGoodsCode.STORE_GOODS_RECOMMEND_CODE, note = PcGoodsCode.STORE_GOODS_RECOMMEND_DESC)
    public Result<List<GoodsVO>> storeGoodsRecommend(@RequestParam("storeId") Long storeId) {
        List<GoodsDTO> goodsDTOList = goodsService.storeGoodsRecommend(storeId, null, 10);
        return new Result<List<GoodsVO>>().ok(ConvertUtils.sourceToTarget(goodsDTOList, GoodsVO.class));
    }

    /**
     * 店铺热卖商品
     *
     * @return
     */
    @GetMapping("hot")
    @ApiOperation("店铺热卖商品")
    @LogContext(code = PcGoodsCode.STORE_HOT_GOODS_CODE, note = PcGoodsCode.STORE_HOT_GOODS_DESC)
    public Result<List<GoodsVO>> hotGoods(@RequestParam("storeId") Long storeId) {
        List<GoodsDTO> goodsDTOList = goodsService.storeGoodsRecommend(storeId, null, 5);
        return new Result<List<GoodsVO>>().ok(ConvertUtils.sourceToTarget(goodsDTOList, GoodsVO.class));
    }

    /**
     * 同类推荐
     *
     * @return
     */
    @GetMapping("like")
    @ApiOperation("同类推荐")
    @LogContext(code = PcGoodsCode.STORE_LIKE_GOODS_CODE, note = PcGoodsCode.STORE_LIKE_GOODS_DESC)
    public Result<List<GoodsVO>> likeGoods(@RequestParam("storeId") Long storeId,
                                           @RequestParam(value = "storeGoodsRecommend", required = false) Long storeGoodsRecommend) {
        List<GoodsDTO> goodsDTOList = goodsService.storeGoodsRecommend(storeId, storeGoodsRecommend, 5);
        return new Result<List<GoodsVO>>().ok(ConvertUtils.sourceToTarget(goodsDTOList, GoodsVO.class));
    }

    /**
     * 分页查询当前商品评价信息
     *
     * @param params 分页参数
     */
    @GetMapping("page")
    @ApiOperation("分页查询当前商品评价信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = Constant.PAGE, value = "当前页码，从1开始", paramType = "query", required = true, dataType = "int"),
            @ApiImplicitParam(name = Constant.LIMIT, value = "每页显示记录数", paramType = "query", required = true, dataType = "int"),
            @ApiImplicitParam(name = Constant.ORDER_FIELD, value = "排序字段", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "specId", value = "商品SKUID", paramType = "query", dataType = "Long"),
            @ApiImplicitParam(name = "goodsId", value = "商品SPUID", paramType = "query", dataType = "Long"),
            @ApiImplicitParam(name = "type", value = "评价类型 1 全部 2 带图评价 3 好评 4 中评 5 差评", paramType = "query", dataType = "integer"),
            @ApiImplicitParam(name = Constant.ORDER, value = "排序方式，可选值(asc、desc)", paramType = "query", dataType = "String")
    })
    @LogContext(code = PcGoodsCode.GOODS_DETAIL_EVALUATE_CODE, note = PcGoodsCode.GOODS_DETAIL_EVALUATE_DESC)
    public Result<EvaluateListVO> page(@ApiIgnore @RequestParam Map<String, Object> params) {
        if (params.get(MAP_PARAMS_GOODS_ID_STR) == null) {
            Long goodsId = goodsSpecService.findGoodsIdBySpecId(Long.parseLong(params.get("specId").toString()));
            if (goodsId != null) {
                params.put(MAP_PARAMS_GOODS_ID_STR, String.valueOf(goodsId));
            }
        }
        H5EvaluateGoodsDTO h5EvaluateGoodsDTO = evaluateGoodsService.pcEvaluatePage(params);

        return new Result<EvaluateListVO>().ok(ConvertUtils.sourceToTarget(h5EvaluateGoodsDTO, EvaluateListVO.class));
    }

    /**
     * 根据评价ID获取评价详情
     *
     * @param id
     */
    @GetMapping("{id}")
    @ApiOperation("根据评价ID获取评价详情")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "评价主键ID", paramType = "query", dataType = "Long")
    })
    @LogContext(code = PcGoodsCode.GOODS_DETAIL_EVALUATE_INFO_CODE, note = PcGoodsCode.GOODS_DETAIL_EVALUATE_INFO_DESC)
    public Result<EvaluateGoodsVO> findById(@PathVariable("id") Long id) {

        EvaluateGoodsDTO data = evaluateGoodsService.findById(id, null);
        if (data == null) {
            return new Result<EvaluateGoodsVO>().error(EvaluateErrorCodeEnum.NOT_BLANK.value(), "评论不存在");
        }
        return new Result<EvaluateGoodsVO>().ok(ConvertUtils.sourceToTarget(data, EvaluateGoodsVO.class));
    }

    /**
     * 查询商品收藏数量
     *
     * @param goodsId
     * @param
     * @return
     */
    @GetMapping("fav/num")
    @ApiOperation("查询商品收藏数量")
    @LogContext(code = PcFavoriteCode.DELETE_GOODS_FAVORITE_CODE, note = PcFavoriteCode.DELETE_GOODS_FAVORITE_DESC)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "goodsId", value = "商品ID", paramType = "query", dataType = "long"),
    })
    public Result goodsFavNum(@RequestParam(value = "goodsId") Long goodsId) {

        Integer integer = favoritesService.goodsTotalFav(goodsId);

        return new Result<>().ok(integer);
    }

}
