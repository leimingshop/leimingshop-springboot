/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.service.auditlog.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.leimingtech.commons.mybatis.service.impl.BaseServiceImpl;
import com.leimingtech.commons.tools.constant.Constant;
import com.leimingtech.commons.tools.page.PageData;
import com.leimingtech.commons.tools.utils.ConvertUtils;
import com.leimingtech.modules.dao.auditlog.AuditLogDao;
import com.leimingtech.modules.dto.auditlog.AuditLogDTO;
import com.leimingtech.modules.dto.store.UpdateStoreStatusDTO;
import com.leimingtech.modules.entity.auditlog.AuditLogEntity;
import com.leimingtech.modules.enums.store.StoreEnum;
import com.leimingtech.modules.service.auditlog.AuditLogService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * 审核记录表
 *
 * @author chengqian chengqian@leimingtech.com
 * @since v1.0.0 2019-12-23
 */
@Service
@Transactional(rollbackFor = Exception.class)

public class AuditLogServiceImpl extends BaseServiceImpl<AuditLogDao, AuditLogEntity> implements AuditLogService {
    /**
     * 分页
     *
     * @param params
     * @return
     */
    @Override

    public PageData<AuditLogDTO> page(@RequestParam Map<String, Object> params) {
        IPage<AuditLogEntity> page = baseDao.selectPage(
                getPage(params, Constant.UPDATE_DATE, false),
                getWrapper(params)
        );

        return getPageData(page, AuditLogDTO.class);
    }

    /**
     * 查询所有
     *
     * @param params
     * @return
     */
    @Override

    public List<AuditLogDTO> list(@RequestParam Map<String, Object> params) {
        List<AuditLogEntity> entityList = baseDao.selectList(getWrapper(params));

        return ConvertUtils.sourceToTarget(entityList, AuditLogDTO.class);
    }

    /**
     * 条件构造器
     *
     * @param params
     * @return
     */
    private QueryWrapper<AuditLogEntity> getWrapper(Map<String, Object> params) {
        String contentId = (String) params.get("contentId");
        String auditType = (String) params.get("auditType");

        QueryWrapper<AuditLogEntity> wrapper = new QueryWrapper<>();
        wrapper.eq(StringUtils.isNotBlank(contentId), "content_id", contentId);
        wrapper.eq(StringUtils.isNotBlank(auditType), "audit_type", auditType);

        return wrapper;
    }

    /**
     * 根据ID 获取信息
     *
     * @param id
     * @return
     */
    @Override

    public AuditLogDTO get(Long id) {
        AuditLogEntity entity = baseDao.selectById(id);

        return ConvertUtils.sourceToTarget(entity, AuditLogDTO.class);
    }

    /**
     * 保存
     *
     * @param dto
     */
    @Override
    @Transactional(rollbackFor = Exception.class)

    public void save(@RequestBody AuditLogDTO dto) {
        AuditLogEntity entity = ConvertUtils.sourceToTarget(dto, AuditLogEntity.class);

        insert(entity);
    }

    /**
     * 更新
     *
     * @param dto
     */
    @Override
    @Transactional(rollbackFor = Exception.class)

    public void update(@RequestBody AuditLogDTO dto) {
        AuditLogEntity entity = ConvertUtils.sourceToTarget(dto, AuditLogEntity.class);

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
        //logicDelete(ids, AuditLogEntity.class);

        //物理删除
        baseDao.deleteBatchIds(Arrays.asList(ids));
    }

    /**
     * 保存审核记录
     *
     * @param updateStoreStatusDTO
     */

    @Override
    public void saveLog(@RequestBody UpdateStoreStatusDTO updateStoreStatusDTO) {
        AuditLogDTO auditLogDTO = new AuditLogDTO();

        if (updateStoreStatusDTO.getId() == null) {
            auditLogDTO.setContentId(updateStoreStatusDTO.getStoreId());
        } else {
            auditLogDTO.setContentId(updateStoreStatusDTO.getId());
        }

        if (updateStoreStatusDTO.getRegisterAuditStatus() == StoreEnum.AUDIT_STATUS.value()) {
            auditLogDTO.setSubmitType(1);
        } else {
            auditLogDTO.setSubmitType(2);
        }

        auditLogDTO.setAuditStatus(updateStoreStatusDTO.getRegisterAuditStatus());
        auditLogDTO.setRemark(updateStoreStatusDTO.getRegisterAuditCause());
        auditLogDTO.setAuditType(updateStoreStatusDTO.getAuditType());
        auditLogDTO.setOperator(updateStoreStatusDTO.getOperator());
        insert(ConvertUtils.sourceToTarget(auditLogDTO, AuditLogEntity.class));

    }

}