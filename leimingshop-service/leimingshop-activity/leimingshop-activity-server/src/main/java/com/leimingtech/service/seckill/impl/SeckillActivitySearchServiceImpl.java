/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.service.seckill.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.leimingtech.commons.tools.constant.Constant;
import com.leimingtech.commons.tools.page.PageData;
import com.leimingtech.commons.tools.redis.RedisUtils;
import com.leimingtech.commons.tools.utils.DateUtils;
import com.leimingtech.dto.setting.SettingSeckillDTO;
import com.leimingtech.exception.ServiceException;
import com.leimingtech.modules.constant.ActivityMongoConstants;
import com.leimingtech.modules.constant.ActivityRedisConstants;
import com.leimingtech.modules.constant.ElasticSearchConstant;
import com.leimingtech.modules.dto.PageModelDTO;
import com.leimingtech.modules.dto.seckill.*;
import com.leimingtech.modules.enums.ActivityTypeEnum;
import com.leimingtech.modules.goods.activity.SpecActivityVO;
import com.leimingtech.modules.service.NosqlService;
import com.leimingtech.modules.service.seckill.SeckillActivitySearchService;
import com.leimingtech.modules.statuscode.ActivityStatusCode;
import com.leimingtech.modules.utils.EsDataUtils;
import com.leimingtech.service.setting.SettingService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;
import java.util.*;

/**
 * <秒杀活动搜索管理>
 *
 * @author 刘远杰
 * @email 2634443725@qq.com
 * @since 1.0 2020/3/13
 */
@Slf4j
@Service

public class SeckillActivitySearchServiceImpl implements SeckillActivitySearchService {

    private static final String SESSIONID = "sessionId";

    private static final String SPEC_ACTIVITY_LIST = "specActivityList";

    private static final String ACTIVITY_DAY = "activityDay";

    @Autowired
    private SettingService settingService;

    @Autowired
    private RedisUtils redisUtils;

    @Autowired
    private EsDataUtils esDataUtils;

    @Autowired
    private NosqlService nosqlService;

    /**
     * 功能描述：
     * <正在进行的秒杀场次信息>
     *
     * @param
     * @return
     * @date 2020/3/13 11:06
     * @author 刘远杰
     **/
    @Override

    public StartSeckillSessionDTO startSeckillSession() {
        StartSeckillSessionDTO startSeckillSession = new StartSeckillSessionDTO();
        // 1.查询秒杀广告
        SettingSeckillDTO seckillSetting = settingService.getSeckillSetting();
        startSeckillSession.setAdvImageUrl(seckillSetting.getAdvImageUrl());
        startSeckillSession.setAdvUrl(seckillSetting.getAdvUrl());

        // 2.查询正在秒杀场次
        Object obj = null;
        try {
            obj = redisUtils.get(ActivityRedisConstants.SECKILL_SESSION);
        } catch (Exception e) {
            log.error("查询缓存秒杀场次异常，{}", e);
            throw new ServiceException(ActivityStatusCode.FIND_CACHE_SECKILL_SESSION_EXCEPTION);
        }
        if (obj == null) {
            log.info("未获取到秒杀场次");
            return startSeckillSession;
        }

        // 秒杀场次集合
        List<SeckillSessionDTO> sessionDTOList = JSONArray.parseArray(obj.toString(), SeckillSessionDTO.class);
        // 获取下一场未秒杀场次
        for (int i = sessionDTOList.size() - 1; i >= 0; i--) {
            if (!sessionDTOList.get(i).getActivityStartDate().after(new Date())) {
                // 开始时间早于或等于当前时间 场次已开始
                startSeckillSession.setSessionId(sessionDTOList.get(i).getId());
                startSeckillSession.setActivityStartDate(sessionDTOList.get(i).getActivityStartDate());
                if (i + 1 <= sessionDTOList.size() - 1) {
                    // 下一场开始时间设置为本场结束时间
                    startSeckillSession.setActivityEndDate(sessionDTOList.get(i + 1).getActivityStartDate());
                } else {
                    startSeckillSession.setActivityEndDate(sessionDTOList.get(i).getActivityEndDate());
                }
                break;
            }
        }
        return startSeckillSession;
    }

