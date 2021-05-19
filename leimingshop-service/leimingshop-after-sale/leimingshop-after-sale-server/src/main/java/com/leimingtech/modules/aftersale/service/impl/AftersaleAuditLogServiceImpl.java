/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.aftersale.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.leimingtech.commons.mybatis.service.impl.CrudServiceImpl;
import com.leimingtech.commons.tools.page.PageData;
import com.leimingtech.commons.tools.utils.ConvertUtils;
import com.leimingtech.modules.aftersale.dao.AftersaleAuditLogDao;
import com.leimingtech.modules.aftersale.dto.*;
import com.leimingtech.modules.aftersale.entity.AftersaleAuditLogEntity;
import com.leimingtech.modules.aftersale.service.AftersaleApplyService;
import com.leimingtech.modules.aftersale.service.AftersaleAuditLogService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.beans.BeanCopier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * 售后审核记录
 *
 * @author huangkeyuan
 * @email huangkeyuan@leimingtech.dao
 * @since 1.0.0 2019-06-13
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class AftersaleAuditLogServiceImpl extends CrudServiceImpl<AftersaleAuditLogDao, AftersaleAuditLogEntity, AftersaleAuditLogDTO> implements AftersaleAuditLogService {

    @Resource
    private AftersaleAuditLogDao aftersaleAuditLogDao;
    @Autowired
    private AftersaleApplyService aftersaleApplyService;

    @Override
    public QueryWrapper<AftersaleAuditLogEntity> getWrapper(Map<String, Object> params) {
        String id = params.get("id").toString();

        QueryWrapper<AftersaleAuditLogEntity> wrapper = new QueryWrapper<>();
        wrapper.eq(StringUtils.isNotBlank(id), "aftersale_sn", id);

        return wrapper;
    }

    /**
     * 保存售后审核
     *
     * @param dto
     */
    @Override

    public void save(@RequestBody AftersaleAuditLogSaveDTO dto) {
        AftersaleAuditLogEntity reasonDescriptionEntity = ConvertUtils.sourceToTarget(dto, AftersaleAuditLogEntity.class);
        aftersaleAuditLogDao.insert(reasonDescriptionEntity);
    }

    /**
     * 修改售后审核
     *
     * @param dto
     */
    @Override

    public void update(@RequestBody AftersaleAuditLogDTO dto) {
        super.update(dto);
    }

    /**
     * 删除售后审核
     *
     * @param ids
     */
    @Override

    public void delete(@RequestBody Long[] ids) {
        super.delete(ids);
    }

    /**
     * 根据ID查询售后审核
     *
     * @param id
     * @return
     */
    @Override

    public AftersaleAuditLogDTO get(Long id) {
        return super.get(id);
    }

    /**
     * @return java.util.List<dao.leimingtech.dto.reason.AftersaleAuditLogDTO>
     * @Description 查询所有的售后审核列表
     * @Param * @param params:
     * @Author huangkeyuan
     * @Date 18:13 2019-06-10
     */
    @Override

    public List<AftersaleAuditLogDTO> list(@RequestParam Map<String, Object> params) {
        return super.list(params);
    }

    /**
     * @return java.util.List<dao.leimingtech.dto.reason.AftersaleLogDTO>
     * @Description 查询所有的售后日志列表
     * @Param * @param params:
     * @Author huangkeyuan
     * @Date 18:13 2019-06-10
     */
    @Override

    public List<AftersaleLogListDTO> listLog(Long aftersaleSn) {
        return aftersaleAuditLogDao.findLog(aftersaleSn);
    }

    /**
     * @return dao.leimingtech.commons.tools.page.PageData<dao.leimingtech.dto.reason.AftersaleAuditLogDTO>
     * @Description 分页查询所有的售后审核记录列表
     * @Param * @param params:
     * @Author huangkeyuan
     * @Date 13:40 2019-06-11
     */
    @Override

    public PageData<AftersaleAuditLogPageDTO> pageData(@RequestParam Map<String, Object> params, @RequestParam(value = "storeId", required = false) Long storeId) {
        if (storeId != null) {
            params.put("storeId", storeId);
        }

        // 分页
        IPage<AftersaleAuditLogEntity> page = getPage(params, "laal.create_date", false);
        // 查询
        List<AftersaleAuditLogPageDTO> list = aftersaleAuditLogDao.findPage(params, page);
        return new PageData<>(list, page.getTotal());
    }

    /**
     * @Author weixianchun
     * @Description 导出审核列表查询
     * @Param params
     * @Date 2019/12/4 11:39
     * @Return java.util.List<com.leimingtech.modules.aftersale.dto.AftersaleAuditLogPageDTO>
     * @version 1.0
     */

    @Override
    public List<AftersaleAuditLogPageDTO> findListExport(@RequestParam Map<String, Object> params) {
        return baseDao.findPage(params, null);
    }

    @Override

    public AftersaleAuditDetailDTO detail(Long id) {
        AftersaleAuditDetailDTO aftersaleAuditDetailDTO = new AftersaleAuditDetailDTO();
        AftersaleAuditLogDTO aftersaleAuditLogDTO = this.get(id);
        if (null == aftersaleAuditLogDTO) {
            return null;
        }
        AftersaleApplyDetailDTO detail = aftersaleApplyService.detail(aftersaleAuditLogDTO.getAftersaleSn());
        if (null == detail) {
            return null;
        }
        BeanCopier.create(AftersaleApplyDetailDTO.class, AftersaleAuditDetailDTO.class, false).copy(detail, aftersaleAuditDetailDTO, null);
        aftersaleAuditDetailDTO.setId(id);
        return aftersaleAuditDetailDTO;
    }

    @Override

    public void cancelAuditing(@RequestParam("aftersaleSn") Long aftersaleSn) {
        baseDao.updateCancel(aftersaleSn);
    }

}
