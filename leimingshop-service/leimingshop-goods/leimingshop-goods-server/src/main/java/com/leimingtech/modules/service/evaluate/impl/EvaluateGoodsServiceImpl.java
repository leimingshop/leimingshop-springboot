/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.service.evaluate.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.leimingtech.commons.mybatis.service.impl.CrudServiceImpl;
import com.leimingtech.commons.tools.constant.Constant;
import com.leimingtech.commons.tools.page.PageData;
import com.leimingtech.commons.tools.utils.ConvertUtils;
import com.leimingtech.commons.tools.utils.DataMaskingUtils;
import com.leimingtech.dto.setting.SettingSeniorDTO;
import com.leimingtech.dto.setting.reward.rule.GrowthRuleSettingDTO;
import com.leimingtech.dto.setting.reward.rule.MoreRuleSettingDTO;
import com.leimingtech.dto.setting.reward.rule.PointRuleSettingDTO;
import com.leimingtech.enums.SettingsEnum;
import com.leimingtech.modules.constant.order.OrderTimeConstants;
import com.leimingtech.modules.dao.evaluate.EvaluateGoodsDao;
import com.leimingtech.modules.dto.evaluate.EvaluateGoodsDTO;
import com.leimingtech.modules.dto.evaluate.FindEvaluateGoodsDTO;
import com.leimingtech.modules.dto.evaluate.H5EvaluateGoodsDTO;
import com.leimingtech.modules.dto.evaluate.UpdateEvaluateReadStateDTO;
import com.leimingtech.modules.dto.goods.GoodsDTO;
import com.leimingtech.modules.dto.log.point.PointLogPackDTO;
import com.leimingtech.modules.dto.member.MemberDTO;
import com.leimingtech.modules.dto.order.OrderDTO;
import com.leimingtech.modules.dto.order.OrderGoodsDTO;
import com.leimingtech.modules.dto.store.StoreDTO;
import com.leimingtech.modules.entity.evaluate.EvaluateGoodsEntity;
import com.leimingtech.modules.enums.EvaluateEnum;
import com.leimingtech.modules.enums.evaluate.GoodsEnum;
import com.leimingtech.modules.enums.point.MemberPointLogEnum;
import com.leimingtech.modules.service.evaluate.EvaluateGoodsService;
import com.leimingtech.modules.service.goods.GoodsInfoService;
import com.leimingtech.modules.service.goods.GoodsService;
import com.leimingtech.modules.service.log.point.PointLogService;
import com.leimingtech.modules.service.member.MemberService;
import com.leimingtech.modules.service.order.OrderGoodsService;
import com.leimingtech.modules.service.order.OrderService;
import com.leimingtech.modules.service.store.StoreService;
import com.leimingtech.modules.vo.evaluate.EvaluateGoodsInfoVO;
import com.leimingtech.modules.vo.member.MemberVoDTO;
import com.leimingtech.mq.constant.MqConstant;
import com.leimingtech.mq.service.RabbitMqSendService;
import com.leimingtech.service.setting.PointSettingService;
import com.leimingtech.service.setting.SettingService;
import com.leimingtech.service.stopword.StopWordService;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 商品评价管理
 *
 * @author chengqian
 * @email 2634443725@gmail.com
 * @since 7.0 2019-05-15
 */
@Service

public class EvaluateGoodsServiceImpl extends CrudServiceImpl<EvaluateGoodsDao, EvaluateGoodsEntity, EvaluateGoodsDTO> implements EvaluateGoodsService {
    @Resource
    private EvaluateGoodsDao evaluateGoodsDao;
    @Resource
    private OrderGoodsService orderGoodsService;
    @Autowired
    private MemberService memberService;
    @Autowired
    private SettingService settingService;
    @Autowired
    private GoodsService goodsService;

    @Autowired
    private RabbitMqSendService rabbitMqSendService;

    @Autowired
    private PointSettingService pointSettingService;

    @Autowired
    private PointLogService pointLogService;

    @Autowired
    private GoodsInfoService goodsInfoService;

    @Autowired
    private StoreService storeService;

    @Autowired
    private StopWordService stopWordService;

    @Autowired
    private OrderService orderService;


