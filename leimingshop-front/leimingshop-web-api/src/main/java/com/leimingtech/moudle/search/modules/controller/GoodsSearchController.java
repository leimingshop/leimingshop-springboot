/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.moudle.search.modules.controller;

import com.alibaba.fastjson.JSON;
import com.leimingtech.commons.tools.exception.ErrorCode;
import com.leimingtech.commons.tools.utils.HttpContextUtils;
import com.leimingtech.commons.tools.utils.IpUtils;
import com.leimingtech.commons.tools.utils.Result;
import com.leimingtech.commons.tools.validator.ValidatorUtils;
import com.leimingtech.commons.tools.validator.group.AddGroup;
import com.leimingtech.logging.MonitorLogger;
import com.leimingtech.logging.MonitorLoggerFactory;
import com.leimingtech.modules.constant.ActivityMongoConstants;
import com.leimingtech.modules.dto.coupons.FrontCouponsActivityPageDTO;
import com.leimingtech.modules.dto.coupons.GoodsDetailCouponsListDTO;
import com.leimingtech.modules.dto.member.MemberDTO;
import com.leimingtech.modules.dto.reduce.FrontReduceActivityPageDTO;
import com.leimingtech.modules.enums.ActivityTypeEnum;
import com.leimingtech.modules.service.NosqlService;
import com.leimingtech.moudle.search.modules.dto.*;
import com.leimingtech.moudle.search.modules.dto.optimize.OptimizeSpecGoodsDetailVO;
import com.leimingtech.moudle.search.modules.service.CouponsSearchService;
import com.leimingtech.moudle.search.modules.service.GoodsSearchService;
import com.leimingtech.moudle.search.modules.service.ReduceSearchService;
import com.leimingtech.mq.constant.MqConstant;
import com.leimingtech.mq.service.RabbitMqSendService;
import com.leimingtech.security.SecurityCurrentUser;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author kuangweiguo
 * @version 1.0
 * @date 2019/6/29 2:40 PM
 */
@RequestMapping("search")
@RestController
@Api(tags = "商品搜索")
public class GoodsSearchController {

    private MonitorLogger logger = MonitorLoggerFactory.getMonitorLogger(GoodsSearchController.class);

    @Autowired
    private GoodsSearchService goodsSearchService;

    @Autowired
    private RabbitMqSendService rabbitMqSendService;
    @Autowired
    private CouponsSearchService couponsSearchService;
    @Autowired
    private ReduceSearchService reduceSearchService;
    @Autowired
    private NosqlService nosqlService;

    /**
     * 商品关键字匹配（返回商品集合与筛选条件集合）
     *
     * @param goodsSearchDTO: 商品搜索对象
     * @return: 商品索引以及筛选集合
     */
    @PostMapping("goods")
    @ApiOperation("商品搜索")
    public Result<GoodsSearchVO> searchGoods(@RequestBody GoodsSearchDTO goodsSearchDTO) {
        if (goodsSearchDTO.getPageSize() == null) {
            //默认排序字段
            goodsSearchDTO.setPageSize(10);
        }
        if (goodsSearchDTO.getPageNo() == null) {
            //默认排序字段
            goodsSearchDTO.setPageNo(1);
        }
        GoodsSearchVO goodsSearchVO = goodsSearchService.goodsSerarch(goodsSearchDTO);
        if (SecurityCurrentUser.userIsLogin()) {
            MemberDTO userDetail = SecurityCurrentUser.getUserDetail();
            if (null != userDetail.getId() && StringUtils.isNotBlank(goodsSearchDTO.getKeyword())) {
                // 发送MQ保存商品搜索记录
                Map<String, String> map = new HashMap<>(10);
                map.put("keyword", goodsSearchDTO.getKeyword());
                map.put("memberId", userDetail.getId().toString());
                map.put("searchIp", IpUtils.getIpAddr(HttpContextUtils.getHttpServletRequest()));
                rabbitMqSendService.sendMsg(MqConstant.LEIMINGTECH_SAVE_SEACH_HISTORY_QUEUE, JSON.toJSONString(map));
            }
        }
        return new Result<GoodsSearchVO>().ok(goodsSearchVO);
    }


