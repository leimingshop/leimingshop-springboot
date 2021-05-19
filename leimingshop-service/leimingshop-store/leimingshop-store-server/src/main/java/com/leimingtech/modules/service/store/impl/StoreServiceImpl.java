/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.service.store.impl;

import cn.hutool.core.bean.BeanUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.leimingtech.commons.mybatis.service.impl.CrudServiceImpl;
import com.leimingtech.commons.tools.config.AddressPrefixProperties;
import com.leimingtech.commons.tools.page.PageData;
import com.leimingtech.commons.tools.redis.UserDetailRedis;
import com.leimingtech.commons.tools.security.user.SellerDetail;
import com.leimingtech.commons.tools.utils.ConvertUtils;
import com.leimingtech.commons.tools.utils.DateUtils;
import com.leimingtech.exception.ServiceException;
import com.leimingtech.modules.dao.store.StoreDao;
import com.leimingtech.modules.dto.auth.SettingStoreAuthDTO;
import com.leimingtech.modules.dto.auth.StoreAuthDTO;
import com.leimingtech.modules.dto.evaluate.FindEvaluateStoreDTO;
import com.leimingtech.modules.dto.favorites.StoreFavoritesDTO;
import com.leimingtech.modules.dto.grade.StoreGradeDTO;
import com.leimingtech.modules.dto.index.IndexDTO;
import com.leimingtech.modules.dto.index.StoreUserFunctionDTO;
import com.leimingtech.modules.dto.role.UpdateStoreRoleDTO;
import com.leimingtech.modules.dto.store.*;
import com.leimingtech.modules.dto.storeclass.StoreClassDTO;
import com.leimingtech.modules.dto.storeclass.StoreGoodsClassDTO;
import com.leimingtech.modules.dto.user.StoreUserDTO;
import com.leimingtech.modules.dto.usermanage.StoreUserManageDTO;
import com.leimingtech.modules.entity.store.StoreEntity;
import com.leimingtech.modules.enums.EvaluateEnum;
import com.leimingtech.modules.enums.store.StoreEnum;
import com.leimingtech.modules.service.auth.StoreAuthService;
import com.leimingtech.modules.service.evaluate.EvaluateStoreService;
import com.leimingtech.modules.service.favorites.StoreFavoritesService;
import com.leimingtech.modules.service.goods.GoodsService;
import com.leimingtech.modules.service.goods.GoodsSyncIndexService;
import com.leimingtech.modules.service.grade.StoreGradeService;
import com.leimingtech.modules.service.index.StoreUserFunctionService;
import com.leimingtech.modules.service.menu.StoreMenuService;
import com.leimingtech.modules.service.role.StoreRoleService;
import com.leimingtech.modules.service.store.StoreAuditService;
import com.leimingtech.modules.service.store.StoreAuthAuditService;
import com.leimingtech.modules.service.store.StoreService;
import com.leimingtech.modules.service.storeclass.StoreClassService;
import com.leimingtech.modules.service.user.StoreUserService;
import com.leimingtech.modules.service.usermanage.StoreUserManageService;
import com.leimingtech.modules.statuscode.StoreStatusCode;
import com.leimingtech.mq.constant.MqConstant;
import com.leimingtech.mq.service.RabbitMqSendService;
import com.leimingtech.upload.service.UploadService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.text.DecimalFormat;
import java.util.*;

import static com.leimingtech.modules.enums.FavoriteEnum.ALREADY_FAVORITE;
import static com.leimingtech.modules.enums.FavoriteEnum.NOT_FAVORITE;

/**
 * 店铺表
 *
 * @author chengqian
 * @email 543826372@qq.com
 * @since 1.0.0 2019-06-06
 */
@Service

public class StoreServiceImpl extends CrudServiceImpl<StoreDao, StoreEntity, StoreDTO> implements StoreService {

    @Autowired

    private StoreFavoritesService storeFavoritesService;

    @Autowired

    private EvaluateStoreService evaluateStoreService;
    @Autowired

    private StoreAuthService storeAuthService;
    @Autowired
    private GoodsService goodsService;

    @Autowired

    private StoreUserService storeUserService;

    @Autowired

    private StoreUserManageService storeUserManageService;

    @Autowired

    private StoreGradeService storeGradeService;

    @Autowired

    private StoreClassService storeClassService;

    @Autowired
    private GoodsSyncIndexService goodsSyncIndexService;

    @Autowired
    private RabbitMqSendService rabbitMqSendService;

    @Autowired

    private StoreRoleService storeRoleService;

    @Autowired

    private StoreAuditService storeAuditService;

    @Autowired

    private StoreAuthAuditService storeAuthAuditService;

    @Autowired
    private UserDetailRedis userDetailRedis;

    @Autowired

    private StoreUserFunctionService storeUserFunctionService;

    @Autowired

    private StoreMenuService storeMenuService;

    @Autowired
    private UploadService uploadService;