    /**
     * 条件构造器
     *
     * @param params 分页参数
     */
    @Override
    public QueryWrapper<EvaluateGoodsEntity> getWrapper(Map<String, Object> params) {
        String goodsName = (String) params.get("goodsName");
        String replyState = (String) params.get("replyState");
        String startDate = (String) params.get("startDate");
        String endDate = (String) params.get("endDate");
        String storeId = (String) params.get("storeId");
        String evaluateState = (String) params.get("evaluateState");
        String readState = (String) params.get("readState");
        QueryWrapper<EvaluateGoodsEntity> wrapper = new QueryWrapper<>();
        wrapper.like(StringUtils.isNotBlank(goodsName), "goods_name", goodsName);
        wrapper.ge(StringUtils.isNotBlank(startDate), Constant.CREATE_DATE, startDate);
        wrapper.le(StringUtils.isNotBlank(endDate), Constant.CREATE_DATE, endDate);
        wrapper.eq(StringUtils.isNotBlank(evaluateState), "evaluate_state", evaluateState);
        wrapper.eq(StringUtils.isNotBlank(readState), "read_state", readState);
        wrapper.eq(StringUtils.isNotBlank(storeId), "store_id", storeId);
        if (StringUtils.isNotBlank(replyState) && String.valueOf(GoodsEnum.REPLY_STATE_YES.value()).equals(replyState)) {
            wrapper.isNotNull("reply_content");
        } else if (StringUtils.isNotBlank(replyState) && String.valueOf(GoodsEnum.REPLY_STATE_NO.value()).equals(replyState)) {
            wrapper.isNull("reply_content");
        }
        wrapper.orderByAsc("read_state");
        return wrapper;
    }

    /**
     * seller 端 分页查询当前店铺商品评价 条件构造器
     *
     * @param params 分页参数
     * @return
     */
    @Override

    public PageData<EvaluateGoodsDTO> storePage(@RequestParam Map<String, Object> params) {

        IPage<EvaluateGoodsEntity> page = this.baseDao.selectPage(this.getPage(params,
                Constant.CREATE_DATE, false),
                this.getFeignWrapper(params));
        return this.getPageData(page, this.currentDtoClass());
    }

    /**
     * 保存商品评价（导入版）
     *
     * @param list 商品评价集合
     */
    @Override

    public void saveEvaluate(@RequestBody List<EvaluateGoodsDTO> list) {
        //遍历解析到的评价list信息
        List<EvaluateGoodsEntity> evaluateGoodsEntityList = new ArrayList<>();
        for (EvaluateGoodsDTO evaluateGoodsDTO : list) {
            EvaluateGoodsEntity evaluateGoods = new EvaluateGoodsEntity();
            if (evaluateGoodsDTO.getGoodsId() != null) {
//                    根据商品id获得商品信息
                GoodsDTO goods = goodsService.get(evaluateGoodsDTO.getGoodsId());
                evaluateGoods.setStoreId(goods.getStoreId());
                evaluateGoods.setStoreName(goods.getStoreName());

            }
            MemberDTO memberDTO = memberService.selectMemberByUserName(evaluateGoodsDTO.getMemberName());
            //保存商品id
            evaluateGoods.setGoodsId(evaluateGoodsDTO.getGoodsId());
            //评价分数
            evaluateGoods.setEvaluateScores(Integer.valueOf(evaluateGoodsDTO.getEvaluateScores()));
            //商品名称
            evaluateGoods.setGoodsName(evaluateGoodsDTO.getGoodsName());
            //评价内容
            evaluateGoods.setEvaluateContent(evaluateGoodsDTO.getEvaluateContent());
            //评价者名称
            evaluateGoods.setMemberName(evaluateGoodsDTO.getMemberName());
            //评价者id
            evaluateGoods.setMemberId(memberDTO.getId());
            //0表示不是 1表示是匿名评价
            evaluateGoods.setIsAnonymous(Integer.valueOf(evaluateGoodsDTO.getIsAnonymous()));
            //评价信息的状态 0为正常 1为禁止显示
            evaluateGoods.setEvaluateState(Integer.valueOf(evaluateGoodsDTO.getEvaluateState()));
            //todo 保存订单id,编号，订单商品ID
            evaluateGoodsEntityList.add(evaluateGoods);

        }
        //保存评价信息
        insertBatch(evaluateGoodsEntityList);

    }

    /**
     * 新增商品评价
     *
     * @param evaluateGoodsDTO 商品评价实体
     */

    @Override

