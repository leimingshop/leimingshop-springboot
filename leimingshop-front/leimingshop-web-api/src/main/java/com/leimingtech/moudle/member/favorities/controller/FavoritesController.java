/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.moudle.member.favorities.controller;

import com.leimingtech.commons.tools.annotation.LogContext;
import com.leimingtech.commons.tools.constant.Constant;
import com.leimingtech.commons.tools.exception.CustomException;
import com.leimingtech.commons.tools.exception.ErrorCode;
import com.leimingtech.commons.tools.page.PageData;
import com.leimingtech.commons.tools.utils.ConvertUtils;
import com.leimingtech.commons.tools.utils.Result;
import com.leimingtech.commons.tools.validator.ValidatorUtils;
import com.leimingtech.commons.tools.validator.group.AddGroup;
import com.leimingtech.modules.dto.favorites.FavoritesDTO;
import com.leimingtech.modules.dto.favorites.FavoritesPageDTO;
import com.leimingtech.modules.service.favorites.FavoritesService;
import com.leimingtech.modules.vo.FavResulltVO;
import com.leimingtech.moudle.member.favorities.code.PcFavoriteCode;
import com.leimingtech.moudle.member.favorities.vo.favorites.FavoritesPageVO;
import com.leimingtech.security.SecurityCurrentUser;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.HashMap;
import java.util.Map;


/**
 * 用户收藏
 *
 * @author chengqian
 * @email
 * @since 1.0.0 2019-05-15
 */
@RestController
@RequestMapping("member/goods/favorites")
@Api(tags = "用户商品收藏")
public class FavoritesController {

    @Autowired
    private FavoritesService favoritesService;

    @GetMapping("page")
    @ApiOperation("获取用户收藏商品列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = Constant.PAGE, value = "当前页码，从1开始", paramType = "query", required = true, dataType = "int"),
            @ApiImplicitParam(name = Constant.LIMIT, value = "每页显示记录数", paramType = "query", required = true, dataType = "int"),
            @ApiImplicitParam(name = Constant.ORDER_FIELD, value = "排序字段", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = Constant.ORDER, value = "排序方式，可选值(asc、desc)", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "goodsName", value = "商品名称", paramType = "query", dataType = "String")
    })
    @LogContext(code = PcFavoriteCode.GOODS_FAVORITE_PAGE_CODE, note = PcFavoriteCode.GOODS_FAVORITE_PAGE_DESC)
    public Result<PageData<FavoritesPageVO>> page(@ApiIgnore @RequestParam Map<String, Object> params) {
        // 获取memberId
        Long memberId = null;
        try {
            memberId = SecurityCurrentUser.getUserDetail().getId();
        } catch (CustomException e) {
            return new Result<PageData<FavoritesPageVO>>().error(ErrorCode.UNAUTHORIZED, "请先登录");
        }
        params.put("memberId", memberId.toString());

        PageData<FavoritesPageDTO> page = favoritesService.favPage(params);
        PageData<FavoritesPageVO> date = new PageData<>();
        date.setTotal(page.getTotal());
        date.setList(ConvertUtils.sourceToTarget(page.getList(), FavoritesPageVO.class));
        return new Result<PageData<FavoritesPageVO>>().ok(date);
    }

    /**
     * 商品收藏
     *
     * @param dto
     * @return
     */
    @PostMapping
    @ApiOperation("收藏商品")
    @LogContext(code = PcFavoriteCode.SAVE_GOODS_FAVORITE_CODE, note = PcFavoriteCode.SAVE_GOODS_FAVORITE_DESC)
    public Map<String, Object> save(@RequestBody FavoritesDTO dto) {
        Map<String, Object> resultMap = new HashMap<>(2);
        //效验数据
        ValidatorUtils.validateEntity(dto, AddGroup.class);
        Long memberId = null;
        try {
            // 获取用户信息
            memberId = SecurityCurrentUser.getUserDetail().getId();
        } catch (CustomException e) {
            resultMap.put("code", ErrorCode.UNAUTHORIZED);
            resultMap.put("msg", "请先登录");
            return resultMap;
        }
        dto.setMemberId(memberId);
        dto.setMemberName(SecurityCurrentUser.getUserDetail().getMemberName());
        resultMap = favoritesService.saveGoodsFav(dto);

        return resultMap;
    }

    /**
     * 取消收藏
     *
     * @param ids
     * @return
     */
    @DeleteMapping
    @ApiOperation("取消商品收藏")
    @LogContext(code = PcFavoriteCode.DELETE_GOODS_FAVORITE_CODE, note = PcFavoriteCode.DELETE_GOODS_FAVORITE_DESC)
    public Result delete(@RequestBody Long[] ids) {
        Long memberId = null;
        try {
            // 获取用户信息
            memberId = SecurityCurrentUser.getUserDetail().getId();
        } catch (CustomException e) {
            return new Result().error(ErrorCode.UNAUTHORIZED, "请先登录");
        }
        favoritesService.delete(ids, memberId);

        return new Result().ok("", "取消成功");
    }

    /**
     * 查询用户是否收藏商品和店铺
     *
     * @param specId  规格id
     * @param storeId 店铺ID
     * @return
     */
    @GetMapping("isfavorites")
    @ApiOperation("查询用户是否收藏商品和店铺")
    @LogContext(code = PcFavoriteCode.DELETE_GOODS_FAVORITE_CODE, note = PcFavoriteCode.DELETE_GOODS_FAVORITE_DESC)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "specId", value = "商品规格ID", paramType = "query", required = false, dataType = "long"),
            @ApiImplicitParam(name = "storeId", value = "店铺ID", paramType = "query", required = false, dataType = "long"),
    })
    public Result<FavResulltVO> isFavorites(@RequestParam(value = "specId", required = false) Long specId,
                                            @RequestParam(value = "storeId", required = false) Long storeId) {
        Long memberId = null;
        try {
            // 获取用户信息
            memberId = SecurityCurrentUser.getUserDetail().getId();
        } catch (CustomException e) {
            return new Result<FavResulltVO>().ok(null, "请先登录");
        }
        FavResulltVO favGoodsStore = favoritesService.isFavGoodsStore(memberId, storeId, specId);

        return new Result<FavResulltVO>().ok(favGoodsStore);
    }
}
