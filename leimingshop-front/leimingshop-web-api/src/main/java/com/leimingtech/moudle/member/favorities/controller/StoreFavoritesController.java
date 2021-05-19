/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.moudle.member.favorities.controller;

import com.leimingtech.commons.tools.annotation.LogContext;
import com.leimingtech.commons.tools.constant.Constant;
import com.leimingtech.commons.tools.exception.CustomException;
import com.leimingtech.commons.tools.page.PageData;
import com.leimingtech.commons.tools.utils.ConvertUtils;
import com.leimingtech.commons.tools.utils.Result;
import com.leimingtech.commons.tools.validator.AssertUtils;
import com.leimingtech.commons.tools.validator.ValidatorUtils;
import com.leimingtech.commons.tools.validator.group.AddGroup;
import com.leimingtech.commons.tools.validator.group.DefaultGroup;
import com.leimingtech.modules.dto.favorites.StoreFavoritesPageDTO;
import com.leimingtech.modules.dto.favorites.StoreFavoritesSaveDTO;
import com.leimingtech.modules.service.favorites.StoreFavoritesService;
import com.leimingtech.moudle.member.favorities.code.PcFavoriteCode;
import com.leimingtech.moudle.member.favorities.vo.favorites.StoreFavoritesPageVO;
import com.leimingtech.security.SecurityCurrentUser;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.Map;


/**
 * 用户店铺收藏
 *
 * @author chengqian
 * @since 1.0.0 2019-05-15
 */
@RestController
@RequestMapping("member/store/favorites")
@Api(tags = "用户店铺收藏")
public class StoreFavoritesController {
    @Autowired
    private StoreFavoritesService storeFavoritesService;

    @GetMapping("page")
    @ApiOperation("获取用户收藏店铺列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = Constant.PAGE, value = "当前页码，从1开始", paramType = "query", required = true, dataType = "int"),
            @ApiImplicitParam(name = Constant.LIMIT, value = "每页显示记录数", paramType = "query", required = true, dataType = "int"),
            @ApiImplicitParam(name = Constant.ORDER_FIELD, value = "排序字段", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = Constant.ORDER, value = "排序方式，可选值(asc、desc)", paramType = "query", dataType = "String")
    })
    @LogContext(code = PcFavoriteCode.STORE_FAVORITE_PAGE_CODE, note = PcFavoriteCode.STORE_FAVORITE_PAGE_DESC)
    public Result<PageData<StoreFavoritesPageVO>> page(@ApiIgnore @RequestParam Map<String, Object> params) {
        Long userId = null;
        try {
            // 获取用户信息
            userId = SecurityCurrentUser.getUserDetail().getId();
        } catch (CustomException e) {
            return new Result().error("请先登录");
        }
        params.put("memberId", userId);
        PageData<StoreFavoritesPageDTO> page = storeFavoritesService.storePage(params);
        PageData<StoreFavoritesPageVO> data = new PageData<>();
        data.setTotal(page.getTotal());
        data.setList(ConvertUtils.sourceToTarget(page.getList(), StoreFavoritesPageVO.class));
        return new Result<PageData<StoreFavoritesPageVO>>().ok(data);
    }

    /**
     * 收藏店铺
     *
     * @param dto
     * @return
     */
    @PostMapping
    @ApiOperation("收藏店铺")
    @LogContext(code = PcFavoriteCode.SAVE_STORE_FAVORITE_CODE, note = PcFavoriteCode.SAVE_STORE_FAVORITE_DESC)
    public Result save(@RequestBody StoreFavoritesSaveDTO dto) {
        //效验数据
        ValidatorUtils.validateEntity(dto, AddGroup.class, DefaultGroup.class);
        Long memberId = null;
        String memberName = "";
        try {
            // 获取用户信息
            memberId = SecurityCurrentUser.getUserDetail().getId();
            memberName = SecurityCurrentUser.getUserDetail().getMemberName();
        } catch (CustomException e) {
            return new Result().error("请先登录");
        }

        storeFavoritesService.saveStoreFav(dto, memberId, memberName);

        return new Result().ok("", "关注成功");
    }

    /**
     * 取消店铺收藏
     *
     * @param storeIds
     * @return
     */
    @DeleteMapping
    @ApiOperation("取消店铺收藏")
    @LogContext(code = PcFavoriteCode.DELETE_STORE_FAVORITE_CODE, note = PcFavoriteCode.DELETE_STORE_FAVORITE_DESC)
    public Result delete(@RequestBody Long[] storeIds) {
        //效验数据
        AssertUtils.isArrayEmpty(storeIds, "id");
        Long memberId = null;
        try {
            // 获取用户信息
            memberId = SecurityCurrentUser.getUserDetail().getId();
        } catch (CustomException e) {
            return new Result().error("请先登录");
        }
        storeFavoritesService.delete(storeIds, memberId);

        return new Result().ok("", "取消成功");
    }
}
