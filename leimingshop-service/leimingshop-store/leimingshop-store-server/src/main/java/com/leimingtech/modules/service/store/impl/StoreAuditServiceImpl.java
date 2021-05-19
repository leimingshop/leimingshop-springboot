/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.service.store.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.leimingtech.commons.mybatis.service.impl.BaseServiceImpl;
import com.leimingtech.commons.tools.page.PageData;
import com.leimingtech.commons.tools.utils.ConvertUtils;
import com.leimingtech.modules.dao.store.StoreAuditDao;
import com.leimingtech.modules.dto.goodsclass.GoodsClassQueryNameDTO;
import com.leimingtech.modules.dto.store.*;
import com.leimingtech.modules.dto.storeclass.StoreClassDTO;
import com.leimingtech.modules.dto.storeclass.StoreGoodsClassDTO;
import com.leimingtech.modules.entity.store.StoreAuditEntity;
import com.leimingtech.modules.enums.store.StoreEnum;
import com.leimingtech.modules.service.goodsclass.GoodsClassService;
import com.leimingtech.modules.service.store.StoreAuditService;
import com.leimingtech.modules.service.store.StoreService;
import com.leimingtech.modules.service.storeclass.StoreClassService;
import com.leimingtech.mq.constant.MqConstant;
import com.leimingtech.mq.service.RabbitMqSendService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * 店铺审核表
 *
 * @author chengqian chengqian@leimingtech.com
 * @since v1.0.0 2019-12-20
 */
@Service
@Transactional(rollbackFor = Exception.class)

public class StoreAuditServiceImpl extends BaseServiceImpl<StoreAuditDao, StoreAuditEntity> implements StoreAuditService {

    @Autowired
    private StoreService storeService;

    @Autowired
    private StoreClassService storeClassService;

    @Autowired
    private GoodsClassService goodsClassService;

    @Autowired
    private RabbitMqSendService rabbitMqSendService;

    /**
     * 分页信息
     *
     * @param params
     * @return
     */
    @Override

    public PageData<StoreAuditPageDTO> page(@RequestParam Map<String, Object> params) {
        //分页
        IPage<StoreAuditEntity> page = getPage(params, null, false);
        //查询
        String startTime = (String) params.get("startTime");
        String endTime = (String) params.get("endTime");
        if (StringUtils.isNotBlank(startTime) && StringUtils.isNotBlank(endTime)) {
            startTime = startTime + " 00:00:00";
            endTime = endTime + " 23:59:59";
            params.put("startTime", startTime);
            params.put("endTime", endTime);
        }
        List<StoreAuditPageDTO> list = baseDao.auditPage(params);
        return new PageData<StoreAuditPageDTO>(list, page.getTotal());
    }

    /**
     * 查询所有，不分页
     *
     * @param params
     * @return
     */
    @Override

    public List<StoreAuditDTO> list(Map<String, Object> params) {
        List<StoreAuditEntity> entityList = baseDao.selectList(getWrapper(params));

        return ConvertUtils.sourceToTarget(entityList, StoreAuditDTO.class);
    }

    private QueryWrapper<StoreAuditEntity> getWrapper(Map<String, Object> params) {
        String id = (String) params.get("id");

        QueryWrapper<StoreAuditEntity> wrapper = new QueryWrapper<>();
        wrapper.eq(StringUtils.isNotBlank(id), "id", id);

        return wrapper;
    }

    /**
     * 根据id获取信息
     *
     * @param id
     * @return
     */
    @Override

    public StoreAuditDTO get(Long id) {
        StoreAuditEntity entity = baseDao.selectById(id);
        List<StoreGoodsClassDTO> auditStoreClass = getAuditStoreClass(id);
        StoreAuditDTO storeAuditDTO = ConvertUtils.sourceToTarget(entity, StoreAuditDTO.class);
        storeAuditDTO.setStoreGoodsClassDTO(auditStoreClass);
        return storeAuditDTO;
    }

    /**
     * 保存修改记录
     *
     * @param dto
     */
    @Override
    @Transactional(rollbackFor = Exception.class)

