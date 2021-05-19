/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.moudle.cart.controller;

import com.alibaba.fastjson.JSONObject;
import com.leimingtech.commons.tools.annotation.LogContext;
import com.leimingtech.commons.tools.constant.Constant;
import com.leimingtech.commons.tools.exception.ErrorCode;
import com.leimingtech.commons.tools.page.PageData;
import com.leimingtech.commons.tools.redis.RedisKeys;
import com.leimingtech.commons.tools.redis.RedisUtils;
import com.leimingtech.commons.tools.utils.ConvertUtils;
import com.leimingtech.commons.tools.utils.IdGenerator;
import com.leimingtech.commons.tools.utils.Result;
import com.leimingtech.commons.tools.validator.AssertUtils;
import com.leimingtech.commons.tools.validator.ValidatorUtils;
import com.leimingtech.commons.tools.validator.group.AddGroup;
import com.leimingtech.dto.setting.SettingAnnouncementTipsDTO;
import com.leimingtech.enums.SettingsEnum;
import com.leimingtech.modules.dto.cart.*;
import com.leimingtech.modules.dto.coupons.MemberCouponsIndexDTO;
import com.leimingtech.modules.dto.member.MemberDTO;
import com.leimingtech.modules.dto.recommend.CartRecommendPageDTO;
import com.leimingtech.modules.dto.reduce.FrontReduceActivityPageDTO;
import com.leimingtech.modules.enums.ActivityTypeEnum;
import com.leimingtech.modules.enums.cart.CartEnum;
import com.leimingtech.modules.enums.coupons.CouponsEnum;
import com.leimingtech.modules.service.cart.CartService;
import com.leimingtech.modules.service.coupons.CouponsActivitySearchService;
import com.leimingtech.modules.service.favorites.FavoritesService;
import com.leimingtech.modules.service.recommend.CartRecommendService;
import com.leimingtech.modules.service.reduce.ReduceActivitySearchService;
import com.leimingtech.moudle.cart.code.PcCartCode;
import com.leimingtech.moudle.cart.vo.FrontReduceActivityPageVO;
import com.leimingtech.moudle.cart.vo.PcIndexCartVO;
import com.leimingtech.moudle.cart.vo.ResultCartVO;
import com.leimingtech.moudle.cart.vo.SettlementVO;
import com.leimingtech.moudle.operation.index.vo.CartRecommendVO;
import com.leimingtech.security.SecurityCurrentUser;
import com.leimingtech.service.setting.SettingService;
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

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 购物车模块API
 *
 * @author lixiang
 * @version V1.0
 * @date 2020/5/9 10:09
 **/
@Slf4j
@RestController
@RequestMapping("cart")
@Api(tags = "购物车")
public class CartController {

    @Autowired
    private CartService cartService;

    @Autowired
    private RedisUtils redisUtils;

    @Autowired
    private FavoritesService favoritesService;

    @Autowired
    private CartRecommendService cartRecommendService;

    @Autowired
    private SettingService settingService;
    @Autowired
    private CouponsActivitySearchService couponsActivitySearchService;

    @Autowired
    private ReduceActivitySearchService reduceActivitySearchService;


    /**
     * 购物车列表，pc首页弹窗接口
     *
     * @param request
     * @return
     */
    @GetMapping("list")
    @ApiOperation("首页购物车弹窗")
    @LogContext(code = PcCartCode.INDEX_CART_CODE, note = PcCartCode.INDEX_CART_DESC)
    public Result<PcIndexCartVO> indexPage(HttpServletRequest request) {
        PcIndexCartDTO pcIndexCartDTO = null;
        if (!SecurityCurrentUser.userIsLogin()) {
            // 获取购物车key
            String frontCartKey = RedisKeys.getFrontCartKey(getCookie(request));
            Map<String, Object> map = redisUtils.hGetAll(frontCartKey);
            pcIndexCartDTO = cartService.pcIndexCart(new ArrayList(map.values()), null);
        } else {
            pcIndexCartDTO = cartService.pcIndexCart(new ArrayList(), SecurityCurrentUser.getUserDetail().getId());
        }
        return new Result<PcIndexCartVO>().ok(ConvertUtils.sourceToTarget(pcIndexCartDTO, PcIndexCartVO.class));
    }