    @Override
    public QueryWrapper<StoreEntity> getWrapper(Map<String, Object> params) {
        String storeId = (String) params.get("storeId");
        String storeName = (String) params.get("storeName");
        String gradeId = (String) params.get("gradeId");
        String storeType = (String) params.get("storeType");
        String startTime = (String) params.get("startTime");
        String endTime = (String) params.get("endTime");
        String storeIdList = (String) params.get("storeIdList");
        List<Long> storeIdLists = JSON.parseArray(storeIdList, Long.class);

        QueryWrapper<StoreEntity> wrapper = new QueryWrapper<>();
        wrapper.eq(StringUtils.isNotBlank(storeId), "id", storeId);
        wrapper.eq(StringUtils.isNotBlank(gradeId), "grade_id", gradeId);
        wrapper.eq(StringUtils.isNotBlank(storeType), "store_type", storeType);
        wrapper.like(StringUtils.isNotBlank(storeName), "store_name", storeName);
        wrapper.ge(StringUtils.isNotBlank(startTime), "create_date", startTime);
        wrapper.le(StringUtils.isNotBlank(endTime), "create_date", endTime);
        wrapper.notIn(CollectionUtils.isNotEmpty(storeIdLists), "id", storeIdLists);
        return wrapper;
    }

    /**
     * 分页查询信息
     *
     * @param params 分页参数
     * @return
     */
    @Override

    public PageData<PageStoreDTO> findPage(@RequestParam Map<String, Object> params) {
        //分页
        IPage<StoreEntity> page = getPage(params, "ls.create_date", false);
        //查询
        String startTime = (String) params.get("startTime");
        String endTime = (String) params.get("endTime");
        if (StringUtils.isNotBlank(startTime) && StringUtils.isNotBlank(endTime)) {
            startTime = startTime + " 00:00:00";
            endTime = endTime + " 23:59:59";
            params.put("startTime", startTime);
            params.put("endTime", endTime);
        }
        List<PageStoreDTO> list = baseDao.findPage(params);
        return new PageData<>(list, page.getTotal());
    }

    /**
     * 入住待审核分页
     *
     * @param params
     * @return
     */

    @Override
    public PageData<PageStoreDTO> findAuditPage(@RequestParam Map<String, Object> params) {
        //分页
        IPage<StoreEntity> page = getPage(params, null, false);

        //查询
        String startTime = (String) params.get("startTime");
        String endTime = (String) params.get("endTime");
        if (StringUtils.isNotBlank(startTime) && StringUtils.isNotBlank(endTime)) {
            startTime = startTime + " 00:00:00";
            endTime = endTime + " 23:59:59";
            params.put("startTime", startTime);
            params.put("endTime", endTime);
        }
        List<PageStoreDTO> list = baseDao.findAuditPage(params);
        return new PageData<>(list, page.getTotal());
    }

    /**
     * 根据ID获取信息
     *
     * @param id 主键ID
     * @return
     */
    @Override

    public StoreDTO get(Long id) {
        StoreDTO storeDTO = super.get(id);
        if (storeDTO == null) {
            return new StoreDTO();
        }
        if (StringUtils.isBlank(storeDTO.getQrCode()) &&
                storeDTO.getRegisterAuditStatus() == StoreEnum.AUDIT_STATUS_YES.value()) {
            storeDTO.setQrCode(uploadService.uploadQrCode(AddressPrefixProperties.adddressPrefix + "#/pagesA/store/home?storeId=" + id));
            updateById(ConvertUtils.sourceToTarget(storeDTO, StoreEntity.class));
        }
        return storeDTO;
    }

    /**
     * 新增数据
     *
     * @param dto 参数实体
     */

    @Override

    public Long saveEntity(@RequestBody StoreDTO dto) {

        StoreEntity storeEntity = ConvertUtils.sourceToTarget(dto, StoreEntity.class);
        super.insert(storeEntity);

        return storeEntity.getId();
    }

    /**
     * 修改信息
     *
     * @param dto 参数提示
     */
    @Override

