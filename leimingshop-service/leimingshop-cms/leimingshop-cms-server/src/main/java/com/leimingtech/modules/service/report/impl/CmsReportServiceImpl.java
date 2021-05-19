/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.service.report.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.leimingtech.commons.mybatis.service.impl.BaseServiceImpl;
import com.leimingtech.commons.tools.constant.Constant;
import com.leimingtech.commons.tools.page.PageData;
import com.leimingtech.commons.tools.security.user.SecurityUser;
import com.leimingtech.commons.tools.utils.ConvertUtils;
import com.leimingtech.modules.dao.report.CmsReportDao;
import com.leimingtech.modules.dto.report.CmsArticleReportAdminInfoDTO;
import com.leimingtech.modules.dto.report.CmsReportDTO;
import com.leimingtech.modules.dto.report.CmsReportPageDto;
import com.leimingtech.modules.entity.report.CmsReportEntity;
import com.leimingtech.modules.service.report.CmsReportService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 投诉管理
 *
 * @author hecheng 347861695@qq.com
 * @since v1.0.0 2020-04-02
 */
@Service
@Transactional(rollbackFor = Exception.class)

public class CmsReportServiceImpl extends BaseServiceImpl<CmsReportDao, CmsReportEntity> implements CmsReportService {

    @Override

    public PageData<CmsReportPageDto> page(@RequestParam Map<String, Object> params) {
        int pageNum = Integer.valueOf(params.get(Constant.PAGE).toString());
        int limit = Integer.valueOf(params.get(Constant.LIMIT).toString());
        Page<CmsReportPageDto> page = new Page<>(pageNum, limit);
        List<CmsReportPageDto> list = baseDao.selectReportPage(page, params);
        return new PageData(list, page.getTotal());
    }

    @Override

    public List<CmsReportDTO> list(Map<String, Object> params) {
        List<CmsReportEntity> entityList = baseDao.selectList(getWrapper(params));

        return ConvertUtils.sourceToTarget(entityList, CmsReportDTO.class);
    }

    private QueryWrapper<CmsReportEntity> getWrapper(Map<String, Object> params) {
        String id = (String) params.get("id");

        QueryWrapper<CmsReportEntity> wrapper = new QueryWrapper<>();
        wrapper.eq(StringUtils.isNotBlank(id), "id", id);

        return wrapper;
    }

    @Override

    public CmsReportDTO get(Long id) {
        CmsReportEntity entity = baseDao.selectById(id);

        return ConvertUtils.sourceToTarget(entity, CmsReportDTO.class);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)

    public void save(@RequestBody CmsReportDTO dto) {
        Date date = new Date();
        CmsReportEntity entity = ConvertUtils.sourceToTarget(dto, CmsReportEntity.class);
        entity.setCreateDate(date);
        entity.setUpdateDate(date);
        insert(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)

    public void update(@RequestBody CmsReportDTO dto) {
        CmsReportEntity entity = ConvertUtils.sourceToTarget(dto, CmsReportEntity.class);
        entity.setUpdateDate(new Date());
        updateById(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)

    public void deleteList(@RequestBody Long[] ids) {
        //逻辑删除
        //logicDelete(ids, CmsReportEntity.class);
        for (Long id : ids) {
            baseDao.updateDelById(id);
        }
        //物理删除
        //baseDao.deleteBatchIds(Arrays.asList(ids));
    }

    @Override

    public void statusUpdate(@RequestParam Map<String, Object> params) {
        CmsReportEntity entity = new CmsReportEntity();
        entity.setId(Long.valueOf(params.get("id").toString()));
        entity.setReportResult(params.get("reportResult").toString());
        entity.setReportStatus(Integer.valueOf(params.get("reportStatus").toString()));
        entity.setUpdater(SecurityUser.getUserName());
        entity.setUpdateDate(new Date());
        baseDao.updateById(entity);
    }

    @Override

    public CmsArticleReportAdminInfoDTO getArticleReportAdmin(Long id) {
        return baseDao.selectArticleReportInfo(id);
    }

    @Override
    public List<CmsReportDTO> selectMemberAndFlag(@RequestParam Map<String, Object> params) {
        return baseDao.selectMemberAndFlag(params);
    }
}