    /**
     * 购物车列表
     *
     * @return
     */
    @GetMapping("page")
    @ApiOperation("购物车列表")
    @LogContext(code = PcCartCode.INDEX_CART_PAGE_CODE, note = PcCartCode.INDEX_CART_PAGE_DESC)
    public Result<ResultCartVO> page(HttpServletRequest request) {
        ResultCartDTO resultCartDTO = new ResultCartDTO();
        if (!SecurityCurrentUser.userIsLogin()) {
            // 获取购物车key
            String frontCartKey = RedisKeys.getFrontCartKey(getCookie(request));

            Map<String, Object> map = redisUtils.hGetAll(frontCartKey);
            List<CartDTO> cartDTOList = new ArrayList(map.values());
            resultCartDTO = cartService.getRedisCart(cartDTOList, CartEnum.PC_CART_LIST.value());
        } else {
            resultCartDTO = cartService.disabledCart(SecurityCurrentUser.getUserDetail().getId());
        }
        return new Result<ResultCartVO>().ok(ConvertUtils.sourceToTarget(resultCartDTO, ResultCartVO.class));
    }


    /**
     * 加入购物车
     *
     * @param dto 参数实体
     * @return
     */
    @PostMapping
    @ApiOperation("加入购物车")
    @LogContext(code = PcCartCode.INDEX_SAVE_CART_CODE, note = PcCartCode.INDEX_SAVE_CART_DESC)
    public Result save(@RequestBody SaveCartDTO dto, HttpServletRequest request) {

        if (dto.getActivityType() == null) {
            dto.setActivityType(ActivityTypeEnum.NO_ACTIVITY.value());
        }

        // 判断是否登录，未登录存入redis，已登录存数据库
        if (!SecurityCurrentUser.userIsLogin()) {
            // 获取redisKEY
            String redisKey = getCookie(request);
            String frontCartKey = RedisKeys.getFrontCartKey(redisKey);
            cartService.savaRedisCart(dto, frontCartKey);
            return new Result().ok(redisKey, "加入购物车成功");
        }

        // 保存购物车
        cartService.save(dto, SecurityCurrentUser.getUserDetail().getId());
        return new Result().ok(null, "加入购物车成功");
    }