    public void update(@RequestBody UpdateStoreInfo dto) {
        //----------- 更新店铺
        StoreDTO storeDTO = ConvertUtils.sourceToTarget(dto.getFindStoreDTO(), StoreDTO.class);
        //校验店铺等级，如果店铺下的商品数量超过当前等级所限制的数量，编辑失败
        Integer goodsCount = goodsService.findGoodsCount(storeDTO.getId(), null);
        // 查询店铺等级所限制的数量
        StoreGradeDTO storeGradeDTO = storeGradeService.get(storeDTO.getGradeId());
        if (goodsCount != null && goodsCount.intValue() > storeGradeDTO.getSgGoodsLimit()) {
            throw new ServiceException(StoreStatusCode.STORE_GOODS_MORE);
        }
        storeDTO.setIsEnable(dto.getIsEnable());
        super.update(storeDTO);
        /**
         * 如果被禁用的话，
         * 下架店铺下所有的商品，
         * 已经下单的不影响，
         * 更新es，
         * 更新购物车
         * 发布商品做限制
         */
        if (dto.getIsEnable() == 1) {
            // 下架所有的商品，规格
            goodsService.updateShowByStoreId(storeDTO.getId());
            //更新es
            goodsSyncIndexService.goodsIndexSyncByStoreId(storeDTO.getId());
            //更新购物车
            rabbitMqSendService.sendMsg(MqConstant.LEIMINGTECH_STORE_ID_CART_GOODS_STATUS, String.valueOf(storeDTO.getId()));
        }

        //----------- 更新店铺分类
        if (dto.getStoreAuthDTO() != null) {
            StoreAuthDTO storeAuthDTO = ConvertUtils.sourceToTarget(dto.getStoreAuthDTO(), StoreAuthDTO.class);
            storeAuthService.update(storeAuthDTO);
        }
        storeClassService.updateBatch(dto);

        //----------- 更新店铺用户
        StoreUserDTO storeUserDTO = ConvertUtils.sourceToTarget(dto, StoreUserDTO.class);
        storeUserService.update(storeUserDTO);

        // -----------更新店铺用户关联表
        StoreUserManageDTO storeUserManageDTO = new StoreUserManageDTO();
        storeUserManageDTO.setStoreUserId(storeUserDTO.getId());
        storeUserManageDTO.setIsEnable(dto.getIsEnable());
        storeUserManageDTO.setRoleId(dto.getRoleId());
        storeUserManageDTO.setStoreId(dto.getFindStoreDTO().getId());
        storeUserManageService.updateByUserId(storeUserManageDTO);

        //----------- 同步商品更新数据库
        goodsService.updateStoreName(storeDTO.getStoreName(), storeDTO.getId(), storeDTO.getStoreType());
        // 更新店铺索引
        rabbitMqSendService.sendMsg(MqConstant.LEIMINGTECH_STORE_INDEX_UPDATE_SINGLE, String.valueOf(storeDTO.getId()));
    }


    /**
     * 根据ID删除
     *
     * @param ids 店铺ID
     */

    @Override
    public void delete(@RequestBody Long[] ids) {
        // 根据店铺ID 查询出店铺用户
        Long[] userId = storeUserManageService.findUserIdByUserId(ids);
        // 删除店铺用户
        if (userId.length > 0) {
            storeUserService.delete(userId);
        }
        // 删除店铺
        super.delete(ids);
        // 删除店铺认证
        storeAuthService.deleteByStoreId(ids);
        // 删除商品
        goodsService.deleteByStoreId(ids);
        //删除店铺下角色，用户，去掉用户角色关联
        storeUserManageService.updateByStoreId(ids[0]);
        // 删除店铺
        rabbitMqSendService.sendMsg(MqConstant.LEIMINGTECH_DELETE_STORE_INDEX, String.valueOf(ids[0]));
    }

    /**
     * 查询导出
     *
     * @param params 查询参数
     * @return
     */
    @Override

    public List<StoreDTO> list(@RequestParam Map<String, Object> params) {
        return super.list(params);
    }


    /**
     * 店铺首页
     *
     * @param storeId
     * @return
     */

    @Override
    public StoreIndexDTO selectStoreIndex(Long storeId, @RequestParam("userId") Long userId) {

        StoreEntity storeEntity = baseDao.selectById(storeId);
        StoreIndexDTO storeIndexDTO = ConvertUtils.sourceToTarget(storeEntity, StoreIndexDTO.class);
        Integer count = goodsService.selectCount(storeId);
        storeIndexDTO.setGoodsCount(count);
        storeIndexDTO.setIsFavorite(favoritesFlag(storeId, userId));
        return storeIndexDTO;
    }

    /**
     * 获取店铺关注标记
     *
     * @param storeId
     * @param userId
     * @return
     */
    private Integer favoritesFlag(Long storeId, Long userId) {
        if (null != userId) {
            StoreFavoritesDTO storeFavoritesDTO = storeFavoritesService.isFavorite(userId, storeId);
            if (null != storeFavoritesDTO) {
                return ALREADY_FAVORITE.value();
            }
        }
        return NOT_FAVORITE.value();
    }

    /**
     * 店铺详情
     *
     * @param storeId
     * @return
     */