    public Long saveEvaluateGoods(@RequestBody EvaluateGoodsDTO evaluateGoodsDTO) {

        EvaluateGoodsEntity evaluateGoodsEntity = ConvertUtils.sourceToTarget(evaluateGoodsDTO, EvaluateGoodsEntity.class);
        // 根据订单商品id查询商品信息
        OrderGoodsDTO orderGoodsDTO = orderGoodsService.getById(evaluateGoodsDTO.getOrderGoodsId(), null, null);
        MemberDTO memberDTO = memberService.getById(evaluateGoodsDTO.getMemberId());
        evaluateGoodsEntity.setGoodsId(orderGoodsDTO.getGoodsId());
        evaluateGoodsEntity.setMemberAvatar(memberDTO.getMemberAvatar());
        evaluateGoodsEntity.setGoodsName(orderGoodsDTO.getGoodsName());
        evaluateGoodsEntity.setOrderSn(orderGoodsDTO.getOrderSn());
        evaluateGoodsEntity.setGoodsSpecId(orderGoodsDTO.getSpecId());
        evaluateGoodsEntity.setStoreId(orderGoodsDTO.getStoreId());
        evaluateGoodsEntity.setOrderId(orderGoodsDTO.getOrderId());
        evaluateGoodsEntity.setOrderGoodsId(orderGoodsDTO.getId());
        evaluateGoodsEntity.setSpecInfo(orderGoodsDTO.getSpecAttrValueName());
        evaluateGoodsEntity.setGoodsPrice(orderGoodsDTO.getSpecPrice());
        evaluateGoodsEntity.setStoreName(orderGoodsDTO.getStoreName());
        evaluateGoodsEntity.setSpecMainPicture(orderGoodsDTO.getGoodsImage());
        evaluateGoodsEntity.setReadState(GoodsEnum.EVALUATE_STATE.value());
        //处理评价内容
        String content = stopWordService.replaceStopWord(evaluateGoodsEntity.getEvaluateContent());
        evaluateGoodsEntity.setEvaluateContent(content);
        evaluateGoodsDao.insert(evaluateGoodsEntity);


        Map<String, Object> memberParams = new HashMap<>(10);
        memberParams.put("memberId", evaluateGoodsEntity.getMemberId());
        memberParams.put("memberName", evaluateGoodsEntity.getMemberName());
        memberParams.put("sourceType", MemberPointLogEnum.FIRST_EVALUATE_ORDER_SOURCE_TYPE.code());
        memberParams.put("pointDesc", MemberPointLogEnum.FIRST_EVALUATE_ORDER_SOURCE_TYPE.value());
        memberService.savePoint(memberParams);

        String evaluateImage = evaluateGoodsEntity.getEvaluateImage();

        int evaluateImageNumber = evaluateImage.length() - evaluateImage.replace(",", "").length() + 1;
        // 评价完成计算积分和成长值
        Integer evaluateNumber = 0;
        if (StringUtils.isNotBlank(evaluateGoodsEntity.getEvaluateContent())) {
            evaluateNumber = evaluateGoodsEntity.getEvaluateContent().length();
        }

        this.savePoint(evaluateNumber, evaluateImageNumber, evaluateGoodsEntity.getGoodsPrice(), evaluateGoodsEntity.getMemberId(), evaluateGoodsEntity.getMemberName());

        orderGoodsDTO.setEvaluationTime(new Date());
        orderGoodsDTO.setEvaluationStatus(EvaluateEnum.EVALUATE_YES.intValue());
        orderGoodsService.updateOrderGoods(orderGoodsDTO);
        // 更新订单表状态
        orderGoodsService.updateOrderEvaluateStatus(orderGoodsDTO.getOrderId());

        String message = JSONObject.toJSONString(orderGoodsDTO.getGoodsId());
        rabbitMqSendService.sendMsg(MqConstant.LEIMINGTECH_UPDATE_EVALUATE_COUNT_QUEUE, message);
        return evaluateGoodsEntity.getId();
    }