    public void save(@RequestBody UpdateStoreBasicDTO dto) {
        StoreAuditEntity entity = ConvertUtils.sourceToTarget(dto, StoreAuditEntity.class);
        entity.setInfoAuditStatus(StoreEnum.AUDIT_STATUS.value());
        insert(entity);
        saveClass(dto.getStoreClassId(), entity.getStoreId(), entity.getId());

        UpdateStoreStatusDTO updateStoreStatusDTO = new UpdateStoreStatusDTO();
        updateStoreStatusDTO.setId(entity.getId());
        updateStoreStatusDTO.setOperator(entity.getCreator());
        updateStoreStatusDTO.setAuditType(1);
        updateStoreStatusDTO.setRegisterAuditStatus(StoreEnum.AUDIT_STATUS.value());
        rabbitMqSendService.sendMsg(MqConstant.LEIMINGTECH_SAVE_SOTRE_AUDIT_LOG_QUEUE, JSON.toJSONString(updateStoreStatusDTO));
    }

    /**
     * 修改记录
     *
     * @param dto
     */
    @Override
    @Transactional(rollbackFor = Exception.class)

    public void update(@RequestBody UpdateStoreBasicDTO dto) {
        StoreAuditEntity entity = ConvertUtils.sourceToTarget(dto, StoreAuditEntity.class);
        entity.setInfoAuditStatus(StoreEnum.AUDIT_STATUS.value());
        updateById(entity);
        baseDao.deleteByStoreId(entity.getStoreId());
        saveClass(dto.getStoreClassId(), entity.getStoreId(), entity.getId());


    }

    /**
     * 删除
     *
     * @param ids
     */
    @Override
    @Transactional(rollbackFor = Exception.class)

    public void delete(@RequestBody Long[] ids) {
        //逻辑删除
        //logicDelete(ids, StoreAuditEntity.class);

        //物理删除
        baseDao.deleteBatchIds(Arrays.asList(ids));
    }

    /**
     * 店铺基础信息回显
     *
     * @param storeId
     * @return
     */

    @Override
    public StoreAuditDTO getInfoByStoreId(@RequestParam("storeId") Long storeId, @RequestParam(value = "createDate", required = false) String createDate) {
        StoreAuditDTO infoByStoreId = baseDao.getInfoByStoreId(storeId, createDate);
        if (infoByStoreId == null) {
            StoreDTO storeDTO = storeService.get(storeId);
            infoByStoreId = ConvertUtils.sourceToTarget(storeDTO, StoreAuditDTO.class);
            infoByStoreId.setStoreId(storeId);
            infoByStoreId.setId(null);
            infoByStoreId.setInfoAuditStatus(StoreEnum.AUDIT_STATUS_YES.value());
            infoByStoreId.setInfoAuditCause(storeDTO.getRegisterAuditCause());
            List<StoreGoodsClassDTO> storeGoodsClassDTOList = storeClassService.selectStoreClass(storeId);
            infoByStoreId.setStoreGoodsClassDTO(storeGoodsClassDTOList);
            return infoByStoreId;
        }
        List<StoreGoodsClassDTO> auditStoreClass = getAuditStoreClass(infoByStoreId.getId());
        infoByStoreId.setStoreGoodsClassDTO(auditStoreClass);

        return infoByStoreId;
    }

    /**
     * 修改店铺普通信息审核状态 审核通过，同步信息到店铺入住表
     *
     * @param updateStoreStatusDTO
     */

    @Override
    public void updateStoreStatus(@RequestBody UpdateStoreStatusDTO updateStoreStatusDTO) {
        baseDao.updateStoreStatus(updateStoreStatusDTO);
    }

    /**
     * 获取待审核店铺分类
     *
     * @param id 审核记录ID
     * @return
     */

    @Override
    public List<StoreGoodsClassDTO> getAuditStoreClass(@RequestParam("storeId") Long id) {
        List<Long> classId = baseDao.getStoreClass(id);
        List<GoodsClassQueryNameDTO> goodsClassQueryNameDTOList = goodsClassService.selectListByClassId(classId);
        return ConvertUtils.sourceToTarget(goodsClassQueryNameDTOList, StoreGoodsClassDTO.class);
    }


    public void saveClass(Long[] classIds, Long storeId, Long id) {

        List<StoreClassDTO> list = new ArrayList();
        for (Long classId : classIds) {
            StoreClassDTO storeClassDTO = new StoreClassDTO();
            storeClassDTO.setId(id);
            storeClassDTO.setStoreId(storeId);
            storeClassDTO.setClassId(classId);
            list.add(storeClassDTO);
        }
        baseDao.saveStoreClass(list);
    }
}