    @Override
    public StoreInfoDTO selectStoreInfo(Long storeId,
                                        @RequestParam(value = "userId", required = false) Long userId) {
        StoreEntity storeEntity = baseDao.selectById(storeId);
        StoreInfoDTO storeInfoDTO = ConvertUtils.sourceToTarget(storeEntity, StoreInfoDTO.class);
        //店铺关注人数
        storeInfoDTO.setFavoriteCount(getStoreGoodsCount(storeId));
        //TODO 店铺评分待完善
        //店铺评分
        FindEvaluateStoreDTO findEvaluateStoreDTO = getFindEvaluateStoreDTO(storeId);
        findEvaluateStoreDTO = new FindEvaluateStoreDTO();
        if (storeEntity.getGoodsEvaluate() == null) {
            double c = Math.random() * 5;
            double d = Math.random() * 5;
            double e = Math.random() * 5;
            DecimalFormat df = new DecimalFormat("0.00");
            storeEntity.setAfterSaleEvaluate(Double.valueOf(df.format(c)));
            storeEntity.setLogisticsEvaluate(Double.valueOf(df.format(d)));
            storeEntity.setGoodsEvaluate(Double.valueOf(df.format(e)));
            baseDao.updateById(storeEntity);
        }
        findEvaluateStoreDTO.setAfterSaleEvaluate(storeEntity.getAfterSaleEvaluate());
        findEvaluateStoreDTO.setLogisticsEvaluate(storeEntity.getLogisticsEvaluate());
        findEvaluateStoreDTO.setGoodsEvaluate(storeEntity.getGoodsEvaluate());
        findEvaluateStoreDTO.setStoreLevel(5);
        storeInfoDTO.setFindEvaluateStoreDTO(findEvaluateStoreDTO);
        //店铺电话
        Long id = storeId;
        StoreAuthDTO storeAuthDTO = storeAuthService.findByStoreId(id);
        storeInfoDTO.setCompanyPhone(storeAuthDTO.getCompanyPhone());
        storeInfoDTO.setElectronicBusinessLicense(storeAuthDTO.getElectronicBusinessLicense());
        storeInfoDTO.setSynthesizeEvaluate(EvaluateEnum.NOT_EVALUATE_NUM.value());

        if (null != userId) {
            //获取用户是否关注店铺
            storeInfoDTO.setIsFavorite(favoritesFlag(storeId, userId));
        } else {
            //用户未登录 默认未收藏
            storeInfoDTO.setIsFavorite(NOT_FAVORITE.value());
        }

        return storeInfoDTO;
    }

    /**
     * 移动端商品详情页 店铺数据
     *
     * @param storeId
     * @return
     */

    @Override
    public GoodsInfoStoreDTO selectGoodsInfoStore(Long storeId,
                                                  @RequestParam(value = "userId", required = false) Long userId) {
        StoreEntity storeEntity = baseDao.selectById(storeId);
        GoodsInfoStoreDTO goodsInfoStoreDTO = ConvertUtils.sourceToTarget(storeEntity, GoodsInfoStoreDTO.class);
        //店铺关注人数
        goodsInfoStoreDTO.setFavoriteCount(getStoreGoodsCount(storeId));
        //店铺综合评分
        FindEvaluateStoreDTO findEvaluateStoreDTO = getFindEvaluateStoreDTO(storeId);
        if (BeanUtil.isEmpty(findEvaluateStoreDTO)) {
            goodsInfoStoreDTO.setSynthesizeEvaluate(EvaluateEnum.NOT_EVALUATE_NUM.value());
        } else {
            double synthesizeEvaluate = (findEvaluateStoreDTO.getSevalDeliverycredit() + findEvaluateStoreDTO.getSevalDesccredit()
                    + findEvaluateStoreDTO.getSevalServicecredit()) / EvaluateEnum.EVALUATE_NUM.value();
            goodsInfoStoreDTO.setSynthesizeEvaluate(synthesizeEvaluate);
        }

        //获取用户是否关注店铺
        if (null != userId) {
            //获取用户是否关注店铺
            goodsInfoStoreDTO.setIsFavorite(favoritesFlag(storeId, userId));
        } else {
            //用户未登录 默认未收藏
            goodsInfoStoreDTO.setIsFavorite(NOT_FAVORITE.value());
        }
        return goodsInfoStoreDTO;
    }

    /**
     * 获取店铺商品数量
     *
     * @param storeId 店铺ID
     * @return
     */
    private Integer getStoreGoodsCount(Long storeId) {
        return storeFavoritesService.selectFavoriteCount(storeId);
    }

    /**
     * 获取店铺评分
     *
     * @param storeId 店铺ID
     * @return FindEvaluateStoreDTO 店铺评分对象
     */
    private FindEvaluateStoreDTO getFindEvaluateStoreDTO(Long storeId) {
        return evaluateStoreService.findAvgPoint(storeId);
    }


    /**
     * 更新店铺信息
     *
     * @param settingStoreAuthDTO 参数
     */
    @Override

    public void updateSetting(@RequestBody SettingStoreAuthDTO settingStoreAuthDTO) {
        // 更新店铺信息
        StoreDTO storeDTO = new StoreDTO();
        storeDTO.setId(settingStoreAuthDTO.getStoreId());
        storeDTO.setStoreLogo(settingStoreAuthDTO.getStoreLogo());
        storeDTO.setStoreName(settingStoreAuthDTO.getStoreName());
        storeDTO.setStoreIntro(settingStoreAuthDTO.getStoreIntro());
        update(storeDTO);
        goodsService.updateStoreName(settingStoreAuthDTO.getStoreName(), settingStoreAuthDTO.getStoreId(), null);

        // 更新店铺认证
        StoreAuthDTO storeAuthDTO = ConvertUtils.sourceToTarget(settingStoreAuthDTO, StoreAuthDTO.class);
        if (storeAuthDTO.getId() != null) {
            storeAuthService.update(storeAuthDTO);
        } else {
            storeAuthService.save(storeAuthDTO);
        }
        // 更新店铺索引
        rabbitMqSendService.sendMsg(MqConstant.LEIMINGTECH_STORE_INDEX_UPDATE_SINGLE, String.valueOf(storeDTO.getId()));

    }