    /**
     * 条件构造器seller端
     *
     * @param params 分页查询参数
     * @return
     */
    private Wrapper<EvaluateGoodsEntity> getFeignWrapper(@RequestParam Map<String, Object> params) {

        //todo 查询条件增加店铺Id
        QueryWrapper<EvaluateGoodsEntity> wrapper = new QueryWrapper<>();
        String evaluateScores = (String) params.get("evaluateScores");
        String readState = (String) params.get("readState");
        String isContent = (String) params.get("isContent");

        if (StringUtils.isNotBlank(evaluateScores)) {
            // evaluateScores ==1 的时候为差评
            wrapper.in(StringUtils.equals(String.valueOf(GoodsEnum.EVALUATE_SCORES_ONE.value()), evaluateScores), "evaluate_scores", 1, 0);
            // evaluateScores ==3 的时候为中评
            wrapper.in(StringUtils.equals(String.valueOf(GoodsEnum.IS_CONTENT.value()), evaluateScores), "evaluate_scores", 3, 2);
            // evaluateScores ==5 的时候为好评
            wrapper.in(StringUtils.equals(String.valueOf(GoodsEnum.IS_CONTENT.value()), evaluateScores), "evaluate_scores", 5, 4);
        }

        //isContent=1时有评价内容
        wrapper.isNotNull(StringUtils.equals(String.valueOf(GoodsEnum.IS_CONTENT.value()), isContent), "evaluate_content");
        //isContent=0时没有评价内容
        wrapper.isNull(StringUtils.equals(String.valueOf(GoodsEnum.IS_NOT_CONTENT.value()), isContent), "evaluate_content");
        wrapper.eq("evaluate_state", GoodsEnum.EVALUATE_STATE.value());
        wrapper.eq(StringUtils.isNotBlank(readState), "read_state", readState);
        wrapper.orderByAsc("read_state");


        return wrapper;
    }

    /**
     * 分页查询商品评价
     *
     * @param params 商品评价实体
     * @return
     */
    @Override

    public PageData<EvaluateGoodsDTO> page(@RequestParam Map<String, Object> params) {

        return super.page(params);
    }

    /**
     * 根据ID删除商品评价
     *
     * @param ids 商品评价ID
     */

    @Override
    public void delete(@RequestBody Long[] ids) {
        super.delete(ids);
    }


    /***
     *
     * 根据ID查询
     * @param id 评价Id 或者 订单id
     * @return
     */
    @Override

    public EvaluateGoodsDTO findById(@PathVariable(value = "id", required = false) Long id,
                                     @RequestParam(value = "orderGoodsId", required = false) Long orderGoodsId) {
        EvaluateGoodsDTO evaluateGoodsDTO = baseDao.findById(id, orderGoodsId);
        MemberVoDTO memberVoDTO = memberService.selectMemberById(evaluateGoodsDTO.getMemberId());
        evaluateGoodsDTO.setNickName(memberVoDTO.getNickName());
        evaluateGoodsDTO.setMemberGrade(memberVoDTO.getGradeName());
        // 查询店铺头像
        StoreDTO storeDTO = storeService.get(evaluateGoodsDTO.getStoreId());
        evaluateGoodsDTO.setStoreLogo(storeDTO.getStoreLogo());
        return evaluateGoodsDTO;
    }


    /**
     * 更新信息
     *
     * @param dto 实体
     */
    @Override

    public void update(@RequestBody EvaluateGoodsDTO dto) {
        super.update(dto);
    }

    /**
     * 批量显示或者隐藏信息
     *
     * @param ids   主键ID
     * @param state 状态  0 显示   1 隐藏
     */
    @Override

    public void batchUpdate(@RequestBody Long[] ids, @RequestParam("state") int state) {
        List<EvaluateGoodsEntity> evaluateGoodsEntityList = new ArrayList<>();
        EvaluateGoodsEntity evaluateGoodsEntity = null;
        for (long id : ids) {
            evaluateGoodsEntity = new EvaluateGoodsEntity();
            evaluateGoodsEntity.setId(id);
            evaluateGoodsEntity.setEvaluateState(state);
            //显示或隐藏时修改读取状态为已读
            evaluateGoodsEntity.setReadState(GoodsEnum.READ_YES.value());
            evaluateGoodsEntityList.add(evaluateGoodsEntity);
        }
        updateBatchById(evaluateGoodsEntityList);


    }


    /**
     * f分页查询当前用户已评价的商品（front）
     *
     * @param params 查询参数
     * @return
     */
    @Override

    public PageData<FindEvaluateGoodsDTO> evaluatePage(@RequestParam Map<String, Object> params) {

        //分页
        IPage<EvaluateGoodsEntity> page = getPage(params, "create_date", false);
        //查询
        List<FindEvaluateGoodsDTO> list = baseDao.findPage(params);
        list.stream().forEach(e -> {
            StoreDTO storeDTO = storeService.get(e.getStoreId());
            e.setStoreLogo(storeDTO == null ? "" : storeDTO.getStoreLogo());
            MemberVoDTO memberDTO = memberService.selectMemberById(e.getMemberId());
            e.setMemberGrade(memberDTO != null && memberDTO.getMemberInfoDTO() != null ? memberDTO.getMemberInfoDTO().getGradeName() : "");
            e.setMemberName(DataMaskingUtils.mobileEncrypt(e.getMemberName()));
        });
        return new PageData<>(list, page.getTotal());
    }

