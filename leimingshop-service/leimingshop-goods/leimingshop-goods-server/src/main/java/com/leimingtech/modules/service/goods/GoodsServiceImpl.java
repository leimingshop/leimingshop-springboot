/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.service.goods;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.ObjectUtil;
import com.alibaba.excel.EasyExcel;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.leimingtech.commons.mybatis.enums.DelFlagEnum;
import com.leimingtech.commons.mybatis.service.impl.CrudServiceImpl;
import com.leimingtech.commons.tools.constant.Constant;
import com.leimingtech.commons.tools.page.PageData;
import com.leimingtech.commons.tools.redis.RedisKeys;
import com.leimingtech.commons.tools.redis.RedisUtils;
import com.leimingtech.commons.tools.security.user.SecurityUser;
import com.leimingtech.commons.tools.utils.ConvertUtils;
import com.leimingtech.commons.tools.utils.DateUtils;
import com.leimingtech.dto.setting.SettingGoodsAuditDTO;
import com.leimingtech.enums.SettingsEnum;
import com.leimingtech.exception.ServiceException;
import com.leimingtech.logging.MonitorLogger;
import com.leimingtech.logging.MonitorLoggerFactory;
import com.leimingtech.modules.dao.goods.GoodsDao;
import com.leimingtech.modules.dto.brand.BrandDTO;
import com.leimingtech.modules.dto.cart.UpdateCartStatusDTO;
import com.leimingtech.modules.dto.freight.template.FreightTemplateDTO;
import com.leimingtech.modules.dto.goods.*;
import com.leimingtech.modules.dto.goods.check.GoodsCheckDTO;
import com.leimingtech.modules.dto.goods.check.GoodsCheckSaveDTO;
import com.leimingtech.modules.dto.goods.cms.CircleGoodsSearchDTO;
import com.leimingtech.modules.dto.goods.detail.*;
import com.leimingtech.modules.dto.goods.index.IndexGoodsDataDTO;
import com.leimingtech.modules.dto.goods.modify.GoodsModifyDTO;
import com.leimingtech.modules.dto.goods.modify.ValidSpecSerial;
import com.leimingtech.modules.dto.goods.price.GoodsSpecPriceUpdateDTO;
import com.leimingtech.modules.dto.goods.price.GoodsSpecStorageUpdateDTO;
import com.leimingtech.modules.dto.goods.record.GoodsRecordDTO;
import com.leimingtech.modules.dto.goods.spec.*;
import com.leimingtech.modules.dto.goods.time.GoodsTimeDTO;
import com.leimingtech.modules.dto.goodsclass.GoodsClassListDTO;
import com.leimingtech.modules.dto.goodsclass.GoodsClassQueryNameDTO;
import com.leimingtech.modules.dto.goodslable.GoodsLabelDTO;
import com.leimingtech.modules.dto.goodslable.GoodsLabelRelDTO;
import com.leimingtech.modules.dto.goodslable.GoodsLabelRelSaveDTO;
import com.leimingtech.modules.dto.grade.StoreGradeDTO;
import com.leimingtech.modules.dto.price.UpdateBatchPriceLog;
import com.leimingtech.modules.dto.stock.UpdateBatchStockLog;
import com.leimingtech.modules.dto.store.StoreDTO;
import com.leimingtech.modules.dto.storegoodsclass.StoreGoodsClassDTO;
import com.leimingtech.modules.dto.warning.StorageWarningDTO;
import com.leimingtech.modules.entity.goods.GoodsEntity;
import com.leimingtech.modules.enums.cart.CartEnum;
import com.leimingtech.modules.enums.goods.*;
import com.leimingtech.modules.enums.goodsclass.GoodsClassErrorCodeEnum;
import com.leimingtech.modules.enums.store.StoreEnum;
import com.leimingtech.modules.excel.goods.EasyGoodsCheckExcel;
import com.leimingtech.modules.excel.goods.EasyGoodsExcel;
import com.leimingtech.modules.excel.goods.GoodsTemplateExcel;
import com.leimingtech.modules.listener.GoodsImportListener;
import com.leimingtech.modules.service.activity.goods.ActivityGoodsService;
import com.leimingtech.modules.service.brand.BrandService;
import com.leimingtech.modules.service.freight.template.FreightTemplateService;
import com.leimingtech.modules.service.goods.check.GoodsCheckService;
import com.leimingtech.modules.service.goods.spec.*;
import com.leimingtech.modules.service.goods.time.GoodsTimeService;
import com.leimingtech.modules.service.goodsclass.GoodsClassService;
import com.leimingtech.modules.service.goodslabel.GoodsLabelRelService;
import com.leimingtech.modules.service.goodslabel.GoodsLabelService;
import com.leimingtech.modules.service.grade.StoreGradeService;
import com.leimingtech.modules.service.store.StoreService;
import com.leimingtech.modules.service.storegoodsclass.StoreGoodsClassService;
import com.leimingtech.modules.service.warning.StorageWarningService;
import com.leimingtech.modules.statuscode.GoodsStatusCode;
import com.leimingtech.modules.statuscode.StoreStatusCode;
import com.leimingtech.modules.vo.GoodsImportResultVO;
import com.leimingtech.mq.constant.MqConstant;
import com.leimingtech.mq.service.RabbitMqSendService;
import com.leimingtech.service.setting.SettingService;
import com.leimingtech.upload.service.UploadService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.beans.BeanCopier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 商品表
 *
 * @author dy
 * @email 1197793912@qq.com
 * @since 1.0.0 2019-06-04
 */
@Slf4j
@Service

public class GoodsServiceImpl extends CrudServiceImpl<GoodsDao, GoodsEntity, GoodsDTO> implements GoodsService {

    private static MonitorLogger mlogger = MonitorLoggerFactory.getMonitorLogger(GoodsServiceImpl.class);

    @Autowired

    private GoodsInfoService goodsInfoService;

    @Autowired

    private GoodsAttributeService goodsAttributeService;

    @Autowired
    private ActivityGoodsService activityGoodsService;
    @Autowired

    private GoodsSpecService goodsSpecService;

    @Autowired

    private GoodsTimeService goodsTimeService;

    @Autowired

    private SpecAttributeService specAttributeService;

    @Autowired

    private SpecAttributeValueService specAttributeValueService;

    @Autowired

    private FreightTemplateService freightTemplateService;

    @Autowired

    private SpecAttributePictureService specAttributePictureService;

    @Autowired

    private SpecAttributeRelationService specAttributeRelationService;

    @Autowired

    private GoodsCheckService goodsCheckService;

    @Autowired
    private RabbitMqSendService rabbitMqSendService;

    @Autowired
    private StoreService storeService;

    @Autowired
    private StoreGradeService storeGradeService;

    @Autowired
    private SettingService settingService;

    @Autowired
    private GoodsClassService goodsClassService;

    @Autowired
    private GoodsLabelRelService goodsLabelRelService;

    @Autowired
    private GoodsLabelService goodsLabelService;

    @Autowired
    private StorageWarningService storageWarningService;
    @Autowired
    private StoreGoodsClassService storeGoodsClassService;
    @Autowired
    private BrandService brandService;
    @Autowired
    private UploadService uploadService;
//
//    @Autowired
//    private RecommendService recommendService;

    @Autowired
    private RedisUtils redisUtils;


    @Override
    public QueryWrapper<GoodsEntity> getWrapper(Map<String, Object> params) {
        String id = (String) params.get("id");
        String goodsName = (String) params.get("goodsName");
        String storeName = (String) params.get("storeName");
        QueryWrapper<GoodsEntity> wrapper = new QueryWrapper<>();
        wrapper.eq(StringUtils.isNotBlank(id), "id", id);
        wrapper.like(StringUtils.isNotBlank(goodsName), "goods_name", goodsName);
        wrapper.like(StringUtils.isNotBlank(storeName), "store_name", storeName);

        return wrapper;
    }


    /**
     * 功能描述：
     * <添加活动商品分页>
     *
     * @param params 查询条件
     * @return
     * @date 2020/3/10 10:43
     * @author 刘远杰
     **/
    @Override

    public PageData<ActivitityGoodsInfoDTO> selectGoodsAndStoragePage(@RequestParam Map<String, Object> params) {
        IPage page = getPage(params, null, false);
        List<ActivitityGoodsInfoDTO> dtos = baseDao.selectGoodsAndStorageList(page, params);
        return new PageData<>(dtos, page.getTotal());
    }


    /**
     * 根据商品id获取商品信息
     *
     * @param goodsList 商品id集合
     * @return
     */

    @Override
    public List<TopicGoodsDTO> selectGoodsInfoByGoodsId(@RequestBody List<Long> goodsList) {
        return baseDao.selectGoodsInfoByGoodsId(goodsList);
    }

    /**
     * 分页查询
     *
     * @param params
     * @return
     */
    @Override

    public PageData<GoodsListDTO> pageGoods(@RequestParam Map<String, Object> params) {

        int pageNum = Integer.valueOf(params.get(Constant.PAGE).toString());
        int limit = Integer.valueOf(params.get(Constant.LIMIT).toString());
        Page<GoodsListDTO> goodsListPageDTO = new Page<>(pageNum, limit);

        String type = (String) params.get("message");
        if (StringUtils.isNotBlank(type)) {
            StorageWarningDTO data = storageWarningService.get(Long.valueOf(params.get("storeId").toString()));
            params.put("warnStorage", data.getStorage());
        }
        List<GoodsListDTO> page = new ArrayList<>();
        Object goodsId = params.get("goodsId");
        // 楼层查询商品的时候需要把已经关联的商品信息放在第一个
        if (goodsId != null) {
            page = baseDao.floorGoodsPage(goodsListPageDTO, params);
        } else {
            page = baseDao.selectGoodsList(goodsListPageDTO, params);

        }
        return new PageData(page, goodsListPageDTO.getTotal());
    }


    /**
     * 列表
     *
     * @param params
     * @return
     */
    @Override

    public List<GoodsDTO> list(@RequestParam Map<String, Object> params) {
        return super.list(params);
    }


    @Override
    public GoodsDatailDTO getDetail(Long goodsId) {

        //商品基础信息
        GoodsDatailDTO goodsDatailDTO = baseDao.getDetail(goodsId);

        //商品标签信息
        HashMap<String, Object> goodsRelParams = new HashMap<>(10);
        goodsRelParams.put("goodsId", goodsId.toString());
        List<GoodsLabelRelDTO> labelRelDTOList = goodsLabelRelService.list(goodsRelParams);

        goodsDatailDTO.setLabelIds(Optional.ofNullable(labelRelDTOList).orElse(new ArrayList<>())
                .stream().map(GoodsLabelRelDTO::getLabelId).collect(Collectors.toList()));

        //商品属性信息
        List<GoodsAttributeInfoDTO> goodsAttributeDTOList = goodsAttributeService.getByGoodsId(goodsId);
        goodsDatailDTO.setGoodsAttributeDTOList(goodsAttributeDTOList);

        //商品规格属性和属性值信息
        List<SpecAttributeDetailDTO> specAttributeDetailDTOList = specAttributeService.getByGoodsId(goodsId);
        goodsDatailDTO.setSpecAttributeDetailDTOList(specAttributeDetailDTOList);

        //查询规格图片信息
        List<SpecAttributePictureDetailDTO> specAttributePictureDetailDTOList = specAttributeValueService.getPicListByGoodsId(goodsId);
        goodsDatailDTO.setSpecAttributePictureDetailDTOList(specAttributePictureDetailDTOList);

        //查询商品图片信息
        List<DefaultSpecPictureDTO> defaultSpecPictureDTOList = specAttributePictureService.getDefaultPic(goodsId);
        goodsDatailDTO.setSpecPictureListDefaultDTO(defaultSpecPictureDTOList);

        //商品规格信息
        List<GoodsSpecDetailDTO> goodsSpecDetailDTOList = goodsSpecService.selectByGoodsId(goodsId);
        goodsDatailDTO.setGoodsSpecDetailDTOList(goodsSpecDetailDTOList);

        //商品定时信息
        GoodsTimeDTO goodsTimeDTO = goodsTimeService.selectByGoodsId(goodsId);
        if (goodsTimeDTO != null) {
            goodsDatailDTO.setGoodsType(GoodsStatusEnum.GOODS_PUBLISH_TIMING.value());
            goodsDatailDTO.setShelfTime(goodsTimeDTO.getShelfTime());
        } else {
            goodsDatailDTO.setGoodsType(GoodsStatusEnum.GOODS_PUBLISH_NOW.value());
        }

        // 商品活动库存
        Map<Long, Integer> activityGoodsSurplusStorageMap = activityGoodsService.getActivityGoodsSurplusStorage(goodsId);
        if (CollectionUtils.isNotEmpty(goodsSpecDetailDTOList)
                && activityGoodsSurplusStorageMap != null
                && activityGoodsSurplusStorageMap.size() > 0) {
            for (GoodsSpecDetailDTO goodsSpecDetailDTO : goodsSpecDetailDTOList) {
                if (activityGoodsSurplusStorageMap.containsKey(goodsSpecDetailDTO.getId())) {
                    // 普通库存 + 活动库存
                    goodsSpecDetailDTO.setSpecStorage(goodsSpecDetailDTO.getSpecStorage() + activityGoodsSurplusStorageMap.get(goodsSpecDetailDTO.getId()));
                    // 总库存
                    goodsDatailDTO.setSpecStorage(goodsDatailDTO.getSpecStorage() + activityGoodsSurplusStorageMap.get(goodsSpecDetailDTO.getId()));
                }
            }
        }

        return goodsDatailDTO;
    }

    /**
     * 查询详情
     *
     * @param id
     * @return
     */
    @Override

    public GoodsDTO get(Long id) {
        return super.get(id);
    }

    /**
     * 功能描述:
     * 〈根据ids查询商品集合〉
     *
     * @param goodsIds 商品spuid
     * @param storeId  店铺id
     * @author : 刘远杰
     */

    @Override
    public List<GoodsDTO> getByGoodsIds(@RequestBody List<Long> goodsIds,
                                        @RequestParam(value = "storeId", required = false) Long storeId) {
        List<GoodsEntity> entityList = baseDao.selectList(Wrappers.<GoodsEntity>lambdaQuery()
                .eq(storeId != null, GoodsEntity::getStoreId, storeId)
                .in(GoodsEntity::getId, goodsIds)
                .eq(GoodsEntity::getDelFlag, DelFlagEnum.NORMAL.value()));
        return ConvertUtils.sourceToTarget(entityList, GoodsDTO.class);
    }

    /**
     * 商品发布（保存）
     *
     * @param dto
     * @date 2020年1月6日15:14
     * @author xuzhch
     */
    @Override

    @Transactional(rollbackFor = Exception.class)
    public Long save(@RequestBody GoodsConserveDTO dto) {
        //提前设置一个默认规格ID
        long id = IdWorker.getId();
        //--------------------------------基本信息------------------------------------
        //保存商品信息 返回商品实体
        GoodsEntity goodsEntity = saveGoodsEntity(dto, id);
        // 获取商品id
        Long goodsId = goodsEntity.getId();
        //保存商品附加信息
        saveGoodsInfo(dto, goodsId);
        //保存商品与标签信息关联表
        saveGoodsLabelRel(dto, goodsId);
        //-------------------基本信息保存结束------------------------
        //规格属性和规格属性值id生成
        Map<Long, Long> newIdMap = replaceIdSave(dto.getSpecAttributeAndValueSaveDTOList());
        //打印商品规格和规格属性值生成的ID与DTO 的 iD映射关系
        Map<String, String> logMap = new HashMap<>(10);
        if (MapUtils.isNotEmpty(newIdMap)) {
            logMap.put("newIdMap", newIdMap.toString());
        }
        mlogger.info(GoodsStatusCode.GOODS_SAVE_SUCCESS_CODE, GoodsStatusCode.GOODS_SAVE_SUCCESS_NEWID_MSG, logMap);

        /*
         *  商品规格保存 ，如果商品规格不存在
         *      说明商品没有选择规格，则设置默认规格
         *      否·则说明商品选择了规格，则封装规格信息数据，添加到集合
         */
        List<GoodsSpecSaveDTO> goodsSpecSaveDTOList = null;
        if (CollectionUtils.isEmpty(dto.getGoodsSpecSaveDTOList())) {
            //不存在封装默认规格
            List<GoodsSpecSaveDTO> defaultSpec = getDefaultSpec(dto, id, goodsId, goodsEntity.getGoodsMainPicture());
            goodsSpecService.saveBatch(defaultSpec);
        } else {
            goodsSpecSaveDTOList = fillgoodsSpec(dto.getGoodsSpecSaveDTOList(),
                    goodsId, newIdMap, goodsEntity.getSpecId());
            fillgoodsSpecName(dto.getGoodsSpecSaveDTOList(), dto.getSpecAttributeAndValueSaveDTOList());
            goodsSpecService.saveBatch(goodsSpecSaveDTOList);
        }
        //--------------------------------商品属性保存--------------------------
        saveGoodsAttributeList(dto, goodsId);
        //--------------------------------商品规格属性和规格值保存--------------------
        saveSpecAttrAndSpecAttrVal(dto.getSpecAttributeAndValueSaveDTOList(), goodsId);
        //-------------------------------保存商品图片信息-----------------------
        this.saveGoodsPic(dto.getSpecAttributePictureSaveDTOList(), dto.getSpecAttributePictureSaveListDefaultDTO(), newIdMap, goodsId);
        //--------------------------------商品规格属性与属性值关联保存对象------------------------
        saveAttrValRelation(goodsSpecSaveDTOList);
        //--------------------------------定时发布保存------------------------
        saveGoodsScheduledRelease(dto, goodsId);
        mlogger.info(GoodsStatusCode.GOODS_SAVE_SUCCESS_CODE, GoodsStatusCode.GOODS_SAVE_SUCCESS_GOODS_MSG + goodsId);
        return goodsEntity.getId();
    }

    private void fillgoodsSpecName(List<GoodsSpecSaveDTO> goodsSpecSaveDTOList, List<SpecAttributeAndValueSaveDTO> specAttributeAndValueSaveDTOList) {
        for (GoodsSpecSaveDTO goodsSpecSaveDTO : goodsSpecSaveDTOList) {
            List<SpecAttributeRelationSaveDTO> specAttributeRelationSaveDTOList = goodsSpecSaveDTO.getSpecAttributeRelationSaveDTOList();
            StringBuilder specAttrValueName = new StringBuilder();
            for (SpecAttributeRelationSaveDTO specAttributeRelationSaveDTO : specAttributeRelationSaveDTOList) {
                for (SpecAttributeAndValueSaveDTO specAttributeAndValueSaveDTO : specAttributeAndValueSaveDTOList) {
                    if (specAttributeRelationSaveDTO.getSpecAttrId().equals(specAttributeAndValueSaveDTO.getSpecAttrId())
                            && specAttributeRelationSaveDTO.getSpecAttrValueId().equals(specAttributeAndValueSaveDTO.getSpecAttrValueId())
                            && specAttributeAndValueSaveDTO.getIsSelect().equals(GoodsStatusEnum.SPEC_ATTR_CHECKED.value())) {
                        specAttrValueName = specAttrValueName.append(specAttributeAndValueSaveDTO.getSpecAttrName() + ":" + specAttributeAndValueSaveDTO.getSpecAttrValue() + " ");

                    }
                }
            }
            goodsSpecSaveDTO.setSpecAttrValueName(specAttrValueName.substring(0, specAttrValueName.length() - 1));
        }
    }