    /**
     * 查询店铺信息
     *
     * @param userId 店铺信息
     * @return
     */
    @Override

    public List<UserAllStoreDTO> findByUserId(@RequestParam("userId") Long userId) {


        return baseDao.findByUserId(userId);
    }


    /**
     * 根据店铺等级查询是否有关联店铺
     *
     * @param gradeId
     * @return
     */

    @Override
    public Integer selectStoreByGrade(Long gradeId) {
        return baseDao.selectCount(new QueryWrapper<StoreEntity>().eq("grade_id", gradeId));
    }

    /**
     * 查询店铺名称是否重复
     *
     * @param storeName 店铺名称
     * @param storeId   店铺ID
     * @return
     */

    @Override

    public Integer findStoreName(@RequestParam("storeName") String storeName,
                                 @RequestParam(value = "storeId", required = false) Long storeId) {

        return baseDao.findStoreName(storeName, storeId);
    }

    /**
     * 查询店铺等级关联的店铺数量
     *
     * @param gradeId 等级ID
     * @return
     */

    @Override
    public Integer findGradeCount(@RequestParam("gradeId") Long gradeId) {
        return baseDao.findGradeCount(gradeId);
    }

    @Override

    public List<StoreDTO> selectStoresByIds(@RequestBody Long[] storeIds) {
        List<StoreEntity> storeEntities = baseDao.selectBatchIds(Arrays.asList(storeIds));
        List<StoreDTO> storeDTOList = ConvertUtils.sourceToTarget(storeEntities, StoreDTO.class);
        return storeDTOList;
    }

    /**
     * 导出所有数据
     *
     * @param params
     * @return
     */

    @Override
    public List<ExportStoreDTO> exportList(@RequestParam Map<String, Object> params) {

        String startTime = (String) params.get("startTime");
        String endTime = (String) params.get("endTime");
        if (StringUtils.isNotBlank(startTime) && StringUtils.isNotBlank(endTime)) {
            startTime = startTime + " 00:00:00";
            endTime = endTime + " 23:59:59";
            params.put("startTime", startTime);
            params.put("endTime", endTime);
        }
        List<ExportStoreDTO> page = baseDao.exportList(params);
        for (ExportStoreDTO exportStoreDTO : page) {
            if (Integer.valueOf(exportStoreDTO.getStoreType()) == StoreEnum.SELF_STORE.value()) {
                exportStoreDTO.setStoreType("自营商户");
            } else {
                exportStoreDTO.setStoreType("普通商户");
            }

            if (Integer.valueOf(exportStoreDTO.getIsEnable()) == StoreEnum.IS_ENABLE_YES.value()) {
                exportStoreDTO.setIsEnable("启用");
            } else {
                exportStoreDTO.setIsEnable("禁用");
            }

        }


        return page;
    }

    /**
     * 根据店铺ID 查询店铺状态
     *
     * @param storeId
     */

    @Override
    public Integer findStoreStatus(@RequestParam("storeId") Long storeId) {
        return baseDao.findStoreStatus(storeId);
    }

    /**
     * 创建店铺
     *
     * @param createStoreDTO
     * @param type           1 admin 端创建店铺  2 seller端创建店铺
     */

    @Override
    public void createStore(@RequestBody CreateStoreDTO createStoreDTO,
                            @RequestParam(required = false, value = "type") Integer type) {

        StoreEntity storeEntity = ConvertUtils.sourceToTarget(createStoreDTO, StoreEntity.class);
        storeEntity.setId(IdWorker.getId());
        storeEntity.setStoreType(Integer.valueOf(createStoreDTO.getStoreType()));
        if (type != null && type == StoreEnum.ADMIN_TYPE.value()) {
            storeEntity.setRegisterAuditStatus(StoreEnum.AUDIT_STATUS_YES.value());

        } else {
            storeEntity.setRegisterAuditStatus(StoreEnum.AUDIT_STATUS_SUB.value());
            //  默认店铺等级
            setStoreGrade(storeEntity);
        }
        insert(storeEntity);

        // 创建店铺角色关联
        createUserManage(storeEntity.getId(), createStoreDTO.getUserId(), type);

        StoreAuthDTO storeAuthDTO = ConvertUtils.sourceToTarget(createStoreDTO, StoreAuthDTO.class);
        storeAuthDTO.setStoreId(storeEntity.getId());
        storeAuthService.save(storeAuthDTO);

        saveStoreClass(createStoreDTO.getStoreClassId(), storeEntity.getId());

        if (type != null && type == StoreEnum.ADMIN_TYPE.value()) {
            // 更新店铺索引
            rabbitMqSendService.sendMsg(MqConstant.LEIMINGTECH_STORE_INDEX_UPDATE_SINGLE, String.valueOf(storeEntity.getId()));
        }

    }