    /**
     * 功能描述：
     * <即将开始的秒杀场次信息>
     *
     * @param
     * @return
     * @date 2020/3/13 11:06
     * @author 刘远杰
     **/
    @Override

    public List<PrestartSeckillSessionDTO> prestartSeckillSessionList() {
        List<PrestartSeckillSessionDTO> prestartSeckillSessionList = new ArrayList<>();

        // 1.查询正在秒杀场次
        Object obj = null;
        try {
            obj = redisUtils.get(ActivityRedisConstants.SECKILL_SESSION);
        } catch (Exception e) {
            log.error("查询缓存秒杀场次异常，{}", e);
            throw new ServiceException(ActivityStatusCode.FIND_CACHE_SECKILL_SESSION_EXCEPTION);
        }
        if (obj == null) {
            return prestartSeckillSessionList;
        }

        // 秒杀场次集合
        List<SeckillSessionDTO> sessionDTOList = JSONArray.parseArray(obj.toString(), SeckillSessionDTO.class);
        // 获取下一场未秒杀场次
        Date now = new Date();
        Date end = DateUtils.parse(DateUtils.format(DateUtils.addDateDays(now, 1), DateUtils.DATE_PATTERN), DateUtils.DATE_PATTERN);
        for (int i = 0; i <= sessionDTOList.size() - 1; i++) {
            if (sessionDTOList.get(i).getActivityStartDate().after(now) && sessionDTOList.get(i).getActivityStartDate().before(end)) {
                PrestartSeckillSessionDTO prestartSeckillSession = new PrestartSeckillSessionDTO();
                prestartSeckillSession.setSessionId(sessionDTOList.get(i).getId());
                prestartSeckillSession.setActivityStartDate(sessionDTOList.get(i).getActivityStartDate());
                prestartSeckillSessionList.add(prestartSeckillSession);
            } else if (!sessionDTOList.get(i).getActivityStartDate().before(end)) {
                break;
            }
        }
        return prestartSeckillSessionList;
    }

    /**
     * 功能描述：
     * <秒杀更多预告天数列表>
     *
     * @param
     * @return
     * @date 2020/3/15 19:53
     * @author 刘远杰
     **/
    @Override

    public List<MoreDaysSeckillDTO> morePrestartSeckillSessionList() {
        List<MoreDaysSeckillDTO> preStartDays = new ArrayList<>();

        // 1.获取秒杀预设天数
        SettingSeckillDTO seckillSetting = settingService.getSeckillSetting();
        if (seckillSetting == null) {
            log.info("获取秒杀设置失败");
            return preStartDays;
        }
        Integer presetDays = seckillSetting.getPresetDays();

        if (presetDays == 0) {
            // 不预设
            log.info("秒杀预设天数为0天");
            return preStartDays;
        }

        Date now = new Date();
        // 封装日期集合
        for (int i = 1; i <= presetDays; i++) {
            MoreDaysSeckillDTO moreDaysSeckill = new MoreDaysSeckillDTO();
            moreDaysSeckill.setActivityDay(DateUtils.format(DateUtils.addDateDays(now, i), DateUtils.DATE_PATTERN));
            moreDaysSeckill.setActivityDayStr(DateUtils.format(DateUtils.addDateDays(now, i), "MM.dd"));
            preStartDays.add(moreDaysSeckill);
        }
        return preStartDays;
    }

    /**
     * 功能描述：
     * <正在进行的秒杀场次分页>
     *
     * @param
     * @return
     * @date 2020/3/13 11:06
     * @author 刘远杰
     **/
    @Override

    public PageData<SeckillGoodsPageDTO> startSeckillGoodsPage(@RequestParam Map<String, Object> params) {
        int page = Integer.parseInt(params.get(Constant.PAGE).toString());
        int limit = Integer.parseInt(params.get(Constant.LIMIT).toString());
        List<SeckillGoodsPageDTO> seckillGoodsList = new ArrayList<>();
        String sessionIds = (String) params.get(SESSIONID);
        if (StringUtils.isBlank(sessionIds)) {
            log.error("秒杀SessionId，参数错误{}", params.get("sessionId"));
            return new PageData<SeckillGoodsPageDTO>(seckillGoodsList, 0L);
        }
        Long sessionId = Long.parseLong(params.get("sessionId").toString());
        // 查询指定场次商品分页集合
        List<String> jsonRsList = new ArrayList<>();
        Long total = getGoodsBysessionId(page, limit, sessionId, jsonRsList);

        if (CollectionUtils.isNotEmpty(jsonRsList)) {
            // 封装进行中或已经开始场次商品分页数据
            jsonRsList.forEach(jsonRs -> createSeckillGoodsListForStart(Collections.singletonList(sessionId), seckillGoodsList, jsonRs));
        }
        return new PageData<>(seckillGoodsList, total);
    }