    /**
     * 保存商品信息
     *
     * @param dto 商品保存对象，id 默认规格ID，goodsEntity商品实体
     * @return 商品默认主图
     * @date 2019/12/30/030 17:28
     * @author xuzhch
     **/
    private GoodsEntity saveGoodsEntity(@RequestBody GoodsConserveDTO dto, Long id) {
        GoodsEntity goodsEntity = ConvertUtils.sourceToTarget(dto, GoodsEntity.class);
        long goodsId = IdWorker.getId();
        goodsEntity.setId(goodsId);
        StoreDTO storeDTO = storeService.get(dto.getStoreId());
        //判断店铺是否被禁用
        Integer storeStatus = storeService.findStoreStatus(dto.getStoreId());
        if (storeStatus == 1) {
            throw new ServiceException(StoreStatusCode.STORE_IS_NULL_ENABLE);
        }
        //商品审核状态
        fillGoodsStatus(goodsEntity, dto.getGoodsType(), storeDTO.getStoreType());
        //保存店铺名称
        goodsEntity.setStoreName(storeDTO.getStoreName());
        goodsEntity.setStoreType(storeDTO.getStoreType());
        //封装商品主图
        for (DefaultSpecPictureDTO defaultSpecPictureDTO : dto.getSpecAttributePictureSaveListDefaultDTO()) {
            if (defaultSpecPictureDTO.getIsMainPicture() == GoodsStatusEnum.IS_MAIN_PICTURE.value()) {
                String goodsMainPic = defaultSpecPictureDTO.getPictureUrl();
                goodsEntity.setGoodsMainPicture(goodsMainPic);
                this.setGoodsCustomPicSize(goodsEntity);
                break;
            }
        }
        goodsEntity.setSpecId(id);
        baseDao.insert(goodsEntity);
        Map<String, String> goodsIdLogMap = new HashMap<>(10);
        goodsIdLogMap.put("goodsId", goodsEntity.getId().toString());
        mlogger.info(GoodsStatusCode.GOODS_SAVE_SUCCESS_CODE, GoodsStatusCode.GOODS_SAVE_SUCCESS_MSG, goodsIdLogMap);
        return goodsEntity;
    }

    /**
     * 保存商品附加信息
     *
     * @param dto 商品保存对象，goodsId 商品Id
     * @return
     * @date 2019/12/30/030 17:27
     * @author xuzhch
     **/
    private void saveGoodsInfo(GoodsConserveDTO dto, Long goodsId) {
        GoodsInfoDTO goodsInfoDTO = ConvertUtils.sourceToTarget(dto, GoodsInfoDTO.class);
        goodsInfoDTO.setId(goodsId);
        goodsInfoService.save(goodsInfoDTO);

    }

    /**
     * 商品与标签关联保存
     *
     * @param dto 商品保存对象，goodsEntity商品对象
     * @date 2019/12/30/030 17:26
     * @author xuzhch
     **/
    private void saveGoodsLabelRel(@RequestBody GoodsConserveDTO dto, Long goodsId) {
        if (null != dto.getLabelIds() && dto.getLabelIds().length > 0) {

            // 获得商品标签关联表保存对象
            List<GoodsLabelRelSaveDTO> list = getGoodsLabelRel(dto.getLabelIds(), goodsId);
            goodsLabelRelService.insertBatch(list);
        }
    }

    /**
     * 发布商品时 保存商品属性方法
     *
     * @param dto     商品保存对象
     * @param goodsId 商品Id
     */
    private void saveGoodsAttributeList(@RequestBody GoodsConserveDTO dto, Long goodsId) {
        if (CollectionUtils.isNotEmpty(dto.getGoodsAttributeSaveDTOList())) {
            List<GoodsAttributeSaveDTO> attributeSaveDTOList = dto.getGoodsAttributeSaveDTOList();
            for (GoodsAttributeSaveDTO goodsAttribute : attributeSaveDTOList) {
                goodsAttribute.setGoodsId(goodsId);
            }
            goodsAttributeService.save(attributeSaveDTOList);
        }
    }

    /**
     * 保存商品规格属性和规格属性值
     *
     * @param list    规格属性和规格属性值信息集合
     * @param goodsId 商品ID
     * @return
     * @date 2020/1/6/006 15:00
     * @author xuzhch
     **/
    private void saveSpecAttrAndSpecAttrVal(@RequestBody List<SpecAttributeAndValueSaveDTO> list, Long goodsId) {
        if (CollectionUtils.isNotEmpty(list)) {
            specAttributeService.saveBatch(fillSpecAttributeAndValue(list, goodsId));
        }
    }

    /**
     * 保存商品图片信息
     *
     * @param specAttributePictureSaveDTOList 商品规格图片
     * @param defaultSpecPictureDTOList       商品主图
     * @param goodsId                         商品ID
     * @param newIdMap                        规格属性Id和规格属性值ID的新旧Id映射关系
     * @date 2020/1/6/006 14:57
     * @author xuzhch
     **/
    private void saveGoodsPic(List<SpecAttributePictureSaveDTO> specAttributePictureSaveDTOList, List<DefaultSpecPictureDTO> defaultSpecPictureDTOList, Map<Long, Long> newIdMap, Long goodsId) {
        //替换属性ID
        this.replacePictuerId(specAttributePictureSaveDTOList, newIdMap);
        //商品图片  默认图片
        List<SpecAttributePictureSaveDTO> defaultPicList = Optional.ofNullable(ConvertUtils.sourceToTarget(defaultSpecPictureDTOList,
                SpecAttributePictureSaveDTO.class)).orElse(new ArrayList<>());
        specAttributePictureSaveDTOList.addAll(defaultPicList);
        for (SpecAttributePictureSaveDTO specAttributePictureSaveDTO : specAttributePictureSaveDTOList) {
            //设置商品ID
            specAttributePictureSaveDTO.setGoodsId(goodsId);
            //增加自定义尺寸图片
            String pictureUrl = specAttributePictureSaveDTO.getPictureUrl();
            if (StringUtils.isNotBlank(pictureUrl)) {
                String[] split = pictureUrl.split("\\.");
                String s = split[split.length - 1];
                specAttributePictureSaveDTO.setOneHundredPxPictureUrl(pictureUrl + CustomPictureSizeEnum.ONE_HUNDRED_PX.value() + s);
                specAttributePictureSaveDTO.setTwoHundredPxPictureUrl(pictureUrl + CustomPictureSizeEnum.TWO_HUNDRED_PX.value() + s);
                specAttributePictureSaveDTO.setFourHundredPxPictureUrl(pictureUrl + CustomPictureSizeEnum.FOUR_HUNDRED_PX.value() + s);
                specAttributePictureSaveDTO.setEightHundredPxPictureUrl(pictureUrl + CustomPictureSizeEnum.EIGHT_HUNDRED_PX.value() + s);
            }
        }
        specAttributePictureService.save(specAttributePictureSaveDTOList);
    }

    /**
     * 保存商品定时发布信息
     *
     * @param dto     商品保存对象
     * @param goodsId 商品ID
     * @date 2020/1/6/006 15:07
     * @author xuzhch
     **/
    private void saveGoodsScheduledRelease(@RequestBody GoodsConserveDTO dto, Long goodsId) {
        if (GoodsStatusEnum.GOODS_PUBLISH_TIMING.value() == dto.getGoodsType()) {
            GoodsTimeDTO goodsTimeDTO = fillGoodsTime(dto.getShelfTime(), goodsId, dto.getStoreId(), dto.getGoodsType());
            goodsTimeService.save(goodsTimeDTO);
        }
    }


    /**
     * 封装商品标签关联表对象
     *
     * @param labelIds
     * @param id
     * @return
     */
    private List<GoodsLabelRelSaveDTO> getGoodsLabelRel(Long[] labelIds, Long id) {

        // 批量保存商品标签关联内容
        List<GoodsLabelRelSaveDTO> labelRelSaveDTOList = new ArrayList<>();
        for (Long labelId : labelIds) {
            GoodsLabelRelSaveDTO labelRelSaveDTO = new GoodsLabelRelSaveDTO();
            labelRelSaveDTO.setLabelId(labelId);
            //根据标签id查询标签名称
            GoodsLabelDTO labelDTO = goodsLabelService.get(labelId);
            labelRelSaveDTO.setLabelName(labelDTO.getLabelName());
            labelRelSaveDTO.setGoodsId(id);
            labelRelSaveDTOList.add(labelRelSaveDTO);
        }
        return labelRelSaveDTOList;
    }

    /**
     * 修改商品基本信息
     *
     * @param dto
     */
    @Override

    @Transactional(rollbackFor = Exception.class)
    public Long update(@RequestBody GoodsModifyDTO dto) {
        List<Long> goodsIds = new ArrayList<>();
        // 同步购物车参数
        List<UpdateCartStatusDTO> updateCartStatusDTO = new ArrayList<>();
        UpdateCartStatusDTO statusDTO = new UpdateCartStatusDTO();
        statusDTO.setGoodsId(dto.getId());
        statusDTO.setStatus(GoodsSpecStatusEnum.GOODS_SPEC_SHOW_DOWN.value());
        updateCartStatusDTO.add(statusDTO);
        goodsIds.add(dto.getId());
        Boolean activityFlag = activityGoodsService.updateGoodsActivityHandle(goodsIds);
        if (!activityFlag) {
            throw new ServiceException(GoodsStatusCode.GOODS_ACTIVITY_START_ERROR);
        }

        //------------------------------------修改商品基本信息------------------------------------------------
        //修改商品信息
        GoodsEntity goodsEntity = updateGoodEntity(dto);
        String goodsMainPic = goodsEntity.getGoodsMainPicture();
        //---------修改商品详情信息---------
        updateGoodsInfo(dto);
        //---------修改标签信息----------
        updateGoodsLabelRel(dto);
        //---------修改属性---------
        updateGoodsAttributeList(dto);
        //---------修改中间表:同时修改规格---------
        //执行标记:  规格属性变化:改为false
        boolean delType = true;
        //选中的属性数组
        Long[] specAttributeModifyIds = dto.getSpecAttributeModifyIds();

        //根据属性删除
        /*
         * 判断选中的规格属性值是否是原有的规格属性值  如果不是就根据商品ID 删除中间表与规格信息
         */
        if (ArrayUtil.isNotEmpty(specAttributeModifyIds)) {
            //判断长度是否相等,不相等删除全部
            //原有数量
            Integer attrNumOld = specAttributeRelationService.selectCountBygoodsId(dto.getId());
            //不相等  增加或去除的属性
            if (specAttributeModifyIds.length != attrNumOld) {
                //删除中间表
                specAttributeRelationService.deleteByGoodsId(dto.getId());
                //删除规格Id
                goodsSpecService.delBygoodsId(dto.getId());
                delType = false;
            }

        }
        //根据属性值进行删除
        /*
         * delType  为true说明上面操作未执行 需要判断操作 删除规格信息和 中间表数据
         */
        if (delType) {
            //移除的属性值数组
            Long[] specAttributeAndValueModifyIds = dto.getSpecAttributeAndValueModifyIds();

            //日志
            HashMap<String, String> attrValLogMap = new HashMap<>(10);
            if (ArrayUtil.isNotEmpty(specAttributeAndValueModifyIds)) {
                attrValLogMap.put("specAttributeAndValueModifyIds", Arrays.toString(specAttributeAndValueModifyIds));
                mlogger.info(GoodsStatusCode.GOODS_UPDATE_SUCCESS_CODE,
                        GoodsStatusCode.GOODS_UPDATE_SUCCESS_DEL_VAL_MSG, attrValLogMap);

                //根据规格属性值id获取中间表中的specid
                List<SpecAttributeRelationDTO> specAttributeRelationDTOList =
                        specAttributeRelationService.selectSpecIdBySpecAttrValueId(specAttributeAndValueModifyIds);

                //存在需要移除规格
                HashMap<String, String> attrRelLogMap = new HashMap<>(10);
                if (CollectionUtils.isNotEmpty(specAttributeRelationDTOList)) {
                    //删除规格id集合
                    List<Long> specIds = new ArrayList<>();
                    for (SpecAttributeRelationDTO specAttributeRelationDTO : specAttributeRelationDTOList) {
                        UpdateCartStatusDTO cartStatusDTO = new UpdateCartStatusDTO();
                        cartStatusDTO.setSpecId(specAttributeRelationDTO.getSpecId());
                        cartStatusDTO.setGoodsId(specAttributeRelationDTO.getGoodsId());
                        cartStatusDTO.setStatus(CartEnum.CART_STATUS_DEL.value());
                        updateCartStatusDTO.add(cartStatusDTO);
                        specIds.add(specAttributeRelationDTO.getSpecId());
                    }

                    //删除中间表
                    Map<String, String> logMapSpecId = new HashMap<>(10);
                    logMapSpecId.put("specIds", specIds.toString());
                    mlogger.info(GoodsStatusCode.GOODS_UPDATE_SUCCESS_CODE,
                            GoodsStatusCode.GOODS_UPDATE_SUCCESS_DEL_SPEC_MSG, logMapSpecId);
                    specAttributeRelationService.deleteBySpecId(specIds.toArray(new Long[specIds.size()]));

                    //删除规格
                    goodsSpecService.delete(specIds.toArray(new Long[specIds.size()]));
                }
            }
        }

        //-------------------------------------------删除图片----------------------------------------------------------
        Long goodsId = dto.getId();
        specAttributePictureService.deleteByGoodsId(goodsId);

        //-----------------------------------------------保存----------------------------------------------------------

        //修改规格属性和规格属性值
        /*
         *   判断规格属性 和规格属性值状态 新增属性值、原有、新增属性和属性值
         *   封装属性和属性值信息数据 并保存
         */
        List<SpecAttributeAndValueSaveDTO> newSpecAttrValueList = new ArrayList<>();
        List<SpecAttributeAndValueSaveDTO> specAttributeAndValueSaveDTOList = dto.getSpecAttributeAndValueSaveDTOList();
        List<SpecAttributeAndValueSaveDTO> specAttrUpdateDtoList = new ArrayList<>();
        for (SpecAttributeAndValueSaveDTO specAttributeAndValueSaveDTO : specAttributeAndValueSaveDTOList) {
            if (specAttributeAndValueSaveDTO.getAddType() == GoodsStatusEnum.GOODS_ADD_OLD.value()) {
                specAttrUpdateDtoList.add(specAttributeAndValueSaveDTO);
            } else {
                //新增
                specAttributeAndValueSaveDTO.setSpecAttrId(specAttributeAndValueSaveDTO.getSpecAttrId());
                newSpecAttrValueList.add(specAttributeAndValueSaveDTO);
            }
        }
        if (specAttrUpdateDtoList.size() > 0) {
            specAttributeService.updateSpecAttrVal(specAttrUpdateDtoList);
        }
        //新规格值  替换得到map  进行保存
        Map<Long, Long> newSpecAttrValIds = replaceId(newSpecAttrValueList);
        if (CollectionUtils.isNotEmpty(newSpecAttrValueList)) {
            //封装商品id
            fillSpecAttributeAndValue(newSpecAttrValueList, dto.getId());
            specAttributeService.saveBatch(newSpecAttrValueList);
        }

        //---------------------------------------修改规格-----------------------------------------------------------
        List<GoodsSpecSaveDTO> goodsSpecSaveDTOList = dto.getGoodsSpecSaveDTOList();
        //新规格
        List<GoodsSpecSaveDTO> newGoodsSpecSaveDTOList = new ArrayList<>();
        //库存修改记录
        List<GoodsSpecStorageUpdateDTO> goodsSpecStorageUpdateDTOList = new ArrayList<>();
        //价格修改记录
        List<GoodsSpecPriceUpdateDTO> goodsSpecPriceUpdateDTOList = new ArrayList<>();
        /**
         * 上面删除规格和中间表数据后 在这里修改规格和中间表数据
         *
         */
        if (delType) {
            //存在规格
            if (CollectionUtils.isNotEmpty(goodsSpecSaveDTOList)) {
                fillgoodsSpecName(dto.getGoodsSpecSaveDTOList(), dto.getSpecAttributeAndValueSaveDTOList());
                for (GoodsSpecSaveDTO goodsSpecSaveDTO : goodsSpecSaveDTOList) {
                    //根据规格关联的属性值判断
                    List<SpecAttributeRelationSaveDTO> specAttributeRelationSaveDTOList = goodsSpecSaveDTO.getSpecAttributeRelationSaveDTOList();
                    Long type = null;
                    //规格属性值list
                    List<Long> specValueIdList = new ArrayList<>();
                    for (SpecAttributeRelationSaveDTO specAttributeRelationSaveDTO : specAttributeRelationSaveDTOList) {
                        specValueIdList.add(specAttributeRelationSaveDTO.getSpecAttrValueId());
                    }
                    //获取specid
                    type = specAttributeRelationService.selectSpecId(specValueIdList, specValueIdList.size());
                    //原有sku
                    if (type != null) {
                        GoodsSpecDTO goodsSpecDTO = goodsSpecService.get(type);
                        goodsSpecSaveDTO.setId(type);
                        if (StringUtils.isNotBlank(goodsSpecSaveDTO.getSpecAttrName())) {
                            goodsSpecSaveDTO.setSpecName(dto.getGoodsName() + " " + goodsSpecSaveDTO.getSpecAttrName().replaceAll(",", " "));
                        } else {
                            goodsSpecSaveDTO.setSpecName(dto.getGoodsName());
                        }
                        goodsSpecService.updateSpec(goodsSpecSaveDTO);
                        //库存修改记录保存
                        GoodsSpecStorageUpdateDTO goodsSpecStorageUpdateDTO = saveStockLog(goodsSpecSaveDTO.getSpecStorage(),
                                goodsSpecDTO.getSpecStorage(), goodsSpecSaveDTO.getId(), goodsSpecSaveDTO.getSpecSerial());
                        goodsSpecStorageUpdateDTOList.add(goodsSpecStorageUpdateDTO);
                        //价格修改记录保存
                        GoodsSpecPriceUpdateDTO goodsSpecPriceUpdateDTO = savePicLog(goodsSpecSaveDTO.getId(), goodsSpecSaveDTO.getSpecSerial(),
                                goodsSpecSaveDTO.getSpecSellPrice(), goodsSpecSaveDTO.getSpecCostPrice(),
                                goodsSpecDTO.getSpecCostPrice(), goodsSpecDTO.getSpecSellPrice());
                        goodsSpecPriceUpdateDTOList.add(goodsSpecPriceUpdateDTO);
                    } else {
                        //新规格
                        newGoodsSpecSaveDTOList.add(goodsSpecSaveDTO);
                    }
                }


            } else {
                //默认规格
                // 查询数据库规格信息
                List<GoodsSpecDetailDTO> goodsSpecDetailDTOList = goodsSpecService.selectByGoodsId(dto.getId());
                if (CollectionUtils.isNotEmpty(goodsSpecDetailDTOList)) {
                    //修改
                    List<GoodsSpecSaveDTO> goodsSpecSaveDetailListDTO = ConvertUtils.sourceToTarget(goodsSpecDetailDTOList, GoodsSpecSaveDTO.class);


                    for (GoodsSpecSaveDTO goodsSpecSaveDTO : goodsSpecSaveDetailListDTO) {
                        //库存修改记录保存
                        GoodsSpecStorageUpdateDTO goodsSpecStorageUpdateDTO = saveStockLog(dto.getSpecStorage(),
                                goodsSpecSaveDTO.getSpecStorage(), goodsSpecSaveDTO.getId(), goodsSpecSaveDTO.getSpecSerial());
                        goodsSpecStorageUpdateDTOList.add(goodsSpecStorageUpdateDTO);
                        //价格修改记录保存
                        GoodsSpecPriceUpdateDTO goodsSpecPriceUpdateDTO = savePicLog(goodsSpecSaveDTO.getId(), goodsSpecSaveDTO.getSpecSerial(),
                                dto.getSpecSellPrice(), dto.getSpecCostPrice(),
                                goodsSpecSaveDTO.getSpecCostPrice(),
                                goodsSpecSaveDTO.getSpecSellPrice());
                        goodsSpecPriceUpdateDTOList.add(goodsSpecPriceUpdateDTO);

                        goodsSpecSaveDTO.setSpecName(dto.getGoodsName());
                        goodsSpecSaveDTO.setSpecStorage(dto.getSpecStorage());
                        goodsSpecSaveDTO.setSpecSellPrice(dto.getSpecSellPrice());
                        goodsSpecSaveDTO.setSpecCostPrice(dto.getSpecCostPrice());
                        goodsSpecSaveDTO.setSpecWeight(dto.getSpecWeight());
                        goodsSpecService.updateSpec(goodsSpecSaveDTO);
                    }
                    //修改规格主图
                    goodsSpecService.updateSpecMainPic(goodsSpecDetailDTOList.get(0).getId(), goodsMainPic);
                } else {
                    //新增
                    long id = IdWorker.getId();
                    List<GoodsSpecSaveDTO> defaultSpec = getDefaultSpec(dto, id, goodsMainPic);
                    goodsSpecService.saveBatch(defaultSpec);
                    goodsEntity.setSpecId(defaultSpec.get(0).getId());
                    baseDao.editGoods(goodsEntity);
                }
            }
        } else {
            //全删
            newGoodsSpecSaveDTOList = dto.getGoodsSpecSaveDTOList();
        }

        //记录
        String userName = SecurityUser.getUserName();
        if (CollectionUtils.isNotEmpty(goodsSpecStorageUpdateDTOList)) {
            //保存库存修改数据
            UpdateBatchStockLog updateBatchStockLog = new UpdateBatchStockLog();
            updateBatchStockLog.setGoodsSpecStorageUpdateDTOList(goodsSpecStorageUpdateDTOList);
            updateBatchStockLog.setUserName(userName);
            updateBatchStockLog.setType(1);
            rabbitMqSendService.sendMsg(MqConstant.LEIMINGTECH_STOCK_RECORD_QUEUE,
                    JSON.toJSONString(updateBatchStockLog));
        }
        if (CollectionUtils.isNotEmpty(goodsSpecPriceUpdateDTOList)) {
            //保存价格修改数据
            UpdateBatchPriceLog updateBatchPriceLog = new UpdateBatchPriceLog();
            updateBatchPriceLog.setGoodsSpecPriceUpdateDTOList(goodsSpecPriceUpdateDTOList);
            updateBatchPriceLog.setUserName(userName);
            rabbitMqSendService.sendMsg(MqConstant.LEIMINGTECH_PRICE_RECORD_QUEUE,
                    JSON.toJSONString(updateBatchPriceLog));
        }

        //新sku保存
        if (CollectionUtils.isNotEmpty(newGoodsSpecSaveDTOList)) {
            Long id = IdWorker.getId();
            List<GoodsSpecSaveDTO> goodsSpecSaveAddIdListDTO = fillgoodsSpec(newGoodsSpecSaveDTOList, goodsEntity.getId(), newSpecAttrValIds, id);
            fillgoodsSpecName(dto.getGoodsSpecSaveDTOList(), dto.getSpecAttributeAndValueSaveDTOList());
            //修改规格信息
            goodsSpecService.saveBatch(goodsSpecSaveAddIdListDTO);
            //-------------修改中间表---------------
            saveAttrValRelation(newGoodsSpecSaveDTOList);
        }

        //保存规格属性图片
        this.saveGoodsPic(dto.getSpecAttributePictureSaveDTOList(), dto.getSpecAttributePictureSaveListDefaultDTO(), newSpecAttrValIds, dto.getId());


        //默认id重新设置
        List<GoodsSpecDetailDTO> goodsSpecDetailDTOList = goodsSpecService.selectByGoodsId(dto.getId());
        if (CollectionUtils.isNotEmpty(goodsSpecDetailDTOList)) {
            goodsEntity.setSpecId(goodsSpecDetailDTOList.get(0).getId());
            GoodsSpecSaveDTO goodsSpecSaveDTO = new GoodsSpecSaveDTO();
            goodsSpecSaveDTO.setId(goodsSpecDetailDTOList.get(0).getId());
            goodsSpecSaveDTO.setMainFlag(1);
            goodsSpecService.updateSpec(goodsSpecSaveDTO);
            baseDao.editGoods(goodsEntity);
        }
        //存在与否都进行删除操作
        //删除以前定时任务
        goodsTimeService.deleteByGoodsId(Arrays.asList(dto.getId()));
        //定时发布保存
        if (GoodsStatusEnum.GOODS_PUBLISH_TIMING.value() == dto.getGoodsType()) {
            GoodsTimeDTO goodsTimeDTO = fillGoodsTime(dto.getShelfTime(), dto.getId(), dto.getStoreId(), dto.getGoodsType());
            if (dto.getType() == GoodsStatusEnum.IS_ADMIN.value()) {
                goodsTimeDTO.setOperatorType(GoodsTimeStatusEnum.OPERATOR_ADMIN.value());
            }
            goodsTimeService.save(goodsTimeDTO);
        }
        mlogger.info(GoodsStatusCode.GOODS_UPDATE_SUCCESS_CODE, GoodsStatusCode.GOODS_UPDATE_SUCCESS_UPDATE_GOODS_MSG + dto.getId());

        //如果规格删除同步es
        if (CollectionUtils.isNotEmpty(updateCartStatusDTO)) {
            rabbitMqSendService.sendMsg(MqConstant.LEIMINGTECH_CART_GOODS_STATUS,
                    JSON.toJSONString(updateCartStatusDTO));
        }
        //索引添加
        return dto.getId();
    }

