/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.service.sys.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.leimingtech.commons.mybatis.service.impl.BaseServiceImpl;
import com.leimingtech.commons.tools.constant.Constant;
import com.leimingtech.commons.tools.page.PageData;
import com.leimingtech.commons.tools.utils.ConvertUtils;
import com.leimingtech.dao.SysLogOperationDao;
import com.leimingtech.dto.SysLogOperationDTO;
import com.leimingtech.entity.SysLogOperationEntity;
import com.leimingtech.service.sys.SysLogOperationService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * 操作日志
 *
 * @since 1.0.0
 */
@Service

public class SysLogOperationServiceImpl extends BaseServiceImpl<SysLogOperationDao, SysLogOperationEntity> implements SysLogOperationService {
    /**
     * 分页
     *
     * @param params
     * @return
     */

    @Override
    public PageData<SysLogOperationDTO> page(@RequestParam Map<String, Object> params) {
        IPage<SysLogOperationEntity> page = baseDao.selectPage(
                getPage(params, Constant.CREATE_DATE, false),
                getWrapper(params)
        );

        return getPageData(page, SysLogOperationDTO.class);
    }

    /**
     * 导出日志信息
     *
     * @param params
     * @return
     */

    @Override
    public List<SysLogOperationDTO> list(@RequestParam Map<String, Object> params) {
        List<SysLogOperationEntity> entityList = baseDao.selectList(getWrapper(params));
        return Optional.of(ConvertUtils.sourceToTarget(entityList, SysLogOperationDTO.class)).orElse(new ArrayList<>());
    }

    private QueryWrapper<SysLogOperationEntity> getWrapper(Map<String, Object> params) {
        String module = (String) params.get("module");
        String status = (String) params.get("status");

        QueryWrapper<SysLogOperationEntity> wrapper = new QueryWrapper<>();
        wrapper.like(StringUtils.isNotBlank(module), "module", module);
        wrapper.eq(StringUtils.isNotBlank(status), "status", status);

        return wrapper;
    }

    @Transactional(rollbackFor = Exception.class)
    public void save(SysLogOperationEntity entity) {
        insert(entity);
    }

}