    /**
     * 未登录删除缓存中的购物车
     *
     * @param specId 规格ID
     * @return
     */
    @DeleteMapping
    @ApiOperation("未登录删除缓存中的购物车")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "specId", value = "规格ID")
    })
    @LogContext(code = PcCartCode.INDEX_NOT_LOGIN_DELETE_CART_CODE, note = PcCartCode.INDEX_NOT_LOGIN_DELETE_CART_DESC)
    public Result delete(@RequestBody String[] specId, HttpServletRequest request) {
        String frontCartKey = RedisKeys.getFrontCartKey(getCookie(request));
        redisUtils.hDel(frontCartKey, specId);
        return new Result().ok("删除成功");
    }

    /**
     * 已登录批量删除和单个删除购物车
     *
     * @param cartId  购物车ID
     * @param delType 1 单个删除 2 批量删除
     * @return
     */
    @DeleteMapping("delete")
    @ApiOperation("已登录批量删除和单个删除购物车")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "cartId", value = "购物车ID，单个删除需要传", paramType = "query", required = false, dataType = "Long"),
            @ApiImplicitParam(name = "delType", value = "1 单个删除 2 批量删除", paramType = "query", dataType = "integer")
    })
    @LogContext(code = PcCartCode.INDEX_LOGIN_DELETE_CART_CODE, note = PcCartCode.INDEX_LOGIN_DELETE_CART_DESC)
    public Result deleteCart(@RequestParam(value = "cartId", required = false) Long cartId,
                             @RequestParam(value = "delType") Integer delType) {
        // 获取用户ID
        if (!SecurityCurrentUser.userIsLogin()) {
            return new Result().error(ErrorCode.UNAUTHORIZED, "用户未登录");
        }

        if (delType == 1) {
            cartService.delete(new Long[]{cartId});
        } else {
            MemberDTO memberDTO = SecurityCurrentUser.getUserDetail();
            cartService.deleteIsSelectCart(memberDTO.getId());
        }

        return new Result().ok(null, "删除成功");
    }

    /**
     * 购物车同步数据
     */
    @PostMapping("merge/cart")
    @ApiOperation("购物车同步数据")
    @LogContext(code = PcCartCode.INDEX_MERGE_CART_CODE, note = PcCartCode.INDEX_MERGE_CART_DESC)
    public Result merge(HttpServletRequest request) {

        if (!SecurityCurrentUser.userIsLogin()) {
            return new Result().error(ErrorCode.UNAUTHORIZED, "用户未登录");
        }
        // 获取用户信息
        MemberDTO memberDTO = SecurityCurrentUser.getUserDetail();
        // 获取前端用户未登录下的购物车key
        String cartKey = RedisKeys.getFrontCartKey(getCookie(request));
        // 同步购物车数据
        cartService.synchCart(cartKey, memberDTO.getId());
        // 同步完成 删除用户未登录下的购物车数据
        redisUtils.delete(cartKey);
        return new Result().ok(null, "数据同步成功");
    }

    /**
     * 修改购物车的选中状态
     *
     * @param cartId 购物车ID
     * @param state  选中状态 0 取消选中 1 选中
     * @return
     */
    @PostMapping("update/check")
    @ApiOperation("修改购物车的选中状态")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "state", value = "选中状态 0 取消选中， 1 选中", paramType = "query", required = true, dataType = "integer")
    })
    @LogContext(code = PcCartCode.INDEX_CHECK_CART_CODE, note = PcCartCode.INDEX_CHECK_CART_DESC)
    public Result updateCheck(@RequestParam Long cartId, Integer state, HttpServletRequest request) {
        AssertUtils.isNull(cartId, "购物车id不能为空");
        if (!SecurityCurrentUser.userIsLogin()) {
            String frontCartKey = RedisKeys.getFrontCartKey(getCookie(request));
            // 验证redis中有没有相同的数据
            Map<String, Object> map = redisUtils.hGetAll(frontCartKey);
            List<CartDTO> cartDTOList = new ArrayList(map.values());
            for (CartDTO cartDTO : cartDTOList) {
                if (cartDTO.getId().equals(cartId)) {
                    cartDTO.setIsSelect(state);
                    redisUtils.hSet(frontCartKey, String.valueOf(cartDTO.getSpecId()), cartDTO);
                }
            }
        } else {
            CartDTO cartDTO = new CartDTO();
            cartDTO.setId(cartId);
            cartDTO.setIsSelect(state);
            cartService.updateCart(cartDTO);
        }

        return new Result().ok(null, "修改成功");
    }

    /**
     * 全选或者取消全选
     *
     * @param storeId 店铺ID
     * @param state   选中状态 0 取消全选， 1 全选
     * @return
     */
    @PostMapping("check/all")
    @ApiOperation("购物车取消全选或者全选")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "state", value = "选中状态 0 取消全选， 1 全选", paramType = "query", required = true, dataType = "integer")
    })
    @LogContext(code = PcCartCode.INDEX_CHECK_ALL_CART_CODE, note = PcCartCode.INDEX_CHECK_ALL_CART_DESC)
    public Result checkAll(@RequestParam(value = "storeId", required = false, defaultValue = "") Long storeId,
                           Integer state, HttpServletRequest request) {
        if (SecurityCurrentUser.userIsLogin()) {
            Long memberId = SecurityCurrentUser.getUserDetail().getId();
            //更改选中状态
            cartService.updateIsSelect(memberId, storeId, state);
        } else {
            String frontCartKey = RedisKeys.getFrontCartKey(getCookie(request));
            Map<String, Object> map = redisUtils.hGetAll(frontCartKey);
            List<CartDTO> cartDTOList = new ArrayList(map.values());
            for (CartDTO cartDTO : cartDTOList) {
                if (storeId == null) {
                    cartDTO.setIsSelect(state);
                } else if (cartDTO.getStoreId().equals(storeId)) {
                    cartDTO.setIsSelect(state);
                }
                redisUtils.hSet(frontCartKey, String.valueOf(cartDTO.getSpecId()), cartDTO);
            }
        }
        return new Result().ok(null, "修改成功");
    }


    /**
     * 查询购物车内商品数量
     *
     * @param request
     * @return
     */
    @GetMapping("goods/num")
    @ApiOperation("查询购物车内商品数量")
    @LogContext(code = PcCartCode.INDEX_CART_NUM_CODE, note = PcCartCode.INDEX_CART_NUM_DESC)
    public Result<Integer> findGoodsNum(HttpServletRequest request) {

        Integer count = 0;
        if (SecurityCurrentUser.userIsLogin()) {
            // 获取用户信息
            MemberDTO userDetail = SecurityCurrentUser.getUserDetail();
            count = cartService.findCartNum(userDetail.getId());
        } else {
            String frontCartKey = RedisKeys.getFrontCartKey(getCookie(request));
            Map<String, Object> map = redisUtils.hGetAll(frontCartKey);
            count = map.size();
        }
        return new Result<Integer>().ok(count);
    }

    /**
     * 购物车移入收藏
     *
     * @param cartId
     * @return
     */
    @PostMapping("favorites")
    @ApiOperation("购物车移入收藏")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "cartId", value = "购物车Id：批量移入不传值，单个移入传单个购物车Id", paramType = "query", required = false, dataType = "Long")
    })
    @LogContext(code = PcCartCode.INDEX_CART_FAVORITE_CODE, note = PcCartCode.INDEX_CART_FAVORITE_DESC)
    public Result cartFav(@RequestParam(value = "cartId", required = false) Long cartId) {

        //获取用户信息
        if (!SecurityCurrentUser.userIsLogin()) {
            return new Result<>().error(ErrorCode.UNAUTHORIZED, "您未登录，无法进行此操作");
        }
        // 获取用户信息
        Long memberId = SecurityCurrentUser.getUserDetail().getId();

        //单个移入
        if (cartId != null) {
            favoritesService.cartOneFavorites(cartId);
        } else {
            //批量移入
            favoritesService.cartFavoriteGoods(memberId);
        }


        return new Result().ok(null, "移入收藏成功");
    }


    /**
     * 获取reidsKey
     *
     * @return
     */
    private String getCookie(HttpServletRequest request) {

        String redisKey = request.getHeader("redisKey");
        if (StringUtils.isBlank(redisKey)) {
            redisKey = String.valueOf(IdGenerator.defaultSnowflakeId());

        }
        return redisKey;
    }

    /**
     * 再次购买
     *
     * @return
     */
    @PostMapping("again")
    @ApiOperation("再次购买")
    public Result againBuy(@RequestBody AgainSaveCartDTO againSaveCartDTO, HttpServletRequest request) {
        if (againSaveCartDTO != null && CollectionUtils.isNotEmpty(againSaveCartDTO.getSaveCartDTO())) {
            for (SaveCartDTO saveCartDTO : againSaveCartDTO.getSaveCartDTO()) {
                saveCartDTO.setActivityId(againSaveCartDTO.getActivityId());
                saveCartDTO.setActivityType(againSaveCartDTO.getActivityType());
                Result save = save(saveCartDTO, request);
                if (save.getCode() != 200) {
                    return save;
                }
            }
        }
        return new Result().ok(null, "加入购物车成功");
    }

    @GetMapping("recommend/page")
    @ApiOperation("购物车商品推荐列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = Constant.PAGE, value = "当前页码，从1开始", paramType = "query", required = true, dataType = "int"),
            @ApiImplicitParam(name = Constant.LIMIT, value = "每页显示记录数(默认第一页展示10个，下拉加载)", defaultValue = "10", paramType = "query", required = true, dataType = "int")
    })
    public Result<PageData<CartRecommendVO>> page(@ApiIgnore @RequestParam Map<String, Object> params) {

        PageData<CartRecommendPageDTO> page = cartRecommendService.pageList(params);
        PageData<CartRecommendVO> pageData = new PageData<>();
        if (CollectionUtils.isNotEmpty(page.getList())) {
            List<CartRecommendVO> cartRecommendVOList = ConvertUtils.sourceToTarget(page.getList(), CartRecommendVO.class);
            pageData.setList(cartRecommendVOList);
            pageData.setTotal(page.getTotal());
        }
        return new Result<PageData<CartRecommendVO>>().ok(pageData);
    }

    /**
     * 购物车去结算接口
     *
     * @return 填写订单实体
     * @date 2019/6/27 20:21
     * @author lixiang
     **/
    @GetMapping("settlement")
    @ApiOperation("购物车去结算")
    @LogContext(code = PcCartCode.INDEX_CART_SETTMENT_CODE, note = PcCartCode.INDEX_CART_SETTMENT_DESC)
    public Result<SettlementVO> settlement() {

        if (!SecurityCurrentUser.userIsLogin()) {
            return new Result<SettlementVO>().error(ErrorCode.UNAUTHORIZED, "用户未登录");
        }
        // 获取用户信息
        Long memberId = SecurityCurrentUser.getUserDetail().getId();
//        Long memberId = 1192326496660873218L;

        SettlementDTO settlementDTO = cartService.settlement(memberId);

        if (settlementDTO != null) {
            // 获取温馨提示
            settlementDTO = packageSettlementData(settlementDTO);

            return new Result<SettlementVO>().ok(ConvertUtils.sourceToTarget(settlementDTO, SettlementVO.class), "购物车去结算成功");
        } else {
            return new Result<SettlementVO>().error(ErrorCode.INTERNAL_SERVER_ERROR, "服务器内部异常");
        }
    }

    @PutMapping("settlement/price/flush")
    @ApiOperation("购物车切换优惠活动")
    @LogContext(code = PcCartCode.INDEX_CART_SETTMENT_CHANGE_CODE, note = PcCartCode.INDEX_CART_SETTMENT_CHANGE_DESC)
    public Result<SettlementVO> recountSettlementPrice(@RequestBody CartSettlementActivtyDTO dto) {
        if (!SecurityCurrentUser.userIsLogin()) {
            return new Result<SettlementVO>().error(ErrorCode.UNAUTHORIZED, "用户未登录");
        }
        // 获取用户信息
        Long memberId = SecurityCurrentUser.getUserDetail().getId();
//        Long memberId = 1192326496660873218L;

        // 校验会员优惠券
        String memberCouponsId = dto.getMemberCouponsId();
        Map<String, Long> storeCouponsMap = new HashMap<>(2);
        if (StringUtils.isNotBlank(memberCouponsId)) {
            JSONObject jsonObject = JSONObject.parseObject(memberCouponsId);
            for (String key : jsonObject.keySet()) {
                Object o = jsonObject.get(key);
                if (o != null) {
                    MemberCouponsIndexDTO memberCoupons = couponsActivitySearchService.getMemberCoupons(Long.parseLong(o.toString()));
                    if (memberCoupons == null || !memberCoupons.getMemberId().equals(memberId)) {
                        // 未查询到会员优惠券
                        return new Result<SettlementVO>().error(ErrorCode.FORBIDDEN, "优惠券不存在");
                    } else if (CouponsEnum.COUPON_CAN_USE.value() != memberCoupons.getCouponsState()) {
                        // 优惠券不可用
                        return new Result<SettlementVO>().error(ErrorCode.FORBIDDEN, "优惠券不可用");
                    }
                    storeCouponsMap.put(key, Long.parseLong(o.toString()));
                }
            }
        }

        SettlementDTO settlementDTO = cartService.recountSettlementPrice(storeCouponsMap, memberId, dto.getAddressId(), 0);
        if (settlementDTO != null) {
            return new Result<SettlementVO>().ok(ConvertUtils.sourceToTarget(settlementDTO, SettlementVO.class), "重新计算金额成功");
        } else {
            return new Result<SettlementVO>().error("切换活动失败");
        }
    }

    /**
     * 立即购买接口
     *
     * @param specId: 规格ID
     * @param number: 购买数量
     * @date 2019/6/27 14:58
     * @author lixiang
     **/
    @GetMapping("buynow")
    @ApiOperation("立即购买")
    @LogContext(code = PcCartCode.INDEX_CART_BUYNOW_CODE, note = PcCartCode.INDEX_CART_BUYNOW_DESC)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "specId", value = "商品规格ID", paramType = "query", required = true, dataType = "long"),
            @ApiImplicitParam(name = "number", value = "购买数量", paramType = "query", required = true, dataType = "int"),
    })
    public Result<SettlementVO> buyNow(@RequestParam("specId") Long specId,
                                       @RequestParam("number") Integer number) {


        AssertUtils.isNull(specId, "商品规格Id不能为空");

        AssertUtils.isNull(number, "商品购买数量不能为空");

        if (!SecurityCurrentUser.userIsLogin()) {
            return new Result<SettlementVO>().error(ErrorCode.UNAUTHORIZED, "用户未登录");
        }
        // 获取用户信息
        Long memberId = SecurityCurrentUser.getUserDetail().getId();
//        Long memberId = 1192326496660873218L;

        SettlementDTO settlementDTO = cartService.buyNow(memberId, specId, number);

        if (settlementDTO != null) {
            // 获取温馨提示
            packageSettlementData(settlementDTO);
            return new Result<SettlementVO>().ok(ConvertUtils.sourceToTarget(settlementDTO, SettlementVO.class), "立即购买去结算成功");
        } else {
            return new Result<SettlementVO>().error(ErrorCode.INTERNAL_SERVER_ERROR, "服务器内部异常");
        }
    }

    @PutMapping("buynow/price/flush")
    @ApiOperation("立即购买切换优惠活动/地址")
    @LogContext(code = PcCartCode.INDEX_CART_BUYNOW_CHANGE_CODE, note = PcCartCode.INDEX_CART_BUYNOW_CHANGE_DESC)
    public Result<SettlementVO> recountSettlementPrice(@RequestBody NowSettlementActivtyDTO dto) {
        // 数据校验
        ValidatorUtils.validateEntity(dto, AddGroup.class);

        if (!SecurityCurrentUser.userIsLogin()) {
            return new Result<SettlementVO>().error(ErrorCode.UNAUTHORIZED, "用户未登录");
        }
        // 获取用户信息
        Long memberId = SecurityCurrentUser.getUserDetail().getId();
        dto.setMemberId(memberId);

        SettlementDTO settlementDTO = cartService.recountSettlementPrice(dto);
        if (settlementDTO != null) {
            return new Result<SettlementVO>().ok(ConvertUtils.sourceToTarget(settlementDTO, SettlementVO.class), "重新计算金额成功");
        } else {
            return new Result<SettlementVO>().error("切换活动失败");
        }
    }

    /**
     * @Author: LX 17839193044@162.com
     * @Description: 抽取方法查询公告信息
     * @Date: 2019/6/17 14:19
     * @Version: V1.0
     */
    private SettlementDTO packageSettlementData(SettlementDTO settlementDTO) {
        //1、获取购物车公告提示
        SettingAnnouncementTipsDTO announcementTips = settingService.getAnnouncementTipsSet();
        if (announcementTips != null && StringUtils.equals(announcementTips.getCartTipsOpen(),
                SettingsEnum.CART_TIPS_OPEN.value())) {
            settlementDTO.setKindlyeminder(announcementTips.getCartTipsContent());
        }

        return settlementDTO;
    }

    /**
     * 发起拼团
     *
     * @param specId  商品规格id
     * @param number  购买数量
     * @param groupId 拼团活动id
     * @return
     * @date 2020-03-20 11:28
     * @author huangkeyuan@leimingtech.com
     **/
    @GetMapping("group/buynow")
    @ApiOperation("发起拼团")
    @LogContext(code = PcCartCode.INDEX_CART_GROUP_BUYNOW_CODE, note = PcCartCode.INDEX_CART_GROUP_BUYNOW_DESC)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "specId", value = "商品规格ID", paramType = "query", required = true, dataType = "long"),
            @ApiImplicitParam(name = "number", value = "购买数量", paramType = "query", required = true, dataType = "int"),
            @ApiImplicitParam(name = "groupId", value = "拼团活动id", paramType = "query", required = true, dataType = "long"),
            @ApiImplicitParam(name = "addressId", value = "会员地址id", paramType = "query", dataType = "long"),
    })
    public Result<SettlementVO> groupBuyNow(@RequestParam("specId") Long specId,
                                            @RequestParam("number") Integer number,
                                            @RequestParam("groupId") Long groupId,
                                            @RequestParam(value = "addressId", required = false) Long addressId) {
        AssertUtils.isNull(specId, "商品规格Id不能为空");

        AssertUtils.isNull(number, "商品购买数量不能为空");

        AssertUtils.isNull(groupId, "拼团活动id不能为空");

        if (!SecurityCurrentUser.userIsLogin()) {
            return new Result<SettlementVO>().error(ErrorCode.UNAUTHORIZED, "用户未登录");
        }
        // 获取用户信息
        Long memberId = SecurityCurrentUser.getUserDetail().getId();

        SettlementDTO settlementDTO = cartService.groupBuyNow(memberId, specId, number, groupId, addressId);

        if (settlementDTO != null) {
            // 获取温馨提示
            packageSettlementData(settlementDTO);
            return new Result<SettlementVO>().ok(ConvertUtils.sourceToTarget(settlementDTO, SettlementVO.class), "立即购买去结算成功");
        } else {
            return new Result<SettlementVO>().error(ErrorCode.INTERNAL_SERVER_ERROR, "服务器内部异常");
        }
    }

    /**
     * 查询购物车商品是否收藏
     */
    @GetMapping("isfavorites")
    @ApiOperation("查询购物车商品是否收藏")
    public Result isFavorites(@RequestParam Long specId) {

        if (!SecurityCurrentUser.userIsLogin()) {
            return new Result().error("用户未登录");
        }
        Long memberId = SecurityCurrentUser.getUserDetail().getId();
        Integer count = favoritesService.findIsFavorites(specId, memberId);
        if (count > 0) {
            return new Result().ok("用户已收藏");
        }
        return new Result().ok(null, "未收藏");
    }

    @GetMapping("goods")
    @ApiOperation("商品可选满减活动列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "goodsId", value = "商品spuID", required = true, paramType = "query", dataType = "long"),
            @ApiImplicitParam(name = "activityId", value = "已选活动id", paramType = "query", dataType = "long"),
            @ApiImplicitParam(name = "activityType", value = "已选活动类型 0优惠券 1满减", paramType = "query", dataType = "integer")
    })
    public Result<List<FrontReduceActivityPageVO>> goodsDetailCouponsList(@ApiIgnore @RequestParam Map<String, Object> params) {
        List<FrontReduceActivityPageDTO> frontReduceActivityPageDTOList = reduceActivitySearchService.goodsDetailCouponsList(params);
        return new Result<List<FrontReduceActivityPageVO>>().ok(ConvertUtils.sourceToTarget(frontReduceActivityPageDTOList, FrontReduceActivityPageVO.class), "查询成功");
    }
}