    /**
     * 保存默认店铺等级
     *
     * @param storeEntity
     */
    private void setStoreGrade(StoreEntity storeEntity) {
        Long gradeId = storeGradeService.selectGradeId();

        storeEntity.setGradeId(gradeId);


    }

    /**
     * 创建店铺角色关联
     *
     * @param storeId 店铺ID
     * @param userId  用户ID
     */
    private void createUserManage(Long storeId, Long userId, Integer type) {

        // seller端创建店铺
        StoreUserManageDTO storeUserManageDTO = storeRoleService.getByUserId(userId);
        storeUserManageDTO.setStoreId(storeId);
        storeUserManageService.update(storeUserManageDTO);
        StoreUserManageDTO storeUserManage = new StoreUserManageDTO();
        storeUserManage.setStoreId(storeId);
        storeUserManage.setRoleId(storeUserManageDTO.getRoleId());
        storeUserManageService.save(storeUserManage);

        if (type != null && type == StoreEnum.ADMIN_TYPE.value()) {
            // admin端创建店铺更新角色信息
            UpdateStoreRoleDTO storeRoleDTO = new UpdateStoreRoleDTO();
            storeRoleDTO.setId(storeUserManageDTO.getRoleId());
            storeRoleDTO.setRoleName("超级管理员");
            storeRoleDTO.setRoleMark(1);
            storeRoleDTO.setRoleRemark("超级管理员，拥有所有权限");
            storeRoleService.update(storeRoleDTO);

        }

    }

    /**
     * 修改店铺信息
     *
     * @param updateRegisterStoreDTO
     */

    @Override
    public void updateStoreInfo(@RequestBody UpdateStoreBasicDTO updateRegisterStoreDTO) {

        StoreEntity storeEntity = ConvertUtils.sourceToTarget(updateRegisterStoreDTO, StoreEntity.class);

        updateById(storeEntity);
    }

    /**
     * 修改店铺入住信息
     *
     * @param updateRegisterStoreDTO
     */

    @Override
    public void updateRegisterStore(@RequestBody UpdateRegisterStoreDTO updateRegisterStoreDTO,
                                    @RequestParam(required = false, value = "type") Integer type) {
        StoreEntity storeEntity = ConvertUtils.sourceToTarget(updateRegisterStoreDTO, StoreEntity.class);
        storeEntity.setRegisterAuditStatus(StoreEnum.AUDIT_STATUS_SUB.value());
        if (type != null && type == StoreEnum.ADMIN_TYPE.value()) {
            storeEntity.setRegisterAuditStatus(StoreEnum.AUDIT_STATUS_YES.value());
            verifyStoreGrade(storeEntity);
            //----------- 同步商品更新数据库
            goodsService.updateStoreName(storeEntity.getStoreName(), storeEntity.getId(), storeEntity.getStoreType());
        }
        updateById(storeEntity);

        storeClassService.deleteByStoreId(storeEntity.getId());
        saveStoreClass(updateRegisterStoreDTO.getStoreClassId(), storeEntity.getId());
        StoreAuthDTO storeAuthDTO = ConvertUtils.sourceToTarget(updateRegisterStoreDTO, StoreAuthDTO.class);
        storeAuthDTO.setId(updateRegisterStoreDTO.getAuthId());
        storeAuthDTO.setStoreId(storeEntity.getId());
        storeAuthService.update(storeAuthDTO);
    }

    /**
     * 校验店铺发布商品数量是否超过店铺等级限制数量
     *
     * @param storeEntity
     */
    private void verifyStoreGrade(StoreEntity storeEntity) {
        //校验店铺等级，如果店铺下的商品数量超过当前等级所限制的数量，编辑失败
        Integer goodsCount = goodsService.findGoodsCount(storeEntity.getId(), null);
        // 查询店铺等级所限制的数量
        StoreGradeDTO storeGradeDTO = storeGradeService.get(storeEntity.getGradeId());
        if (goodsCount != null && goodsCount.intValue() > storeGradeDTO.getSgGoodsLimit()) {
            throw new ServiceException(StoreStatusCode.STORE_GOODS_MORE);
        }
    }

    /**
     * 店铺详情
     *
     * @param userId  用户ID
     * @param storeId 店铺ID
     * @return
     */

    @Override
    public RegisterStoreInfoDTO storeInfoByUserId(@RequestParam(value = "userId", required = false) Long userId,
                                                  @RequestParam(value = "storeId", required = false) Long storeId) {

        RegisterStoreInfoDTO registerStoreInfoDTO = baseDao.storeInfoByUserId(userId, storeId);
        if (registerStoreInfoDTO != null) {
            List<StoreGoodsClassDTO> storeGoodsClassDTOList = storeClassService.selectStoreClass(registerStoreInfoDTO.getStoreId());
            registerStoreInfoDTO.setStoreGoodsClassDTO(storeGoodsClassDTOList);
        }

        return registerStoreInfoDTO;
    }

    /**
     * 修改店铺状态
     *
     * @param updateStoreStatusDTO
     */