    /**
     * 根据关键字智能提示（支持中文、拼音）
     *
     * @param keyword: 搜索关键字
     * @return 智能提示集合
     * @date 2019/12/17 16:00
     * @author lixiangx@leimingtech.com
     **/
    @GetMapping("tips")
    @ApiOperation("商品搜索自动提示")
    public Result<List<String>> searchWordTips(@RequestParam(value = "keyword") String keyword) {
        List<String> result = goodsSearchService.intellisenseWord(keyword);
        return new Result<List<String>>().ok(result);
    }

    /**
     * 查询店铺下商品
     *
     * @param goodsSearchDTO 商品搜索对象
     * @return
     */
    @PostMapping("store/goods")
    @ApiOperation("店铺商品")
    public Result<GoodsSearchVO> searchStoreGoods(@RequestBody GoodsSearchDTO goodsSearchDTO) {
        if (null == goodsSearchDTO.getStoreId()) {
            logger.error(String.valueOf(ErrorCode.NOT_NULL), "缺少店铺ID");
            return new Result<GoodsSearchVO>().error(ErrorCode.NOT_NULL, "店铺ID不能为空");
        }
        if (StringUtils.isBlank(goodsSearchDTO.getSortField())) {
            //默认排序字段
            goodsSearchDTO.setSortField("specSellPrice");
        }
        if (goodsSearchDTO.getPageSize() == null) {
            //默认排序字段
            goodsSearchDTO.setPageSize(10);
        }
        if (goodsSearchDTO.getPageNo() == null) {
            //默认排序字段
            goodsSearchDTO.setPageNo(1);
        }
        GoodsSearchVO goodsSearchVO = goodsSearchService.goodsSerarch(goodsSearchDTO);
        return new Result<GoodsSearchVO>().ok(goodsSearchVO);
    }


    /**
     * 商品筛选条件搜索（只返回商品搜素接口）
     *
     * @param filterDTO: 商品搜索筛选条件
     * @return: 商品搜索结果集
     * @date 2019/7/26 15:01
     * @author lixiang
     **/
    @PostMapping("filter")
    @ApiOperation("商品搜索")
    public Result<GoodsSearchFilterVO> searchFilterGoods(@RequestBody GoodsSearchFilterDTO filterDTO) {
        if (filterDTO.getPageSize() == null) {
            //默认排序字段
            filterDTO.setPageSize(10);
        }
        if (filterDTO.getPageNo() == null) {
            //默认排序字段
            filterDTO.setPageNo(1);
        }
        GoodsSearchFilterVO goodsSearchFilterVO = goodsSearchService.goodsFilterSerarch(filterDTO);
        return new Result<GoodsSearchFilterVO>().ok(goodsSearchFilterVO);
    }