    /**
     * 修改商品附加信息
     *
     * @param dto 商品编辑对象
     * @date 2020/1/8/008 14:52
     * @author xuzhch
     **/
    private void updateGoodsInfo(@RequestBody GoodsModifyDTO dto) {
        GoodsInfoDTO goodsInfoDTO = ConvertUtils.sourceToTarget(dto, GoodsInfoDTO.class);
        goodsInfoDTO.setId(dto.getId());
        goodsInfoService.update(goodsInfoDTO);
    }

    /**
     * 修改商品属性信息
     * 先删除后保存
     *
     * @param dto 商品编辑对象
     * @date 2020/1/6/006 14:30
     * @author xuzhch
     **/
    private void updateGoodsAttributeList(@RequestBody GoodsModifyDTO dto) {
        goodsAttributeService.deleteByGoodsId(dto.getId());
        List<GoodsAttributeSaveDTO> goodsAttributeSaveDTOList = dto.getGoodsAttributeDTOList();
        if (CollectionUtils.isNotEmpty(goodsAttributeSaveDTOList)) {
            for (GoodsAttributeSaveDTO goodsAttribute : goodsAttributeSaveDTOList) {
                goodsAttribute.setId(null);
                goodsAttribute.setGoodsId(dto.getId());
            }
        }
        goodsAttributeService.update(goodsAttributeSaveDTOList);
    }

    /**
     * 修改商品与标签信息
     *
     * @param dto 商品修改对象 ，goodsEntity 商品实体
     * @date 2019/12/30/030 18:00
     * @author xuzhch
     **/
    private void updateGoodsLabelRel(@RequestBody GoodsModifyDTO dto) {
        //修改商品标签关联表数据
        List<GoodsLabelRelSaveDTO> labelRelSaveDTOList = new ArrayList<>();
        if (null != dto.getLabelIds() && dto.getLabelIds().length > 0) {
            // 获得商品标签关联表保存对象
            List<GoodsLabelRelSaveDTO> saveDTOList = getGoodsLabelRel(dto.getLabelIds(), dto.getId());
            //删除原商品关联关系
            goodsLabelRelService.deleteByGoodsId(dto.getId());
            //批量新增
            goodsLabelRelService.insertBatch(saveDTOList);
        }
    }

    /**
     * 修改商品信息
     *
     * @param dto 商品修改对象 ，goodsEntity 商品实体
     * @return
     * @date 2019/12/30/030 18:00
     * @author xuzhch
     **/
    private GoodsEntity updateGoodEntity(@RequestBody GoodsModifyDTO dto) {
        GoodsEntity goodsEntity = ConvertUtils.sourceToTarget(dto, GoodsEntity.class);
        //审核状态设置  平台操作直接通过
        if (dto.getType() == GoodsStatusEnum.IS_ADMIN.value()) {
            goodsEntity.setGoodsStatus(GoodsStatusEnum.GOODS_AUDIT_PASS.value());
            goodsEntity.setInvoiceFlag(null);
            goodsEntity.setInvoiceContent(null);
            goodsEntity.setInvoiceContent(null);
        } else {
            StoreDTO storeDTO = storeService.get(dto.getStoreId());
            fillGoodsStatus(goodsEntity, dto.getGoodsType(), storeDTO.getStoreType());
        }

        //封装商品主图
        for (DefaultSpecPictureDTO defaultSpecPictureDTO : dto.getSpecAttributePictureSaveListDefaultDTO()) {
            if (defaultSpecPictureDTO.getIsMainPicture() == GoodsStatusEnum.IS_MAIN_PICTURE.value()) {
                goodsEntity.setGoodsMainPicture(defaultSpecPictureDTO.getPictureUrl());
                this.setGoodsCustomPicSize(goodsEntity);
            }
        }
        //每次编辑都需要重新上架
        goodsEntity.setGoodsShow(GoodsStatusEnum.GOODS_SHOW_NO.value());
        //保存
        baseDao.updateById(goodsEntity);
        Map<String, String> goodsLogMap = new HashMap<>(10);
        goodsLogMap.put("goodsId", goodsEntity.getId().toString());
        mlogger.info(GoodsStatusCode.GOODS_UPDATE_SUCCESS_CODE, GoodsStatusCode.GOODS_UPDATE_SUCCESS_MSG, goodsLogMap);
        return goodsEntity;
    }

    private void setGoodsCustomPicSize(GoodsEntity goodsEntity) {
        String pictureUrl = goodsEntity.getGoodsMainPicture();
        if (StringUtils.isNotBlank(pictureUrl)) {
            String[] split = pictureUrl.split("\\.");
            String suffix = split[split.length - 1];
            goodsEntity.setOneHundredPxPictureUrl(pictureUrl + CustomPictureSizeEnum.ONE_HUNDRED_PX.value() + suffix);
            goodsEntity.setTwoHundredPxPictureUrl(pictureUrl + CustomPictureSizeEnum.TWO_HUNDRED_PX.value() + suffix);
            goodsEntity.setFourHundredPxPictureUrl(pictureUrl + CustomPictureSizeEnum.FOUR_HUNDRED_PX.value() + suffix);
            goodsEntity.setEightHundredPxPictureUrl(pictureUrl + CustomPictureSizeEnum.EIGHT_HUNDRED_PX.value() + suffix);
        }
    }

    /**
     * 删除商品
     *
     * @param ids
     */
    @Override

    public void delete(@RequestBody Long[] ids) {
        super.delete(ids);
    }


    /**
     * 修改商品上下架状态
     *
     * @param id:        商品ID
     * @param goodsShow: 上下架状态 0下架 1上架
     */
    @Override

    public void updateShowNow(@RequestParam("id") Long id, @RequestParam("goodsShow") Integer goodsShow) {
        if (goodsShow == GoodsStatusEnum.GOODS_SHOW_ON.value()) {
            Date uptime = new Date();
            //立即上架
            baseDao.updateUpShow(id, uptime, goodsShow);
        } else {
            //立即下架
            baseDao.updateShow(id, goodsShow);
        }
        //删除定时表中对应的定时
        goodsTimeService.deleteByGoodsId(Arrays.asList(id));
        //商品上下架记录
        this.sendMqSaveGoodsRecord(Arrays.asList(id), goodsShow);
    }

    /**
     * 校验该店铺已发布的商品数量是否超出店铺等级允许的已发布数量
     *
     * @param goodsIds 商品ID
     * @return
     * @date 2019年11月6日
     * @author xuzhch
     */
    @Override

    public Boolean validatePublishGoodsNum(@RequestBody Long[] goodsIds) {

        Long storeId = this.get(goodsIds[0]).getStoreId();
        //判断店铺是否被禁用
        StoreDTO storeDTO = storeService.get(storeId);
        if (null == storeDTO) {
            throw new ServiceException(StoreStatusCode.STORE_IS_NOT_FOUND);
        }
        if (storeDTO.getIsEnable() == StoreEnum.IS_ENABLE_NO.value()) {
            throw new ServiceException(StoreStatusCode.STORE_IS_NULL_ENABLE);
        }

        // 判断商品操作状态是当前状态不一致的数量
        List<GoodsEntity> goodsEntities = baseDao.selectBatchIds(Arrays.asList(goodsIds));
        int publishCount = (int) goodsEntities.stream()
                .filter(p -> p.getGoodsShow() != GoodsStatusEnum.GOODS_SHOW_ON.value()).count();

        if (null != storeDTO.getGradeId()) {
            // 店铺已发布的商品数量
            Integer count = baseDao.selectCount(Wrappers.<GoodsEntity>lambdaQuery()
                    .eq(GoodsEntity::getStoreId, storeId)
                    .eq(GoodsEntity::getGoodsShow, GoodsStatusEnum.GOODS_SHOW_ON.value()));

            StoreGradeDTO storeGradeDTO = storeGradeService.get(storeDTO.getGradeId());
            //店铺等级允许发布的商品数量
            Integer goodsLimit = storeGradeDTO.getSgGoodsLimit();
            return count + publishCount <= goodsLimit;
        }
        return false;
    }


    @Override

    public Boolean adminValidatePublishGoodsNum(@RequestBody Long[] ids) {

        List<GoodsEntity> goodsEntities = baseDao.selectBatchIds(Arrays.asList(ids));
        Set<Long> storeIds = new TreeSet<>();
        for (GoodsEntity goodsEntity : goodsEntities) {
            storeIds.add(goodsEntity.getStoreId());
        }
        for (Long id : storeIds) {
            Integer storeStatus = storeService.findStoreStatus(id);
            if (null == storeStatus) {
                throw new ServiceException(StoreStatusCode.STORE_IS_NOT_FOUND);
            }
            if (storeStatus == 1) {
                throw new ServiceException(StoreStatusCode.STORE_IS_NULL_ENABLE);
            }
        }
        Long[] storeLongIds = storeIds.toArray(new Long[storeIds.size()]);
        List<StoreDTO> storeDTOList = storeService.selectStoresByIds(storeLongIds);
        for (StoreDTO storeDTO : storeDTOList) {
            Integer currentCount = baseDao.selectCount(new QueryWrapper<GoodsEntity>()
                    .eq("store_id", storeDTO.getId()).eq("goods_show", GoodsStatusEnum.GOODS_SHOW_ON.value()).eq("del_flag", 0));
            StoreGradeDTO storeGradeDTO = storeGradeService.get(storeDTO.getGradeId());
            if (null == storeGradeDTO || null == storeGradeDTO.getSgGoodsLimit()) {
                continue;
            }
            Integer maxCount = storeGradeDTO.getSgGoodsLimit();
            int count = 0;
            for (GoodsEntity goodsEntity : goodsEntities) {
                if ((goodsEntity.getStoreId().compareTo(storeDTO.getId())) == 0) {
                    count++;
                }
            }
            boolean flag = (count + currentCount) < maxCount ? true : false;
            if (flag) {
                continue;
            } else {
                return false;
            }
        }

        return true;
    }


    /**
     * 定时任务调用修改上下架/发布 状态
     * 1、先判断是否有可执行的定时任务
     * 2、修改上下架或发布状态
     *
     * @return 店铺ID集合
     * @date 2019/12/31/031 10:15
     * @author xuzhch
     **/
    @Override