    /**
     * 计算出好评率
     *
     * @param goodsId
     * @return
     */

    @Override
    public String reputably(@RequestParam("goodsId") Long goodsId) {


        return baseDao.reputably(goodsId);
    }

    /**
     * 订单已完成，自动评价定时任务
     */

    @Override
    public void saveEvaluateTime() {
        String data = settingService.queryRedisByName(SettingsEnum.SENIOR.value());
        SettingSeniorDTO seniorSet = JSON.parseObject(data, SettingSeniorDTO.class);
        if (seniorSet != null) {
            // 获取自动评价时间
            String autoComment = seniorSet.getAutoComment();
            Integer cancelOrder = Integer.valueOf(autoComment);
            long cancelOrderLong = OrderTimeConstants.DAY * cancelOrder;
            // 当前时间时间戳
            Date nowDate = new Date();
            long nowLong = nowDate.getTime();
            //获取可取消订单的时间
            Long delLong = nowLong - cancelOrderLong;
            Date delDate = new Date(delLong);
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String createTime = df.format(delDate);
            // 查询出所有可评价的订单商品
            List<OrderGoodsDTO> orderGoodsDTOList = orderGoodsService.findOverTimeOrder(createTime);

            // 评价集合
            List<EvaluateGoodsEntity> evaluateGoodsDTOList = new ArrayList<>();
            // 遍历订单商品去评价
            if (CollectionUtils.isNotEmpty(orderGoodsDTOList)) {
                for (OrderGoodsDTO orderGoodsDTO : orderGoodsDTOList) {
                    EvaluateGoodsEntity evaluateGoodsDTO = new EvaluateGoodsEntity();
                    MemberVoDTO memberVoDTO = memberService.selectMemberById(orderGoodsDTO.getBuyerId());
                    evaluateGoodsDTO.setEvaluateScores(EvaluateEnum.EVALUATE_SCORES.intValue());
                    evaluateGoodsDTO.setGoodsId(orderGoodsDTO.getGoodsId());
                    evaluateGoodsDTO.setGoodsName(orderGoodsDTO.getGoodsName());
                    evaluateGoodsDTO.setGoodsSpecId(orderGoodsDTO.getSpecId());
                    evaluateGoodsDTO.setStoreId(orderGoodsDTO.getStoreId());
                    evaluateGoodsDTO.setOrderId(orderGoodsDTO.getOrderId());
                    evaluateGoodsDTO.setOrderGoodsId(orderGoodsDTO.getId());
                    evaluateGoodsDTO.setMemberId(orderGoodsDTO.getBuyerId());
                    evaluateGoodsDTO.setSpecInfo(orderGoodsDTO.getSpecAttrValueName());
                    evaluateGoodsDTO.setGoodsPrice(orderGoodsDTO.getSpecPrice());
                    evaluateGoodsDTO.setOrderSn(orderGoodsDTO.getOrderSn());
                    evaluateGoodsDTO.setStoreName(orderGoodsDTO.getStoreName());
                    evaluateGoodsDTO.setMemberAvatar(memberVoDTO.getMemberAvatar());
                    evaluateGoodsDTO.setMemberName(memberVoDTO.getMemberName());
                    evaluateGoodsDTO.setSpecMainPicture(orderGoodsDTO.getGoodsImage());
                    evaluateGoodsDTOList.add(evaluateGoodsDTO);
                    orderGoodsDTO.setEvaluationStatus(EvaluateEnum.EVALUATE_YES.intValue());
                    orderGoodsDTO.setEvaluationTime(new Date());
                    goodsInfoService.updateEvalusteNum(orderGoodsDTO.getGoodsId());
                }
                insertBatch(evaluateGoodsDTOList);
                orderGoodsService.updateBetch(orderGoodsDTOList);

            }
        }
    }


    @Override
    public List<EvaluateGoodsDTO> list(@RequestParam Map<String, Object> params) {
        return super.list(params);
    }

