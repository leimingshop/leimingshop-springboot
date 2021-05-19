/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.service.goods.spec;

import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.leimingtech.commons.mybatis.enums.DelFlagEnum;
import com.leimingtech.commons.mybatis.service.impl.CrudServiceImpl;
import com.leimingtech.commons.tools.constant.Constant;
import com.leimingtech.commons.tools.page.PageData;
import com.leimingtech.commons.tools.security.user.SecurityUser;
import com.leimingtech.commons.tools.utils.ConvertUtils;
import com.leimingtech.exception.ServiceException;
import com.leimingtech.logging.MonitorLogger;
import com.leimingtech.logging.MonitorLoggerFactory;
import com.leimingtech.modules.dao.goods.spec.GoodsSpecDao;
import com.leimingtech.modules.dto.cart.UpdateCartStatusDTO;
import com.leimingtech.modules.dto.goods.GoodsDTO;
import com.leimingtech.modules.dto.goods.GoodsInfoDTO;
import com.leimingtech.modules.dto.goods.PartGoodsSpecDTO;
import com.leimingtech.modules.dto.goods.detail.GoodsMobileDTO;
import com.leimingtech.modules.dto.goods.detail.GoodsSkuDetailDTO;
import com.leimingtech.modules.dto.goods.detail.GoodsSpecDetailDTO;
import com.leimingtech.modules.dto.goods.modify.ValidSpecSerial;
import com.leimingtech.modules.dto.goods.price.*;
import com.leimingtech.modules.dto.goods.record.GoodsRecordDTO;
import com.leimingtech.modules.dto.goods.spec.*;
import com.leimingtech.modules.dto.goods.spec.list.GoodsSpecListDTO;
import com.leimingtech.modules.dto.goods.time.GoodsTimeDTO;
import com.leimingtech.modules.dto.price.UpdateBatchPriceLog;
import com.leimingtech.modules.dto.spec.GoodsSpecShowDTO;
import com.leimingtech.modules.dto.stock.UpdateBatchStockLog;
import com.leimingtech.modules.entity.goods.spec.GoodsSpecEntity;
import com.leimingtech.modules.enums.goods.CustomPictureSizeEnum;
import com.leimingtech.modules.enums.goods.GoodsSpecStatusEnum;
import com.leimingtech.modules.enums.goods.GoodsStatusEnum;
import com.leimingtech.modules.enums.goods.GoodsTimeStatusEnum;
import com.leimingtech.modules.service.activity.goods.ActivityGoodsService;
import com.leimingtech.modules.service.goods.GoodsInfoService;
import com.leimingtech.modules.service.goods.GoodsService;
import com.leimingtech.modules.service.goods.time.GoodsTimeService;
import com.leimingtech.modules.service.member.MemberService;
import com.leimingtech.modules.statuscode.GoodsStatusCode;
import com.leimingtech.mq.constant.MqConstant;
import com.leimingtech.mq.service.RabbitMqSendService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.beans.BeanCopier;
import org.springframework.retry.annotation.Recover;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @Author: weixianchun
 * @Description: 商品规格管理
 * @Date :2019/6/5 9:20
 * @Version V1.0
 **/
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class GoodsSpecServiceImpl extends CrudServiceImpl<GoodsSpecDao, GoodsSpecEntity, GoodsSpecDTO> implements GoodsSpecService {
    private static MonitorLogger mlogger = MonitorLoggerFactory.getMonitorLogger(GoodsSpecServiceImpl.class);


    @Autowired

    private GoodsService goodsService;

    @Autowired

    private SpecAttributeService specAttributeService;

    @Autowired

    private GoodsTimeService goodsTimeService;

    @Autowired

    private GoodsAttributeService goodsAttributeService;

    @Autowired

    private GoodsInfoService goodsInfoService;

    @Autowired
    private RabbitMqSendService rabbitMqSendService;

    @Autowired
    private MemberService memberService;

    @Autowired
    private ActivityGoodsService activityGoodsService;

    /**
     * @Author: weixianchun
     * @Description: 条件构造器
     * @Date :2019/6/5 9:21
     * @Param params 查询条件
     * @Version V1.0
     **/
    @Override
    public QueryWrapper<GoodsSpecEntity> getWrapper(Map<String, Object> params) {
        String id = (String) params.get("id");
        String goodsId = (String) params.get("goodsId");
        String goodsShow = (String) params.get("goodsShow");

        QueryWrapper<GoodsSpecEntity> wrapper = new QueryWrapper<>();
        wrapper.eq(StringUtils.isNotBlank(id), "id", id);
        wrapper.eq(StringUtils.isNotBlank(goodsId), "goods_id", goodsId);
        wrapper.eq(StringUtils.isNotBlank(goodsShow), "goods_show", goodsShow);
        return wrapper;
    }

    /**
     * @Author: weixianchun
     * @Description: 查询商品SKU列表并分页
     * @Date :2019/5/28 19:51
     * @Param params 查询条件
     * @Version V1.0
     **/

    @Override
    public PageData<GoodsSpecListDTO> pageSpec(@RequestParam Map<String, Object> params) {
        IPage<GoodsSpecEntity> page = getPage(params, null, false);
        List<GoodsSpecListDTO> list = baseDao.findListSku(params);
        return new PageData<>(list, page.getTotal());
    }

    /**
     * @Author: weixianchun
     * @Description: 查询所有商品规格信息
     * @Date :2019/5/28 19:51
     * @Param params 查询条件
     * @Version V1.0
     **/

    @Override
    public List<GoodsSpecDTO> list(@RequestParam Map<String, Object> params) {
        return super.list(params);
    }

    /**
     * @Author: weixianchun
     * @Description: 根据商品规格id查询信息
     * @Date :2019/5/28 19:52
     * @Param id 商品规格id
     * @Version V1.0
     **/

    @Override
    public GoodsSpecDTO get(Long id) {
        return super.get(id);
    }

    /**
     * 功能描述：
     * <根据规格id集合查询>
     *
     * @param ids 规格id集合
     * @return
     * @date 2020/3/10 17:38
     * @author 刘远杰
     **/
    @Override

    public List<GoodsSpecDTO> getByIds(@RequestBody List<Long> ids) {
        QueryWrapper<GoodsSpecEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.in("id", ids);
        List<GoodsSpecEntity> entityList = baseDao.selectList(queryWrapper);
        return ConvertUtils.sourceToTarget(entityList, GoodsSpecDTO.class);
    }

    /**
     * @Author: weixianchun
     * @Description: 批量保存商品规格信息
     * @Date :2019/5/28 19:52
     * @Param dtoList 批量保存list
     * @Version V1.0
     **/

    @Override
    public void saveBatch(@RequestBody List<GoodsSpecSaveDTO> dtoList) {

        //转换为GoodsSpecEntity
        List<GoodsSpecEntity> goodsSpecEntities = ConvertUtils.sourceToTarget(dtoList, GoodsSpecEntity.class);
        goodsSpecEntities.forEach(this::setSpecCustomPicSize);
        //批量保存
        super.insertBatch(goodsSpecEntities);

    }

    private void setSpecCustomPicSize(GoodsSpecEntity goodsSpecEntity) {
        String pictureUrl = goodsSpecEntity.getSpecMainPicture();
        if (StringUtils.isNotBlank(pictureUrl)) {
            String[] split = pictureUrl.split("\\.");
            String suffix = split[split.length - 1];
            goodsSpecEntity.setOneHundredPxPictureUrl(pictureUrl + CustomPictureSizeEnum.ONE_HUNDRED_PX.value() + suffix);
            goodsSpecEntity.setTwoHundredPxPictureUrl(pictureUrl + CustomPictureSizeEnum.TWO_HUNDRED_PX.value() + suffix);
            goodsSpecEntity.setFourHundredPxPictureUrl(pictureUrl + CustomPictureSizeEnum.FOUR_HUNDRED_PX.value() + suffix);
            goodsSpecEntity.setEightHundredPxPictureUrl(pictureUrl + CustomPictureSizeEnum.EIGHT_HUNDRED_PX.value() + suffix);
        }
    }

    /**
     * @Author: weixianchun
     * @Description: 修改sku信息
     * @Date :2019/6/7 1:53
     * @Param dto 实体
     * @Version V1.0
     **/

    @Override
    public Long update(@RequestBody GoodsSpecUpdateSkuDTO dto) {
        GoodsSpecEntity goodsSpecEntity = baseDao.selectById(dto.getId());
        List<Long> goodsIds = new ArrayList<>();
        goodsIds.add(goodsSpecEntity.getGoodsId());

        Boolean activityFlag = activityGoodsService.updateGoodsActivityHandle(goodsIds);
        if (!activityFlag) {
            throw new ServiceException(GoodsStatusCode.GOODS_ACTIVITY_START_ERROR);
        }
        //修改商品规格
        baseDao.updateById(ConvertUtils.sourceToTarget(dto, GoodsSpecEntity.class));
        List<Long> ids = new ArrayList<>();
        ids.add(dto.getId());

        //修改默认规格价格库存
        if (StringUtils.isBlank(goodsSpecEntity.getSpecAttrName())) {
            goodsService.updatePriceBySpecId(dto.getId(),
                    dto.getSpecSellPrice(), dto.getSpecCostPrice());
        }
        String userName = SecurityUser.getUserName();

        //判断是否修改价格
        if (goodsSpecEntity.getSpecSellPrice().compareTo(dto.getSpecSellPrice()) != 0 ||
                goodsSpecEntity.getSpecCostPrice().compareTo(dto.getSpecCostPrice()) != 0) {
            List<GoodsSpecPriceUpdateDTO> goodsSpecPriceUpdateDTOList = new ArrayList<>();
            GoodsSpecPriceUpdateDTO goodsSpecPriceUpdateDTO = new GoodsSpecPriceUpdateDTO();
            goodsSpecPriceUpdateDTO.setId(dto.getId());
            goodsSpecPriceUpdateDTO.setSpecSerial(goodsSpecEntity.getSpecSerial());
            goodsSpecPriceUpdateDTO.setSpecCostPrice(dto.getSpecCostPrice());
            goodsSpecPriceUpdateDTO.setBeforeSpecSellPrice(goodsSpecEntity.getSpecSellPrice());
            goodsSpecPriceUpdateDTO.setBeforeSpecCostPrice(goodsSpecEntity.getSpecCostPrice());
            goodsSpecPriceUpdateDTO.setSpecSellPrice(dto.getSpecSellPrice());
            goodsSpecPriceUpdateDTOList.add(goodsSpecPriceUpdateDTO);


            UpdateBatchPriceLog updateBatchPriceLog = new UpdateBatchPriceLog();
            updateBatchPriceLog.setGoodsSpecPriceUpdateDTOList(goodsSpecPriceUpdateDTOList);
            updateBatchPriceLog.setUserName(userName);
            rabbitMqSendService.sendMsg(MqConstant.LEIMINGTECH_PRICE_RECORD_QUEUE, JSON.toJSONString(updateBatchPriceLog));
        }
        //判断是否修改库存

        if (!goodsSpecEntity.getSpecStorage().equals(dto.getSpecStorage())) {
            List<GoodsSpecStorageUpdateDTO> goodsSpecStorageUpdateDTOList = new ArrayList<>();
            GoodsSpecStorageUpdateDTO goodsSpecStorageUpdateDTO = new GoodsSpecStorageUpdateDTO();
            goodsSpecStorageUpdateDTO.setId(dto.getId());
            goodsSpecStorageUpdateDTO.setSpecSerial(goodsSpecEntity.getSpecSerial());
            goodsSpecStorageUpdateDTO.setSpecStorage(dto.getSpecStorage());
            goodsSpecStorageUpdateDTO.setBeforeSpecStorage(goodsSpecEntity.getSpecStorage());
            goodsSpecStorageUpdateDTOList.add(goodsSpecStorageUpdateDTO);

            UpdateBatchStockLog updateBatchStockLog = new UpdateBatchStockLog();
            updateBatchStockLog.setGoodsSpecStorageUpdateDTOList(goodsSpecStorageUpdateDTOList);
            updateBatchStockLog.setUserName(userName);

            rabbitMqSendService.sendMsg(MqConstant.LEIMINGTECH_STOCK_RECORD_QUEUE,
                    JSON.toJSONString(updateBatchStockLog));
        }
        //购物车同步
        List<UpdateCartStatusDTO> updateCartStatusDTOList = new ArrayList<>();
        UpdateCartStatusDTO updateCartStatusDTO = updateCart(userName,
                goodsSpecEntity.getId(), goodsSpecEntity.getSpecShow(),
                dto.getSpecStorage(), dto.getSpecSellPrice());
        updateCartStatusDTOList.add(updateCartStatusDTO);
        rabbitMqSendService.sendMsg(MqConstant.LEIMINGTECH_CART_GOODS_STATUS,
                JSON.toJSONString(updateCartStatusDTOList));
        Map<String, String> logMap = new HashMap<>(10);
        logMap.put("SKU_ID为", dto.getId() + "");
        mlogger.info(GoodsStatusCode.SKU_UPDATE_OPERATION_CODE, GoodsStatusCode.SKU_UPDATE_OPERATION_SUCCESS_MESSAGE, logMap);
        return goodsSpecEntity.getGoodsId();

    }

    /**
     * @Author: weixianchun
     * @Description: 根据商品规格id删除商品规格信息
     * @Date :2019/5/28 19:53
     * @Param ids 商品规格id
     * @Version V1.0
     **/

    @Override
    public void delete(@RequestBody Long[] ids) {
        super.delete(ids);
    }


    /**
     * 根据商品ID查询商品规格集合
     *
     * @param goodsId 商品id
     * @return
     */

    @Override
    public List<GoodsSpecDetailDTO> selectByGoodsId(Long goodsId) {
        return baseDao.selectByGoodsId(goodsId);
    }

    /**
     * @Author: weixianchun
     * @Description: 根据商品id修改商品规格上下架状态
     * @Date :2019/6/13 21:50
     * @Param goodsId 商品id
     * @Param specShow 规格状态
     * @Version V1.0
     **/

    @Override
    public void updateShowByGoodsId(@RequestParam("goodsId") Long goodsId, @RequestParam("specShow") int specShow) {

        // 根据商品id修改商品规格上下架状态
        baseDao.updateShowByGoodsId(goodsId, specShow);
        String userName = SecurityUser.getUserName();
        List<UpdateCartStatusDTO> updateCartStatusDTOList = new ArrayList<>();
        GoodsDTO goodsDTO = goodsService.get(goodsId);
        // 根据商品id查询规格详细信息
        List<GoodsSpecDetailDTO> goodsSpecDetailDTOList = baseDao.selectByGoodsId(goodsId);

        goodsSpecDetailDTOList.forEach(goodsSpecDetailDTO -> {
                    UpdateCartStatusDTO updateCartStatusDTO = updateCart(userName, goodsSpecDetailDTO.getId(), specShow,
                            goodsSpecDetailDTO.getSpecStorage(), goodsSpecDetailDTO.getSpecSellPrice());
                    updateCartStatusDTOList.add(updateCartStatusDTO);
                }
        );

        // 发送MQ修改购物车商品上下架
        rabbitMqSendService.sendMsg(MqConstant.LEIMINGTECH_CART_GOODS_STATUS, JSON.toJSONString(updateCartStatusDTOList));

        List<GoodsRecordDTO> list = new ArrayList<>();
        GoodsRecordDTO goodsRecordDTO = new GoodsRecordDTO();
        goodsRecordDTO.setGoodsId(goodsId);
        goodsRecordDTO.setGoodsShow(specShow);
        list.add(goodsRecordDTO);

        // 发送MQ消息保存商品上下架保存记录
        rabbitMqSendService.sendMsg(MqConstant.LEIMINGTECH_GOODS_SHOW_QUEUE, JSON.toJSONString(list));
        Map<String, String> logMap = new HashMap<>(10);
        logMap.put("商品Id为", goodsId + "");
        mlogger.info(GoodsStatusCode.SPEC_SHOW_UPDATE_OPERATION_CODE, GoodsStatusCode.SPEC_SHOW_UPDATE_OPERATION_SUCCESS_BY_GOODSID_MESSAGE, logMap);
    }

    /**
     * @Author: weixianchun
     * @Description: 根据规格id修改上下架状态(定时)
     * @Date :2019/6/14 10:02
     * @Param id 规格id
     * @Version V1.0
     **/

    @Override
    public List<Long> updateShowTiming() {

        //查询所有可执行的定时任务
        List<GoodsTimeDTO> goodsTimeDTOList = goodsTimeService.selectList(new Date(), GoodsTimeStatusEnum.SPEC.value());
        if (CollectionUtils.isNotEmpty(goodsTimeDTOList)) {
            List<Long> specIds = new ArrayList<>();
            List<Long> goodsIds = new ArrayList<>();
            for (GoodsTimeDTO goodsTimeDTO : goodsTimeDTOList) {
                if (goodsTimeDTO.getShowStatus() == GoodsStatusEnum.GOODS_SHOW_ON.value()) {
                    Boolean flag = goodsService.validatePublishGoodsNum(new Long[]{goodsTimeDTO.getGoodsId()});
                    if (!flag) {
                        continue;
                    }
                }
                specIds.add(goodsTimeDTO.getSpecId());
                goodsIds.add(goodsTimeDTO.getGoodsId());
                GoodsSpecEntity goodsSpecEntity = baseDao.selectById(goodsTimeDTO.getSpecId());
                goodsSpecEntity.setSpecShow(goodsTimeDTO.getShowStatus());
                baseDao.updateShowBySpecId(goodsTimeDTO.getSpecId(), goodsSpecEntity.getSpecShow());
                String userName = SecurityUser.getUserName();
                List<UpdateCartStatusDTO> updateCartStatusDTOList = new ArrayList<>();
                UpdateCartStatusDTO updateCartStatusDTO = new UpdateCartStatusDTO();
                updateCartStatusDTO.setUpdateName(userName);
                updateCartStatusDTO.setSpecId(goodsTimeDTO.getSpecId());
                updateCartStatusDTO.setStatus(goodsSpecEntity.getSpecShow());
                updateCartStatusDTO.setSpecStorage(goodsSpecEntity.getSpecStorage());
                updateCartStatusDTO.setSpecSellPrice(goodsSpecEntity.getSpecSellPrice());
                updateCartStatusDTOList.add(updateCartStatusDTO);
                rabbitMqSendService.sendMsg(MqConstant.LEIMINGTECH_CART_GOODS_STATUS,
                        JSON.toJSONString(updateCartStatusDTOList));
                updateSpecShow(goodsSpecEntity.getSpecShow(), goodsSpecEntity.getGoodsId());
            }
            //删除定时表中数据
            goodsTimeService.deleteBySpecIds(specIds);
            HashMap<String, String> logMap = new HashMap<>(10);
            logMap.put("规格Id为", specIds.toString());
            mlogger.info(GoodsStatusCode.SPEC_SHOW_UPDATE_OPERATION_CODE, GoodsStatusCode.SPEC_SHOW_UPDATE_OPERATION_SUCCESS_MESSAGE, logMap);
            return goodsIds;
        }
        return null;
    }

    /**
     * 判断操作
     * id   规格id
     */
    private void updateSpecShow(int specShow, Long goodsId) {
        if (specShow == GoodsSpecStatusEnum.GOODS_SPEC_SHOW_UP.value()) {
            //上架  规格上架 商品上架
            goodsService.updateShowNow(goodsId, specShow);
        } else {
            //下架   不存在上架规格  商品下架
            QueryWrapper<GoodsSpecEntity> wrapper = new QueryWrapper<>();
            wrapper.eq("goods_id", goodsId);
            wrapper.eq(Constant.DEL_FLAG, DelFlagEnum.NORMAL.value());
            wrapper.eq("spec_show", GoodsStatusEnum.GOODS_SHOW_ON.value());
            Integer count = baseDao.selectCount(wrapper);
            if (count == 0) {
                goodsService.updateShowNow(goodsId, specShow);
            }
        }
    }


    /**
     * 填充定时表字段  上下架操作
     */
    private GoodsTimeDTO fillGoodsTimeShowType(GoodsSpecShowDTO dto) {

        GoodsTimeDTO goodsTimeDTO = new GoodsTimeDTO();
        goodsTimeDTO.setShelfTime(dto.getShelfTime());
        goodsTimeDTO.setOperationalStatus(GoodsTimeStatusEnum.GOODS_TIME_STATUS_SHOW.value());
        return goodsTimeDTO;
    }

    /**
     * 修改规格信息
     *
     * @param dto
     */

    @Override
    public void updateSpec(GoodsSpecSaveDTO dto) {

        GoodsSpecEntity goodsSpecEntity = ConvertUtils.sourceToTarget(dto, GoodsSpecEntity.class);
        goodsSpecEntity.setSpecShow(GoodsSpecStatusEnum.GOODS_SPEC_SHOW_WAIT.value());
        this.setSpecCustomPicSize(goodsSpecEntity);
        baseDao.updateById(goodsSpecEntity);
    }

    /**
     * 根据规格ID 查询规格详情
     *
     * @param specId 规格id
     * @return
     */

    @Override
    public GoodsSkuDetailDTO getBySpecId(Long specId) {
        //根据规格ID 查询规格详情
        GoodsSkuDetailDTO goodsSkuDetailDTO = baseDao.selectSkuDetailById(specId);
        //根据商品id查询商品信息
        GoodsDTO goodsDTO = goodsService.get(goodsSkuDetailDTO.getGoodsId());
        //通过分类id获取分类名称
        goodsSkuDetailDTO.setFirstGcName(StringUtils.isNotBlank(goodsDTO.getFirstGcName()) ? goodsDTO.getFirstGcName() : "");
        goodsSkuDetailDTO.setSecondGcName(StringUtils.isNotBlank(goodsDTO.getSecondGcName()) ? goodsDTO.getSecondGcName() : "");
        goodsSkuDetailDTO.setThirdGcName(StringUtils.isNotBlank(goodsDTO.getThirdGcName()) ? goodsDTO.getThirdGcName() : "");
        return goodsSkuDetailDTO;
    }

    /**
     * 商品规格价格和库存列表
     *
     * @param goodsId 商品id
     * @return
     */

    @Override
    public GoodsSpecPriceAndStorageListDTO selectListByGoodsId(Long goodsId) {

        GoodsSpecPriceAndStorageListDTO goodsSpecPriceAndStorageListDTO = new GoodsSpecPriceAndStorageListDTO();
        //查询商品规格的价格和库存
        List<GoodsSpecPriceAndStorageDTO> goodsSpecPriceAndStorageDTOList = baseDao.selectListByGoodsId(goodsId);
        if (CollectionUtils.isEmpty(goodsSpecPriceAndStorageDTOList)) {
            goodsSpecPriceAndStorageDTOList = baseDao.selectDefaultByGoodsId(goodsId);
        }
        //查询商品的规格属性名称
        List<SpecAttrNameDTO> specAttrNameDTOList = specAttributeService.getNameListByGoodsId(goodsId);

        // 活动库存
        Map<Long, Integer> activityGoodsSurplusStorageMap = activityGoodsService.getActivityGoodsSurplusStorage(goodsId);
        if (CollectionUtils.isNotEmpty(goodsSpecPriceAndStorageDTOList)) {
            for (GoodsSpecPriceAndStorageDTO goodsSpecPriceAndStorageDTO : goodsSpecPriceAndStorageDTOList) {
                if (activityGoodsSurplusStorageMap != null && activityGoodsSurplusStorageMap.size() > 0) {
                    goodsSpecPriceAndStorageDTO.setSpecActivityStorage(activityGoodsSurplusStorageMap.getOrDefault(goodsSpecPriceAndStorageDTO.getId(), 0));
                } else {
                    goodsSpecPriceAndStorageDTO.setSpecActivityStorage(0);
                }
            }
        }

        //封装数据并返回
        goodsSpecPriceAndStorageListDTO.setGoodsSpecPriceAndStorageDTOList(goodsSpecPriceAndStorageDTOList);
        goodsSpecPriceAndStorageListDTO.setSpecAttrNameDTOList(specAttrNameDTOList);
        return goodsSpecPriceAndStorageListDTO;
    }

    /**
     * 批量保存规格价格
     *
     * @param goodsSpecPriceUpdateDTOList 批量保存规格价格list
     */

    @Override
    public Long updateSpecPrice(@RequestBody List<GoodsSpecPriceUpdateDTO> goodsSpecPriceUpdateDTOList) {
        List<GoodsSpecEntity> goodsSpecEntities = ConvertUtils.sourceToTarget(goodsSpecPriceUpdateDTOList, GoodsSpecEntity.class);
        //索引添加
        GoodsSpecEntity specEntity = baseDao.selectById(goodsSpecEntities.get(0).getId());
        Long goodsId = specEntity.getGoodsId();
        if (CollectionUtils.isNotEmpty(goodsSpecEntities) && goodsSpecEntities.size() == 1) {
            if (StringUtils.isNotBlank(specEntity.getSpecAttrName()) && StringUtils.isNotBlank(specEntity.getSpecAttrValueName())) {
                GoodsDTO goodsDTO = new GoodsDTO();
                goodsDTO.setId(goodsId);
                goodsDTO.setSpecCostPrice(specEntity.getSpecCostPrice());
                goodsDTO.setSpecSellPrice(specEntity.getSpecSellPrice());
                goodsService.updateGoods(goodsDTO);
            }
        }
        List<Long> goodsIds = new ArrayList<>();
        goodsIds.add(goodsId);
        Boolean activityFlag = activityGoodsService.updateGoodsActivityHandle(goodsIds);
        if (!activityFlag) {
            throw new ServiceException(GoodsStatusCode.GOODS_ACTIVITY_START_ERROR);
        }

        super.updateBatchById(goodsSpecEntities);
        String userName = SecurityUser.getUserName();
        List<UpdateCartStatusDTO> updateCartStatusDTOList = new ArrayList<>();
        for (GoodsSpecEntity goodsSpecEntity : goodsSpecEntities) {
            UpdateCartStatusDTO updateCartStatusDTO = updateCart(userName,
                    goodsSpecEntity.getId(), goodsSpecEntity.getSpecShow(),
                    goodsSpecEntity.getSpecStorage(), goodsSpecEntity.getSpecSellPrice());
            updateCartStatusDTOList.add(updateCartStatusDTO);
        }
        //规格索引同步

        //同步购物车
        rabbitMqSendService.sendMsg(MqConstant.LEIMINGTECH_CART_GOODS_STATUS,
                JSON.toJSONString(updateCartStatusDTOList));
        //默认规格修改
        if (goodsSpecEntities.size() == 1 && StringUtils.isBlank(goodsSpecEntities.get(0).getSpecAttrName())) {
            goodsService.updatePriceBySpecId(goodsSpecEntities.get(0).getId(),
                    goodsSpecEntities.get(0).getSpecSellPrice(),
                    goodsSpecEntities.get(0).getSpecCostPrice());

        }
        UpdateBatchPriceLog updateBatchPriceLog = new UpdateBatchPriceLog();
        updateBatchPriceLog.setGoodsSpecPriceUpdateDTOList(goodsSpecPriceUpdateDTOList);
        updateBatchPriceLog.setUserName(userName);
        rabbitMqSendService.sendMsg(MqConstant.LEIMINGTECH_PRICE_RECORD_QUEUE,
                JSON.toJSONString(updateBatchPriceLog));

        mlogger.info(GoodsStatusCode.GOODS_SPEC_PRICE_UPDATE_OPERATION_CODE, GoodsStatusCode.GOODS_SPEC_PRICE_UPDATE_OPERATION_SUCCESS_MESSAGE);
        return goodsId;
    }

    /**
     * 批量保存规格库存
     *
     * @param goodsSpecStorageUpdateDTOList 批量保存规格库存list
     */

    @Override
    public void updateSpecStorageLog(@RequestBody List<GoodsSpecStorageUpdateDTO> goodsSpecStorageUpdateDTOList) {

        List<GoodsSpecEntity> goodsSpecEntities = ConvertUtils.sourceToTarget(goodsSpecStorageUpdateDTOList, GoodsSpecEntity.class);
        GoodsSpecEntity specEntity = baseDao.selectById(goodsSpecEntities.get(0).getId());
        Long goodsId = specEntity.getGoodsId();
        List<Long> goodsIds = new ArrayList<>();
        goodsIds.add(goodsId);
        Boolean activityFlag = activityGoodsService.updateGoodsActivityHandle(goodsIds);
        if (!activityFlag) {
            throw new ServiceException(GoodsStatusCode.GOODS_ACTIVITY_START_ERROR);
        }

        String userName = SecurityUser.getUserName();
        super.updateBatchById(goodsSpecEntities);
        //索引
        UpdateBatchStockLog updateBatchStockLog = new UpdateBatchStockLog();
        updateBatchStockLog.setGoodsSpecStorageUpdateDTOList(goodsSpecStorageUpdateDTOList);
        updateBatchStockLog.setUserName(userName);
        updateCartStorage(goodsSpecStorageUpdateDTOList, userName);
        rabbitMqSendService.sendMsg(MqConstant.LEIMINGTECH_STOCK_RECORD_QUEUE,
                JSON.toJSONString(updateBatchStockLog));
        mlogger.info(GoodsStatusCode.GOODS_SPEC_STORAGE_UPDATE_OPERATION_CODE, GoodsStatusCode.GOODS_SPEC_STORAGE_UPDATE_OPERATION_SUCCESS_MESSAGE);

    }

    /**
     * 更新购物车库存
     *
     * @param goodsSpecStorageUpdateDTOList
     */
    private void updateCartStorage(List<GoodsSpecStorageUpdateDTO> goodsSpecStorageUpdateDTOList, String userName) {
        List<UpdateCartStatusDTO> cartStatusDTOList = new ArrayList<>();
        goodsSpecStorageUpdateDTOList.stream().forEach(g -> {
            UpdateCartStatusDTO updateCartStatusDTO = updateCart(userName,
                    g.getId(), null,
                    g.getSpecStorage(), null);
            cartStatusDTOList.add(updateCartStatusDTO);
        });
        rabbitMqSendService.sendMsg(MqConstant.LEIMINGTECH_CART_GOODS_STATUS,
                JSON.toJSONString(cartStatusDTOList));

    }

    /**
     * 校验SKU是否重复
     *
     * @param specSerial
     */

    @Override
    public List<String> checkSpecSerial(@RequestBody ValidSpecSerial specSerial) {
        List<GoodsSpecEntity> goodsSpecEntities = new ArrayList<>();
        if (specSerial.getGoodsId() != null) {
            goodsSpecEntities = baseDao.selectList(new QueryWrapper<GoodsSpecEntity>().ne("goods_id", specSerial.getGoodsId())
                    .in("spec_serial", specSerial.getSerials()));
        } else {
            goodsSpecEntities = baseDao.selectList(new QueryWrapper<GoodsSpecEntity>().in("spec_serial", specSerial.getSerials()));
        }
        if (CollectionUtils.isNotEmpty(goodsSpecEntities)) {
            Set<String> serialsSet = new HashSet<>();
            for (GoodsSpecEntity goodsSpecEntity : goodsSpecEntities) {
                serialsSet.add(goodsSpecEntity.getSpecSerial());
            }
            List<String> serials = new ArrayList<>(serialsSet);
            return serials;
        }
        return Collections.emptyList();
    }

    /**
     * 功能描述:
     * 〈根据规格id查询商品〉
     *
     * @param specId 规格ID
     * @author : 许志成
     */

    @Override
    public GoodsMobileDTO findMobeilGoodsInfo(Long specId) {
        GoodsMobileDTO goodsMobileDTO = baseDao.findMobeilGoodsInfo(specId);
        Long goodsId = goodsMobileDTO.getGoodsId();
        List<GoodsAttributeInfoDTO> goodsAttributeInfoDTOList = goodsAttributeService.getByGoodsId(goodsId);
        goodsMobileDTO.setGoodsAttributeInfoDTOList(goodsAttributeInfoDTOList);

        return goodsMobileDTO;
    }

    /**
     * 功能描述:
     * 〈订单更新商品规格库存，保存库存修改记录〉
     *
     * @param params key 规格id value 修改数量，正数为扣除，复数为增加
     * @param type   0规格不存在异常 1规格不存在放弃当前商品操作
     * @return : void
     * @author : 刘远杰
     */

    @Override
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public boolean updateStorage(@RequestBody Map<String, String> params, @RequestParam("type") Integer type) {
        log.info("执行GoodsSpecService.updateStorage({},{})", params, type);
        if (params.isEmpty()) {
            return true;
        }
        List<Long> specIds = new ArrayList<>();

        // 库存修改记录
        UpdateBatchStockLog updateBatchStockLog = new UpdateBatchStockLog();
        // 修改商品库存
        List<GoodsSpecStorageUpdateDTO> goodsSpecStorageUpdateDTOList = updateStorage(params, specIds, type);

        // 修改es规格库存
        rabbitMqSendService.sendMsg(MqConstant.LEIMINGTECH_GOODS_SPEC_INDEX_UPDATE_SINGLE, JSONArray.toJSONString(specIds));

        // 修改购物车es
        updateCartStorage(goodsSpecStorageUpdateDTOList, null);

        // 添加库存修改记录
        updateBatchStockLog.setGoodsSpecStorageUpdateDTOList(goodsSpecStorageUpdateDTOList);
        updateBatchStockLog.setUserName("系统");
        updateBatchStockLog.setType(1);
        rabbitMqSendService.sendMsg(MqConstant.LEIMINGTECH_STOCK_RECORD_QUEUE,
                JSON.toJSONString(updateBatchStockLog));
        return true;

    }

    /**
     * 功能描述:
     * 〈更新商品库存，新开启一个事务〉
     *
     * @param params
     * @param specIds
     * @param type    0规格不存在异常 1规格不存在放弃当前商品操作
     * @author : 刘远杰
     */
    public List<GoodsSpecStorageUpdateDTO> updateStorage(Map<String, String> params, List<Long> specIds, Integer type) {
        List<GoodsSpecStorageUpdateDTO> goodsSpecStorageUpdateDTOList = new ArrayList<>();
        List<GoodsSpecStorageUpdateDTO> specList = new ArrayList<>();
        List<GoodsSpecEntity> goodsSpecEntityList = new ArrayList<>();
        for (String key : params.keySet()) {
            GoodsSpecEntity goodsSpecEntity = new GoodsSpecEntity();
            // 遍历
            Long specId = Long.parseLong(key);
            Integer num = Integer.parseInt(params.get(key));

            // 查询商品规格信息
            GoodsSpecDTO goodsSpecDTO = this.get(specId);

            if (type == 1 && goodsSpecDTO == null) {
                // 增加库存规格已删除，不增加库存操作
                continue;
            }

            // 封装修改库存销量实体
            goodsSpecEntity.setId(specId);
            goodsSpecEntity.setSpecStorage(goodsSpecDTO.getSpecStorage() - num);
            goodsSpecEntity.setVersion(goodsSpecDTO.getVersion());
            goodsSpecEntityList.add(goodsSpecEntity);
            specIds.add(goodsSpecEntity.getId());

            GoodsSpecStorageUpdateDTO goodsSpecStorageUpdateDTO = new GoodsSpecStorageUpdateDTO();
            goodsSpecStorageUpdateDTO.setBeforeSpecStorage(goodsSpecDTO.getSpecStorage());
            goodsSpecStorageUpdateDTO.setSpecStorage(goodsSpecDTO.getSpecStorage() - num);
            goodsSpecStorageUpdateDTO.setId(goodsSpecDTO.getId());
            goodsSpecStorageUpdateDTO.setGoodsId(goodsSpecDTO.getGoodsId());
            goodsSpecStorageUpdateDTOList.add(goodsSpecStorageUpdateDTO);
            if (num > 0) {
                specList.add(goodsSpecStorageUpdateDTO);
            }
        }

        if (CollectionUtils.isNotEmpty(specList)) {
            rabbitMqSendService.sendMsg(MqConstant.LEIMINGTECH_STORAGE_WARNING_QUEUE, JSONArray.toJSONString(specList));
        }

        if (CollectionUtils.isNotEmpty(goodsSpecEntityList)) {
            // 批量更新规格库存
            Integer row = baseDao.updateStockBatchByIdAndVersion(goodsSpecEntityList);
            if (goodsSpecEntityList.size() != row) {
                log.error("库存扣减失败，开始重试，{}", goodsSpecEntityList);
                throw new ServiceException("扣减库存失败");
            }
        }
        return goodsSpecStorageUpdateDTOList;
    }

    /**
     * 功能描述:
     * 〈更新商品规格销量〉
     *
     * @param params key 规格id value 修改数量，正数为扣除，复数为增加
     * @param type   0规格不存在异常 1规格不存在放弃当前商品操作
     * @return : void
     * @author : 刘远杰
     */

    @Override
    public boolean updateSale(@RequestBody Map<String, String> params, @RequestParam("type") Integer type) {
        log.info("执行GoodsSpecService.updateSale({},{})", params, type);

        // 待更新销量的规格集合
        List<GoodsSpecEntity> goodsSpecEntityList = new ArrayList<>();
        // 待更新销量的商品附加信息集合
        List<GoodsInfoDTO> goodsInfoDTOList = new ArrayList<>();

        for (String key : params.keySet()) {

            // 规格Id
            Long specId = Long.parseLong(key);
            // 销量
            Integer saleNum = Integer.parseInt(params.get(key));

            // 查询商品规格信息
            GoodsSpecDTO goodsSpecDTO = this.get(specId);

            // 当前规格已删除放弃当前商品销量更新操作
            if (type == 1 && goodsSpecDTO == null) {
                continue;
            }

            // 查询商品信息
            GoodsInfoDTO goodsInfoDTO = goodsInfoService.get(goodsSpecDTO.getGoodsId());

            if (goodsInfoDTO != null) {
                GoodsSpecEntity goodsSpecEntity = new GoodsSpecEntity();
                // 封装修改库存销量实体
                goodsSpecEntity.setId(specId);
                goodsSpecEntity.setSpecSaleNum(goodsSpecDTO.getSpecSaleNum() + saleNum);
                goodsSpecEntity.setVersion(goodsSpecDTO.getVersion());
                goodsSpecEntityList.add(goodsSpecEntity);

                // 记录集合中是否存在该商品信息
                boolean flag = true;
                // 判断商品是否已存在
                for (GoodsInfoDTO infoDTO : goodsInfoDTOList) {
                    if (infoDTO.getId().equals(goodsInfoDTO.getId())) {
                        // 已添加同商品下的规格
                        infoDTO.setSaleNum(infoDTO.getSaleNum() + saleNum);
                        flag = false;
                        break;
                    }
                }
                if (flag) {
                    // 未添加同商品下的规格
                    goodsInfoDTO.setSaleNum(goodsInfoDTO.getSaleNum() + saleNum);
                    goodsInfoDTOList.add(goodsInfoDTO);
                }

            }
        }

        // 批量更新商品详细信息的销量信息
        if (CollectionUtils.isNotEmpty(goodsInfoDTOList)) {
            Integer count = goodsInfoService.updateList(goodsInfoDTOList);
            if (goodsInfoDTOList.size() != count) {
                log.error("商品销量更新失败{}", goodsInfoDTOList);
                throw new ServiceException("商品销量更新失败");
            }
        }

        // 批量更新规格销量
        Integer row = baseDao.updateSaleBatchByIdAndVersion(goodsSpecEntityList);
        if (goodsSpecEntityList.size() != row) {
            log.error("商品规格销量扣减失败,{}", goodsSpecEntityList);
            throw new ServiceException("规格销量更新失败");
        }

        // 取出商品附加信息id做为key，销量为value
        Map<String, Object> goodsInfoMap = goodsInfoDTOList.stream().collect(Collectors.toMap(t -> String.valueOf(t.getId()), GoodsInfoDTO::getSaleNum));
        // 取出规格id做为key，销量为value
        Map<String, Object> specMap = goodsSpecEntityList.stream().collect(Collectors.toMap(t -> String.valueOf(t.getId()), GoodsSpecEntity::getSpecSaleNum));

        // 修改es商品销量
        rabbitMqSendService.sendMsg(MqConstant.LEIMINGTECH_GOODS_INFO_SALE_NUM_UPDATE, JSON.toJSONString(goodsInfoMap));
        // 修改es规格销量
        rabbitMqSendService.sendMsg(MqConstant.LEIMINGTECH_GOODS_SPEC_SALE_NUM_UPDATE, JSONArray.toJSONString(specMap));

        return true;
    }


    /**
     * 获取规格表中的乐观锁版本
     *
     * @param specId: 规格ID
     * @return 乐观锁版本号
     * @date 2019/6/26 23:37
     * @author lixiang
     **/
    @Override

    public GoodsSpecVersionDTO findVersionById(Long specId) {
        GoodsSpecEntity goodsSpecEntity = baseDao.selectById(specId);
        GoodsSpecVersionDTO goodsSpecVersionDTO = new GoodsSpecVersionDTO();
        BeanCopier.create(goodsSpecEntity.getClass(), GoodsSpecVersionDTO.class, false)
                .copy(goodsSpecEntity, goodsSpecVersionDTO, null);
        return goodsSpecVersionDTO;
    }

    /**
     * 根据商品id 删除规格
     *
     * @param id
     */

    @Override
    public void delBygoodsId(Long id) {
        QueryWrapper<GoodsSpecEntity> wrapper = new QueryWrapper<>();
        wrapper.eq(Constant.DEL_FLAG, DelFlagEnum.NORMAL.value());
        wrapper.eq("goods_id", id);
        baseDao.delete(wrapper);
    }

    /**
     * 根据商品ids 删除规格
     *
     * @param goodsIds 商品ID数组
     */

    @Override
    public void deleteBatchBygoodsIds(@RequestBody Long[] goodsIds) {
        baseDao.delete(Wrappers.<GoodsSpecEntity>lambdaQuery()
                .in(GoodsSpecEntity::getGoodsId, goodsIds)
                .eq(GoodsSpecEntity::getDelFlag, DelFlagEnum.NORMAL.value()));
    }

    /**
     * 查询规格总库存
     *
     * @param goodsId
     * @return
     */

    @Override
    public Integer selectCount(Long goodsId) {
        return baseDao.selectCountStorage(goodsId);
    }

    /**
     * 修改商品主图
     *
     * @param id
     * @param specMainPic
     */

    @Override
    public void updateSpecMainPic(@RequestParam("id") Long id, @RequestParam("specMainPic") String specMainPic) {
        baseDao.updateSpecMainPic(id, specMainPic);
    }

    @Override

    public List<Long> selectByGoodsIds(@RequestBody List<Long> goodsIds) {
        return baseDao.selectSpecIdsByGoodsIds(goodsIds);
    }

    /**
     * 根据规格ID 查询出商品ID 可以查询出已删除的商品
     */

    @Override
    public Long findGoodsIdBySpecId(@RequestParam("specId") Long specId) {
        return baseDao.findGoodsIdBySpecId(specId);
    }

    /**
     * 根据规格ID 查询出店铺ID
     *
     * @param specId
     * @return
     */

    @Override
    public GoodsDTO getStoreIdBySpecId(@RequestParam("specId") Long specId) {
        return baseDao.getStoreIdBySpecId(specId);
    }

    /**
     * 查询当前店铺下预警数量
     *
     * @param storeId
     * @param storage
     * @return
     */

    @Override
    public Integer findCount(@RequestParam("storeId") Long storeId, @RequestParam("storage") Integer storage) {

        return baseDao.findCount(storeId, storage);
    }


    /**
     * 发送MQ消息,并保存到商品上下架记录表
     *
     * @param specIds
     * @param specShow
     */
    private void sendMqsaveGoodsRecord(List<Long> specIds, Integer specShow) {
        List<GoodsRecordDTO> goodsRecordDTOList = new ArrayList<>();
        for (int i = 0; i < specIds.size(); i++) {
            GoodsRecordDTO goodsRecordDTO = new GoodsRecordDTO();
            goodsRecordDTO.setGoodsShow(specShow);
            goodsRecordDTO.setSpecId(specIds.get(i));
            goodsRecordDTOList.add(goodsRecordDTO);
        }

        rabbitMqSendService.sendMsg(MqConstant.LEIMINGTECH_GOODS_SHOW_QUEUE,
                JSONArray.toJSONString(goodsRecordDTOList));
    }

    /**
     * 同步购物车封装数据
     *
     * @param userName      操作人
     * @param id            规格id
     * @param specShow      上下架状态
     * @param storage       库存
     * @param specSellPrice 售价
     * @return
     */
    private UpdateCartStatusDTO updateCart(String userName, Long id, Integer specShow, Integer storage, BigDecimal specSellPrice) {
        UpdateCartStatusDTO updateCartStatusDTO = new UpdateCartStatusDTO();
        updateCartStatusDTO.setUpdateName(userName);
        updateCartStatusDTO.setSpecId(id);
        updateCartStatusDTO.setStatus(specShow);
        updateCartStatusDTO.setSpecStorage(storage);
        updateCartStatusDTO.setSpecSellPrice(specSellPrice);
        return updateCartStatusDTO;
    }

    /**
     * Retryable重试失败执行的方法，参一:Retryable监听的异常，其余参数为Retryable注解方法上的参数
     *
     * @param e:      异常对象
     * @param params: 修改库存销量参数
     * @return
     * @date 2019/7/10 17:30
     * @author lixiang
     **/
    @Recover
    public void recover(RuntimeException e, Map<String, String> params) {
        log.error("商品扣减库存失败，事务执行回滚，修改库存参数如下:{}", params);
        TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
    }

    /**
     * 根据商品ID 上下架规格信息
     *
     * @param ids       商品ID集合
     * @param goodsShow 上下架状态（2未上架,0下架状态，1上架状态）
     * @return
     * @date 2020/3/4/004 18:23
     * @author xuzhch
     **/
    @Override

    public void updateShowByGoodsIds(@RequestBody List<Long> ids, @RequestParam("goodsShow") Integer goodsShow) {
        // 根据商品id修改商品规格上下架状态
        this.update(Wrappers.<GoodsSpecEntity>lambdaUpdate()
                .set(GoodsSpecEntity::getSpecShow, goodsShow)
                .in(GoodsSpecEntity::getGoodsId, ids));
        //获取管理员用户名
        String userName = SecurityUser.getUserName();
        /**
         * 1、创建修改购物车商品状态集合、商品修改记录集合
         * 2、根据商品id查询规格数据并遍历
         * 3、封装同步购物车商品数据、商品修改记录数据
         * 4、发送MQ消息，同步购物车商品、保存商品修改记录
         * */
        List<UpdateCartStatusDTO> updateCartStatusDTOList = new ArrayList<>();
        List<GoodsSpecEntity> goodsSpecEntities = baseDao.selectList(Wrappers.<GoodsSpecEntity>lambdaQuery().in(GoodsSpecEntity::getGoodsId, ids));
        List<GoodsRecordDTO> goodsRecordDTOList = new ArrayList<>();
        goodsSpecEntities.forEach(goodsSpecEntity -> {
            UpdateCartStatusDTO updateCartStatusDTO = updateCart(userName, goodsSpecEntity.getId(), goodsShow,
                    goodsSpecEntity.getSpecStorage(), goodsSpecEntity.getSpecSellPrice());
            updateCartStatusDTOList.add(updateCartStatusDTO);
            GoodsRecordDTO goodsRecordDTO = new GoodsRecordDTO();
            goodsRecordDTO.setSpecId(goodsSpecEntity.getId());
            goodsRecordDTO.setGoodsId(goodsSpecEntity.getGoodsId());
            goodsRecordDTO.setGoodsShow(goodsShow);
            goodsRecordDTOList.add(goodsRecordDTO);
        });
        // 发送MQ修改购物车商品上下架
        rabbitMqSendService.sendMsg(MqConstant.LEIMINGTECH_CART_GOODS_STATUS, JSON.toJSONString(updateCartStatusDTOList));
        // 发送MQ消息保存商品上下架保存记录
        rabbitMqSendService.sendMsg(MqConstant.LEIMINGTECH_GOODS_SHOW_QUEUE, JSON.toJSONString(goodsRecordDTOList));
        Map<String, String> logMap = new HashMap<>(10);
        logMap.put("商品Id为", JSON.toJSONString(ids));
        mlogger.info(GoodsStatusCode.SPEC_SHOW_UPDATE_OPERATION_CODE, GoodsStatusCode.SPEC_SHOW_UPDATE_OPERATION_SUCCESS_BY_GOODSID_MESSAGE, logMap);

    }


    @Override
    public void updateSpecShow(@RequestBody GoodsSpecShowDTO dto) {
        //商品ID
        Long goodsId = dto.getGoodsId();
        if (dto.getSpecShow() == GoodsStatusEnum.GOODS_SHOW_ON.value()) {
            Boolean flag = goodsService.validatePublishGoodsNum(new Long[]{goodsId});
            if (!flag) {
                throw new ServiceException(GoodsStatusCode.GOODS_PUBLISH_COUNT_OUT);
            }
        }
        //规格ID集合
        List<Long> specIds = dto.getIds();
        //规格上下架状态（2未上架,0下架状态，1上架状态）
        Integer specShow = dto.getSpecShow();

        if (CollectionUtils.isNotEmpty(specIds) && null != specShow) {
            //商品规格上下架
            this.update(Wrappers.<GoodsSpecEntity>lambdaUpdate().set(GoodsSpecEntity::getSpecShow, specShow).in(GoodsSpecEntity::getId, specIds));
            //判断 如果所有规格商品上下架操作一直 那么该商品也进行上下架操作
            updateSpecShow(specShow, goodsId);
            /**
             * 1、创建修改购物车商品状态集合、商品修改记录集合
             * 2、根据商品id查询规格数据并遍历
             * 3、封装同步购物车商品数据、商品修改记录数据
             * 4、发送MQ消息，同步购物车商品、保存商品修改记录
             * */
            List<UpdateCartStatusDTO> updateCartStatusDTOList = new ArrayList<>();
            List<GoodsRecordDTO> goodsRecordDTOList = new ArrayList<>();
            String userName = SecurityUser.getUserName();
            specIds.forEach(specId -> {
                //购物车
                UpdateCartStatusDTO updateCartStatusDTO = new UpdateCartStatusDTO();
                updateCartStatusDTO.setSpecId(specId);
                updateCartStatusDTO.setStatus(specShow);
                updateCartStatusDTO.setUpdateName(userName);
                updateCartStatusDTO.setSpecId(specId);
                updateCartStatusDTOList.add(updateCartStatusDTO);
                //商品修改记录
                GoodsRecordDTO goodsRecordDTO = new GoodsRecordDTO();
                goodsRecordDTO.setGoodsShow(specShow);
                goodsRecordDTO.setSpecId(specId);
                goodsRecordDTO.setGoodsId(goodsId);
                goodsRecordDTOList.add(goodsRecordDTO);
            });
            //同步购物车
            rabbitMqSendService.sendMsg(MqConstant.LEIMINGTECH_CART_GOODS_STATUS, JSON.toJSONString(updateCartStatusDTOList));
            rabbitMqSendService.sendMsg(MqConstant.LEIMINGTECH_GOODS_SHOW_QUEUE, JSONArray.toJSONString(goodsRecordDTOList));
            //日志记录
            Map<String, String> logMap = new HashMap<>(10);
            logMap.put("规格Id为:", JSON.toJSONString(specIds));
            mlogger.info(GoodsStatusCode.SPEC_SHOW_UPDATE_OPERATION_CODE, GoodsStatusCode.SPEC_SHOW_UPDATE_OPERATION_SUCCESS_MESSAGE, logMap);
        }
    }

    /**
     * 定时上下架信息保存、更新
     *
     * @param dto
     * @return
     * @date 2020/3/5/005 10:22
     * @author xuzhch
     **/

    @Override
    public void timedUpdateSpecShow(@RequestBody GoodsSpecShowDTO dto) {
        //规格ID集合
        List<Long> specIds = dto.getIds();
        //规格上下架状态（2未上架,0下架状态，1上架状态）
        Integer specShow = dto.getSpecShow();
        //商品ID
        Long goodsId = dto.getGoodsId();
        if (CollectionUtils.isNotEmpty(specIds) && null != specShow) {
            specIds.forEach(specId -> {
                GoodsTimeDTO timeDTO = goodsTimeService.selectBySpecId(specId);
                if (null == timeDTO) {
                    // 商品定时上下架状态与时间
                    GoodsTimeDTO goodsTimeDTO = new GoodsTimeDTO();
                    goodsTimeDTO.setShelfTime(dto.getShelfTime());
                    goodsTimeDTO.setOperationalStatus(GoodsTimeStatusEnum.GOODS_TIME_STATUS_SHOW.value());
                    goodsTimeDTO.setGoodsId(goodsId);
                    goodsTimeDTO.setShowStatus(specShow);
                    goodsTimeService.save(goodsTimeDTO);
                } else {
                    // 修改商品定时上下架状态与时间
                    timeDTO.setShelfTime(dto.getShelfTime());
                    timeDTO.setOperationalStatus(GoodsTimeStatusEnum.GOODS_TIME_STATUS_SHOW.value());
                    timeDTO.setGoodsId(goodsId);
                    timeDTO.setSpecId(specId);
                    timeDTO.setShowStatus(specShow);
                    goodsTimeService.updateStatusAndTime(timeDTO);
                }
            });
            //日志记录
            Map<String, String> logMap = new HashMap<>(10);
            logMap.put("保存商品规格定时上下架信息，规格ID为:", JSON.toJSONString(specIds));
            mlogger.info(GoodsStatusCode.SPEC_SHOW_UPDATE_OPERATION_CODE, GoodsStatusCode.SPEC_SHOW_UPDATE_OPERATION_SUCCESS_MESSAGE, logMap);
        }
    }

    /**
     * 功能描述 根据goodsid查找spec
     *
     * @param * @param goodsId
     * @return java.util.List<com.leimingtech.modules.dto.goods.detail.GoodsSpecDetailDTO>
     * @author lishuo
     * @date 28/6/2020
     */
    @Override
    public List<GoodsSpecDetailDTO> findSpecByGoodsId(Long goodsId) {
        List<GoodsSpecDetailDTO> goodsSpecDetailDTOList = baseDao.selectByGoodsId(goodsId);
        return goodsSpecDetailDTOList;
    }

    /**
     * @Author: weixianchun
     * @Description: 修改商品规格上下架状态
     * @Date :2019/6/6 18:27
     * @Param ids 规格id
     * @Param specShow 规格状态
     * @Version V1.0
     **/

    @Override
    public Long updateShow(@RequestBody GoodsSpecShowDTO dto) {
        List<Long> specIds = new ArrayList<>();
        Long goodsId = null;
        //购物车同步
        List<UpdateCartStatusDTO> updateCartStatusDTOList = new ArrayList<>();
        for (Long id : dto.getIds()) {
            GoodsSpecEntity goodsSpecEntity = baseDao.selectById(id);
            goodsId = goodsSpecEntity.getGoodsId();
            GoodsDTO goodsDTO = goodsService.get(goodsSpecEntity.getGoodsId());
            if (ObjectUtil.isNotNull(goodsSpecEntity)) {
                if (dto.getSpecShow() == GoodsStatusEnum.GOODS_SHOW_ON.value()) {
                    Boolean flag = goodsService.validatePublishGoodsNum(new Long[]{goodsDTO.getId()});
                    if (!flag) {
                        throw new ServiceException(GoodsStatusCode.GOODS_PUBLISH_COUNT_OUT);
                    }
                }
                if (GoodsStatusEnum.GOODS_SHOW_TYPE_TIMING.value() == dto.getShowType()) {
                    goodsTimeService.deleteBySpecId(id);
                    GoodsTimeDTO goodsTimeDTO = fillGoodsTimeShowType(dto);
                    goodsTimeDTO.setGoodsId(goodsSpecEntity.getGoodsId());
                    goodsTimeDTO.setSpecId(id);
                    goodsTimeDTO.setStoreId(goodsDTO.getStoreId());
                    goodsTimeDTO.setShowStatus(dto.getSpecShow());
                    goodsTimeService.save(goodsTimeDTO);
                } else if (GoodsStatusEnum.GOODS_SHOW_TYPE_NOW.value() == dto.getShowType()) {
                    //立即上下架
                    baseDao.updateShowBySpecId(id, dto.getSpecShow());
                    //索引
                    specIds.add(id);
                    String userName = SecurityUser.getUserName();
                    //购物车
                    UpdateCartStatusDTO updateCartStatusDTO = updateCart(userName,
                            id, goodsSpecEntity.getSpecShow(),
                            goodsSpecEntity.getSpecStorage(), goodsSpecEntity.getSpecSellPrice());
                    updateCartStatusDTOList.add(updateCartStatusDTO);


                    //判断
                    updateSpecShow(dto.getSpecShow(), goodsSpecEntity.getGoodsId());
                    //删除定时表中对应的定时
                    goodsTimeService.deleteBySpecIds(specIds);
                    //商品上下架记录
                    sendMqsaveGoodsRecord(specIds, dto.getSpecShow());
                }
            }
        }
        //同步购物车
        rabbitMqSendService.sendMsg(MqConstant.LEIMINGTECH_CART_GOODS_STATUS,
                JSON.toJSONString(updateCartStatusDTOList));
        HashMap<String, String> logMap = new HashMap<>(10);
        logMap.put("规格Id为", dto.getIds().toString());
        mlogger.info(GoodsStatusCode.SPEC_SHOW_UPDATE_OPERATION_CODE, GoodsStatusCode.SPEC_SHOW_UPDATE_OPERATION_SUCCESS_MESSAGE, logMap);
        return goodsId;

    }

    @Override

    public PartGoodsSpecDTO findGoodsSpecById(@RequestParam("id") Long id) {
        //查询商品规格
        return baseDao.findGoodsSpecById(id);
    }

    /**
     * 功能描述 批量保存
     *
     * @param * @param saveGoodsSpecTotalListDTO
     * @return void
     * @author lishuo
     * @date 22/7/2020
     */
    @Override

    public void insertBatch(@RequestParam("saveGoodsSpecDTOListTotal") List<GoodsSpecSaveDTO> saveGoodsSpecTotalListDTO) {

        baseDao.insertBatch(saveGoodsSpecTotalListDTO);
    }

    /**
     * 获取商品总重量
     *
     * @param specList 规格集合
     * @return
     */

    @Override
    public Double getWeight(@RequestBody List<Long> specList) {
        return baseDao.getWeight(specList);
    }
}