    public List<Long> updateShowTiming() {
        //查询所有可执行定时
        List<GoodsTimeDTO> goodsTimeDTOList = goodsTimeService.selectList(new Date(),
                GoodsTimeStatusEnum.GOODS.value());
        List<Long> list = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(goodsTimeDTOList)) {
            for (GoodsTimeDTO goodsTimeDTO : goodsTimeDTOList) {
                if (goodsTimeDTO.getOperationalStatus() == GoodsTimeStatusEnum.GOODS_TIME_STATUS_SHOW.value()) {
                    //执行上下架操作
                    updateGoodsShow(goodsTimeDTO.getGoodsId(), goodsTimeDTO.getShowStatus());
                    list.add(goodsTimeDTO.getGoodsId());
                } else {
                    updateStatusTiming(goodsTimeDTO.getGoodsId(), goodsTimeDTO.getOperatorType());
                    list.add(goodsTimeDTO.getGoodsId());
                }
            }
            //删除定时任务
            goodsTimeService.deleteByGoodsId(list);
        }
        return list;

    }

    /**
     * 修改当前的商品上下架状态
     * id   商品id
     */
    private Long updateGoodsShow(Long id, Integer showStatus) {
        GoodsEntity goodsEntity = baseDao.selectById(id);
        if (goodsEntity != null) {
            if (showStatus == GoodsStatusEnum.GOODS_SHOW_ON.value()) {
                Boolean flag = this.validatePublishGoodsNum(new Long[]{id});
                if (!flag) {
                    throw new ServiceException(GoodsStatusCode.GOODS_PUBLISH_COUNT_OUT);
                }
                Date uptime = new Date();
                baseDao.updateUpShow(id, uptime, showStatus);
            } else {
                baseDao.updateShow(id, showStatus);
            }
            goodsSpecService.updateShowByGoodsId(id, showStatus);
            //保存上下架记录
            this.sendMqSaveGoodsRecord(Arrays.asList(id), showStatus);

            return id;
        }
        return null;

    }

    /**
     * 定时修改发布状态为待审核
     *
     * @param id
     */

    @Override
    public void updateStatusTiming(Long id, @RequestParam("operatorType") Integer operatorType) {
        GoodsEntity goodsEntity = baseDao.selectById(id);
        if (operatorType == GoodsTimeStatusEnum.OPERATOR_ADMIN.value()) {
            //平台操作
            goodsEntity.setGoodsStatus(GoodsStatusEnum.GOODS_AUDIT_PASS.value());
        } else {
            StoreDTO storeDTO = storeService.get(goodsEntity.getStoreId());
            fillGoodsStatus(goodsEntity, GoodsStatusEnum.GOODS_PUBLISH_NOW.value(), storeDTO.getStoreType());
        }

        baseDao.updateById(goodsEntity);
    }


    /**
     * 审核商品
     *
     * @param dto 商品审核对象
     * @return
     * @date 2019/12/31/031 10:19
     * @author xuzhch
     **/

    @Override
    public void checkGoodsStatus(@RequestBody GoodsCheckSaveDTO dto) {
        List<Long> ids = Arrays.asList(dto.getGoodsIds());
        baseDao.updateBatch(ids, dto.getGoodState());
        // 保存审核表
        ids.forEach(goodsId -> {
            GoodsEntity goodsEntity = baseDao.selectById(goodsId);
            if (goodsEntity != null) {
                GoodsCheckDTO goodsCheckDTO = new GoodsCheckDTO();
                BeanCopier.create(GoodsCheckSaveDTO.class, GoodsCheckDTO.class, false).copy(dto, goodsCheckDTO, null);
                goodsCheckDTO.setGoodsId(goodsId);
                goodsCheckDTO.setGoodsName(goodsEntity.getGoodsName());
                if (dto.getGoodState() == GoodsStatusEnum.GOODS_AUDIT_NOTPASS.value()) {
                    goodsCheckDTO.setOperationType(GoodCheckEnum.GOOD_CHECK_FAIL.getValue());
                } else if (dto.getGoodState() == GoodsStatusEnum.GOODS_AUDIT_PASS.value()) {
                    goodsCheckDTO.setOperationType(GoodCheckEnum.GOOD_CHECK_PASS.getValue());
                }
                goodsCheckService.save(goodsCheckDTO);
            }
        });
    }

    /**
     * 校验货号是否重复
     *
     * @param id
     * @return
     */

    @Override
    public Map<String, Object> checkGoodsSerial(Long id) {
        Map<String, Object> res = new HashMap<>(10);
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("goods_serial", id);
        GoodsEntity goodsEntity = baseDao.selectOne(wrapper);
        if (ObjectUtil.isNotNull(goodsEntity)) {
            res.put("code", GoodsErrorCodeEnum.SERIALD_REPEAT.code());
            res.put("msg", GoodsErrorCodeEnum.SERIALD_REPEAT.msg());
        }
        return res;
    }

    /**
     * 填充商品规格表字段   修改
     *
     * @param goodsSpecSaveDTOList 规格实体
     * @param goodsid              商品id
     * @param newIdMap             替换id
     * @return
     */
    private List<GoodsSpecSaveDTO> fillgoodsSpec(List<GoodsSpecSaveDTO> goodsSpecSaveDTOList, Long goodsid, Map<Long, Long> newIdMap, Long specId) {
        for (GoodsSpecSaveDTO goodsSpec : goodsSpecSaveDTOList) {
            goodsSpec.setGoodsId(goodsid);
            goodsSpec.setId(IdWorker.getId());
            List<SpecAttributeRelationSaveDTO> specAttributeRelationSaveDTOList = goodsSpec.getSpecAttributeRelationSaveDTOList();
            for (SpecAttributeRelationSaveDTO specAttributeRelationSaveDTO : specAttributeRelationSaveDTOList) {
                if (newIdMap.get(specAttributeRelationSaveDTO.getSpecAttrId()) != null) {
                    specAttributeRelationSaveDTO.setSpecAttrId(newIdMap.get(specAttributeRelationSaveDTO.getSpecAttrId()));
                }
                if (newIdMap.get(specAttributeRelationSaveDTO.getSpecAttrValueId()) != null) {
                    specAttributeRelationSaveDTO.setSpecAttrValueId(newIdMap.get(specAttributeRelationSaveDTO.getSpecAttrValueId()));
                }

            }
        }
        //重置默认规格id
        goodsSpecSaveDTOList.get(0).setId(specId);
        goodsSpecSaveDTOList.get(0).setMainFlag(1);
        return goodsSpecSaveDTOList;
    }

    /**
     * 获取默认规格  (保存)
     *
     * @return
     */
    private List<GoodsSpecSaveDTO> getDefaultSpec(GoodsConserveDTO dto, Long specId, Long goodsId, String goodsMainPic) {
        //todo 生成默认规格
        GoodsSpecSaveDTO goodsSpecSaveDTO = new GoodsSpecSaveDTO();
        goodsSpecSaveDTO.setId(specId);
        goodsSpecSaveDTO.setGoodsId(goodsId);
        goodsSpecSaveDTO.setSpecName(dto.getGoodsName());
        goodsSpecSaveDTO.setSpecMainPicture(goodsMainPic);
        goodsSpecSaveDTO.setSpecSellPrice(dto.getSpecSellPrice());
        goodsSpecSaveDTO.setSpecCostPrice(dto.getSpecCostPrice());
        //todo  sku
        Long minute = System.currentTimeMillis();
        String serial = String.valueOf(minute);
        String substring = serial.substring(serial.length() - 10);
        goodsSpecSaveDTO.setSpecSerial(substring);
        goodsSpecSaveDTO.setSpecStorage(dto.getSpecStorage());
        goodsSpecSaveDTO.setMainFlag(1);
        goodsSpecSaveDTO.setSpecWeight(dto.getSpecWeight());
        List<GoodsSpecSaveDTO> list = new ArrayList<>();
        list.add(goodsSpecSaveDTO);
        return list;
    }

    /**
     * 获取默认规格  (修改)
     *
     * @return
     */
    private List<GoodsSpecSaveDTO> getDefaultSpec(GoodsModifyDTO dto, Long specId, String specMainPic) {
        //todo 生成默认规格
        GoodsSpecSaveDTO goodsSpecSaveDTO = new GoodsSpecSaveDTO();
        goodsSpecSaveDTO.setId(specId);
        goodsSpecSaveDTO.setGoodsId(dto.getId());
        goodsSpecSaveDTO.setSpecName(dto.getGoodsName());
        goodsSpecSaveDTO.setSpecMainPicture(specMainPic);
        goodsSpecSaveDTO.setSpecSellPrice(dto.getSpecSellPrice());
        goodsSpecSaveDTO.setSpecCostPrice(dto.getSpecCostPrice());
        goodsSpecSaveDTO.setMainFlag(1);

        //todo  sku
        Long minute = System.currentTimeMillis();
        String serial = String.valueOf(minute);
        String substring = serial.substring(serial.length() - 10);
        goodsSpecSaveDTO.setSpecSerial(substring);
        goodsSpecSaveDTO.setSpecStorage(dto.getSpecStorage());
        List<GoodsSpecSaveDTO> list = new ArrayList<>();
        list.add(goodsSpecSaveDTO);
        return list;
    }

    /**
     * 填充商品属性和属性值表
     *
     * @param list
     * @param goodsId
     * @return
     */
    private List<SpecAttributeAndValueSaveDTO> fillSpecAttributeAndValue(List<SpecAttributeAndValueSaveDTO> list, Long goodsId) {
        for (SpecAttributeAndValueSaveDTO specAttributeAndValueSaveDTO : list) {
            specAttributeAndValueSaveDTO.setGoodsId(goodsId);
        }
        return list;
    }


    /**
     * 填充定时表数据,商品发布操作
     */
    private GoodsTimeDTO fillGoodsTime(Date shelfTime, Long id, Long storeId, Integer goodsType) {
        GoodsTimeDTO goodsTimeDTO = new GoodsTimeDTO();
        goodsTimeDTO.setShelfTime(shelfTime);
        goodsTimeDTO.setGoodsId(id);
        goodsTimeDTO.setStoreId(storeId);
        goodsTimeDTO.setShowStatus(2);
        if (GoodsStatusEnum.GOODS_PUBLISH_TIMING.value() == goodsType) {
            goodsTimeDTO.setOperationalStatus(GoodsTimeStatusEnum.GOODS_TIME_STATUS_PUBLISH.value());
        }
        return goodsTimeDTO;
    }


    /**
     * 生成id  替换旧id(save)
     *
     * @param list
     * @return
     */
    private Map<Long, Long> replaceIdSave(List<SpecAttributeAndValueSaveDTO> list) {
        //新id map：  页面id ： 数据库id
        Map<Long, Long> newIdMap = new HashMap<>(10);
        //替换属性值
        Long flag = 0L;
        for (SpecAttributeAndValueSaveDTO specAttributeAndValueSaveDTO : list) {
            if (flag.compareTo(specAttributeAndValueSaveDTO.getSpecAttrId()) != 0) {
                newIdMap.put(specAttributeAndValueSaveDTO.getSpecAttrId(), IdWorker.getId());
                flag = specAttributeAndValueSaveDTO.getSpecAttrId();
            }
            //key  前端表示ID   value 保存数据库属性ID
            //生成属性值ID
            Long specAttrValueId = IdWorker.getId();
            //获取被选中的id存入map中
            //如果商品规格是选中的 就把属性值ID放进Map
            specAttributeAndValueSaveDTO.setSpecAttrId(newIdMap.get(specAttributeAndValueSaveDTO.getSpecAttrId()));
            if (specAttributeAndValueSaveDTO.getIsSelect() == GoodsStatusEnum.SPEC_ATTR_CHECKED.value()) {
                newIdMap.put(specAttributeAndValueSaveDTO.getSpecAttrValueId(), specAttrValueId);
            } else {
                //删除未勾选的属性
                newIdMap.remove(specAttributeAndValueSaveDTO.getSpecAttrId());
            }
            //设置属性值Id
            specAttributeAndValueSaveDTO.setSpecAttrValueId(specAttrValueId);
        }
        return newIdMap;
    }

    /**
     * 生成id  替换旧id
     */
    private Map<Long, Long> replaceId(List<SpecAttributeAndValueSaveDTO> dtoList) {
        HashMap<Long, Long> newIdMap = new HashMap<>(10);
        if (CollectionUtils.isNotEmpty(dtoList)) {
            for (SpecAttributeAndValueSaveDTO specAttributeAndValueSaveDTO : dtoList) {
                //addType:修改操作填写 是否新增 0:原有; 1:新增属性值  2:新增属性和属性值

                if (specAttributeAndValueSaveDTO.getAddType() == GoodsStatusEnum.GOODS_ADD_NEW.value()) {
                    //新值旧属性
                    Long specAttrValId = IdWorker.getId();
                    //获取被选中的id存入map中
                    if (specAttributeAndValueSaveDTO.getIsSelect() == GoodsStatusEnum.SPEC_ATTR_CHECKED.value()) {
                        newIdMap.put(specAttributeAndValueSaveDTO.getSpecAttrValueId(), specAttrValId);
                    } else {
                        //删除未勾选的属性
                        newIdMap.remove(specAttributeAndValueSaveDTO.getSpecAttrId());
                    }
                    specAttributeAndValueSaveDTO.setSpecAttrValueId(specAttrValId);

                } else {
                    //新值新属性
                    Long specAttrValId = IdWorker.getId();
                    //获取被选中的id存入map中
                    if (specAttributeAndValueSaveDTO.getIsSelect() == GoodsStatusEnum.SPEC_ATTR_CHECKED.value()) {
                        newIdMap.put(specAttributeAndValueSaveDTO.getSpecAttrValueId(), specAttrValId);
                    }
                    newIdMap.put(specAttributeAndValueSaveDTO.getSpecAttrId(), IdWorker.getId());
                    specAttributeAndValueSaveDTO.setSpecAttrValueId(specAttrValId);
                    specAttributeAndValueSaveDTO.setSpecAttrId(newIdMap.get(specAttributeAndValueSaveDTO.getSpecAttrId()));
                }
            }
        }
        return newIdMap;
    }