    /**
     * @param evaluateNumber: 用户评价字数
     * @param evaluateImages: 用户评价图片数
     * @param goodsPrice:     商品单价
     * @param memberId:       用户id
     * @param memberName:     用户名称
     * @return void
     * @Description 用户评价完成增加对应的积分和成长值
     * @Author huangkeyuan
     * @Date 16:08 2019-12-26
     */
    private void savePoint(Integer evaluateNumber, Integer evaluateImages, BigDecimal goodsPrice, Long memberId, String memberName) {
        String pointSetting = pointSettingService.findFromRedis("morerule");
        MoreRuleSettingDTO moreRuleSettingDTO = JSONObject.parseObject(pointSetting, MoreRuleSettingDTO.class);

        GrowthRuleSettingDTO growthRuleSettingDTO = moreRuleSettingDTO.getGrowthRule();

        PointRuleSettingDTO pointRuleSettingDTO = moreRuleSettingDTO.getPointRule();

        //integer装换为bigDecimal
        BigDecimal goodsPriceMinGrowth = new BigDecimal(growthRuleSettingDTO.getGoodsPriceMinGrowth().toString());

        BigDecimal goodsPriceMinPoint = new BigDecimal(pointRuleSettingDTO.getGoodsPriceMinGrowth().toString());


        if (goodsPrice.compareTo(goodsPriceMinGrowth) > 0 && goodsPrice.compareTo(goodsPriceMinPoint) > 0) {

            Integer finalPoint = 0;

            Integer finalGrowth = 0;

            // 计算评价字数成长值
            if (evaluateNumber > growthRuleSettingDTO.getEvaluationWordCount()) {
                finalGrowth = growthRuleSettingDTO.getEvaluationWordGrowth();
            }

            // 计算评价图片成长值
            if (evaluateImages > growthRuleSettingDTO.getEvaluationPicCount()) {
                finalGrowth = finalGrowth + growthRuleSettingDTO.getEvaluationPicGrowth();
            }

            // 计算评价字数积分
            if (evaluateNumber > pointRuleSettingDTO.getEvaluationWordCount()) {
                finalPoint = pointRuleSettingDTO.getEvaluationWordGrowth();
            }

            // 计算评价图片积分
            if (evaluateImages > pointRuleSettingDTO.getEvaluationPicCount()) {
                finalPoint = finalPoint + pointRuleSettingDTO.getEvaluationPicGrowth();
            }

            //当积分值大于0时才进行积分保存增加
            PointLogPackDTO pointLogPackDTO = new PointLogPackDTO(
                    memberId,
                    memberName,
                    finalPoint.intValue(),
                    finalGrowth.intValue(),
                    MemberPointLogEnum.MORE_RULES_EVALUATE.value(),
                    MemberPointLogEnum.MORE_RULES_EVALUATE.code(),
                    MemberPointLogEnum.INSERT_ALL.code());

            pointLogService.packPointLog(pointLogPackDTO);


        } else if (goodsPrice.compareTo(goodsPriceMinGrowth) > 0 && goodsPrice.compareTo(goodsPriceMinPoint) <= 0) {

            Integer finalGrowth = 0;

            // 计算成长值
            if (evaluateNumber > growthRuleSettingDTO.getEvaluationWordCount()) {
                finalGrowth = growthRuleSettingDTO.getEvaluationWordGrowth();
            }

            if (evaluateImages > growthRuleSettingDTO.getEvaluationPicCount()) {
                finalGrowth = finalGrowth + growthRuleSettingDTO.getEvaluationPicGrowth();
            }

            PointLogPackDTO pointLogPackDTO = new PointLogPackDTO(
                    memberId,
                    memberName,
                    0,
                    finalGrowth.intValue(),
                    MemberPointLogEnum.MORE_RULES_EVALUATE.value(),
                    MemberPointLogEnum.MORE_RULES_EVALUATE.code(),
                    MemberPointLogEnum.INSERT_GROWTH.code());

            pointLogService.packPointLog(pointLogPackDTO);

        } else if (goodsPrice.compareTo(goodsPriceMinGrowth) <= 0 && goodsPrice.compareTo(goodsPriceMinPoint) > 0) {

            Integer finalPoint = 0;

            // 计算积分
            if (evaluateNumber > pointRuleSettingDTO.getEvaluationWordCount()) {
                finalPoint = pointRuleSettingDTO.getEvaluationWordGrowth();
            }

            if (evaluateImages > pointRuleSettingDTO.getEvaluationPicCount()) {
                finalPoint = finalPoint + pointRuleSettingDTO.getEvaluationPicGrowth();
            }

            if (finalPoint > 0) {

                PointLogPackDTO pointLogPackDTO = new PointLogPackDTO(
                        memberId,
                        memberName,
                        finalPoint.intValue(),
                        0,
                        MemberPointLogEnum.MORE_RULES_EVALUATE.value(),
                        MemberPointLogEnum.MORE_RULES_EVALUATE.code(),
                        MemberPointLogEnum.INSERT_POINT.code());

                pointLogService.packPointLog(pointLogPackDTO);
            }

        }
    }