    @Override
    public void updateStoreStatus(@RequestBody UpdateStoreStatusDTO updateStoreStatusDTO) {

        if (updateStoreStatusDTO.getAuditType() != null && updateStoreStatusDTO.getAuditType() == 1) {
            storeAuditService.updateStoreStatus(updateStoreStatusDTO);
            // 审核通过，同步信息
            if (updateStoreStatusDTO.getRegisterAuditStatus() == StoreEnum.AUDIT_STATUS_YES.value()) {
                StoreAuditDTO storeAuditDTO = storeAuditService.get(updateStoreStatusDTO.getId());
                StoreEntity storeEntity = ConvertUtils.sourceToTarget(storeAuditDTO, StoreEntity.class);
                storeEntity.setId(storeAuditDTO.getStoreId());
                storeEntity.setCreateDate(null);
                updateById(storeEntity);
                List<StoreClassDTO> auditStoreClass = baseDao.getAuditStoreClass(storeAuditDTO.getId());
                storeClassService.deleteByStoreId(storeEntity.getId());
                storeClassService.save(auditStoreClass);

                //----------- 同步商品更新数据库
                goodsService.updateStoreName(storeEntity.getStoreName(), storeEntity.getId(), storeEntity.getStoreType());
                // 更新店铺索引
                rabbitMqSendService.sendMsg(MqConstant.LEIMINGTECH_STORE_INDEX_UPDATE_SINGLE, String.valueOf(storeEntity.getId()));
                // 同步商品ES
                goodsSyncIndexService.goodsIndexSyncByStoreId(storeEntity.getId());
                //更新购物车
                rabbitMqSendService.sendMsg(MqConstant.LEIMINGTECH_STORE_ID_CART_GOODS_STATUS, String.valueOf(storeEntity.getId()));
            }
        } else if (updateStoreStatusDTO.getAuditType() != null && updateStoreStatusDTO.getAuditType() == StoreEnum.COMPANY_AUDIT.value()) {
            storeAuthAuditService.updateStoreStatus(updateStoreStatusDTO);
            //审核通过同步信息
            if (updateStoreStatusDTO.getRegisterAuditStatus() == StoreEnum.AUDIT_STATUS_YES.value()) {
                StoreAuthAuditDTO storeAuthAuditDTO = storeAuthAuditService.get(updateStoreStatusDTO.getId());
                StoreAuthDTO storeAuthDTO = ConvertUtils.sourceToTarget(storeAuthAuditDTO, StoreAuthDTO.class);
                Long authId = storeAuthService.getStoreAuthId(storeAuthAuditDTO.getStoreId());
                storeAuthDTO.setId(authId);
                storeAuthService.update(storeAuthDTO);
            }
        } else {
            StoreEntity storeEntity = new StoreEntity();
            storeEntity.setId(updateStoreStatusDTO.getStoreId());
            storeEntity.setRegisterAuditStatus(updateStoreStatusDTO.getRegisterAuditStatus());
            storeEntity.setRegisterAuditCause(updateStoreStatusDTO.getRegisterAuditCause());

            if (updateStoreStatusDTO.getRegisterAuditStatus() == StoreEnum.AUDIT_STATUS_YES.value()) {
                // 修改角色信息
                UpdateStoreRoleDTO storeRoleDTO = storeRoleService.getRoleByStoreId(storeEntity.getId());
                storeRoleDTO.setRoleName("超级管理员");
                storeRoleDTO.setRoleMark(1);
                storeRoleDTO.setRoleRemark("超级管理员，拥有所有权限");
                storeRoleService.update(storeRoleDTO);
                Long userId = storeUserManageService.getUserIdByStoreId(updateStoreStatusDTO.getStoreId());
                userDetailRedis.logout(userId);
                // 更新店铺索引
                rabbitMqSendService.sendMsg(MqConstant.LEIMINGTECH_STORE_INDEX_UPDATE_SINGLE, String.valueOf(storeEntity.getId()));
            }
            updateById(storeEntity);
        }
        rabbitMqSendService.sendMsg(MqConstant.LEIMINGTECH_SAVE_SOTRE_AUDIT_LOG_QUEUE, JSON.toJSONString(updateStoreStatusDTO));
    }

    /**
     * 根据店铺id查询详情（入住详情）
     *
     * @param storeId
     * @return
     */

    @Override
    public RegisterStoreInfoDTO adminInfo(@RequestParam("storeId") Long storeId) {
        RegisterStoreInfoDTO registerStoreInfoDTO = baseDao.adminInfo(storeId);
        List<StoreGoodsClassDTO> storeGoodsClassDTOList = storeClassService.selectStoreClass(storeId);
        registerStoreInfoDTO.setStoreGoodsClassDTO(storeGoodsClassDTOList);
        return registerStoreInfoDTO;
    }

    /**
     * admin端创建店铺
     *
     * @param adminCreateStoreDTO
     */