//    private Map<Long, Long> replaceId(List<SpecAttributeAndValueSaveDTO> dtoList) {
//        HashMap<Long, Long> newIdMap = new HashMap<>(10);
//        if (CollectionUtils.isNotEmpty(dtoList)) {
//            for (SpecAttributeAndValueSaveDTO specAttributeAndValueSaveDTO : dtoList) {
//                if (specAttributeAndValueSaveDTO.getAddType() == GoodsStatusEnum.GOODS_ADD_NEW.value()) {
//                    //新值旧属性
//                    Long specAttrValId = IdWorker.getId();
//                    //获取被选中的id存入map中
//                    if (specAttributeAndValueSaveDTO.getIsSelect() == GoodsStatusEnum.SPEC_ATTR_CHECKED.value()) {
//                        newIdMap.put(specAttributeAndValueSaveDTO.getSpecAttrValueId(), specAttrValId);
//                    }
//                    specAttributeAndValueSaveDTO.setSpecAttrValueId(specAttrValId);
//                } else {
//                    //新值新属性
//                    Long specAttrValId = IdWorker.getId();
//                    //获取被选中的id存入map中
//                    if (specAttributeAndValueSaveDTO.getIsSelect() == GoodsStatusEnum.SPEC_ATTR_CHECKED.value()) {
//                        newIdMap.put(specAttributeAndValueSaveDTO.getSpecAttrValueId(), specAttrValId);
//                    }
//                    newIdMap.put(specAttributeAndValueSaveDTO.getSpecAttrId(), IdWorker.getId());
//                    specAttributeAndValueSaveDTO.setSpecAttrValueId(specAttrValId);
//                }
//            }
//            for (SpecAttributeAndValueSaveDTO specAttributeAndValueSaveDTO : dtoList) {
//                if (specAttributeAndValueSaveDTO.getAddType() == GoodsStatusEnum.GOODS_ADD_NEWS.value()) {
//                    //新值新属性
//                    specAttributeAndValueSaveDTO.setSpecAttrId(newIdMap.get(specAttributeAndValueSaveDTO.getSpecAttrId()));
//                    //删除未勾选的属性
//                    if (specAttributeAndValueSaveDTO.getIsSelect() == GoodsStatusEnum.SPEC_ATTR_UNCHECKED.value()) {
//                        newIdMap.remove(specAttributeAndValueSaveDTO.getSpecAttrId());
//                    }
//                }
//
//            }
//        }
//        return newIdMap;
//    }

    /**
     * 规格属性图片id替换
     */
    private List<SpecAttributePictureSaveDTO> replacePictuerId(List<SpecAttributePictureSaveDTO> list, Map<Long, Long> newIdMap) {
        for (SpecAttributePictureSaveDTO specAttributePictureSaveDTO : list) {
            if (newIdMap.get(specAttributePictureSaveDTO.getSpecAttrValueId()) != null) {
                specAttributePictureSaveDTO.setSpecAttrValueId(newIdMap.get(specAttributePictureSaveDTO.getSpecAttrValueId()));
            }
            if (newIdMap.get(specAttributePictureSaveDTO.getSpecAttrId()) != null) {
                specAttributePictureSaveDTO.setSpecAttrId(newIdMap.get(specAttributePictureSaveDTO.getSpecAttrId()));
            }

        }
        return list;
    }

    /**
     * 填充图片数据
     *
     * @param list
     * @return
     */
    private void fillSpecAttrPic(List<SpecAttributePictureSaveDTO> list, Long id) {
        for (SpecAttributePictureSaveDTO specAttributePictureSaveDTO : list) {
            specAttributePictureSaveDTO.setGoodsId(id);
            String pictureUrl = specAttributePictureSaveDTO.getPictureUrl();
            if (StringUtils.isNotBlank(pictureUrl)) {
                String[] split = pictureUrl.split("\\.");
                String s = split[split.length - 1];
                specAttributePictureSaveDTO.setOneHundredPxPictureUrl(pictureUrl + CustomPictureSizeEnum.ONE_HUNDRED_PX.value() + s);
                specAttributePictureSaveDTO.setTwoHundredPxPictureUrl(pictureUrl + CustomPictureSizeEnum.TWO_HUNDRED_PX.value() + s);
                specAttributePictureSaveDTO.setFourHundredPxPictureUrl(pictureUrl + CustomPictureSizeEnum.FOUR_HUNDRED_PX.value() + s);
                specAttributePictureSaveDTO.setEightHundredPxPictureUrl(pictureUrl + CustomPictureSizeEnum.EIGHT_HUNDRED_PX.value() + s);
            }
        }
    }


    /**
     * 根据店铺Id查询商品数量
     *
     * @param storeId
     * @return
     */

    @Override
    public Integer selectCount(Long storeId) {

        return baseDao.selectCount(new QueryWrapper<GoodsEntity>().eq("store_id", storeId));
    }


    /**
     * 功能描述:
     * 〈根据规格id查询商品〉
     *
     * @param specId 规格id
     * @author : 刘远杰
     */

    @Override
    public GoodsDTO findGoodsBySpecId(@RequestParam("specId") Long specId) {
        GoodsEntity goodsEntity = baseDao.findGoodsBySpecId(specId);
        return ConvertUtils.sourceToTarget(goodsEntity, GoodsDTO.class);
    }

    /**
     * @Author: weixianchun
     * @Description: 根据品牌id查询品牌数量
     * @Date :2019/6/26 10:41
     * @Param brandId 品牌id
     * @Version V1.0
     **/

    @Override
    public int findCountByBrandId(@RequestParam("brandId") Long brandId) {
        //根据品牌id查询品牌数量
        return baseDao.findCountByBrandId(brandId);
    }

    /**
     * @Author: weixianchun
     * @Description: 根据分类id查询分类名称
     * @Date :2019/7/8 11:51
     * @Param classIds 分类id
     * @Version V1.0
     **/

    @Override
    public List<String> findCountByClassId(@RequestBody Long[] classIds) {
        //根据分类id查询分类名称
        return baseDao.findCountByClassId(classIds);
    }

    /**
     * 发送mq保存商品记录
     *
     * @param ids
     */
    private void sendMqSaveGoodsRecord(List<Long> ids, Integer goodsShow) {
        List<GoodsRecordDTO> goodsRecordDTOList = new ArrayList<>();
        for (Long id : ids) {
            GoodsRecordDTO goodsRecordDTO = new GoodsRecordDTO();
            goodsRecordDTO.setGoodsShow(goodsShow);
            goodsRecordDTO.setGoodsId(id);
            goodsRecordDTOList.add(goodsRecordDTO);
        }
        rabbitMqSendService.sendMsg(MqConstant.LEIMINGTECH_GOODS_SHOW_QUEUE,
                JSONArray.toJSONString(goodsRecordDTOList));
    }


    /**
     * 修改商品表中店铺名称
     *
     * @param storeName 店铺名称
     * @param storeId   店铺ID
     * @return
     * @date 2019/12/31/031 10:25
     * @author xuzhch
     **/
    @Override

    public void updateStoreName(@RequestParam("storeName") String storeName,
                                @RequestParam("storeId") Long storeId,
                                @RequestParam("storeType") Integer storeType) {
        baseDao.updateStoreName(storeName, storeId, storeType);
    }

    /**
     * 删除店铺下的商品
     *
     * @param ids 店铺ID
     */

    @Override
    public void deleteByStoreId(@RequestBody Long[] ids) {
        baseDao.deleteByStoreId(ids);
    }

    /**
     * @Author: weixianchun
     * @Description: 根据(默认)规格id修改商品价格
     * @Date :2019/7/16 21:33
     * @Param specId 规格id
     * @Param specSellPrice 销售价
     * @Param specCostPrice 成本价
     * @Version V1.0
     **/

    @Override
    public void updatePriceBySpecId(@RequestParam("specId") Long specId,
                                    @RequestParam("specSellPrice") BigDecimal specSellPrice,
                                    @RequestParam("specCostPrice") BigDecimal specCostPrice) {
        baseDao.updatePriceBySpecId(specId, specSellPrice, specCostPrice);
    }


    /**
     * 审核填充
     *
     * @param goodsEntity 商品
     * @param goodsType   商品状态 立即 定时 草稿
     * @param storeType   店铺类型 自营 普通
     */
    private void fillGoodsStatus(GoodsEntity goodsEntity, Integer goodsType, Integer storeType) {
        /**
         *商品审核逻辑
         *      立即发布: 自营店铺 通过 30
         *               普通店铺 获取商品审核系统设置开关,开:待审核 10, 关:审核通过 30
         *      定时发布:设置状态为:未发布 50
         */
        if (GoodsStatusEnum.GOODS_PUBLISH_NOW.value() == goodsType) {
            GoodsCheckDTO goodsCheckDTO = new GoodsCheckDTO();
            goodsCheckDTO.setGoodsId(goodsEntity.getId());
            goodsCheckDTO.setGoodsName(goodsEntity.getGoodsName());
            goodsCheckDTO.setOperationType(GoodCheckEnum.GOOD_CHECK_SUB.getValue());

            // 定时任务没有发布人   加个判断插入发布人
            if (StringUtils.isNotBlank(goodsEntity.getCreator())) {
                goodsCheckDTO.setCreator(goodsEntity.getCreator());
            }
            //立即发布设置为待审核
            if (storeType != StoreEnum.SELF_STORE.value()) {
                // 获取商品审核系统设置开关 进行判断
                String queryRedisByName = settingService.queryRedisByName(SettingsEnum.GOODS_AUDIT.value());
                SettingGoodsAuditDTO goodsAuditSet = JSON.parseObject(queryRedisByName, SettingGoodsAuditDTO.class);

                if (goodsAuditSet != null &&
                        SettingsEnum.GOODS_AUDIT_OPEN.value().equals(goodsAuditSet.getGoodsAuditOpen())) {
                    // 商品审核开关打开设置为待审核
                    goodsEntity.setGoodsStatus(GoodsStatusEnum.GOODS_AUDIT_WAIT.value());
                    goodsCheckDTO.setGoodState(GoodsStatusEnum.GOODS_AUDIT_WAIT.value());
                    goodsCheckService.save(goodsCheckDTO);
                } else {
                    // 商品审核开关关闭设置为审核通过
                    goodsEntity.setGoodsStatus(GoodsStatusEnum.GOODS_AUDIT_PASS.value());
                    goodsCheckDTO.setGoodState(GoodsStatusEnum.GOODS_AUDIT_PASS.value());
                    // 商品发布保存发布记录  默认系统审核并保存审核记录
                    goodsCheckService.saveBatch(goodsCheckDTO, 1);
                }
            } else {
                goodsEntity.setGoodsStatus(GoodsStatusEnum.GOODS_AUDIT_PASS.value());
                goodsCheckDTO.setGoodState(GoodsStatusEnum.GOODS_AUDIT_PASS.value());
                // 商品发布保存发布记录  默认系统审核并保存审核记录
                goodsCheckService.saveBatch(goodsCheckDTO, 2);
            }

        } else if (GoodsStatusEnum.GOODS_PUBLISH_DRAFT.value() == goodsType) {
            //说明是草稿
            goodsEntity.setGoodsStatus(GoodsStatusEnum.GOODS_DRAFT.value());
        } else {
            goodsEntity.setGoodsStatus(GoodsStatusEnum.GOODS_AUDIT_UNPUBLISHED.value());
        }

    }


    /**
     * 保存中间表
     *
     * @param goodsSpecSaveDTOList 规格list
     */
    private void saveAttrValRelation(List<GoodsSpecSaveDTO> goodsSpecSaveDTOList) {
        List<SpecAttributeRelationDTO> specAttributeRelationDTOList = new ArrayList<SpecAttributeRelationDTO>();

        //日志
        if (CollectionUtils.isNotEmpty(goodsSpecSaveDTOList)) {
            for (GoodsSpecSaveDTO goodsSpecSaveDTO : goodsSpecSaveDTOList) {
                //获取属性规格关联
                List<SpecAttributeRelationSaveDTO> specAttributeRelationSaveDTOList =
                        goodsSpecSaveDTO.getSpecAttributeRelationSaveDTOList();
                for (SpecAttributeRelationSaveDTO specAttributeRelationSaveDTO : specAttributeRelationSaveDTOList) {
                    //放入关联表集合
                    SpecAttributeRelationDTO specAttributeRelationDTO = new SpecAttributeRelationDTO();
                    specAttributeRelationDTO.setSpecId(goodsSpecSaveDTO.getId());
                    specAttributeRelationDTO.setGoodsId(goodsSpecSaveDTO.getGoodsId());
                    specAttributeRelationDTO.setSpecAttrId(specAttributeRelationSaveDTO.getSpecAttrId());
                    specAttributeRelationDTO.setSpecAttrValueId(specAttributeRelationSaveDTO.getSpecAttrValueId());
                    specAttributeRelationDTO.setIsMain(specAttributeRelationSaveDTO.getIsMain());
                    specAttributeRelationDTOList.add(specAttributeRelationDTO);
                }
            }
            specAttributeRelationService.save(specAttributeRelationDTOList);
        }
    }

    /**
     * 保存库存价格日志
     *
     * @param specStorage       规格库存
     * @param beforeSpecStorage 修改前库存
     * @param id                规格id
     */
    private GoodsSpecStorageUpdateDTO saveStockLog(Integer specStorage, Integer beforeSpecStorage,
                                                   Long id, String specSerial) {
        //库存修改记录保存
        GoodsSpecStorageUpdateDTO goodsSpecStorageUpdateDTO = new GoodsSpecStorageUpdateDTO();
        goodsSpecStorageUpdateDTO.setSpecStorage(specStorage);
        goodsSpecStorageUpdateDTO.setBeforeSpecStorage(beforeSpecStorage);
        goodsSpecStorageUpdateDTO.setId(id);
        goodsSpecStorageUpdateDTO.setSpecSerial(specSerial);
        return goodsSpecStorageUpdateDTO;
    }

    /**
     * 保存价格日志
     *
     * @param id                  规格id
     * @param specSerial          规格编号
     * @param sellPrice           售价
     * @param costPrice           成本价
     * @param beforeSpecCostPrice 修改前成本价
     * @param specSellPrice       修改前售价
     * @return
     */
    private GoodsSpecPriceUpdateDTO savePicLog(Long id, String specSerial,
                                               BigDecimal sellPrice, BigDecimal costPrice,
                                               BigDecimal beforeSpecCostPrice, BigDecimal specSellPrice) {
        //价格修改记录保存
        GoodsSpecPriceUpdateDTO goodsSpecPriceUpdateDTO = new GoodsSpecPriceUpdateDTO();
        goodsSpecPriceUpdateDTO.setId(id);
        goodsSpecPriceUpdateDTO.setSpecSerial(specSerial);
        goodsSpecPriceUpdateDTO.setSpecSellPrice(sellPrice);
        goodsSpecPriceUpdateDTO.setSpecCostPrice(costPrice);
        goodsSpecPriceUpdateDTO.setBeforeSpecCostPrice(beforeSpecCostPrice);
        goodsSpecPriceUpdateDTO.setBeforeSpecSellPrice(specSellPrice);
        return goodsSpecPriceUpdateDTO;
    }


    /**
     * 校验商品货号是否重复
     *
     * @param goodsSerials
     * @return 返回重复的货号集合
     * @author xuzhch 2019年11月18日
     */
    @Override

    public List<String> validSpecSerials(@RequestBody ValidSpecSerial goodsSerials) {
        return goodsSpecService.checkSpecSerial(goodsSerials);
    }

    /**
     * 校验商品参数是否合法
     *
     * @param dto
     * @return 返回状态码和信息
     * @author xuzhch 2019年11月18日
     */
    @Override

    public Map<String, String> validGoodsParams(@RequestBody GoodsConserveDTO dto) {
        List<Long> classIds = new ArrayList<>();
        classIds.add(dto.getFirstGcId());
        classIds.add(dto.getSecondGcId());
        classIds.add(dto.getThirdGcId());
        List<GoodsClassQueryNameDTO> goodsClassQueryNameDTOList = goodsClassService.selectListByClassId(classIds);
        if (goodsClassQueryNameDTOList.size() < classIds.size()) {
            throw new ServiceException(GoodsStatusCode.GOODS_CLASS_IS_DEL);
        }
        return null;
    }

    /**
     * 根据店铺ID 下架所有的商品，规格
     *
     * @param storeId
     */

    @Override
    public void updateShowByStoreId(@RequestParam("storeId") Long storeId) {
        //下架商品
        baseDao.updateShowByStoreId(storeId);
        // 下架商品所有规格
        baseDao.updateGoodsSpecShow(storeId);
        // 拒绝所有待审核商品
        baseDao.updateStatus(storeId);
        //删除定时上下架商品
        baseDao.deleteGoodsTime(storeId);

    }

    /**
     * 查询店铺下的商品数量
     *
     * @param storeId 店铺ID
     * @param type    商品状态  1 审核拒绝 2 审核通过
     * @return 商品数量
     * @date 2019年12月31日10:52
     * @author xuzhch
     */

    @Override
    public Integer findGoodsCount(@RequestParam("storeId") Long storeId, @RequestParam(value = "type", required = false) Integer type) {
        return baseDao.findStoreGoodsNum(storeId, type);
    }

    /**
     * 根据后台分类ID集合 查询商品数量 并按照分类ID分组
     *
     * @param classLists 分类ID集合
     * @return
     * @date 2019/12/31/031 10:49
     * @author xuzhch
     **/
    @Override

    public List<Map<String, Object>> selectCountByClassIds(@RequestBody List<Long> classLists) {
        return baseDao.selectCountByClassIds(classLists);
    }

    /**
     * 商品审核列表
     *
     * @param params 参数
     * @return
     * @date 2020/1/7/007 14:27
     * @author xuzhch
     **/
    @Override

    public PageData<GoodsListDTO> pageVerify(@RequestParam Map<String, Object> params) {
        int pageNum = Integer.valueOf(params.get(Constant.PAGE).toString());
        int limit = Integer.valueOf(params.get(Constant.LIMIT).toString());
        Page<GoodsListDTO> goodsListPageDTO = new Page<>(pageNum, limit);
        List<GoodsListDTO> page = baseDao.pageVerify(
                goodsListPageDTO,
                params

        );
        return new PageData(page, goodsListPageDTO.getTotal());
    }

    /**
     * 功能描述:
     * <根据标签推荐id查询关联商品信息>
     *
     * @param params 标签推荐id
     * @return 标签关联的商品信息
     * @date 2020/1/10 9:46
     * @author weixianchun
     **/

    @Override
    public PageData<GoodsListDTO> findByLabelId(@RequestParam Map<String, Object> params) {

        int pageNum = Integer.valueOf(params.get(Constant.PAGE).toString());
        int limit = Integer.valueOf(params.get(Constant.LIMIT).toString());
        Page<GoodsListDTO> goodsListPageDTO = new Page<>(pageNum, limit);
        List<GoodsListDTO> page = baseDao.findByLabelId(goodsListPageDTO, params);

        return new PageData(page, goodsListPageDTO.getTotal());
    }

    /**
     * 功能描述:
     * <查询未关联商品信息并分页>
     *
     * @param params
     * @return 标签未关联的商品信息
     * @date 2020/1/10 15:28
     * @author weixianchun
     **/

    @Override
    public PageData<GoodsListDTO> labelUnassociated(@RequestParam Map<String, Object> params) {

        int pageNum = Integer.valueOf(params.get(Constant.PAGE).toString());
        int limit = Integer.valueOf(params.get(Constant.LIMIT).toString());
        Page<GoodsListDTO> goodsListPageDTO = new Page<>(pageNum, limit);
        List<GoodsListDTO> page = baseDao.labelUnassociated(goodsListPageDTO, params);

        return new PageData(page, goodsListPageDTO.getTotal());
    }


    @Override

    @Transactional(rollbackFor = Exception.class)
    public void updateGoodsShow(@RequestBody GoodsShowDTO dto) {
        //商品ID集合
        List<Long> ids = dto.getIds();
        //商品上下架状态
        Integer goodsShow = dto.getGoodsShow();
        if (CollectionUtils.isNotEmpty(ids) && goodsShow != null) {
            //查询是否存在待审核的商品 并抛出异常
            this.checkGoodsStatus(ids);
            Date date = new Date();
            //修改商品上下架
            baseDao.updateGoodsShowBatch(dto, date);
            //规格上下架
            goodsSpecService.updateShowByGoodsIds(ids, goodsShow);
            //删除定时上下架表记录
            goodsTimeService.deleteByGoodsId(dto.getIds());
            //保存商品上下架记录
            this.sendMqSaveGoodsRecord(dto.getIds(), dto.getGoodsShow());
        }
    }


    @Override

    public void timedUpdateGoodsShow(@RequestBody GoodsShowDTO dto) {
        //商品ID集合
        List<Long> ids = dto.getIds();
        //商品上下架状态
        Integer goodsShow = dto.getGoodsShow();
        if (CollectionUtils.isNotEmpty(ids) && goodsShow != null && null != dto.getShelfTime()) {
            for (Long id : ids) {
                GoodsTimeDTO timeDTO = goodsTimeService.selectByGoodsId(id);
                if (null == timeDTO) {
                    // 商品定时上下架状态与时间
                    GoodsTimeDTO goodsTimeDTO = new GoodsTimeDTO();
                    goodsTimeDTO.setShelfTime(dto.getShelfTime());
                    goodsTimeDTO.setOperationalStatus(GoodsTimeStatusEnum.GOODS_TIME_STATUS_SHOW.value());
                    goodsTimeDTO.setGoodsId(id);
                    goodsTimeDTO.setShowStatus(goodsShow);
                    goodsTimeService.save(goodsTimeDTO);
                } else {
                    // 修改商品定时上下架状态与时间
                    timeDTO.setShelfTime(dto.getShelfTime());
                    timeDTO.setOperationalStatus(GoodsTimeStatusEnum.GOODS_TIME_STATUS_SHOW.value());
                    timeDTO.setGoodsId(id);
                    timeDTO.setShowStatus(goodsShow);
                    goodsTimeService.updateStatusAndTime(timeDTO);
                }
            }
            mlogger.info(GoodsStatusCode.TIMED_GOODS_SHOW_UPDATE_CODE, GoodsStatusCode.TIMED_GOODS_SHOW_UPDATE_SUCCESS_MSG);
        }
    }

    @Override
    public PageData<EasyGoodsExcel> goodsExport(@RequestParam Map<String, Object> params) {
        //分页参数
        Long curPage = null;
        Long limit = null;
        params.put("goodsStatus", 30);
        if (params.get(Constant.PAGE) != null && !"".equals(params.get(Constant.PAGE))) {
            curPage = Long.parseLong((String) params.get(Constant.PAGE));
        }
        if (params.get(Constant.LIMIT) != null && !"".equals(params.get(Constant.LIMIT))) {
            limit = Long.parseLong((String) params.get(Constant.LIMIT));
        }
        Page<EasyGoodsExcel> page = new Page<>(curPage, limit);
        List<EasyGoodsExcel> list = baseDao.goodsExport(page, params);

        return new PageData(list, page.getTotal());
    }


    /**
     * 查询是否存在待审核的商品
     *
     * @param ids 商品ID集合
     * @return
     * @date 2020/3/4/004 17:18
     * @author xuzhch
     **/
    private void checkGoodsStatus(List<Long> ids) {
        //查询是否存在待审核的商品
        int num = baseDao.selectCount(Wrappers.<GoodsEntity>lambdaQuery()
                .in(GoodsEntity::getId, ids)
                .ne(GoodsEntity::getGoodsStatus, GoodsStatusEnum.GOODS_AUDIT_PASS.value())
        );
        if (num > 0) {
            throw new ServiceException(GoodsStatusCode.GOODS_STATUS_ERROR);
        }
    }

    /**
     * 店铺商品实况
     *
     * @param storeId
     */

    @Override
    public GoodsLiveDTO goodsLive(@RequestParam("storeId") Long storeId, @RequestParam(value = "type", required = false) Integer type) {

        GoodsLiveDTO goodsLiveDTO = null;

        String goodsLiveKey = RedisKeys.getGoodsLiveKey(storeId);
        Long expire = 60L * 30;

        Object object = redisUtils.get(goodsLiveKey);

        if (object == null || type == 1) {
            goodsLiveDTO = baseDao.goodsLive(storeId);
            goodsLiveDTO.setCreateDate(new Date());
            redisUtils.set(goodsLiveKey, goodsLiveDTO, expire);
        } else {
            goodsLiveDTO = (GoodsLiveDTO) object;
        }

        return goodsLiveDTO;
    }

    /**
     * 首页>基础概况>商品数据查询
     *
     * @param startDateStr 开始时间字符串
     * @param endDateStr   结束时间字符串
     * @return IndexGoodsDataDTO
     * @date 2020/4/7/007 12:00
     * @author xuzhch
     */

    @Override
    public IndexGoodsDataDTO indexGoodsData(@RequestParam("startDateStr") String startDateStr, @RequestParam("endDateStr") String endDateStr) {
        Date startDate = DateUtils.parse(startDateStr, DateUtils.DATE_TIME_PATTERN);
        Date endDate = DateUtils.parse(endDateStr, DateUtils.DATE_TIME_PATTERN);
        return baseDao.selectIndexGoodsData(startDate, endDate);
    }


    /**
     * 指定商品分页搜索
     *
     * @param params
     * @param goodIds
     * @return
     * @Author: yuhaifeng
     */
    @Override

    public PageData<CircleGoodsSearchDTO> circleGoodsSearch(@RequestParam Map<String, Object> params, @RequestParam Set<Long> goodIds) {
        int pageNum = Integer.valueOf(params.get(Constant.PAGE).toString());
        int limit = Integer.valueOf(params.get(Constant.LIMIT).toString());
        Page<CircleGoodsSearchDTO> page = new Page<>(pageNum, limit);
        List<CircleGoodsSearchDTO> goodsList = baseDao.circleGoodsSearch(page, goodIds);
        return new PageData(goodsList, page.getTotal());
    }


    @Override
    public PageData<EasyGoodsCheckExcel> goodsCheckExport(@RequestParam Map<String, Object> params) {
        //分页参数
        Long curPage = null;
        Long limit = null;
        if (params.get(Constant.PAGE) != null && !"".equals(params.get(Constant.PAGE))) {
            curPage = Long.parseLong(params.get(Constant.PAGE).toString());
        }
        if (params.get(Constant.LIMIT) != null && !"".equals(params.get(Constant.LIMIT))) {
            limit = Long.parseLong(params.get(Constant.LIMIT).toString());
        }
        Page<EasyGoodsCheckExcel> page = new Page<>(curPage, limit);
        List<EasyGoodsCheckExcel> list = baseDao.goodsCheckExport(page, params);

        return new PageData(list, page.getTotal());
    }

    /**
     * 功能描述：
     * <查询运费模板关联商品数量>
     *
     * @param freightTemplateId 运费模板id
     * @return
     * @date 2020/4/24 14:43
     * @author 刘远杰
     **/
    @Override

    public Integer countGoodsByFreightTemplateId(@RequestParam("freightTemplateId") Long freightTemplateId) {
        QueryWrapper<GoodsEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("freight_template_id", freightTemplateId);
        return baseDao.selectCount(queryWrapper);
    }

    /**
     * 功能描述：
     * <更新商品运费模板>
     *
     * @param oldFreightTemplateId
     * @param newFreightTemplateId
     * @param storeId
     * @return
     * @date 2020/4/24 17:31
     * @author 刘远杰
     **/
    @Override

    public void updateGoodsFreightTemplate(@RequestParam("oldFreightTemplateId") Long oldFreightTemplateId,
                                           @RequestParam("newFreightTemplateId") Long newFreightTemplateId,
                                           @RequestParam("storeId") Long storeId) {

        // 校验新运费模板id
        FreightTemplateDTO freightTemplateDTO = freightTemplateService.getStoreFreightTemplateById(newFreightTemplateId, storeId);
        if (freightTemplateDTO == null) {
            return;
        }

        QueryWrapper<GoodsEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("store_id", storeId).eq("freight_template_id", oldFreightTemplateId);
        GoodsEntity goodsEntity = new GoodsEntity();
        goodsEntity.setFreightTemplateId(newFreightTemplateId);

        baseDao.update(goodsEntity, queryWrapper);

    }

    /**
     * 查询店铺商品分类下面是否关联商品
     *
     * @param ids
     * @return
     */

    @Override
    public List<String> findClassName(@RequestBody Long[] ids) {

        return baseDao.findClassName(ids);
    }

    /**
     * 查询店铺分类下的商品数量
     *
     * @param classLists
     * @return
     */

    @Override
    public List<Map<String, Object>> findClassGoodsCount(@RequestBody List<Long> classLists) {
        return baseDao.findClassGoodsCount(classLists);
    }

    /**
     * 查询楼层关联的商品
     *
     * @param collect
     * @return
     */

    @Override
    public List<GoodsDTO> findNamePricePic(@RequestBody List<String> collect) {
        return baseDao.findNamePricePic(collect);
    }

    /**
     * 店铺商品推荐
     *
     * @param storeId             店铺ID
     * @param storeGoodsFirstGcId 店铺商品分类Id
     * @param limit               显示条数
     * @return
     */

    @Override
    public List<GoodsDTO> storeGoodsRecommend(@RequestParam("storeId") Long storeId,
                                              @RequestParam(value = "storeGoodsFirstGcId", required = false) Long storeGoodsFirstGcId,
                                              @RequestParam("limit") Integer limit) {
        return baseDao.storeGoodsRecommend(storeId, limit, storeGoodsFirstGcId);
    }


    @Override
    public void updateGoods(@RequestBody GoodsDTO goodsDTO) {
        GoodsEntity goodsEntity = ConvertUtils.sourceToTarget(goodsDTO, GoodsEntity.class);
        baseDao.updateById(goodsEntity);
    }


    /**
     * 功能描述 导出商品的数据到模板中
     *
     * @param params 导出参数
     * @return java.util.List<com.leimingtech.modules.excel.goods.GoodsTemplateExcel>
     * @author lishuo
     * @date 24/6/2020
     */
    @Override

    public List<GoodsTemplateExcel> exportGoodsToExcel(@RequestParam Map<String, Object> params) {
        List<GoodsTemplateExcel> list = new ArrayList<>();
        // 商品信息的查询
        int pageNum = Integer.valueOf(params.get(Constant.PAGE).toString());
        int limit = Integer.valueOf(params.get(Constant.LIMIT).toString());
        Page<GoodsExportDTO> page = new Page<>(pageNum, limit);
        params.put("start", pageNum);
        params.put("limit", limit);
        List<GoodsExportDTO> goodsExportDTOList = baseDao.selectGoodsToExcelTest(params);
        PageData<GoodsExportDTO> goodsExportPageDataDTO = new PageData<>(goodsExportDTOList, page.getTotal());
        int total = goodsExportPageDataDTO.getTotal();
        // 数据处理
        for (GoodsExportDTO goodsExportDTO : goodsExportDTOList) {
            GoodsTemplateExcel goodsTemplateExcel = new GoodsTemplateExcel();
            // 商品分类
            goodsTemplateExcel.setGoodsGcName(goodsExportDTO.getFirstGcName() + ";" + goodsExportDTO.getSecondGcName() + ";" + goodsExportDTO.getThirdGcName());
            // 店铺分类
            if (goodsExportDTO.getFirstStoreGoodsGcName() != null) {
                goodsTemplateExcel.setStoreGcName(goodsExportDTO.getFirstStoreGoodsGcName() + ";" + goodsExportDTO.getSecondStoreGoodsGcName());
            }
            goodsTemplateExcel.setGoodsName(goodsExportDTO.getGoodsName());
            goodsTemplateExcel.setGoodsSubName(goodsExportDTO.getGoodsSubTitle());
            goodsTemplateExcel.setGoodsBrand(goodsExportDTO.getBrandName());
            goodsTemplateExcel.setGoodsLabel(goodsExportDTO.getGoodsLabelName());
            goodsTemplateExcel.setGoodsCostPrice(goodsExportDTO.getSpecCostPrice());
            goodsTemplateExcel.setGoodsSellPrice(goodsExportDTO.getSpecSellPrice());
            goodsTemplateExcel.setDeliveryType(goodsExportDTO.getDevlierType());
            goodsTemplateExcel.setExpressFlag(goodsExportDTO.getExpressFlag());
            goodsTemplateExcel.setFreightTemplate(goodsExportDTO.getFreightTemplateName());
            goodsTemplateExcel.setInvoice(goodsExportDTO.getInvoiceFlag());
            goodsTemplateExcel.setInvoiceType(goodsExportDTO.getInvoiceType());
            goodsTemplateExcel.setInvoiceContent(goodsExportDTO.getInvoiceContent());
            List<GoodsAttributeDTO> goodsAttributeDTO = goodsExportDTO.getGoodsAttributeDTOList();
            StringBuilder goodsAttrAndValue = new StringBuilder();
            if (CollectionUtils.isNotEmpty(goodsAttributeDTO)) {
                for (GoodsAttributeDTO attributeDTO : goodsAttributeDTO) {
                    if (attributeDTO != null) {
                        Map<String, String> goodsAttributeMap = new HashMap<>(10);
                        goodsAttributeMap.put(attributeDTO.getAttrName(), attributeDTO.getAttrValue());
                        String s = JSON.toJSONString(goodsAttributeMap);
                        goodsAttrAndValue.append(s).append(";");
                    }
                }
            }
            StringBuilder picUrl = new StringBuilder();
            if (goodsAttrAndValue.length() > 0) {
                goodsTemplateExcel.setGoodsAttrAndValue(goodsAttrAndValue.toString().substring(0, goodsAttrAndValue.toString().length() - 1));
            }
            List<GoodsSpecDetailDTO> goodsSpecDetailDTOList = goodsExportDTO.getGoodsSpecDetailDTOList();
            StringBuilder specDetil = new StringBuilder();
            StringBuilder specAttrValueNameString = new StringBuilder();
            // 遍历改商品下的规格商品
            for (GoodsSpecDetailDTO goodsSpecDetailDTO : goodsSpecDetailDTOList) {
                // 获得规格商品的值和属性名称
                Map<String, String> specNameAndValueMap = new HashMap<>(10);
                String specAttrValueName = goodsSpecDetailDTO.getSpecAttrValueName();
                if (specAttrValueName != null) {
                    String[] attrValueNameArray = specAttrValueName.split(" ");
                    for (String attrValueName : attrValueNameArray) {
                        String[] attrValueAndName = attrValueName.split(":");
                        if (attrValueAndName.length == 2) {
                            specNameAndValueMap.put(attrValueAndName[0], attrValueAndName[1]);
                        }
                    }
                    specAttrValueNameString.append(JSON.toJSONString(specNameAndValueMap)).append(";");
                }
                specDetil.append(goodsSpecDetailDTO.getSpecSellPrice()).append(",").append(goodsSpecDetailDTO.getSpecCostPrice()).append(",").append(goodsSpecDetailDTO.getSpecStorage()).append(";");
            }
            // 图片的查询 查询主图信息
            List<DefaultSpecPictureDTO> defaultPic = goodsExportDTO.getDefaultSpecPictureDTOList();
            defaultPic.forEach(defaultSpecPictureDTO -> {
                if (StringUtils.isNotEmpty(defaultSpecPictureDTO.getPictureUrl())) {
                    picUrl.append(defaultSpecPictureDTO.getPictureUrl()).append(";");
                }
            });
            if (picUrl.length() > 0) {
                goodsTemplateExcel.setPictureUrl(picUrl.substring(0, picUrl.length() - 1).toString());
            }
            //图片查询  查询规格的信息
            List<SpecAttributePictureDetailDTO> picListByGoodsId = specAttributeValueService.getPicListByGoodsId(goodsExportDTO.getGoodsId());
            StringBuilder skuPicUrl = new StringBuilder();
            for (SpecAttributePictureDetailDTO specAttributePictureDetailDTO : picListByGoodsId) {
                List<SpecAttributePictureUrlDTO> specAttributePictureListDTO = specAttributePictureDetailDTO.getSpecAttributePictureDTOList();
                for (SpecAttributePictureUrlDTO specAttributePictureDTOList : specAttributePictureListDTO) {
                    if (StringUtils.isNotEmpty(specAttributePictureDTOList.getPictureUrl())) {
                        skuPicUrl.append(specAttributePictureDTOList.getPictureUrl()).append(",");
                    }
                }
                if (skuPicUrl.length() > 0) {
                    skuPicUrl.deleteCharAt(skuPicUrl.lastIndexOf(",")).append(";");
                }
            }
            if (skuPicUrl.length() > 0) {
                goodsTemplateExcel.setSkuPictureUrl(skuPicUrl.toString().substring(0, skuPicUrl.toString().length() - 1));
            }
            if (specAttrValueNameString.length() > 0) {
                goodsTemplateExcel.setGoodsSepcNameValue(specAttrValueNameString.toString().substring(0, specAttrValueNameString.toString().length() - 1));
            }
            if (specDetil.length() > 0) {
                goodsTemplateExcel.setGoodsSetting(specDetil.toString().substring(0, specDetil.toString().length() - 1));
            }
            GoodsInfoDTO goodsInfoDTO = goodsExportDTO.getGoodsInfoDTO();
            if (goodsInfoDTO != null) {
                goodsTemplateExcel.setGoodsInfo(goodsInfoDTO.getMobileBody());
                goodsTemplateExcel.setAfterSaleTemplate(goodsInfoDTO.getAfterSale());
            }
            goodsTemplateExcel.setTotalPage(total);
            list.add(goodsTemplateExcel);

        }
        return list;
    }


    /**
     * 功能描述 商品保存逻辑
     *
     * @param list                   需要保存的数据
     * @param goodsClassListDTOList  商品的分类信息
     * @param brandDTOList           品牌的分类信息
     * @param storeGoodsClassDTOList 店铺的分类信息
     * @param goodsLabelDTOList      商品标签的集合
     * @param freightTemplateDTOList 运费模板的集合
     * @param storeDTO               店铺的信息
     * @author lishuo
     * @date 6/7/2020
     */
    private void saveGoodsFromExcel(List<GoodsTemplateExcel> list, List<GoodsClassListDTO> goodsClassListDTOList, List<BrandDTO> brandDTOList, List<StoreGoodsClassDTO> storeGoodsClassDTOList, List<GoodsLabelDTO> goodsLabelDTOList, List<FreightTemplateDTO> freightTemplateDTOList, StoreDTO storeDTO) {
        // 保存goodsentity的集合
        List<GoodsEntity> goodsEntityList = new ArrayList<>();
        // goods check 的集合
//        List<GoodsCheckDTO> goodsCheckDTOList = new ArrayList<>();
        //商品附件信息的集合
        List<GoodsLabelRelSaveDTO> goodsLabelRelSaveDTOList = new ArrayList<>();
        List<GoodsInfoDTO> goodsInfoDTOList = new ArrayList<>();
        // spec的保存的集合
        List<GoodsSpecSaveDTO> saveGoodsSpecListTotalDTO = new ArrayList<>();
        //属性保存的集合
        List<GoodsAttributeSaveDTO> attributeSaveDTOList = new ArrayList<>();
        //spec name 的保存的集合
        List<SpecAttributeSaveDTO> specAttributeSaveTotalDTOList = new ArrayList<>();
        //spec value 的保存的集合
        List<SpecAttributeValueSaveDTO> specAttributeValueSaveTotalListDTO = new ArrayList<>();
        // 关联表的保存
        List<SpecAttributeRelationDTO> specAttributeRelationDTOList = new ArrayList<>();
        // 图片表的保存
        List<SpecAttributePictureSaveDTO> specAttributePictureSaveDTOList = new ArrayList<>();
        //处理数据 的集合
        List<GoodsClassListDTO> firstGoodsClassListDTOList = new ArrayList<>();
        List<GoodsClassListDTO> secondGoodsClassListDTOList = new ArrayList<>();
        List<GoodsClassListDTO> thirdGoodsClassListDTOList = new ArrayList<>();
        List<StoreGoodsClassDTO> firstStoreGoodsClassDTOList = new ArrayList<>();
        List<StoreGoodsClassDTO> secondStoreGoodsClassDTOList = new ArrayList<>();
        Map<String, List<GoodsClassListDTO>> firstGoodsClassMap = new HashMap<>(10);
        Map<Long, List<GoodsClassListDTO>> secondGoodsClassMap = new HashMap<>(10);
        Map<Long, List<GoodsClassListDTO>> thirdGoodsClassMap = new HashMap<>(10);
        Map<String, List<StoreGoodsClassDTO>> firstStoreGoodsClassMap = new HashMap<>(10);
        Map<Long, List<StoreGoodsClassDTO>> secondStoreGoodsClassMap = new HashMap<>(10);
        for (GoodsClassListDTO goodsClassListDTO : goodsClassListDTOList) {
            String gcIdpath = goodsClassListDTO.getGcIdpath();
            String[] split = StringUtils.split(gcIdpath, ",");
            if (split.length == 1) {
                firstGoodsClassListDTOList.add(goodsClassListDTO);
            } else if (split.length == 2) {
                secondGoodsClassListDTOList.add(goodsClassListDTO);
            } else {
                thirdGoodsClassListDTOList.add(goodsClassListDTO);
            }
        }
        if (CollectionUtil.isNotEmpty(storeGoodsClassDTOList)) {
            for (StoreGoodsClassDTO storeGoodsClassDTO : storeGoodsClassDTOList) {
                if (storeGoodsClassDTO.getGcParentId().equals(0L)) {
                    firstStoreGoodsClassDTOList.add(storeGoodsClassDTO);
                } else {
                    secondStoreGoodsClassDTOList.add(storeGoodsClassDTO);
                }
            }
            firstStoreGoodsClassMap = firstStoreGoodsClassDTOList.stream().collect(Collectors.groupingBy(StoreGoodsClassDTO::getGcName));
            secondStoreGoodsClassMap = secondStoreGoodsClassDTOList.stream().collect(Collectors.groupingBy(StoreGoodsClassDTO::getGcParentId));
        }
        firstGoodsClassMap = firstGoodsClassListDTOList.stream().collect(Collectors.groupingBy(GoodsClassListDTO::getGcName));
        secondGoodsClassMap = secondGoodsClassListDTOList.stream().collect(Collectors.groupingBy(GoodsClassListDTO::getGcParentId));
        thirdGoodsClassMap = thirdGoodsClassListDTOList.stream().collect(Collectors.groupingBy(GoodsClassListDTO::getGcParentId));
        String defaultPicUrl = settingService.getDefaultAvatarsSet().getAvatar();
        for (GoodsTemplateExcel goodsTemplateExcel : list) {
            long specId = IdWorker.getId();
            // 外链图片替换
            replacePicUrl(goodsTemplateExcel);
            // 商品的处理
            GoodsEntity goodsEntity = savegoods(goodsTemplateExcel, specId, firstGoodsClassMap, secondGoodsClassMap, thirdGoodsClassMap, firstStoreGoodsClassMap, secondStoreGoodsClassMap, brandDTOList, freightTemplateDTOList, defaultPicUrl, storeDTO);
            if (goodsEntity == null) {
                continue;
            }
            goodsEntity.setCreateDate(new Date());
            goodsEntity.setUpdateDate(new Date());
            goodsEntity.setCreator(storeDTO.getStoreLinkman());
            goodsEntity.setUpdater(storeDTO.getStoreLinkman());
            // 设置了不是虚拟物品
            goodsEntity.setVirtualFlag(0);
            // 导入商品设置为草稿
            goodsEntity.setGoodsStatus(GoodsStatusEnum.GOODS_DRAFT.value());
//            importGoodsCheck(goodsEntity, goodsCheckDTOList, settingGoodsAuditDTO, storeDTO);
            goodsEntityList.add(goodsEntity);
            //商品附加信息的保存
            saveGoodsInfoTemplate(goodsEntity, goodsTemplateExcel, goodsInfoDTOList, goodsLabelRelSaveDTOList);
            // 商品属性的保存
            saveGoodsAttributeTemplate(goodsTemplateExcel.getGoodsAttrAndValue(), goodsEntity.getId(), attributeSaveDTOList);
            // spec 属性值和属性名称的处理
            Map<String, Long> specNameIdMap = new HashMap<>(10);
            Map<String, Long> specValueIdMap = new HashMap<>(10);
            List<String> specValue1 = new ArrayList<>();
            List<String> specValue2 = new ArrayList<>();
            List<String> specValue3 = new ArrayList<>();
//            Map<Long, Long> specNameAndValueId = new HashMap<>(10);
            List<String> specName = new ArrayList<>();
            // 处理sku的属性值和名称
            getattrNameAndValue(goodsTemplateExcel, specName, specValue1, specValue2, specValue3, defaultPicUrl);
            // sku 的判断保存
            // sku的保存
            List<GoodsSpecSaveDTO> goodsSpecSaveDTOList = saveSku(goodsTemplateExcel, specId, goodsEntity, specValue1);
            saveGoodsSpecListTotalDTO.addAll(goodsSpecSaveDTOList);
            // sku attr name 的保存
            List<SpecAttributeSaveDTO> specAttributeSaveDTOList = saveSkuAttrName(specName, goodsEntity, specNameIdMap);
            specAttributeSaveTotalDTOList.addAll(specAttributeSaveDTOList);
            // sku value的保存
            List<SpecAttributeValueSaveDTO> specAttributeValueSaveDTOList = saveSkuAttrValue(specValue1, specValue2, specValue3, specValueIdMap, goodsEntity, specAttributeSaveDTOList);
            specAttributeValueSaveTotalListDTO.addAll(specAttributeValueSaveDTOList);
            // 关联表保存
            saveSepcRel(goodsEntity, goodsSpecSaveDTOList, goodsTemplateExcel, specNameIdMap, specValueIdMap, specAttributeRelationDTOList);
            // 图片的保存
            savePicRel(goodsEntity, specValue1, goodsTemplateExcel, specNameIdMap, specValueIdMap, specName, specAttributePictureSaveDTOList);
        }
        // 保存数据
        baseDao.insertBatch(goodsEntityList);
        if (CollectionUtils.isNotEmpty(goodsLabelRelSaveDTOList)) {
            goodsLabelRelService.saveBatch(goodsLabelRelSaveDTOList);
        }
        if (CollectionUtils.isNotEmpty(goodsInfoDTOList)) {
            goodsInfoService.saveBatch(goodsInfoDTOList);
        }
        goodsSpecService.insertBatch(saveGoodsSpecListTotalDTO);
        if (CollectionUtils.isNotEmpty(attributeSaveDTOList)) {
            goodsAttributeService.saveBatch(attributeSaveDTOList);
        }
        specAttributeService.insertBatch(specAttributeSaveTotalDTOList);
        specAttributeValueService.saveByList(specAttributeValueSaveTotalListDTO);
        specAttributeRelationService.saveBatch(specAttributeRelationDTOList);
        specAttributePictureService.saveBatch(specAttributePictureSaveDTOList);

    }

    /**
     * 功能描述 图片的保存
     *
     * @param goodsEntity
     * @param specValue1
     * @param goodsTemplateExcel
     * @param specNameIdMap
     * @param specValueIdMap
     * @param specName
     * @param specAttributePictureSaveDTOList
     * @return void
     * @author lishuo
     * @date 7/7/2020
     */
    private void savePicRel(GoodsEntity goodsEntity, List<String> specValue1, GoodsTemplateExcel goodsTemplateExcel, Map<String, Long> specNameIdMap, Map<String, Long> specValueIdMap, List<String> specName, List<SpecAttributePictureSaveDTO> specAttributePictureSaveDTOList) {
        Map<String, String> attrPicMap = new HashMap<>(10);
        String goodsSpec = goodsTemplateExcel.getGoodsSepcNameValue();
        String goodsSpecRel = goodsSpec.replace("{", "").replace("}", "").replace("\"", "");
        String[] specNameAndValues = goodsSpecRel.split(";");
        for (int i = 0; i < specValue1.size(); i++) {
            attrPicMap.put(specValue1.get(i), goodsTemplateExcel.getSkuPictureUrl().split(";")[i]);
        }
        for (int i = 0; i < specValue1.size(); i++) {
            String s = attrPicMap.get(specValue1.get(i));
            String[] split = s.split(",");
            for (int k = 0; k < split.length; k++) {

                SpecAttributePictureSaveDTO specAttributePictureSaveDTO = new SpecAttributePictureSaveDTO();
                specAttributePictureSaveDTO.setSpecAttrId(specNameIdMap.get(specName.get(0)));
                specAttributePictureSaveDTO.setSpecAttrValueId(specValueIdMap.get(specValue1.get(i)));
                specAttributePictureSaveDTO.setGoodsId(goodsEntity.getId());
                if (k == 0) {
                    specAttributePictureSaveDTO.setIsMainPicture(1);
                } else {
                    specAttributePictureSaveDTO.setIsMainPicture(0);
                }
                specAttributePictureSaveDTO.setSort(k);
                specAttributePictureSaveDTO.setPictureUrl(split[k]);
                specAttributePictureSaveDTO.setId(IdWorker.getId());
                specAttributePictureSaveDTOList.add(specAttributePictureSaveDTO);

            }
        }
        String[] skuMainPicArray = goodsTemplateExcel.getPictureUrl().split(";");
        for (int i = 0; i < skuMainPicArray.length; i++) {
            SpecAttributePictureSaveDTO specAttributePictureSaveDTO = new SpecAttributePictureSaveDTO();
            specAttributePictureSaveDTO.setSpecAttrId(0L);
            specAttributePictureSaveDTO.setSpecAttrValueId(0L);
            specAttributePictureSaveDTO.setGoodsId(goodsEntity.getId());
            specAttributePictureSaveDTO.setIsMainPicture(1);
            specAttributePictureSaveDTO.setId(IdWorker.getId());
            specAttributePictureSaveDTO.setPictureUrl(skuMainPicArray[i]);
            specAttributePictureSaveDTOList.add(specAttributePictureSaveDTO);
        }
        log.info("图片保存成功");
    }

    /**
     * 功能描述 sku attr name 与value 关联表的保存
     *
     * @param goodsEntity
     * @param saveGoodsSpecDTOList
     * @param goodsTemplateExcel
     * @param specNameIdMap
     * @param specValueIdMap
     * @param specAttributeRelationDTOList
     * @return void
     * @author lishuo
     * @date 7/7/2020
     */
    private void saveSepcRel(GoodsEntity goodsEntity, List<GoodsSpecSaveDTO> saveGoodsSpecDTOList, GoodsTemplateExcel goodsTemplateExcel, Map<String, Long> specNameIdMap, Map<String, Long> specValueIdMap, List<SpecAttributeRelationDTO> specAttributeRelationDTOList) {
        String goodsSpec = goodsTemplateExcel.getGoodsSepcNameValue();
        String goodsSpecRel = goodsSpec.replace("{", "").replace("}", "").replace("\"", "");
        String[] specNameAndValues = goodsSpecRel.split(";");
        for (int i = 0; i < saveGoodsSpecDTOList.size(); i++) {
            String[] nameAndValue = specNameAndValues[i].split(",");
            for (int k = 0; k < nameAndValue.length; k++) {
                String[] split = nameAndValue[k].split(":");
                SpecAttributeRelationDTO specAttributeRelationDTO = new SpecAttributeRelationDTO();
                specAttributeRelationDTO.setGoodsId(goodsEntity.getId());
                specAttributeRelationDTO.setSpecAttrValueId(specValueIdMap.get(split[1]));
                specAttributeRelationDTO.setSpecAttrId(specNameIdMap.get(split[0]));
                specAttributeRelationDTO.setSpecId(saveGoodsSpecDTOList.get(i).getId());
                specAttributeRelationDTO.setId(IdWorker.getId());
                if (i == 0) {
                    specAttributeRelationDTO.setIsMain(1);
                } else {
                    specAttributeRelationDTO.setIsMain(0);
                }

                specAttributeRelationDTOList.add(specAttributeRelationDTO);
            }
        }
        log.info("sku关联表保存成功");
    }

    /**
     * 功能描述 sku attr value 保存
     *
     * @param specValue1               sku 属性名称第一个对应的值的集合
     * @param specValue2
     * @param specValue3
     * @param specValueIdMap           attrvalue：attrvalueId
     * @param goodsEntity
     * @param specAttributeSaveDTOList
     * @return java.util.List<com.leimingtech.modules.dto.goods.spec.SpecAttributeValueSaveDTO>
     * @author lishuo
     * @date 8/7/2020
     */
    private List<SpecAttributeValueSaveDTO> saveSkuAttrValue(List<String> specValue1, List<String> specValue2, List<String> specValue3, Map<String, Long> specValueIdMap, GoodsEntity goodsEntity, List<SpecAttributeSaveDTO> specAttributeSaveDTOList) {
        List<SpecAttributeValueSaveDTO> specAttributeValueSaveDTOList = new ArrayList<>();
        for (int i = 0; i < specValue1.size(); i++) {
            SpecAttributeValueSaveDTO specAttributeValueSaveDTO = new SpecAttributeValueSaveDTO();
            specAttributeValueSaveDTO.setId(IdWorker.getId());
            specAttributeValueSaveDTO.setGoodsId(goodsEntity.getId());
            specAttributeValueSaveDTO.setIsSelect(1);
            specAttributeValueSaveDTO.setSpecAttrValue(specValue1.get(i));
            specAttributeValueSaveDTO.setIsMain(1);
            specAttributeValueSaveDTO.setSpecAttrId(specAttributeSaveDTOList.get(0).getId());
            specValueIdMap.put(specValue1.get(i), specAttributeValueSaveDTO.getId());
            specAttributeValueSaveDTOList.add(specAttributeValueSaveDTO);
        }
        if (specValue2.size() > 0) {
            for (String specValue : specValue2) {
                SpecAttributeValueSaveDTO specAttributeValueSaveDTO = new SpecAttributeValueSaveDTO();
                specAttributeValueSaveDTO.setId(IdWorker.getId());
                specAttributeValueSaveDTO.setGoodsId(goodsEntity.getId());
                specAttributeValueSaveDTO.setIsSelect(1);
                specAttributeValueSaveDTO.setSpecAttrValue(specValue);
                specAttributeValueSaveDTO.setSpecAttrId(specAttributeSaveDTOList.get(1).getId());
                specValueIdMap.put(specValue, specAttributeValueSaveDTO.getId());
                specAttributeValueSaveDTOList.add(specAttributeValueSaveDTO);
            }
        }
        if (specValue3.size() > 0) {
            for (String specValue : specValue3) {
                SpecAttributeValueSaveDTO specAttributeValueSaveDTO = new SpecAttributeValueSaveDTO();
                specAttributeValueSaveDTO.setId(IdWorker.getId());
                specAttributeValueSaveDTO.setGoodsId(goodsEntity.getId());
                specAttributeValueSaveDTO.setIsSelect(1);
                specAttributeValueSaveDTO.setSpecAttrValue(specValue);
                specAttributeValueSaveDTO.setSpecAttrId(specAttributeSaveDTOList.get(2).getId());
                specValueIdMap.put(specValue, specAttributeValueSaveDTO.getId());
                specAttributeValueSaveDTOList.add(specAttributeValueSaveDTO);
            }
        }
        log.info("sku attr value 保存成功");
        return specAttributeValueSaveDTOList;
    }

    /**
     * 功能描述 sku attr name 保存
     *
     * @param specName      sku attr name 的集合
     * @param goodsEntity
     * @param specNameIdMap sku attr name: id
     * @return java.util.List<com.leimingtech.modules.dto.goods.spec.SpecAttributeSaveDTO>
     * @author lishuo
     * @date 8/7/2020
     */
    private List<SpecAttributeSaveDTO> saveSkuAttrName(List<String> specName, GoodsEntity goodsEntity, Map<String, Long> specNameIdMap) {
        List<SpecAttributeSaveDTO> specAttributeSaveDTOList = new ArrayList<>(specName.size());
        for (int i = 0; i < specName.size(); i++) {
            SpecAttributeSaveDTO specAttributeSaveDTO = new SpecAttributeSaveDTO();
            specAttributeSaveDTO.setId(IdWorker.getId());
            specAttributeSaveDTO.setGoodsId(goodsEntity.getId());
            specAttributeSaveDTO.setIsSelect(1);
            specAttributeSaveDTO.setSpecAttrName(specName.get(i));
            if (i == 0) {
                specAttributeSaveDTO.setIsMain(1);
            }
            specAttributeSaveDTO.setId(IdWorker.getId());
            specAttributeSaveDTOList.add(specAttributeSaveDTO);

            specNameIdMap.put(specName.get(i), specAttributeSaveDTO.getId());
        }
        log.info("sku attr value 保存成功");
        return specAttributeSaveDTOList;
    }

    /**
     * 功能描述 保存sku
     *
     * @param goodsTemplateExcel
     * @param specId             specId
     * @param goodsEntity
     * @param specValue1
     * @return java.util.List<com.leimingtech.modules.dto.goods.spec.GoodsSpecSaveDTO>
     * @author lishuo
     * @date 6/7/2020
     */
    private List<GoodsSpecSaveDTO> saveSku(GoodsTemplateExcel goodsTemplateExcel, Long specId, GoodsEntity goodsEntity, List<String> specValue1) {
        String goodsSpec = goodsTemplateExcel.getGoodsSepcNameValue();
        String goodsSpecRel = goodsSpec.replace("{", "").replace("}", "").replace("\"", "");
        String[] specNameAndValues = goodsSpecRel.split(";");
        List<SpecAttributeAndValueSaveDTO> specAttributeAndValueSaveDTOList = new ArrayList<>();

        List<GoodsSpecDTO> specNameAndValueList = new ArrayList<>();
        String[] skuPrarm = goodsTemplateExcel.getGoodsSetting().split(";");
        String[] picUrlArray = StringUtils.split(goodsTemplateExcel.getSkuPictureUrl(), ";");
        List<GoodsSpecSaveDTO> saveGoodsSpecDTOList = new ArrayList<>();
        for (int i = 0; i < specNameAndValues.length; i++) {
            GoodsSpecSaveDTO goodsSpecDTO = new GoodsSpecSaveDTO();
            if (i == 0) {
                goodsSpecDTO.setId(specId);
            } else {
                long goodsSpecId = IdWorker.getId();
                goodsSpecDTO.setId(goodsSpecId);
            }
            goodsSpecDTO.setGoodsId(goodsEntity.getId());
            long specSerialId = IdWorker.getId();
            double substring = Math.ceil(Math.random() * 10000000000f);
            goodsSpecDTO.setSpecSerial(Double.toString(substring));
            StringBuilder specValue = new StringBuilder();
            //多规格
            String[] split = specNameAndValues[i].split(",");
            for (String s1 : split) {
                SpecAttributeDetailDTO specAttributeDetailDTO = new SpecAttributeDetailDTO();
                String[] split1 = s1.split(":");
                specValue.append(split1[1]).append(" ");
            }
            goodsSpecDTO.setSpecName(goodsEntity.getGoodsName() + specValue.toString());
            String[] sku = skuPrarm[i].split(",");
            goodsSpecDTO.setSpecSellPrice(BigDecimal.valueOf(Double.valueOf(sku[0])));
            goodsSpecDTO.setSpecCostPrice(BigDecimal.valueOf(Double.valueOf(sku[1])));
            goodsSpecDTO.setSpecStorage(Integer.valueOf(sku[2]));
            goodsSpecDTO.setSpecAttrValueName(specNameAndValues[i].replace(",", " "));
            if (i == 0) {
                goodsSpecDTO.setMainFlag(1);
            } else {
                goodsSpecDTO.setMainFlag(0);
            }
            if (picUrlArray == null) {
                goodsSpecDTO.setSpecMainPicture(null);
            } else {
                goodsSpecDTO.setSpecMainPicture(picUrlArray[specValue1.indexOf(specValue.toString().replace(" ", ",").split(",")[0])].split(",")[0]);
            }
            long minute = System.currentTimeMillis();
            String serial = String.valueOf(minute);
            double specSerial = Math.ceil(Math.random() * 10000000000f);
            goodsSpecDTO.setSpecSerial(Double.toString(specSerial));
            String replace = specValue.toString().replace(" ", ",");
            goodsSpecDTO.setSpecAttrName(replace.substring(0, replace.length() - 1));
            goodsSpecDTO.setId(IdWorker.getId());
            saveGoodsSpecDTOList.add(goodsSpecDTO);
        }
        log.info("sku保存成功");
        return saveGoodsSpecDTOList;
    }

    /**
     * 功能描述 处理sku的规格名称和规格值
     *
     * @param goodsTemplateExcel
     * @param specName           名称集合
     * @param specValue1         名称1的值的集合
     * @param specValue2         名称2的值的集合
     * @param specValue3         名称3的值的集合
     * @param defaultPicUrl
     * @return void
     * @author lishuo
     * @date 6/7/2020
     */
    private void getattrNameAndValue(GoodsTemplateExcel goodsTemplateExcel, List<String> specName, List<String> specValue1, List<String> specValue2, List<String> specValue3, String defaultPicUrl) {
        String goodsSpec = goodsTemplateExcel.getGoodsSepcNameValue();
        if (goodsSpec == null) {
            // 商品没有规格属性的话 使用默认的
            goodsSpec = "{\"颜色\":\"黑色\"};{\"颜色\":\"白色\"}";
            goodsTemplateExcel.setGoodsSetting("0,0,0;0,0,0");
            goodsTemplateExcel.setGoodsSepcNameValue(goodsSpec);
            goodsTemplateExcel.setPictureUrl(defaultPicUrl + ";" + defaultPicUrl);
            goodsTemplateExcel.setSkuPictureUrl(defaultPicUrl + ";" + defaultPicUrl);
        }
        String goodsSpecRel = goodsSpec.replace("{", "").replace("}", "").replace("\"", "");
        String[] specNameAndValues = goodsSpecRel.split(";");
        for (int i = 0; i < specNameAndValues.length; i++) {
            String[] oneSpecNameAndValue = specNameAndValues[i].split(",");
            for (int k = 0; k < oneSpecNameAndValue.length; k++) {
                String[] attrAndValue = oneSpecNameAndValue[k].split(":");
                if (!specName.contains(attrAndValue[0])) {
                    specName.add(attrAndValue[0]);
                }
                if (k == 0) {
                    if (!specValue1.contains(attrAndValue[1])) {
                        specValue1.add(attrAndValue[1]);
                    }
                }
                if (k == 1) {
                    if (!specValue2.contains(attrAndValue[1])) {
                        specValue2.add(attrAndValue[1]);
                    }
                }
                if (k == 2) {
                    if (!specValue3.contains(attrAndValue[1])) {
                        specValue3.add(attrAndValue[1]);
                    }
                }
            }
        }
    }

    /**
     * 功能描述 保存商品的属性
     *
     * @param goodsAttrAndValue    商品属性{key：value}
     * @param goodsId              goodsId
     * @param attributeSaveDTOList
     * @return void
     * @author lishuo
     * @date 6/7/2020
     */
    private void saveGoodsAttributeTemplate(String goodsAttrAndValue, Long goodsId, List<GoodsAttributeSaveDTO> attributeSaveDTOList) {
        if (goodsAttrAndValue != null) {
            String goodsAttrRe = goodsAttrAndValue.replace("{", "").replace("}", "").replace("\"", "");
            String[] goodsAttrs = goodsAttrRe.split(";");
            for (String s : goodsAttrs) {
                GoodsAttributeSaveDTO goodsAttributeDTO = new GoodsAttributeSaveDTO();
                String[] split = s.split(":");
                if (split != null && split.length == 2) {
                    goodsAttributeDTO.setAttrName(split[0]);
                    goodsAttributeDTO.setAttrValue(split[1]);
                }
                goodsAttributeDTO.setGoodsId(goodsId);
                goodsAttributeDTO.setId(IdWorker.getId());
                attributeSaveDTOList.add(goodsAttributeDTO);
            }
        }
        log.info("商品属性保存成功");
    }

    /**
     * 功能描述 保存商品售后信息和标签信息
     *
     * @param goodsEntity              实体
     * @param goodsTemplateExcel       excel中的数据
     * @param goodsInfoDTOList
     * @param goodsLabelRelSaveDTOList
     * @return void
     * @author lishuo
     * @date 6/7/2020
     */
    private void saveGoodsInfoTemplate(GoodsEntity goodsEntity, GoodsTemplateExcel goodsTemplateExcel, List<GoodsInfoDTO> goodsInfoDTOList, List<GoodsLabelRelSaveDTO> goodsLabelRelSaveDTOList) {
        Long goodsId = goodsEntity.getId();
        String afterSaleTemplate = goodsTemplateExcel.getAfterSaleTemplate();
        if (StringUtils.isNotEmpty(afterSaleTemplate)) {
            GoodsInfoDTO goodsInfoDTO = new GoodsInfoDTO();
            goodsInfoDTO.setAfterSale(afterSaleTemplate);
            goodsInfoDTO.setMobileBody(goodsTemplateExcel.getGoodsInfo());
            goodsInfoDTO.setId(goodsId);
            goodsInfoDTOList.add(goodsInfoDTO);
        } else {
            GoodsInfoDTO goodsInfoDTO = new GoodsInfoDTO();
            goodsInfoDTO.setId(goodsId);
            goodsInfoDTO.setMobileBody(goodsTemplateExcel.getGoodsInfo());
            goodsInfoDTOList.add(goodsInfoDTO);
        }
        //保存标签信息
        String goodsLabel = goodsTemplateExcel.getGoodsLabel();
        if (goodsLabel != null) {
            Long goodsLabelId = goodsLabelService.findByLabelName(goodsLabel);
            GoodsLabelRelSaveDTO goodsLabelRelSaveDTO = new GoodsLabelRelSaveDTO();
            if (goodsLabelId != null) {
                goodsLabelRelSaveDTO.setGoodsId(goodsId);
                goodsLabelRelSaveDTO.setLabelId(goodsLabelId);
                goodsLabelRelSaveDTO.setLabelName(goodsLabel);
            } else {
                goodsLabelRelSaveDTO.setGoodsId(goodsId);
            }
            goodsLabelRelSaveDTO.setId(IdWorker.getId());
            goodsLabelRelSaveDTOList.add(goodsLabelRelSaveDTO);
        }
        log.info("商品附加信息保存成功");
    }

    /**
     * 功能描述 商品保存
     *
     * @param goodsTemplateExcel       要保存的数据
     * @param specId                   specId
     * @param firstGoodsClassMap       商品一级分类集合
     * @param secondGoodsClassMap      商品二级分类集合
     * @param thirdGoodsClassMap       商品三级分类结合
     * @param firstStoreGoodsClassMap  店铺的一级分类
     * @param secondStoreGoodsClassMap 店铺的热机分类
     * @param brandDTOList             品牌的集合
     * @param freightTemplateDTOList   运费模板的集合
     * @param defaultPicUrl            默认的图片url
     * @param storeDTO                 店铺信息
     * @return GoodsEntity 保存的实体
     * @author lishuo
     * @date 6/7/2020
     */
    private GoodsEntity savegoods(GoodsTemplateExcel goodsTemplateExcel, long specId, Map<String, List<GoodsClassListDTO>> firstGoodsClassMap, Map<Long, List<GoodsClassListDTO>> secondGoodsClassMap, Map<Long, List<GoodsClassListDTO>> thirdGoodsClassMap, Map<String, List<StoreGoodsClassDTO>> firstStoreGoodsClassMap, Map<Long, List<StoreGoodsClassDTO>> secondStoreGoodsClassMap, List<BrandDTO> brandDTOList, List<FreightTemplateDTO> freightTemplateDTOList, String defaultPicUrl, StoreDTO storeDTO) {
        GoodsEntity goodsEntity = new GoodsEntity();

        // 商品分类判断
        String goodsGcName = goodsTemplateExcel.getGoodsGcName();
        String[] goodsGcNames = StringUtils.split(goodsGcName, ";");
        List<GoodsClassListDTO> goodsClassListDTOList = firstGoodsClassMap.get(goodsGcNames[0]);
        Long firstGcId = goodsClassListDTOList.get(0).getId();
        List<GoodsClassListDTO> collect = secondGoodsClassMap.get(firstGcId).stream().filter(goodsClassListDTO -> goodsClassListDTO.getGcName().equals(goodsGcNames[1])).collect(Collectors.toList());
        Long secondGcId = collect.get(0).getId();
        List<GoodsClassListDTO> collect1 = thirdGoodsClassMap.get(secondGcId).stream().filter(goodsClassListDTO -> goodsClassListDTO.getGcName().equals(goodsGcNames[2])).collect(Collectors.toList());
        Long thirdGcId = collect1.get(0).getId();
        goodsEntity.setFirstGcId(firstGcId);
        goodsEntity.setFirstGcName(goodsGcNames[0]);
        goodsEntity.setSecondGcId(secondGcId);
        goodsEntity.setSecondGcName(goodsGcNames[1]);
        goodsEntity.setThirdGcId(thirdGcId);
        goodsEntity.setThirdGcName(goodsGcNames[2]);
        // 店铺商品分类的判断 为空 或者有两个
        if (goodsTemplateExcel.getStoreGcName() != null) {
            String storeGcName = goodsTemplateExcel.getStoreGcName();
            String[] storeGcNames = StringUtils.split(storeGcName, ";");
            if (storeGcNames.length == GoodsClassErrorCodeEnum.STORE_GOODS_CLASS_SECOND.value()) {
                List<StoreGoodsClassDTO> storeGoodsClassDTOList = firstStoreGoodsClassMap.get(storeGcNames[0]);
                if (CollectionUtil.isNotEmpty(storeGoodsClassDTOList)) {
                    Long firstStoreGcId = storeGoodsClassDTOList.get(0).getId();
                    List<StoreGoodsClassDTO> collect2 = secondStoreGoodsClassMap.get(firstStoreGcId).stream().filter(storeGoodsClassDTO -> storeGoodsClassDTO.getGcName().equals(storeGcNames[1])).collect(Collectors.toList());
                    if (CollectionUtil.isNotEmpty(collect2)) {
                        goodsEntity.setFirstStoreGoodsGcId(firstStoreGcId);
                        goodsEntity.setFirstStoreGoodsGcName(storeGcNames[0]);
                        Long secondStoreGcId = collect2.get(0).getId();
                        goodsEntity.setSecondStoreGoodsGcId(secondStoreGcId);
                        goodsEntity.setSecondStoreGoodsGcName(storeGcNames[1]);
                    }
                }

            }
        }
        // 品牌的判断
        String goodsBrandName = goodsTemplateExcel.getGoodsBrand();
        if (StringUtils.isNotEmpty(goodsBrandName)) {
            List<BrandDTO> collect2 = brandDTOList.stream().filter(brandDTO -> brandDTO.getBrandName().equals(goodsBrandName)).collect(Collectors.toList());
            if (CollectionUtil.isNotEmpty(collect2)) {
                goodsEntity.setBrandId(collect2.get(0).getId());
                goodsEntity.setBrandName(goodsBrandName);
            }
        }
        // 商品主图
        String[] pictureUrls = StringUtils.split(goodsTemplateExcel.getPictureUrl(), ";");
        if (pictureUrls != null) {
            goodsEntity.setGoodsMainPicture(pictureUrls[0]);
        }
        // 运费模板的判断
        Integer expressFlag = goodsTemplateExcel.getExpressFlag();
        if (expressFlag != null && 1 == expressFlag) {
            String freightTemplateName = goodsTemplateExcel.getFreightTemplate();
            List<FreightTemplateDTO> collect2 = freightTemplateDTOList.stream().filter(freightTemplateDTO -> freightTemplateDTO.getTemplateName().equals(freightTemplateName)).collect(Collectors.toList());
            goodsEntity.setDevlierType(expressFlag);
            goodsEntity.setFreightTemplateId(collect2.get(0).getId());
            goodsEntity.setExpressFlag(1);
        }
        goodsEntity.setDevlierType(expressFlag);
        // 发票
        goodsEntity.setInvoiceFlag(goodsTemplateExcel.getInvoice());
        goodsEntity.setInvoiceType(goodsTemplateExcel.getInvoiceType());
        goodsEntity.setInvoiceContent(goodsTemplateExcel.getInvoiceContent());
        // 其他信息
        goodsEntity.setSpecId(specId);
        goodsEntity.setStoreName(storeDTO.getStoreName());
        goodsEntity.setStoreId(storeDTO.getId());
        goodsEntity.setStoreType(storeDTO.getStoreType());
        goodsEntity.setSpecCostPrice(goodsTemplateExcel.getGoodsCostPrice());
        goodsEntity.setSpecSellPrice(goodsTemplateExcel.getGoodsSellPrice());
        goodsEntity.setGoodsName(goodsTemplateExcel.getGoodsName());
        goodsEntity.setGoodsSubTitle(goodsTemplateExcel.getGoodsSubName());
        if (goodsTemplateExcel.getPictureUrl() == null) {
            goodsEntity.setGoodsMainPicture(defaultPicUrl);
        } else {
            goodsEntity.setGoodsMainPicture(goodsTemplateExcel.getPictureUrl().split(";")[0]);
        }
        long serialId = IdWorker.getId();
        String serialStr = Long.toString(serialId).substring(9, 19);
        goodsEntity.setGoodsSerial(Long.valueOf(serialStr));
        goodsEntity.setGcId(thirdGcId);
        goodsEntity.setGcName(goodsGcNames[2]);
        goodsEntity.setFreightBearType(goodsTemplateExcel.getFreightBearType());
        // 未上架
        goodsEntity.setGoodsShow(2);
        // 审核状态填充
        Long goodsId = IdWorker.getId();
        goodsEntity.setId(goodsId);
        log.info("商品保存成功");
        return goodsEntity;
    }

    /**
     * 功能描述 过滤外链图片
     *
     * @param goodsTemplateExcel
     * @return void
     * @author lishuo
     * @date 6/7/2020
     */
    private void replacePicUrl(GoodsTemplateExcel goodsTemplateExcel) {
        String[] pictureUrlArray = StringUtils.split(goodsTemplateExcel.getPictureUrl(), ";");
        // 商品主图进行替换
        if (pictureUrlArray != null && pictureUrlArray.length > 0) {
            StringBuilder pictureUrl = new StringBuilder();
            for (int i = 0; i < pictureUrlArray.length; i++) {
                if (!pictureUrlArray[i].startsWith("/group1")) {
                    pictureUrl.append(uploadService.downloadToUrl(pictureUrlArray[i])).append(";");
                } else {
                    pictureUrl.append(pictureUrlArray[i]).append(";");
                }
            }
            goodsTemplateExcel.setPictureUrl(pictureUrl.toString().substring(0, pictureUrl.length() - 1));
        }
        // sku 图片进行替换
        String skuPictureUrl = goodsTemplateExcel.getSkuPictureUrl();
        if (skuPictureUrl != null && skuPictureUrl.length() > 0) {
            StringBuilder skuPicSb = new StringBuilder();
            String[] skuPic = StringUtils.split(skuPictureUrl, ";");
            if (skuPic.length > 0) {
                for (int i = 0; i < skuPic.length; i++) {
                    String[] skuPicGroup = StringUtils.split(skuPic[i], ",");
                    for (int k = 0; k < skuPicGroup.length; k++) {
                        if (!skuPicGroup[k].startsWith("/group1")) {
                            skuPicSb.append(uploadService.downloadToUrl(skuPicGroup[k])).append(",");
                        } else {
                            skuPicSb.append(skuPicGroup[k]).append(",");
                        }
                    }
                    skuPicSb.deleteCharAt(skuPicSb.lastIndexOf(",")).append(";");
                }
            }
            goodsTemplateExcel.setSkuPictureUrl(skuPicSb.toString().substring(0, skuPicSb.length() - 1));
        }
    }


    /**
     * 购物车校验商品信息
     *
     * @param goodsId 商品ID
     * @param specId  规格ID
     * @return
     */
    @Override

    public ValidateGoodsDTO findValidationGoods(@RequestParam("goodsId") Long goodsId, @RequestParam("specId") Long specId) {
        return baseDao.findValidationGoods(goodsId, specId);
    }

    @Override

    public GoodsDTO findByGoodsId(@RequestParam("goodsId") Long goodsId) {

        return baseDao.selectByIdToRec(goodsId);
    }

    /**
     * 功能描述  分页查询
     *
     * @param params: 参数
     * @author lishuo
     * @date 2020/7/27 14:15
     * @return: com.leimingtech.commons.tools.page.PageData<com.leimingtech.modules.dto.goods.GoodsDTO>
     **/
    @Override

    public PageData<GoodsDTO> pageGoodsDTO(@RequestParam Map<String, Object> params) {
        Integer page = Integer.valueOf(params.get(Constant.PAGE).toString());
        Integer limit = Integer.valueOf(params.get(Constant.LIMIT).toString());
        Page goodsPageDTO = new Page<>(page, limit);
        List<GoodsDTO> goodsDTOList = baseDao.selectByPage(goodsPageDTO, params);
        return new PageData<>(goodsDTOList, goodsPageDTO.getTotal());
    }

    @Override
    public GoodsImportResultVO importGoods(@RequestPart("file") MultipartFile file, @RequestParam(name = "storeId") Long storeId) {
        // 查询出所有的分类
        List<GoodsClassListDTO> goodsClassListDTOList = goodsClassService.findAllGoodsClass();
        // 查询出所有的品牌
        List<BrandDTO> brandDTOList = brandService.findAllBrand();
        // 查询出所有的店铺的商品分类
        List<StoreGoodsClassDTO> storeGoodsClassDTOList = storeGoodsClassService.findAllStoreGoodsClass(storeId);

        // 查询出所有的标签信息
        Map<String, Object> param = new HashMap<>(10);
        param.put("del_flag", "0");
        List<GoodsLabelDTO> goodsLabelDTOList = goodsLabelService.list(param);


        //查询出所有的运费模板信息
        param.put("store_id", storeId);
        List<FreightTemplateDTO> freightTemplateDTOList = freightTemplateService.list(param);


        // 处理list集合
        List<GoodsClassListDTO> firstGoodsClassListDTOList = new ArrayList<>();
        List<GoodsClassListDTO> secondGoodsClassListDTOList = new ArrayList<>();
        List<GoodsClassListDTO> thirdGoodsClassListDTOList = new ArrayList<>();
        for (GoodsClassListDTO goodsClassListDTO : goodsClassListDTOList) {
            String gcIdpath = goodsClassListDTO.getGcIdpath();
            String[] split = StringUtils.split(gcIdpath, ",");
            if (split.length == 1) {
                firstGoodsClassListDTOList.add(goodsClassListDTO);
            } else if (split.length == 2) {
                secondGoodsClassListDTOList.add(goodsClassListDTO);
            } else {
                thirdGoodsClassListDTOList.add(goodsClassListDTO);
            }
        }
        Map<String, List<GoodsClassListDTO>> firstGoodsClassMap = firstGoodsClassListDTOList.stream().collect(Collectors.groupingBy(GoodsClassListDTO::getGcName));
        Map<Long, List<GoodsClassListDTO>> secondGoodsClassMap = secondGoodsClassListDTOList.stream().collect(Collectors.groupingBy(GoodsClassListDTO::getGcParentId));
        Map<Long, List<GoodsClassListDTO>> thirdGoodsClassMap = thirdGoodsClassListDTOList.stream().collect(Collectors.groupingBy(GoodsClassListDTO::getGcParentId));


        GoodsImportListener goodsImportListener = new GoodsImportListener(firstGoodsClassMap, secondGoodsClassMap, thirdGoodsClassMap, freightTemplateDTOList);
        try {
            EasyExcel.read(file.getInputStream(), GoodsTemplateExcel.class, goodsImportListener).sheet().doRead();
        } catch (Exception e) {
            log.error("导入出现异常:" + e);
            throw new ServiceException("导入出现异常");
        }
        // 获取校验后的错误信息 不为空 直接返回 不保存数据
        List<Map<String, String>> errorMessage = goodsImportListener.getErrorMessage();
        if (errorMessage != null && errorMessage.size() > 0) {
            GoodsImportResultVO goodsImportResultVO = new GoodsImportResultVO();
            goodsImportResultVO.setErrorMessage(errorMessage);
            return goodsImportResultVO;
        }
        List<GoodsTemplateExcel> list = goodsImportListener.getList();
        // 进行保存逻辑
        List<GoodsEntity> listGoodsEntity = goodsImportListener.getListGoodsEntity();
        if (listGoodsEntity.size() != list.size()) {
            GoodsImportResultVO goodsImportResultVO = new GoodsImportResultVO();
            goodsImportResultVO.setErrorMessage(errorMessage);
            return goodsImportResultVO;
        }
        // 店铺信息的获取
        StoreDTO storeDTO = storeService.get(storeId);
        //导入保存商品信息
        this.saveGoodsFromExcel(list, goodsClassListDTOList, brandDTOList, storeGoodsClassDTOList, goodsLabelDTOList, freightTemplateDTOList, storeDTO);
        GoodsImportResultVO goodsImportResultVO = new GoodsImportResultVO();
        goodsImportResultVO.setGoodsSum(list.size());
        return goodsImportResultVO;
    }

    /**
     * 商品推荐
     *
     * @param goodsId 商品ID
     * @param userId  用户ID
     * @param addFlag 是否补充商品（0：不补充 1：补充）
     * @return
     */
