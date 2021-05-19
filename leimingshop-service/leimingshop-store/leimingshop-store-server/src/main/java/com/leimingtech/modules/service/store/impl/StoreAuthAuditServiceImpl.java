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
import com.leimingtech.modules.dao.store.StoreAuthAuditDao;
import com.leimingtech.modules.dto.auth.StoreAuthDTO;
import com.leimingtech.modules.dto.store.StoreAuthAuditDTO;
import com.leimingtech.modules.dto.store.StoreAuthAuditPageDTO;
import com.leimingtech.modules.dto.store.UpdateStoreStatusDTO;
import com.leimingtech.modules.entity.store.StoreAuthAuditEntity;
import com.leimingtech.modules.enums.store.StoreEnum;
import com.leimingtech.modules.service.auth.StoreAuthService;
import com.leimingtech.modules.service.store.StoreAuthAuditService;
import com.leimingtech.mq.constant.MqConstant;
import com.leimingtech.mq.service.RabbitMqSendService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

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

public class StoreAuthAuditServiceImpl extends BaseServiceImpl<StoreAuthAuditDao, StoreAuthAuditEntity> implements StoreAuthAuditService {

    @Autowired
    private StoreAuthService storeAuthService;

    @Autowired
    private RabbitMqSendService rabbitMqSendService;

    /**
     * 分页信息
     *
     * @param params
     * @return
     */
    @Override

    public PageData<StoreAuthAuditPageDTO> page(@RequestParam Map<String, Object> params) {
        //分页
        IPage<StoreAuthAuditEntity> page = getPage(params, null, false);
        //查询
        String startTime = (String) params.get("startTime");
        String endTime = (String) params.get("endTime");
        if (StringUtils.isNotBlank(startTime) && StringUtils.isNotBlank(endTime)) {
            startTime = startTime + " 00:00:00";
            endTime = endTime + " 23:59:59";
            params.put("startTime", startTime);
            params.put("endTime", endTime);
        }
        List<StoreAuthAuditPageDTO> list = baseDao.authAuditPage(params);
        return new PageData<StoreAuthAuditPageDTO>(list, page.getTotal());
    }

    /**
     * 查询所有
     *
     * @param params
     * @return
     */
    @Override

    public List<StoreAuthAuditDTO> list(Map<String, Object> params) {
        List<StoreAuthAuditEntity> entityList = baseDao.selectList(getWrapper(params));

        return ConvertUtils.sourceToTarget(entityList, StoreAuthAuditDTO.class);
    }

    private QueryWrapper<StoreAuthAuditEntity> getWrapper(Map<String, Object> params) {
        String id = (String) params.get("id");

        QueryWrapper<StoreAuthAuditEntity> wrapper = new QueryWrapper<>();
        wrapper.eq(StringUtils.isNotBlank(id), "id", id);

        return wrapper;
    }

    /**
     * 根据ID获取信息
     *
     * @param id
     * @return
     */
    @Override

    public StoreAuthAuditDTO get(Long id) {
        StoreAuthAuditEntity entity = baseDao.selectById(id);

        return ConvertUtils.sourceToTarget(entity, StoreAuthAuditDTO.class);
    }

    /**
     * 保存信息
     *
     * @param dto
     */
    @Override
    @Transactional(rollbackFor = Exception.class)

    public void save(@RequestBody StoreAuthAuditDTO dto) {
        StoreAuthAuditEntity entity = ConvertUtils.sourceToTarget(dto, StoreAuthAuditEntity.class);

        insert(entity);
        UpdateStoreStatusDTO updateStoreStatusDTO = new UpdateStoreStatusDTO();
        updateStoreStatusDTO.setId(entity.getId());
        updateStoreStatusDTO.setOperator(entity.getCreator());
        updateStoreStatusDTO.setAuditType(2);
        updateStoreStatusDTO.setRegisterAuditStatus(StoreEnum.AUDIT_STATUS.value());
        rabbitMqSendService.sendMsg(MqConstant.LEIMINGTECH_SAVE_SOTRE_AUDIT_LOG_QUEUE, JSON.toJSONString(updateStoreStatusDTO));
    }

    /**
     * 修改信息
     *
     * @param dto
     */
    @Override
    @Transactional(rollbackFor = Exception.class)

    public void update(@RequestBody StoreAuthAuditDTO dto) {
        StoreAuthAuditEntity entity = ConvertUtils.sourceToTarget(dto, StoreAuthAuditEntity.class);

        updateById(entity);
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
        //logicDelete(ids, StoreAuthAuditEntity.class);

        //物理删除
        baseDao.deleteBatchIds(Arrays.asList(ids));
    }

    /**
     * 店铺公司信息回显
     *
     * @param storeId
     * @return
     */

    @Override
    public StoreAuthAuditDTO getInfoByStoreId(@RequestParam("storeId") Long storeId,
                                              @RequestParam(value = "createDate", required = false) String createDate) {
        StoreAuthAuditDTO infoByStoreId = baseDao.getInfoByStoreId(storeId, createDate);
        if (infoByStoreId == null) {
            StoreAuthDTO storeAuthDTO = storeAuthService.findByStoreId(storeId);
            infoByStoreId = ConvertUtils.sourceToTarget(storeAuthDTO, StoreAuthAuditDTO.class);
            infoByStoreId.setStoreId(storeId);
            infoByStoreId.setId(null);
            infoByStoreId.setAuthAuditCause(storeAuthDTO.getAuthAuditCause());
            infoByStoreId.setAuthAuditStatus(StoreEnum.AUDIT_STATUS_YES.value());
        }
        return infoByStoreId;
    }

    /**
     * 更新店铺公司审核状态
     *
     * @param updateStoreStatusDTO
     */

    @Override
    public void updateStoreStatus(@RequestBody UpdateStoreStatusDTO updateStoreStatusDTO) {
        baseDao.updateStoreStatus(updateStoreStatusDTO);
    }

}