    @PostMapping("collect/bills")
    @ApiOperation("优惠券去凑单商品列表页面")
    public Result<CouponsGoodsPageDTO> collectBillsGoods(@RequestBody GoCollectBillsDTO goCollectBillsDTO, HttpServletRequest request) {
        // 校验数据
        ValidatorUtils.validateEntity(goCollectBillsDTO, AddGroup.class);
        Long memberId = null;
        MemberDTO userDetail = null;
        if (ActivityTypeEnum.OTHER_SETTLEMENT.value() != goCollectBillsDTO.getCollectBillType()) {
            // 需要登录
            try {
                userDetail = SecurityCurrentUser.getUserDetail();
            } catch (Exception e) {
                return new Result<CouponsGoodsPageDTO>().error(ErrorCode.UNAUTHORIZED, "请登录");
            }
            memberId = userDetail.getId();
        } else {
            // 不需要登录
            if (SecurityCurrentUser.userIsLogin()) {
                memberId = SecurityCurrentUser.getUserDetail().getId();
            }
        }
//        memberId = 1192326496660873218L;
        if (0 == goCollectBillsDTO.getCollectBillType() && 1 == goCollectBillsDTO.getCollectBillType()) {
            if (goCollectBillsDTO.getBeforeCollectSpecId() == null) {
                return new Result<CouponsGoodsPageDTO>().error(ErrorCode.FORBIDDEN, "规格id不能为空");
            } else if (goCollectBillsDTO.getBeforeCollectNum() == null) {
                return new Result<CouponsGoodsPageDTO>().error(ErrorCode.FORBIDDEN, "商品数量不能为空");
            } else if (goCollectBillsDTO.getBeforeCollectAmount() == null) {
                return new Result<CouponsGoodsPageDTO>().error(ErrorCode.FORBIDDEN, "结算价格不能为空");
            }
        }
        if (StringUtils.isBlank(goCollectBillsDTO.getSortField())) {
            //默认排序字段
            goCollectBillsDTO.setSortField("specSaleNum");
        }
        if (StringUtils.isBlank(goCollectBillsDTO.getSortType())) {
            //默认排序方式
            goCollectBillsDTO.setSortType("DESC");
        }
        if (goCollectBillsDTO.getPageSize() == null) {
            //默认排序字段
            goCollectBillsDTO.setPageSize(10);
        }
        if (goCollectBillsDTO.getPageNo() == null) {
            //默认排序字段
            goCollectBillsDTO.setPageNo(1);
        }
        CouponsGoodsPageDTO couponsGoodsPageDTO = goodsSearchService.collectBillsGoods(goCollectBillsDTO, memberId, request);
        return new Result<CouponsGoodsPageDTO>().ok(couponsGoodsPageDTO, "查询成功");
    }

    @PostMapping("collect/bills/reduce")
    @ApiOperation("满减活动去凑单商品列表页面")
    public Result<ReduceGoodsPageDTO> collectBillsReduceGoods(@RequestBody GoCollectBillsDTO goCollectBillsDTO, HttpServletRequest request) {
        // 校验数据
        ValidatorUtils.validateEntity(goCollectBillsDTO, AddGroup.class);
        Long memberId = null;
        MemberDTO userDetail = null;
        if (ActivityTypeEnum.OTHER_SETTLEMENT.value() != goCollectBillsDTO.getCollectBillType()) {
            // 需要登录
            try {
                userDetail = SecurityCurrentUser.getUserDetail();
            } catch (Exception e) {
                return new Result<ReduceGoodsPageDTO>().error(ErrorCode.UNAUTHORIZED, "请登录");
            }
            memberId = userDetail.getId();
        } else {
            // 不需要登录
            if (SecurityCurrentUser.userIsLogin()) {
                memberId = SecurityCurrentUser.getUserDetail().getId();
            }
        }
//        memberId = 1192326496660873218L;
        if (0 == goCollectBillsDTO.getCollectBillType() || 1 == goCollectBillsDTO.getCollectBillType()) {
            if (goCollectBillsDTO.getBeforeCollectSpecId() == null) {
                return new Result<ReduceGoodsPageDTO>().error(ErrorCode.FORBIDDEN, "规格id不能为空");
            } else if (goCollectBillsDTO.getBeforeCollectNum() == null) {
                return new Result<ReduceGoodsPageDTO>().error(ErrorCode.FORBIDDEN, "商品数量不能为空");
            } else if (goCollectBillsDTO.getBeforeCollectAmount() == null) {
                return new Result<ReduceGoodsPageDTO>().error(ErrorCode.FORBIDDEN, "结算价格不能为空");
            }
        }
        if (StringUtils.isBlank(goCollectBillsDTO.getSortField())) {
            //默认排序字段
            goCollectBillsDTO.setSortField("specSaleNum");
        }
        if (StringUtils.isBlank(goCollectBillsDTO.getSortType())) {
            //默认排序方式
            goCollectBillsDTO.setSortType("DESC");
        }
        if (goCollectBillsDTO.getPageSize() == null) {
            //默认排序字段
            goCollectBillsDTO.setPageSize(10);
        }
        if (goCollectBillsDTO.getPageNo() == null) {
            //默认排序字段
            goCollectBillsDTO.setPageNo(1);
        }
        ReduceGoodsPageDTO reduceGoodsPageDTO = goodsSearchService.collectBillsReduceGoods(goCollectBillsDTO, memberId, request);
        return new Result<ReduceGoodsPageDTO>().ok(reduceGoodsPageDTO, "查询成功");
    }