//
//    @Override
//    public List<GoodsRecommendDTO> hotRecommend(@RequestParam(value = "goodsId", required = false) Long goodsId,
//                                                @RequestParam(value = "userId", required = false) Long userId,
//                                                @RequestParam("addFlag") Integer addFlag) {
//        List<String> idList = null;
//        try {
//            // 相关推荐
//            if (goodsId != null) {
//                idList = recommendService.relateRecommend(goodsId.toString(), addFlag);
//            } else if (userId != null) {
//                // 猜你喜欢
//                idList = recommendService.guessYouLike(userId.toString(), addFlag);
//            } else {
//                //  热门推荐
//                idList = recommendService.recommandByHotList(addFlag);
//            }
//        } catch (Exception e) {
//            log.error("推荐商品查询异常：" + e);
//            throw new ServiceException("推荐商品查询失败");
//        }
//
//        if (CollectionUtils.isEmpty(idList)) {
//            return Collections.emptyList();
//        }
//        return baseDao.getGoodsList(idList);
//    }
    @Override
    public Integer selectStoreCountByGoodsIds(@RequestBody List<Long> goodsIds) {
        return baseDao.selectStoreCountByGoodsIds(goodsIds);
    }


    @Override
    public void deleteBatch(@RequestBody Long[] goodsIds) {
        List<GoodsEntity> goodsEntities = baseDao.selectList(Wrappers.<GoodsEntity>lambdaQuery()
                .in(GoodsEntity::getId, goodsIds)
                .eq(GoodsEntity::getPublishFlag, GoodsStatusEnum.NO_PUBLISH_FLAG));
        List<Long> goodsIdList = goodsEntities.stream().map(GoodsEntity::getId).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(goodsIdList)) {
            return;
        }
        Long[] arrays = goodsIdList.toArray(new Long[goodsIdList.size()]);
        log.info("商品删除开始");
        //删除规格表信息
        goodsSpecService.deleteBatchBygoodsIds(arrays);
        //删除商品属性信息
        goodsAttributeService.deleteBatchBygoodsIds(arrays);
        //删除商品附加信息
        goodsInfoService.deleteBatchBygoodsIds(arrays);
        //删除图片表信息
        specAttributePictureService.deleteBatchBygoodsIds(arrays);
        //删除商品与规格中间表信息
        specAttributeRelationService.deleteBatchBygoodsIds(arrays);
        //删除规格属性信息
        specAttributeService.deleteBatchBygoodsIds(arrays);
        //删除规格属性值信息
        specAttributeValueService.deleteBatchBygoodsIds(arrays);
        //删除商品标签信息
        goodsLabelRelService.deleteBatchBygoodsIds(arrays);

        baseDao.delete(Wrappers.<GoodsEntity>lambdaQuery()
                .in(GoodsEntity::getId, arrays)
                .eq(GoodsEntity::getDelFlag, DelFlagEnum.NORMAL.value()));
        log.info("商品删除成功");
    }
}