    /**
     * 功能描述：
     * <即将售罄秒杀商品列表>
     *
     * @param params
     * @return
     * @date 2020/3/16 10:51
     * @author 刘远杰
     **/
    @Override

    public PageData<SeckillGoodsPageDTO> preendSeckillGoodsPage(@RequestParam Map<String, Object> params) {
        List<Long> sessionIds = new ArrayList<>();
        // 1.获取即将结束场次id
        Long sessionId = Long.parseLong(params.get("sessionId").toString());
        // 查询redis秒杀场次
        Object obj = redisUtils.get(ActivityRedisConstants.SECKILL_SESSION);
        if (obj == null) {
            log.info("未获取到秒杀场次,查询秒杀商品为空");
            return new PageData<>(Collections.emptyList(), 0);
        }

        List<SeckillSessionDTO> sessionList = JSONArray.parseArray(obj.toString(), SeckillSessionDTO.class);
        for (int i = 0; i <= sessionList.size() - 1; i++) {
            // 匹配查询日期所有场次 不等于当前场次id则为即将开始秒杀
            if (!sessionList.get(i).getId().equals(sessionId)) {
                sessionIds.add(sessionList.get(i).getId());
            } else {
                break;
            }
        }

        if (CollectionUtils.isEmpty(sessionIds)) {
            log.info("未获取到秒杀场次,查询秒杀商品未空");
            return new PageData<>(Collections.emptyList(), 0);
        }

        int page = Integer.parseInt(params.get(Constant.PAGE).toString());
        int limit = Integer.parseInt(params.get(Constant.LIMIT).toString());
        // 查询指定场次商品分页集合
        List<String> jsonRsList = new ArrayList<>();
        Long total = getGoodsBysessionIds(page, limit, sessionIds, jsonRsList, 1);

        List<SeckillGoodsPageDTO> seckillGoodsList = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(jsonRsList)) {
            // 封装进行中或已经开始场次商品分页数据
            jsonRsList.forEach(jsonRs -> createSeckillGoodsListForStart(sessionIds, seckillGoodsList, jsonRs));
        }
        return new PageData<>(seckillGoodsList, total);
    }

    /**
     * 功能描述：
     * <即将开始的秒杀场次分页>
     *
     * @param params   查询条件
     * @param memberId 会员id
     * @return
     * @date 2020/3/15 20:29
     * @author 刘远杰
     **/
    @Override

    public PageData<PrestartSeckillGoodsPageDTO> seckillGoodsPage(@RequestParam Map<String, Object> params,
                                                                  @RequestParam(value = "memberId", required = false) Long memberId) {
        List<Long> sessionIds = new ArrayList<>();
        if (params.get(ACTIVITY_DAY) != null && StringUtils.isNotBlank(params.get(ACTIVITY_DAY).toString())) {
            String activityDay = params.get("activityDay").toString();
            // 1.获取查询日期开始的所有场次
            // 查询redis秒杀场次
            Object obj = redisUtils.get(ActivityRedisConstants.SECKILL_SESSION);
            if (obj == null) {
                log.info("未获取到秒杀场次,查询秒杀商品未空");
                return new PageData<>(Collections.emptyList(), 0);
            }
            List<SeckillSessionDTO> sessionList = JSONArray.parseArray(obj.toString(), SeckillSessionDTO.class);
            for (int i = 0; i <= sessionList.size() - 1; i++) {
                // 匹配查询日期所有场次
                if (DateUtils.format(sessionList.get(i).getActivityStartDate(), DateUtils.DATE_PATTERN).equals(activityDay)) {
                    sessionIds.add(sessionList.get(i).getId());
                } else if (!sessionList.get(i).getActivityStartDate().before(DateUtils.addDateDays(DateUtils.parse(activityDay, DateUtils.DATE_PATTERN), 1))) {
                    break;
                }
            }
        } else if (params.get(SESSIONID) != null && StringUtils.isNotBlank(params.get(SESSIONID).toString())) {
            Long sessionId = Long.parseLong(params.get("sessionId").toString());
            sessionIds.add(sessionId);
        }

        if (CollectionUtils.isEmpty(sessionIds)) {
            log.info("未获取到秒杀场次,查询秒杀商品未空");
            return new PageData<>(Collections.emptyList(), 0);
        }

        // 2.查询指定场次集合商品分页集合
        int page = Integer.parseInt(params.get(Constant.PAGE).toString());
        int limit = Integer.parseInt(params.get(Constant.LIMIT).toString());
        List<String> jsonRsList = new ArrayList<>();
        Long total = getGoodsBysessionIds(page, limit, sessionIds, jsonRsList, 0);

        List<PrestartSeckillGoodsPageDTO> preStartSeckillGoodsList = new ArrayList<>();
        // 3.页面展示数据封装
        if (CollectionUtils.isNotEmpty(jsonRsList)) {
            jsonRsList.forEach(jsonRs -> {
                // spec
                JSONObject spec = JSONObject.parseObject(jsonRs);
                PrestartSeckillGoodsPageDTO seckillGoods = new PrestartSeckillGoodsPageDTO();
                seckillGoods.setId(spec.get("goodsId") != null ? Long.parseLong(spec.get("goodsId").toString()) : null);
                seckillGoods.setSpecId(spec.get("id") != null ? Long.parseLong(spec.get("id").toString()) : null);
                seckillGoods.setSpecSellPrice(spec.get("specSellPrice") != null ? new BigDecimal(spec.get("specSellPrice").toString()).setScale(2, BigDecimal.ROUND_DOWN) : null);

                JSONObject mainSpec = JSONObject.parseObject(spec.toString());
                if (mainSpec.get(SPEC_ACTIVITY_LIST) != null) {
                    List<SpecActivityVO> specActivityList = JSONArray.parseArray(mainSpec.get("specActivityList").toString(), SpecActivityVO.class);
                    for (SpecActivityVO specActivityVO : specActivityList) {
                        if (sessionIds.contains(specActivityVO.getSessionId()) && specActivityVO.getActivityType().equals(ActivityTypeEnum.SECKILL_ACTIVITY.value())) {
                            seckillGoods.setActivityPrice(specActivityVO.getActivityPrice().setScale(2, BigDecimal.ROUND_DOWN));
                            seckillGoods.setActivityStartDate(specActivityVO.getActivityStartDate());
                            seckillGoods.setSessionId(specActivityVO.getSessionId());
                            seckillGoods.setActivityId(specActivityVO.getActivityId());
                            seckillGoods.setRestrictionQuantity(specActivityVO.getRestrictionQuantity());

                            // 是否提醒
                            Map<String, Object> queryParams = new HashMap<>();
                            queryParams.put("activityId", specActivityVO.getActivityId());
                            queryParams.put("activityType", ActivityTypeEnum.SECKILL_ACTIVITY.value());
                            queryParams.put("goodsId", Long.parseLong(spec.get("goodsId").toString()));
                            queryParams.put("memberId", memberId);
                            List<String> remindJsonList = nosqlService.queryData(ActivityMongoConstants.ACTIVITY_GOODS_REMIND, queryParams);
                            if (CollectionUtils.isNotEmpty(remindJsonList) && memberId != null) {
                                // 已设置提醒
                                seckillGoods.setRemindFlag(1);
                            } else {
                                // 未设置提醒
                                seckillGoods.setRemindFlag(0);
                            }
                            break;
                        }
                    }
                }

                // 商品名称图片获取
                String goodsJson = esDataUtils.getDateById(ElasticSearchConstant.GOODS_INDEX, seckillGoods.getId().toString());
                JSONObject goods = JSONObject.parseObject(goodsJson);
                seckillGoods.setGoodsName(goods.get("goodsName") != null ? goods.get("goodsName").toString() : "");
                seckillGoods.setGoodsMainPicture(goods.get("goodsMainPicture") != null ? goods.get("goodsMainPicture").toString() : "");
                seckillGoods.setStoreType(goods.get("storeType") != null ? Integer.parseInt(goods.get("storeType").toString()) : null);
                seckillGoods.setGoodsSubTitle(goods.get("goodsSubTitle") != null ? goods.get("goodsSubTitle").toString() : "");

                preStartSeckillGoodsList.add(seckillGoods);
            });
        }
        return new PageData<>(preStartSeckillGoodsList, total);
    }

    /**
     * 功能描述：
     * <查询指定场次商品分页集合>
     *
     * @param page      页码
     * @param limit     每页记录数
     * @param sessionId 场次id
     * @return
     * @date 2020/3/13 15:54
     * @author 刘远杰
     **/
    private Long getGoodsBysessionId(int page, int limit, Long sessionId, List<String> jsonRsList) {
        PageModelDTO pageModelDTO = new PageModelDTO();
        Map<String, Map<String, Object>> subEqualsSearchCondition = pageModelDTO.getSubEqualsSearchCondition();
        Map<String, Object> equalsSearchCondition = pageModelDTO.getEqualsSearchCondition();
        Map<String, String> sortFileds = pageModelDTO.getSortFileds();

        // 分页
        pageModelDTO.setIsPage(true);
        pageModelDTO.setPageNum(page);
        pageModelDTO.setPageSize(limit);

        // 同场次添加顺序正序排序
        sortFileds.put("specActivityList.id", "asc");

        // 查询场次id为指定场次的秒杀活动商品
        equalsSearchCondition.put("specShow", 1);
        Map<String, Object> subParams = new HashMap<>();
        subParams.put("sessionId", sessionId);
        subParams.put("activityType", ActivityTypeEnum.SECKILL_ACTIVITY.value());
        subEqualsSearchCondition.put("specActivityList", subParams);

        pageModelDTO.setCollapseField("goodsId");
        esDataUtils.queryData(pageModelDTO, ElasticSearchConstant.GOODS_SPEC_INDEX);

        jsonRsList.addAll(pageModelDTO.getJsonRsList());
        return pageModelDTO.getTotal() != null ? pageModelDTO.getTotal() : 0;
    }

    /**
     * 功能描述：
     * <查询指定场次集合商品分页集合>
     *
     * @param page       页码
     * @param limit      每页记录数
     * @param sessionIds 场次id集合
     * @param sortType   排序规则
     * @return
     * @date 2020/3/13 15:54
     * @author 刘远杰
     **/
    private Long getGoodsBysessionIds(int page, int limit, List<Long> sessionIds, List<String> jsonRsList, Integer sortType) {
        PageModelDTO pageModelDTO = new PageModelDTO();
        Map<String, Map<String, Object>> subEqualsSearchCondition = pageModelDTO.getSubEqualsSearchCondition();
        Map<String, Map<String, Object[]>> subInSearchCondition = pageModelDTO.getSubInSearchCondition();
        Map<String, Object> equalsSearchCondition = pageModelDTO.getEqualsSearchCondition();
        Map<String, String> sortFileds = pageModelDTO.getSortFileds();

        // 分页
        pageModelDTO.setIsPage(true);
        pageModelDTO.setPageNum(page);
        pageModelDTO.setPageSize(limit);

        if (sortType == 0) {
            // 场次开始时间正序排序
            sortFileds.put("specActivityList.activityStartDate", "asc");
            // 同场次添加顺序正序排序
            sortFileds.put("specActivityList.id", "asc");
        } else if (sortType == 1) {
            // 结束时间正序排序
            sortFileds.put("specActivityList.activityEndDate", "asc");
            // 同场次添加顺序正序排序
            sortFileds.put("specActivityList.id", "asc");
        }

        // 查询场次id为指定场次的秒杀活动商品
        equalsSearchCondition.put("specShow", 1);
        Map<String, Object> subEqualParams = new HashMap<>();
        subEqualParams.put("activityType", ActivityTypeEnum.SECKILL_ACTIVITY.value());
        subEqualsSearchCondition.put("specActivityList", subEqualParams);
        Map<String, Object[]> subInParams = new HashMap<>();
        subInParams.put("sessionId", sessionIds.toArray());
        subInSearchCondition.put("specActivityList", subInParams);

        pageModelDTO.setCollapseField("goodsId");
        esDataUtils.queryData(pageModelDTO, ElasticSearchConstant.GOODS_SPEC_INDEX);

        jsonRsList.addAll(pageModelDTO.getJsonRsList());
        return pageModelDTO.getTotal() != null ? pageModelDTO.getTotal() : 0;
    }

    /**
     * 功能描述：
     * <封装进行中或已经开始场次商品分页数据>
     *
     * @param sessionIds       场次id
     * @param seckillGoodsList 场次商品集合
     * @param jsonRs           es商品数据
     * @return
     * @date 2020/3/16 10:47
     * @author 刘远杰
     **/
    private void createSeckillGoodsListForStart(List<Long> sessionIds, List<SeckillGoodsPageDTO> seckillGoodsList, String jsonRs) {
        // spec
        JSONObject spec = JSONObject.parseObject(jsonRs);
        SeckillGoodsPageDTO seckillGoods = new SeckillGoodsPageDTO();
        seckillGoods.setId(spec.get("goodsId") != null ? Long.parseLong(spec.get("goodsId").toString()) : null);
        seckillGoods.setSpecId(spec.get("id") != null ? Long.parseLong(spec.get("id").toString()) : null);
        seckillGoods.setSpecSellPrice(spec.get("specSellPrice") != null ? new BigDecimal(spec.get("specSellPrice").toString()).setScale(2, BigDecimal.ROUND_DOWN) : null);

        JSONObject mainSpec = JSONObject.parseObject(spec.toString());
        if (mainSpec.get(SPEC_ACTIVITY_LIST) != null) {
            List<SpecActivityVO> specActivityList = JSONArray.parseArray(mainSpec.get("specActivityList").toString(), SpecActivityVO.class);
            for (SpecActivityVO specActivityVO : specActivityList) {
                if (sessionIds.contains(specActivityVO.getSessionId()) && specActivityVO.getActivityType().equals(ActivityTypeEnum.SECKILL_ACTIVITY.value())) {
                    seckillGoods.setActivityPrice(specActivityVO.getActivityPrice().setScale(2, BigDecimal.ROUND_DOWN));
                    break;
                }
            }
        }

        // 商品名称图片获取
        String goodsJson = esDataUtils.getDateById(ElasticSearchConstant.GOODS_INDEX, seckillGoods.getId().toString());
        JSONObject goods = JSONObject.parseObject(goodsJson);
        seckillGoods.setGoodsName(goods.get("goodsName") != null ? goods.get("goodsName").toString() : "");
        seckillGoods.setGoodsMainPicture(goods.get("goodsMainPicture") != null ? goods.get("goodsMainPicture").toString() : "");
        seckillGoods.setGoodsSubTitle(goods.get("goodsSubTitle") != null ? goods.get("goodsSubTitle").toString() : "");
        seckillGoodsList.add(seckillGoods);
        // 计算库存
        Object collapse = spec.get("collapse");
        Integer activityStorage = 0;
        Integer activitySurplusStorage = 0;
        if (collapse != null) {
            JSONArray specArray = JSONArray.parseArray(collapse.toString());
            for (Object goodsSpecObj : specArray) {
                JSONObject goodsSpec = JSONObject.parseObject(goodsSpecObj.toString());
                if (goodsSpec.get("specActivityList") != null) {
                    List<SpecActivityVO> specActivityList = JSONArray.parseArray(goodsSpec.get("specActivityList").toString(), SpecActivityVO.class);
                    for (SpecActivityVO specActivityVO : specActivityList) {
                        if (sessionIds.contains(specActivityVO.getSessionId()) && specActivityVO.getActivityType().equals(ActivityTypeEnum.SECKILL_ACTIVITY.value())) {
                            activityStorage += specActivityVO.getActivityStorage();
                            activitySurplusStorage += specActivityVO.getActivitySurplusStorage();
                            break;
                        }
                    }
                }
            }
        }
        seckillGoods.setActivityStorage(activityStorage);
        seckillGoods.setActivitySurplusStorage(activitySurplusStorage);
    }

}