    @GetMapping("goods/details")
    @ApiOperation("商品详情")
    public Result<SpecGoodsDetailVO> detailsDetails(@ModelAttribute GoodsDetailsSearchDTO goodsDetailsSearchDTO) {
        if (goodsDetailsSearchDTO.getSpecId() == null) {
            logger.info("500", "规格参数空");
            return new Result<SpecGoodsDetailVO>().error("规格id为空");
        }
        // 获取登录信息
        Long memberId = null;
        int loginFlag = 0;
        if (SecurityCurrentUser.userIsLogin()) {
            loginFlag = 1;
            memberId = SecurityCurrentUser.getUserDetail().getId();
        }

        SpecGoodsDetailVO goodsDetailsVO = goodsSearchService.goodsSerarchSpec(goodsDetailsSearchDTO.getSpecId(), goodsDetailsSearchDTO.getGoodsId());
        goodsDetailsVO.setLoginFlag(loginFlag);
        goodsDetailsVO.getGoodsDetailsVO().setCouponsFlag(0);
        // 获取商品列表页优惠券列表
        Long goodsId = goodsDetailsVO.getGoodsDetailsVO().getGoodsId();
        Long brandId = goodsDetailsVO.getGoodsDetailsVO().getGoodsVO().getBrandId();
        Long storeId = goodsDetailsVO.getGoodsDetailsVO().getGoodsVO().getStoreId();
        Long storeGoodsClass = goodsDetailsVO.getGoodsDetailsVO().getGoodsVO().getSecondStoreGoodsGcId();
        GoodsDetailCouponsListDTO goodsDetailCouponsListDTO = couponsSearchService.goodsDetailCouponsList(goodsId, brandId, storeId, storeGoodsClass);

        if (CollectionUtils.isNotEmpty(goodsDetailCouponsListDTO.getRecedList())) {
            // 用户已领取过该商品的优惠券，获取第一个用户优惠券数据
            FrontCouponsActivityPageDTO frontCouponsActivityPageDTO = new FrontCouponsActivityPageDTO();
            frontCouponsActivityPageDTO.setFaceValue(goodsDetailCouponsListDTO.getRecedList().get(0).getFaceValue());
            frontCouponsActivityPageDTO.setLimitAmount(goodsDetailCouponsListDTO.getRecedList().get(0).getLimitAmount());
            goodsDetailsVO.getGoodsDetailsVO().setFrontCouponsActivityPageDTO(frontCouponsActivityPageDTO);
            goodsDetailsVO.getGoodsDetailsVO().setCouponsFlag(1);
        } else if (CollectionUtils.isNotEmpty(goodsDetailCouponsListDTO.getCanRecList())) {
            // 商品存在可领取优惠券，获取第一个优惠券活动数据
            goodsDetailsVO.getGoodsDetailsVO().setFrontCouponsActivityPageDTO(goodsDetailCouponsListDTO.getCanRecList().get(0));
            goodsDetailsVO.getGoodsDetailsVO().setCouponsFlag(1);
        }

        // 获取商品列表页满减活动数据
        goodsDetailsVO.getGoodsDetailsVO().setReduceFlag(0);
        FrontReduceActivityPageDTO frontReduceActivityPageDTO = reduceSearchService.goodsDetailReduceList(goodsId, brandId, storeId, storeGoodsClass);
        if (null != frontReduceActivityPageDTO) {
            goodsDetailsVO.getGoodsDetailsVO().setFrontReduceActivityPageDTO(frontReduceActivityPageDTO);
            goodsDetailsVO.getGoodsDetailsVO().setReduceFlag(1);
        }

        // 返回拼团需要的用户头像
        if (SecurityCurrentUser.userIsLogin()) {
            goodsDetailsVO.getGoodsDetailsVO().setMemberAvatar(SecurityCurrentUser.getUserDetail().getMemberAvatar());
        }

        // 活动商品提醒
        if (memberId != null) {
            List<SpecActivityVO> specActivityList = goodsDetailsVO.getGoodsDetailsVO().getSpecActivityList();
            if (CollectionUtils.isNotEmpty(specActivityList)) {
                for (SpecActivityVO specActivity : specActivityList) {
                    // 是否提醒
                    Map<String, Object> queryParams = new HashMap<>(10);
                    queryParams.put("activityId", specActivity.getActivityId().toString());
                    queryParams.put("activityType", ActivityTypeEnum.SECKILL_ACTIVITY.value());
                    queryParams.put("goodsId", goodsDetailsVO.getGoodsDetailsVO().getGoodsId().toString());
                    queryParams.put("memberId", memberId.toString());
                    List<String> remindJsonList = nosqlService.queryData(ActivityMongoConstants.ACTIVITY_GOODS_REMIND, queryParams);
                    if (CollectionUtils.isNotEmpty(remindJsonList)) {
                        // 已设置提醒
                        specActivity.setRemindFlag(1);
                    } else {
                        // 未设置提醒
                        specActivity.setRemindFlag(0);
                    }
                }
            }
        }

        goodsDetailsVO.getGoodsDetailsVO().getGoodsVO().setGoodsSpecAttrVOList(null);
        goodsDetailsVO.getGoodsDetailsVO().getGoodsVO().setSpecAttrValueRefVO(null);
        return new Result<SpecGoodsDetailVO>().ok(goodsDetailsVO);
    }