    @Override
    public void adminCreateStore(@RequestBody AdminCreateStoreDTO adminCreateStoreDTO) {

        CreateStoreDTO createStoreDTO = ConvertUtils.sourceToTarget(adminCreateStoreDTO, CreateStoreDTO.class);
        createStoreDTO.setUserId(adminCreateStoreDTO.getUserId());
        createStore(createStoreDTO, StoreEnum.ADMIN_TYPE.value());


    }

    /**
     * 保存店铺分类
     *
     * @param StoreClassId
     * @param storeId
     */
    private void saveStoreClass(Long[] storeClassId, Long storeId) {
        List<StoreClassDTO> list = new ArrayList<>();
        if (storeClassId != null) {
            for (Long classId : storeClassId) {
                StoreClassDTO storeClassDTO = new StoreClassDTO();
                storeClassDTO.setClassId(classId);
                storeClassDTO.setStoreId(storeId);
                list.add(storeClassDTO);
            }
        }

        storeClassService.save(list);
    }

    /**
     * 修改店铺启用禁用状态
     *
     * @param storeId  店铺ID
     * @param isEnable 启用禁用 0 启用， 1 禁用
     */

    @Override
    public void updateStoreEnable(@RequestParam("storeId") Long storeId, @RequestParam("isEnable") Integer isEnable) {
        StoreEntity storeEntity = new StoreEntity();
        storeEntity.setId(storeId);
        storeEntity.setIsEnable(isEnable);
        updateById(storeEntity);
        /**
         * 如果被禁用的话，
         * 下架店铺下所有的商品，
         * 已经下单的不影响，
         * 更新es，
         * 更新购物车
         * 发布商品做限制
         */
        if (isEnable == 1) {
            // 下架所有的商品，规格
            goodsService.updateShowByStoreId(storeId);
            //更新es
            goodsSyncIndexService.goodsIndexSyncByStoreId(storeId);
            //更新购物车
            rabbitMqSendService.sendMsg(MqConstant.LEIMINGTECH_STORE_ID_CART_GOODS_STATUS, String.valueOf(storeId));
        }
        //更新店铺索引
        rabbitMqSendService.sendMsg(MqConstant.LEIMINGTECH_STORE_INDEX_UPDATE_SINGLE, String.valueOf(storeEntity.getId()));

    }

    /**
     * 根据用户id查询店铺列表
     *
     * @param userId
     * @return
     */

    @Override
    public PageData<StoreDTO> storeList(@RequestParam("userId") Long userId) {
        Map<String, Object> params = new HashMap<>(10);
        params.put("userId", userId);
        //分页
        IPage<StoreEntity> page = getPage(params, null, false);

        List<StoreDTO> list = baseDao.storeList(params);
        return new PageData<>(list, page.getTotal());
    }

    /**
     * 查询出所有的店铺Id
     *
     * @return
     */

    @Override
    public List<Long> findStoreAll() {
        return baseDao.findStoreAll();
    }

    /**
     * 店铺首页
     *
     * @param sellerDetail
     * @return
     */

    @Override
    public IndexDTO index(@RequestBody SellerDetail sellerDetail) {
        IndexDTO indexDTO = new IndexDTO();
        // 店铺基本信息
        PageStoreDTO storeDTO = baseDao.storeInfo(sellerDetail.getStoreId(), sellerDetail.getId());
        // 查询店铺首页功能按钮
        Map<String, Object> params = new HashMap<>(10);
        params.put("userId", sellerDetail.getId());
        List<StoreUserFunctionDTO> indexListDTO = storeUserFunctionService.list(params);
        if (CollectionUtils.isEmpty(indexListDTO)) {
            //如果为空的情况下就默认8个权限
            indexListDTO = storeMenuService.defaultMenu(sellerDetail.getRoleId(), sellerDetail.getSuperAdmin(), null);
        }
        indexListDTO.stream().forEach(s -> {
            s.setMenuUrl(s.getMenuUrl().replace("/", "-"));
        });
        indexDTO.setIndexDTO(indexListDTO);
        indexDTO.setStoreDTO(storeDTO);

        return indexDTO;
    }

    /**
     * 首页>基础概况>店铺数据查询
     *
     * @param startDateStr 开始时间字符串
     * @param endDateStr   结束时间字符串
     * @return IndexStoreDataDTO
     * @date 2020/4/7/007 12:03
     * @author xuzhch
     */

    @Override
    public IndexStoreDataDTO indexStoreData(@RequestParam("startDateStr") String startDateStr, @RequestParam("endDateStr") String endDateStr) {
        Date startDate = DateUtils.parse(startDateStr, DateUtils.DATE_TIME_PATTERN);
        Date endDate = DateUtils.parse(endDateStr, DateUtils.DATE_TIME_PATTERN);
        return baseDao.selectIndexStoreData(startDate, endDate);
    }

    /**
     * 查询店铺信息
     *
     * @param storeId 店铺ID
     * @return
     */

    @Override
    public PageStoreDTO findStoreInfoById(@RequestParam("storeId") Long storeId) {
        return baseDao.findStoreInfoById(storeId);
    }
}