    /**
     * 功能描述:
     * <批量修改读取状态或者违规删除状态>
     *
     * @return
     * @date 2020/3/16 17:56
     * @author weixianchun
     **/

    @Override
    public boolean updateState(@RequestBody UpdateEvaluateReadStateDTO evaluateReadStateDTO) {

        List<EvaluateGoodsEntity> evaluateGoodsEntities = new ArrayList<>();
        for (Long id : evaluateReadStateDTO.getIds()) {
            EvaluateGoodsEntity evaluateGoodsEntity = new EvaluateGoodsEntity();
            evaluateGoodsEntity.setId(id);
            evaluateGoodsEntity.setReadState(evaluateReadStateDTO.getReadState());
            evaluateGoodsEntity.setIllegalDel(evaluateReadStateDTO.getIllegalDel());
            evaluateGoodsEntity.setEvaluateState(evaluateReadStateDTO.getState());
            if (evaluateReadStateDTO.getIllegalDel() != null) {
                evaluateGoodsEntity.setEvaluateState(evaluateReadStateDTO.getIllegalDel());
            }

            if (StringUtils.isNotBlank(evaluateReadStateDTO.getReplyCount())) {
                evaluateGoodsEntity.setReplyContent(evaluateReadStateDTO.getReplyCount());
                evaluateGoodsEntity.setReplyDate(new Date());
            }

            evaluateGoodsEntities.add(evaluateGoodsEntity);
        }
        return updateBatchById(evaluateGoodsEntities);
    }

    /**
     * pc端评价列表
     *
     * @param params
     * @return
     */

    @Override
    public H5EvaluateGoodsDTO pcEvaluatePage(@RequestParam Map<String, Object> params) {
        H5EvaluateGoodsDTO h5EvaluateGoodsDTO = baseDao.selectEvaCount(params);
        PageData<FindEvaluateGoodsDTO> findEvaluateGoodsPageDataDTO = this.evaluatePage(params);
        String reputably = this.reputably(Long.valueOf(params.get("goodsId").toString()));
        h5EvaluateGoodsDTO.setPage(findEvaluateGoodsPageDataDTO);
        h5EvaluateGoodsDTO.setReputably(StringUtils.isNotBlank(reputably) ? reputably : "100");
        return h5EvaluateGoodsDTO;
    }

    /**
     * 根据订单id查询已评价信息
     *
     * @param orderId
     * @return
     */

    @Override
    public List<EvaluateGoodsInfoVO> pcEvaInfo(@RequestParam("orderId") Long orderId) {

        List<EvaluateGoodsInfoVO> evaluateGoodsDTOList = baseDao.pcEvaInfo(orderId);

        if (CollectionUtils.isNotEmpty(evaluateGoodsDTOList)) {
            StoreDTO storeDTO = storeService.get(evaluateGoodsDTOList.get(0).getStoreId());
            evaluateGoodsDTOList.stream().forEach(e -> {
                e.setStoreLogo(storeDTO.getStoreLogo());
                OrderDTO orderDTO = orderService.get(e.getOrderId());
                e.setCreateOrderDate(orderDTO.getCreateDate());
                if (StringUtils.isNotBlank(e.getEvaluateImage())) {
                    String[] split = e.getEvaluateImage().split(",");
                    e.setEvaluateImg(split);
                }
            });
        }
        return evaluateGoodsDTOList;
    }

    /**
     * 根据goodsid查询当前商品的评价数量
     *
     * @param goodsId
     * @return
     */

    @Override
    public Integer findEvaluateNum(@RequestParam("goodsId") Long goodsId) {
        Integer evaluateNum = baseDao.findEvaluateNum(goodsId);
        return evaluateNum == null ? 0 : evaluateNum;
    }
}