    /**
     * 商品详情页接口（优化版本）
     *
     * @date 2020/5/18 16:26
     * @author lixiangx@leimingtech.com
     **/
    @GetMapping("goods/details/optimize")
    @ApiOperation("商品详情（优化）")
    public Result<OptimizeSpecGoodsDetailVO> optimizeDetailsDetails(@ModelAttribute GoodsDetailsSearchDTO goodsDetailsSearchDTO) {

        ValidatorUtils.validateEntity(goodsDetailsSearchDTO, AddGroup.class);

        Integer loginFlag = 0;
        // 获取登录信息
        Long memberId = null;
        if (SecurityCurrentUser.userIsLogin()) {
            loginFlag = 1;
            memberId = SecurityCurrentUser.getUserDetail().getId();
        }

        // 商品数据的查询
        OptimizeSpecGoodsDetailVO goodsDetailsVO = goodsSearchService.goodsSerarchSpecOptimize(goodsDetailsSearchDTO.getSpecId(), goodsDetailsSearchDTO.getGoodsId());
        // 设置用户登录标记
        goodsDetailsVO.setLoginFlag(loginFlag);
        // 优惠卷设置默认值
        goodsDetailsVO.getGoodsDetailsVO().setCouponsFlag(0);

        // 获取商品列表页优惠券列表
        Long goodsId = goodsDetailsVO.getGoodsDetailsVO().getGoodsId();
        Long brandId = goodsDetailsVO.getGoodsDetailsVO().getSpecGoodsVO().getBrandId();
        Long storeId = goodsDetailsVO.getGoodsDetailsVO().getSpecGoodsVO().getStoreId();
        GoodsDetailCouponsListDTO goodsDetailCouponsListDTO = couponsSearchService.goodsDetailCouponsList(goodsId, brandId, storeId, null);

        if (CollectionUtils.isNotEmpty(goodsDetailCouponsListDTO.getRecedList())) {
            // 用户已领取过该商品的优惠券，获取第一个用户优惠券数据
            FrontCouponsActivityPageDTO frontCouponsActivityPageDTO = new FrontCouponsActivityPageDTO();
            frontCouponsActivityPageDTO.setFaceValue(goodsDetailCouponsListDTO.getRecedList().get(0).getFaceValue());
            frontCouponsActivityPageDTO.setLimitAmount(goodsDetailCouponsListDTO.getRecedList().get(0).getLimitAmount());
            goodsDetailsVO.getGoodsDetailsVO().setFrontCouponsActivityPageDTO(frontCouponsActivityPageDTO);
            goodsDetailsVO.getGoodsDetailsVO().setCouponsFlag(1);
        } else if (CollectionUtils.isNotEmpty(goodsDetailCouponsListDTO.getCanRecList())) {
            // 商品存在可领取优惠券，获取第一个优惠券活动数据
            goodsDetailsVO.getGoodsDetailsVO().setFrontCouponsActivityPageDTO(goodsDetailCouponsListDTO.getCanRecList().get(0));
            goodsDetailsVO.getGoodsDetailsVO().setCouponsFlag(1);
        }

        // 获取商品列表页满减活动数据
        goodsDetailsVO.getGoodsDetailsVO().setReduceFlag(0);
        FrontReduceActivityPageDTO frontReduceActivityPageDTO = reduceSearchService.goodsDetailReduceList(goodsId, brandId, storeId, null);
        if (null != frontReduceActivityPageDTO) {
            goodsDetailsVO.getGoodsDetailsVO().setFrontReduceActivityPageDTO(frontReduceActivityPageDTO);
            goodsDetailsVO.getGoodsDetailsVO().setReduceFlag(1);
        }

        // 返回拼团需要的用户头像
        if (SecurityCurrentUser.userIsLogin()) {
            goodsDetailsVO.getGoodsDetailsVO().setMemberAvatar(SecurityCurrentUser.getUserDetail().getMemberAvatar());
        }


        // 活动商品提醒
        List<SpecActivityVO> specActivityList = goodsDetailsVO.getGoodsDetailsVO().getSpecActivityList();
        if (CollectionUtils.isNotEmpty(specActivityList)) {
            for (SpecActivityVO specActivity : specActivityList) {
                // 是否提醒
                Map<String, Object> queryParams = new HashMap<>(10);
                queryParams.put("activityId", specActivity.getActivityId().toString());
                queryParams.put("activityType", ActivityTypeEnum.SECKILL_ACTIVITY.value());
                queryParams.put("goodsId", goodsDetailsVO.getGoodsDetailsVO().getGoodsId().toString());
                queryParams.put("memberId", memberId.toString());
                List<String> remindJsonList = nosqlService.queryData(ActivityMongoConstants.ACTIVITY_GOODS_REMIND, queryParams);
                if (CollectionUtils.isNotEmpty(remindJsonList)) {
                    // 已设置提醒
                    specActivity.setRemindFlag(1);
                } else {
                    // 未设置提醒
                    specActivity.setRemindFlag(0);
                }
            }
        }
        return new Result<OptimizeSpecGoodsDetailVO>().ok(goodsDetailsVO);
    }


    /**
     * 搜索店铺
     *
     * @param storeSearchDTO 搜索店铺
     * @return
     */
    @PostMapping("store")
    @ApiOperation("搜索店铺")
    public Result<StoreSearchFilterVO> searchStore(@RequestBody SearchStoreDTO storeSearchDTO) {
        if (StringUtils.isBlank(storeSearchDTO.getSortField())) {
            //默认排序字段
            storeSearchDTO.setSortField("storeFavNum");
        }
        if (storeSearchDTO.getPageSize() == null) {
            //默认条数
            storeSearchDTO.setPageSize(10);
        }
        if (storeSearchDTO.getPageNo() == null) {
            //默认页数
            storeSearchDTO.setPageNo(1);
        }
        StoreSearchFilterVO searchStoreDTO = goodsSearchService.storeSerarch(storeSearchDTO);
        return new Result<StoreSearchFilterVO>().ok(searchStoreDTO);
    }

}
